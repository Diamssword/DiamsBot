package diamssword.bot.alexa;

import diamssword.bot.actions.AlexaAction;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Help implements ISubAlexa
	{
		@Override
		public String[] getSubCommand() {
			return new String[] {"help"};
		}

		@Override
		public void execute(MessageReceivedEvent event, String trigger, String after) {
			String res = "**List of avaiable subcommands for Alexa:**\n";
			for(Class <? extends ISubAlexa> subC: AlexaAction.getSubsAlexa())
			{
				ISubAlexa sub;
				try {
					sub = subC.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					event.getChannel().sendMessage(":warning: HU-HO    Ching Chong, somthing gone wong").queue();
					return;
				}
				res= res+sub.getName()+"  |  Commands:"+alias(sub.getSubCommand())+"  |  Desc:"+sub.getDesc()+"\n";
			}
			event.getChannel().sendMessage(res).queue();
		}
		private String alias(String[] als)
		{
			String res="";
			for(String s : als)
			{
				res=res+"/"+s;
			}
			return res.substring(1);
		}
		@Override
		public String getName() {
			return "Help";
		}

		@Override
		public String getDesc() {
			return "show help for Alexa";
		}

	}
