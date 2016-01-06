package libraries;

import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import spelElementen.Obstakel;
import spelElementen.SpelElement;
import system.Veld;

public class Map extends JPanel
{
	private ImageIcon map;
	private int width, height, x, y;
	private String mapString;
	
	public Map(ArrayList<SpelElement> elementenLijst,Veld veld)
	{
		mapString="map1";
		setMap(mapString);
		width = 990;
		height = 1030;
		x = -97;
		y = -315;
	}
	
	public String getMap()
	{
		return mapString;
	}
	
	public void setMap(String mapString)
	{
		this.mapString=mapString;
		map = new ImageIcon(Map.class.getResource("/resources/images/maps/"+mapString+".png"));
		if(mapString=="map3")
		{
			width=1980;
			height=2060;
		}
		else if(mapString!="map3")
		{
			width=990;
			height=1030;
		}
	}
	
	public int getWidthVal()
	{
		return width;
	}
	
	public int getHeightVal()
	{
		return height;
	}
	
	public int getYPos()
	{
		return(y);
	}
	
	public void setYPos(int y)
	{
		this.y=y;
	}
	
	public int getXPos()
	{
		return(x);
	}
	
	public void setXPos(int x)
	{
		this.x=x;
	}
	
	public void drawMap(Graphics g)
	{
		g.drawImage(map.getImage(),x,y,width,height,this);
	}
}
