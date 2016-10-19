package com.NEAT;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

class NEATAI implements Runnable {
	public static boolean stop;
	private String path;
	private ArrayList<Generation> generations;
	private static final int POPULATION = 20;
	private char[] outputs;
	private Rectangle screen;
	private static final String SIGNATURE = "82356987109892847569812709846840957890879283123050399846924597293879";

	NEATAI(Rectangle screenIn, char output[], String filePath) {
		stop = false;
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

	static File createNEATSaveFile(String pathTocreate) {
		File created = new File(pathTocreate);
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

	@Override
	public void run() {
		while (!stop) {
			generations.add(new Generation());
			generations.get(generations.size() - 1).run();
		}
	}
}