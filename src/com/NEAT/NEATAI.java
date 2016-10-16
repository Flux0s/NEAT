package com.NEAT;

import java.io.*;
import java.util.ArrayList;

class NEATAI<T> {
	private String path;
	private ArrayList<ArrayList<Integer>> runningInput;
	private ArrayList<Generation> generations;
	private int inputs, outputs, population;
	private ScreenInput screen;
	private static final String SIGNATURE = "82356987109892847569812709846840957890879283123050399846924597293879";

	NEATAI(int numInputs, int numOutputs, int populationSize, String filePath) {
		inputs = numInputs;
		outputs = numOutputs;
		population = populationSize;
		path = filePath;
		generations = new ArrayList<Generation>();
		runningInput = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < numInputs; i++)
			runningInput.add(new ArrayList<Integer>());
	}

	public void updateInput(int[][] inputs) {
		runningInput = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < inputs.length; i++)
			for (int j = 0; j < inputs[0].length; j++) {
				runningInput.get(i).add(inputs[i][j]);
			}
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
}