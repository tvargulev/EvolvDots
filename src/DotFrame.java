import javax.swing.*;
import java.awt.*;


public class DotFrame extends JFrame {
	private JPanel contentPane;
	private RacePanel rp;
	private JPanel buttonPanel;
	private boolean run;
	SimulationThread sm;

	public DotFrame(String title){
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());

		rp = new RacePanel();
		addButtonPanel();
		contentPane.add(rp,BorderLayout.CENTER);

		pack();
	}
	public void addButtonPanel(){
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));


		JSpinner sleep = new JSpinner();

		JButton start = new JButton("Start");
		start.addActionListener(e -> {

			if(sm==null ||!sm.isAlive()){
				sm=new SimulationThread(rp.getPopulation(),(int)sleep.getValue());
				sm.start();
			}

		});
		buttonPanel.add(start);


		JButton stop = new JButton("Stop");
		stop.addActionListener(e -> {
			if(sm!=null && !sm.isInterrupted())
				sm.interrupt();
		});
		buttonPanel.add(stop);

		JLabel spinLabel = new JLabel("Pause between moving:");
		buttonPanel.add(spinLabel);
		SpinnerNumberModel spinnerNumberModel =new  SpinnerNumberModel(10,0,3000,1);
		sleep.setModel(spinnerNumberModel);
		buttonPanel.add(sleep);

		contentPane.add(buttonPanel,BorderLayout.NORTH);
	}

}
