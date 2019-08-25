package diamssword.bot.storage;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.diamssword.bot.api.References;
import com.diamssword.bot.api.storage.AGuildStorage;
import com.diamssword.bot.api.storage.GuildStorage;
import com.diamssword.bot.api.storage.ISavable;
import com.diamssword.bot.api.storage.IStorage;
import com.diamssword.bot.api.utils.ELoggerControl;

import net.dv8tion.jda.core.entities.Guild;

public class Storage implements IStorage{
	public static Logger LOG = References.getLogger("DiamsBot-Storage", Color.YELLOW, ELoggerControl.NO_CLASS_NAME);
	@SuppressWarnings("rawtypes")
	public static List<AGuildStorage> list = new ArrayList<AGuildStorage>();


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void init(Guild g)
	{
		File f =new File("botdata/"+g.getId());
		f.mkdirs();

		for(AGuildStorage gs : list)
		{
			gs.init(g.getId());
			ISavable sav = gs.load(g.getId());
			if(sav != null)
			gs.set(g,sav);
			LOG.log(Level.INFO,"Loaded GuildStorage:"+gs.getSavableClass().getSimpleName()+" for "+g.getName()+"'s guild!");
		}
	}


	@SuppressWarnings("rawtypes")
	@Override
	public void registerStorage(GuildStorage storage) {
		list.add(storage);
		
	}


	@SuppressWarnings("rawtypes")
	@Override
	public void deleteStorage(GuildStorage storage) {
		list.remove(storage);
		
	}

}
