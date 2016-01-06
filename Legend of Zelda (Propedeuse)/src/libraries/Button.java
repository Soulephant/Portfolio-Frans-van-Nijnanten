package libraries;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Button extends JPanel
{
	private int xPos, yPos;
	private String label;
	private ImageIcon focusIMG;
	private boolean focus;
	
	public Button(int xPos, int yPos, String label)
	{
		this.xPos = xPos;
		this.yPos = yPos;
		this.label = label;
		focusIMG = new ImageIcon(Button.class.getResource("/resources/images/overlay.png"));
	}
	
	public void setFocus(boolean focus)
	{
		this.focus=focus;
	}
	
	public void setLabel(String label)
	{
		this.label=label;
	}
	
	public void drawButton(Graphics g)
	{
		if(focus==true)
		{
			g.drawImage(focusIMG.getImage(), xPos-25, yPos, this);
		}
		
	}
}
