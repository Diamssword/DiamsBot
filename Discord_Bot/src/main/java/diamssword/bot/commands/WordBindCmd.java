package diamssword.bot.commands;

import com.diamssword.bot.api.actions.ICommand;
import com.diamssword.bot.api.utils.StringUtils;

import diamssword.bot.actions.WordImgAction;
import diamssword.bot.storage.savable.WordImgSavable;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class WordBindCmd implements ICommand{

	public WordBindCmd()
	{

	}
	@Override
	public String getName() {

		return "wordbind";
	}

	@Override
	public String getDesc() {
		return "bind an image to a setence or word";
	}

	@Override
	public void execute(MessageReceivedEvent event, String ... args) {
		if(!event.getAuthor().isFake() && !event.getAuthor().isBot())
		{
			if(args.length>1)
			{
				if(args[0].equals("list"))
				{

					WordImgSavable ob =WordImgAction.storage.get(event.getGuild().getId());
					if(ob == null)
					{
						ob = new WordImgSavable();
					}
					String res = "List of binded words :\n";
					
					for(String key :ob.data.keySet())
					{
						res=res+key+"  >  "+ob.data.get(key)+"\n";
					}
					event.getChannel().sendMessage(res).queue();
					return;
				}
				int splitInd =0;
				for(int i=0;i<args.length;i++)
				{
					if(args[i].replaceAll(" ", "").equals(">"))
						splitInd=i;
				} 

				if(splitInd!=0)
				{
					WordImgSavable ob =WordImgAction.storage.get(event.getGuild().getId());
					if(ob == null)
					{
						ob = new WordImgSavable();
					}
					String res="";
					String trig = "";
					for(int i=0;i<splitInd;i++)
					{
						trig=trig+args[i]+" ";
					}
					for(int i=splitInd+1;i<args.length;i++)
					{
						res=res+args[i]+" ";
					}
					res =StringUtils.removeSpaces(res);
					trig =StringUtils.removeSpaces(trig);
					ob.data.put(trig.toLowerCase(), res);
					WordImgAction.storage.save(event.getGuild().getId());
					
					event.getChannel().sendMessage("'"+res+ "' binded to '"+trig+"'").queue();
				}
				else
					event.getChannel().sendMessage("spearate the 2 parts with ' > '").queue();
					
			}
		}

	}

	@Override
	public String getUsage() {
		return ">wordbind <words> > <url/words/emote>     />wordbind list";
	}

	@Override
	public void init() {
	}
}
