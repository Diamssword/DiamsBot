package diamssword.bot.actions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import com.diamssword.bot.api.actions.IAction;
import com.diamssword.bot.api.utils.MembersUtil;

import diamssword.bot.utils.IsThisImage;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class IsThisAction implements IAction{

	@Override
	public void execute(MessageReceivedEvent event,String trigger, String before, String after) {
		if(!event.getAuthor().isFake())
		{
			if(before.contains("http://")  || before.contains("https://"))
			{
				String[] parts=before.split(" ", 2);
				IsThisImage img =	new IsThisImage(after, parts[0].replaceAll(" ", ""),parts[1]);
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				try {
					ImageIO.write(img.get(),"png", os);
				} catch (IOException e) {}

				InputStream fis = new ByteArrayInputStream(os.toByteArray());
				event.getChannel().sendFile(fis, "is_this_"+event.getAuthor().getName()+".png").queue();
				
			}
			else
			{
				while(before.charAt(before.length()-1)==' ')
					before = before.substring(0, before.length()-1);
				List<Member> mb = MembersUtil.getMembersMatching(event.getGuild().getMembers(),before);
				if(!mb.isEmpty())
				{
					Member memb =mb.get(0);
					IsThisImage img =	new IsThisImage(after, memb.getUser());
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					try {
						ImageIO.write(img.get(),"png", os);
					} catch (IOException e) {}

					InputStream fis = new ByteArrayInputStream(os.toByteArray());
					event.getChannel().sendFile(fis, "is_this_"+event.getAuthor().getName()+".png").queue();
				}
			}
		}

	}

	@Override
	public String[] getTriggers(Guild g) {
		return new String[] {">is this","> is this"};
	}

	@Override
	public String usage() {
		// TODO Auto-generated method stub
		return "[pseudo] | ([url]  [Title]) > Is This [text]";
	}

	@Override
	public String name() {
		return "'Is This' generator";
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

}
