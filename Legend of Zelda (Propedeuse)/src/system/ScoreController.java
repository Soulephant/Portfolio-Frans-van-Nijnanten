package system;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class ScoreController extends JPanel
{
	private LevelPaneel level;
	
	public ScoreController(LevelPaneel level)
	{
		this.level=level;
		this.setLocation(0,0);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawString("This is the score controller", 10, 10);
	}
}
