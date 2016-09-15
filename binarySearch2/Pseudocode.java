package binarySearch;

import java.awt.Font;
import java.awt.Graphics;

public class Pseudocode {
	
	private String[] code;
	private int x, y, linespace = 30;
	private Font font;
	
	public Pseudocode(String[] str, int i, int j) {
		// TODO Auto-generated constructor stub
		code = str;
		x = i;
		y = j;
	}
	
	public void paint(Graphics g) {
		font = new Font("Serif", Font.BOLD, 15);
		g.setFont(font);
		for(int i=0;i<code.length;i++) {
			if(i==4 || i==5 || i==7 || i==9 || i==11)
				g.drawString(code[i], x + 50, y + linespace*i);
			else if (i==6 || i==8 || i==10)
				g.drawString(code[i], x + 75, y + linespace*i);
			else if (i==0 || i==14)
				g.drawString(code[i], x, y + linespace*i);
			else
				g.drawString(code[i], x + 25, y + linespace*i);
		}				
	}
}
