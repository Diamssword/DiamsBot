package diamssword.bot.alexa;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.diamssword.bot.api.actions.ITickable;
import com.diamssword.bot.api.storage.JsonGuildStorage;
import com.diamssword.bot.api.utils.MembersUtil;
import com.diamssword.bot.api.utils.StringUtils;

import diamssword.bot.alexa.RemindAlexa.RemindDatas.Reminder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class RemindAlexa implements ISubAlexa,ITickable
{

	public static JsonGuildStorage<RemindDatas> storage = new JsonGuildStorage<RemindDatas>("alexa/remind.json", RemindDatas.class);
	@Override
	public String[] getSubCommand() {
		return new String[] {"remind","remember"};
	}

	@Override
	public void execute(MessageReceivedEvent event, String trigger, String after) {
		Reminder rem =new Reminder();
		String[] parts = after.split(" ");
		rem.sourceID=event.getAuthor().getId();
		rem.ChannelID = event.getChannel().getId();
		if(parts[0].equals("me"))
		{
			rem.destinationID = event.getAuthor().getId();

		}
		else
		{
			List<Member> members=MembersUtil.getMembersMatching(event.getGuild().getMembers(),parts[0]);
			if(members.isEmpty())
			{
				event.getChannel().sendMessage(":exclamation: wrong usage or not found member : alexa remind <me/member name>").queue();
				return;
			}
			rem.destinationID =members.get(0).getUser().getId();
		}
		if(parts.length>=2)
		{
			long date = 0;
			String message;
			boolean flag = parts[1].equalsIgnoreCase("in");

			String time =flag? parts[2] :parts[1];
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
				date = System.currentTimeMillis()+date;
			else
			{
				if( date< System.currentTimeMillis()+10)
				{
					event.getChannel().sendMessage("Can't remind something in the past!");
					return;
				}

			}
			message = StringUtils.weldArgs(parts, flag?3:2, parts.length);
			rem.time = date;
			rem.message = message;
			storage.get(event.getGuild().getId()).get().list.add(rem);
			storage.save(event.getGuild());
			MembersUtil.mentionMember(event.getChannel(), event.getGuild().getMemberById(rem.destinationID), "Reminder registred : Remind to @member the "+DateFormat.getInstance().format(new Date(rem.time))+" to **"+rem.message+"**");
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
	public String getName() {
		return "Remind";
	}

	@Override
	public String getDesc() {
		return "Remind <@somone/me> (in) YMDhms <message> : exemple> remind me in 10D2h drink milk where Y:year,M:month,D:day,h:hour,m:minutes,s:seconds  ";
	}

	public static class RemindDatas
	{
		List<Reminder> list = new ArrayList<Reminder>();
		public static class Reminder
		{
			public String sourceID;
			public String destinationID;
			public String ChannelID;
			public String message;
			public long time;
		}
	}

	@Override
	public void tick(Guild guild) {
		List<Reminder> ls = new ArrayList<Reminder>();
		ls.addAll(storage.get(guild.getId()).get().list);
		for(Reminder rem : ls)
		{
			if(rem.time <= System.currentTimeMillis())
			{
				storage.get(guild.getId()).get().list.remove(rem);
				
				String msg="Hey <@"+rem.destinationID+">! @member want to reminds you : **"+rem.message+"**!";
				if(rem.sourceID.equals(rem.destinationID))
					msg="Hey <@"+rem.destinationID+">! You asked me to remind you : **"+rem.message+"**!";
				MembersUtil.mentionMember(guild.getTextChannelById(rem.ChannelID), guild.getMemberById(rem.sourceID), msg);
			}
		}
		storage.save(guild.getId());
	}

	@Override
	public void tickSingle() {

	}

	@Override
	public int everyMS() {
		return 1000;
	}

	@Override
	public void init() {

	}

}