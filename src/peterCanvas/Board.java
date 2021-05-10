package peterCanvas;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

//import client.Drawing;

import javax.swing.JTextArea;
import javax.swing.JButton;

public class Board extends JFrame implements MouseListener,MouseMotionListener{

	private JPanel contentPane;
	protected Canvas canvas;
	protected int x1, y1, x2, y2;
	
	protected Graphics2D g;
	
	private ArrayList<Shape> shapeList = new ArrayList<Shape>();
	
	protected String mode = "line";
	
	protected Rectangle2D eraser = null;
	
	public Board() {
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		canvas = new Canvas(); 
		canvas.setLocation(109, 10);
		canvas.setSize(315, 241); 
		contentPane.add(canvas);
		canvas.addMouseMotionListener(this);
		canvas.addMouseListener(this);
		
		canvas.setBackground(Color.WHITE);
		
		JButton eraserButton = new JButton("Eraser");
		eraserButton.setBounds(10, 43, 93, 23);
		contentPane.add(eraserButton);
		
		JButton lineButton = new JButton("Line");
		lineButton.setBounds(10, 10, 93, 23);
		contentPane.add(lineButton);
		
		JButton clearButton = new JButton("Clear");
		clearButton.setBounds(10, 76, 93, 23);
		contentPane.add(clearButton);
		
		eraserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mode = "eraser";
			}
		});
		
		lineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mode = "line";
			}
		});
		
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
			}
		});
		
		setVisible(true);
		g =(Graphics2D)canvas.getGraphics();
	}
	
	public synchronized void paintByOther(Shape newShape) {
		shapeList.add(newShape);
		g.draw(newShape);
	}
	
	public synchronized void removeByOther(Shape newShape) {
		g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for(Shape s:shapeList) {
			if(s.equals(newShape)) {
				shapeList.remove(s);
			}else {
				g.draw(s);
			}
		}
	}
	
	public synchronized ArrayList<Shape> getShapeList(){
		return shapeList;
	}

	@Override
	public void mouseDragged(MouseEvent e){
		Point p = canvas.getMousePosition();
		if(mode.equals("line")) {
			if(p!=null) {
				x2 = p.x;
				y2 = p.y;
				Line2D a = new Line2D.Float(x1, y1, x2, y2);
				shapeList.add(a);
				g.draw(a);
				x1 = x2;
				y1 = y2;
				
			}
		}else if(mode.equals("eraser")) {
			Rectangle2D r = new Rectangle2D.Float(p.x-2,p.y-2,4,4);
			for(Shape s :shapeList) {
				if(s.intersects(r)) {
					shapeList.remove(s);
					g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
					for(Shape s1:shapeList) {
						g.draw(s1);
					}
				}
			}
		}
		
	}
	
	public void mousePressed(MouseEvent e) {
		Point p = canvas.getMousePosition();
		if(mode.equals("line")) {
			x1 = p.x;
			y1 = p.y;
		}else if(mode.equals("eraser")) {
			Rectangle2D r = new Rectangle2D.Float(p.x-2,p.y-2,4,4);
			for(Shape s :shapeList) {
				if(s.intersects(r)) {
					shapeList.remove(s);
					g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
					for(Shape s1:shapeList) {
						g.draw(s1);
					}
				}
			}
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		if(mode.equals("eraser")) {
			Point p = canvas.getMousePosition();
			if(shapeList.contains(eraser)) {
				shapeList.remove(eraser);
				eraser = new Rectangle2D.Float(p.x-5,p.y-5,10,10);
				shapeList.add(eraser);
				g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
				for(Shape s1:shapeList) {
					g.draw(s1);
				}
			}else {
				eraser = new Rectangle2D.Float(p.x-5,p.y-5,10,10);
				g.draw(eraser);
				shapeList.add(eraser);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
		new Board().setVisible(true);
	}
}
