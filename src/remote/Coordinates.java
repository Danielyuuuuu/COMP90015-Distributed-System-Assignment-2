package remote;

import java.io.Serializable;

public class Coordinates implements Serializable{
	private final Integer x;
	private final Integer y;
	
	public Coordinates(Integer x, Integer y) {
		this.x = x;
		this.y = y;
	}
	
	
	public Integer getX() {
		return x;
	}
	
	public Integer getY() {
		return y;
	}
}
