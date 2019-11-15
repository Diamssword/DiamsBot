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
		/*	if(args.length>=1)
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
			}*/
		
			String[] strs = format(event, args.length>=1?args[0]:null);
			for(String s : strs)
			{
				event.getChannel().sendMessage(s).queue();
			}
		}

	}

	@Override
	public String getUsage() {
		return ">help (name or word)";
	}

	private String[] format(MessageReceivedEvent event,String key)
	{
		String s = "Liste des commandes DiamsBot : \n";
		for(ICommand c : Registry.commands)
		{
			if(key == null || c.getName().toLowerCase().contains(key.toLowerCase()))
			s = s+ ">"+c.getName()+ " : "+ c.getDesc()+ ".  Usage: " +c.getUsage()+"\n";
		}
		s=s+ "\n Liste des actions DiamsBot : \n";
		for(IAction a : Registry.actions)
		{
			if(key == null || a.name().toLowerCase().contains(key.toLowerCase()))
			s = s+ a.name()+ " =>  "+ a.usage()+"\n";
		}
		String[] res = new String[(s.length()/2000)+1];
		for(int i=0;i<res.length;i++)
		{
			res[i]= s.substring(i*2000,Math.min((i+1)*2000,s.length()));
		}
		//.substring(0,2000)
		return res;

	}

	@Override
	public void init() {

	}
}
