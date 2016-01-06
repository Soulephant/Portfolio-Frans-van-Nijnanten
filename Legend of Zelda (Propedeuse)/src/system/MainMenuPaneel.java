package system;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;

import binary.Opstart;
import libraries.Button;
import libraries.Sound;

public class MainMenuPaneel extends MenuPaneel implements KeyListener
{
	public MainMenuPaneel(Opstart frame)
	{
		setLayout(null);//Layout set to null to allow panels within panels
		this.frame = frame;//Necessary for control of frame(startup class)
		addKeyListener(this);
		requestFocus();
		
		//xPos and yPos used to determine start coordinates to draw buttons
		xPos = (frame.getWidth()/2)-75;
		yPos = (frame.getHeight()/2)-100;
		
		//if/else condition used to determine whether to draw main menu buttons
		//or ingame menu buttons
		if(frame.getSession()==false)
		{
			for(int i=0; i<4; i++)
			{
				//Set the string label/label for the "buttons"
				switch(i)
				{
					case 0:
						label = "New Game";
						break;
					case 1:
						label = "Load Game";
						break;
					case 2:
						label = "Options";
						break;
					case 3:	
						label = "Quit Game";
						break;
				}
				Button knop = new Button(xPos, yPos, label);
				knoppenLijst.add(knop);
				add(knop);
				yPos+=52;
				text = new ImageIcon(MenuPaneel.class.getResource("/resources/images/text.png"));
			}
			Sound sound = new Sound("test\\test.WAV",true);
			///home/densetsushun/Downloads/0.1-V-S/Sounds/Map & Models/Intro 1.WAV
			///test/test.WAV
		}
		else if(frame.getSession()==true)
		{
			for(int i=0; i<5; i++)
			{
				switch(i)
				{
					case 0:	
						label = "Resume";
						break;
					case 1:
						label = "New Game";
						break;
					case 2:
						label = "Load Game";
						break;
					case 3:
						label = "Options";
						break;
					case 4:	
						label = "Quit to Main Menu";
						break;
				}
				Button knop = new Button(xPos, yPos, label);
				knoppenLijst.add(knop);
				add(knop);
				yPos+=50;
				text = new ImageIcon(MenuPaneel.class.getResource("/resources/images/text2.png"));
			}
		}
		knoppenLijst.get(keyPosition).setFocus(true);//Give focus to selected key
		repaint();
		
	}

	public void paintComponent(Graphics g)
	{
		drawLogo(g);//Calls method drawLogo from parent class MenuPaneel
		//For loop used to draw all buttons(with method drawButton from class Button)
		g.drawImage(text.getImage(), (getWidth()/2)-(text.getIconWidth()/2), 205, this);
		for(Button b: knoppenLijst)
		{
			b.drawButton(g);
		}
	}

	public void keyPressed(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		
		switch(keyCode)
		{
		//VK_DOWN and VK_UP(down and up keys) used to change keyPosition
		case KeyEvent.VK_DOWN:
			keyPosition++;
			if(keyPosition==knoppenLijst.size())
			{
				keyPosition=0;
			}
			for(Button b: knoppenLijst)
			{
				b.setFocus(false);
			}
			knoppenLijst.get(keyPosition).setFocus(true);
			break;
		case KeyEvent.VK_UP:
			keyPosition--;
			if(keyPosition<0)
			{
				keyPosition = knoppenLijst.size()-1;
			}
			for(Button b: knoppenLijst)
			{
				b.setFocus(false);
			}
			knoppenLijst.get(keyPosition).setFocus(true);
			break;
			
			//VK_ENTER and VK_SPACE used to press selected button
		case KeyEvent.VK_ENTER:
			//Changes current screen based on selected button
			if(frame.getSession()==true)
			{
				switch(keyPosition)
				{
					case 0:
						frame.setScreen(true,2);
						break;
					case 1:
						frame.setScreen(true,2);
						break;
					case 2:
						break;
					case 3:
						frame.setScreen(true,1);
						break;
					case 4:
						frame.setScreen(false,0);
				}
			}
			else if(frame.getSession()==false)
			{
				switch(keyPosition)
				{
					case 0:
						frame.setScreen(true,2);
						break;
					case 1:
						
						break;
					case 2:
						frame.setScreen(false,1);
						break;
					case 3:
						System.exit(0);//Closes window and kills process
						break;
				}
			}
			break;
		case KeyEvent.VK_SPACE:
			if(frame.getSession()==true)
			{
				switch(keyPosition)
				{
					case 0:
						frame.setScreen(true,2);
						break;
					case 1:
						frame.setScreen(true,2);
						break;
					case 2:
						break;
					case 3:
						frame.setScreen(true,1);
						break;
					case 4:
						frame.setScreen(false,0);
				}
			}
			else if(frame.getSession()==false)
			{
				switch(keyPosition)
				{
					case 0:
						frame.setScreen(true,2);
						break;
					case 1:
						
						break;
					case 2:
						frame.setScreen(false,1);
						break;
					case 3:
						System.exit(0);
						break;
				}
			}
			break;
		case KeyEvent.VK_ESCAPE:
			if(frame.getSession()==false)
			{
				System.exit(0);
			}
			else if(frame.getSession()==true)
			{
				frame.setScreen(true,2);
			}
			break;
		}
		repaint();
	}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
}
