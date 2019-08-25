package diamssword.bot.storage;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.diamssword.bot.api.utils.LoadUtils;

public class Inventory {

	public Item[] items = new Item[27];

	public Item getItem(int slot)
	{
		int s = slot;
		if(slot >= items.length)
			s = items.length-1;
		while(this.items[s] == null && s>0)
		{
			s-=1;
		}

		return this.items[s]; 
	}

	public boolean addItem(Item i)
	{
		int s = 0;
		while(items[s] != null && s < items.length)
		{
			s++;
		}
		if(s > items.length-1)
			return false;
		else
		{
			items[s] = i;
			return true;
		}
	}

	public BufferedImage inventoryImage()
	{
		BufferedImage inv = LoadUtils.loadImg("inv.png");
		int w = 32;
		int h = 28;
		Graphics g = inv.getGraphics();
		for(int i = 0;i<this.items.length;i++)
		{
			Item item = items[i];
			if( item != null)
			{
				g.drawImage(LoadUtils.loadImg("items/"+item.id+".png"), w, h, 64,64,null);
				w+=8+64;
				if(i == 8 || i == 17)
				{
					w = 32;
					h+=8+64;
				}
			}
		}
		g.dispose();
		return inv;
	}

}
