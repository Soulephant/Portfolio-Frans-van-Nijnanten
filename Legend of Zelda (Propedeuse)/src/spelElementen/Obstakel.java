package spelElementen;

import java.awt.Color;
import java.awt.Graphics;

public class Obstakel extends SpelElement
{
	private int x,y,width,height;
	
	public Obstakel(int x, int y, int width, int height)
	{
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		setLocation(x,y);
		setSize(width,height);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawRect(0, 0, getWidth()-1, getHeight()-1);
	}
}
