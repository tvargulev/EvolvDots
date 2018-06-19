public class Position {
	int x;
	int y;

	public Position(int x , int y){
		this.x=x;
		this.y=y;
	}

	public int distanceTo(Position a){
		return Math.abs(a.x-x)+Math.abs(a.y-y);
	}
}
