package pl.shockah.shocky.console.views;

import pl.shockah.shocky.console.ConsoleThread;
import pl.shockah.shocky.console.TextInputHandler;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenWriter;
import com.googlecode.lanterna.terminal.TerminalSize;

public class ViewMain extends View {
	protected TextInputHandler tih = new TextInputHandler();
	
	public void update(ConsoleThread thread, Screen screen, ScreenWriter sw) {
		TerminalSize ts = screen.getTerminalSize();
		String input = tih.toString();
		
		Key key;
		while ((key = screen.readInput()) != null) tih.handle(key);
		
		for (int x = 0; x < ts.getColumns(); x++) sw.drawString(x,ts.getRows()-2,"=");
		sw.drawString(0,ts.getRows()-1,input);
		screen.setCursorPosition(tih.getPosition(),ts.getRows()-1);
	}
}