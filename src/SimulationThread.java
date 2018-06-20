public class SimulationThread extends Thread{
	private Population myPopulation;
	private int sleep;


	public SimulationThread(Population myPopulation,int sleep){
		this.myPopulation=myPopulation;
		this.sleep = sleep;
	}

	@Override
	public void run() {
		while(true){
			myPopulation.run(sleep);
			if(interrupted())
				return;
		}
	}
}
