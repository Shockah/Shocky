package pl.shockah.shocky;

import org.json.JSONObject;
import pl.shockah.Config;

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
}