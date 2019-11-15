package diamssword.bot.alexa;

import java.io.IOException;

import com.diamssword.bot.api.References;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PlayNow implements ISubAlexa
{

	@Override
	public String[] getSubCommand() {
		return new String[] {"playnow","playn","pn"};
	}

	@Override
	public void execute(MessageReceivedEvent event, String trigger, String after) {
		YTVideoJson[] vids;
		try {
			vids = HttpClient.searchvid(5, after);
			if(vids.length >=1)
			{
				Member memb =event.getGuild().getMemberById(event.getAuthor().getId());
				References.PlayerManager.loadTrackForMember("https://www.youtube.com/watch?v="+vids[0].id.videoId, event.getGuild().getId(), memb);
				event.getChannel().sendMessage("**Playing https://www.youtube.com/watch?v="+vids[0].id.videoId+"**").queue();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public String getName() {
		return "PlayNow";
	}

	@Override
	public String getDesc() {
		return "play the first youtbue video found whit the research, Usage: play <research>";
	}

}