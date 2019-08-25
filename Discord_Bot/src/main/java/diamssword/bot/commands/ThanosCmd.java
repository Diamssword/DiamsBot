package diamssword.bot.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.diamssword.bot.api.References;
import com.diamssword.bot.api.actions.ICommand;
import com.diamssword.bot.api.actions.ITickable;
import com.diamssword.bot.api.actions.Registry;
import com.diamssword.bot.api.audio.Langues;
import com.diamssword.bot.api.permissions.Perm;
import com.diamssword.bot.api.utils.MembersUtil;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Invite;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.restaction.InviteAction;

public class ThanosCmd implements ICommand,ITickable{

	public List<String> list= new ArrayList<String>();
	public ThanosCmd()
	{

	}
	private Random rand = new Random();
	@Override
	public String getName() {

		return "snap";
	}

	@Override
	public String getDesc() {
		return "snap half of the guild members";
	}

	@Override
	public void execute(MessageReceivedEvent event, String ... args) {
		if(list.contains(event.getMember().getUser().getId()))
		{
			list.remove(event.getMember().getUser().getId());
			for(Member m : event.getGuild().getMembers())
			{
				if(m.getVoiceState().getChannel()!= null)
				{
					References.PlayerManager.loadTrackForMember(References.TTSMaker.speak(Langues.en_us, "I am inevitable.").getAbsolutePath(), event.getGuild().getId(), m);
					break;
				}
			}
			TextChannel tc =null;
			for(TextChannel chan : event.getGuild().getTextChannels())
			{
				if(chan.canTalk())
				{
					tc = chan;
					break;
				}
			}
			Invite inv =new InviteAction(References.bot, tc.getId()).setMaxUses(1).setMaxAge(0).complete();
			if(inv != null)
			{
				tc.sendMessage("http://aryatra.com/wp-content/uploads/2019/05/i-am-inevitable-thanos-300x166.jpg").queue();
				tc.sendMessage("https://imgur.com/a/dLJIK57").queue();
				List<Member> choosen = new ArrayList<Member>();
				while(choosen.size() < event.getGuild().getMembers().size()/2)
					for(Member memb : event.getGuild().getMembers())
					{
						if(choosen.size() >= event.getGuild().getMembers().size()/2 )
							break;
						if(rand.nextBoolean())
						{
							choosen.add(memb);
						}
					}
				for(Member memb: choosen)
				{
					MembersUtil.mentionMember(tc, memb, "**@member> I don't feel so good...**");
					memb.getUser().openPrivateChannel().complete().sendMessage("Hello, You have been saved by the great Titan!\n The snap erased you from the guild: "+event.getGuild().getName()+"! \n You can come back withthis invite: "+inv.getURL()).queue();
					event.getGuild().getController().kick(memb).queue();
				}


			}


		}
		else
		{
			event.getChannel().sendMessage("êtes vous sûr? Claquez une deuxième fois pour confirmer...").queue();
			list.add(event.getMember().getUser().getId());
		}
	}

	@Override
	public String getUsage() {
		return ">snap";
	}

	@Override
	public void init() {
		References.PermManager.addPermGeneral(this.getName(), Perm.OWNER);
		Registry.registerTickable(this);
	}

	@Override
	public void tick(Guild guild) {
		list.clear();

	}

	@Override
	public void tickSingle() {


	}

	@Override
	public int everyMS() {
		return 10000;
	}
}
