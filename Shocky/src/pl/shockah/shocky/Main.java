package pl.shockah.shocky;

import java.io.File;
import pl.shockah.Config;
import pl.shockah.shocky.bot.ServerManager;
import pl.shockah.shocky.console.ConsolePrintStream;
import pl.shockah.shocky.console.ConsoleThread;
import pl.shockah.shocky.console.TextInputThread;
import pl.shockah.shocky.console.TextInputWaitingListener;
import pl.shockah.shocky.console.tabs.ConsoleTabInput;
import com.mongodb.DB;
import com.mongodb.Mongo;

public class Main {
	public static String[] args;
	public static ConsoleThread ct;
	public static ServerManager manager;
	
	public static Mongo mongo;
	public static DB db;
	
	public static void main(String[] args) {
		Main.args = args;
		main();
	}
	public static void main() {
		(ct = new ConsoleThread()).start();
		
		File f = new File("mongo.cfg");
		if (!f.exists()) {
			runConfigWizard();
			return;
		}
		
		try {
			Config cfg = new Config();
			cfg.load(f);
			if (!cfg.exists("host")) runConfigWizard();;
		} catch (Exception e) {runConfigWizard();}
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
		cps.print("User: ");
		input = tiwl.get();
		cps.print(input);
		cfg.set("user",input);
		cps.println();
		
		ct.tit.tih.clear();
		tab.password = true;
		cps.print("Password: ");
		input = tiwl.get();
		for (int i = 0; i < input.length(); i++) cps.print('*');
		cfg.set("pass",input);
		cps.println();
		tab.password = false;
		
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