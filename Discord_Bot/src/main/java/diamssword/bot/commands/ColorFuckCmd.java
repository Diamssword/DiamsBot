package diamssword.bot.commands;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;

import com.diamssword.bot.api.actions.ICommand;
import com.google.gson.Gson;

import diamssword.bot.utils.RedditJson;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ColorFuckCmd implements ICommand{

	public ColorFuckCmd()
	{

	}
	private Random rand = new Random();
	@Override
	public String getName() {

		return "IFuck";
	}

	@Override
	public String getDesc() {
		return "fuck up colors of a passed image";
	}

	@Override
	public void execute(MessageReceivedEvent event, String ... args) {
		if(!event.getAuthor().isFake() && !event.getAuthor().isBot())
		{
			if(args.length>=1)
			{
				if(args[0].contains("http"))
				{
					try {
						URL url = new URL(args[0]);
						BufferedImage img = ImageIO.read(url.openStream());
						for(int x = 0;x<img.getWidth();x++)
						{
							for(int y = 0;y<img.getHeight();y++)
							{
								int add = rand.nextInt(0xFF);
								boolean less = rand.nextBoolean();

								int rgb = img.getRGB(x, y);
								if(less)
									img.setRGB(x, y,rgb-add);
								else
									img.setRGB(x, y,rgb+add);
							}
						}
						ByteArrayOutputStream os = new ByteArrayOutputStream();
						try {
							ImageIO.write(img,"png", os);
						} catch (IOException e) {}

						InputStream fis = new ByteArrayInputStream(os.toByteArray());
						event.getChannel().sendFile(fis, event.getAuthor().getName()+"ifuck.png").queue();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			else
			{

				Scanner s = null;
				try {
					s = new Scanner(new URL("https://meme-api.herokuapp.com/gimme").openStream());
					String res = "";
					while(s.hasNextLine())
					{
						res = res+s.nextLine();
					}
					RedditJson post = new Gson().fromJson(res, RedditJson.class);

					BufferedImage img = post.getImage();
					for(int x = 0;x<img.getWidth();x++)
					{
						for(int y = 0;y<img.getHeight();y++)
						{
							int def =img.getRGB(x, y);
							int a =  125+rand.nextInt(100);
							int r =  rand.nextInt(100);
							int g =  rand.nextInt(100);
							int b =  rand.nextInt(100);
							int value = ((a & 0xFF) << 24) |
									((r & 0xFF) << 16) |
									((g & 0xFF) << 8)  |
									((b & 0xFF) << 0);
							boolean f = rand.nextBoolean();
							if(f)
								img.setRGB(x, y,def+value);
							else
								img.setRGB(x, y,def-value);
						}
					}
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					try {
						ImageIO.write(img,"png", os);
					} catch (IOException e) {}

					InputStream fis = new ByteArrayInputStream(os.toByteArray());
					event.getChannel().sendFile(fis, event.getAuthor().getName()+"ifuck.png").queue();
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally
				{
					if(s!= null)
						s.close();
				}
			}
		}
	}

	@Override
	public String getUsage() {
		return ">ifuck <imglink>";
	}

	@Override
	public void init() {

	}
}
