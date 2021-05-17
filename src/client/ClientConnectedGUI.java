package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.SwingConstants;

import remote.TextMessage;

import javax.swing.JButton;
import java.awt.Color;

public class ClientConnectedGUI {

	private JFrame frame;
	private Client client;
	private JLabel lbl_error;
	private JList<String> client_list;
	private Boolean isWhiteBoardOpened = false;
	private JTextField textInputField;
	private JTextArea textArea;
	private JScrollPane scrollPane_1;
	private JLabel lbl_chatWindow;
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
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(16, 162, 193, 187);
		frame.getContentPane().add(scrollPane);
		
		client_list = new JList<String>();
		client_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		client_list.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		scrollPane.setViewportView(client_list);
		
		JLabel lbl_userList = new JLabel("User List:");
		lbl_userList.setHorizontalAlignment(SwingConstants.LEFT);
		lbl_userList.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lbl_userList.setBounds(16, 136, 97, 25);
		frame.getContentPane().add(lbl_userList);
		
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
	    				client.getRMI().clientDisconnect(client.getUserName());
	    				isWhiteBoardOpened = false;
	    				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    			}
	    			else {
	    				frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    			}
					
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});	
		
		btn_joinWhiteBoard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (client.getRMI().isWhiteBoardStarted() && !isWhiteBoardOpened) {
						isWhiteBoardOpened = true;
						WhiteBoard whiteBoard = new WhiteBoard(client);
						whiteBoard.setVisible(true);
					}
					else if (isWhiteBoardOpened) {
						JOptionPane.showMessageDialog(frame, "You have already opened the white board.");
					}
					else {
						JOptionPane.showMessageDialog(frame, "The manager hasn't created a white board.");
					}
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
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
					CopyOnWriteArrayList<TextMessage> textMessages = client.getRMI().getTextMessages();
					textArea.setText("");
					for (TextMessage text: textMessages) {
						textArea.append(text.getUserName() + ": " + text.getText() + "\n");
					}
					client_list.setListData(client.getRMI().getUserList());
					
					client_list.setListData(client.getRMI().getUserList());
					if (client.getRMI().haveIBeenKicked(client.getUserName())) {
						lbl_error.setText("You have been kicked!");
						lbl_error.setVisible(true);
						JOptionPane.showMessageDialog(frame, "You have been kicked!");
						isWhiteBoardOpened = false;
						System.exit(0);
					}
					else if(client.getRMI().isManagerDisconnected()) {
						lbl_error.setText("The manager has ended the connection");
						lbl_error.setVisible(true);
						JOptionPane.showMessageDialog(frame, "The manager has ended the connection.");
						isWhiteBoardOpened = false;
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
