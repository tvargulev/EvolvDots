import javax.swing.*;
import java.awt.*;

public class Population {
	private int width = 1200; // TODO fix this
	private int height = 600;
	private Position goal;
	private Dot[] currGeneration;
	private JPanel drawingPanel;
	private double totalFitness = 0;
	private int genNo = 1;
	private int maxSteps = 1000;


	public Population(JPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
		drawingPanel.setPreferredSize(new Dimension(width,height));
		Position start = new Position(20,height/2);
		goal = new Position(width -50, height/2);


		currGeneration = new Dot[maxSteps];
		for (int i = 0; i < currGeneration.length; i++) {
			currGeneration[i] = new Dot(start, width, height, goal);
		}
	}

	public void updateAll() {
		for (Dot p : currGeneration)
			p.update();
		drawingPanel.repaint();
	}

	public void calculateFitness() {
		totalFitness = 0;
		for (Dot p : currGeneration) {
			p.calculateFitness();
			totalFitness = totalFitness + p.getWeight();
		}
	}

	// stores  the fittest dotes in the first position
	public void saveFittestDot() {
		int index = 0;
		double maxFitness = currGeneration[index].getWeight();
		for (int i = 1; i < currGeneration.length; i++) {
			if (maxFitness < currGeneration[i].getWeight()) {
				maxFitness = currGeneration[i].getWeight();
				index = i;
			}
		}
		Dot bestDot = currGeneration[index];
		currGeneration[index] = currGeneration[0];
		currGeneration[0] = bestDot;
		if(bestDot.isReachedGoal())
		maxSteps = bestDot.getBrain().step;  // TODO check if it has reached the goal ???
	}

	public void mutate() {
		for (int i = 1; i < currGeneration.length; i++) { // doesn't mutate the first one
			currGeneration[i].mutate();
		}
	}

	public void crossover() {
		Dot[] nextGen = new Dot[currGeneration.length];
		Dot bestDot = currGeneration[0];
		bestDot.restart();

		for (int i = 1; i < nextGen.length; i++) {
			Dot A = getParent();
			Dot B = getParent();
			nextGen[i] = A.crossover(B);
		}
		currGeneration = nextGen;
		currGeneration[0] = bestDot;
		genNo++;
	}

	private Dot getParent() {
		double rnd = Math.random() * totalFitness;
		double curFitnes = 0;
		for (int i = 0; i < currGeneration.length; i++) {
			curFitnes += currGeneration[i].getWeight(); //TODO fix doenst work
			if (rnd <= curFitnes)
				return currGeneration[i];
		}
		return null;
	}

	public Dot[] getCurrGeneration() {
		return currGeneration;
	}

	public Position getGoal() {
		return goal;
	}

//	public void go() {
//		int generations = 1000;
//		for (int i = 0; i < generations; i++) { // TODO increase later
//			if (i % 100 == 0)
//				run(100); //TODO increase later later
//			else
//				run(1);
//		}
//	}


	public void run(int sleep) {
		for (int i = 0; i < maxSteps; i++) {
			updateAll();

			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				//e.printStackTrace();
			}
		}

		calculateFitness(); //TODO No mutation on last generation;
		saveFittestDot();
		int reached = 0;
		for(Dot d : currGeneration){
			if(d.isReachedGoal())
				reached++;
		}

		System.out.println("Generation No:" + genNo + "Dots reached goal: "+reached
				+"  maxWeight:"+ currGeneration[0].getWeight()
				+ " in number of steps:" + currGeneration[0].getBrain().step);



		mutate();
		crossover();
	}

	public Dot getFittest(){
		return currGeneration[0];
	}
	public int getGenNumber() {
		return genNo;
	}
}
