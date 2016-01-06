package system;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import binary.Opstart;
import libraries.Button;

public class MenuPaneel extends Paneel
{
	//Attributes found in every menu
	protected Opstart frame;
	protected ArrayList<Button>knoppenLijst;
	protected String label;
	protected int keyPosition, xPos, yPos;
	protected ImageIcon logo,background,text;
	
	public MenuPaneel()
	{
		setLayout(null);
		knoppenLijst = new ArrayList<Button>();
		setFocusable(true);
		requestFocus();
		logo = new ImageIcon(MenuPaneel.class.getResource("/resources/images/logo.png"));
		background = new ImageIcon(MenuPaneel.class.getResource("/resources/images/menu.png"));
		keyPosition=0;//Set default key position to 0 
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponents(g);
	}
	
	public void drawLogo(Graphics g)
	{
		g.drawImage(background.getImage(),(getWidth()/2-300),(getHeight()/2-300),this);
	}
}