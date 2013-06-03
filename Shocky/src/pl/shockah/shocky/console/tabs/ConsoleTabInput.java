package pl.shockah.shocky.console.tabs;

import java.util.Iterator;
import java.util.LinkedList;
import pl.shockah.shocky.console.ConsoleThread;
import com.googlecode.lanterna.terminal.TerminalSize;

public class ConsoleTabInput extends ConsoleTabOutput {
	public ConsoleTabInput(ConsoleThread ct, String title) {
		super(ct,title);
	}

	public void update() {
		TerminalSize size = ct.screen.getTerminalSize();
		int yy = size.getRows()-3;
		if (sb.length() > 0) ct.writer.drawString(0,yy--,sb.toString());
		
		Iterator<String> it = ((LinkedList<String>)lines).descendingIterator();
		while (it.hasNext()) {
			ct.writer.drawString(0,yy--,it.next());
			if (yy < 0) break;
		}
		
		for (int i = 0; i < size.getColumns(); i++) ct.writer.drawString(i,size.getRows()-2,"=");
		ct.writer.drawString(0,size.getRows()-1,ct.tit.tih.toString());
		ct.screen.setCursorPosition(ct.tit.tih.getPosition(),size.getRows()-1);
	}
}