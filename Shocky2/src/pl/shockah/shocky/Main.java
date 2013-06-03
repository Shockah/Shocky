package pl.shockah.shocky;

import java.io.File;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.shockah.Config;
import pl.shockah.shocky.bot.BotManager;
import pl.shockah.shocky.bot.ServerManager;
import pl.shockah.shocky.bot.ident.IdentMethodManager;
import pl.shockah.shocky.console.ConsolePrintStream;
import pl.shockah.shocky.console.ConsoleThread;
import pl.shockah.shocky.console.TextInputThread;
import pl.shockah.shocky.console.TextInputWaitingListener;
import pl.shockah.shocky.console.tabs.ConsoleTabInput;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class Main {
	public static String[] args;
	public static ConsoleThread ct;
	public static ServerManager manager;
	
	public static MongoClient mongo;
	public static DB db;
	
	public static void main(String[] args) {
		Main.args = args;
		main();
	}
	public static void main() {
		try {
			(ct = new ConsoleThread()).start();
			Config cfg = checkConfig();
			cfg.save(new File("mongo.cfg"));
			
			if (cfg.exists("port")) {
				mongo = new MongoClient(cfg.getString("host"),cfg.getInt("port"));
			} else {
				mongo = new MongoClient(cfg.getString("host"));
			}
			db = mongo.getDB(cfg.getString("db"));
			if (!cfg.getString("user").isEmpty()) {
				if (!db.authenticate(cfg.getString("user"),cfg.getString("pass").toCharArray())) {
					throw new RuntimeException("Couldn't authenticate into database.");
				}
			}
			
			manager = new ServerManager();
			new Thread(){
				public void run() {
					try {
						DBCollection col = db.getCollection("servers");
						JSONArray jServers = JSONUtil.toJSONArray(col.find());
						for (int i = 0; i < jServers.length(); i++) {
							JSONObject jServer = jServers.getJSONObject(i);
							JSONObject jIdent = jServer.has("ident") ? jServer.getJSONObject("ident") : null;
							BotManager botManager = new BotManager(
									jServer.getString("botname"),
									jServer.getString("host"),
									jServer.has("port") ? jServer.getInt("port") : 6667,
									jIdent != null ? IdentMethodManager.getMethod(jIdent.getString("method")) : null
							);
							if (jIdent != null) botManager.setIdentDocument(jIdent);
							Main.manager.register(botManager);
							
							if (jServer.has("channels")) {
								JSONArray jChannels = jServer.getJSONArray("channels");
								for (int j = 0; j < jChannels.length(); j++) botManager.joinChannel(jChannels.getString(j));
							}
							if (botManager.getBots().isEmpty()) botManager.connectNew();
						}
					} catch (Exception e) {e.printStackTrace();}
				}
			}.start();
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public static Config checkConfig() {
		File f = new File("mongo.cfg");
		if (!f.exists()) return runConfigWizard();;
		
		Config cfg = new Config();
		try {
			cfg.load(f);
			if (!cfg.exists("host")) return runConfigWizard();
		} catch (Exception e) {return runConfigWizard();}
		return cfg;
	}
	
	public static Config runConfigWizard() {
		ct.tit.end();
		TextInputWaitingListener tiwl = new TextInputWaitingListener();
		ct.tit = new TextInputThread(ct,tiwl,null);
		
		ConsoleTabInput tab = new ConsoleTabInput(ct,"Config Wizard");
		ct.tabs.add(tab);
		ct.tabs.select(tab);
		ct.tit.start();
		
		Config cfg = new Config();
		ConsolePrintStream cps = new ConsolePrintStream(ct,tab);
		cps.println("---> MongoDB config <---");
		String input;
		
		ct.tit.tih.clear();
		cps.print("Host (leave blank for 'localhost'): ");
		input = tiwl.get();
		if (input.isEmpty()) input = "localhost";
		cps.print(input);
		cfg.set("host",input);
		cps.println();
		
		ct.tit.tih.clear();
		cps.print("Database (leave blank for 'shocky'): ");
		input = tiwl.get();
		if (input.isEmpty()) input = "shocky";
		cps.print(input);
		cfg.set("db",input);
		cps.println();
		
		ct.tit.tih.clear();
		cps.print("Port (leave blank for default): ");
		input = tiwl.get();
		if (input.isEmpty()) input = "<default>";
		cps.print(input);
		if (!input.equals("<default>")) cfg.set("port",Integer.parseInt(input));
		cps.println();
		
		ct.tit.tih.clear();
		cps.print("User: ");
		input = tiwl.get();
		cps.print(input);
		cfg.set("user",input);
		cps.println();
		
		if (!input.isEmpty()) {
			ct.tit.tih.clear();
			tab.password = true;
			cps.print("Password: ");
			input = tiwl.get();
			for (int i = 0; i < input.length(); i++) cps.print('*');
			cfg.set("pass",input);
			cps.println();
			tab.password = false;
		}
		
		ct.tit.end();
		ct.setupTextInputThread();
		cps.close();
		ct.tabs.remove(tab);
		ct.tabs.selectAt(0);
		
		try {
			System.out.println(JSONUtil.toJSONObject(cfg).toString(2));
		} catch (Exception e) {e.printStackTrace();}
		return cfg;
	}
}