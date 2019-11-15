package diamssword.bot.actions;

import com.diamssword.bot.api.References;
import com.diamssword.bot.api.actions.IAction;
import com.diamssword.bot.api.audio.Langues;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class DadJokeAction implements IAction{

	@Override
	public void execute(MessageReceivedEvent event,String trigger, String before, String after) {
		if(!event.getAuthor().isFake() && !event.getAuthor().isBot())
		{
			
			if(event.isFromType(ChannelType.TEXT))
			{
				Member memb =event.getGuild().getMemberById(event.getAuthor().getId());
				VoiceChannel chan =memb.getVoiceState().getChannel();
				if(chan !=null)
				{
					References.PlayerManager.loadTrackForMember(References.TTSMaker.speak(Langues.en_us,"Hi "+after+" ! I'm Dad!").getAbsolutePath(),event.getGuild().getId(),memb);
				}
			}
			event.getChannel().sendMessage("Hi **"+after+"** ! I'm Dad!").queue();
		}

	}

	@Override
	public String[] getTriggers(Guild g) {
		return new String[] {"I'm","Im","I am","I Am","I'M","I AM"};
	}

	@Override
	public String usage() {
		return "Hi Stupid, I'm Dad!";
	}

	@Override
	public String name() {
		return "'Dad Jokes";
	}

	@Override
	public void init() {
		
	}

}
