package diamssword.bot.alexa;

import java.util.ArrayList;
import java.util.List;

import com.diamssword.bot.api.References;
import com.diamssword.bot.api.audio.IGuildPlayer;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
public class AlexaControls
{
	public static class Skip implements ISubAlexa
	{

		@Override
		public String[] getSubCommand() {
			return new String[] {"skip","sk","s"};
		}
		@Override
		public void execute(MessageReceivedEvent event, String trigger, String after) {

			IGuildPlayer p=	References.PlayerManager.getGuildPlayer(event.getGuild().getId());
			if(p.getPlayer().getPlayingTrack() != null)
				event.getChannel().sendMessage("**Skiped "+p.getPlayer().getPlayingTrack().getInfo().title+"**").queue();
			else
				event.getChannel().sendMessage("**Nothing to Skip!**").queue();	
			p.skip();
		}
		@Override
		public String getName() {
			return "Skip";
		}
		@Override
		public String getDesc() {
			return "Skip the current played song";
		}


	}
	public static class Stop implements ISubAlexa
	{

		@Override
		public String[] getSubCommand() {
			return new String[] {"stop","stfu"};
		}
		@Override
		public void execute(MessageReceivedEvent event, String trigger, String after) {

			IGuildPlayer p=	References.PlayerManager.getGuildPlayer(event.getGuild().getId());
			if(p.getPlayer().getPlayingTrack() != null)
				event.getChannel().sendMessage("**Stopped all musics**").queue();
			else
				event.getChannel().sendMessage("**Nothing to Stop!**").queue();	
			p.clear();
		}
		@Override
		public String getName() {
			return "Stop";
		}
		@Override
		public String getDesc() {
			return "Remove all the queued song";
		}
	}
	
	public static interface IAlexaControl
	{
		public String[] acceptedTriggers();
		public void onTriggerFound(MessageReceivedEvent event, String trigger);
		public String channelId();
		public String memberId();
	}
	
	public static List<IAlexaControl> WAITING_CONTROLS = new ArrayList<IAlexaControl>();
	
}
