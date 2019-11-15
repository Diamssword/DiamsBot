package diamssword.bot.storage.savable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.diamssword.bot.api.storage.ISavable;
import com.google.gson.Gson;

public class JudasSavable implements ISavable<JudasSavable>
{
	public static final String KEY="JUDASCMD";
	public Map<String,Boolean> data= new HashMap<String,Boolean>();
	@Override
	public String path() {
		return "commands/judas.data";
	}

	@Override
	public JudasSavable fromText(String json, Gson gson) {
		String[] lines =json.split(";");
		for(String s : lines)
		{
			if(s.length()>5)
			{
				String[] s1 = s.split("=");
				boolean b =s1[1].equals("true");
				data.put(s1[0], b);
			}

		}
		return this;
	}

	@Override
	public String toText(Gson json) {
		String res = "";
		for(String key : data.keySet())
		{
			res=res+key+"="+data.get(key)+";\n";
		}
		return res;
	}
	public static class JudasSavableWords implements ISavable<JudasSavableWords>
	{
		public static final String KEY="JUDASCMD";
		public List<String> names= new ArrayList<String>();
		public List<String> adjs= new ArrayList<String>();
		@Override
		public String path() {
			return "commands/judasWords.data";
		}

		@Override
		public JudasSavableWords fromText(String json, Gson gson) {
			JudasSavableWords obj= gson.fromJson(json, JudasSavableWords.class);
			if(obj == null)
				obj = new JudasSavableWords();
			if(obj.adjs == null)
				obj.adjs = new ArrayList<String>();
			if(obj.names == null)
				obj.names = new ArrayList<String>();
			return obj;
		}

		@Override
		public String toText(Gson json) {
		
			return json.toJson(this);
		}

		@Override
		public Class<?> savedClass() {
			return this.getClass();
		}
	}
	@Override
	public Class<?> savedClass() {
		return this.getClass();
	}
}