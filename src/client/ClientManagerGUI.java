/*
 * The program is written by Yifei Yu
 * Student ID: 945753
 */

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
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.SwingConstants;

import remote.TextMessage;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;



import java.awt.Canvas;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class ClientManagerGUI {

	private JFrame frame;
	private Client client;
	private JList<String> client_list;
	private String selectedUser;
	private JLabel popUpMessage;
	private JLabel errorMessage;
	private JTextField textInputField;
	private JTextArea textArea;
	private JScrollPane scrollPane_1;
	private JLabel lbl_chatWindow;

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
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(230, 128, 253, 174);
		frame.getContentPane().add(scrollPane_1);
		
		textArea = new JTextArea();
		scrollPane_1.setViewportView(textArea);
		textArea.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		textInputField = new JTextField();
		textInputField.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		textInputField.setBounds(221, 314, 203, 35);
		frame.getContentPane().add(textInputField);
		textInputField.setColumns(10);
		
		JButton btn_sendText = new JButton("Send");
		btn_sendText.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btn_sendText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btn_sendText.setBounds(425, 320, 69, 29);
		frame.getContentPane().add(btn_sendText);
		
		lbl_chatWindow = new JLabel("Chat Window:");
		lbl_chatWindow.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lbl_chatWindow.setBounds(230, 100, 137, 25);
		frame.getContentPane().add(lbl_chatWindow);
		
		
		btn_sendText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String newText = textInputField.getText();
				String userName = client.getUserName();
				textInputField.setText("");
				textArea.append(userName + ": " + newText);
				
				try {
					client.getRMI().updateTextMessages(new TextMessage(userName, newText));
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	try {
		    		int toExit = JOptionPane.showConfirmDialog(
							frame,
							String.format(
								"Are you sure you want to close the application?"),
							"Warning",
							JOptionPane.YES_NO_OPTION);
	    			if (toExit == JOptionPane.YES_OPTION) {
	    				client.getRMI().managerDisconnect();
	    				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    			}
	    			else {
	    				frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    			}
					
				} catch (RemoteException e) {
					e.printStackTrace();
				}
		    }
		});		
		
		client_list.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent arg0) {	
            	popUpMessage.setVisible(false);
            	errorMessage.setVisible(false);
            	selectedUser = client_list.getSelectedValue();
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
			try {
				while(true) {
					CopyOnWriteArrayList<TextMessage> textMessages = client.getRMI().getTextMessages();
					textArea.setText("");
					for (TextMessage text: textMessages) {
						textArea.append(text.getUserName() + ": " + text.getText() + "\n");
					}
					client_list.setListData(client.getRMI().getUserList());
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
					Thread.sleep(200);
				}
			} catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}
}
