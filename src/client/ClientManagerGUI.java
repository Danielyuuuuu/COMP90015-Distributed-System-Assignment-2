package client;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;



import java.awt.Canvas;
import java.awt.Graphics;
import javax.swing.JFrame;


public class ClientManagerGUI {

	private JFrame frame;
	private Client client;
	private JList<String> client_list;
	private String selectedUser;
	private JLabel popUpMessage;
	private JLabel errorMessage;
	
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
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
	
	
//	public void loadGUI(Client client) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ClientManagerGUI window = new ClientManagerGUI(client);
//					window.frame.setVisible(true);
////					this.client = client;
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public ClientManagerGUI(Client client) {
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
		frame.getContentPane().setForeground(Color.BLACK);
		frame.setBounds(100, 100, 500, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lbl_heading = new JLabel("White Board Manager");
		lbl_heading.setBounds(147, 6, 203, 25);
		lbl_heading.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_heading.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		frame.getContentPane().add(lbl_heading);
		
		JButton btn_newWhiteBoard = new JButton("New White Board");
		btn_newWhiteBoard.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		btn_newWhiteBoard.setBounds(6, 79, 212, 29);
		frame.getContentPane().add(btn_newWhiteBoard);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(16, 162, 193, 187);
		frame.getContentPane().add(scrollPane);
		
		client_list = new JList<String>();
		client_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		client_list.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		scrollPane.setViewportView(client_list);
		
		JButton btn_kick = new JButton("Kick User");
		btn_kick.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		btn_kick.setBounds(6, 121, 212, 29);
		frame.getContentPane().add(btn_kick);
		btn_kick.setEnabled(false);
		
		popUpMessage = new JLabel("New label");
		popUpMessage.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		popUpMessage.setHorizontalAlignment(SwingConstants.CENTER);
		popUpMessage.setForeground(Color.MAGENTA);
		popUpMessage.setBounds(6, 43, 488, 24);
		frame.getContentPane().add(popUpMessage);
		popUpMessage.setVisible(false);
		
		errorMessage = new JLabel("New label");
		errorMessage.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		errorMessage.setHorizontalAlignment(SwingConstants.CENTER);
		errorMessage.setForeground(Color.RED);
		errorMessage.setBounds(6, 43, 488, 24);
		frame.getContentPane().add(errorMessage);
		errorMessage.setVisible(false);
		
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	try {
					client.getRMI().managerDisconnect();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});		
		
		client_list.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent arg0) {	
            	popUpMessage.setVisible(false);
            	errorMessage.setVisible(false);
            	selectedUser = client_list.getSelectedValue();
            	System.out.println("Selected user: " + selectedUser);
//            	client_list.setSel
            	if(selectedUser.equals(client.getUserName())) {
            		btn_kick.setEnabled(false);
            	}
            	else {
            		btn_kick.setEnabled(true);
            	}
            }
        });
		
		
		btn_newWhiteBoard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (client.getRMI().isWhiteBoardStarted()) {
						JOptionPane.showMessageDialog(frame, "You have already created a white board.");
					}
					else {
						client.getRMI().startWhiteBoard();
						new WhiteBoard(client).setVisible(true);
					}
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		
		btn_kick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				popUpMessage.setVisible(false);
            	errorMessage.setVisible(false);
				System.out.println("The selected user: " + selectedUser);
				
				try {
					if (client.getRMI().kickUser(selectedUser)) {
						popUpMessage.setText("User: " + selectedUser + " has been kicked");
						popUpMessage.setVisible(true);
					}
					else {
						errorMessage.setText("User: " + selectedUser + " does not exist");
						errorMessage.setVisible(true);
					}
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					popUpMessage.setVisible(false);
	            	errorMessage.setVisible(true);
	            	errorMessage.setText("User: " + selectedUser + " does not exist");
	            	
				}
			}
		});
	}
	
	private class ClientListener implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				while(true) {
					client_list.setListData(client.getRMI().getUserList());
//					System.out.println(client_list.toString());
					List<String> clientWaitList = client.getRMI().getClientsWaitList();
					if (clientWaitList.size() > 0) {
						int allowToJoin = JOptionPane.showConfirmDialog(
								frame,
								String.format(
									"User %s wants to join the whiteboard.",
									clientWaitList.get(0)),
								"New Connection",
								JOptionPane.YES_NO_OPTION);
						if (allowToJoin == JOptionPane.YES_OPTION) {
							client.getRMI().acceptClient(clientWaitList.get(0));
						}
						else {
							client.getRMI().declineClient(clientWaitList.get(0));
						}
					}
					Thread.sleep(400);
				}
			} catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
}
