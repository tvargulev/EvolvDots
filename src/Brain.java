public class Brain {
	Position[] direction;
	int step = 0 ;

	public Brain(Position[] direction){
		this.direction=direction;
		step = 0;
	}

	public Brain(int size){
		direction = new Position[size];
		for(int i =0 ; i<direction.length;i++){
			direction[i]=getNewPosition();
		}
	}
	public static Position getNewPosition(){
		return new Position(getRandom(),getRandom());
	}
	private static int getRandom(){
		int i =(int)(Math.random()*3);
		if(i == 2 )
			return 1;
		else if( i == 1)
			return 0;
		else
			return -1;
	}
}
