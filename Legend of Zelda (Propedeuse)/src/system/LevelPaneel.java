package system;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import libraries.Controller;
import binary.Opstart;

public class LevelPaneel extends Paneel implements KeyListener
{
	private Opstart frame;
	private Controller control;
	
	public LevelPaneel(Opstart frame)
	{
		setLayout(null);//Set layout to null for panels within panel
		this.frame = frame;//Necessary to control frame(main class)
		addKeyListener(this);
		setFocusable(true);
		requestFocus();
		
		//Create instance of object ScoreController
		//Works well for now!
		ScoreController score = new ScoreController(this);
		score.setBounds(0,0,600,100);
		add(score);
		repaint();
		
		//Create instance of object Veld
		Veld veld =  new Veld();
		veld.setBounds(0,100,500,475);
		veld.requestFocus();
		add(veld);
		repaint();

		control = new Controller();
	}
	
	public void keyPressed(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		
		switch(keyCode)
		{
		case KeyEvent.VK_UP:
			Veld.getLink().toggleMove(true);
			Veld.getLink().move(0);
			control.moveMap(Veld.getLink(),Veld.getMap());
			repaint();
			break;
		case KeyEvent.VK_DOWN:
			Veld.getLink().toggleMove(true);
			Veld.getLink().move(1);
			control.moveMap(Veld.getLink(),Veld.getMap());
			repaint();
			break;
		case KeyEvent.VK_LEFT:
			Veld.getLink().toggleMove(true);
			Veld.getLink().move(2);
			control.moveMap(Veld.getLink(),Veld.getMap());
			repaint();			
			break;
		case KeyEvent.VK_RIGHT:
			Veld.getLink().toggleMove(true);
			Veld.getLink().move(3);
			control.moveMap(Veld.getLink(),Veld.getMap());
			repaint();			
			break;
		case KeyEvent.VK_ENTER:
			
			break;
		case KeyEvent.VK_ESCAPE:
			frame.setScreen(true,3);//Set screen to ingame menu
			break;
		}
	}
	
	public void keyReleased(KeyEvent e)
	{
		Veld.getLink().toggleMove(false);
		repaint();
	}
	public void keyTyped(KeyEvent e){}
}
