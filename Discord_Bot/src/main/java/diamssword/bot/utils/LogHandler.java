package diamssword.bot.utils;

import java.awt.Color;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import com.diamssword.bot.api.utils.ELoggerControl;
import com.diamssword.bot.api.utils.ILogHandler;

import diamssword.bot.Main;

public class LogHandler extends Handler implements ILogHandler {
	public final ELoggerControl[] control;
	public PrintStream customPrint = null;
	public LogHandler()
	{
		control =new ELoggerControl[] {ELoggerControl.NONE};
	}
	public LogHandler(ELoggerControl... control)
	{
		this.control = control;
	}
	public LogHandler(Color consoleColor,ELoggerControl... control)
	{
		this.control = control;
		customPrint = new PrintStream(new ConsoleOutputStream(Main.uiFrame.console, consoleColor));
	}
	@Override
	public void publish(LogRecord record) {
		String time = "";
		if(!this.have(ELoggerControl.NO_TIME_CODE))
			time = formatTime(record.getMillis());
		String className="";
		if(!this.have(ELoggerControl.NO_CLASS_NAME))
		{
			if(this.have(ELoggerControl.REDUCED_CLASS_NAME))
			{
				className = " : "+record.getSourceClassName().substring(record.getSourceClassName().lastIndexOf('.')+1);
			}
			else
			{
				className = " : "+record.getSourceClassName();
			}
		}
		String s = time+" ["+record.getLoggerName()+"] "+record.getLevel().getName()+className+" : "+record.getMessage();
		if(this.customPrint == null)
		{
			if(record.getLevel() == Level.SEVERE || record.getLevel() == Level.WARNING)
				System.err.println(s);
			else
				System.out.println(s);
		}
		else
		{
			customPrint.println(s);
		}

	}

	@Override
	public void flush() {

	}

	@Override
	public void close() throws SecurityException {

	}

	private String formatTime(long time)
	{
		Calendar c =Calendar.getInstance();
		c.setTime(new Date(time));
		return String.format("%d:%d:%d:%d", 
				c.get(Calendar.HOUR_OF_DAY),
				c.get(Calendar.MINUTE),
				c.get(Calendar.SECOND),
				c.get(Calendar.MILLISECOND)
				);
	}

	public boolean have(ELoggerControl control)
	{
		for(ELoggerControl ctrl : this.control)
		{
			if(ctrl == control)
				return true;
		}
		return false;
	}

	@Override
	public ILogHandler instanciate(Color color,ELoggerControl... control) {
		if(color == null)
			return new LogHandler(control);
		return new LogHandler(color,control);
	}

}
