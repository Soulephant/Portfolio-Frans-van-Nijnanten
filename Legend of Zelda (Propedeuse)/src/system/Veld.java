package system;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

import libraries.Map;
import spelElementen.Link;
import spelElementen.Obstakel;
import spelElementen.SpelElement;
import spelElementen.Vijand;

public class Veld extends JPanel implements KeyListener,ActionListener
{
	private ArrayList<SpelElement>elementenLijst;
	private static Link Link;
	private Vijand Vijand;
	private static Map map; 
	private Timer timer;
	
	public Veld()
	{
		setLayout(null);
		elementenLijst = new ArrayList<SpelElement>();
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
		
		map = new Map(elementenLijst,this);
		
		int width = 32;
		int height = 44;
		Link = new Link(this,260,215,width,height);
		Link.setBounds(260,215,width,height);
		Link.requestFocus();
		elementenLijst.add(Link);
		add(Link);
		repaint();

		
		Obstakel ob = new Obstakel(181,47,185,193);
		elementenLijst.add(ob);
		add(ob);
		repaint();
		
		Vijand = new Vijand(350,350,50,50,this);
		Vijand.setBounds(350,350,50,50);
		elementenLijst.add(Vijand);
		add(Vijand);
		repaint();
		
		timer = new Timer(200,this);
	}
	
	public static Link getLink()
	{
		return Link;
	}
	
	public static Map getMap()
	{
		return map;
	}
	
	public ArrayList<SpelElement> getElementen()
	{
		return elementenLijst;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		map.drawMap(g);
	}
	
	public void actionPerformed(ActionEvent e){}
	public void keyPressed(KeyEvent e){}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
}
