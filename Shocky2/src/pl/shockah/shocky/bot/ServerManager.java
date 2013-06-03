package pl.shockah.shocky.bot;

import java.util.Collections;
import java.util.List;
import pl.shockah.Util;

public class ServerManager {
	private List<BotManager> botManagers = Util.syncedList(BotManager.class);
	
	public List<BotManager> getBotManagers() {
		return Collections.unmodifiableList(botManagers);
	}
	
	public void register(BotManager botManager) {
		botManagers.add(botManager);
	}
}