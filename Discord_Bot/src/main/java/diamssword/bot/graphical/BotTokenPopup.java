package diamssword.bot.graphical;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;

public class BotTokenPopup implements IFramePopup{

	public String title;
	public BotTokenPopup(String title)
	{
		this.title = title;
	}
	@Override
	public String message() {
		return title;
	}

	@Override
	public Object[] buttons() {
		JButton b =new JButton("OK");
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				

			}});
		JTextField f = new JTextField();
		f.setSize(100, 20);
	/*	return new Object[] {
				f,
				b
		};*/
		return null;
	}

	@Override
	public String title() {
		return "Token du bot";
	}

}
