package spelElementen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import libraries.Controller;
import libraries.Map;
import system.Veld;

public class Link extends SpelElement
{
	private ImageIcon link;
	private Veld veld;
	private Controller control;
	
	public Link(Veld veld, int x, int y, int width, int height)
	{
		this.setLocation(x,y);
		this.setSize(width,height);
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.veld=veld;
		link = new ImageIcon(Link.class.getResource("/resources/images/link/down.gif"));
	}
	
	public void setLocation(int x, int y)
	{
		super.setLocation(x, y);
		this.x=x;
		this.y=y;
	}
	
	public void toggleMove(boolean move)
	{
		if(move==false)
		{
			switch(direction)
			{
				case UP:
					link = new ImageIcon(Link.class.getResource("/resources/images/link/up.gif"));
					break;
				case DOWN:
					link = new ImageIcon(Link.class.getResource("/resources/images/link/down.gif"));
					break;
				case LEFT:
					link = new ImageIcon(Link.class.getResource("/resources/images/link/left.gif"));
					width = link.getIconWidth();
					height = link.getIconHeight();
					break;
				case RIGHT:
					link = new ImageIcon(Link.class.getResource("/resources/images/link/right.gif"));
					width = link.getIconWidth();
					height = link.getIconHeight();
					break;
			}
			repaint();
		}
		else if(move==true)
		{
			switch(direction)
			{
				case UP:
					link = new ImageIcon(Link.class.getResource("/resources/images/link/upMove.gif"));
					break;
				case DOWN:
					link = new ImageIcon(Link.class.getResource("/resources/images/link/downMove.gif"));
					break;
				case LEFT:
					break;
				case RIGHT:
					link = new ImageIcon(Link.class.getResource("/resources/images/link/rightMove.gif"));
					width = link.getIconWidth();
					height = link.getIconHeight();
					break;
			}
			repaint();
		}
	}
	
	public void move(int dir)
	{
		super.move(dir);
		this.direction=dir;
		//control.detectCollision(veld.getElementen(), this);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(link.getImage(),0,0,width,height,this);
	}
}