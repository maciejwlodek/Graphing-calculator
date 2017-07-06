import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class GraphCalcPanel extends JPanel{
	JTextField jtf;
	ButtonPanel bp;
	
	public GraphCalcPanel(){
		setLayout(new BorderLayout());
		jtf = new JTextField();
		jtf.setHorizontalAlignment(JTextField.RIGHT);
		jtf.setFont(new Font("Arial", Font.BOLD, 15));
		bp = new ButtonPanel(jtf,ButtonPanel.GRAPH);
		add(jtf, BorderLayout.NORTH);
		add(bp, BorderLayout.CENTER);
	}
}
