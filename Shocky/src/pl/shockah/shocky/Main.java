package pl.shockah.shocky;

import java.io.File;
import pl.shockah.Config;
import pl.shockah.shocky.console.ConsoleThread;
import com.mongodb.DB;
import com.mongodb.Mongo;

public class Main {
	public static String[] args;
	
	public static Mongo mongo;
	public static DB db;
	
	public static void main(String[] args) {
		Main.args = args;
		main();
	}
	public static void main() {
		try {
			File f = new File("config.cfg");
			if (f.exists()) {
				Config cfg = new Config();
				cfg.load(f);
				
				if (!cfg.exists("mongo->host") || !cfg.exists("mongo->db")) new ConsoleThread(true).start();
				else new ConsoleThread().start();
			} else new ConsoleThread(true).start();
		} catch (Exception e) {e.printStackTrace();}
	}
}