package pl.shockah.shocky.bot.ident;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class IdentMethodManager {
	protected static Map<String,IIdentMethod> map = Collections.synchronizedMap(new HashMap<String,IIdentMethod>());
	
	static {
		map.put("NickServ",new IdentNickServ());
	}
	
	public static IIdentMethod getMethod(String methodName) {
		return map.get(methodName);
	}
}