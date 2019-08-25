package diamssword.bot.audio;

import com.diamssword.bot.api.audio.IChannelTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;

public class MemberTrack implements IChannelTrack{
	private final Member member;
	private AudioTrack track;
	public MemberTrack(Member member, AudioTrack track)
	{
		this.member = member;
		this.track = track;
	}
	@Override
	public AudioTrack getTrack() {
		return track;
	}

	@Override
	public VoiceChannel getChannel() {
		return member.getVoiceState().getChannel();
	}
	

}
