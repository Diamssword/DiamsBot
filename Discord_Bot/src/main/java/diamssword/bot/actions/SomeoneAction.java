package diamssword.bot.actions;

import com.diamssword.bot.api.actions.IAction;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SomeoneAction implements IAction{

	@Override
	public void execute(MessageReceivedEvent event,String trigger, String before, String after) {
		if(!event.getAuthor().isFake() && !event.getAuthor().isBot())
		{
			MessageEmbed msg =new MessageEmbed(after, after, after, null, null, 0, null, null, null, null, null, null, null) {}	;
			event.getChannel().sendMessage("Hi **"+after+"** ! I'm Dad!").queue();
		}

	}

	@Override
	public String[] getTriggers(Guild g) {
		return new String[] {"I'm","Im","I am","I Am","I'M","I AM"};
	}

	@Override
	public String usage() {
		return "Hi Stupid, I'm Dad!";
	}

	@Override
	public String name() {
		return "'Dad Jokes";
	}

	@Override
	public void init() {
		
	}

}
