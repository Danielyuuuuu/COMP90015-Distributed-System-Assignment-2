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
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class WhiteBoard extends JFrame implements MouseListener, MouseMotionListener{
	private JPanel panel;
	private Canvas canvas;
	private int x1, y1, x2, y2;
	private ArrayList<Shape> shapeList = new ArrayList<Shape>();
	protected Graphics2D g;
	
	public WhiteBoard() {
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
		panel.add(canvas);
		canvas.setBackground(Color.WHITE);
		
		setVisible(true);
		g =(Graphics2D)canvas.getGraphics();
	}
	
	@Override
	public void mouseDragged(MouseEvent e){
		Point p = canvas.getMousePosition();
		
		if(p!=null) {
			x2 = p.x;
			y2 = p.y;
			Line2D a = new Line2D.Float(x1, y1, x2, y2);
			shapeList.add(a);
			g.draw(a);
			x1 = x2;
			y1 = y2;
			
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
		Point p = canvas.getMousePosition();
		x1 = p.x;
		y1 = p.y;
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
	
	public static void main(String[] args) {
		new WhiteBoard().setVisible(true);
	}
}
