package com.NEAT;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

class NEATAI extends Thread {
	public static boolean save;
	private String path;
	private ArrayList<Generation> generations;
	public static final int POPULATION = 20, NUMINPUTNODES = 20;
	private char[] outputs;
	private Rectangle screen;
	private static final String SIGNATURE = "82356987109892847569812709846840957890879283123050399846924597293879";

	NEATAI(Rectangle screenIn, char output[], String filePath) {
		save = false;
		//TODO: reset the selected screen size to be easily divided into the specified number of sections.
		screen = screenIn;
		outputs = output;
		path = filePath;
		generations = new ArrayList<Generation>();
	}

	private static boolean isSavedNEAT(File testFile) {
		boolean wasSigned = false;
		BufferedReader iStream = null;
		if (testFile == null || !testFile.exists() || !testFile.getAbsolutePath().contains(".txt"))
			return (false);
		try {
			iStream = new BufferedReader(new FileReader(testFile));
			String firstLine = iStream.readLine();
			wasSigned = firstLine.equals(SIGNATURE);
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
		return (wasSigned);
	}

	static File createNEATSaveFile(String pathToCreate) {
		File created = new File(pathToCreate);
		PrintWriter oStream = null;
		if (created.exists()) {
			if (!NEATAI.isSavedNEAT(created))
				return (null);
			else
				return (created);
		}
		try {
			oStream = new PrintWriter(new FileWriter(created));
			created.createNewFile();
			oStream.println(SIGNATURE);
		} catch (IOException e) {
			return (null);
		} finally {
			if (oStream != null)
				oStream.close();
		}
		return (created);
	}

	private void save() {
		File NEAT = new File(path);
		PrintWriter oStream = null;
		try {
			oStream = new PrintWriter(new FileWriter(NEAT));
			oStream.println(this);
		} catch (IOException ignored) {
		} finally {
			if (oStream != null)
				oStream.close();
		}
	}

	//Loads a NEATAI from the specified path
	private void load(File NEAT) {
//		TODO: code the load method.
	}

	@Override
	public void run() {
		File NEAT = new File(path);
		if (NEATAI.isSavedNEAT(NEAT))
			load(NEAT);
		else
			generations.add(new Generation(screen, outputs));
		while (!save) {
			generations.get(generations.size() - 1).run();
			try {
				sleep(1000);
			} catch (Exception ignore) {
			}
		}
		try {
			generations.get(generations.size() - 1).join();
		} catch (Exception ignore) {
		}
		save();
	}

	//This returns the number of generations followed by each generation with a space between them
	public String toString() {
		String out = (generations.size() + "\n");
		for (int i = 0; i < generations.size(); i++)
			out += (generations.get(i) + "\n");
		return (out);
	}
}