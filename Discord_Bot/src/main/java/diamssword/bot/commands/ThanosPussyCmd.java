package diamssword.bot.commands;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.diamssword.bot.api.References;
import com.diamssword.bot.api.actions.ICommand;
import com.diamssword.bot.api.actions.ITickable;
import com.diamssword.bot.api.actions.Registry;
import com.diamssword.bot.api.audio.Langues;
import com.diamssword.bot.api.utils.MembersUtil;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ThanosPussyCmd implements ICommand,ITickable{

	public List<String> list= new ArrayList<String>();
	public ThanosPussyCmd()
	{

	}
	private Random rand = new Random();
	@Override
	public String getName() {

		return "snapPussy";
	}

	@Override
	public String getDesc() {
		return "snap half of the guild members (but doesn't kick them)";
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
			TextChannel tc =event.getTextChannel();
			Role role = null;
			List<Role> roles =event.getGuild().getRolesByName("Soul Stone", true);
			if(roles.isEmpty() || !roles.get(0).getName().equalsIgnoreCase("Soul Stone"))
				role =event.getGuild().getController().createRole().setColor(new Color(255, 183, 15)).setName("Soul Stone").setMentionable(true).complete();
			else
				role = roles.get(0);
			tc.sendMessage("http://aryatra.com/wp-content/uploads/2019/05/i-am-inevitable-thanos-300x166.jpg").queue();
			tc.sendMessage("https://imgur.com/a/dLJIK57").queue();
			List<Member> choosen = new ArrayList<Member>();
			while(choosen.size() < event.getGuild().getMembers().size()/2)
				for(Member memb : event.getGuild().getMembers())
				{	
					event.getGuild().getController().removeRolesFromMember(memb, role).queue();
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
				event.getGuild().getController().addRolesToMember(memb, role).queue();
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
		return ">snapPussy";
	}

	@Override
	public void init() {
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
