package pl.shockah.shocky.bot;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.exception.NickAlreadyInUseException;
import pl.shockah.Util;
import pl.shockah.shocky.bot.ident.IIdentMethod;

public class BotManager {
	private final String host;
	private final int port;
	private final IIdentMethod identMethod;
	private String name;
	private int channelLimit = 25;
	private List<PircBotX> bots = Util.syncedList(PircBotX.class);
	
	public BotManager(String name, String host) {this(name,host,6667);}
	public BotManager(String name, String host, int port) {this(name,host,port,null);}
	public BotManager(String name, String host, int port, IIdentMethod identMethod) {
		this.name = name;
		this.host = host;
		this.port = port;
		this.identMethod = identMethod;
	}
	
	public List<PircBotX> getBots() {
		return Collections.unmodifiableList(bots);
	}
	
	public PircBotX connectNew() throws IOException, IrcException {
		PircBotX bot = new PircBotX();
		
		bot.setName(name);
		bot.setAutoNickChange(true);
		bot.setVerbose(true);
//		bot.setAutoReconnect(true);
//		bot.setAutoReconnectChannels(true);
		bot.setEncoding("UTF-8");
		
		try {
			bot.connect(host,port);
		} catch (NickAlreadyInUseException e) {}
		
		if (identMethod != null) identMethod.ident(this,bot);
		
		bots.add(bot);
		return bot;
	}
	
	public PircBotX joinChannel(String name) throws IOException, IrcException {
		for (PircBotX bot : bots) {
			if (bot.getChannels().size() < channelLimit) {
				bot.joinChannel(name);
				return bot;
			}
		}
		PircBotX bot = connectNew();
		bot.joinChannel(name);
		return bot;
	}
	
	public PircBotX partChannel(String name) {
		for (PircBotX bot : bots) {
			for (Channel channel : bot.getChannels()) {
				if (channel.getName().equals(name)) {
					bot.partChannel(channel);
					return bot;
				}
			}
		}
		return null;
	}
	public PircBotX partChannel(Channel channel) {
		for (PircBotX bot : bots) {
			if (bot.getChannels().contains(channel)) {
				bot.partChannel(channel);
				return bot;
			}
		}
		return null;
	}
	
	public int getChannelLimit() {return channelLimit;}
	public void setChannelLimit(int limit) {channelLimit = limit;}
}