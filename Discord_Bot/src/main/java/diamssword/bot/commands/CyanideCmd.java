package diamssword.bot.commands;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.Scanner;

import com.diamssword.bot.api.actions.ICommand;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CyanideCmd implements ICommand{

	
	public CyanideCmd()
	{

	}
	private Random rand = new Random();
	@Override
	public String getName() {

		return "cyanide";
	}

	@Override
	public String getDesc() {
		return "show a random Cyanide And Hapiness Comics";
	}

	@Override
	public void execute(MessageReceivedEvent event, String ... args) {
		if(!event.getAuthor().isFake() && !event.getAuthor().isBot())
		{
			event.getChannel().sendMessage(getRandom()).queue();
		
		}
	}

	@Override
	public String getUsage() {
		return ">cyanide";
	}

	@Override
	public void init() {

	}
	
	private String getRandom()
	{
		String content = null;
		URLConnection connection = null;
		try {
		  connection =  new URL("http://explosm.net/comics/"+rand.nextInt(6000)+1).openConnection();
		  Scanner scanner = new Scanner(connection.getInputStream());
		  while(scanner.hasNext())
		  {
		  content = content+scanner.next();
		  }
		  scanner.close();
		  int index =content.indexOf("main-comic");
		  String part1 = content.substring(index);
		  int index1 = part1.indexOf(">");
		  return "http://"+part1.substring(18, index1-2);
		}catch ( IOException ex ) {
		   return getRandom();
		}
	}
}
