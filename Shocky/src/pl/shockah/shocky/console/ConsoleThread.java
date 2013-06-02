package pl.shockah.shocky.console;

import java.io.PrintStream;
import java.util.Collections;
import java.util.LinkedList;
import pl.shockah.SelectionList;
import pl.shockah.shocky.StoppableThread;
import pl.shockah.shocky.console.tabs.ConsoleTab;
import pl.shockah.shocky.console.tabs.ConsoleTabOutput;
import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenWriter;
import com.googlecode.lanterna.terminal.Terminal.Color;

public class ConsoleThread extends StoppableThread {
	public static PrintStream
		psSystemOut = System.out,
		psSystemErr = System.err;
	
	public final Screen screen = TerminalFacade.createScreen();
	public final ScreenWriter writer = new ScreenWriter(screen);
	public SelectionList<ConsoleTab> tabs = new SelectionList<ConsoleTab>(Collections.synchronizedList(new LinkedList<ConsoleTab>()));
	
	public ConsoleThread() {
		ConsoleTabOutput tab;
		
		tabs.add(tab = new ConsoleTabOutput(this,"Output"));
		System.setOut(new ConsolePrintStreamWrapper(this,System.out,tab));
		
		tabs.add(tab = new ConsoleTabOutput(this,"Errors"));
		System.setErr(new ConsolePrintStreamWrapper(this,System.err,tab));
	}
	
	public void onRun() {
		if (screen == null || writer == null) return;
		setPriority((Thread.NORM_PRIORITY+Thread.MIN_PRIORITY)/2);
		screen.startScreen();
		while (running) {
			writer.fillScreen(' ');
			
			ConsoleTab selected = tabs.getSelected();
			int selectedX = -1;
			
			int x = 0;
			for (ConsoleTab tab : tabs) {
				if (tab == selected) selectedX = x;
				x += drawTab(tab,x);
			}
			
			if (selectedX != -1) {
				writer.setBackgroundColor(Color.WHITE);
				writer.setForegroundColor(Color.BLACK);
				drawTab(selected,selectedX);
				writer.setBackgroundColor(Color.DEFAULT);
				writer.setForegroundColor(Color.DEFAULT);
			}
			
			selected.update();
			if (screen.resizePending()) screen.completeRefresh(); else screen.refresh();
			
			try {
				Thread.sleep(10);
			} catch (Exception e) {}
		}
		screen.stopScreen();
	}
	
	private int drawTab(ConsoleTab tab, int x) {
		String title = tab.getTitle();
		writer.drawString(x,0,"# "+title+" #");
		for (int i = 0; i < title.length()+4; i++) writer.drawString(x+i,1,"#");
		return title.length()+3;
	}
}