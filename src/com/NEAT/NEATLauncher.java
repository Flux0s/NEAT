package com.NEAT;

import com.BattleshipAI.ComputerBattleshipPlayer;
import com.BattleshipAI.PlayerEvaluator;
import com.BattleshipAI.Position;

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
		//noinspection SuspiciousNameCombination
		NEATConsole.setMinimumSize(new Dimension(WINDOW_WIDTH - 30, WINDOW_WIDTH));
		NEATConsole.setFont(new Font("Source Code Pro", Font.PLAIN, 12));
		NEATConsole.setAutoscrolls(true);
		NEATConsole.setSelectedTextColor(Color.GRAY);
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
		if (source == button && button.getText().equals("Run")) {
			if (/*!program1.isSelected() && */!program2.isSelected()) {
				updateConsole(/*"Please Select the type of input below. "*/ "Currently only the screen input is implemented");
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
				updateConsole("File path has been confirmed.\nApplication Started using '" + program1.getText() + "' as input.");
				button.setText("Stop Program");
				StartNEAT(1);
			} else if (program2.isSelected()) {
				updateConsole("File path has been confirmed.\nApplication Started using '" + program2.getText() + "' as input.");
				button.setText("Stop Program");
				StartNEAT(2);
				//begin program here with screen capture as input
			}
		} else if (source == button && !button.getText().equals("Run")) {
			updateConsole("Saving and Stopping Program Now!");
			System.exit(0);
		}
	}

	private void StartNEAT(int inputProgram) {
		if (inputProgram == 1) {
			AI = new NEATAI(10, 2, 5, null);
			NEATBattleship NEATAI = new NEATBattleship();
			PlayerEvaluator evaluator = new PlayerEvaluator(NEATAI, Integer.MAX_VALUE);
		} else if (inputProgram == 2) {
			AI = new NEATAI(10, 2, 5, null);
		}
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
		if (source == runningPaths.getEditor().getEditorComponent() && e.getKeyChar() == KeyEvent.VK_ENTER) {
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

	}

	private void updateConsole(String update) {
		NEATConsole.append("\n" + update + "\n");
	}

	public class NEATBattleship extends ComputerBattleshipPlayer {
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

	}

	public static void main(String[] args) throws IOException {
		NEATLauncher launcher = new NEATLauncher();
	}
}