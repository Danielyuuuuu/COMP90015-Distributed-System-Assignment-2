package client;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;


public class WhiteBoard extends JFrame implements MouseListener, MouseMotionListener{

	private JPanel panel;
	private Canvas canvas;
	private int x1, y1, x2, y2;
//	private List<Shape> whiteBoardContent = Collections.synchronizedList(new ArrayList<Shape>());
//	private Hashtable<Shape, Color> whiteBoardContent = new Hashtable<>();
	ConcurrentHashMap<Shape, Color> whiteBoardContent = new ConcurrentHashMap<>();
	
	private Boolean isWhiteBoardInUse = false;
	protected Graphics2D g;
	private Client client;
	private Mode mode = Mode.LINE;
	private int currentWhiteBoardSize = 0;

	private Color currentColor = Color.BLACK;

	/**
	 * Create the frame.
	 */
	public WhiteBoard(Client client) {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setBounds(100, 100, 450, 300);
//		contentPane = new JPanel();
//		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
//		contentPane.setLayout(new BorderLayout(0, 0));
//		setContentPane(contentPane);
		
		
		this.client = client;
		panel = new JPanel();
		setBounds(100, 100, 500, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(panel);
		panel.setLayout(null);
		
		canvas = new Canvas();
		canvas.setLocation(0, 100);
		canvas.setSize(500, 272); 
		canvas.addMouseMotionListener(this);
		canvas.addMouseListener(this);
		canvas.setBackground(Color.WHITE);
		panel.add(canvas);
		
		JButton btn_line = new JButton("Line");
		btn_line.setBounds(0, 6, 117, 29);
		panel.add(btn_line);
		
		JButton btn_clrcle = new JButton("Circle");
		btn_clrcle.setBounds(129, 6, 117, 29);
		panel.add(btn_clrcle);
		
		JButton btn_rectangle = new JButton("Rectangle");
		btn_rectangle.setBounds(383, 6, 117, 29);
		panel.add(btn_rectangle);
		
		JButton btn_oval = new JButton("Oval");
		btn_oval.setBounds(258, 6, 117, 29);
		panel.add(btn_oval);
		
		JButton btn_clearBoard = new JButton("Clear Board");
		btn_clearBoard.setBounds(383, 65, 117, 29);
		panel.add(btn_clearBoard);
		
		JButton btn_black = new JButton("Black");
		btn_black.setBounds(0, 65, 117, 29);
		panel.add(btn_black);
		
		JButton btn_blue = new JButton("Blue");
		btn_blue.setBounds(129, 65, 117, 29);
		panel.add(btn_blue);
		
		JButton btn_text = new JButton("Text");
		btn_text.setBounds(0, 36, 117, 29);
		panel.add(btn_text);
		
		setVisible(true);
		g = (Graphics2D)canvas.getGraphics();
		
		
		new Thread(new WhiteBoardListener()).start();
		
		btn_line.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mode = Mode.LINE;
			}
		});
		
		btn_clrcle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mode = Mode.CIRCLE;
			}
		});
		
		btn_oval.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mode = Mode.OVAL;
			}
		});
		
		btn_rectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mode = Mode.RECTANGLE;
			}
		});
		
		btn_clearBoard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					while (!client.getRMI().resetWhiteBoard()) {
						
					}
//					g.setColor(getBackground());
					
	                g.clearRect(0, 0, 500, 272);
	                
					System.out.println("clear board");
					
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		btn_black.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				g.setColor(Color.BLACK);
				currentColor = Color.BLACK;
				System.out.println("Changing to Black");
			}
		});
		
		btn_blue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				g.setColor(Color.BLUE);
				currentColor = Color.BLUE;
				System.out.println("Changing to blue");
			}
		});
		
		btn_text.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mode = Mode.TEXT;
			}
		});
	}
	
	@Override
	public void mouseDragged(MouseEvent e){
		Point position = canvas.getMousePosition();
		
		if(position!=null) {
			if (mode == Mode.LINE) {
				x2 = position.x;
				y2 = position.y;
				Line2D line = new Line2D.Float(x1, y1, x2, y2);
				g.setColor(currentColor);
				g.draw(line);
				isWhiteBoardInUse = true;
//				whiteBoardContent.add(line);
				
				whiteBoardContent.put(line, g.getColor());
				System.out.println("drawing color: " + g.getColor());
				
				isWhiteBoardInUse = false;
				x1 = x2;
				y1 = y2;
			}

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
			
			if (mode == Mode.CIRCLE) {
				Ellipse2D circle = new Ellipse2D.Float(x1, y1, 20, 20);
				g.setColor(currentColor);
				g.draw(circle);
				isWhiteBoardInUse = true;
//				whiteBoardContent.add(circle);
				
				whiteBoardContent.put(circle, currentColor);
				
				
				isWhiteBoardInUse = false;
			}
			else if (mode == Mode.OVAL) {
				Ellipse2D oval = new Ellipse2D.Float(x1, y1, 10, 20);
				g.setColor(currentColor);
				g.draw(oval);
				isWhiteBoardInUse = true;
//				whiteBoardContent.add(oval);
				
				whiteBoardContent.put(oval, currentColor);
				
				isWhiteBoardInUse = false;
			}
			else if (mode == Mode.RECTANGLE) {
				Rectangle2D rectangle = new Rectangle2D.Float(x1, y1, 20, 20);
				g.setColor(currentColor);
				g.draw(rectangle);
				isWhiteBoardInUse = true;
//				whiteBoardContent.add(rectangle);
				
				whiteBoardContent.put(rectangle, currentColor);

				
				isWhiteBoardInUse = false;
			}
			else if (mode == Mode.TEXT) {
				String text = JOptionPane.showInputDialog(panel, "Input Text");
				g.setColor(Color.BLACK);
				g.drawString(text, x1, y1);
			}
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
	
	public void drawExistingContent(ConcurrentHashMap<Shape, Color> whiteBoardContent) {
		
		if (this.currentWhiteBoardSize > whiteBoardContent.size()) {
			g.clearRect(0, 0, 500, 272);
		}
		this.currentWhiteBoardSize = whiteBoardContent.size();
		
//		for (Shape content: whiteBoardContent) {
//			g.draw(content);
//		}
		
		Iterator<ConcurrentHashMap.Entry<Shape, Color> > itr = ((ConcurrentHashMap<Shape, Color>) whiteBoardContent).entrySet().iterator();
		while (itr.hasNext()) {
            ConcurrentHashMap.Entry<Shape, Color> entry = itr.next();
            g.setColor(entry.getValue());
            g.draw(entry.getKey());
        }
		
		
//		Iterator<Shape> it = this.whiteBoardContent.iterator();
//		while(it.hasNext()) {
//			g.draw(it.next());
//		}
		
		Iterator<ConcurrentHashMap.Entry<Shape, Color> > itr2 = ((ConcurrentHashMap<Shape, Color>) this.whiteBoardContent).entrySet().iterator();
		while (itr2.hasNext()) {
            ConcurrentHashMap.Entry<Shape, Color> entry = itr2.next();
            g.setColor(entry.getValue());
            g.draw(entry.getKey());
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
						drawExistingContent(client.getRMI().getWhiteBoardContent());
						while (!client.getRMI().drawWhiteBoard(whiteBoardContent)){
							Thread.sleep(100);
						}
						whiteBoardContent = new ConcurrentHashMap<Shape, Color>();
						Thread.sleep(400);
					
					
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
