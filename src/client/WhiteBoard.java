package client;


import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerListener;
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
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import remote.Coordinates;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;


public class WhiteBoard extends JFrame implements MouseListener, MouseMotionListener{

	private JPanel panel;
	private Canvas canvas;
	private int x1, y1, x2, y2;
//	private List<Shape> whiteBoardContent = Collections.synchronizedList(new ArrayList<Shape>());
//	private Hashtable<Shape, Color> whiteBoardContent = new Hashtable<>();
	private ConcurrentHashMap<Shape, Color> whiteBoardContent = new ConcurrentHashMap<>();
	
	
	
//	private Boolean isWhiteBoardInUse = false;
	protected Graphics2D g;
	private Client client;
	private Mode mode = Mode.LINE;
	private int currentWhiteBoardSize = 0;
	private int currentWhiteBoardTextSize = 0;

	private Color currentColor = Color.BLACK;
	
	private ConcurrentHashMap<Coordinates, String> textList = new ConcurrentHashMap<>();

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
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
		
		JButton btn_text = new JButton("Text");
		btn_text.setBounds(0, 36, 117, 29);
		panel.add(btn_text);
		
		JButton btn_colour = new JButton("Choose Colour");
		btn_colour.setBounds(0, 65, 117, 29);
		panel.add(btn_colour);
		
		setVisible(true);
		g = (Graphics2D)canvas.getGraphics();
		
		
		new Thread(new WhiteBoardListener()).start();
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	try {
//					client.getRMI().managerDisconnect();
		    		if (client.getRMI().isManager(client.getUserName())) {
		    			int toExit = JOptionPane.showConfirmDialog(
								panel,
								String.format(
									"Are you sure you want to close the white board?"),
								"Warning",
								JOptionPane.YES_NO_OPTION);
		    			if (toExit == JOptionPane.YES_OPTION) {
		    				client.getRMI().resetWhiteBoard();
		    				client.getRMI().closeWhiteBoard();
		    				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		    			}
		    			else {
		    				setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		    			}
		    		}
		    		System.out.println("Closing");
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});	
		
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
					client.getRMI().resetWhiteBoard();
					g.clearRect(0, 0, 500, 272);
					System.out.println("clear board");
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		btn_text.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mode = Mode.TEXT;
			}
		});
		
		btn_colour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				  
		        // color chooser Dialog Box
				Color selectedColor = JColorChooser.showDialog(panel,
		                    "Select a colour", currentColor);
				if (selectedColor != null) {
					currentColor = selectedColor;
				}
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
//				isWhiteBoardInUse = true;
//				whiteBoardContent.add(line);
				
				whiteBoardContent.put(line, g.getColor());
//				System.out.println("drawing color: " + g.getColor());
				
//				isWhiteBoardInUse = false;
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
//				Ellipse2D circle = new Ellipse2D.Float(x1, y1, 20, 20);
//				g.setColor(currentColor);
//				g.draw(circle);
////				isWhiteBoardInUse = true;
////				whiteBoardContent.add(circle);
//				
//				whiteBoardContent.put(circle, currentColor);
//				
				
//				isWhiteBoardInUse = false;
			}
			else if (mode == Mode.OVAL) {
				Ellipse2D oval = new Ellipse2D.Float(x1, y1, 10, 20);
				g.setColor(currentColor);
				g.draw(oval);
//				isWhiteBoardInUse = true;
//				whiteBoardContent.add(oval);
				
				whiteBoardContent.put(oval, currentColor);
				
//				isWhiteBoardInUse = false;
			}
			else if (mode == Mode.RECTANGLE) {
				Rectangle2D rectangle = new Rectangle2D.Float(x1, y1, 20, 20);
				g.setColor(currentColor);
				g.draw(rectangle);
//				isWhiteBoardInUse = true;
//				whiteBoardContent.add(rectangle);
				
				whiteBoardContent.put(rectangle, currentColor);

				
//				isWhiteBoardInUse = false;
			}
			else if (mode == Mode.TEXT) {
				String text = JOptionPane.showInputDialog(panel, "Input Text");
				if (text != null) {
					g.setColor(Color.BLACK);
					g.drawString(text, x1, y1);
					this.textList.put(new Coordinates(x1, y1), text);
				}
				
			}
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("mouse released");
		Point position = canvas.getMousePosition();
		if (position != null) {
			System.out.println("position != null");
//			position.x;
//			position.y;
			if (mode == Mode.CIRCLE) {
				
				System.out.println("in draw circle");
				float dist1 = position.x - x1;
				float dist2 = position.y - y1;
				if (dist1 < 0) {
					dist1 = (float) (0.0 - dist1);
				}
				if (dist2 < 0) {
					dist2 = (float) (0.0 - dist2); 
				}
				
				float distToUse;
				if (dist1 > dist2) {
					distToUse = dist1;
				}
				else {
					distToUse = dist2;
				}
				
				System.out.println("distance: " + distToUse);
				Ellipse2D circle = new Ellipse2D.Float(x1, y1, distToUse, distToUse);
				g.setColor(currentColor);
				g.draw(circle);
				whiteBoardContent.put(circle, currentColor);
				
				

			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void drawExistingContent(ConcurrentHashMap<Shape, Color> whiteBoardContent, ConcurrentHashMap<Coordinates, String> textList) {
		
		if (this.currentWhiteBoardSize > whiteBoardContent.size() || this.currentWhiteBoardTextSize > textList.size()) {
			g.clearRect(0, 0, 500, 272);
		}
		this.currentWhiteBoardSize = whiteBoardContent.size();
		this.currentWhiteBoardTextSize = textList.size();
		
//		for (Shape content: whiteBoardContent) {
//			g.draw(content);
//		}
		
		Iterator<ConcurrentHashMap.Entry<Shape, Color>> itr2 = ((ConcurrentHashMap<Shape, Color>) this.whiteBoardContent).entrySet().iterator();
		while (itr2.hasNext()) {
            ConcurrentHashMap.Entry<Shape, Color> entry = itr2.next();
            g.setColor(entry.getValue());
            g.draw(entry.getKey());
        }
		
//		Iterator<TextAndCoordinates> itr3 = this.textList.iterator();
//		while (itr3.hasNext()) {
//			TextAndCoordinates textAndCoordinates = (TextAndCoordinates) itr3.next();
//			g.setColor(Color.BLACK);
//			g.drawString(textAndCoordinates.getText(), textAndCoordinates.getX(), textAndCoordinates.getY());
//        }
		Iterator<ConcurrentHashMap.Entry<Coordinates, String>> itr3 = ((ConcurrentHashMap<Coordinates, String>) this.textList).entrySet().iterator();
		while (itr3.hasNext()) {
			ConcurrentHashMap.Entry<Coordinates, String> entry = itr3.next();
            g.setColor(Color.BLACK);
            g.drawString(entry.getValue(), entry.getKey().getX(), entry.getKey().getY());
        }
		
		Iterator<ConcurrentHashMap.Entry<Shape, Color>> itr = ((ConcurrentHashMap<Shape, Color>) whiteBoardContent).entrySet().iterator();
		while (itr.hasNext()) {
            ConcurrentHashMap.Entry<Shape, Color> entry = itr.next();
            g.setColor(entry.getValue());
            g.draw(entry.getKey());
        }
			
		
//		Iterator<TextAndCoordinates> itr4 = textList.iterator();
//		while (itr4.hasNext()) {
//			TextAndCoordinates textAndCoordinates = (TextAndCoordinates) itr4.next();
//			g.setColor(Color.BLACK);
//			g.drawString(textAndCoordinates.getText(), textAndCoordinates.getX(), textAndCoordinates.getY());
//        }
		
		Iterator<ConcurrentHashMap.Entry<Coordinates, String>> itr4 = ((ConcurrentHashMap<Coordinates, String>) textList).entrySet().iterator();
		while (itr4.hasNext()) {
			ConcurrentHashMap.Entry<Coordinates, String> entry = itr4.next();
            g.setColor(Color.BLACK);
            g.drawString(entry.getValue(), entry.getKey().getX(), entry.getKey().getY());
        }
	}
	
	
	private class WhiteBoardListener implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				while(true) {
						drawExistingContent(client.getRMI().getWhiteBoardContent(), client.getRMI().getTextList());
						client.getRMI().drawWhiteBoard(whiteBoardContent);
						client.getRMI().drawText(textList);
						
						textList = new ConcurrentHashMap<Coordinates, String>();
						whiteBoardContent = new ConcurrentHashMap<Shape, Color>();
						
						if (!client.getRMI().isWhiteBoardStarted() && !client.getRMI().isManager(client.getUserName())) {
//							panel.setVisible(false);
							JOptionPane.showMessageDialog(panel, "The manager has closed the white board.");
							Window win = SwingUtilities.getWindowAncestor(panel);
							win.dispose();
							return;
						}
						Thread.sleep(200);

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
