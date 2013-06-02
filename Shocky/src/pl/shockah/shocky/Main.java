package pl.shockah.shocky;

import pl.shockah.shocky.bot.ServerManager;
import pl.shockah.shocky.console.ConsoleThread;
import com.mongodb.DB;
import com.mongodb.Mongo;

public class Main {
	public static String[] args;
	public static ServerManager manager;
	
	public static Mongo mongo;
	public static DB db;
	
	public static void main(String[] args) {
		Main.args = args;
		main();
	}
	public static void main() {
		new ConsoleThread().start();
		System.out.println("asdf");
		System.out.println("ghjk");
	}
}