package diamssword.bot.commands;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.diamssword.bot.api.actions.ICommand;
import com.google.gson.Gson;

import diamssword.bot.utils.RedditJson;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ImageCmd implements ICommand{

	public ImageCmd()
	{

	}
	@Override
	public String getName() {

		return "meme";
	}

	@Override
	public String getDesc() {
		return "show a random meme";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(MessageReceivedEvent event, String ... args) {
		if(!event.getAuthor().isFake())
		{

			Callable t =new Callable() {

				@Override
				public Object call() throws Exception {

					String sub = "";
					if(args.length >= 1 && !args[0].equals(""))
					{
						sub="/"+args[0];
					}
					try {
						Scanner s = new Scanner(new URL("https://meme-api.herokuapp.com/gimme"+sub).openStream());
						String res = "";
						while(s.hasNextLine())
						{
							res = res+s.nextLine();
						}
						RedditJson post = new Gson().fromJson(res, RedditJson.class);
						if(post.title == null || post.url == null)
						{
							event.getChannel().sendMessage("Nothing found on this sub T.T").queue();
							s.close();
							return null;
						}
						event.getChannel().sendMessage(post.title+"\n"+post.url).queue();
						s.close();
					} catch (IOException e) {
						event.getChannel().sendMessage("No subreddit found for "+sub).queue();
					}
					return null;
				}};
				ExecutorService executor = Executors.newSingleThreadExecutor();
				try {
					 executor.submit(t).get(6, TimeUnit.SECONDS);
				} catch (InterruptedException | ExecutionException | TimeoutException e) {
					event.getChannel().sendMessage("No subreddit found for this sub").queue();
				}
				executor.shutdown();
		}
	}

	@Override
	public String getUsage() {
		return ">meme <subreddit>";
	}

	@Override
	public void init() {
		
	}
}
