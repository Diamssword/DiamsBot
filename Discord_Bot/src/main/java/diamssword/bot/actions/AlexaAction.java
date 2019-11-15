package diamssword.bot.actions;

import java.util.ArrayList;
import java.util.List;

import com.diamssword.bot.api.References;
import com.diamssword.bot.api.actions.IAction;
import com.diamssword.bot.api.utils.StringUtils;

import diamssword.bot.alexa.AlexaControls;
import diamssword.bot.alexa.AlexaControls.IAlexaControl;
import diamssword.bot.alexa.AlexaControls.Skip;
import diamssword.bot.alexa.AlexaControls.Stop;
import diamssword.bot.alexa.Help;
import diamssword.bot.alexa.ISubAlexa;
import diamssword.bot.alexa.Play;
import diamssword.bot.alexa.PlayNow;
import diamssword.bot.alexa.RemindAlexa;
import diamssword.bot.alexa.ThisIsSoSad;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class AlexaAction implements IAction{

	private static List<Class <? extends ISubAlexa>> subsAlexa = new ArrayList<Class <? extends ISubAlexa>>();
	@Override
	public void execute(MessageReceivedEvent event,String trigger, String before, String after) {
		if(!event.getAuthor().isFake() && !event.getAuthor().isBot())
		{
			if(before.equals(""))
			{
				if(trigger.equalsIgnoreCase("Alexa"))
				{
					String[] parts =after.replaceFirst(" ", "").split(" ");
					for(Class <? extends ISubAlexa> subC: subsAlexa)
					{
						ISubAlexa sub;
						try {
							sub = subC.newInstance();
						} catch (InstantiationException | IllegalAccessException e) {
							e.printStackTrace();
							event.getChannel().sendMessage(":warning: HU-HO    Ching Chong, somthing gone wong").queue();
							return;
						}
						for(String trig : sub.getSubCommand())
						{
							if(trig.equalsIgnoreCase(parts[0]))
							{
								sub.execute(event, parts[0], StringUtils.weldArgs(parts, 1, parts.length));
								return;
							}
						}
					}
					new Help().execute(event, trigger, after);
				}
				else
				{
					List<IAlexaControl> ls =new ArrayList<IAlexaControl>();
					ls.addAll(AlexaControls.WAITING_CONTROLS);
					for(IAlexaControl ctrl :ls)
					{
						if(ctrl.channelId().equals(event.getChannel().getId()))
						{
							if(ctrl.memberId().equals(event.getAuthor().getId()))
							{
								for(String s: ctrl.acceptedTriggers())
								{
									if(s.equalsIgnoreCase(trigger))
									{
										ctrl.onTriggerFound(event, trigger);
										AlexaControls.WAITING_CONTROLS.remove(ctrl);
										return;
									}
								}
							}
						}
					}
				}
			}

		}

	}

	@Override
	public String[] getTriggers(Guild g) {
		List<String> res = new ArrayList<String>();
		res.add("Alexa");
		for(IAlexaControl ctrl :AlexaControls.WAITING_CONTROLS)
		{
			for(String str : ctrl.acceptedTriggers())
			{
				res.add(str);
			}
		}
		return res.toArray(new String[0]);
	}

	@Override
	public String usage() {
		return "alexa <subcommand> ore help";
	}

	@Override
	public String name() {
		return "Alexa compagnon";
	}

	@Override
	public void init() {
		subsAlexa.add(Help.class);
		subsAlexa.add(PlayNow.class);
		subsAlexa.add(Play.class);
		subsAlexa.add(Skip.class);
		subsAlexa.add(Stop.class);
		subsAlexa.add(RemindAlexa.class);
		subsAlexa.add(ThisIsSoSad.class);
		References.Storage.registerStorage(RemindAlexa.storage);
	}



	public static List<Class<? extends ISubAlexa>> getSubsAlexa()
	{
		return subsAlexa;
	}
	public static void addSubAlexa(Class<? extends ISubAlexa> clazz)
	{
		subsAlexa.add(clazz);
	}



}
