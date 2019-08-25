package diamssword.bot.actions;

import java.util.ArrayList;
import java.util.List;

import com.diamssword.bot.api.actions.IAction;
import com.diamssword.bot.api.storage.GuildStorage;

import diamssword.bot.storage.Storage;
import diamssword.bot.storage.savable.WordImgSavable;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class WordImgAction implements IAction{
	public static GuildStorage<WordImgSavable> storage = new GuildStorage<WordImgSavable>(WordImgSavable.class);
	@Override
	public void execute(MessageReceivedEvent event,String trigger, String before, String after) {
		if(!event.getAuthor().isFake() && !event.getAuthor().isBot())
		{
			WordImgSavable ob =storage.get(event.getGuild().getId());
			if(ob != null)
			{
				String rep=ob.data.get(trigger.toLowerCase());
				if(rep != null)
				event.getChannel().sendMessage(rep).queue();;
			}
		}

	}

	/*public void updateTriggers(Guild g)
	{
		int index = -1;
		for(int i=0;i<guildRefresh.size();i++ )
		{
			if(guildRefresh.get(i).equals(g.getId()))
			{
				index = i;
				break;
			}
		}
		if(index>-1 || firstLaunch)
		{
			if(!firstLaunch)
				guildRefresh.remove(index);
			

			
			firstLaunch=false;
		}
	}*/
	@Override
	public String[] getTriggers(Guild g) {
		List<String> ls = new ArrayList<String>();
		WordImgSavable ob =storage.get(g.getId());;
		if(ob != null)
		{
			for(String str : ob.data.keySet())
			{
				ls.add(str);
			}
		}
		return  ls.toArray(new String[0]);
	}

	@Override
	public String usage() {
		return "Use >wordBind command to bind a image to a phrase";
	}

	@Override
	public String name() {
		return "Word to Image Binder";
	}

	@Override
	public void init() {
		Storage.list.add(storage);
	}
}
