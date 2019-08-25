package diamssword.bot.commands;

import java.util.Random;

import com.diamssword.bot.api.actions.ICommand;
import com.diamssword.bot.api.utils.MembersUtil;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class OpCmd implements ICommand{

	public OpCmd()
	{

	}
	private Random rand = new Random();
	@Override
	public String getName() {

		return "op";
	}

	@Override
	public String getDesc() {
		return "remplacement du canard";
	}

	@Override
	public void execute(MessageReceivedEvent event, String ... args) {
		if(!event.getAuthor().isFake() && !event.getAuthor().isBot())
		{

			if(args.length >=1)
			{
				Member m =MembersUtil.getMembersMatching(event.getGuild().getMembers(), args[0]).get(0);
				if(args.length >=2)
				{
					Role r = event.getGuild().getRolesByName(args[1], true).get(0);
					if(m.getRoles().contains(r))
						event.getGuild().getController().removeRolesFromMember(m,r ).queue();
					else
						event.getGuild().getController().addRolesToMember(m, r).queue();
					return;
				}
				Role r = event.getGuild().getRolesByName("Dégénérés", true).get(0);
				if(m.getRoles().contains(r))
					event.getGuild().getController().removeRolesFromMember(m,r ).queue();
				else
					event.getGuild().getController().addRolesToMember(m, r).queue();
			}
		}
	}

	@Override
	public String getUsage() {
		return ">op <User>";
	}

	@Override
	public void init() {

	}
}
