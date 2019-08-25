package diamssword.bot.audio;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.diamssword.bot.api.References;
import com.diamssword.bot.api.audio.IChannelTrack;
import com.diamssword.bot.api.audio.IGPlayerManager;
import com.diamssword.bot.api.audio.IGuildPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.core.entities.Invite.Guild;
import net.dv8tion.jda.core.entities.Member;

public class GPlayerManager implements IGPlayerManager {

	public final static AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
	public static final Logger LOG = References.getLogger("DiamsBot-Audio");
	private Map<String,GuildPlayer> players = new HashMap<String,GuildPlayer>();
	public GPlayerManager()
	{
		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);
		playerManager.setPlayerCleanupThreshold(10000);
		
	}
	@Override
	public IGuildPlayer getGuildPlayer(String guildID)
	{
		GuildPlayer play= players.get(guildID);
		if(play==null)
		{
			play = new GuildPlayer(guildID);
			players.put(guildID, play);
		}
		return (IGuildPlayer) play;
	}
	@Override
	public IGuildPlayer getGuildPlayer(Guild g)
	{
		return this.getGuildPlayer(g.getId());
	}
	@Override
	public void queueTrack(String GuildID,IChannelTrack track)
	{
		this.getGuildPlayer(GuildID).queue(track);
	}
	@Override
	public void queueTrack(Guild guild,IChannelTrack track)
	{
		this.getGuildPlayer(guild).queue(track);
	}
	@Override
	public void loadTrackForMember(String source, String guildID,Member member )
	{
		playerManager.loadItem(source, new AudioLoadResultHandler() {
			  @Override
			  public void trackLoaded(AudioTrack track) {
			queueTrack(guildID,(IChannelTrack) new MemberTrack(member,track));
			  }

			  @Override
			  public void playlistLoaded(AudioPlaylist playlist) {
			
			  }

			  @Override
			  public void noMatches() {
			    LOG.log(Level.WARNING,"Source can't be loaded: "+source);
			  }

			  @Override
			  public void loadFailed(FriendlyException throwable) {
				   LOG.log(Level.WARNING,throwable.getMessage());
			  }
			});
	}
}
