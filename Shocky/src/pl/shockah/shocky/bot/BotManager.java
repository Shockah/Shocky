package pl.shockah.shocky.bot;

import java.util.Collections;
import java.util.List;
import org.pircbotx.PircBotX;
import pl.shockah.Util;

public class BotManager {
	private List<PircBotX> bots = Util.syncedList(PircBotX.class);
	
	public List<PircBotX> getAllBots() {
		return Collections.unmodifiableList(bots);
	}
}