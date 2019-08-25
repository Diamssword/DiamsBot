package diamssword.bot.actions;

import java.net.MalformedURLException;
import java.net.URL;

import com.diamssword.bot.api.References;
import com.diamssword.bot.api.actions.IAction;
import com.diamssword.bot.api.audio.Langues;
import com.diamssword.bot.api.utils.LoadUtils;
import com.google.gson.Gson;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ChatBot implements IAction{
	private static Gson gson = new Gson();

		@Override
		public void execute(MessageReceivedEvent event,String trigger, String before, String after) {
			if(!event.getAuthor().isFake() && !event.getAuthor().isBot())
			{
				if(before.length() <1)
				{
					String res;
					try {
						res = LoadUtils.urlToString(new URL("https://some-random-api.ml/chatbot?message="+after.replaceAll(" ", "%20")));
						JsonResp resp =gson.fromJson(res, JsonResp.class);
						if(event.isFromType(ChannelType.TEXT))
						{
							Member memb =event.getGuild().getMemberById(event.getAuthor().getId());
							VoiceChannel chan =memb.getVoiceState().getChannel();
							if(chan !=null)
							{
								References.PlayerManager.loadTrackForMember(References.TTSMaker.speak(Langues.en_us,resp.response).getAbsolutePath(),event.getGuild().getId(),memb);
								return;
							}
						}
						event.getChannel().sendMessageFormat(resp.response).queue();
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}

				}
			}

		}

		@Override
		public String[] getTriggers(Guild g) {
			Member m = g.getMemberById(References.bot.getSelfUser().getId());
			return new String[] {m.getAsMention()};
		}

		@Override
		public String usage() {
			return "Start your message by notifing the bot (@DiamsBot) with a message and he will respond";
		}

		@Override
		public String name() {
			return "ChatBot";
		}

		@Override
		public void init() {
		}

		public static class JsonResp{
			public String response;
		}
}
