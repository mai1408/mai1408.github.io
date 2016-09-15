package binarySearch;

import java.awt.Color;
import java.awt.Graphics;

public class Bar {
	
	private int x, y, width = 15, height;
	private int position;
	
	public Bar(int i, int j, int a) {
		// TODO Auto-generated constructor stub
		x = i;
		y = j;
		height = a;
	}
	
	public void paint(Graphics g) {
		// TODO Auto-generated method stub		
		if(getPosition() == 0)
			g.setColor(Color.GREEN);
		else
			g.setColor(Color.RED);
		g.fillRect(x, y, width, height);
		g.setColor(Color.BLACK);
		if(Math.abs(height) < 10)
			if(height == 0)
				g.drawString("null", x+(width/4), y-5);
			else		
				g.drawString(Integer.toString(height), x+(width/4), y-5);
		else
			g.drawString(Integer.toString(height), x, y-5);
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	

}
