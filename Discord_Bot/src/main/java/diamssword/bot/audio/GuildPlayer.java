package diamssword.bot.audio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.diamssword.bot.api.References;
import com.diamssword.bot.api.audio.IChannelTrack;
import com.diamssword.bot.api.audio.IGuildPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import net.dv8tion.jda.core.entities.Guild;

public class GuildPlayer extends AudioEventAdapter implements IGuildPlayer {

	private AudioPlayer player;
	List<IChannelTrack> tracks = new ArrayList<IChannelTrack>();
	public final String guildID;
	private Guild guild;
	int pos =0;
	public GuildPlayer(String guildID)
	{
		this.guildID = guildID;
		getPlayer();
		getGuild();
	}
	public Guild getGuild()
	{
		if(guild == null)
			return guild = References.bot.getGuildById(guildID);
		return guild;	
	}
	public AudioPlayer getPlayer()
	{
		if(player == null)
		{
			player = GPlayerManager.playerManager.createPlayer();
			getGuild().getAudioManager().setSendingHandler(new AudioPlayHandler(player));
			player.removeListener(this);
			player.addListener(this);
			return	player;
		}
		return player;
	}
	@Override
	public void onPlayerPause(AudioPlayer player) {
	}

	@Override
	public void onPlayerResume(AudioPlayer player) {
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		new File(track.getInfo().uri).delete();
		if (endReason.mayStartNext) {
			pos+=1;
			if(pos >= tracks.size())
			{
				tracks.clear();
				pos=0;
				this.getGuild().getAudioManager().closeAudioConnection();
				return;
			}
			getGuild().getAudioManager().openAudioConnection(tracks.get(pos).getChannel());
			player.playTrack(tracks.get(pos).getTrack());
		}
		if(endReason== AudioTrackEndReason.CLEANUP)
		{
			guild = null;
			tracks.clear();
		}
	}

	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
		onTrackEnd(player,track, AudioTrackEndReason.LOAD_FAILED);
	}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
	}

	public void queue(IChannelTrack track) {
		tracks.add(track);
		GPlayerManager.LOG.log(Level.INFO,"Queuing "+track.getTrack().getIdentifier() );
		if(this.getPlayer().getPlayingTrack() == null)
		{	
			getGuild().getAudioManager().openAudioConnection(tracks.get(pos).getChannel());
			getGuild().getAudioManager().setSendingHandler(new AudioPlayHandler(player));
			this.getPlayer().playTrack(tracks.get(pos).getTrack());
		}


	}
	public void skip()
	{
			pos+=1;
			if(pos >= tracks.size())
			{
				tracks.clear();
				pos=0;
				this.getGuild().getAudioManager().closeAudioConnection();
				return;
			}
			getGuild().getAudioManager().openAudioConnection(tracks.get(pos).getChannel());
			player.playTrack(tracks.get(pos).getTrack());
	}
	public void clear()
	{
		this.getPlayer().stopTrack();
		this.getGuild().getAudioManager().closeAudioConnection();
		this.tracks.clear();
	}
}