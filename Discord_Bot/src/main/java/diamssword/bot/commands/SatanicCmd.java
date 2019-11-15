package diamssword.bot.commands;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.imageio.ImageIO;

import com.diamssword.bot.api.actions.ICommand;
import com.diamssword.bot.api.utils.LoadUtils;
import com.diamssword.bot.api.utils.StringUtils;
import com.google.gson.Gson;

import diamssword.bot.utils.RedditJson;
import diamssword.bot.utils.SatanicFont;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SatanicCmd implements ICommand{

	public SatanicCmd()
	{

	}
	@Override
	public String getName() {

		return "satanic";
	}

	@Override
	public String getDesc() {
		return "translate a sentence to satanic";
	}

	@Override
	public void execute(MessageReceivedEvent event, String ... args) {
		if(!event.getAuthor().isFake())
		{
			if(args.length>=1)
			{
				String txt = StringUtils.weldArgs(args, 0, args.length);
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				try {
					ImageIO.write(SatanicFont.fromString(txt), "png", os);
				} catch (IOException e) {
					e.printStackTrace();
				}
				LoadUtils.sendFile(event.getChannel(), os.toByteArray(), txt+".png", "");
			}
		}
	}

	@Override
	public String getUsage() {
		return ">satanic <sentence>";
	}

	@Override
	public void init() {

	}
}
