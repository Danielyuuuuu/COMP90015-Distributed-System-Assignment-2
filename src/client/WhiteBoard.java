package client;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.io.EOFException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class WhiteBoard extends JFrame implements MouseListener, MouseMotionListener{
	private JPanel panel;
	private Canvas canvas;
	private int x1, y1, x2, y2;
	private ArrayList<Shape> whiteBoardContent = new ArrayList<>();
	private Boolean isWhiteBoardInUse = false;
	protected Graphics2D g;
	private Client client;
	
	
	public WhiteBoard(Client client) {
		this.client = client;
		panel = new JPanel();
		setBounds(100, 100, 500, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(panel);
		panel.setLayout(null);
		
		canvas = new Canvas();
		canvas.setLocation(0, 0);
		canvas.setSize(500, 400); 
		canvas.addMouseMotionListener(this);
		canvas.addMouseListener(this);
		canvas.setBackground(Color.WHITE);
		panel.add(canvas);
		
		setVisible(true);
		g =(Graphics2D)canvas.getGraphics();
		
		new Thread(new WhiteBoardListener()).start();
	}
	
	@Override
	public void mouseDragged(MouseEvent e){
		Point position = canvas.getMousePosition();
		
		if(position!=null) {
			x2 = position.x;
			y2 = position.y;
			Line2D line = new Line2D.Float(x1, y1, x2, y2);
			isWhiteBoardInUse = true;
			whiteBoardContent.add(line);
			isWhiteBoardInUse = false;
			g.draw(line);
			x1 = x2;
			y1 = y2;
//			try {
//				client.getRMI().drawWhiteBoard((Shape) line);
//			} catch (RemoteException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Point position = canvas.getMousePosition();
		if (position != null) {
			x1 = position.x;
			y1 = position.y;
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void drawExistingContent(ArrayList<Shape> whiteBoardContent) {
		for (Shape content: whiteBoardContent) {
			g.draw(content);
		}
	}
	
//	public static void main(String[] args) {
//		new WhiteBoard().setVisible(true);
//	}
	
	private class WhiteBoardListener implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				while(true) {
					while(!isWhiteBoardInUse) {
						drawExistingContent(client.getRMI().getWhiteBoardContent());
						client.getRMI().drawWhiteBoard(whiteBoardContent);
						whiteBoardContent = new ArrayList<Shape>();
						Thread.sleep(400);
					}
				}
			}catch(ConcurrentModificationException e1) {
//				System.out.println("ConcurrentModificationException");
				e1.printStackTrace();
			}
			catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}
}
