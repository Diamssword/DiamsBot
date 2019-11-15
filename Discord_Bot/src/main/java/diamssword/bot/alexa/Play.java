package diamssword.bot.alexa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.diamssword.bot.api.References;

import diamssword.bot.alexa.AlexaControls.IAlexaControl;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Play implements ISubAlexa,IAlexaControl
{
	private List<String> videos = new ArrayList<String>();
	private String channel;
	private String member;
	@Override
	public String[] getSubCommand() {
		return new String[] {"play","p"};
	}
	@Override
	public void execute(MessageReceivedEvent event, String trigger, String after) {
		YTVideoJson[] vids;
		try {
			vids = HttpClient.searchvid(5, after);
			if(vids.length==1)
			{
				Member memb =event.getGuild().getMemberById(event.getAuthor().getId());
				References.PlayerManager.loadTrackForMember("https://www.youtube.com/watch?v="+vids[0].id.videoId, event.getGuild().getId(), memb);
				event.getChannel().sendMessage("**Playing https://www.youtube.com/watch?v="+vids[0].id.videoId+"**").queue();
			}
			else if(vids.length <=0)
			{
				event.getChannel().sendMessage("**Nothing found for "+after+"**").queue();	
			}
			else
			{
				String txt ="";
				int i = 1;
				for(YTVideoJson vid : vids)
				{
					txt= txt+i+" "+vid.snippet.title+"  BY "+vid.snippet.channelTitle+"\n";
					videos.add(vid.id.videoId);
					i++;
				}
				txt=txt+"**Type 1,2,3.... to select your choice**";
				event.getChannel().sendMessage(txt).queue();
				this.channel =event.getChannel().getId();
				this.member = event.getAuthor().getId();
				AlexaControls.WAITING_CONTROLS.add(this);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public String getName() {
		return "Play";
	}
	@Override
	public String getDesc() {
		return "Search and play a song";
	}
	@Override
	public String[] acceptedTriggers() {
		List<String> res= new ArrayList<String>();
		for(int i=1;i<= videos.size();i++)
			res.add(i+"");
		return res.toArray(new String[0]);
	}
	@Override
	public void onTriggerFound(MessageReceivedEvent event, String trigger) {
			int i = Integer.parseInt(trigger);
			Member memb =event.getGuild().getMemberById(event.getAuthor().getId());
			References.PlayerManager.loadTrackForMember("https://www.youtube.com/watch?v="+videos.get(i-1), event.getGuild().getId(), memb);
			event.getChannel().sendMessage("**Playing https://www.youtube.com/watch?v="+videos.get(i-1)+"**").queue();
	}
	@Override
	public String channelId() {
		return this.channel;
	}
	@Override
	public String memberId() {
		return this.member;
	}
}