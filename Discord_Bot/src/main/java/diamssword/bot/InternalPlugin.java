package diamssword.bot;

import com.diamssword.bot.api.IPlugin;
import com.diamssword.bot.api.References;
import com.diamssword.bot.api.actions.ITickable;
import com.diamssword.bot.api.actions.Registry;

import diamssword.bot.alexa.RemindAlexa;
import diamssword.bot.commands.Commands;
import net.dv8tion.jda.core.entities.Guild;

public class InternalPlugin implements IPlugin,ITickable{

	@Override
	public String id() {
		return "diamsbot_internal";
	}

	@Override
	public String name() {
		return "DiamsBot Internal Plugin";
	}

	@Override
	public void preInit() {


	}

	@Override
	public void init() {
		Commands.init();
		Registry.registerTickable(this);
		Registry.registerTickable(new RemindAlexa());
	}

	@Override
	public void postInit() {

	}

	@Override
	public void tick(Guild guild) {	}

	@Override
	public int everyMS() {
		return 5000;
	}

	@Override
	public void tickSingle() {
		String str="";
		for(Guild g : References.bot.getGuilds())
		{
			str = str+g.getName()+" > "+g.getId()+"\n";
		}
		if(Main.uiFrame != null)
			Main.uiFrame.serversList.setText(str);
		Main.uiFrame.repaint();

	}
}
