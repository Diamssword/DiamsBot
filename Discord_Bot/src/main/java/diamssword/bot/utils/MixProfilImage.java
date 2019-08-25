package diamssword.bot.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.diamssword.bot.api.utils.LoadUtils;
import com.diamssword.bot.api.utils.StringUtils;

public class MixProfilImage {

	BufferedImage base;
	BufferedImage avatar;
	String text;
	public MixProfilImage(String text, BufferedImage image)
	{
		base = LoadUtils.loadImg("discord_profile.png");
		this.text = text;
		this.avatar = image;
	}

	private void create()
	{
		BufferedImage result = new BufferedImage(500,360,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g1 =result.createGraphics();
		g1.drawImage(avatar, 160, 37, 182, 182, null);
		g1.drawImage(base, 0, 0, 500, 360, null);
		g1.setFont(g1.getFont().deriveFont(50f).deriveFont(Font.ITALIC));

		g1.setFont(g1.getFont().deriveFont(50f).deriveFont(Font.ITALIC+Font.BOLD));
		g1.setFont(g1.getFont().deriveFont(StringUtils.getMaxFittingFontSize(g1, g1.getFont(), text, 470, 50)+0.0f));
		g1.setColor(new Color(44,0,0));
		g1.drawString(text, 250-(g1.getFontMetrics().stringWidth(text)/2), 288);

		g1.dispose();
		this.base = result;
	}
	public BufferedImage get()
	{
		this.create();
		return this.base;
	}
}
