package diamssword.bot.commands;

import com.diamssword.bot.api.actions.ICommand;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class StatCmd implements ICommand{

	@Override
	public String getName() {

		return "stat";
	}

	@Override
	public String getDesc() {
		return "show your stats";
	}

	@Override
	public void execute(MessageReceivedEvent event, String ... args) {
			if(!event.getAuthor().isFake())
			{
				if(args.length > 0 && args[0].equals("up"))
				 {
		//		StatSave.get(event.getAuthor().getId()).lvl+=1;
				}
				event.getChannel().sendMessage(format(event)).queue();
			}

	}

	@Override
	public String getUsage() {
		return ">stat";
	}

	private String format(MessageReceivedEvent event)
	{
//		Stats s =StatSave.get(event.getAuthor().getId());
		int tot = 0;//(int) Math.floor(((double)s.xp/(double)s.xpMax) * 10);
		String xpStr = "[";
		for(int i= 0;i < tot;i++)
		{
			xpStr= xpStr+"=";
		}
		for(int i= tot;i < 10;i++)
		{
			xpStr= xpStr+" -";
		}
		xpStr= xpStr+"]";
		return event.getAuthor().getAsMention()+":\n Lvl:          "+/*s.lvl+*/"\n"+xpStr+"\nHP: "+"❤❤❤🖤🖤🖤"+"\n"+"Mana: <=====----->";
				
	}

	@Override
	public void init() {
		
	}
}
