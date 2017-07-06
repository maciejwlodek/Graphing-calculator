import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class MainWindow extends JFrame{

	JTabbedPane jtp;
	
	public MainWindow(){
	
		jtp = new JTabbedPane();
		jtp.add("Infix", new InfixPanel());
		//jtp.add("Postfix", new JButton());
		jtp.add("Graphing", new GraphCalcPanel());	
		
		add(jtp);
		setTitle("Maciej's Calculator");
		setVisible(true);
		setSize(360,500);
		//setLocationRelativeTo(null);
		
	}
	
	

}
