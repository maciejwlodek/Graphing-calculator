import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.*;

public class InfixPanel extends JPanel {

	
	JTextField jtf;
	ButtonPanel bp;
	
	public InfixPanel(){
		
		setLayout(new BorderLayout());
		jtf = new JTextField();
		jtf.setHorizontalAlignment(JTextField.RIGHT);
		jtf.setFont(new Font("Arial", Font.BOLD, 15));
		bp = new ButtonPanel(jtf,ButtonPanel.INFIX);
		add(jtf, BorderLayout.NORTH);
		add(bp, BorderLayout.CENTER);
		
	}
}
