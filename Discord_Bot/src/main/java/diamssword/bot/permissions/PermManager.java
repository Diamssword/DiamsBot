package diamssword.bot.permissions;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.diamssword.bot.api.actions.ICommand;
import com.diamssword.bot.api.permissions.EPermFilter;
import com.diamssword.bot.api.permissions.IPermManager;
import com.diamssword.bot.api.permissions.Perm;
import com.diamssword.bot.api.storage.GuildStorage;
import com.diamssword.bot.api.storage.ISavable;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import diamssword.bot.storage.Storage;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.impl.RoleImpl;

public class PermManager implements IPermManager {

	private  static Map<String,List<Perm>> defPermsMap = new HashMap<String,List<Perm>>();
	//	private  Map<String,Map<String,List<Perm>>> permsMap = new HashMap<String,Map<String,List<Perm>>>();
	private static GuildStorage<PermSavable> storage =  new GuildStorage<PermSavable>(PermSavable.class);

	public void init()
	{
		Storage.list.add(storage);
	}
	@Override
	public void addPermGeneral(String command, Perm perm) {
		List<Perm> p =defPermsMap.get(command);
		if(p == null)
		{
			p = new ArrayList<Perm>();
			defPermsMap.put(command, p);
		}	
		p.add(perm);
	}
	@Override
	public void removePermGeneral(String command, Perm perm) {
		List<Perm> p =defPermsMap.get(command);
		if(p != null)
		{
			p.remove(perm);
		}
	}
	@Override
	public void addPerm(String serverID, String command, Perm perm) {
		Map<String,List<Perm>> map =storage.get(serverID).data;
		if(map == null)
			map = new HashMap<String,List<Perm>>();
		List<Perm> list =map.get(command);
		if( list == null)
			list = new ArrayList<Perm>();

		map.put(command, addPerm(list,perm));
		storage.save(serverID);
	}
	@Override
	public void removePerm(String serverID, String command, Perm perm) {
		Map<String,List<Perm>> map =storage.get(serverID).data;
		if(map != null)
		{
			List<Perm> list =map.get(command);
			if( list != null)
			{
				list.remove(perm);
				storage.save(serverID);
			}
		}
	}
	@Override
	public List<Perm> getPerms(String serverID, String command) {
		Map<String,List<Perm>> map =storage.get(serverID).data;
		if(map == null || map.get(command) == null)
		{
			return getPermsGeneral(command);
		}
		return map.get(command);
	}

	@Override
	public List<Perm> getPermsGeneral(String command) {
		List<Perm> ls =defPermsMap.get(command);
		if(ls == null )
		{
			ls = new ArrayList<Perm>();
			ls.add(Perm.ALL);
		}
		return ls;
	}


	public List<Perm> addPerm(List<Perm> perms, Perm toAdd)
	{
		List<Perm> res = new ArrayList<Perm>();
		if(perms.isEmpty())
		{
			res.add(toAdd);
		} 
		for(Perm p : perms)
		{
			int val = p.compare(toAdd);
			if(val != 0)
			{
				if(val == 1)
				{
					res.add(p);
				}
				res.add(toAdd);
				break;
			}
			else
				res.add(p);
		}
		return res;
	}
	@Override
	public boolean canUse(Member member, ICommand command) {
		if(member.getGuild() == null)
			return true;
		List<Perm> perms =this.getPerms(member.getGuild().getId(), command.getName());
		if(perms.contains(Perm.ADMIN))
			if(member.hasPermission(Perm.ADMIN.grade.getPermissions()))
				return true;
		if(perms.contains(Perm.ALL))
			return true;
		if(perms.contains(Perm.OWNER))
			if(member.isOwner())
				return true;
		for(Perm p : perms)
		{
			if(p.filter == EPermFilter.below)
			{
				for(Role pM :member.getRoles())
				{
					if(pM.getPosition()<= p.grade.getPosition())
						return true;
				}
			}
			else if(p.filter ==EPermFilter.only)
			{
				for(Role pM :member.getRoles())
				{
					if(pM.getId().equals(p.grade.getId()))
						return true;
				}
			}
		}
		return false;
	}

	public static class PermSavable implements ISavable<PermSavable>
	{
		public Map<String,List<Perm>> data= new HashMap<String,List<Perm>>();
		@Override
		public String path() {
			return "permsData.json";
		}

		@Override
		public PermSavable fromText(String json, Gson gson) {
			Gson g =gson.newBuilder().registerTypeAdapter(Perm.class, new PermDeserializer()).create();
			return g.fromJson(json, this.getClass());
		}

		@Override
		public String toText(Gson json) {
			Gson g =json.newBuilder().registerTypeAdapter(Perm.class, new PermSerializer()).create();
			return g.toJson(this);
		}

		@Override
		public Class<?> savedClass() {
			return this.getClass();
		}
	}

	public static class PermSerializer implements JsonSerializer<Perm>
	{
		public JsonElement serialize(Perm perm, Type typeOfSrc, JsonSerializationContext context)
		{
			JsonObject obj = new JsonObject();
			obj.add("filter",new JsonPrimitive(perm.filter.toString()));

			JsonObject obj1 = new JsonObject();
			obj1.add("id",new JsonPrimitive(perm.grade.getId()));
			obj1.add("perms",new JsonPrimitive(perm.grade.getPermissionsRaw()));
			obj.add("role", obj1);
			return obj;
		}
	}
	public static class PermDeserializer implements JsonDeserializer<Perm>
	{
		public Perm deserialize(JsonElement elem, Type typeOfSrc, JsonDeserializationContext context)
		{
			JsonObject obj = elem.getAsJsonObject();
			JsonElement fil = obj.get("filter");
			EPermFilter f = EPermFilter.only;
			if(fil !=null)
				f =EPermFilter.valueOf(fil.getAsString());
			JsonElement el1 = obj.get("role");
			Role r = Perm.ALL.grade;
			if(el1!=null)
			{
				JsonObject obj1 =el1.getAsJsonObject();
				JsonElement id = obj1.get("id");
				long id1 = 0;
				if(id!=null)
					id1 = Long.parseLong(id.getAsString());
				long raw1 = 0;
				JsonElement raw = obj1.get("perms");

				if(raw != null)
					raw1 = raw.getAsLong();
				r=new RoleImpl(id1, null).setRawPermissions(raw1);
			}
			return new Perm(r, f);
		}
	}

}
