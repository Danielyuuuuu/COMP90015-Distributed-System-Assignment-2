package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
	private ClientConnectedGUI clientConnectedGUI;

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
		
		JLabel errorMessage = new JLabel("New label");
		errorMessage.setForeground(Color.RED);
		errorMessage.setHorizontalAlignment(SwingConstants.CENTER);
		errorMessage.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		errorMessage.setBounds(6, 56, 488, 22);
		frame.getContentPane().add(errorMessage);
		errorMessage.setVisible(false);
		
		button_connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String hostName = textField_hostname.getText().strip();
					int portNumber = Integer.parseInt(textField_portnumber.getText().strip());
					String userName = textField_username.getText().strip();
					
					//Connect to the rmiregistry that is running on localhost
					Registry registry = LocateRegistry.getRegistry("localhost", 3333);
		           
					//Retrieve the stub/proxy for the remote math object from the registry
					IRemoteWhiteBoard remoteWhiteBoard = (IRemoteWhiteBoard) registry.lookup("whiteBoard");
					
					System.out.println("Client: calling remote methods");
					remoteWhiteBoard.printHello();
					
					clientConnectedGUI = new ClientConnectedGUI();
					clientConnectedGUI.loadGUI();
					frame.dispose();
					
				}
				catch(Exception e1) {
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
