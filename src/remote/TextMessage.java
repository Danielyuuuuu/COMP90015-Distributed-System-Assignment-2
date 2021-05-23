/*
 * The program is written by Yifei Yu
 * Student ID: 945753
 */

package remote;

import java.io.Serializable;

public class TextMessage implements Serializable{
	private final String userName;
	private final String text;
	
	public TextMessage(String userName, String text) {
		this.userName = userName;
		this.text = text;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getText() {
		return text;
	}
}
