package diamssword.bot.commands;

import java.util.Random;

import com.diamssword.bot.api.actions.ICommand;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class DiceCmd implements ICommand{

	public DiceCmd()
	{

	}
	private Random rand = new Random();
	@Override
	public String getName() {

		return "roll";
	}

	@Override
	public String getDesc() {
		return "roll a dice!";
	}

	@Override
	public void execute(MessageReceivedEvent event, String ... args) {
		if(!event.getAuthor().isFake() && !event.getAuthor().isBot())
		{

			if(args.length <1)
			{
		 		event.getChannel().sendMessage(((char)(127922))+"Result : "+rand.nextInt(6)+1+((char)(127922))).queue();
			}
		}
	}

	@Override
	public String getUsage() {
		return ">roll";
	}

	@Override
	public void init() {

	}
}
