package pl.shockah.shocky.console.tabs;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import pl.shockah.shocky.console.ConsoleThread;
import com.googlecode.lanterna.terminal.TerminalSize;

public class ConsoleTabOutput extends ConsoleTab {
	public List<String> lines = Collections.synchronizedList(new LinkedList<String>());
	public StringBuffer sb = new StringBuffer();
	
	public ConsoleTabOutput(ConsoleThread ct, String title) {
		super(ct,title);
	}

	public void update() {
		int yy = ct.screen.getTerminalSize().getRows()-1;
		if (sb.length() > 0) ct.writer.drawString(0,yy--,sb.toString());
		
		while (lines.size() > 500) lines.remove(0);
		LinkedList<String> rev = new LinkedList<String>(lines);
		Iterator<String> it = rev.descendingIterator();
		while (it.hasNext()) {
			ct.writer.drawString(0,yy--,it.next());
			if (yy < 2) break;
		}
		
		TerminalSize size = ct.screen.getTerminalSize();
		ct.screen.setCursorPosition(size.getColumns()-1,size.getRows()-1);
	}
}