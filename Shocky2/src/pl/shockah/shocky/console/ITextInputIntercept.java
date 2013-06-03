package pl.shockah.shocky.console;

import com.googlecode.lanterna.input.Key;

public interface ITextInputIntercept {
	public boolean interceptTextInput(Key key);
}