package peterCanvas;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JTextArea;

public class ClientBoard extends Board {

	private JPanel contentPane;
	
	private String username;
	
	private String managerName;
	
	private ass2Remote remoteMethods;
	
	public ClientBoard(String usernameIn,String managerNameIn,ass2Remote remoteMethodsIn) {
		super();
		
		username=usernameIn;
		managerName=managerNameIn;
		remoteMethods=remoteMethodsIn;
		
		
		setTitle("Client board  "+usernameIn);
		getContentPane().setLayout(null);
		
		Thread t = new Thread(() -> {
			while(true) {
				try {
					if(remoteMethods.getMainBoardInfo(managerName)!=null) {
						ArrayList<Shape> mainList = remoteMethods.getMainBoardInfo(managerName);
						g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
						for(Shape s:mainList) {
							g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
							g.draw(s);
						}
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		t.start();
		
	}
		
	
	public String getClientName() {
		return username;
	}
	
	@Override
	public void mouseDragged(MouseEvent e){
		
		Point p = canvas.getMousePosition();
		try {
			if(mode.equals("line")) {
				if(p!=null) {
					x2 = p.x;
					y2 = p.y;
					Line2D a = new Line2D.Float(x1, y1, x2, y2);	

					remoteMethods.paintOnMainBoard(a, managerName);
					
					x1 = x2;
					y1 = y2;
				}
			}else if(mode.equals("eraser")) {
				Rectangle2D r = new Rectangle2D.Float(p.x-2,p.y-2,4,4);
				ArrayList<Shape> mainList = remoteMethods.getMainBoardInfo(managerName);
				for(Shape s :mainList) {
					if(s.intersects(r)) {
						remoteMethods.removeOnMainBoard(s, managerName);
					}
				}
			}
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void mousePressed(MouseEvent e) {
		Point p = canvas.getMousePosition();
		try {
			if(mode.equals("line")) {
				x1 = p.x;
				y1 = p.y;
			}else if(mode.equals("eraser")) {
				Rectangle2D r = new Rectangle2D.Float(p.x-2,p.y-2,4,4);
				ArrayList<Shape> mainList = remoteMethods.getMainBoardInfo(managerName);
				for(Shape s :mainList) {
					if(s.intersects(r)) {
						remoteMethods.removeOnMainBoard(s, managerName);
					}
				}
			}
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		try {
			if(mode.equals("eraser")) {
				Point p = canvas.getMousePosition();
				ArrayList<Shape> mainList = remoteMethods.getMainBoardInfo(managerName);
				if(mainList.contains(eraser)) {
					remoteMethods.removeOnMainBoard(eraser, managerName);
					eraser = new Rectangle2D.Float(p.x-5,p.y-5,10,10);
					remoteMethods.paintOnMainBoard(eraser, managerName);
				}else {
					eraser = new Rectangle2D.Float(p.x-5,p.y-5,10,10);
					remoteMethods.paintOnMainBoard(eraser, managerName);
				}
			}
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
		

}
