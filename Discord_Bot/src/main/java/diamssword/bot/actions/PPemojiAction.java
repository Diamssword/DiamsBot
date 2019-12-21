package diamssword.bot.actions;

import java.util.List;

import com.diamssword.bot.api.actions.IAction;
import com.diamssword.bot.api.utils.MembersUtil;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PPemojiAction implements IAction{

	@Override
	public void execute(MessageReceivedEvent event,String trigger, String before, String after) {
		if(!event.getAuthor().isFake())
		{
			System.out.println(trigger+"/"+before+"/"+after);
			if(after.contains(":"))
			{
				{
					User m;
					List<Member> ms = MembersUtil.getMembersMatching(event.getGuild().getMembers(), after.substring(0,after.indexOf(":")));
					System.out.println(after.substring(0,after.indexOf(":")));
					if(ms.isEmpty())
					{
						return;
					}
					m = ms.get(0).getUser();
					Member me = event.getMember();
					EmbedBuilder bu =new EmbedBuilder().setAuthor(me.getEffectiveName(),"http://google.com", me.getUser().getEffectiveAvatarUrl());
					bu.addField("", before, true);
					bu.addField("", after.substring(after.indexOf(":")+1), true);
					bu.setColor(0x36393f);
					bu.setImage(m.getEffectiveAvatarUrl());
					event.getChannel().sendMessage(bu.build()).queue();
					event.getChannel().deleteMessageById(event.getMessageId()).queue();
				}
			}
		}

	}

	@Override
	public String[] getTriggers(Guild g) {
		return new String[] {":>"};
	}

	@Override
	public String usage() {
		return ":>pseudo:";
	}

	@Override
	public String name() {
		return "PP Emoji";
	}

	@Override
	public void init() {

	}
	public boolean shouldBeSpaced()
	{
		return false;
		
	}
}

