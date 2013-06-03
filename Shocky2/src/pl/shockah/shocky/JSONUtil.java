package pl.shockah.shocky;

import org.json.JSONArray;
import org.json.JSONObject;
import pl.shockah.Config;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public final class JSONUtil {
	public static JSONObject toJSONObject(Config cfg) {
		JSONObject json = new JSONObject();
		toJSONObject(cfg,json);
		return json;
	}
	private static void toJSONObject(Config cfg, JSONObject json) {
		for (String key : cfg.getKeys()) try {
			json.put(key,cfg.getString(key));
		} catch (Exception e) {}
		
		for (String cfgKey : cfg.getKeysSubconfigs()) try {
			JSONObject json2 = new JSONObject();
			toJSONObject(cfg.getConfig(cfgKey),json2);
			json.put(cfgKey,json2);
		} catch (Exception e) {}
	}
	
	public static JSONObject toJSONObject(DBObject doc) {
		try {
			return new JSONObject(doc.toString());
		} catch (Exception e) {e.printStackTrace();}
		return null;
	}
	public static JSONArray toJSONArray(DBCursor cur) {
		JSONArray json = new JSONArray();
		while (cur.hasNext()) json.put(toJSONObject(cur.next()));
		return json;
	}
}