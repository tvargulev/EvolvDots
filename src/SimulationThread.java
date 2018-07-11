public class SimulationThread extends Thread{
	private Population myPopulation;


	public SimulationThread(Population myPopulation){
		this.myPopulation=myPopulation;
	}

	@Override
	public void run() {
		while(true){
			myPopulation.run();
			if(interrupted())
				return;
		}
	}
}
