import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		JFrame mainFrame = new JFrame("Genetic Algorithm test");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		RacePanel rp = new RacePanel();
		mainFrame.setContentPane(rp);
		mainFrame.pack();
		mainFrame.setVisible(true);

		rp.go();

	}
}
