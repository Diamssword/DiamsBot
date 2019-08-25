package diamssword.bot.lol;

import java.net.MalformedURLException;
import java.net.URL;

import com.diamssword.bot.api.utils.LoadUtils;
import com.google.gson.Gson;

import diamssword.bot.lol.json.GameJson;
import diamssword.bot.lol.json.SummonerJson;

public class LolData {
	public static final String KEY = "RGAPI-07e7643b-8414-4b17-98ee-9d6b3a6dbdba"; 
	public static SummonerJson getAccountByName(String name)
	{
		String url = "https://euw1.api.riotgames.com/lol/summoner/v4/summoners/by-name/"+name+"?api_key="+KEY;
		try {
			String s = LoadUtils.urlToString(new URL(url));
			if(s.length()>1)
			{
				return new Gson().fromJson(s, SummonerJson.class);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return new SummonerJson();

	}
	public static GameJson getCurrentGame(String summonerID)
	{
		String url = "https://euw1.api.riotgames.com/lol/spectator/v4/active-games/by-summoner/"+summonerID+"?api_key="+KEY;
		try {
			String s = LoadUtils.urlToString(new URL(url));
			System.out.println(s);
			if(s.length()>1)
			{
				return new Gson().fromJson(s, GameJson.class);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return new GameJson();
	}
}
