package pl.shockah.shocky.console.views;

import java.io.File;
import pl.shockah.Config;
import pl.shockah.shocky.console.ConsoleThread;
import pl.shockah.shocky.console.ITextInputFilter;
import pl.shockah.shocky.console.ITextInputListener;
import pl.shockah.shocky.console.TextInputHandler;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.screen.ScreenWriter;
import com.googlecode.lanterna.terminal.TerminalSize;

public class ViewConfigWizard extends View {
	public Stage
		stageHost = new Stage(){
			public void onEnable() {
				tih = new TextInputHandler(this,null);
				tih.append("localhost");
			}
			public void update(ConsoleThread thread, Screen screen, ScreenWriter sw) {
				sw.drawString(0,2,"MongoDB hostname:");
				sw.drawString(0,3,"(leave blank for localhost)");
			}
			public void onTextAccept(String text) {
				cfg.set("mongo->host",text);
				advance(stageAuth);
			}
		},
		stageAuth = new Stage(){
			public void onEnable() {
				tih = new TextInputHandler(this,new ITextInputFilter.YesNo());
			}
			public void update(ConsoleThread thread, Screen screen, ScreenWriter sw) {
				sw.drawString(0,2,"Is authentication required? (Y/N) :");
			}
			public void onTextAccept(String text) {
				if (text.isEmpty()) return;
				advance(text.equalsIgnoreCase("y") ? stageAuthUser : null);
			}
		},
		stageAuthUser = new Stage(){
			public void onEnable() {
				tih = new TextInputHandler(this,null);
			}
			public void update(ConsoleThread thread, Screen screen, ScreenWriter sw) {
				sw.drawString(0,2,"MongoDB username:");
			}
			public void onTextAccept(String text) {
				cfg.set("mongo->user",text);
				advance(stageAuthPass);
			}
		},
		stageAuthPass = new Stage(){
			public void onEnable() {
				tih = new TextInputHandler(this,null);
			}
			public void update(ConsoleThread thread, Screen screen, ScreenWriter sw) {
				sw.drawString(0,2,"MongoDB password:");
			}
			public void onTextAccept(String text) {
				cfg.set("mongo->pass",text);
				advance(null);
			}
			public String modifyInputDisplay(String input) {
				int len = input.length();
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < len; i++) sb.append('*');
				return sb.toString();
			}
		};
	
	abstract class Stage implements ITextInputListener {
		public abstract void onEnable();
		public abstract void update(ConsoleThread thread, Screen screen, ScreenWriter sw);
		public abstract void onTextAccept(String s);
		
		public void advance(Stage stageNew) {
			stage = stageNew;
			if (stage == null) tih = new TextInputHandler(); else stage.onEnable();
		}
		public String modifyInputDisplay(String input) {
			return input;
		}
	}
	
	protected TextInputHandler tih;
	protected Config cfg;
	protected Stage stage = stageHost;
	
	public ViewConfigWizard() {
		cfg = new Config();
		try {
			File f = new File("config.cfg");
			if (f.exists()) cfg.load(f);
		} catch (Exception e) {}
		
		stage.onEnable();
	}
	
	public void update(ConsoleThread thread, Screen screen, ScreenWriter sw) {
		TerminalSize ts = screen.getTerminalSize();
		String input = tih.toString();
		if (stage != null) input = stage.modifyInputDisplay(input);
		
		Key key;
		while ((key = screen.readInput()) != null) tih.handle(key);
		
		sw.drawString(0,0,"--> Shocky, the IRC bot <--",ScreenCharacterStyle.Bold);
		if (stage != null) stage.update(thread,screen,sw);
		
		for (int x = 0; x < ts.getColumns(); x++) sw.drawString(x,ts.getRows()-2,"=");
		sw.drawString(0,ts.getRows()-1,input);
		screen.setCursorPosition(tih.getPosition(),ts.getRows()-1);
	}
}