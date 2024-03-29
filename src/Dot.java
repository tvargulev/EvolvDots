import java.awt.*;
import java.util.List;

/**
 * class that represents an individual dot
 */
public class Dot {
	private Position currPos; // current Position
	private Brain brain; // stores the each step of the dot
	private int velocity; // how much blocks the dot can move in one step
	private int maxVelocity = 10;
	private int acceleration;
	private int maxX;
	private int maxY;
	private boolean isAlive = true;
	private boolean reachedGoal = false;
	private double weight = 0;
	private Position start; // where the dot spawns
	private Position goal;

	public Dot(Position start, int maxX, int maxY, Position goal) {
		this.start = start;
		brain = new Brain(1000); // TODO parameterise
		currPos = new Position(start.x, start.y);
		this.goal = goal;
		velocity = 1;
		acceleration = 1; // TODO increase??
		this.maxY = maxY;
		this.maxX = maxX;
	}

	public Dot(Position start, int maxX, int maxY, Position goal, Brain newBrain) {
		this(start, maxX, maxY, goal);
		brain = newBrain;
	}

	/**
	 * moves the dot
	 */
	private void move() {
		//System.out.print("Old x="+ currPos.x+" : y="+currPos.y+" ");
		currPos.x += velocity * brain.direction[brain.step].x;
		currPos.y += velocity * brain.direction[brain.step].y;
		brain.step++;

		if (velocity < maxVelocity) {
			velocity += acceleration;
			if(velocity>maxVelocity)
				velocity=maxVelocity;
		}
	}

	/**
	 * moves the dot and updates its state
	 * @param blocks the current obstructions
	 */
	public void update(List<Shape> blocks) {
		if (isAlive && !reachedGoal) {
			move();
			if (currPos.distanceTo(goal) <= 5)
				reachedGoal = true;
			if (currPos.x > maxX - 10 || currPos.y > maxY - 10 || currPos.x < 3 || currPos.y < 3) {
				isAlive = false;
			}
			for (Shape s : blocks) {
				if (s.contains(currPos.x, currPos.y))
					isAlive = false;
			}
			//System.out.println((maxX/2-150)+ " "+ (maxY/2-150));
		}
	}

	public Position getCurrPos() {
		return currPos;
	}

	public Brain getBrain() {
		return brain;
	}

	public double getWeight() {
		return weight;
	}

	/**
	 * mutates the dot
	 */
	public void mutate() {
		double mutationprob = 0.01;
		for (int i = 0; i < brain.direction.length; i++) {
			if (Math.random() <= mutationprob) {
				brain.direction[i] = Brain.getNewPosition();
			}
		}
	}

	/**
	 * calculates the fitness of the dot
	 */
	public void calculateFitness() {
		if (reachedGoal) {
			weight = 0.5 + (double) (brain.direction.length * brain.direction.length) / (brain.step * brain.step);
		} else
			weight = 1 / (double) (currPos.distanceTo(goal) * currPos.distanceTo(goal));
		//System.out.println(weight);
	}

	public boolean isReachedGoal() {
		return reachedGoal;
	}

	/**
	 * performs a crossover on two dots
	 * @param b the second parent
	 * @return the child
	 */
	public Dot crossover(Dot b) {
		Position[] newDirections = new Position[brain.direction.length];
		int chunk = 5;
		for (int i = 0; i < newDirections.length; i += chunk) {
			if (Math.random() < 0.5) {
				for (int j = 0; j < chunk && i + j < newDirections.length; j++)
					newDirections[i + j] = this.brain.direction[i + j];
			} else
				for (int j = 0; j < chunk && i + j < newDirections.length; j++)
					newDirections[i + j] = b.brain.direction[i + j];
		}

		return new Dot(start, maxX, maxY, goal, new Brain(newDirections));
	}

	/**
	 * returns the dot to the start
	 */
	public void restart() {
		currPos = new Position(start.x, start.y);
		velocity = 1;
		acceleration = 1;
		brain.step = 0;
		isAlive = true;
		reachedGoal = false;
	}
}
