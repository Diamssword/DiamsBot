package diamssword.bot.commands;

import java.util.List;

import com.diamssword.bot.api.actions.ICommand;
import com.diamssword.bot.api.utils.MembersUtil;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class GetPPCmd implements ICommand{

	public GetPPCmd()
	{

	}
	@Override
	public String getName() {

		return "getpp";
	}

	@Override
	public String getDesc() {
		return "get you pp";
	}

	@Override
	public void execute(MessageReceivedEvent event, String ... args) {
		if(!event.getAuthor().isFake() && !event.getAuthor().isBot())
		{
			User m;
			if(args.length <1)
			{
				m=event.getAuthor();
			}
			else
			{
				List<Member> ms = MembersUtil.getMembersMatching(event.getGuild().getMembers(), args[0]);
				if(ms.isEmpty())
				{
					event.getChannel().sendMessage("User "+args[0]+" not found!").queue();
					return;
				}
				m = ms.get(0).getUser();
			}
			event.getChannel().sendMessage(m.getAvatarUrl()).queue();
		}
	}

	@Override
	public String getUsage() {
		return ">getpp (username)";
	}

	@Override
	public void init() {

	}
}
