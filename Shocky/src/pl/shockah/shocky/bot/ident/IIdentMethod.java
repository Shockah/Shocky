package pl.shockah.shocky.bot.ident;

import org.json.JSONObject;
import org.pircbotx.PircBotX;
import pl.shockah.shocky.bot.BotManager;

public interface IIdentMethod {
	public boolean ident(BotManager botManager, PircBotX bot, JSONObject jIdent);
}