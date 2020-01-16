package diamssword.bot;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;

import com.diamssword.bot.api.References;
import com.diamssword.bot.api.actions.Registry;
import com.diamssword.bot.api.storage.GlobalOptions;

import diamssword.bot.audio.GPlayerManager;
import diamssword.bot.audio.TTS.TextToSpeechBot;
import diamssword.bot.graphical.BotTokenPopup;
import diamssword.bot.graphical.Frame;
import diamssword.bot.permissions.PermManager;
import diamssword.bot.plugins.LoadPlugins;
import diamssword.bot.storage.Storage;
import diamssword.bot.tickable.Tickable;
import diamssword.bot.utils.LogHandler;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;

//https://discordapp.com/oauth2/authorize?client_id=519224623523692577&scope=bot&permissions=2080631922
public class Main {
	public static Logger LOG;
	//public static JDA bot;
	public static Frame uiFrame;
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		new Thread() {
			public void run() {
				try {
					uiFrame = new Frame();
					uiFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.run();
		References.logHandler= new LogHandler();
		LOG = References.getLogger("DiamsBot-Main");
		LOG.log(Level.INFO, "--Searching for Plugins--");
		LoadPlugins.Find();
		LOG.log(Level.INFO, "...Done!");
		LOG.log(Level.INFO, "--PreInit Plugins--");
		LoadPlugins.preinit();
		LOG.log(Level.INFO, "...Done!");
		LOG.log(Level.INFO, "--Creating Storage and audioManager instance--");
		References.PlayerManager = new GPlayerManager();
		References.Storage = new Storage();
		References.TTSMaker = new TextToSpeechBot();
		LOG.log(Level.INFO, "...DONE!!");
		LOG.log(Level.INFO, "--Building and loading DiamsBot--");
		GlobalOptions opts =References.Storage.getGlobal();
		String key =opts.BotToken;
		if(key.length()<50)
		{
			key= (String) uiFrame.createPopup(new BotTokenPopup("Humm...Il manque le token du bot! Entrez le ici! "),true);
			 opts.BotToken = key;
		}
		try {
			References.bot = new JDABuilder(key).build();
		} catch (LoginException e) {
			 key =(String) uiFrame.createPopup(new BotTokenPopup("Humm...Le token du bot n'est pas valide! Entrez le ici! "),true);
			 opts.BotToken = key;
			 try {
				References.bot = new JDABuilder(key).build();
			} catch (LoginException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		 References.Storage.saveGlobal();
		References.bot.addEventListener(new BotEventsHandler());
		try {
			References.bot.awaitReady();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		LOG.log(Level.INFO, "--Init Plugins--");
		LoadPlugins.init();
		LOG.log(Level.INFO, "--Init PermissionManager--");
		References.PermManager = new PermManager();
		((PermManager)References.PermManager).init();
		LOG.log(Level.INFO, "--Loading Commands--");
		Registry.init();
		Registry.initActions();
		LOG.log(Level.INFO, "...Done!");
		LOG.log(Level.INFO, "--Loading Stored Datas for each guilds--");
		for(Guild g : References.bot.getGuilds())
			((Storage) References.Storage).init(g);
		((Storage) References.Storage).init(References.GLOBAL_STORAGE_ID);
		LOG.log(Level.INFO, "...Done!");
		LOG.log(Level.INFO, "--Clearing Caches--");
		((TextToSpeechBot)References.TTSMaker).clearCache();
		LOG.log(Level.INFO, "...Done!");
		LOG.log(Level.INFO, "--Initing ticking Thread--");
		Tickable.TimerThread();
		LOG.log(Level.INFO, "...Done!");
		LOG.log(Level.INFO, "--PostInit Plugins--");
		Statut();
		LoadPlugins.postinit();
	}


 
	public static void kill()
	{

		try{
			References.bot.shutdown();
			for(Guild g : References.bot.getGuilds())
			{
				if(!g.getVoiceChannels().isEmpty())
					g.getAudioManager().closeAudioConnection();

			}
		}catch(Exception e)
		{
			System.exit(0);
		}
		System.exit(0);
	}
	public static void Statut()
	{
		References.bot.getPresence().setGame(Game.playing(">help for a list of commands"));

	}
}