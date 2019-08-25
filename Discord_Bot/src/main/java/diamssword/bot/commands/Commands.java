package diamssword.bot.commands;

import com.diamssword.bot.api.actions.Registry;

import diamssword.bot.actions.ChatBot;
import diamssword.bot.actions.IsThisAction;
import diamssword.bot.actions.WordImgAction;
import diamssword.bot.actions.WordVoiceAction;
import diamssword.bot.lol.LolCmd;

public class Commands {

	public static void init()
	{
		Registry.registerCmd(new JudasCmd());
		Registry.registerCmd(new HelpCmd());
		Registry.registerCmd(new ImageCmd());
		Registry.registerCmd(new WordBindCmd());
		Registry.registerCmd(new VoiceBindCmd());
		Registry.registerCmd(new ColorFuckCmd());
		Registry.registerCmd(new FactCmd());
		Registry.registerCmd(new DiceCmd());
		Registry.registerCmd(new WizzCmd());
		Registry.registerCmd(new LolCmd());
		Registry.registerCmd(new ProfilMixCmd());
		Registry.registerCmd(new PokedexCmd());
		Registry.registerCmd(new CyanideCmd());
		Registry.registerCmd(new TTSCmd());
		//Registry.registerCmd(new OpCmd());
		Registry.registerCmd(new PermCmd());
		Registry.registerCmd(new ThanosCmd());
		Registry.registerCmd(new ThanosPussyCmd());
		

		Registry.registerAction(new IsThisAction());
		Registry.registerAction(new WordImgAction());
		Registry.registerAction(new WordVoiceAction());
		Registry.registerAction(new ChatBot());
	}
	
}
