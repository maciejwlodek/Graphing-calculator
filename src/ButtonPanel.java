import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

import javax.swing.*;

public class ButtonPanel extends JPanel{

	JButton[] functionGrid;
	JButton[] numGrid;
	JButton[] operGrid;
	
	JPanel functionPanel;
	JPanel numPanel;
	JPanel operPanel;
	
	String[] opers = {"(","+","-",")","*","/","C","^","="};
	String[] funcs = {"sin","cos","tan","arcsin","arccos","arctan","ln","log","sqrt","abs","floor","ceiling","gamma"};
	List fList = Arrays.asList(funcs);
	
	JTextField writeToField;
	
	MyActionListener mal;
	
	boolean justEntered = false;
	
	static final int INFIX = 1;
	static final int GRAPH = 2;
	static final int POSTFIX = 3;

	
	//type equals "Infix", "Graph", or "Postfix
	int type;
	
	public ButtonPanel(JTextField writeToField, int type){
		
		this.writeToField = writeToField;
		this.type=type;
		
		mal = new MyActionListener();
		
		functionGrid = new JButton[15];
		numGrid = new JButton[12];
		operGrid = new JButton[9];

		numPanel = new JPanel();
		numPanel.setLayout(new GridLayout(4,3,5,5));
		
		functionPanel = new JPanel();
		functionPanel.setLayout(new GridLayout(5,3));
		
		operPanel = new JPanel();
		operPanel.setLayout(new GridLayout(3,3,5,5));

		setLayout(new BorderLayout(15,15));
		
		for(int i=1;i<11;i++){
			numGrid[i] = new JButton(""+(i%10));
			numGrid[i].setContentAreaFilled(false);
			numGrid[i].setFont(new Font("Arial", Font.BOLD, 25));
			numGrid[i].addActionListener(mal);
			numPanel.add(numGrid[i]);
		}
		numGrid[10] = new JButton(".");
		numGrid[10].setContentAreaFilled(false);
		numGrid[10].setFont(new Font("Arial", Font.BOLD, 25));
		numGrid[10].addActionListener(mal);
		numPanel.add(numGrid[10]);
		
		if(type == GRAPH){
			numGrid[11] = new JButton("x");
			numGrid[11].setContentAreaFilled(false);
			numGrid[11].setFont(new Font("Arial", Font.BOLD, 25));
			numGrid[11].addActionListener(mal);
			numPanel.add(numGrid[11]);
		}
		
		

		
		for(int i=0;i<9;i++){
			operGrid[i] = new JButton(opers[i]);
			operGrid[i].setContentAreaFilled(false);
			operGrid[i].setFont(new Font("Arial", Font.BOLD, 25));
			operGrid[i].addActionListener(mal);
			operPanel.add(operGrid[i]);
		}
		
//		functionGrid[0] = new JButton("Available functions");
//		functionGrid[0].setContentAreaFilled(false);
//		functionGrid[0].setBorderPainted(false);
//		functionPanel.add(functionGrid[0]);
		for(int i=0;i<13;i++){
			functionGrid[i] = new JButton(funcs[i]);
			functionGrid[i].setContentAreaFilled(false);
			functionGrid[i].addActionListener(mal);
			functionPanel.add(functionGrid[i]);
		}
		functionGrid[13] = new JButton("pi");
		functionGrid[13].setContentAreaFilled(false);
		functionGrid[13].addActionListener(mal);
		functionPanel.add(functionGrid[13]);
		
		functionGrid[14] = new JButton("e");
		functionGrid[14].setContentAreaFilled(false);
		functionGrid[14].addActionListener(mal);
		functionPanel.add(functionGrid[14]);

		add(functionPanel, BorderLayout.NORTH);
		add(numPanel, BorderLayout.CENTER);
		add(operPanel, BorderLayout.EAST);

		
	}
	
	class MyActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			JButton jb = (JButton) e.getSource();
			String text = jb.getText();
			if(text.equals("=")){	
				if(type == INFIX){
					String expression = writeToField.getText();
					double answer = Solver.fullCalculation(expression);
					writeToField.setText(answer+"");
				}
				if(type == GRAPH){
					String function = writeToField.getText();
					new GraphFrame(function);
				}
			}
			else if(text.equals("C")){
				writeToField.setText("");
			}
			else if(text.equals("B")){
				String expression = writeToField.getText();
				if(expression.length()>0){
					writeToField.setText(expression.substring(0,expression.length()-1));
				}
			}
			else if(fList.contains(text)){
				String expression = writeToField.getText();
				writeToField.setText(expression+text+"(");
			}
			else{
				String expression = writeToField.getText();
				writeToField.setText(expression+text);
			}
		}
		
	}
	
}
