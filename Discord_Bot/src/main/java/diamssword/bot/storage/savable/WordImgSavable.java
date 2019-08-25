package diamssword.bot.storage.savable;

import java.util.HashMap;
import java.util.Map;

import com.diamssword.bot.api.storage.ISavable;
import com.google.gson.Gson;

public class WordImgSavable implements ISavable<WordImgSavable>
{
	public Map<String,String> data = new HashMap<String,String>();
	@Override
	public String path() {
		return "actions/wordimg.data";
	}

	@Override
	public WordImgSavable fromText(String json, Gson gson) {
		return gson.fromJson(json, this.getClass());
	}

	@Override
	public String toText(Gson json) {
	
		return json.toJson(this);
	}
}