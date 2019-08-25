package diamssword.bot.commands;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

import com.diamssword.bot.api.actions.ICommand;
import com.diamssword.bot.api.utils.LoadUtils;
import com.google.gson.Gson;

import diamssword.bot.utils.FactJson;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class FactCmd implements ICommand{

	public static String[] sources=new String[] {"https://some-random-api.ml/facts/fox","http://randomuselessfact.appspot.com/random.json?language=en","http://randomuselessfact.appspot.com/random.json?language=en","http://randomuselessfact.appspot.com/random.json?language=en","https://some-random-api.ml/facts/cat","https://some-random-api.ml/facts/dog","https://some-random-api.ml/facts/bird","https://some-random-api.ml/facts/koala","https://some-random-api.ml/facts/panda"};
	public FactCmd()
	{

	}
	private Random rand = new Random();
	@Override
	public String getName() {

		return "fact";
	}

	@Override
	public String getDesc() {
		return "give you a random fact";
	}

	@Override
	public void execute(MessageReceivedEvent event, String ... args) {
		if(!event.getAuthor().isFake() && !event.getAuthor().isBot())
		{


			try {
				String res = LoadUtils.urlToString(new URL(sources[rand.nextInt(sources.length)]));
				FactJson post = new Gson().fromJson(res, FactJson.class);
				if(post == null)
				{
					event.getChannel().sendMessage("I have fucked up somwhere :/").queue();
					return;
				}
				if(post.fact == null ||post.fact.length()<2)
				{
				String text = "```bash\n\""+post.text+"\"\n```Source: "+post.source_url;
				event.getChannel().sendMessage(text).queue();
				}else
				{
					String text = "```bash\n\""+post.fact+"\"\n```";
					event.getChannel().sendMessage(text).queue();
				}
						
			} catch (IOException e) {
				e.printStackTrace();
				event.getChannel().sendMessage("I have fucked up somwhere :/").queue();
			}
		}
	}

	@Override
	public String getUsage() {
		return ">fact";
	}

	@Override
	public void init() {

	}
}
