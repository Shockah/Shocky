package pl.shockah.shocky.console;

import java.util.LinkedList;
import java.util.List;

public class TextInputWaitingListener implements ITextInputListener {
	protected List<String> accepted = new LinkedList<String>();
	
	public void onTextAccept(String text) {
		accepted.add(text);
	}
	
	public String get() {
		while (accepted.isEmpty()) {
			try {
				Thread.sleep(10);
			} catch (Exception e) {e.printStackTrace();}
		}
		return accepted.remove(0);
	}
}