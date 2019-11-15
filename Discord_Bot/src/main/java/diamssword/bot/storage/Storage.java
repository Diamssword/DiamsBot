package diamssword.bot.storage;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.diamssword.bot.api.References;
import com.diamssword.bot.api.storage.AGuildStorage;
import com.diamssword.bot.api.storage.GlobalOptions;
import com.diamssword.bot.api.storage.ISavable;
import com.diamssword.bot.api.storage.IStorage;
import com.diamssword.bot.api.utils.ELoggerControl;
import com.google.gson.Gson;

import net.dv8tion.jda.core.entities.Guild;

public class Storage implements IStorage{
	public static Logger LOG = References.getLogger("DiamsBot-Storage", Color.YELLOW, ELoggerControl.NO_CLASS_NAME);
	private GlobalOptions globaloptions;
	@SuppressWarnings("rawtypes")
	public static List<AGuildStorage> list = new ArrayList<AGuildStorage>();


	static boolean first= true;
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
			if(first)
			LOG.log(Level.INFO,"Loaded GuildStorage:"+gs.getSavableClass().getSimpleName()+"!");
		}
		first = false;
	}


	@SuppressWarnings("rawtypes")
	@Override
	public void registerStorage(AGuildStorage storage) {
		list.add(storage);

	}


	@SuppressWarnings("rawtypes")
	@Override
	public void deleteStorage(AGuildStorage storage) {
		list.remove(storage);

	}

	public GlobalOptions getGlobal()
	{
		if(globaloptions == null)
		{
			File f =new File("botdata/global.json");
			try {
				Scanner r = new Scanner(f);
				String s = "";
				while(r.hasNext())
				{
					s = s+r.next();
				}
				r.close();
				globaloptions=	new Gson().fromJson(s, GlobalOptions.class);
				if(globaloptions == null)
					globaloptions = new GlobalOptions();	
			} catch (FileNotFoundException e) {
				globaloptions = new GlobalOptions();
				e.printStackTrace();
			}
		}
		return globaloptions;
	}

	public void saveGlobal()
	{
		if(globaloptions != null)
		{
			File f =new File("botdata/global.json");
			try {
				FileWriter w = new FileWriter(f);
				String s =	new Gson().toJson(globaloptions);
				w.write(s);
				w.close();
			} catch ( IOException e) {
				e.printStackTrace();
			}
		}
	}


}
