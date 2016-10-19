package com.NEAT;

/**
 * Created by Jason on 5/27/2016.
 * TODO: Create a list genomes mapped to their corresponding finesses
 */

import java.util.ArrayList;

public class Species {

	private static final double SPECIATIONTHRESHOLD = 3.0;
	private static final double C1 = 3.0;
	private static final double C2 = 3.0;
	private static final double C3 = 3.0;
	private static final double MATECHANCE = 0.2;
	private static final double MUTATECHANCE = 0.8;
	private static final double RANDOMIZEGENE = 0.1;
	private static final double ENABLECHANCE = 0.1;

	private ArrayList<Genome> members;

	public Species(ArrayList<Node> inLayer, ArrayList<Node> outLayer) {
		members = new ArrayList<Genome>();
		members.add(new Genome(inLayer, outLayer));
	}

	// Returns whether or not a new species needs to be created
	public boolean add(Genome newGenome) {
		if (isInSpecies(newGenome)) {
			members.add(newGenome);
			return (false);
		}
		return (true);
	}

	public Genome reproduce() {
		Genome G1, G2;
		if (members.size() > 1 && Math.random() <= MATECHANCE) {
			int index1 = (int) (Math.random() * members.size()), index2 = (int) (Math.random() * members.size());
			G1 = members.get(index1);
			while (index2 == index1)
				index2 = (int) (Math.random() * members.size());
			G2 = members.get(index2);
			return (crossOver(G1, G2));
		}
		return (null);
	}

	public void normalizeFitness() {
		for (int i = 0; i < members.size(); i++)
			members.get(i).setFitness(members.get(i).getFitness() / members.size());
	}

	public Genome getIndividual(int index) {
		return (members.get(index));
	}

	private boolean isInSpecies(Genome newGenome) {
		double compatabilityDiffrence;
		compatabilityDiffrence = compatabilityCheck(newGenome, members.get((int) (Math.random() * members.size())));

		if (compatabilityDiffrence >= SPECIATIONTHRESHOLD)
			return (false);
		return (true);
	}

	private Genome crossOver(Genome G1, Genome G2) {
		int excessThreshold;
		Genome child = null, fit = G2, weak = G1, small = G1, large = G2;
		ArrayList<Node> nodes = new ArrayList<Node>();
		ArrayList<Link> links = new ArrayList<Link>();
		if (G1.getFitness() > G2.getFitness()) {
			fit = G1;
			weak = G2;
		}
		if (G1.size() > G2.size()) {
			large = G1;
			small = G2;
		}
		excessThreshold = small.getGenes().get(small.size()).getId();

		for (int i = 0; i < large.size(); i++)
			for (int j = 0; j < small.size(); j++) {
				// If the gene in the larger parent is shared
				if (large.getGenes().get(i).getId() == small.getGenes().get(i).getId())
					// Randomly choose a gene from one of the parents
					if (Math.random() < 0.5)
						links.add(large.getGenes().get(i).copy());
					else
						links.add(small.getGenes().get(j).copy());

					// If the gene in the larger genome is excess
				else if (large.getGenes().get(i).getId() > excessThreshold) {
					// If there is equal fitness
					if (large.getFitness() == small.getFitness())
						// Decide whether to inherit the gene
						if (Math.random() < 0.5)
							links.add(large.getGenes().get(i).copy());
						else
							links.add(fit.getGenes().get(i).copy());
				}
			}
		// After Genome construction determine if there is a mutation
		if (Math.random() <= MUTATECHANCE)
			// Determine if the mutation is a link or a node
			if (Math.random() <= 0.5)
				child = new Genome(child, true);
			else
				child = new Genome(child, false);
		randomizeGenes(child);
		return (child);
	}

	private void randomizeGenes(Genome G) {
		for (int i = 0; i < G.size(); i++)
			if (Math.random() <= RANDOMIZEGENE)
				G.getGenes().get(i).randomizeWeight();
	}

	private void enableGenes(Genome G) {
		for (int i = 0; i < G.size(); i++)
			if (Math.random() <= ENABLECHANCE)
				G.get(i).enable();
	}

	private double fittestWeight(int innovation) {
		double max = -1, weight = 1;
		for (int i = 0; i < members.size(); i++)
			for (int j = 0; j < members.get(i).size(); j++)
				if (members.get(i).getGenes().get(j).getId() == innovation && members.get(i).getFitness() > max) {
					max = members.get(i).getFitness();
					weight = members.get(i).getGenes().get(j).getWeight();
				}
		return (weight);
	}

	private double compatabilityCheck(Genome G1, Genome G2) {
		Genome smallerGenome, largerGenome;
		double E = 1, N, D = 0, W = 0;

		if (G1.size() > G2.size()) {
			smallerGenome = G2;
			largerGenome = G1;
			N = G1.size();
		} else {
			smallerGenome = G1;
			largerGenome = G2;
			N = G2.size();
		}
		if (smallerGenome.size() >= 20 && largerGenome.size() >= 20)
			E = largerGenome.getGenes().get(largerGenome.size() - 1).getId() - smallerGenome.getGenes().get(smallerGenome.size() - 1).getId();
		// Calculate the number of Disjoint genes and the average weight difference
		for (int i = 0; i < smallerGenome.size(); i++) {
			boolean disjoint = true;
			for (int j = 0; j < largerGenome.size(); j++)
				// If the innovation numbers match in both genomes the gene is not disjoint
				if (smallerGenome.getGenes().get(i).getId() == largerGenome.getGenes().get(i).getId()) {
					W += Math.abs(smallerGenome.getGenes().get(i).getWeight() - largerGenome.getGenes().get(i).getWeight());
					disjoint = false;
					j = largerGenome.size();
				}
			if (disjoint)
				D++;
		}
		W /= N - (D + E);
		double diffrence = (C1 * E / N) + (C2 * D / N) + C3 * W;
		return (diffrence);
	}
}