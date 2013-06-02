package pl.shockah.shocky.console.tabs;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import pl.shockah.shocky.console.ConsoleThread;
import com.googlecode.lanterna.terminal.TerminalSize;

public class ConsoleTabOutput extends ConsoleTab {
	public List<String> lines = new LinkedList<String>();
	public StringBuffer sb = new StringBuffer();
	
	public ConsoleTabOutput(ConsoleThread ct, String title) {
		super(ct,title);
	}
	
	public int getHeight() {
		return ct.screen.getTerminalSize().getRows()-2;
	}

	public void update() {
		int yy = ct.screen.getTerminalSize().getRows()-1;
		if (sb.length() > 0) ct.writer.drawString(0,yy--,sb.toString());
		
		Iterator<String> it = ((LinkedList<String>)lines).descendingIterator();
		while (it.hasNext()) {
			ct.writer.drawString(0,yy--,it.next());
			if (yy < 0) break;
		}
		
		TerminalSize size = ct.screen.getTerminalSize();
		ct.screen.setCursorPosition(size.getColumns()-1,size.getRows()-1);
	}
}
