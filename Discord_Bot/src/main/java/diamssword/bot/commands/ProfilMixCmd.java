package diamssword.bot.commands;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import com.diamssword.bot.api.actions.ICommand;
import com.diamssword.bot.api.utils.LoadUtils;
import com.diamssword.bot.api.utils.MembersUtil;

import diamssword.bot.utils.MixProfilImage;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ProfilMixCmd implements ICommand{

	public ProfilMixCmd()
	{

	}
	private Random rand = new Random();
	@Override
	public String getName() {

		return "hell";
	}

	@Override
	public String getDesc() {
		return "mix profiles of members";
	}

	@Override
	public void execute(MessageReceivedEvent event, String ... args) {
		if(!event.getAuthor().isFake() && !event.getAuthor().isBot())
		{
			List<Member> victimes= new ArrayList<Member>();
			if(args.length >= 1)
			{
				for(String name : args)
				{
					List<Member> ls = MembersUtil.getMembersMatching(event.getGuild().getMembers(), name);
					if(!ls.isEmpty())
						victimes.add(ls.get(0));
				}
			}
			else
			{
				for(Member m :event.getGuild().getMembers())
					if(m.getOnlineStatus() == OnlineStatus.IDLE || m.getOnlineStatus() == OnlineStatus.ONLINE ||m.getOnlineStatus() == OnlineStatus.DO_NOT_DISTURB)
						victimes.add(m);
			}
			if(victimes.size()<2)
			{
				event.getChannel().sendMessage("I need names of at least 2 victimes!").queue();
				return;
			}
			BufferedImage[] images = new BufferedImage[victimes.size()];
			for(int i=0;i<victimes.size();i++)
			{
				try {
					images[i]= LoadUtils.dlImage(new URL(victimes.get(i).getUser().getEffectiveAvatarUrl()));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			BufferedImage img = new BufferedImage(128,128,BufferedImage.TYPE_INT_ARGB);
			for(int x = 0;x<img.getWidth();x++)
			{
				for(int y = 0;y<img.getHeight();y++)
				{
					try{ 
					int rgb = images[rand.nextInt(images.length)].getRGB(x, y);
					img.setRGB(x, y,rgb);
					}catch(ArrayIndexOutOfBoundsException e) {
						try{ 
						int rgb = images[rand.nextInt(images.length)].getRGB(x, y);
						img.setRGB(x, y,rgb);
						
						}catch(ArrayIndexOutOfBoundsException e1) {}
					}
				}
			}
			MixProfilImage imgF = new MixProfilImage(mix(victimes), img);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				ImageIO.write(imgF.get(),"png", os);
			} catch (IOException e) {}

			InputStream fis = new ByteArrayInputStream(os.toByteArray());
			event.getChannel().sendFile(fis, event.getAuthor().getName()+"mix.png").queue();
		}
	}

	@Override
	public String getUsage() {
		return ">hell <user1> <user2> <user3>...";
	}

	@Override
	public void init() {

	}
	private String mix(List<Member> vict)
	{
		List<Integer> chars= new ArrayList<Integer>();
		for(Member mb : vict)
		{
			for(int c1 : mb.getUser().getName().toCharArray())
				chars.add(c1);

		}
		Collections.shuffle(chars);
		int len =16+rand.nextInt(17);
		len=Math.min(len, chars.size());
		StringBuilder res = new StringBuilder();
		for(int i=0;i<len;i++)
		{
			res.append((char)((int)chars.get(i)));
		}
		return res.toString();
	}
}
