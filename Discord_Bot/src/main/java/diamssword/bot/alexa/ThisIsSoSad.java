package diamssword.bot.alexa;

import java.io.IOException;

import com.diamssword.bot.api.References;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ThisIsSoSad implements ISubAlexa
{

	@Override
	public String[] getSubCommand() {
		return new String[] {"ThisIsSoSad"};
	}

	@Override
	public void execute(MessageReceivedEvent event, String trigger, String after) {
		YTVideoJson[] vids;
		try {
			vids = HttpClient.searchvid(5, "Toto Africa");
			if(vids.length >=1)
			{
				Member memb =event.getGuild().getMemberById(event.getAuthor().getId());
				References.PlayerManager.loadTrackForMember("https://www.youtube.com/watch?v="+vids[0].id.videoId, event.getGuild().getId(), memb);
				event.getChannel().sendMessage("...Play Africa BY Toto !").queue();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public String getName() {
		return "Africa!";
	}

	@Override
	public String getDesc() {
		return "";
	}

}