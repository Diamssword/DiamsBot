package diamssword.bot.spotify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class HttpClient {

	public static final String KEY = " Bearer BQBwk0CVk5OJFaLZFu7QNqEjqgyprw_0EYsr9J5aKB3eV7yRbO1o_ItiMJv7J1m8tRmWmwjzSl6czateswk6x7RWBKA9k801-DYAidnabDRTYWBBL8JcJHGNTECPs0j80o-DN1x4SNeYJTeR4Q";
	private final String USER_AGENT = "Mozilla/5.0";
	public void exec() throws IOException
	{
		//https://api.spotify.com/v1/search?q=tania%20bowra&type=artist&token=
		String stringUrl = "https://api.spotify.com/v1/search?q=%22doom%22&type=track";
		URL url = new URL(stringUrl);
		String basicAuth = "Basic " + new String(Base64.getUrlEncoder().encode(KEY.getBytes()));

		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		// optional default is GET
//		con.setRequestMethod("GET");

		//add request header
	//	con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Authorization", KEY);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
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
		SpotifyReqJson js = new GsonBuilder().setPrettyPrinting().create().fromJson(response.toString(), SpotifyReqJson.class);
		System.out.println("Count = "+js.items.length);
		}catch(JsonSyntaxException e)
		{e.printStackTrace();
			
		}
		
	//	System.out.println(response.toString());
		
		
	}
}