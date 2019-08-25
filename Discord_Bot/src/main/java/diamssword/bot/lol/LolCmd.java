package diamssword.bot.lol;

import com.diamssword.bot.api.actions.ICommand;

import diamssword.bot.lol.json.GameJson;
import diamssword.bot.lol.json.GameParticipantJson;
import diamssword.bot.lol.json.SummonerJson;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class LolCmd implements ICommand{

	public LolCmd()
	{

	}
	@Override
	public String getName() {

		return "lol";
	}

	@Override
	public String getDesc() {
		return "League Of legends infos!";
	}

	@Override
	public void execute(MessageReceivedEvent event, String ... args) {
		if(!event.getAuthor().isFake() && !event.getAuthor().isBot())
		{

			if(args.length >= 1 && args[0].matches("^[0-9\\p{L} _\\.]+$"))
			{
				SummonerJson j =LolData.getAccountByName(args[0]);
				GameJson game =LolData.getCurrentGame(j.id);
				event.getChannel().sendMessage("Name: "+j.name+"\nID: "+j.id+"\nLvl: "+j.summonerLevel).queue();
				if(game.gameId!=0)
				{
					event.getChannel().sendMessage("Current Game: "+game.gameId+ "\n Mode: "+game.gameMode+"\nZone: "+game.platformId+"\nDurée: "+(game.gameLength/60)+"min\nJoueurs:").queue();
					for(GameParticipantJson part :game.participants)
					{
						event.getChannel().sendMessage("  "+part.summonerName+"\n		Champion:"+part.championId).queue();
					}
				}
			
			}
		}
	}

	@Override
	public String getUsage() {
		return ">lol <Pseudo Lol>";
	}

	@Override
	public void init() {

	}
}
