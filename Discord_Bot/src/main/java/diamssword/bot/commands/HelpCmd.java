package diamssword.bot.commands;

import com.diamssword.bot.api.actions.IAction;
import com.diamssword.bot.api.actions.ICommand;
import com.diamssword.bot.api.actions.Registry;
import com.diamssword.bot.api.utils.StringUtils;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class HelpCmd implements ICommand{

	@Override
	public String getName() {

		return "help";
	}

	@Override
	public String getDesc() {
		return "show the help";
	}

	@Override
	public void execute(MessageReceivedEvent event, String ... args) {
		if(!event.getAuthor().isFake())
		{
			if(args.length>=1)
			{
				String sub = StringUtils.weldArgs(args, 0, args.length);
				for(ICommand c :Registry.commands)
				{
					if(c.getName().equalsIgnoreCase(sub))
					{
						event.getChannel().sendMessage("Command '"+c.getName()+"' : \n "+c.getDesc()+"\n Usage: "+c.getUsage()).queue();
						return;
					}
				}
				for(IAction c :Registry.actions)
				{
					if(c.name().equalsIgnoreCase(sub))
					{
						event.getChannel().sendMessage("Action '"+c.name()+"' : \n "+c.usage()).queue();
						return;
					}
				}
			}
				event.getChannel().sendMessage(format(event)).queue();
		}

	}

	@Override
	public String getUsage() {
		return ">help";
	}

	private String format(MessageReceivedEvent event)
	{
		String s = "Liste des commandes DiamsBot : \n";
		for(ICommand c : Registry.commands)
		{
			s = s+ ">"+c.getName()+ " : "+ c.getDesc()+ ".  Usage: " +c.getUsage()+"\n";
		}
		s=s+ "\n Liste des actions DiamsBot : \n";
		for(IAction a : Registry.actions)
		{
			s = s+ a.name()+ " =>  "+ a.usage()+"\n";
		}
		return s;

	}

	@Override
	public void init() {

	}
}
