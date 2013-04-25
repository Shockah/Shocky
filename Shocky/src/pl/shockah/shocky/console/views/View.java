package pl.shockah.shocky.console.views;

import pl.shockah.shocky.console.ConsoleThread;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenWriter;

public abstract class View {
	public abstract void update(ConsoleThread thread, Screen screen, ScreenWriter sw);
}