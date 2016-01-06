package libraries;

import java.io.*;
import javax.sound.sampled.*;
import javax.swing.JPanel;

/**
 * @author Frans van Nijnanten
 *
 */

public class Sound
{
	private String path;
	private boolean soundOn = true;
	private boolean doLoop;
	
	static File file;
	static AudioInputStream stream;
	static Clip sound;
	
	public Sound(String path, boolean doLoop)
	{
		this.path = path; 
		this.doLoop = doLoop;
		PlaySound(soundOn);
		
	}
	
	public void PlaySound(boolean soundOn)
	{
		if(soundOn == true)
		{
			try
			{
				file = new File(path);
				System.out.println(path);
				System.out.println(file.toString());
				stream = AudioSystem.getAudioInputStream(file);
				sound = AudioSystem.getClip();
				
				sound.open(stream);
				if(doLoop == true)
				{
					sound.loop(Clip.LOOP_CONTINUOUSLY);
				}
				sound.start();
			}
			catch (Exception e) {}
		}
		else
		{
			sound.stop();
			sound.close();
		}
	}

	public boolean isSoundOn()
	{
		return soundOn;
	}

	public void setSoundOn(boolean soundOn)
	{
		this.soundOn = soundOn;
	}
}