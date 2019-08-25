package diamssword.bot.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class RedditJson {
public String title;
public String url;


public BufferedImage getImage()
{
	try {
		return ImageIO.read(new URL(url).openStream());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return new BufferedImage(10, 10, 1);
}
}
