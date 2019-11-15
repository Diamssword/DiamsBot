package diamssword.bot.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.diamssword.bot.api.utils.LoadUtils;

public class SatanicFont {

	public static Map<Character,Integer> table =new HashMap<Character,Integer>();
	static {
		table.put('a', 1);
		table.put('b', 6);
		table.put('c', 5);
		table.put('d', 3);
		table.put('e', 0);
		table.put('f', 2);
		table.put('g', 4);
		table.put('h', 11);
		table.put('i', 12);
		table.put('j', 4);
		table.put('k', 5);
		table.put('l', 10);
		table.put('m', 13);
		table.put('n', 7);
		table.put('o', 19);
		table.put('p', 9);
		table.put('q', 8);
		table.put('r', 18);
		table.put('s', 15);
		table.put('t', 14);
		table.put('u', 16);
		table.put('v', 17);
		table.put('w', 17);
		table.put('x', 20);
		table.put('y', 12);
		table.put('z', 17);
		table.put(' ', 21);

	}
	public static BufferedImage img = LoadUtils.loadImg("satanic.png");
	public static BufferedImage fromString(String s)
	{
		String reverse = "";
		for(int i = s.length() - 1; i >= 0; i--)
		{
			reverse = reverse + s.charAt(i);
		}
		reverse = reverse.toLowerCase().replaceAll("[^a-z]", " ");
		int lenght = reverse.length()*32;
		BufferedImage res = new BufferedImage(lenght, 40, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graph = res.createGraphics();
		
		for(int i= 0;i<reverse.length();i++)
		{
			int pos=table.get(reverse.charAt(i));
			graph.drawImage(img, i*32, 0, (i*32)+32, 40, pos*32, 0, (pos*32)+32, 40, null);	
		}
		graph.dispose();
		return res;
	}
}
