package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;

public class ClientManagerGUI {

	private JFrame frame;

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
	
	
	public void loadGUI() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientManagerGUI window = new ClientManagerGUI();
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
	public ClientManagerGUI() {
		initialize();
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
	}

}
