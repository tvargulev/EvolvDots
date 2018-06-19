import javax.swing.*;
import java.awt.*;

public class RacePanel extends JPanel {
	private Population myPopulation;
	private Position goal;

	public RacePanel(){
		this.myPopulation = new Population(this);
		goal = myPopulation.getGoal();
		this.setPreferredSize(new Dimension(800,600));

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		Dot[] population = myPopulation.getCurrGeneration();


		// paints the goal
		g2d.setColor(Color.red);
		g2d.fillOval(goal.x-3,goal.y-3,6,6);

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

	public void go(){
		myPopulation.go();
	}
}