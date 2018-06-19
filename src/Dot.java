public class Dot {
	private Position currPos;
	private Brain brain;
	private int velocity;
	private int maxVelocity = 10;
	private int acceleration;
	private int maxX;
	private int maxY;
	private int startX;
	private int startY;
	private boolean isAlive = true;
	private boolean reachedGoal = false;
	private double weight = 0;
	private Position goal;

	public Dot(int startX, int startY, int maxX, int maxY, Position goal) {
		this.startX = startX;
		this.startY = startY;
		brain = new Brain(500);
		currPos = new Position(startX, startY);
		this.goal = goal;
		velocity = 1;
		acceleration = 1; // TODO increase??
		this.maxY = maxY;
		this.maxX = maxX;
	}

	public Dot(int startX, int startY, int maxX, int maxY, Position goal, Brain newBrain) {
		this(startX, startY, maxX, maxY, goal);
		brain = newBrain;
	}

	private void move() {
		//System.out.print("Old x="+ currPos.x+" : y="+currPos.y+" ");
		currPos.x += velocity * brain.direction[brain.step].x;
		currPos.y += velocity * brain.direction[brain.step].y;
		brain.step++;

		if (velocity < maxVelocity) // may result in speed > maxVelocity
			velocity += acceleration;
		//System.out.println("New d x="+ currPos.x+" : y="+currPos.y+" ");
	}

	public void update() {
		if (isAlive && !reachedGoal) {
			move();
			if (currPos.distanceTo(goal) < 5)
				reachedGoal = true;
			if (currPos.x > maxX - 10 || currPos.y > maxY - 10 || currPos.x < 3 || currPos.y < 3) {
				isAlive = false;
			}
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

	public void mutate() {
		for (int i = 0; i < brain.direction.length; i++) {
			if (Math.random() <= 0.01) {
				brain.direction[i] = Brain.getNewPosition();
			}
		}
	}

	public void calculateFitness() {
		if (reachedGoal) {
			weight = 3
					+ (double) (brain.step * brain.step) / (brain.direction.length * brain.direction.length);
		} else
			weight = 1 / (double) (currPos.distanceTo(goal) * currPos.distanceTo(goal));
		//System.out.println(weight);
	}

	public boolean isReachedGoal() {
		return reachedGoal;
	}

	public Dot crossover(Dot b) {
		Position[] newDirections = new Position[brain.direction.length];
		int chunk = 5;
		for (int i = 0; i < newDirections.length; i += chunk) {
			if (Math.random() < 0.5) {
				for (int j = 0; j < chunk && i + j < newDirections.length; j++)
					newDirections[i + j] = this.brain.direction[i + j];
			}
			else
				for (int j = 0; j < chunk && i + j < newDirections.length; j++)
					newDirections[i + j] = b.brain.direction[i + j];
		}


		//TODO implement
		return new Dot(startX, startY, maxX, maxY, goal, new Brain(newDirections)); //TODO not hardcode
	}
}
