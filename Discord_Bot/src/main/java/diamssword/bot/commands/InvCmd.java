package diamssword.bot.commands;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.diamssword.bot.api.actions.ICommand;

import diamssword.bot.storage.Stats;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class InvCmd implements ICommand{

	@Override
	public String getName() {

		return "inv";
	}

	@Override
	public String getDesc() {
		return "show your inventory";
	}

	@Override
	public void execute(MessageReceivedEvent event, String ... args) {
		if(!event.getAuthor().isFake())
		{
			if(args.length > 0 && args[0].equals(""))
			{
				ByteArrayOutputStream os = new ByteArrayOutputStream();
			//	try {
				//	ImageIO.write(StatSave.get(event.getAuthor().getId()).inv.inventoryImage(),"png", os);
			//	} catch (IOException e) {}

				InputStream fis = new ByteArrayInputStream(os.toByteArray());
				event.getChannel().sendFile(fis, event.getAuthor().getName()+".png").queue();
			}
		}

	}

	@Override
	public String getUsage() {
		return ">inv";
	}

	private String format(MessageReceivedEvent event)
	{
		Stats s =null;//StatSave.get(event.getAuthor().getId());
		int tot = (int) Math.floor(((double)s.xp/(double)s.xpMax) * 10);
		String xpStr = "[";
		for(int i= 0;i < tot;i++)
		{
			xpStr= xpStr+"=";
		}
		for(int i= tot;i < 10;i++)
		{
			xpStr= xpStr+" -";
		}
		xpStr= xpStr+"]";
		return event.getAuthor().getAsMention()+":\n Lvl:          "+s.lvl+"\n"+xpStr+"\nHP: "+"❤❤❤🖤🖤🖤"+"\n"+"Mana: <=====----->";

	} 

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

}
