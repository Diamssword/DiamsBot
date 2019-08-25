package diamssword.bot.commands;

import java.util.List;

import com.diamssword.bot.api.actions.ICommand;
import com.diamssword.bot.api.utils.MembersUtil;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class WizzCmd implements ICommand{

	public WizzCmd()
	{

	}
	@Override
	public String getName() {

		return "wizz";
	}

	@Override
	public String getDesc() {
		return "do a annoying wizz";
	}

	@Override
	public void execute(MessageReceivedEvent event, String ... args) {
		if(!event.getAuthor().isFake())
		{


			String sub = "";
			if(args.length >= 1 && !args[0].equals(""))
			{
				List<Member> ls = MembersUtil.getMembersMatching(event.getGuild().getMembers(), args[0]);
				if(!ls.isEmpty())
					sub=ls.get(0).getAsMention();
			}
			for(int i=0;i< 3;i++)
			{
				event.getChannel().sendMessage(new MessageBuilder().setTTS(true).setContent("Wizz "+sub).build()).queue();
				
			}
		}
	}

	@Override
	public String getUsage() {
		return ">wizz <username>";
	}

	@Override
	public void init() {

	}
}
