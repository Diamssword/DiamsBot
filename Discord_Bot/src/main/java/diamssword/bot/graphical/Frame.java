package diamssword.bot.graphical;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.PrintStream;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import com.diamssword.bot.api.utils.LoadUtils;

import diamssword.bot.Main;
import diamssword.bot.utils.ConsoleOutputStream;

@SuppressWarnings("serial")
public class Frame extends JFrame {
	public JBotStatus botStat;
	private JPanel contentPane;
	public JTextPane console;
	public JTextPane serversList;
	private JTextField textField;
	/**
	 * Create the frame.
	 */
	public Frame() {
		setResizable(true);
		this.setUndecorated(false);
		BufferedImage icon1 = LoadUtils.loadImg("bot_status.png");
		BufferedImage icon =new BufferedImage(90, 90, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g =icon.createGraphics();
		g.drawImage(icon1, 0, 0, 90, 90,19,1,19+90,1+90, null);
		g.dispose();
		this.setIconImage(icon);
		this.setTitle("DiamsBot: \"God is dead and we killed him\"");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnstop = new JButton("KILL ME!");
		btnstop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.kill();
			}
		});
		btnstop.setBounds(10, 376, 180, 61);
		contentPane.add(btnstop);

		console = new JTextPane();
		console.setEditable(false);
		console.setBackground(Color.LIGHT_GRAY);
		JPanel consolePane = new JPanel();
		consolePane.setLayout(new BorderLayout());
		//	console.setContentType("text/html;charset=UTF-8");
		consolePane.add(console, BorderLayout.NORTH);
		consolePane.setBackground(Color.LIGHT_GRAY);
		JScrollPane scrollBar = new JScrollPane();
		scrollBar.setBounds(200, 0, contentPane.getWidth()-200,contentPane.getHeight());
		scrollBar.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollBar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollBar.setViewportView(consolePane);
		scrollBar.getVerticalScrollBar().setUnitIncrement(16);
		contentPane.add(scrollBar);

		JLabel lblDiamsbot = new JLabel("DiamsBot\u2122");
		lblDiamsbot.setForeground(Color.WHITE);
		lblDiamsbot.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
		lblDiamsbot.setBounds(41, 11, 115, 35);
		contentPane.add(lblDiamsbot);

		botStat = new JBotStatus();
		botStat.setBounds(26, 57, 130, 150);
		contentPane.add(botStat);
		contentPane.addComponentListener(new ComponentListener() {

			@Override
			public void componentResized(ComponentEvent e) {
				scrollBar.setBounds(200, 0, contentPane.getWidth()-200,contentPane.getHeight());
				scrollBar.repaint();
			}

			@Override
			public void componentMoved(ComponentEvent e) {}

			@Override
			public void componentShown(ComponentEvent e) {}
			@Override
			public void componentHidden(ComponentEvent e) {
			}});

		//redirect outStreams to Graphical Console and log file
		System.setOut(new PrintStream(new ConsoleOutputStream(console,new Color(0,163,30))));
		System.setErr(new PrintStream(new ConsoleOutputStream(console,Color.RED)));
		serversList = new JTextPane();
		serversList.setEditable(false);
		serversList.setBackground(Color.DARK_GRAY);
		serversList.setForeground(Color.white);
		JPanel serversPane = new JPanel();
		serversPane.setLayout(new BorderLayout());
		serversPane.add(serversList, BorderLayout.NORTH);
		serversPane.setBackground(Color.DARK_GRAY);
		serversPane.setBorder(null);
		JScrollPane scrollBarServers = new JScrollPane();
		scrollBarServers.setBounds(10, 229, 180, 136);
		scrollBarServers.setBackground(Color.DARK_GRAY);
		scrollBarServers.setForeground(Color.DARK_GRAY);
		scrollBarServers.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollBarServers.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollBarServers.setViewportView(serversPane);
		scrollBarServers.getVerticalScrollBar().setUnitIncrement(16);
		contentPane.add(scrollBarServers); 
		textField = new JTextField();
		textField.setBounds(10, 229, 180, 136);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel lblGuildsList = new JLabel("Guilds list:");
		lblGuildsList.setForeground(Color.WHITE);
		lblGuildsList.setBounds(10, 216, 105, 14);
		contentPane.add(lblGuildsList);
	}
	public Object createPopup(IFramePopup popup,boolean InputType)
	{
		if(!InputType)
			return JOptionPane.showOptionDialog(this,popup.message() , popup.title(), JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null, popup.buttons(),null);
		else
			return JOptionPane.showInputDialog(this,popup.message() , popup.title(), JOptionPane.YES_OPTION,null, popup.buttons(),null);
	}

}

