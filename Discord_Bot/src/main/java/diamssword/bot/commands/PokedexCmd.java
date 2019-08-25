package diamssword.bot.commands;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.diamssword.bot.api.actions.ICommand;
import com.diamssword.bot.api.utils.LoadUtils;
import com.google.gson.Gson;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PokedexCmd implements ICommand{

	Gson gson = new Gson();
	public PokedexCmd()
	{

	}
	@Override
	public String getName() {

		return "pokedex";
	}

	@Override
	public String getDesc() {
		return "give infos on a pokemon!";
	}

	@Override
	public void execute(MessageReceivedEvent event, String ... args) {
		if(!event.getAuthor().isFake() && !event.getAuthor().isBot())
		{
			if(args.length>=1)
			{

				String res;
				try {
					res = LoadUtils.urlToString(new URL("https://some-random-api.ml/pokedex?pokemon="+args[0]));

					PokemonClass resp =gson.fromJson(res, PokemonClass.class);
					if(resp == null)
					{
						event.getChannel().sendMessage("Je ne connais pas ce Pokemon!").queue();
						return;
					}
					//event.getChannel().sendMessage(resp.format()).queue();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					InputStream is = null;
					try {
						URLConnection openConnection = new URL(resp.sprites.animated==null? resp.sprites.normal : resp.sprites.animated).openConnection();


						openConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
						openConnection.connect();

						is = openConnection.getInputStream();
						byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
						int n;

						while ( (n = is.read(byteChunk)) > 0 ) {
							baos.write(byteChunk, 0, n);
						}
					}
					catch (IOException e) {
						e.printStackTrace ();
					}
					finally {
						if (is != null) { try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						} }
					}
					InputStream fis = new ByteArrayInputStream(baos.toByteArray());
					event.getChannel().sendFile(fis, resp.name+".gif",new MessageBuilder(resp.format()).build()).queue();;
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public String getUsage() {
		return ">pokedex <Pokemon Name>";
	}

	@Override
	public void init() {

	}

	public static class PokemonClass
	{
		public String name;
		public  String[] type;
		public  String[] species;
		public  String[] abilities;
		public String height;
		public String weight;
		public String[] gender;
		public String[] egg_groups;
		public Family family;
		public String description;
		public int generation;
		public Sprites sprites;
		public String format()
		{
			return sprites.animated==null? sprites.normal : sprites.animated+"\n----"+name+"---\nTypes: "+listing(type)+"\nAbilities: "+listing(abilities)+"\n Height:"+height+" | Weight:"+weight+"\n Gender: "+listing(gender)+"\nFamily: "+family.text()+"\n Egg Groups: "+listing(egg_groups)+"\nGeneration: "+generation+"\nDescription: "+description;
		}
		private String listing(String[] ls)
		{
			String res = "";
			for(String s: ls)
				res=res+s+"|";
			return res.substring(0, res.length()-1);
		}
	}
	public static class Family 
	{
		int evolutionStage;
		String[] evolutionLine;
		public String text()
		{

			String res = "";
			for(String s: evolutionLine)
				res=res+s+">";
			return res.substring(0, res.length()-1);
		}
	}
	public static class Sprites 
	{
		String normal;
		String animated;
	}
}
