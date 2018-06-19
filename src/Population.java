import javax.swing.*;

public class Population {
	private int width = 800; // TODO fix this
	private int height = 600;
	private Position goal;
	private Dot[] currGeneration;
	private JPanel drawingPanel;
	private double totalFitness = 0;
	private int genNo = 1;
	private int maxSteps = 400;


	public Population(JPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
		goal = new Position(width / 3, 40);
		currGeneration = new Dot[400];
		for (int i = 0; i < currGeneration.length; i++) {
			currGeneration[i] = new Dot(width / 2, height - 20, width, height, goal);
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

	public void go() {
		int generations = 1000;
		for (int i = 0; i < generations; i++) { // TODO increase later
			if (i % 10 == 0)
				run(1); //TODO increase later later
			else
				run(1);
		}
	}


	private void run(int sleep) {
		for (int i = 0; i < maxSteps; i++) {
			updateAll();

			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
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
		genNo++;
	}

}
