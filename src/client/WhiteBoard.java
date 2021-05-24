/*
 * The program is written by Yifei Yu
 * Student ID: 945753
 */

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
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
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
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import remote.Coordinates;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;


public class WhiteBoard extends JFrame implements MouseListener, MouseMotionListener{

	private JPanel panel;
	private Canvas canvas;
	private int x1, y1, x2, y2;
	private ConcurrentHashMap<Shape, Color> whiteBoardContent = new ConcurrentHashMap<>();
	
	protected Graphics2D g;
	private Client client;
	private Mode mode = Mode.LINE;
	private int currentWhiteBoardSize = 0;
	private int currentWhiteBoardTextSize = 0;
	private int currentImageSize = 0;

	private Color currentColor = Color.BLACK;
	
	private ConcurrentHashMap<Coordinates, String> textList = new ConcurrentHashMap<>();
//	private Boolean toLoadSavedImage = false;
//	BufferedImage image;

	/**
	 * Create the frame.
	 */
	public WhiteBoard(Client client) {
		
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
		
		JButton btn_pen = new JButton("Pen");
		btn_pen.setBounds(129, 36, 117, 29);
		panel.add(btn_pen);
		
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
		
		JButton btn_line = new JButton("Line");
		btn_line.setBounds(0, 6, 117, 29);
		panel.add(btn_line);
		
		setVisible(true);
		g = (Graphics2D)canvas.getGraphics();
		

		
		Thread thread = new Thread(new WhiteBoardListener());
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	try {
		    		if (client.getRMI().isManager(client.getUserName())) {
		    			int toSave = JOptionPane.showConfirmDialog(
								panel,
								String.format(
									"Do you want to save the whiteboard?"),
								"Warning",
								JOptionPane.YES_NO_OPTION);
		    			if (toSave == JOptionPane.YES_OPTION) {
//		    				System.out.println("client.setWhiteBoardClosed();");
//		    				System.out.println("client.isWhiteBoardOpened(): " + client.isWhiteBoardOpened());
		    				thread.stop();
//		    				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		    				
		    				BufferedImage image = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
		    				Graphics2D g2 = (Graphics2D)image.getGraphics();
		    				g2.setBackground(Color.WHITE);
		    				g2.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		    				drawImage(g2, client.getRMI().getWhiteBoardContent(), client.getRMI().getTextList(), client.getRMI().getPreviousDrawingImage());
		    				try {
		    					ImageIO.write(image, "png", new File("canvas.png"));
		    				} catch (Exception e) {
		    					e.printStackTrace();
		    				}
		    				client.getRMI().resetWhiteBoard();
		    				client.getRMI().closeWhiteBoard();
		    				client.setWhiteBoardClosed();
		    			}
		    			else {
//		    				setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		    				thread.stop();
		    				client.getRMI().resetWhiteBoard();
		    				client.getRMI().closeWhiteBoard();
		    				client.setWhiteBoardClosed();
		    			}
		    		}
		    		else {
		    			client.setWhiteBoardClosed();
		    			thread.stop();
		    		}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
		    }
		});	
		
		btn_pen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mode = Mode.PEN;
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
//					byte[] imageByte = client.getRMI().getPreviousDrawingImage();
//					if (imageByte != null) {
//						BufferedImage bi;
//						try {
//							bi = javax.imageio.ImageIO.read(new ByteArrayInputStream(imageByte));
//							g.drawImage(bi, 0, 0, null);
//						} catch (IOException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
//					}
				} catch (RemoteException e1) {
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
		
		thread.start();
		
		
//		try {
//			g.clearRect(0, 0, 500, 272);
//			BufferedImage bi;
//			bi = ImageIO.read(new File("canvas.png"));
//			g.drawImage(bi, 0, 0, null);
//			System.out.println("Draw image");
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} 
		

	}
	
	@Override
	public void mouseDragged(MouseEvent e){
		Point position = canvas.getMousePosition();
		
		if(position!=null) {
			if (mode == Mode.PEN) {
				x2 = position.x;
				y2 = position.y;
				Line2D line = new Line2D.Float(x1, y1, x2, y2);
				g.setColor(currentColor);
				g.draw(line);

				whiteBoardContent.put(line, g.getColor());

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
			
			if (mode == Mode.TEXT) {
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
		Point position = canvas.getMousePosition();
		if (position != null) {

			if (mode == Mode.CIRCLE) {
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
				
				Ellipse2D circle = new Ellipse2D.Float(x1, y1, distToUse, distToUse);
				g.setColor(currentColor);
				g.draw(circle);
				whiteBoardContent.put(circle, currentColor);
			}
			else if (mode == Mode.OVAL) {
				float dist1 = position.x - x1;
				float dist2 = position.y - y1;
				if (dist1 < 0) {
					dist1 = (float) (0.0 - dist1);
				}
				if (dist2 < 0) {
					dist2 = (float) (0.0 - dist2); 
				}
					
				Ellipse2D oval = new Ellipse2D.Float(x1, y1, dist1, dist2);
				g.setColor(currentColor);
				g.draw(oval);
				
				whiteBoardContent.put(oval, currentColor);
			}
			else if (mode == Mode.RECTANGLE) {
				float dist1 = position.x - x1;
				float dist2 = position.y - y1;
				if (dist1 < 0) {
					dist1 = (float) (0.0 - dist1);
				}
				if (dist2 < 0) {
					dist2 = (float) (0.0 - dist2); 
				}
				Rectangle2D rectangle = new Rectangle2D.Float(x1, y1, dist1, dist2);
				g.setColor(currentColor);
				g.draw(rectangle);
				whiteBoardContent.put(rectangle, currentColor);

			}
			else if (mode == Mode.LINE) {
				Line2D line = new Line2D.Float(x1, y1, position.x, position.y);
				g.setColor(currentColor);
				g.draw(line);
				whiteBoardContent.put(line, g.getColor());
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
	
	public void drawExistingContent(ConcurrentHashMap<Shape, Color> whiteBoardContent, ConcurrentHashMap<Coordinates, String> textList, byte[] imageByte) {
		
		if (this.currentWhiteBoardSize > whiteBoardContent.size() || this.currentWhiteBoardTextSize > textList.size()  || (imageByte == null && this.currentImageSize != 0)) {
			System.out.println("reset whiteboard");
			g.clearRect(0, 0, 500, 272);
//			if (imageByte != null) {
//				BufferedImage bi;
//				try {
//					bi = javax.imageio.ImageIO.read(new ByteArrayInputStream(imageByte));
//					g.drawImage(bi, 0, 0, null);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
		}
		this.currentWhiteBoardSize = whiteBoardContent.size();
		this.currentWhiteBoardTextSize = textList.size();
		if (imageByte != null) {
			this.currentImageSize = imageByte.length;
		}
		else {
			this.currentImageSize = 0;
		}
		
		

		
		Iterator<ConcurrentHashMap.Entry<Shape, Color>> itr2 = ((ConcurrentHashMap<Shape, Color>) this.whiteBoardContent).entrySet().iterator();
		while (itr2.hasNext()) {
            ConcurrentHashMap.Entry<Shape, Color> entry = itr2.next();
            g.setColor(entry.getValue());
            g.draw(entry.getKey());
        }

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
			
		Iterator<ConcurrentHashMap.Entry<Coordinates, String>> itr4 = ((ConcurrentHashMap<Coordinates, String>) textList).entrySet().iterator();
		while (itr4.hasNext()) {
			ConcurrentHashMap.Entry<Coordinates, String> entry = itr4.next();
            g.setColor(Color.BLACK);
            g.drawString(entry.getValue(), entry.getKey().getX(), entry.getKey().getY());
        }
	}
	
	public void drawImage(Graphics2D g2, ConcurrentHashMap<Shape, Color> whiteBoardContent, ConcurrentHashMap<Coordinates, String> textList, byte[] imageByte) {
		
		if (imageByte != null) {
			BufferedImage bi;
			try {
				bi = javax.imageio.ImageIO.read(new ByteArrayInputStream(imageByte));
				g2.drawImage(bi, 0, 0, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Iterator<ConcurrentHashMap.Entry<Shape, Color>> itr2 = ((ConcurrentHashMap<Shape, Color>) this.whiteBoardContent).entrySet().iterator();
		while (itr2.hasNext()) {
            ConcurrentHashMap.Entry<Shape, Color> entry = itr2.next();
            g2.setColor(entry.getValue());
            g2.draw(entry.getKey());
        }

		Iterator<ConcurrentHashMap.Entry<Coordinates, String>> itr3 = ((ConcurrentHashMap<Coordinates, String>) this.textList).entrySet().iterator();
		while (itr3.hasNext()) {
			ConcurrentHashMap.Entry<Coordinates, String> entry = itr3.next();
            g2.setColor(Color.BLACK);
            g2.drawString(entry.getValue(), entry.getKey().getX(), entry.getKey().getY());
        }
		
		Iterator<ConcurrentHashMap.Entry<Shape, Color>> itr = ((ConcurrentHashMap<Shape, Color>) whiteBoardContent).entrySet().iterator();
		while (itr.hasNext()) {
            ConcurrentHashMap.Entry<Shape, Color> entry = itr.next();
            g2.setColor(entry.getValue());
            g2.draw(entry.getKey());
        }
			
		Iterator<ConcurrentHashMap.Entry<Coordinates, String>> itr4 = ((ConcurrentHashMap<Coordinates, String>) textList).entrySet().iterator();
		while (itr4.hasNext()) {
			ConcurrentHashMap.Entry<Coordinates, String> entry = itr4.next();
            g2.setColor(Color.BLACK);
            g2.drawString(entry.getValue(), entry.getKey().getX(), entry.getKey().getY());
        }
	}
	
//	public void setToLoadPreviousImage() {
//		this.toLoadSavedImage = true;
//	}
	
//	public void drawImageOnCanvas(BufferedImage bi) {
//		g.drawImage(bi, 0, 0, null);
//	}
	
	private class WhiteBoardListener implements Runnable{

		@Override
		public void run() {
			try {
				if (client.getRMI().toLoadPreviousImage()) {
					Thread.sleep(1000);					
					byte[] imageByte = client.getRMI().getPreviousDrawingImage();
					if (imageByte != null) {
						currentImageSize = imageByte.length;
						BufferedImage bi;
						try {
							bi = javax.imageio.ImageIO.read(new ByteArrayInputStream(imageByte));
							g.drawImage(bi, 0, 0, null);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				while(true) {
						drawExistingContent(client.getRMI().getWhiteBoardContent(), client.getRMI().getTextList(), client.getRMI().getPreviousDrawingImage());
						client.getRMI().drawWhiteBoard(whiteBoardContent);
						client.getRMI().drawText(textList);
						
						textList = new ConcurrentHashMap<Coordinates, String>();
						whiteBoardContent = new ConcurrentHashMap<Shape, Color>();
						
						if (!client.getRMI().isWhiteBoardStarted() && !client.getRMI().isManager(client.getUserName()) && client.isWhiteBoardOpened()) {
							JOptionPane.showMessageDialog(panel, "The manager has closed the white board.");
							Window win = SwingUtilities.getWindowAncestor(panel);
							client.setWhiteBoardClosed();
							win.dispose();
							return;
						}
						Thread.sleep(200);

				}
			}catch(ConcurrentModificationException e1) {
				e1.printStackTrace();
			}
			catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}
}
