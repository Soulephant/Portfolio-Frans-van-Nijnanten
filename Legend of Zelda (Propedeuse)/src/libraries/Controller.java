package libraries;

import java.awt.Rectangle;
import java.util.ArrayList;

import spelElementen.Link;
import spelElementen.Obstakel;
import spelElementen.SpelElement;
import spelElementen.Vijand;

public class Controller
{
	public void detectCollision(ArrayList<SpelElement> elementenLijst, Link link)
	{
		for(SpelElement e:elementenLijst)
		{
			Rectangle p = new Rectangle(link.getX(),link.getY(),link.getWidth(),link.getHeight());
			if(e instanceof Vijand)
			{
				Rectangle r = new Rectangle(e.getX(),e.getY(),e.getWidth(),e.getHeight());
				if(r.intersects(p))
				{
					
				}
			}
			if(e instanceof Obstakel)
			{
				Rectangle r = new Rectangle(e.getX(),e.getY(),e.getWidth(),e.getHeight());
				if(r.intersects(p))
				{
					System.out.println("Collide!");	
				}
				
			}
		}
	}
	

	public void moveMap(Link link, Map map)
	{
		int x1 = link.getX();
		int y1 = link.getY();
		int x2 = x1+link.getWidth();
		int y2 = y1+link.getHeight();
		
		
		if((link.getDir()==0)&&(y1<=(475/2)-22))
		{
			if(map.getYPos()<0)
			{
				map.setYPos(map.getYPos()+4);					
			}
			if(map.getYPos()>0)
			{
				map.setYPos(0);
			}
			if(map.getMap()!="map3")
			{
				if(y1<=0)
				{
					link.setLocation(link.getX(),500-link.getHeight());
					map.setYPos(-1613);
					if(map.getMap()=="map1")
					{
						map.setXPos(map.getXPos()-980);
					}
					map.setMap("map3");				
				}
			}
		}
		if((link.getDir()==1)&&(y2>=(475/2)+22))
		{
			if(map.getYPos()>500-map.getHeightVal())
			{
				map.setYPos(map.getYPos()-4);					
			}
			if(map.getYPos()<500-map.getHeightVal())
			{
				map.setYPos(500-map.getHeightVal());
			}
			if(map.getMap()=="map3")
			{
				if(y2>=500)
				{
					link.setLocation(link.getX(),0);
					map.setYPos(0);
					if((map.getXPos()>=-980)&&(map.getXPos()<=0))
					{
						map.setMap("map2");
					}
					else
					{
						map.setXPos(map.getXPos()+980);
						map.setMap("map1");
					}
				}
			}
		}
		if((link.getDir()==2)&&(x1<=(500/2)-16))
		{
			if(map.getXPos()<0)
			{
				map.setXPos(map.getXPos()+4);					
			}
			if(map.getXPos()>0)
			{
				map.setXPos(0);
			}
			if(x1<=0)
			{
				if(map.getMap()=="map1")
				{
					link.setLocation(500-link.getWidth(), link.getY());
					map.setXPos(500-map.getWidthVal());
					map.setMap("map2");
				}
			}
		}
		if((link.getDir()==3)&&(x2>=(500/2)+16))
		{
			if(map.getXPos()>500-map.getWidthVal())
			{
				map.setXPos(map.getXPos()-4);					
			}
			if(map.getXPos()<500-map.getWidthVal())
			{
				map.setXPos(500-map.getWidthVal());
			}
			if(x2>=500)
			{
				if(map.getMap()=="map2")
				{
					link.setLocation(0,link.getY());
					map.setXPos(0);
					map.setMap("map1");
				}
			}
		}
	}
	
}
