package binary;

import javax.swing.JFrame;
import system.*;
import java.awt.*;

import libraries.Sound;

/**
 * Dit is het opstart klasse van het spel
 * @author Shun Luk
 *
 */

public class Opstart extends JFrame
{
	//Boolean session determines if there's a game in session
	//fullscreen determines if the game is in fullscreen or windowed
	private boolean session, fullscreen;
	private Paneel currentPanel;
	static final int MAIN_MENU = 0;
	static final int OPTIONS_MENU = 1;
	static final int LEVEL_PANEL = 2;
	static final int INGAME_MENU = 3;
	
	public Opstart()
	{
		fullscreen = false;//Set fullscreen to false on startup
		
		setSize(600,600);
		setResizable(false);
		setLocation(200,100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("The Legend of Zelda: A Link to the Past");
		setVisible(true);

		//Hide cursor and disable mousebuttons
		//DO NOT TOUCH
		Toolkit tk = Toolkit.getDefaultToolkit();
		Cursor invisCursor = tk.createCustomCursor(tk.createImage(""),new Point(),null);
		setCursor(invisCursor);
		getGlassPane().setVisible(true);
		
		session = false;//Set session to false(no game in session)
		setScreen(session, MAIN_MENU);//Set screen to main menu
	}
	
	//Method to toggle fullscreen/windowed mode
	//Only error found is resolution problem when fullscreen toggled
	//DO NOT TOUCH UNLESS SOLUTION IS FOUND
	public void toggleFullscreen()
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
		if(fullscreen==false)
		{
			gs.setFullScreenWindow(this);
			setSize(getWidth(),getHeight());
			invalidate();
			validate();
			fullscreen=true;
		}
		else if(fullscreen==true)
		{
			gs.setFullScreenWindow(null);
			setSize(600,600);
			invalidate();
			validate();
			fullscreen=false;
		}
	}
	
	//Getters for boolean fullscreen and session
	public boolean getFullscreen()
	{
		return fullscreen;
	}
	
	public boolean getSession()
	{
		return session;
	}
	
	//Method to set the current screen/panel
	//Main menu, options, levelpanel etc.
	//DO NOT TOUCH
	public void setScreen(boolean session, int panel)
	{
		this.session=session;
		if(currentPanel!=null)
		{
			remove(currentPanel);
		}	
		if(session==false)
		{
			switch(panel)
			{
				case MAIN_MENU:
					currentPanel = new MainMenuPaneel(this);
					add(currentPanel);
					break;
				case OPTIONS_MENU:
					currentPanel = new OptionsMenuPaneel(this);
					add(currentPanel);
			}
		}
		else if(session==true)
		{
			switch(panel)
			{
				case LEVEL_PANEL:
					setSize(500,575);
					currentPanel = new LevelPaneel(this);
					add(currentPanel);
					break;
				case INGAME_MENU:
					setSize(600,600);
					currentPanel = new MainMenuPaneel(this);
					add(currentPanel);
					break;
				case OPTIONS_MENU:
					setSize(600,600);
					currentPanel = new OptionsMenuPaneel(this);
					add(currentPanel);
			}
		}
		currentPanel.requestFocus();
		//"Repaint" the frame
		invalidate();
		validate();
		currentPanel.repaint();
	}
	
	public static void main(String[] args)
	{
		new Opstart();
	}
}