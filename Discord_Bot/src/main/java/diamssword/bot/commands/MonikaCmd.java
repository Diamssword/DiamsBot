package diamssword.bot.commands;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import com.diamssword.bot.api.actions.ICommand;
import com.diamssword.bot.api.utils.LoadUtils;
import com.diamssword.bot.api.utils.StringUtils;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MonikaCmd implements ICommand{

	public MonikaCmd()
	{

	}
	@Override
	public String getName() {

		return "monika";
	}

	@Override
	public String getDesc() {
		return "Monika t-pose meme";
	}

	@Override
	public void execute(MessageReceivedEvent event, String ... args) {
		if(!event.getAuthor().isFake() && !event.getAuthor().isBot())
		{
			String[] arg =StringUtils.weldArgs(args, 0, args.length).split(">");
			if(arg.length>=2)
			{
				boolean flag = arg.length>=3 && arg[2].contains("eye");
			BufferedImage img=this.create(arg[0], arg[1],flag);
			if(img != null)
			{
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				ImageIO.write(img, "png", os);
			} catch (IOException e) {
				e.printStackTrace();
			}
			LoadUtils.sendFile(event.getChannel(), os.toByteArray(), arg[0]+".png", "");
			}
			}
		}
	}

	@Override
	public String getUsage() {
		return ">monika (text1) > (text2) > (optional)eyes";
	}

	@Override
	public void init() {

	}
	private BufferedImage create(String str1,String str2, boolean eyes)
	{
		BufferedImage mon =LoadUtils.loadImg("monika.png");
		Graphics2D g = mon.createGraphics();
		if(eyes)
		{
			BufferedImage eye =LoadUtils.loadImg("eye.png");
			g.drawImage(eye, 484-128, 208-128, 256, 256, null);
			g.drawImage(eye, 527-128, 197-128, 256, 256, null);
		}
		int size = StringUtils.getMaxFittingFontSize(g, g.getFont(), str2, 1000, 70);
		g.setFont(g.getFont().deriveFont((float)size));
		g.setColor(Color.BLACK);

		int x = Math.max(240 - (g.getFontMetrics().stringWidth(str1)/2),0);
		g.drawString(str2, x, 850);
		size = StringUtils.getMaxFittingFontSize(g, g.getFont(), str2, 900, 70);
		g.setFont(g.getFont().deriveFont((float)size));
		x = Math.max(500 - (g.getFontMetrics().stringWidth(str2)/2),0);
		g.drawString(str1, x, 90);
		g.dispose();
		return mon;
	}
}
