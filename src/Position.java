public class Position {
	int x;
	int y;

	public Position(int x , int y){
		this.x=x;
		this.y=y;
	}

	public double distanceTo(Position a){
		return Math.sqrt((x-a.x)*(x-a.x)+(y-a.y)*(y-a.y));
	}
}
