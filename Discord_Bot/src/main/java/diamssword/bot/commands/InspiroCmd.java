package diamssword.bot.commands;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import com.diamssword.bot.api.actions.ICommand;
import com.diamssword.bot.api.utils.LoadUtils;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class InspiroCmd implements ICommand{
	private static URL url;

	public InspiroCmd()
	{
		if(url == null)
			try {
				url=new URL("https://inspirobot.me/api?generate=true");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
	}
	@Override
	public String getName() {

		return "inspiro";
	}

	@Override
	public String getDesc() {
		return "show a random Quote from inspirobot.me";
	}

	@Override
	public void execute(MessageReceivedEvent event, String ... args) {
		if(!event.getAuthor().isFake() && !event.getAuthor().isBot())
		{
			event.getChannel().sendMessage(LoadUtils.urlToString(url)).queue();

		}
	}

	@Override
	public String getUsage() {
		return ">inspiro";
	}

	@Override
	public void init() {

	}
}
