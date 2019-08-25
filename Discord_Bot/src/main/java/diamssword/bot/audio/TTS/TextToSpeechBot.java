package diamssword.bot.audio.TTS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;

import com.diamssword.bot.api.audio.IttsMaker;
import com.diamssword.bot.api.audio.Langues;

import diamssword.bot.Main;

public class TextToSpeechBot implements IttsMaker {

	public static int count = 0;
	public void clearCache()
	{
		try {
			FileUtils.deleteDirectory(new File("botdata/ttscache"));
			Main.LOG.log(Level.INFO, "Cleared TTSCache");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public File speak(Langues lang,String speech)
	{
		new File("botdata/ttsCache").mkdirs();
		File f =new File("botdata/ttsCache/tts"+count+".wav");
		f.delete();
		try {	
			URL url = new URL("https://api.voicerss.org/?key=4f232ef422b8492abeeedf7f8ef038b6&f=48khz_16bit_stereo&hl="+lang.toString().replaceAll("_", "-")+"&src="+URLEncoder.encode(speech, "UTF-8"));
			InputStream in = url.openConnection().getInputStream();
			OutputStream out = new FileOutputStream(f);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			out.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		count++;
		return f;
		
	}
}
