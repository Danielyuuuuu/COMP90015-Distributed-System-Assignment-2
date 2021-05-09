package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;

public class ClientConnectedGUI {

	private JFrame frame;
	private Client client;

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
		btn_joinWhiteBoard.setBounds(6, 73, 219, 29);
		frame.getContentPane().add(btn_joinWhiteBoard);
	}

}
