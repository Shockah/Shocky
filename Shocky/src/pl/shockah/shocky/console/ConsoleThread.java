package pl.shockah.shocky.console;

import pl.shockah.shocky.StoppableThread;
import pl.shockah.shocky.console.views.View;
import pl.shockah.shocky.console.views.ViewConfigWizard;
import pl.shockah.shocky.console.views.ViewMain;
import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenWriter;

public class ConsoleThread extends StoppableThread {
	public View view;
	
	public ConsoleThread() {this(false);}
	public ConsoleThread(boolean configWizard) {
		view = configWizard ? new ViewConfigWizard() : new ViewMain();
	}
	
	public void onRun() {
		Screen screen = TerminalFacade.createScreen();
		if (screen == null) return;
		
		setPriority((Thread.NORM_PRIORITY+Thread.MIN_PRIORITY)/2);
		ScreenWriter sw = new ScreenWriter(screen);
		
		screen.startScreen();
		while (running) {
			sw.fillScreen(' ');
			
			view.update(this,screen,sw);
			if (screen.resizePending()) screen.completeRefresh(); else screen.refresh();
			
			try {
				Thread.sleep(10);
			} catch (Exception e) {}
		}
		screen.stopScreen();
	}
}