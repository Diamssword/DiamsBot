package diamssword.bot;

import java.util.ArrayList;
import java.util.List;

import com.diamssword.bot.api.References;
import com.diamssword.bot.api.actions.IAction;
import com.diamssword.bot.api.actions.ICommand;
import com.diamssword.bot.api.actions.Registry;
import com.diamssword.bot.api.utils.StringUtils;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.StatusChangeEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
/**
 * Handle the mains events of the Bot (message receiving,states changes...)
 * @author Diamssword
 *
 */
public class BotEventsHandler extends ListenerAdapter{


	/**
	 * Called when the state of the bot change (connected, loading...)
	 */
	@Override
	public void onStatusChange(StatusChangeEvent event)
	{
		if(Main.uiFrame != null)
			Main.uiFrame.botStat.setStatus(event.getNewStatus());
	}

	/**
	 *Handle all the text messages received and launch a commands or a Action if the good string is found
	 */
	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		//part wich handle the commands (starting with">")
		if(event.getMessage().getContentDisplay().startsWith(">"))
		{
			String msg = StringUtils.removeSpaces(event.getMessage().getContentRaw());
			msg = msg.substring(1);
			String[] args = msg.split(" ");
			for(ICommand cmd : Registry.commands)
			{
				if(cmd.getName().toLowerCase().equals(args[0].toLowerCase()))
				{
					if(event.getChannel().getType() == ChannelType.PRIVATE || References.PermManager.canUse(event.getMember(), cmd))
					{
						List<String> args1 = new ArrayList<String>();
						for(int i=1;i < args.length;i++)
						{
							if(!args[i].equals("") && !args[i].equals(" "))
								args1.add(args[i]);
						}
						String[] argsF= new String[0];
						if(!args1.isEmpty() && !(args1.size() ==1 && (args1.get(0).equals("") || args1.get(0).equals(" "))))
							argsF= args1.toArray(argsF);
						String[] argsFinal= argsF;
						new Thread(new Runnable() {


							@Override
							public void run() {
								try {
									cmd.getClass().newInstance().execute(event, argsFinal);
								} catch (InstantiationException | IllegalAccessException e) {
									cmd.execute(event, argsFinal);
								}

							}}).start();

					}
					else
					{
						event.getChannel().sendMessage(" :no_entry_sign: **You are not allowed to use this command!**").queue();
					}
					break;
				}

			}
		}	//part wich handle the Actions (containing a word or sentence anywhere in the message)
		else {
			String msg = event.getMessage().getContentRaw();
			for(IAction action : Registry.actions)
			{
				for(String s : action.getTriggers(event.getGuild()))
				{
					if(msg.toLowerCase().contains(s.toLowerCase()))
					{

						int index = msg.toLowerCase().indexOf(s.toLowerCase());
						String first = msg.substring(0, index);
						String last = msg.substring(index+s.length());
						if(!action.shouldBeSpaced() || ((first.length() < 2 ||first.endsWith(" ")) && (last.length() <2 ||last.startsWith(" "))))
						{
							new Thread(new Runnable() {


								@Override
								public void run() {
									try {
										action.getClass().newInstance().execute(event ,s, first,last);
									} catch (InstantiationException | IllegalAccessException e) {
										action.execute(event,s, first,last);
									}

								}}).start();
							break;
						}
					}

				}
			}

			//dedicated THE Duck
			if(event.getMember().getUser().getName().toLowerCase().contains("duck"))
			{
				event.getChannel().addReactionById(event.getMessageId(), event.getGuild().getEmotesByName("rooKwak",true).get(0)).queue();;
			}
		}
	}

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event)
	{

		//GuildController ctrl = new GuildController(event.getGuild());
		//ctrl.setNickname(event.getGuild().getMemberById(bot.getSelfUser().getId()), "Père de toutes choses").queue();

	}
}
