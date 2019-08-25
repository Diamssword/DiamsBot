package diamssword.bot.commands;

import java.util.List;

import com.diamssword.bot.api.References;
import com.diamssword.bot.api.actions.ICommand;
import com.diamssword.bot.api.actions.Registry;
import com.diamssword.bot.api.permissions.EPermFilter;
import com.diamssword.bot.api.permissions.Perm;
import com.diamssword.bot.api.utils.MembersUtil;

import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PermCmd implements ICommand{

	public PermCmd()
	{

	}
	@Override
	public String getName() {

		return "perms";
	}

	@Override
	public String getDesc() {
		return "used to change permissions of each commands";
	}

	@Override
	public void execute(MessageReceivedEvent event, String ... args) {
		if(!event.getAuthor().isFake() && !event.getAuthor().isBot())
		{

			if(args.length >=1 && args[0].equalsIgnoreCase("set"))
			{
				if(args.length>=2) 
				{
					for(ICommand c : Registry.commands)
					{
						if(c.getName().equalsIgnoreCase(args[1]))
						{
							if(args.length>=3)
							{
								if(References.PermManager.canUse(event.getMember(), c))
								{
									if(args[2].equalsIgnoreCase("owner"))
									{
										if(event.getMember().isOwner())
										{
											References.PermManager.addPerm(event.getGuild().getId(), args[1], Perm.OWNER);
											event.getChannel().sendMessage(":white_check_mark:  Permission **OWNER** added for the command **"+args[1]+"**").queue();
										}
										else
											event.getChannel().sendMessage(":no_entry_sign: **You need to be the owner of the guild to set this**").queue();
									}
									else if(args[2].equalsIgnoreCase("admin"))
									{
										if(event.getMember().hasPermission(Perm.ADMIN.grade.getPermissions()))
										{
											References.PermManager.addPerm(event.getGuild().getId(), args[1], Perm.ADMIN);
											event.getChannel().sendMessage(":white_check_mark:  Permission **ADMIN** added for the command **"+args[1]+"**").queue();
										}
										else
											event.getChannel().sendMessage(":no_entry_sign: **You need to have the ADMINISTRATOR permission to set this**").queue();
									}
									else if(args[2].equalsIgnoreCase("all"))
									{

										References.PermManager.addPerm(event.getGuild().getId(), args[1], Perm.ALL);
										event.getChannel().sendMessage(":white_check_mark:  Permission **ALL** added for the command **"+args[1]+"**").queue();
									}
									else
										next(event, args);
								}
								else
									event.getChannel().sendMessage(":no_entry_sign: **You can't modify the perms of a commands you are not allowed to use!**").queue();
							}
							else
								event.getChannel().sendMessage("**Specify a permission > owner(only the owner of the guild can use) or admin(only members with ADMINISTRATOR permissions can use) or all(everyone can use)**").queue();
							return;
						}
					}
				}
			}
		}
	}

	private void next(MessageReceivedEvent event, String ... args) {
		
		
			Role role = null;
			if(args[3].startsWith("<@&"))
			{

				String id =args[3].replaceFirst("<@&", "");
				id = id.substring(0,id.length()-2);
				role = event.getGuild().getRoleById(id);
			}
			else
			{
				List<Role> roles = event.getGuild().getRolesByName(args[3], true);
				if(!roles.isEmpty())
				{
					role = roles.get(0);
				}
			}
			if(role !=null)
			{
			EPermFilter filter = EPermFilter.only;
				if(args[2].equalsIgnoreCase(EPermFilter.only.toString()) || args[2].equalsIgnoreCase(EPermFilter.only.alias))
				filter = EPermFilter.only;
				if(args[2].equalsIgnoreCase(EPermFilter.below.toString()) || args[2].equalsIgnoreCase(EPermFilter.below.alias))
					filter = EPermFilter.below;
				References.PermManager.addPerm(event.getGuild().getId(), args[1], new Perm(role, filter));
				MembersUtil.mentionRole(event.getChannel(), role, ":white_check_mark:  Permission for @role "+(filter == EPermFilter.below? "and below " :"" )+"added for the command **"+args[1]+"**");
			}
			else
			{
				event.getChannel().sendMessage(":no_entry_sign: ** No role found for "+ args[3]+"**").queue();
			}
	}
	private void remove(MessageReceivedEvent event, String ... args) {
	//	if(args.length)
	}
	@Override
	public String getUsage() {
		return ">perms set <command> <owner/admin/all> or (only/below <a role>)";
	}

	@Override
	public void init() {
		References.PermManager.addPermGeneral(this.getName(), Perm.ADMIN);
	}
}
