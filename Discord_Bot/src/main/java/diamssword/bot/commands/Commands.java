package diamssword.bot.commands;

import com.diamssword.bot.api.References;
import com.diamssword.bot.api.actions.Registry;

import diamssword.bot.actions.AlexaAction;
import diamssword.bot.actions.ChatBot;
import diamssword.bot.actions.DadJokeAction;
import diamssword.bot.actions.IsThisAction;
import diamssword.bot.actions.PPemojiAction;
import diamssword.bot.actions.WordImgAction;
import diamssword.bot.actions.WordVoiceAction;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.managers.GuildController;

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
		Registry.registerCmd(new ProfilMixCmd());
		Registry.registerCmd(new PokedexCmd());
		Registry.registerCmd(new CyanideCmd());
		Registry.registerCmd(new TTSCmd());
		Registry.registerCmd(new MonikaCmd());
		Registry.registerCmd(new PermCmd());
		Registry.registerCmd(new SatanicCmd());
		Registry.registerCmd(new EnchantingCmd());
		Registry.registerCmd(new InspiroCmd());
		Registry.registerCmd(new EreaseCmd());
		Registry.registerCmd(new MoneyCmd());
		Registry.registerCmd(new ThanosCmd());
		Registry.registerCmd(new ThanosPussyCmd());
		for(Guild g :References.bot.getGuilds())
		{

			GuildController ctrl = new GuildController(g);
			ctrl.setNickname(g.getMember(References.bot.getSelfUser()), "UwU_Bot").queue();
		}

		Registry.registerAction(new IsThisAction());
		Registry.registerAction(new WordImgAction());
		Registry.registerAction(new WordVoiceAction());
		Registry.registerAction(new ChatBot());
		Registry.registerAction(new DadJokeAction());
		Registry.registerAction(new AlexaAction());
		Registry.registerAction(new PPemojiAction());
	}
	
}
