package diamssword.bot.commands;

import com.diamssword.bot.api.actions.ICommand;
import com.diamssword.bot.api.utils.StringUtils;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import diamssword.bot.actions.WordVoiceAction;
import diamssword.bot.audio.GPlayerManager;
import diamssword.bot.storage.savable.WordImgSavable;
import diamssword.bot.storage.savable.WordVoiceSavable;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class VoiceBindCmd implements ICommand{

	public VoiceBindCmd()
	{

	}
	@Override
	public String getName() {

		return "voicebind";
	}

	@Override
	public String getDesc() {
		return "bind an audio file to a sentence or a word";
	}

	@Override
	public void execute(MessageReceivedEvent event, String ... args) {
		if(!event.getAuthor().isFake() && !event.getAuthor().isBot())
		{
			if(args.length>=1)
			{
				if(args[0].equals("list"))
				{

					WordVoiceSavable ob =WordVoiceAction.storage.get(event.getGuild().getId());
					if(ob == null)
					{
						ob = new WordVoiceSavable();
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
					WordVoiceSavable ob =WordVoiceAction.storage.get(event.getGuild().getId());
					String res="";
					String trig = "";
					for(int i=0;i<splitInd;i++)
					{
						trig=trig+args[i]+" ";
					}
						res=args[splitInd+1];
					res =StringUtils.removeSpaces(res);
					trig =StringUtils.removeSpaces(trig);
				
					//if(validSong(res))
					{
						ob.data.put(trig.toLowerCase(), res);
						WordVoiceAction.storage.save(event.getGuild().getId());
						event.getChannel().sendMessage("'"+res+ "' binded to '"+trig+"'").queue();
					}
				//	event.getChannel().sendMessage("The link '"+res+"' is not a valid audio link").queue();
				}
				else
					event.getChannel().sendMessage("spearate the 2 parts with ' > '").queue();
					
			}
		}

	}

	@Override
	public String getUsage() {
		return ">voicebind <words> > <url>    />wordbind list";
	}

	@Override
	public void init() {
	}

	private boolean flag = false;
	private boolean flag1 = true;
	public boolean validSong(String url)
	{
		GPlayerManager.playerManager.loadItem(url, new AudioLoadResultHandler() {
			  @Override
			  public void trackLoaded(AudioTrack track) {
				flag =true;
				flag1 = false;
			  }

			  @Override
			  public void playlistLoaded(AudioPlaylist playlist) {
				  flag =true;
				  flag1 = false;
			  }

			  @Override
			  public void noMatches() {
				  flag =false;
				  flag1 = false;
			  }

			  @Override
			  public void loadFailed(FriendlyException throwable) {
				  flag =false;
				  flag1 = false;
			  }
			});
		while(flag1)
		{
			System.currentTimeMillis();
		}
		flag1 = true;
		return flag;
	}
}
