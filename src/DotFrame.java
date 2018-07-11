import javax.swing.*;
import java.awt.*;

/**
 * the main frame of the application
 */
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

	/**
	 * adds the buttons to the button panel
	 */
	public void addButtonPanel(){
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		// button that starts the simulation
		JButton start = new JButton("Start");
		start.addActionListener(e -> {
			if(sm==null ||!sm.isAlive()){
				sm=new SimulationThread(rp.getPopulation());
				sm.start();
			}

		});
		buttonPanel.add(start);

		// button that stops the simulation by interrupting the simulation thread
		JButton stop = new JButton("Stop");
		stop.addActionListener(e -> {
			if(sm!=null && !sm.isInterrupted())
				sm.interrupt();
		});
		buttonPanel.add(stop);

		JLabel spinLabel = new JLabel("Pause between moving:");
		buttonPanel.add(spinLabel);

		JSpinner sleep = new JSpinner();
		sleep.addChangeListener(e -> rp.getPopulation().setSleep((int)sleep.getValue()));
		SpinnerNumberModel spinnerNumberModel =new  SpinnerNumberModel(10,0,3000,1);
		sleep.setModel(spinnerNumberModel);
		buttonPanel.add(sleep);

		JButton clear = new JButton("Clear");
		clear.addActionListener(e->{
			rp.clearBlocks();
			rp.repaint();
		});
		buttonPanel.add(clear);

		contentPane.add(buttonPanel,BorderLayout.NORTH);
	}

}
