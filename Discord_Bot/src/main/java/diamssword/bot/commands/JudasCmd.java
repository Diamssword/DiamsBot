package diamssword.bot.commands;

import java.util.List;
import java.util.Random;

import com.diamssword.bot.api.References;
import com.diamssword.bot.api.actions.ICommand;
import com.diamssword.bot.api.actions.ITickable;
import com.diamssword.bot.api.actions.Registry;
import com.diamssword.bot.api.storage.GuildStorage;
import com.diamssword.bot.api.utils.MembersUtil;

import diamssword.bot.storage.Storage;
import diamssword.bot.storage.savable.JudasSavable;
import diamssword.bot.storage.savable.JudasSavable.JudasSavableWords;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.HierarchyException;
import net.dv8tion.jda.core.managers.GuildController;

public class JudasCmd implements ICommand,ITickable{
	private static final String[] nameDef = new String[] { "J.K. Rowling","Loli","Stalin","Polanski","Hervey Weinstein","Math Podcast","Logan Paul","Hanouna","Nabila","Gimms","Ribéry","Benzema","Ruquier","Hitler","Batard","Dégénéré","Salope","Soumise","Lépreux","Soubrette","Limace","PNJ","Traître","Souillure","Putain","Pute","Fille de joie","Bachibouzouk","Ethiopien","Nazis","Le Collabo", "Judas","Sorcier","Sorcière","Canard","Ours","Renard","Cactus","MST",};
	private static final String[] adjDef = new String[] {"du cul","De petite vertue","Pestiféré","Illétré","Borgne","Sanglant(e)","Purulente","à foutre","illégitime","de viol","dégéneré(e)","avorté(e)","Conspirasionniste","Illuminati","anal(e)","jaune","noir","chrétien(ne)","défiguré(e)","abandonné(e) sur le bord de la A7","à l'anus retourné(e)","vlogeur(se)","instagrameur(se)","soumise","consanguin","volant(e)","de tes morts","supremasiste blanc","nazi","juif","arabe","dans ton cul","En plein gangbang","et des tentacules"};
	public static boolean pause= false;
	public static GuildStorage<JudasSavable> storage = new GuildStorage<JudasSavable>(JudasSavable.class);
	public static GuildStorage<JudasSavableWords> storageWords = new GuildStorage<JudasSavableWords>(JudasSavableWords.class);

	@Override
	public String getName() {

		return "judas";
	}

	@Override
	public String getDesc() {
		return "rename a player with offensive names";
	}

	@Override
	public void execute(MessageReceivedEvent event, String ... args) {
		if(args.length >= 1)
		{
			if(args.length>=2)
			{
				if(args[1].equals("-name"))
				{
					JudasSavableWords save = storageWords.get(event.getGuild().getId()); 

					if(save == null)
					{
						storageWords.set(event.getGuild(), new JudasSavableWords());
					}
					if(args[0].equals("-add"))
					{
						String s = "";
						for(int i =2;i<args.length;i++)
						{
							s =s+args[i]+" ";
						}
						s = s.substring(0,s.length()-2);
						save.names.add(s);
						storageWords.save(event.getGuild().getId());
						event.getChannel().sendMessage("'"+s+"' added to the names!").queue();;
						return;
					}else
						if(args[0].equals("-list"))
						{
							String s = "Names list:\n";
							for(String s1 : save.names)
							{
								s =s+s1+"\n";
							}
							event.getChannel().sendMessage(s).queue();
							return;
						}else
							if(args[0].equals("-clear"))
							{
								save.names.clear();
								storageWords.save(event.getGuild().getId());
								event.getChannel().sendMessage("Names list cleared!").queue();
								return;
							}
				}		
				else if(args[1].equals("-adj"))
				{
					if(args[0].equals("-add"))
					{
						JudasSavableWords save = storageWords.get(event.getGuild().getId());
						String s = "";
						for(int i =2;i<args.length;i++)
						{
							s =s+args[i]+" ";
						}
						s = s.substring(0,s.length()-2);
						save.adjs.add(s);
						storageWords.save(event.getGuild().getId());
						event.getChannel().sendMessage("'"+s+"' added to the adjectifs!").queue();;
						return;
					}else
						if(args[0].equals("-list"))
						{
							JudasSavableWords save = storageWords.get(event.getGuild().getId());
							String s = "Adjs list:\n";
							for(String s1 : save.adjs)
							{
								s =s+s1+"\n";
							}
							event.getChannel().sendMessage(s).queue();
							return;
						}
						else if(args[0].equals("-clear"))
						{
							JudasSavableWords save = storageWords.get(event.getGuild().getId());
							save.adjs.clear();
							storageWords.save(event.getGuild().getId());
							event.getChannel().sendMessage("Adjectifs list cleared!").queue();
							return;
						}
				}

			}
			if(args[0].equals("-addDefault"))
			{	
				JudasSavableWords save = storageWords.get(event.getGuild().getId());
				for(String s : nameDef)
				{
					save.names.add(s);
				}
				for(String s : adjDef)
				{
					save.adjs.add(s);
				}
				storageWords.save(event.getGuild().getId());
				event.getChannel().sendMessage("Deafault names and adjs addeds!").queue();
			}
			else
				if(args[0].equals("-pause"))
				{
					pause = !pause;
					String s = "";
					if(pause)
						s="Judas mode paused for all!";
					else
						s="Judas mode unpaused for all!";

					event.getChannel().sendMessage(s).queue();
				}
				else
				{
					List<Member> mb = MembersUtil.getMembersMatching(event.getGuild().getMembers(),args[0]);
					if(!mb.isEmpty())
					{
						JudasSavable save = storage.get(event.getGuild().getId()); 

						if(save == null)
						{
							save = new JudasSavable();
							//save.set(JudasSavable.KEY, save);
						}
						Boolean val =save.data.get(mb.get(0).getUser().getId());
						if(val== null)
							val = false;
						save.data.put(mb.get(0).getUser().getId(), !val);
						storage.save(event.getGuild().getId());

						GuildController ctrl = new GuildController(event.getGuild());
						if(!val)
						{
							ctrl.setNickname(mb.get(0), getNewName(event.getGuild())).queue();
							event.getChannel().sendMessage("Judas mode turned on for "+mb.get(0).getUser().getName()+" !").queue();
						}
						else
						{
							ctrl.setNickname(mb.get(0),mb.get(0).getUser().getName()).queue();
							event.getChannel().sendMessage("Judas mode turned off for "+mb.get(0).getUser().getName()).queue();
						}
					}
				}
		}
	}


	@Override
	public String getUsage() {
		return ">judas <MemberName>/-pause/ <-add/-clear/-list <-name/-adj>>/ -addDefault";
	}

	private static Random rand= new Random();
	public static String getNewName(Guild g)
	{
		JudasSavableWords save = storageWords.get(g.getId()); 
		String[] name = save.names.toArray(new String[0]);
		String[] adj = save.adjs.toArray(new String[0]);
		String s = name[rand.nextInt(name.length)];
		int c = rand.nextInt(3)+1;
		while(c>0)
		{
			s= s+" "+adj[rand.nextInt(adj.length)];
			c--;
		}
		if(s.length()>32)
			return getNewName(g);
		return s;

	}

	@Override
	public void init() {
		Storage.list.add(storage);
		Storage.list.add(storageWords);
		Registry.registerTickable(this);
	}

	private static int random = (int) (Math.random()*2000);
	@Override
	public void tick(Guild guild) {
		if(!JudasCmd.pause)
		{
			random = (int) (Math.random()*2000);
			JudasSavable save= JudasCmd.storage.get(guild.getId());
			if(save != null)
			{
				for(String user : save.data.keySet())
				{
					if(save.data.get(user) != null && save.data.get(user)==true)
					{
						GuildController ctrl = new GuildController(guild);
						if(ctrl != null && References.bot.getUserById(user) != null )
						{
							try {
								ctrl.setNickname(guild.getMember(References.bot.getUserById(user)), JudasCmd.getNewName(guild)).queue();;
							}catch(IllegalArgumentException | HierarchyException e1 ) {};
						}
					}
				}
			}
		}

	}

	@Override
	public int everyMS() {
		return 600000+(random*600000);
	}

	@Override
	public void tickSingle() {

	}


}
