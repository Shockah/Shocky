package pl.shockah.shocky.console;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.input.Key.Kind;

public class TextInputHandler {
	private final ITextInputListener textInputListener;
	private final ITextInputFilter textInputFilter;
	private StringBuilder sb;
	private int pos;
	
	public TextInputHandler() {this(null,null);}
	public TextInputHandler(ITextInputListener textInputListener, ITextInputFilter textInputFilter) {
		this.textInputListener = textInputListener;
		this.textInputFilter = textInputFilter;
		clear();
	}
	
	public String toString() {
		return sb.toString();
	}
	public int getPosition() {
		return pos;
	}
	
	public void clear() {
		sb = new StringBuilder();
		pos = 0;
	}
	
	public void handle(Key key) {
		if (key.getKind() == Kind.NormalKey && key.getCharacter() == 127) {
			if (pos == sb.length()) return;
			String old = toString();
			
			sb.delete(pos,pos+1);
			if (textInputFilter != null && !textInputFilter.acceptText(toString())) sb = new StringBuilder(old);
		} else if (key.getKind() == Kind.Backspace) {
			if (pos == 0) return;
			String old = toString();
			int oldPos = pos;
			
			sb.delete(pos-1,pos);
			pos--;
			if (textInputFilter != null && !textInputFilter.acceptText(toString())) {
				sb = new StringBuilder(old);
				pos = oldPos;
			}
		} else if (key.getKind() == Kind.Home) {
			pos = 0;
		} else if (key.getKind() == Kind.End) {
			pos = sb.length();
		} else if (key.getKind() == Kind.ArrowLeft) {
			pos = Math.max(pos-1,0);;
		} else if (key.getKind() == Kind.ArrowRight) {
			pos = Math.min(pos+1,sb.length());
		} else if (key.getKind() == Kind.NormalKey && key.getCharacter() == 'v' && key.isCtrlPressed()) {
			try {
				String old = toString();
				int oldPos = pos;
				
				String data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
				sb.replace(pos,pos,data);
				pos += data.length();
				if (textInputFilter != null && !textInputFilter.acceptText(toString())) {
					sb = new StringBuilder(old);
					pos = oldPos;
				}
			} catch (Exception e) {}
		} else if (key.getKind() == Kind.NormalKey) {
			if (key.isCtrlPressed() || key.isAltPressed()) return;
			String old = toString();
			int oldPos = pos;
			
			sb.replace(pos,pos,""+key.getCharacter());
			pos++;
			if (textInputFilter != null && !textInputFilter.acceptText(toString())) {
				sb = new StringBuilder(old);
				pos = oldPos;
			}
		} else if (key.getKind() == Kind.Enter) {
			if (textInputListener != null) textInputListener.onTextAccept(toString());
		}
	}
	
	public void append(String s) {
		sb.append(s);
		pos += s.length();
	}
}