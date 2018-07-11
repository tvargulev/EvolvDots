import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class RacePanel extends JPanel {
	private Population myPopulation;
	private Position goal;
	private Shape temp;
	private ArrayList<Rectangle> blocks;

	public RacePanel(){
		this.myPopulation = new Population(this);
		goal = myPopulation.getGoal();
		blocks = new ArrayList<>();
		this.setBackground(Color.white);
		ObstacleListener myObstacleListener = new ObstacleListener();
		this.addMouseListener(myObstacleListener);
		this.addMouseMotionListener(myObstacleListener);
	/*
	this.addMouseListener(new MouseListener() {
			private int startX;
			private int startY;
			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				startX=e.getX(); //TODO only when stopped
				startY=e.getY();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				int endX = e.getX();
				int endY = e.getY();

				Rectangle r = new Rectangle(new Rectangle(Math.min(startX,endX),Math.min(startY,endY),Math.abs(startX-endX),Math.abs(startY-endY)));
				blocks.add(r);
				myPopulation.addBlock(r);
				repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}
		});
		*/

	}

	private class ObstacleListener extends MouseAdapter{
		private int startX;
		private int startY;
		@Override
		public void mousePressed(MouseEvent e) {
			super.mousePressed(e);
			startX=e.getX();
			startY=e.getY();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			super.mouseDragged(e);
			int endX = e.getX();
			int endY = e.getY();

			temp = new Rectangle(new Rectangle(Math.min(startX,endX),Math.min(startY,endY),Math.abs(startX-endX),Math.abs(startY-endY)));
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			super.mouseReleased(e);
			temp = null;

			int endX = e.getX();
			int endY = e.getY();

			Rectangle r = new Rectangle(new Rectangle(Math.min(startX,endX),Math.min(startY,endY),Math.abs(startX-endX),Math.abs(startY-endY)));
			blocks.add(r);
			myPopulation.addBlock(r);
			repaint();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		Dot[] population = myPopulation.getCurrGeneration();
		//paints the boundaries
		g2d.drawRect(0,0,getPreferredSize().width,getPreferredSize().height);

		g2d.setColor(Color.darkGray);
		if(temp!=null)
			g2d.draw(temp);

		//paints obstacles
		g2d.setColor(Color.lightGray);
		for(Rectangle r : blocks){
			g2d.fill(r);
		}


		g2d.setColor(Color.gray);
		if(myPopulation.getFittest().isReachedGoal()){
			g2d.drawString("Generation "+myPopulation.getGenNumber()+
					" reached the goal in "+myPopulation.getFittest().getBrain().step+" steps",10,20);
		}
		g2d.drawString("Generation "+myPopulation.getGenNumber(),10,20);


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

	public void clearBlocks(){
		blocks.clear();
		myPopulation.clearBlocks();
	}
	public Population getPopulation() {
		return myPopulation;
	}
}