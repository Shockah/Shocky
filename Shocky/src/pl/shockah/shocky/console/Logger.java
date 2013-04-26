package pl.shockah.shocky.console;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Logger {
	//region static
	public static Logger
		out = new Logger(),
		err = new Logger();
	//endregion
	
	//region object
	private final List<String> buffer;
	private final List<Integer> bufferColor;
	private final int bufferLines;
	private final SimpleDateFormat sdf;
	private StringBuilder sb = new StringBuilder();
	protected int color;
	
	public Logger() {this(new SimpleDateFormat("HH:mm:ss"));}
	public Logger(int bufferLines) {this(bufferLines,new SimpleDateFormat("HH:mm:ss"));}
	public Logger(SimpleDateFormat sdf) {this(200,sdf);}
	public Logger(int bufferLines, SimpleDateFormat sdf) {
		this.sdf = sdf;
		this.bufferLines = bufferLines;
		buffer = Collections.synchronizedList(new ArrayList<String>(200));
		bufferColor = Collections.synchronizedList(new ArrayList<Integer>(200));
	}
	
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	
	public void print(Object o) {
		print(o,true);
	}
	protected void print(Object o, boolean split) {
		if (split) {
			String[] spl = o.toString().split("\\r?\\n");
			for (int i = 0; i < spl.length; i++) {
				if (i != 0) printLine();
				print(o,false);
			}
		} else {
			if (sb.length() == 0) sb.append(sdf.format(new Date())+" | ");
			sb.append(o);
		}
	}
	
	public void printLine() {
		if (buffer.size() == bufferLines) {
			buffer.remove(0);
			bufferColor.remove(0);
		}
		buffer.add(sb.toString());
		bufferColor.add(color);
		sb = new StringBuilder();
	}
	public void printLine(Object o) {
		print(o);
		printLine();
	}
	//endregion
}