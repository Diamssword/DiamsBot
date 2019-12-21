package diamssword.bot.commands;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.diamssword.bot.api.actions.ICommand;
import com.diamssword.bot.api.actions.ITickable;
import com.diamssword.bot.api.actions.Registry;
import com.diamssword.bot.api.utils.LoadUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MoneyCmd implements ICommand,ITickable{

	public MoneyCmd()
	{

	}
	@Override
	public String getName() {

		return "money";
	}

	@Override
	public String getDesc() {
		return "convert money";
	}

	@Override
	public void execute(MessageReceivedEvent event, String ... args) {
		if(!event.getAuthor().isFake() && !event.getAuthor().isBot())
		{
			if(args[0].equalsIgnoreCase("-list"))
			{
				String res = " :money_with_wings: Currencies Codes : :money_with_wings: \n";
				for(String key : CurrentDatas.keySet())
				{
					res= res+key+" -> "+currenciesNames.get(key)+"\n";
				}
				event.getChannel().sendMessage(res).queue();
			}
			else if(args.length ==1)
			{
				Double d =CurrentDatas.get(args[0].toUpperCase());
				if(d == null)
				{
					event.getChannel().sendMessage("Unknown currency : "+args[0]+" try >money -list").queue();
					return;
				}
				else {event.getChannel().sendMessage(":money_with_wings: 1€ (EURO) = "+d+" "+args[0]+" ("+currenciesNames.get(args[0])+") OR ").queue(); return;}
			}
			else if(args.length ==2)
			{
				try {
					Double amo =Double.parseDouble(args[0]);
					Double d =CurrentDatas.get(args[1].toUpperCase());
					if(d == null)
					{
						event.getChannel().sendMessage("Unknown currency : "+args[0]+" try >money -list").queue();
						return;
					}
					else {event.getChannel().sendMessage(":money_with_wings: "+amo+"€ (EURO) = "+d*amo+" "+args[1]+" ("+currenciesNames.get(args[1])+")").queue(); return;}
				}catch(NumberFormatException e)
				{
					event.getChannel().sendMessage(args[0]+ " is not a valid number, try this : >money 1000 USD").queue();
					return;
				}

			}
			else if(args.length >2)
			{
				try {
					Double amo =Double.parseDouble(args[0]);
					Double d =CurrentDatas.get(args[1].toUpperCase());
					Double d1 =CurrentDatas.get(args[2].toUpperCase());
					if(d == null || d1 == null)
					{
						if(d== null)
						event.getChannel().sendMessage("Unknown currency : "+args[0]+" try >money -list").queue();
						else
							event.getChannel().sendMessage("Unknown currency : "+args[1]+" try >money -list").queue();
						return;
					}
					else {event.getChannel().sendMessage(":money_with_wings: "+amo+" "+args[1]+" ("+currenciesNames.get(args[1])+")"+" = "+(d*amo)*d1+" "+args[2]+" ("+currenciesNames.get(args[2])+")").queue(); return;}
				}catch(NumberFormatException e)
				{
					event.getChannel().sendMessage(args[0]+ " is not a valid number, try this : >money 1000 USD").queue();
					return;
				}

			}
		}
	}

	@Override
	public String getUsage() {
		return ">money <amount><currency> | >money <amount><currency> <currency> | >money -list";
	}

	@Override
	public void init() {
		this.tickSingle();
		Registry.registerTickable(this);
	}

	private static class RatesJson
	{
		Map<String,Double> rates;
	}
	public static Map<String, String> currenciesNames = new HashMap<String,String>();
	{
		currenciesNames.put("USD", "US dollar");
		currenciesNames.put("JPY", "Japanese yen");
		currenciesNames.put("BGN", "Bulgarian lev");
		currenciesNames.put("CZK", "Czech koruna");
		currenciesNames.put("DKK", "Danish krone");
		currenciesNames.put("GBP", "Pound sterling");
		currenciesNames.put("HUF", "Hungarian forint");
		currenciesNames.put("PLN", "Polish zloty");
		currenciesNames.put("RON", "Romanian leu");
		currenciesNames.put("SEK", "Swedish krona");
		currenciesNames.put("CHF", "Swiss franc");
		currenciesNames.put("ISK", "Icelandic krona");
		currenciesNames.put("NOK", "Norwegian krone");
		currenciesNames.put("HRK", "Croatian kuna");
		currenciesNames.put("RUB", "Russian rouble");
		currenciesNames.put("TRY", "Turkish lira");
		currenciesNames.put("AUD", "Australian dollar");
		currenciesNames.put("BRL", "Brazilian real");
		currenciesNames.put("CAD", "Canadian dollar");
		currenciesNames.put("CNY", "Chinese yuan renminbi");
		currenciesNames.put("HKD", "Hong Kong dollar");
		currenciesNames.put("IDR", "Indonesian rupiah");
		currenciesNames.put("ILS", "Israeli shekel");
		currenciesNames.put("INR", "Indian rupee");
		currenciesNames.put("KRW", "South Korean won ");
		currenciesNames.put("MXN", "Mexican peso");
		currenciesNames.put("MYR", "Malaysian ringgit");
		currenciesNames.put("NZD", "New Zealand dolla");
		currenciesNames.put("PHP", "Philippine peso ");
		currenciesNames.put("SGD", "Singapore dollar");
		currenciesNames.put("THB", "Thai baht");
		currenciesNames.put("ZAR", "South African rand");
		currenciesNames.put("CHO", "South African rand");
		currenciesNames.put("EUR", "Euro");
		currenciesNames.put("MARS", "MARS (Kinder)");
		currenciesNames.put("LOOPS", "LOOPS en (kg)");
		currenciesNames.put("RP", "RIOT POINTS");
		currenciesNames.put("BTC", "BITCOIN");
		currenciesNames.put("LOUIS", "LOUIS");

	}
	@Override
	public void tick(Guild guild) {
	}
	@Override
	public void tickSingle() {
		try {
			CurrentDatas = new Gson().fromJson(LoadUtils.urlToString(new URL("https://api.exchangeratesapi.io/latest")),RatesJson.class).rates;
			CurrentDatas.put("EUR", 1D);
			CurrentDatas.put("MARS", 1/0.369D);
			CurrentDatas.put("LOOPS", 177.61989342806394316163410301954D);
			CurrentDatas.put("RP", 840/5D);
			CurrentDatas.put("BTC",Double.parseDouble(LoadUtils.urlToString(new URL("https://blockchain.info/tobtc?currency=EUR&value=1"))));
			CurrentDatas.put("LOUIS",0.0009569D);
			
		} catch (JsonSyntaxException | MalformedURLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public int everyMS() {
		return 6*1000*60;
	}
	public static Map<String,Double> CurrentDatas = new HashMap<String,Double>();
}
