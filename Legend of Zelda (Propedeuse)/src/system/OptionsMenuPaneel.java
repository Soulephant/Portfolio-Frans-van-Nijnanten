package system;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;

import libraries.Button;
import binary.Opstart;

public class OptionsMenuPaneel extends MenuPaneel implements KeyListener
{
	private boolean sound, music;
	
	public OptionsMenuPaneel(Opstart frame)
	{
		setLayout(null);
		this.frame=frame;
		addKeyListener(this);
		requestFocus();
		sound = true;
		music = true;
		
		xPos = (frame.getWidth()/2)-75;
		yPos = (frame.getHeight()/2)-100;
		
		for(int i=0; i<4; i++)
		{
			switch(i)
			{
				case 0:
					label = "Sound effects On";
					break;
				case 1:
					label = "Music On";
					break;
				case 2:
					if(frame.getFullscreen()==true)
					{
						label = "Fullscreen On";
					}
					else if(frame.getFullscreen()==false)
					{
						label = "Fullscreen Off";
					}
					break;
				case 3:	
					label = "Back to Menu";
					break;
			}
			Button knop = new Button(xPos, yPos, label);
			knoppenLijst.add(knop);
			add(knop);
			yPos+=52;
			text = new ImageIcon(MenuPaneel.class.getResource("/resources/images/text3.png"));
		}
		keyPosition=0;
		knoppenLijst.get(keyPosition).setFocus(true);
		repaint();
		
	}

	public void paintComponent(Graphics g)
	{
		drawLogo(g);
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
		case KeyEvent.VK_ENTER:
			switch(keyPosition)
				{
				case 0:
					if(sound==true)
					{
						sound=false;
						label = "Sound effects Off";
					}
					else if(sound==false)
					{
						sound=true;
						label = "Sound effects On";
					}
					knoppenLijst.get(keyPosition).setLabel(label);
					break;
				case 1:
					if(music==true)
					{
						music=false;
						label = "Music Off";
					}
					else if(music==false)
					{
						music=true;
						label = "Music On";
					}
					knoppenLijst.get(keyPosition).setLabel(label);					
					break;
				case 2:
					/*frame.toggleFullscreen();
					if(frame.getFullscreen()==true)
					{
						label = "Fullscreen On";
						knoppenLijst.get(keyPosition).setLabel(label);
					}
					else if(frame.getFullscreen()==false)
					{
						label = "Fullscreen Off";
						knoppenLijst.get(keyPosition).setLabel(label);
					}*/
					break;
				case 3:	
					if(frame.getSession()==false)
					{
						frame.setScreen(false,0);
					}
					else if(frame.getSession()==true)
					{
						frame.setScreen(true,3);
					}
					break;
				}
			break;
		case KeyEvent.VK_SPACE:
			switch(keyPosition)
			{
				case 0:
					if(sound==true)
					{
						sound=false;
						label = "Sound effects Off";
					}
					else if(sound==false)
					{
						sound=true;
						label = "Sound effects On";
					}
					knoppenLijst.get(keyPosition).setLabel(label);
					break;
				case 1:
					if(music==true)
					{
						music=false;
						label = "Music Off";
					}
					else if(music==false)
					{
						music=true;
						label = "Music On";
					}
					knoppenLijst.get(keyPosition).setLabel(label);					
					break;
				case 2:
					frame.toggleFullscreen();
					if(frame.getFullscreen()==true)
					{
						label = "Fullscreen On";
						knoppenLijst.get(keyPosition).setLabel(label);
					}
					else if(frame.getFullscreen()==false)
					{
						label = "Fullscreen Off";
						knoppenLijst.get(keyPosition).setLabel(label);
					}
					break;
				case 3:	
					if(frame.getSession()==false)
					{
						frame.setScreen(false,0);
					}
					else if(frame.getSession()==true)
					{
						frame.setScreen(true,3);
					}
					break;
			}
			break;
		case KeyEvent.VK_ESCAPE:
			if(frame.getSession()==false)
			{
				frame.setScreen(false,0);
			}
			else if(frame.getSession()==true)
			{
				frame.setScreen(true,3);
			}
			break;
		}
		repaint();
	}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
}
