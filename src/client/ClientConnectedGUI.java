package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.Color;

public class ClientConnectedGUI {

	private JFrame frame;
	private Client client;
	private JLabel lbl_error;
	/**
	 * Launch the application.
	 */	
//	public void loadGUI() {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ClientConnectedGUI window = new ClientConnectedGUI();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
	
	
	/**
	 * Create the application.
	 */
	public ClientConnectedGUI(Client client) {
		this.client = client;
		initialize();
		frame.setVisible(true);
		
		new Thread(new ClientListener()).start();
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lbl_heading = new JLabel("White Board Client");
		lbl_heading.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_heading.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		lbl_heading.setBounds(133, 6, 226, 25);
		frame.getContentPane().add(lbl_heading);
		
		JButton btn_joinWhiteBoard = new JButton("Join White Board");
		btn_joinWhiteBoard.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		btn_joinWhiteBoard.setBounds(6, 84, 219, 29);
		frame.getContentPane().add(btn_joinWhiteBoard);
		
		lbl_error = new JLabel("New label");
		lbl_error.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_error.setForeground(Color.RED);
		lbl_error.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lbl_error.setBounds(6, 43, 488, 29);
		frame.getContentPane().add(lbl_error);
		lbl_error.setVisible(false);
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	try {
					client.getRMI().clientDisconnect(client.getUserName());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});	
		
		btn_joinWhiteBoard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WhiteBoard whiteBoard = new WhiteBoard(client);
				whiteBoard.setVisible(true);
			}
		});
	}
	
	private class ClientListener implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				while(true) {
					if (client.getRMI().haveIBeenKicked(client.getUserName())) {
						lbl_error.setText("You have been kicked!");
						lbl_error.setVisible(true);
						JOptionPane.showMessageDialog(frame, "You have been kicked!");
						System.exit(0);
					}
					else if(client.getRMI().isManagerDisconnected()) {
						lbl_error.setText("The manager has ended the connection");
						lbl_error.setVisible(true);
						JOptionPane.showMessageDialog(frame, "The manager has ended the connection.");
						System.exit(0);
					}
					Thread.sleep(400);
				}
			} catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}
}
