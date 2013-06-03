package pl.shockah.shocky.console.tabs;

import java.util.Iterator;
import java.util.LinkedList;
import pl.shockah.shocky.console.ConsoleThread;
import pl.shockah.shocky.console.TextInputThread;
import com.googlecode.lanterna.terminal.TerminalSize;

public abstract class ConsoleTabInput extends ConsoleTabOutput {
	protected boolean open = false;
	protected TextInputThread tit;
	
	public ConsoleTabInput(ConsoleThread ct, String title) {
		super(ct,title);
	}
	
	public abstract void onOpen(); //create and start TextInputThread, wait for result

	public void update() {
		if (!open) {
			onOpen();
			open = true;
		}
		
		TerminalSize size = ct.screen.getTerminalSize();
		int yy = size.getRows()-3;
		if (sb.length() > 0) ct.writer.drawString(0,yy--,sb.toString());
		
		Iterator<String> it = ((LinkedList<String>)lines).descendingIterator();
		while (it.hasNext()) {
			ct.writer.drawString(0,yy--,it.next());
			if (yy < 0) break;
		}
		
		for (int i = 0; i < size.getColumns(); i++) ct.writer.drawString(i,size.getRows()-2,"=");
		ct.screen.setCursorPosition(size.getColumns()-1,size.getRows()-1);
	}
}