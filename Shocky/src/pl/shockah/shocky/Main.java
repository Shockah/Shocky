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
		ct.tit.tih.clear();
		
		Config cfg = new Config();
		ConsolePrintStream cps = new ConsolePrintStream(ct,tab);
		cps.println("---> MongoDB config <---");
		String input;
		
		cps.print("Host: ");
		input = tiwl.get();
		cfg.set("host",input.isEmpty() ? "localhost" : input);
		
		ct.tit.end();
		ct.setupTextInputThread();
		cps.close();
		ct.tabs.remove(tab);
		
		return cfg;
	}
}