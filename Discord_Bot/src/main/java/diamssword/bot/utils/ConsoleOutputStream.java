package diamssword.bot.utils;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Nullable;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class ConsoleOutputStream extends OutputStream {
	private JTextPane textArea;
	private SimpleAttributeSet aset = new SimpleAttributeSet();
	private FileOutputStream file;
	private ByteArrayOutputStream consoleStream;

	public ConsoleOutputStream(JTextPane editorPane,@Nullable Color textColor) {
		new File("botdata/logs/").mkdirs();
		this.textArea = editorPane;
		consoleStream = new ByteArrayOutputStream();
		StyleConstants.setForeground(aset, textColor);
		try {
			file = new FileOutputStream(new File("botdata/logs/bot-"+formatTime(System.currentTimeMillis())+".log"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void write(int b) throws IOException {
		file.write(b);
		consoleStream.write(b);
		try {
			if(b == '\n')
			{		
				textArea.setCharacterAttributes(aset, false);
				textArea.getStyledDocument().insertString(textArea.getDocument().getLength(), consoleStream.toString("UTF8"), aset);
				consoleStream.reset();
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
	private String formatTime(long time)
	{
		Calendar c =Calendar.getInstance();
		c.setTime(new Date(time));
		return String.format("%d.%d.%d-%d.%d.%d",    
				c.get(Calendar.DATE),
				c.get(Calendar.MONTH),
				c.get(Calendar.YEAR),
				c.get(Calendar.HOUR_OF_DAY),
				c.get(Calendar.MINUTE),
				c.get(Calendar.SECOND)
				);
	}
}