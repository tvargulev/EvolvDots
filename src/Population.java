import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Population {
	private int width = 1200; // TODO fix this
	private int height = 600;
	private Position goal;
	private Dot[] currGeneration;
	private JPanel drawingPanel;
	private double totalFitness = 0;
	private int genNo = 1;
	private int maxSteps = 1000;
	private int dotsCount = 1000;
	private ArrayList<Shape> blocks;
	private int sleep = 10; // ms pause between steps


	public Population(JPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
		drawingPanel.setPreferredSize(new Dimension(width,height));
		Position start = new Position(20,height/2); // TODO make changeable
		goal = new Position(width -50, height/2); // TODO make changeable

		blocks = new ArrayList<>();

		currGeneration = new Dot[dotsCount];
		for (int i = 0; i < currGeneration.length; i++) {
			currGeneration[i] = new Dot(start, width, height, goal);
		}
	}

	private void updateAll() {
		for (Dot p : currGeneration)
			p.update(blocks);
		drawingPanel.repaint();
	}

	/**
	 * claculates the fitness of each dot
	 */
	private void calculateFitness() {
		totalFitness = 0;
		for (Dot p : currGeneration) {
			p.calculateFitness();
			totalFitness = totalFitness + p.getWeight();
		}
	}

	/**
	 * stores  the fittest dotes in the first position
	 */
	private void saveFittestDot() {
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

	/**
	 * mutates all dots excepts the first one
	 */
	private void mutate() {
		for (int i = 1; i < currGeneration.length; i++) { // doesn't mutate the first one
			currGeneration[i].mutate();
		}
	}

	/**
	 *  creates the next generation while preserving the fittest dot in the current one
	 */
	private void crossover() {
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

	public void clearBlocks(){
		blocks.clear();
	}

	/**
	 *  returns a parent dot with probability based on fitness level
	 * @return parent dot
	 */
	private Dot getParent() {
		double rnd = Math.random() * totalFitness;
		double currFitness = 0;
		for (int i = 0; i < currGeneration.length; i++) {
			currFitness += currGeneration[i].getWeight();
			if (rnd <= currFitness)
				return currGeneration[i];
		}
		return null; // should never reach this
	}

	public Dot[] getCurrGeneration() {
		return currGeneration;
	}

	public Position getGoal() {
		return goal;
	}

	public void addBlock(Shape s){
		blocks.add(s);
		maxSteps = 1000; // resets
	}

	public void run() {
		for (int i = 0; i < maxSteps; i++) {
			updateAll();

			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				//e.printStackTrace();
			}
		}

		calculateFitness();
		saveFittestDot();
		int reached = 0;
		for(Dot d : currGeneration){
			if(d.isReachedGoal())
				reached++;
		}

		System.out.println("Generation No:" + genNo + " Dots reached goal: "+reached
				+" maxWeight: "+ currGeneration[0].getWeight()
				+ " in number of steps: " + currGeneration[0].getBrain().step);



		mutate();
		crossover();
	}


	public void setSleep(int sleep){
		this.sleep=sleep;
	}
	/**
	 * @return fittest dot in the generation which is stored in the first position
	 */
	public Dot getFittest(){
		return currGeneration[0];
	}

	public int getGenNumber() {
		return genNo;
	}
}
