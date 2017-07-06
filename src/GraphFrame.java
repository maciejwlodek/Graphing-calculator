import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GraphFrame{

	public GraphFrame(String function){
		JOptionPane options = new JOptionPane();
		
		String variable = "x";
		
		double xMin = 0;
		double yMin = -3;
		double xMax = 6;
		double yMax = 3;
		int zoom = 100;
		
		double refinement = 3/((double) zoom);
		
		String derivative = Solver.deriv(function, variable, refinement);
		System.out.println(function);

		int xSize = (int) ((xMax-xMin)*zoom);
		int ySize = (int) ((yMax-yMin)*zoom);
		
		
		String[] functions = {"f(x)","f'(x)"};
		
		int selected = options.showOptionDialog(null, "What would you like to graph?", "Select", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, functions, functions[0]);
		
		try{

			JPanel testGraph = new JPanel();
			if(selected==0){
				testGraph = new GraphPanel(function, variable, refinement, xMin, yMin, xMax, yMax, zoom);
			}
			if(selected==1){
				testGraph = new GraphPanel(derivative, variable, refinement, xMin, yMin, xMax, yMax, zoom);
			}

			//testGraph = new GraphPanel(function, variable, refinement, xMin, yMin, xMax, yMax, zoom);

			JFrame testFrame = new JFrame();
			if(selected==0) testFrame.setTitle("Graph of " + function);
			if(selected==1) testFrame.setTitle("Graph of d/dx (" + function + ")");

			testFrame.getContentPane().setPreferredSize(new Dimension(xSize, ySize));
			testFrame.pack();
			testFrame.setLocationRelativeTo(null);
			testFrame.setResizable(false);
			testFrame.add(testGraph);
			testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			testFrame.setVisible(true);
		}
		catch(Exception arg0){
			System.out.println("There was an error");
		}
		
	}
	
}
