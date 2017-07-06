import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.*;

public class GraphPanel extends JPanel{
	
	final int maxZoom = 300;
	final int minZoom = 20;
	
	String function;
	String variable;
	double refinement;
	double xMin;
	double yMin;
	double xMax;
	double yMax;
	int zoom;
	
	int xSize;
	int ySize;
	
	Point tempPoint=new Point(-1,-1);
	
	Point currentPoint = new Point(0,0);
	boolean currentPointFilled=false;
	
	Graphics g;
	
	ArrayList<Point> list = new ArrayList<Point>();
	
	//boolean isList;

	public GraphPanel(String function, String variable, double refinement, double xMin, double yMin, double xMax,
			double yMax, int zoom) {
		super();
		this.function = function;
		this.variable = variable;
		this.refinement = refinement;
		this.xMin = xMin;
		this.yMin = yMin;
		this.xMax = xMax;
		this.yMax = yMax;
		this.zoom=zoom;
		
		xSize = (int) ((xMax-xMin)*zoom);
		ySize = (int) ((yMax-yMin)*zoom);
		
		//isList = false;
		
		addMouseListener(new MyMouseListener(this));
		addMouseMotionListener(new MyMotionListener());
		addMouseWheelListener(new ScrollListener());

	}
	
//	public GraphPanel(ArrayList<Point> list, int zoom){
//		xMin = list.get(0).getX();
//		yMin = list.get(0).getY();
//		xMax = list.get(list.size()-1).getX();
//		yMax = list.get(list.size()-1).getY();
//		
//		refinement = list.get(1).getX() - xMin;
//		
//		this.zoom = zoom;
//		
//		xSize = (int) ((xMax-xMin)*zoom);
//		ySize = (int) ((yMax-yMin)*zoom);
//		
//		isList = true;
//		
//		addMouseListener(new MyMouseListener(this));
//		addMouseMotionListener(new MyMotionListener());
//		addMouseWheelListener(new ScrollListener());
//
//	}


	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		this.g=g;

		Graphics2D g2 = (Graphics2D) g;
		RenderingHints rh = new RenderingHints(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHints(rh);

		g2.setStroke(new BasicStroke(2));

		g2.drawLine(pointToPixel(xMin,true), pointToPixel(0,false), pointToPixel(xMax,true)+15, pointToPixel(0,false));
		g2.drawLine(pointToPixel(0,true), pointToPixel(yMin,false)+15, pointToPixel(0,true), pointToPixel(yMax,false));

		g2.setStroke(new BasicStroke(1));

		double multiplier=0;
		if(zoom==20) multiplier=4;
		if(zoom>20 && zoom<=40) multiplier=2;
		if(zoom>40 && zoom<=80) multiplier=1;
		if(zoom>80 && zoom<=160) multiplier=0.5;
		if(zoom>160 && zoom<=300) multiplier=0.25;
		//if(zoom==300) multiplier=0.1;

		double temp = xMin;
		while(temp<xMax){
			double a = multiplier*Math.ceil(temp/multiplier);
			g2.drawLine(pointToPixel(a,true),pointToPixel(0,false)-5,pointToPixel(a,true),pointToPixel(0,false)+5);
			g2.drawString(a+"", pointToPixel(a,true), pointToPixel(0,false)-10);
			temp+=multiplier;
		}
		temp=yMin;
		while(temp<yMax){
			double b = multiplier*Math.ceil(temp/multiplier);
			g2.drawLine(pointToPixel(0,true)+5,pointToPixel(b,false),pointToPixel(0,true)-5,pointToPixel(b,false));
			g2.drawString(b+"", pointToPixel(0,true)+10, pointToPixel(b,false));
			temp+=multiplier;
		}
       

        
        for(double i=xMin;i<xMax;i+=refinement){
        	
        	try{
        		double x1 = (double) Math.round(i * 1000000) / 1000000;
        		double x2 = (double) Math.round((i+refinement) * 1000000) / 1000000;
        		double y1 = Solver.evaluateFunction(function, variable, x1);
        		double y2 = Solver.evaluateFunction(function, variable, x2);

        		int x1Int = pointToPixel(x1,true);
        		int x2Int = pointToPixel(x2,true);
        		int y1Int = pointToPixel(y1,false);
        		int y2Int = pointToPixel(y2,false);

        		list.add(new Point(x1Int,y1Int));

        		g2.drawLine(x1Int, y1Int, x2Int, y2Int);

        	}
        	catch(Exception e){
        		//System.out.println("There was an error");
        		//e.printStackTrace();
        	}
        	
        }
        
        
        int radius = 4;
        if(currentPointFilled){
        	g2.fillOval((int) currentPoint.getX()-radius, (int) currentPoint.getY()-radius, 2*radius, 2*radius);
        	double xPos = pixelToPoint((int) currentPoint.getX(),true);
        	double yPos = pixelToPoint((int) currentPoint.getY(),false);
        	xPos = (double) Math.round(xPos * 100) / 100;
        	yPos = (double) Math.round(yPos * 100) / 100;

        	g2.drawString("("+xPos+","+yPos+")", (int) currentPoint.getX()+3, (int) currentPoint.getY());
        }
        
        
        
	}
	
	public int pointToPixel(double point, boolean x){
		if(x) return (int) ((point-xMin)*zoom);
		else return (int) ((yMax-point)*zoom);
	}
	public double pixelToPoint(int pixel, boolean x){
		double pixelD = (double) pixel;
		double zoomD = (double) zoom;
		if(x) return xMin+pixelD/zoomD;
		else return yMax-pixelD/zoomD;
	}
	
	
	class MyMouseListener implements MouseListener{
		GraphPanel graphPanel;
		public MyMouseListener(GraphPanel graphPanel){
			this.graphPanel=graphPanel;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			tempPoint = e.getPoint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			tempPoint = new Point(100,200);
		}

		@Override
		public void mouseEntered(MouseEvent e) {			
		}

		@Override
		public void mouseExited(MouseEvent e) {			
		}
		
	}
	class MyMotionListener implements MouseMotionListener{

		@Override
		public void mouseDragged(MouseEvent e) {
			if(tempPoint.equals(new Point(-1,-1))){
				tempPoint = e.getPoint();
			}
			Point p = e.getPoint();
			currentPointFilled = false;
			
			int xDist = (int) (p.getX()-tempPoint.getX());
			int yDist = (int) (p.getY()-tempPoint.getY());
			
			double ratioX = (xMax-xMin)/((double) xSize); 
			double ratioY = (yMax-yMin)/((double) ySize); 
			
			double xDistActual = xDist*ratioX;
			double yDistActual = yDist*ratioY;
			
			xMin-=xDistActual;
			xMax-=xDistActual;
			yMin+=yDistActual;
			yMax+=yDistActual;
						
			list = new ArrayList<Point>();
			repaint();
			tempPoint = p;
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			
//			int tolerance = 40;
//			
//			Point p = e.getPoint();
//			int min = Integer.MAX_VALUE;
//			Point minPoint =new Point(0,0);
//			for(Point point:list){
//				double d = point.distance(p);
//				if(d<min){
//					min = (int) d;
//					minPoint = point;
//				}
//			}
//			if(min<tolerance){
//				currentPoint = minPoint;
//				currentPointFilled=true;
//				repaint();
//			}	
//			if(min>=tolerance){
//				currentPointFilled=false;
//				repaint();
			//			}

			if(!(list.size()<(xMax-xMin)/refinement-20)){

				Point p = e.getPoint();
				double x = p.getX();
				double ratio = x/xSize;
				int r = (int) (ratio*(xMax-xMin)/refinement);

				try{
					Point p2 = list.get(r);

					if(Math.abs(p2.getY() - p.getY()) < 40){
						currentPoint = p2;
						currentPointFilled=true;
						repaint();
					}
					else{
						currentPointFilled=false;
						repaint();
					}
				}
				catch(Exception arg0){
					//System.out.println("There was an error.");
					//arg0.printStackTrace();
				}
			}
			
			
		}
		
	}
	
	class ScrollListener implements MouseWheelListener{

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			int scroll = -e.getWheelRotation();
			Point p = e.getPoint();
			int x = (int) p.getX();
			int y = (int) p.getY();
			
			double xActual = pixelToPoint(x,true);
			double yActual = pixelToPoint(y,false);

			currentPointFilled = false;

			double zf = Math.pow(1.1, scroll);

			int tempZoom = zoom;

			zoom = (int) (zoom*zf);

			zoom = Math.min(zoom, maxZoom);
			zoom = Math.max(zoom, minZoom);

			double width = (double) xSize;
			double height = (double) ySize;


			double offsetX = width/((double) zoom) - width/((double) tempZoom);
			double offsetY = height/((double) zoom) - height/((double) tempZoom);
			offsetX = offsetX/2;
			offsetY = offsetY/2;

			xMin -= offsetX;
			xMax += offsetX;
			yMin -= offsetY;
			yMax += offsetY;

			list = new ArrayList<Point>();

			repaint();			
		}
	}
}
