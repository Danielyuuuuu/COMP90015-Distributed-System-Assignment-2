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

import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

public class ClientManagerGUI {

	private JFrame frame;
	private Client client;
	private JList<String> client_list;
	private String selectedUser;
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
		
		client_list.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent arg0) {	
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
		
		btn_kick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("The selected user: " + selectedUser);
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
					Thread.sleep(400);
				}
			} catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}
}
