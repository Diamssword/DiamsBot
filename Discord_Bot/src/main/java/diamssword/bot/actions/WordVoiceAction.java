package diamssword.bot.actions;

import java.util.ArrayList;
import java.util.List;

import com.diamssword.bot.api.References;
import com.diamssword.bot.api.actions.IAction;
import com.diamssword.bot.api.storage.GuildStorage;

import diamssword.bot.storage.Storage;
import diamssword.bot.storage.savable.WordImgSavable;
import diamssword.bot.storage.savable.WordVoiceSavable;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class WordVoiceAction implements IAction{
	public static GuildStorage<WordVoiceSavable> storage = new GuildStorage<WordVoiceSavable>(WordVoiceSavable.class);
		@Override
		public void execute(MessageReceivedEvent event,String trigger, String before, String after) {
			if(!event.getAuthor().isFake() && !event.getAuthor().isBot())
			{
				WordImgSavable ob =storage.get(event.getGuild().getId());
				String rep=ob.data.get(trigger.toLowerCase());
				if(rep != null)
				{
					Member dest = null;
					if(event.getGuild().getMember(event.getAuthor()).getVoiceState().inVoiceChannel())
						dest = event.getGuild().getMember(event.getAuthor());
					else
					{
						for(VoiceChannel voice : event.getGuild().getVoiceChannels())
						{
							if(!voice.getMembers().isEmpty())
							{
								dest = voice.getMembers().get(0);
								break;
							}
						}
					}
					if(dest != null)
						References.PlayerManager.loadTrackForMember(rep, event.getGuild().getId(), dest);
				}
			}

		}

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
			return "Use >wordvoice command to bind a word or sentence to a audio file or link";
		}

		@Override
		public String name() {
			return "Word to Voice Binder";
		}

		@Override
		public void init() {
			Storage.list.add(storage);
		}
}
