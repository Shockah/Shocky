package pl.shockah.shocky.console;

import com.googlecode.lanterna.input.Key;
import pl.shockah.shocky.StoppableThread;

public class TextInputThread extends StoppableThread {
	private final ConsoleThread ct;
	private final ITextInputIntercept[] tiis;
	public TextInputHandler tih;
	
	public TextInputThread(ConsoleThread ct, ITextInputListener til, ITextInputFilter tif, ITextInputIntercept... tiis) {
		this.ct = ct;
		this.tiis = tiis;
		tih = new TextInputHandler(til,tif);
	}
	
	public void onRun() {
		while (running) {
			L: while (true) {
				Key key = ct.screen.readInput();
				if (key == null) break;
				for (ITextInputIntercept tii : tiis) if (tii.interceptTextInput(key)) continue L;
				tih.handle(key);
			}
			
			try {
				Thread.sleep(10);
			} catch (Exception e) {}
		}
	}
}