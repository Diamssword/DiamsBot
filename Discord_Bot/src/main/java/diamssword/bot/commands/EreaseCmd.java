package diamssword.bot.commands;

import java.util.List;

import com.diamssword.bot.api.References;
import com.diamssword.bot.api.actions.ICommand;
import com.diamssword.bot.api.permissions.Perm;
import com.diamssword.bot.api.utils.MembersUtil;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class EreaseCmd implements ICommand{


	public EreaseCmd()
	{

	}
	@Override
	public String getName() {

		return "erase";
	}

	@Override
	public String getDesc() {
		return "erase all the message from the bot until a time or a date";
	}

	@Override
	public void execute(MessageReceivedEvent event, String ... args) {
		if(!event.getAuthor().isBot())
		{
			if(args.length>=1)
			{
				String userID = References.bot.getSelfUser().getId();

				long date = 0;
				boolean flag = args[0].equalsIgnoreCase("from");
				boolean flag2 = false;
				String time =flag? args[1] :args[0];
				if((flag && args.length>=3) || (!flag && args.length >=2))
				{
					String user = flag?args[2]:args[1];
					if(user.equalsIgnoreCase("all"))
						flag2 = true;
					List <Member> m =MembersUtil.getMembersMatching(event.getGuild().getMembers(), user);
					if(!m.isEmpty())
						userID = m.get(0).getUser().getId();
				}
				int month = 0;
				int year = 0;
				int day = 0;
				int hour = 0;
				int min = 0;
				int sec = 0;
				int yp =time.indexOf("Y");
				if(yp >0)
				{
					year = Integer.parseInt(time.substring(0,yp));
				}
				int Mp =time.indexOf("M");
				if(Mp >0)
				{
					month = Integer.parseInt(time.substring(yp+1,Mp));
				}
				int dp =time.indexOf("D");
				if(dp >0)
				{
					day = Integer.parseInt(time.substring(Mp+1,dp));
				}
				int hp =time.indexOf("h");
				if(hp >0 )
				{
					hour = Integer.parseInt(time.substring(dp+1,hp));
				}
				int mp =time.indexOf("m");
				if(mp >0 )
				{
					min = Integer.parseInt(time.substring(hp+1,mp));
				}
				int sp =time.indexOf("s");
				if(sp >0 )
				{
					sec = Integer.parseInt(time.substring(mp+1,sp));
				}
				date= ((long)year)*(31536000000l);
				date = date+(((long)month)*(2678400000l));
				date = date+(((long)day)*(86400000l));
				date=date+this.toMs(hour, min, sec);
				if(flag)
					date = System.currentTimeMillis()-date;
				else
				{
					if( date> System.currentTimeMillis())
					{
						event.getChannel().sendMessage("Can't delete a future message!");
						return;
					}

				}
				MessageHistory h1 = event.getChannel().getHistory();
				h1.retrievePast(100).complete();
				List<Message> hist =h1.getRetrievedHistory();
				for(Message msg : hist)
				{
					if(msg.getCreationTime().toInstant().toEpochMilli()>= date && (msg.getAuthor().getId().equals(userID)|| flag2))
					{
						event.getChannel().deleteMessageById(msg.getId()).queue();
					}
				}
			}
		}
	}
	private long toMs(int hour,int min,int sec)
	{
		long res= ((long)hour)*3600000l;
		res=res+ ((long)min)*60000l;
		res = res+ ((long)sec)*1000l;
		return res;
	}
	@Override
	public String getUsage() {
		return ">erase from <time> (author)|| >erase <date> (author)";
	}

	@Override
	public void init() {
		References.PermManager.addPermGeneral(this.getName(), Perm.ADMIN);

	}

}
