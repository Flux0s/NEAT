package com.NEAT;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

class NEATAI implements Runnable {
	public static boolean save, finished;
	private String path;
	private ArrayList<Generation> generations;
	private static final int POPULATION = 20;
	private char[] outputs;
	private Rectangle screen;
	private static final String SIGNATURE = "82356987109892847569812709846840957890879283123050399846924597293879";

	NEATAI(Rectangle screenIn, char output[], String filePath) {
		save = false;
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
			oStream.println(generations.size() + "\n");

		} catch (IOException ignored) {
		} finally {
			if (oStream != null)
				oStream.close();
		}

	}

	//Loads a NEATAI from the specified path
	private void load() {
		File NEAT = new File(path);
	}

	@Override
	public void run() {
//		while (!save) {
//			generations.add(new Generation());
//			generations.get(generations.size() - 1).run();
//		}
//		save();
//		finished = true;
	}

	//This returns the number of generations followed by each generation with a space between them
	public String toString() {
		String out = (generations.size() + "\n");
		for (int i = 0; i < generations.size(); i++)
			out += (generations.get(i) + "\n");
		return (out);
	}
}