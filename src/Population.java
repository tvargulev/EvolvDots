import javax.swing.*;

public class Population {
	private int width = 800; // TODO fix this
	private int height = 600;
	private Position goal;
	private Dot[] currGeneration;
	private JPanel drawingPanel;
	private double totalFitness = 0 ;
	private int genNo = 1;
	private Dot bestDot = null;


	public Population(JPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
		goal = new Position(width / 2, 40);
		currGeneration = new Dot[400];
		for (int i = 0; i < currGeneration.length; i++) {
			currGeneration[i] = new Dot(width / 2, height  - 20, width, height, goal);
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
			totalFitness=totalFitness+p.getWeight();
		}
	}
// stores  the fittest dotes in the first position
	public void saveFittestDot(){
		int index=0;
		double maxFitness =currGeneration[index].getWeight();
		for(int i = 1 ; i< currGeneration.length;i++){
			if(maxFitness<currGeneration[i].getWeight()){
				maxFitness = currGeneration[i].getWeight();
				index = i;
			}
		}
		bestDot = currGeneration[index];
	}
	public void mutate(){
		for(Dot d : currGeneration){ // do not mutate best one
			d.mutate();
		}
	}
	public void crossover(){
		Dot[] nextGen = new Dot[currGeneration.length];

		for(int i =0 ; i<nextGen.length;i++){ //TODO change to one save fittest
			Dot A = getParent();
			Dot B = getParent();
			nextGen[i]= A.crossover(B);
		}


		currGeneration = nextGen;
		// TODO save best one
	}
	private Dot getParent(){
		double rnd = Math.random()*totalFitness;
		double curFitnes=0;
		for(int i=0;i<currGeneration.length;i++){
			curFitnes+=currGeneration[i].getWeight(); //TODO fix doenst work
			if(rnd<=curFitnes)
				return currGeneration[i];
		}
		return  null;
	}

	public Dot[] getCurrGeneration() {
		return currGeneration;
	}

	public Position getGoal() {
		return goal;
	}

	public void go() {
		int generations = 400;
		for (int i = 0; i < generations; i++) { // TODO increase later
			run();
		}
	}


	private void run() {
		for (int i = 0; i < 400; i++) {
			updateAll();
			if(i<10){
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		calculateFitness(); //TODO No mutation on last generation;
		saveFittestDot();
		mutate();
		crossover();
		genNo++;
		System.out.println("Generation No:"+genNo+"  "+currGeneration[0].getWeight()+" in number of steps:"+ currGeneration[0].getBrain().step);
	}

}
