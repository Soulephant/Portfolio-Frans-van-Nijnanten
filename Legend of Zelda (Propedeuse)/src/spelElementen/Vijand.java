package spelElementen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import system.Veld;

public class Vijand extends SpelElement
{
	private Veld veld;
	
	public Vijand(int x, int y, int width, int height, Veld veld)
	{
		this.veld=veld;
		this.setLocation(x,y);
		this.setSize(width,height);
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setColor(Color.RED);
		g.fillRect(0, 0, width, height);
	}
}
