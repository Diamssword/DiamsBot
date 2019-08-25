package diamssword.bot.commands;

import java.util.ArrayList;
import java.util.List;

import com.diamssword.bot.api.References;
import com.diamssword.bot.api.actions.ICommand;
import com.diamssword.bot.api.actions.ITickable;
import com.diamssword.bot.api.actions.Registry;
import com.diamssword.bot.api.audio.Langues;
import com.diamssword.bot.api.permissions.Perm;
import com.diamssword.bot.api.storage.GuildStorage;
import com.diamssword.bot.api.storage.ISavable;
import com.diamssword.bot.api.utils.MembersUtil;
import com.diamssword.bot.api.utils.StringUtils;
import com.google.gson.Gson;

import diamssword.bot.storage.Storage;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class TTSCmd implements ICommand,ITickable{

	public static GuildStorage<TTSSavable> storage = new GuildStorage<TTSSavable>(TTSSavable.class);
		@Override
		public String getName() {

			return "tts";
		}

		@Override
		public String getDesc() {
			return "say something to somone somewhere";
		}

		@Override
		public void execute(MessageReceivedEvent event, String ... args) {
			if(!event.getAuthor().isFake() && !event.getAuthor().isBot())
			{
				if(args[0].equals("-lang"))
				{
					String ls = "Langues dispos (inserez le code de langue juste avant votre text dans la commande): \n";
					for(Langues l:Langues.values())
					{
						ls=ls+l.toString()+" > "+l.name+"\n";
					}
					event.getChannel().sendMessage(ls).queue();
					return;

				}
				if(args[0].equals("-skip"))
				{
					References.PlayerManager.getGuildPlayer(event.getGuild().getId()).skip();
					event.getChannel().sendMessage("Skipped the current tts").queue();
					return;

				}
				if(args[0].equals("-clear"))
				{
					References.PlayerManager.getGuildPlayer(event.getGuild().getId()).clear();
					event.getChannel().sendMessage("List of tts cleared").queue();
					return;

				}
				if(args[0].equals("-defaultLang"))
				{
					Langues lang = null;
					for(Langues l :Langues.values())
					{
						if(args[1].equals(l.toString()))
						{
							lang = l;
							break;
						}
					}
					if(lang != null)
					{

						storage.get(event.getGuild().getId()).lang = lang;
						storage.save(event.getGuild().getId());
						event.getChannel().sendMessage("Default language changed to: "+lang.name+"("+lang+")").queue();
					}
					else
						event.getChannel().sendMessage("You need to specify a lang code (do >tts -lang to have a list)").queue();
					return;

				}
				List<Member> membs =MembersUtil.getMembersMatching(event.getGuild().getMembers(), args[0]);
				Member cible=null;
				for(Member m : membs)
				{
					if(m.getVoiceState().inVoiceChannel())
					{
						cible = m;
						break;
					}
				}
				Langues lang = null;
				for(Langues l :Langues.values())
				{
					if(args[1].equals(l.toString()))
					{
						lang = l;
						break;
					}
				}
				String text="";
				if(lang == null)
				{
					text = StringUtils.weldArgs(args, 1, -1);
					lang = storage.get(event.getGuild().getId()).lang;
				}
				else 
					text = StringUtils.weldArgs(args, 2, -1);
				if(cible != null)
				{
					References.PlayerManager.loadTrackForMember(References.TTSMaker.speak(lang,text).getAbsolutePath(), event.getGuild().getId(), cible);
					MembersUtil.mentionMember(event.getChannel(), cible, "Creating the message for @member !");

				}
				else
				{
					if(!membs.isEmpty())
					{
						Member m =membs.get(0);
						storage.get(event.getGuild().getId()).saved.add(new SavedTTSJson(m.getUser().getId(), lang, text));
						storage.save(event.getGuild().getId());
						MembersUtil.mentionMember(event.getChannel(), m, "@member isn't in a voice channel for now... He will receive your message when he connect to one!");
					}
					else
						event.getChannel().sendMessage("Found no one matching the name "+args[0]+". Try somone else!");
				}


			}
		}

		@Override
		public String getUsage() {
			return ">tts <user> <lang (optional)> <text to say> />tts -langs/-skip/-clear/-defaultLang";
		}

		@Override
		public void init() {
			Storage.list.add(storage);
			Registry.registerTickable(this);
		
		}

		public static class TTSSavable implements ISavable<TTSSavable>
		{
			public static final String KEY="JUDASCMD";
			public Langues lang = Langues.fr_fr;
			public List<SavedTTSJson> saved= new ArrayList<SavedTTSJson>();
			@Override
			public String path() {
				return "commands/tts.data";
			}

			@Override
			public TTSSavable fromText(String json, Gson gson) {
				TTSSavable obj= gson.fromJson(json, TTSSavable.class);
				if(obj == null)
					obj = new TTSSavable();
				return obj;
			}

			@Override
			public String toText(Gson json) {

				return json.toJson(this);
			}
		}

		public static class SavedTTSJson
		{
			public String userID;
			public Langues lang;
			public String text;
			public SavedTTSJson(String Userid, Langues lang, String text)
			{
				this.text = text;
				this.lang = lang;
				this.userID = Userid;
			}
		}

		@Override
		public void tick(Guild g) {

			List<SavedTTSJson> delete = new ArrayList<SavedTTSJson>();
			List<SavedTTSJson> current = storage.get(g.getId()).saved;
			for(SavedTTSJson sa :current)
			{
				if(g.getMemberById(sa.userID).getVoiceState().inVoiceChannel())
				{
					References.PlayerManager.loadTrackForMember(References.TTSMaker.speak(sa.lang,sa.text).getAbsolutePath(), g.getId(), g.getMemberById(sa.userID));
					delete.add(sa);
				}
			}
			for(SavedTTSJson sa : delete)
			{
				current.remove(sa);
			}
			storage.save(g.getId());

		}

		@Override
		public int everyMS() {
			return 2000;
		}

		@Override
		public void tickSingle() {}
}
