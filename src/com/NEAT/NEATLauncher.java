package com.NEAT;

import com.BattleshipAI.ComputerBattleshipPlayer;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;

class NEATLauncher extends JFrame implements ActionListener, KeyListener {
	private final JTextArea NEATConsole;
	private final JButton button;
	private final JRadioButton program1;
	private final JRadioButton program2;
	private final JComboBox<String> runningPaths;
	private static File startupPaths;

	private final String DEFAULT_FILE = ("DefaultFile.txt");

	private NEATAI AI;
	private static Thread t1;
	Rectangle screenCapture;
	private boolean selectingKeys;
	private ArrayList<Character> keys;

	private NEATLauncher() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("NEAT");
		setResizable(false);
		int WINDOW_WIDTH = 450;
		int WINDOW_HEIGHT = WINDOW_WIDTH + 200;
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLayout(new FlowLayout());
		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		int SCREEN_WIDTH = environment.getMaximumWindowBounds().width;
		int SCREEN_HEIGHT = environment.getMaximumWindowBounds().height;
		setLocation((SCREEN_WIDTH - WINDOW_WIDTH) / 2, (SCREEN_HEIGHT - WINDOW_HEIGHT) / 2);
		JPanel p2 = new JPanel(new BorderLayout());
		JPanel p3 = new JPanel(new BorderLayout());
		JPanel p4 = new JPanel();
		p2.setPreferredSize(new Dimension(WINDOW_WIDTH - 30, 50));
		p3.setPreferredSize(new Dimension(WINDOW_WIDTH - 30, 50));
		p2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Program to use as input: "));
		p3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Select or type file name & extention or path to save data: "));
		p4.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		NEATConsole = new JTextArea("Information regarding all runtime process' will display here. To begin, enter the appropriate NEATConsole below, then press 'Run'.\n");
		NEATConsole.setWrapStyleWord(true);
		NEATConsole.setLineWrap(true);
		NEATConsole.setEditable(false);
		NEATConsole.setMinimumSize(new Dimension(WINDOW_WIDTH - 30, WINDOW_WIDTH));
		NEATConsole.setFont(new Font("Source Code Pro", Font.PLAIN, 12));
		NEATConsole.setSelectedTextColor(Color.GRAY);
		NEATConsole.setFocusable(true);
		NEATConsole.addKeyListener(this);
		JScrollPane consoleScroller = new JScrollPane(NEATConsole);
		consoleScroller.setPreferredSize(new Dimension(NEATConsole.getMinimumSize().width, NEATConsole.getMinimumSize().height));
		consoleScroller.setWheelScrollingEnabled(true);


		program1 = new JRadioButton("BattleshipAI");
		program2 = new JRadioButton("Screen Capture");
		program1.addActionListener(this);
		program2.addActionListener(this);
		p2.add(program1, BorderLayout.WEST);
		p2.add(program2, BorderLayout.EAST);

		runningPaths = new JComboBox<>();
		runningPaths.setPreferredSize(new Dimension(p3.getPreferredSize().width, 20));
		runningPaths.setEditable(true);
		runningPaths.getEditor().getEditorComponent().addKeyListener(this);
		runningPaths.setFocusable(true);
		startupPaths = new File("SavedPaths.txt");
		populateFilePaths(runningPaths);
		p3.add(runningPaths, BorderLayout.CENTER);

		button = new JButton("Run");
		button.addActionListener(this);
		p4.add(button);

		add(consoleScroller);
		add(p2);
		add(p3);
		add(p4);
		setVisible(true);

		keys = new ArrayList<Character>();
		keys.add(' ');
	}

	private void populateFilePaths(JComboBox<String> listToPop) {
		BufferedReader iStream = null;
		PrintWriter oStream = null;
		ArrayList<String> paths = null;
		//Parses list of paths from file into an arraylist
		try {
			iStream = new BufferedReader(new FileReader(startupPaths));
			paths = new ArrayList<>();
			String path;
			while ((path = iStream.readLine()) != null)
				paths.add(path);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (iStream != null)
				try {
					iStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		//Empties (By deleting) the file to prepare for re-insertion of in-order paths that exist
		startupPaths.delete();
		try {
			startupPaths.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Removes any files that were parsed but do not exist
		File mostRecent, tempFile;
		for (int i = 0; i < paths.size(); i++) {
			String quickPath = paths.get(i).trim();
			if (quickPath.equals(""))
				quickPath = DEFAULT_FILE;
			tempFile = NEATAI.createNEATSaveFile(quickPath);
			if (tempFile == null) {
				updateConsole("The Following path was removed: \n" + paths.get(i));
				paths.remove(i);
			}
		}
		//Populates the JComboBox while removing elements from the list
		while (!paths.isEmpty()) {
			mostRecent = new File(paths.get(0));
			int indexToRemove = 0;
			for (int i = 0; i < paths.size(); i++) {
				tempFile = new File(paths.get(i));
				if (tempFile.lastModified() < mostRecent.lastModified()) {
					mostRecent = tempFile;
					indexToRemove = i;
				}
			}
			listToPop.addItem(mostRecent.getAbsolutePath());
			try {
				oStream = new PrintWriter(startupPaths);
				oStream.append(mostRecent.getPath() + "\n");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (oStream != null)
					oStream.close();
			}

			paths.remove(indexToRemove);
		}
	}

	public void actionPerformed(ActionEvent e) {
		JComponent source = (JComponent) e.getSource();
		File testFile;
		if (source == program1)
			program2.setSelected(false);
		if (source == program2)
			program1.setSelected(false);
		if (source == button) {
			if (button.getText().equals("Run")) {
				if (!program1.isSelected() && !program2.isSelected()) {
					updateConsole("Please Select the type of input below.");
					return;
				}
				String quickPath = ((String) runningPaths.getSelectedItem()).trim();
				if (quickPath.equals(""))
					quickPath = DEFAULT_FILE;
				testFile = NEATAI.createNEATSaveFile(quickPath);
				if (testFile == null) {
					updateConsole("Please enter or select a valid NEAT save file");
				} else if (program1.isSelected()) {
					//If the user wants to use the built in battleship game
//					updateConsole("File path has been confirmed.\nApplication Started using '" + program1.getText() + "' as input.");
//					button.setText("Stop Program");
//					StartNEAT();
					updateConsole("This option is currently unsupported.");
				} else if (program2.isSelected()) {
					updateConsole("File path has been confirmed.\nApplication Started using '" + program2.getText() + "' as input.");
					NEATConsole.requestFocusInWindow();
					t1.start();
					button.setText("Quit");
				}
			} else {
				updateConsole("Saving and Stopping Program Now!");
//				NEATAI.save = true;
//				while (!NEATAI.finished) ;
				System.exit(0);
			}
		}
	}

	private synchronized void setScreenCapture(Rectangle screenCap) {
		screenCapture = screenCap;
	}

	private synchronized void selectKeys() {
		selectingKeys = true;
	}

	private static void updatePathList(String newFilePath) {
		PrintWriter oStream = null;
		try {
			oStream = new PrintWriter(new FileWriter(startupPaths));
			oStream.append(newFilePath).append("\n");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (oStream != null)
				oStream.close();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		JComponent source = (JComponent) e.getSource();
		if (source == runningPaths.getEditor().getEditorComponent()) {
			if (e.getKeyChar() == KeyEvent.VK_ENTER) {
				File testFile;
				String quickPath = ((String) runningPaths.getSelectedItem()).trim();
				if (quickPath.equals(""))
					quickPath = DEFAULT_FILE;
				for (int i = 0; i < runningPaths.getItemCount(); i++)
					if ((runningPaths.getItemAt(i)).equals(quickPath)) {
						updateConsole("This path has already been listed.");
						return;
					}
				testFile = NEATAI.createNEATSaveFile(quickPath);
				if (testFile != null) {
					runningPaths.addItem(testFile.getPath());
					updatePathList(testFile.getPath());
					updateConsole("List of Files updated!");
				} else
					updateConsole("This file is not a saved NEAT file.");


			}
		} else if (selectingKeys) {
			if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
				updateConsole(keys.get(keys.size() - 1) + " Removed from list of outputs");
				keys.remove(keys.size() - 1);
				String outs = ("Outputs so far: ");
				for (Character key : keys) outs += key + " ";
				updateConsole(outs);
			} else if (e.getKeyChar() == KeyEvent.VK_ENTER) {
				updateConsole("Output selection finished.");
				selectingKeys = false;
				char[] outs = new char[keys.size()];
				for (int i = 1; i < keys.size(); i++)
					outs[i] = keys.get(i);
				AI = new NEATAI(screenCapture, outs, (String) runningPaths.getSelectedItem());
//				AI.start();
			} else {
				keys.add(e.getKeyChar());
				String outs = ("Outputs so far: ");
				for (Character key : keys) outs += key + " ";
				updateConsole(outs);
			}
		}

	}

	private synchronized void updateConsole(String update) {
		NEATConsole.append("\n" + update + "\n");
		NEATConsole.setCaretPosition(NEATConsole.getDocument().getLength());
	}


	public class NEATBattleship extends ComputerBattleshipPlayer {
			/*
			public NEATBattleship() {
				super("NEAT Battleship");
			}

			public void updatePlayer(Position pos, boolean hit, char initial, String boatName, boolean sunk, boolean gameOver, boolean tooManyTurns, int turns) {
				updateGrid(pos, hit, initial);
				updateConsole(peggs.toString());
			}

			public Position shoot() {
				//NEAT kicks-in here
				return (null);
			}
			*/
	}

	public static void main(String[] args) throws IOException {
		NEATLauncher launcher = new NEATLauncher();
		t1 = new Thread() {
			public void run() {
				Rectangle screenCap = null;
				try {
					Point p1 = null, p2 = null;
					launcher.updateConsole("Put your cursor in the TOP LEFT corner of the section of the screen that you'd like to select this position will be saved in: ");
					for (int i = 5; i > 0; i--) {
						launcher.updateConsole(i + "");
						Thread.sleep(1000);
					}
					p1 = MouseInfo.getPointerInfo().getLocation();


					launcher.updateConsole("Put your cursor in the BOTTOM RIGHT corner of the section of the screen that you'd like to select this position will be saved in: ");
					for (int i = 5; i > 0; i--) {
						launcher.updateConsole(i + "");
						Thread.sleep(1000);
					}
					p2 = MouseInfo.getPointerInfo().getLocation();
					screenCap = new Rectangle(p1, new Dimension(p2.x - p1.x, p2.y - p1.y));
				} catch (InterruptedException ignore) {
				}
				launcher.setScreenCapture(screenCap);
				launcher.selectKeys();
				launcher.updateConsole("Press any letter or number that should be used as output for the NEAT\nDELETE to remove the last entry\nENTER to finish: ");
			}
		};
	}
}