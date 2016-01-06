package spelElementen;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class SpelElement extends JPanel
{
	protected int x,y,height,width,direction;
	protected boolean move;
	protected static final int UP=0,DOWN=1,LEFT=2,RIGHT=3;
	
	public SpelElement()
	{
		setOpaque(false);
	}
	
	public void move(int dir)
	{
		this.direction=dir;
		switch(dir)
		{
			case UP:
				y-=2;
				break;
			case DOWN:
				y+=2;
				break;
			case LEFT:
				x-=2;
				break;
			case RIGHT:
				x+=2;
				break;
		}
		setLocation(x,y);
	}
	
	public int getDir()
	{
		return direction;
	}
}
