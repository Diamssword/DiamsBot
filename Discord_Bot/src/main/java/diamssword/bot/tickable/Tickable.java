package diamssword.bot.tickable;

import com.diamssword.bot.api.References;
import com.diamssword.bot.api.actions.ITickable;
import com.diamssword.bot.api.actions.Registry;

import net.dv8tion.jda.core.entities.Guild;
/**
 *  Add the ITickable interface to your class to add a timer event fired every 50 ms for each guilds
 * Be sure to register your classes in the add(ITickable tickable)!
 * @author Diamssword
 *
 */
public class Tickable {


	public static void TimerThread()
	{
		new Thread(new Runnable() {
			long timer = System.currentTimeMillis();
			@Override
			public void run() {
				while (true)
				{
					if(System.currentTimeMillis() > timer+50 )
					{
						for(ITickable tick :Registry.tickables.keySet())
						{
							if(System.currentTimeMillis() > Registry.tickables.get(tick)+tick.everyMS())
							{
								tick.tickSingle();
								for(Guild g : References.bot.getGuilds())
									tick.tick(g);
								Registry.tickables.put(tick,System.currentTimeMillis());
							}
						}
					}
				}
			}}).start(); 
	}
	
}
