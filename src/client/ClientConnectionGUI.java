package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingConstants;

import remote.IRemoteWhiteBoard;

//import org.json.simple.parser.ParseException;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;

public class ClientConnectionGUI {

	private JFrame frame;
	private JTextField textField_hostname;
	private JTextField textField_portnumber;
	private JTextField textField_username;
	private JLabel errorMessage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientConnectionGUI window = new ClientConnectionGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientConnectionGUI() {
		initialize();
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
		
		JLabel guiHeading = new JLabel("White Board Client");
		guiHeading.setHorizontalAlignment(SwingConstants.CENTER);
		guiHeading.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		guiHeading.setBounds(136, 6, 206, 28);
		frame.getContentPane().add(guiHeading);
		
		JLabel lblHostName = new JLabel("Host Name:");
		lblHostName.setHorizontalAlignment(SwingConstants.CENTER);
		lblHostName.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		lblHostName.setBounds(39, 101, 188, 28);
		frame.getContentPane().add(lblHostName);
		
		JLabel lblPortNumber = new JLabel("Port Number:");
		lblPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
		lblPortNumber.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		lblPortNumber.setBounds(39, 152, 188, 28);
		frame.getContentPane().add(lblPortNumber);
		
		JLabel lblUserName = new JLabel("User Name:");
		lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		lblUserName.setBounds(39, 205, 188, 28);
		frame.getContentPane().add(lblUserName);
		
		textField_hostname = new JTextField();
		textField_hostname.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		textField_hostname.setBounds(239, 102, 215, 26);
		frame.getContentPane().add(textField_hostname);
		textField_hostname.setColumns(10);
		
		textField_portnumber = new JTextField();
		textField_portnumber.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		textField_portnumber.setColumns(10);
		textField_portnumber.setBounds(239, 155, 215, 26);
		frame.getContentPane().add(textField_portnumber);
		
		textField_username = new JTextField();
		textField_username.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		textField_username.setColumns(10);
		textField_username.setBounds(239, 208, 215, 26);
		frame.getContentPane().add(textField_username);
		
		JButton button_connect = new JButton("Connect");
		button_connect.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		button_connect.setBounds(179, 285, 145, 30);
		frame.getContentPane().add(button_connect);
		
		errorMessage = new JLabel("New label");
		errorMessage.setForeground(Color.RED);
		errorMessage.setHorizontalAlignment(SwingConstants.CENTER);
		errorMessage.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		errorMessage.setBounds(6, 56, 488, 22);
		frame.getContentPane().add(errorMessage);
		errorMessage.setVisible(false);
		
		
		button_connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				errorMessage.setVisible(false);
				try {
					String hostName = textField_hostname.getText().strip();
					int portNumber = Integer.parseInt(textField_portnumber.getText().strip());
					String userName = textField_username.getText().strip();
					
					if (userName.equals("")) {
						errorMessage.setText("Username can not be empty");
						errorMessage.setVisible(true);
						textField_username.setText("");
					}else {
					
						Registry registry = LocateRegistry.getRegistry(hostName, portNumber);
						IRemoteWhiteBoard remoteWhiteBoard = (IRemoteWhiteBoard) registry.lookup("whiteBoard");
						
						Client client = new Client(hostName, portNumber, userName, registry, remoteWhiteBoard);
						
						if (!client.getRMI().userNameAlreadyExist(userName)) {
							
							
							if (!client.getRMI().hasManager()) {
								client.getRMI().addNewUser(userName);
								new ClientManagerGUI(client);
								frame.dispose();
							}
							else {
								client.getRMI().addClientToWaitList(userName);
								
								textField_hostname.setEnabled(false);
								textField_portnumber.setEnabled(false);
								textField_username.setEnabled(false);
								button_connect.setEnabled(false);
								
								JOptionPane.showMessageDialog(frame, "Waiting for the manager to let you in.");
								while (client.getRMI().isClientInWaitList(userName)) {
									TimeUnit.SECONDS.sleep(1);
								}
								if (!client.getRMI().isClientInDeclinedList(userName)) {
									new ClientConnectedGUI(client);
									frame.dispose();
								}
								else {
									errorMessage.setText("You are not allowed to enter the white board");
									errorMessage.setVisible(true);
								}
							}
							
							textField_hostname.setEnabled(true);
							textField_portnumber.setEnabled(true);
							textField_username.setEnabled(true);
							button_connect.setEnabled(true);
							
						}
						else {
							errorMessage.setText("Username already taken, please choose another one");
							errorMessage.setVisible(true);
							textField_username.setText("");
	
						}
					}
					
				}
				catch(NumberFormatException e1) {
					errorMessage.setText("Please enter the correct port number");
					errorMessage.setVisible(true);
					textField_hostname.setText("");
					textField_portnumber.setText("");
					textField_username.setText("");
				}
				catch(RemoteException e1) {
					errorMessage.setText("Server not found");
					errorMessage.setVisible(true);
					textField_hostname.setText("");
					textField_portnumber.setText("");
					textField_username.setText("");
				}
				catch(Exception e1) {
					e1.printStackTrace();
					errorMessage.setText("Could not connect, please try again");
					errorMessage.setVisible(true);
					textField_hostname.setText("");
					textField_portnumber.setText("");
					textField_username.setText("");
				}

				
			}
		});
			
	}
}
