package diamssword.bot.graphical;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.diamssword.bot.api.utils.LoadUtils;

import net.dv8tion.jda.core.JDA.Status;

public class JBotStatus extends JPanel{
	private static final long serialVersionUID = -7071924805661607077L;
	private Status status = Status.INITIALIZING;
	private static BufferedImage bg = LoadUtils.loadImg("bot_status.png");
	public void setStatus(Status stat)
	{
		this.status = stat;
		this.setOpaque(false);
		this.setBackground(new Color(0,0,0,0));
		this.repaint();
	}
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g1 =bg.createGraphics();
		g1.copyArea(130, yOffset(), 18, 18, -42, 70-yOffset());
		g1.dispose();
		//g1.drawImage(bg, 88, 70, 18, 18, null);
		g.drawImage(bg,0,0,this.getWidth(),this.getHeight()-g.getFontMetrics().getHeight()-5,0, 0, 130,120,null);
		g.setFont(g.getFont().deriveFont(Font.BOLD | Font.ITALIC));
		g.setColor(Color.WHITE);
		g.drawString(status.name(), this.getWidth()/2-(g.getFontMetrics().stringWidth(status.name())/2), this.getHeight()-5);
		//g.dispose();
	}

	private int yOffset()
	{
		if(status == Status.CONNECTED)
			return 0;
		if(status == Status.FAILED_TO_LOGIN|| status ==Status.DISCONNECTED|| status ==Status.ATTEMPTING_TO_RECONNECT)
			return 18;
		if(status == Status.SHUTDOWN || status == Status.SHUTTING_DOWN ||status== Status.INITIALIZING)
			return 54;
		return 36;
	}
}
