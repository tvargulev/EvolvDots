import javax.swing.*;
import java.awt.*;

public class RacePanel extends JPanel {
	private Population myPopulation;
	private Position goal;

	public RacePanel(){
		this.myPopulation = new Population(this);
		goal = myPopulation.getGoal();
		//this.setPreferredSize(new Dimension(800,600));
		this.setBackground(Color.white);

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		Dot[] population = myPopulation.getCurrGeneration();
		//paints the boundaries
		g2d.drawRect(0,0,getPreferredSize().width,getPreferredSize().height);

		g2d.setColor(Color.gray);
		if(myPopulation.getFittest().isReachedGoal()){
			g2d.drawString("Generation "+myPopulation.getGenNumber()+
					"in "+myPopulation.getFittest().getBrain().step+"steps",10,20);
		}
		g2d.drawString("Generation "+myPopulation.getGenNumber(),10,20);

		g2d.fillRect(getPreferredSize().width/2-150,getPreferredSize().height/2-150,300,300); // TODO remove later


		// paints the goal
		g2d.setColor(Color.red);
		g2d.fillOval(goal.x-5,goal.y-5,10,10);

		// paints the fittest Dot //TODO solve for the first time
		g2d.setColor(Color.cyan);
		g2d.fillOval(population[0].getCurrPos().x,population[0].getCurrPos().y,6,6);

		// paints the other dots
		g2d.setColor(Color.black);
		for(int i =1;i<population.length;i++){
			if(population[i].isReachedGoal())
				g2d.setColor(Color.green);
			Position dotPos = population[i].getCurrPos();
			g2d.fillOval(dotPos.x,dotPos.y,6,6);
			g2d.setColor(Color.black);
		}
	}

	public void go(int a){
		myPopulation.run(a);
		//myPopulation.go();
	}

	public Population getPopulation() {
		return myPopulation;
	}
}