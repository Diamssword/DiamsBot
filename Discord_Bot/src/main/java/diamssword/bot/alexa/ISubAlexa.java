package diamssword.bot.alexa;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface ISubAlexa
{
	String[] getSubCommand();
	public void execute(MessageReceivedEvent event,String trigger, String after);
	public String getName();
	public String getDesc();

}