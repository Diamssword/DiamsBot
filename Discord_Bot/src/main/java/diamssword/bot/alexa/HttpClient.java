package diamssword.bot.alexa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class HttpClient {

	public static final String KEY = "AIzaSyDc7h4l7cobakkmHCwDrnKK0B0bDLtBvxY";
	private final static String USER_AGENT = "Mozilla/5.0";
	
	public static YTVideoJson[] searchvid(int maxRes,String research) throws IOException
	{
		//https://api.spotify.com/v1/search?q=tania%20bowra&type=artist&token=
		String stringUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults="+maxRes+"&order=relevance&q="+research.replace(" ", "%20")+"&type=video&key="+KEY;
		URL url = new URL(stringUrl);
		String basicAuth = "Basic " + new String(Base64.getUrlEncoder().encode(KEY.getBytes()));

		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("Sending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		try {
		YTRequestJson req = new GsonBuilder().setPrettyPrinting().create().fromJson(response.toString(), YTRequestJson.class);
		return req.getasVideo();
		}catch(JsonSyntaxException e)
		{e.printStackTrace();
			
		}
		return new YTVideoJson[0];
	//	System.out.println(response.toString());
		
		
	}
}