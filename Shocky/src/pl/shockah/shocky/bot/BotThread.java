package pl.shockah.shocky.bot;

import org.json.JSONArray;
import org.json.JSONObject;
import pl.shockah.shocky.Main;
import pl.shockah.shocky.StoppableThread;
import pl.shockah.shocky.bot.ident.IdentMethodManager;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

public class BotThread extends StoppableThread {
	public void onRun() {
		try {
			Main.mongo = new Mongo();
			Main.db = Main.mongo.getDB("shocky");
			Main.manager = new ServerManager();
			
			DBCollection col = Main.db.getCollection("servers");
			DBCursor cur = col.find();
			try {
				while (cur.hasNext()) {
					JSONObject json = new JSONObject(cur.next().toString());
					JSONObject jIdent = json.has("ident") ? json.getJSONObject("ident") : null;
					BotManager botManager = new BotManager(
							json.getString("name"),
							json.getString("host"),
							json.has("port") ? json.getInt("port") : 6667,
							jIdent != null ? IdentMethodManager.getMethod(jIdent.getString("method")) : null
					);
					if (jIdent != null) botManager.setIdentDocument(jIdent);
					Main.manager.register(botManager);
					
					if (json.has("channels")) {
						JSONArray jChannels = json.getJSONArray("channels");
						for (int i = 0; i < jChannels.length(); i++) botManager.joinChannel(jChannels.getString(i));
					}
					if (botManager.getBots().isEmpty()) botManager.connectNew();
				}
			} finally {cur.close();}
		} catch (Exception e) {e.printStackTrace();}
	}
}