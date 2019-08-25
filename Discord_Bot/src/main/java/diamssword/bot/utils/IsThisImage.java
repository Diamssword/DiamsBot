package diamssword.bot.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import com.diamssword.bot.api.utils.LoadUtils;

import net.dv8tion.jda.core.entities.User;

public class IsThisImage {

	BufferedImage base;
	BufferedImage avatar;
	String text;
	String name;
	public IsThisImage(String text, User user)
	{
		base = LoadUtils.loadImg("is_this.png");
		this.text = text;
		URL url;
		try {
			url = new URL(user.getEffectiveAvatarUrl());

			URLConnection openConnection = url.openConnection();
			try {

				openConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
				openConnection.connect();

			} catch (Exception e) {
				System.out.println("Couldn't create a connection to the link, please recheck the link.");
				e.printStackTrace();
			}

				avatar = ImageIO.read(new BufferedInputStream(openConnection.getInputStream()));
		
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		this.name = user.getName();
	}
	public IsThisImage(String text, String urlStr, String title)
	{
		base = LoadUtils.loadImg("is_this.png");
		this.text = text;
		URL url;
		try {
			url = new URL(urlStr);

			URLConnection openConnection = url.openConnection();
			try {

				openConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
				openConnection.connect();

			} catch (Exception e) {
				System.out.println("Couldn't create a connection to the link, please recheck the link.");
				e.printStackTrace();
			}

				avatar = ImageIO.read(new BufferedInputStream(openConnection.getInputStream()));
		
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		this.name = title;
	}
	private void create()
	{
		int width = avatar.getWidth();
		BufferedImage circleBuffer = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = circleBuffer.createGraphics();
		g2.setClip(new Ellipse2D.Float(0, 0, width, width));
		g2.drawImage(avatar, 0, 0, width, width, null);
		g2.dispose();
		Graphics2D g1 =base.createGraphics();
		g1.setFont(g1.getFont().deriveFont(50f).deriveFont(Font.ITALIC));
		while(g1.getFontMetrics().stringWidth(name) > 500)
			g1.setFont(g1.getFont().deriveFont((float)(g1.getFont().getSize()-1f)));
		g1.drawImage(circleBuffer, 830, 0, 250, 250, null);
		g1.setColor(Color.BLACK);
		g1.drawString(name, 955- (g1.getFontMetrics().stringWidth(name)/2), 255+g1.getFontMetrics().getHeight());
		
		g1.setFont(g1.getFont().deriveFont(50f).deriveFont(Font.ITALIC+Font.BOLD));
		while(g1.getFontMetrics().stringWidth(text) > 1250)
			g1.setFont(g1.getFont().deriveFont((float)(g1.getFont().getSize()-1f)));
		g1.setColor(Color.BLACK);
		g1.drawString("IS THIS "+text, (this.base.getWidth()/2)-(g1.getFontMetrics().stringWidth("IS THIS "+text)/2), 640);
		
	//	g1.drawString("IS THIS "+text, 10, 640);
		g1.dispose();
	}
	public BufferedImage get()
	{
		this.create();
		return this.base;
	}
}
