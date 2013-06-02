package pl.shockah.shocky.console.tabs;

import pl.shockah.shocky.console.ConsoleThread;

public abstract class ConsoleTab {
	protected final ConsoleThread ct;
	protected final String title;
	
	public ConsoleTab(ConsoleThread ct, String title) {
		this.ct = ct;
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public abstract void update();
}