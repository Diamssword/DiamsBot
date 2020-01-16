package diamssword.bot.alexa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.diamssword.bot.api.References;
import com.diamssword.bot.api.storage.JsonGuildStorage;

import diamssword.bot.alexa.AlexaControls.IAlexaControl;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
public class AlexaPlaylist  implements ISubAlexa,IAlexaControl
{
	public static JsonGuildStorage<PlaylistData> storage = new JsonGuildStorage<PlaylistData>("alexa/playlist.json", PlaylistData.class);
	List<YTVideoJson> videos = new ArrayList<YTVideoJson>();
	private String channel;
	private String member;

	@Override
	public String[] getSubCommand() {
		return new String[] {"playlist","pl"};
	}
	@Override
	public void execute(MessageReceivedEvent event, String trigger, String after) {
		if(after.toLowerCase().startsWith("add"))
		{
			String content = after.trim().replaceFirst("add", "");
			YTVideoJson[] vids = new YTVideoJson[0];
			try {
				vids=HttpClient.searchvid(5, content);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(vids.length==1)
			{
				this.addToPlaylist(vids[0], event.getAuthor(),event.getTextChannel());
			}
			if(vids.length == 0)
			{
				event.getChannel().sendMessage("**Sorry, nothing found for "+content+"**").queue();
			}
			else
			{
				String txt ="";
				int i = 1;
				for(YTVideoJson vid : vids)
				{
					txt= txt+i+" "+vid.snippet.title+"  BY "+vid.snippet.channelTitle+"\n";
					videos.add(vid);
					i++;
				}
				txt=txt+"**Type 1,2,3.... to select your choice**";
				event.getChannel().sendMessage(txt).queue();
				this.channel =event.getChannel().getId();
				this.member = event.getAuthor().getId();
				AlexaControls.WAITING_CONTROLS.add(this);
			}
		}
		else if(after.toLowerCase().startsWith("play"))
		{

			Member memb =event.getGuild().getMemberById(event.getAuthor().getId());
			PlaylistData pl =storage.get(References.GLOBAL_STORAGE_ID).get();
			if(pl != null)
			{
				List<YTVideoJson> ls=pl.get(event.getAuthor().getId());
				if(ls.isEmpty())
				{
					event.getChannel().sendMessage("** your playlist is empty! do 'alexa playlist add <video title or url>' to add songs**").queue();
					return;
				}
				event.getChannel().sendMessage("**Queuing your playlist ! You can use 'alexa skip' to skip a song or 'alexa stop' to clear all**").queue();
				for(YTVideoJson vid : ls)
				{
				References.PlayerManager.loadTrackForMember("https://www.youtube.com/watch?v="+vid.id.videoId, event.getGuild().getId(), memb);
				}
			}
		}
	}
	@Override
	public String getName() {
		return "Playlist";
	}
	@Override
	public String getDesc() {
		return "Personal music playlist : pl add <url or title> / play / select";
	}
	public static class PlaylistData 
	{
		public Map<String,List<YTVideoJson>> datas = new HashMap<String,List<YTVideoJson>>();
		public void add(String userID,YTVideoJson video)
		{
			List<YTVideoJson> ls=datas.get(userID);
			if(video != null) {
				if(ls == null)
				{
					ls = new ArrayList<YTVideoJson>();
					datas.put(userID, ls);
				}
				video.snippet.channelTitle = null;
				video.id.kind= null;
				ls.add(video);
			}

		}
		public List<YTVideoJson> get(String userID)
		{
			List<YTVideoJson> res=this.datas.get(userID);
			if(res==null)
				res = new ArrayList<YTVideoJson>();
			return res;
		}

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
		this.addToPlaylist(videos.get(i), event.getAuthor(),event.getTextChannel());
	}
	@Override
	public String channelId() {
		return this.channel;
	}
	@Override
	public String memberId() {
		return this.member;
	}

	public void addToPlaylist(YTVideoJson vid,User user,TextChannel channel)
	{
		PlaylistData pl =storage.get(References.GLOBAL_STORAGE_ID).get();
		if(pl == null)
		{
			pl = new PlaylistData();
			storage.get(References.GLOBAL_STORAGE_ID).set(pl);
		}
		pl.add(user.getId(), vid);
		storage.save(References.GLOBAL_STORAGE_ID);
		channel.sendMessage("**"+vid.snippet.title+" added to your playlist!**" ).queue();
	}
}
