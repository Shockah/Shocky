package pl.shockah.shocky.console;

import java.io.PrintStream;
import java.util.Locale;
import pl.shockah.ZeroOutputStream;
import pl.shockah.shocky.console.tabs.ConsoleTabOutput;

public class ConsolePrintStream extends PrintStream {
	protected final ConsoleThread ct;
	protected final PrintStream ps;
	protected final ConsoleTabOutput tab;
	
	public ConsolePrintStream(ConsoleThread ct, ConsoleTabOutput tab) {
		super(new ZeroOutputStream());
		this.ct = ct;
		ps = null;
		this.tab = tab;
	}
	public ConsolePrintStream(ConsoleThread ct, PrintStream ps, ConsoleTabOutput tab) {
		super(ps);
		this.ct = ct;
		this.ps = ps;
		this.tab = tab;
	}
	
	public int charsLeft() {
		return ct.screen.getTerminalSize().getColumns()-tab.sb.length();
	}
	private void checkNextLine() {
		if (tab.sb.length() == ct.screen.getTerminalSize().getColumns()) nextLine();
	}
	private void nextLine() {
		String s = tab.sb.toString();
		tab.lines.add(s);
		tab.sb = new StringBuffer();
	}
	
	public PrintStream append(char c) {
		if (ps != null) ps.append(c);
		checkNextLine();
		tab.sb.append(c);
		return this;
	}
	public PrintStream append(CharSequence csq) {
		for (int i = 0; i < csq.length(); i++) append(csq.charAt(i));
		return this;
	}
	public PrintStream append(CharSequence csq, int start, int end) {ps.append(csq,start,end); return append(csq.subSequence(start,end));}
	
	public boolean checkError() {if (ps != null) return ps.checkError(); return false;}
	public void close() {if (ps != null) ps.close();}
	public void flush() {if (ps != null) ps.flush();}
	
	public PrintStream format(Locale l, String format, Object... args) {
		if (ps != null) ps.format(l,format,args);
		append(String.format(l,format,args));
		return this;
	}
	public PrintStream format(String format, Object... args) {
		if (ps != null) ps.format(format,args);
		append(String.format(format,args));
		return this;
	}
	
	public void print(boolean b) {append(String.valueOf(b));}
	public void print(char c) {append(c);}
	public void print(char[] s) {append(new String(s));}
	public void print(double d) {append(String.valueOf(d));}
	public void print(float f) {append(String.valueOf(f));}
	public void print(int i) {append(String.valueOf(i));}
	public void print(long l) {append(String.valueOf(l));}
	public void print(Object obj) {append(String.valueOf(obj));}
	public void print(String s) {append(s == null ? "null" : s);}
	
	public PrintStream printf(Locale l, String format, Object... args) {return format(l,format,args);}
	public PrintStream printf(String format, Object... args) {return format(format,args);}
	
	public void println() {if (ps != null) ps.println(); nextLine();}
	public void println(boolean x) {print(x); println();}
	public void println(char x) {print(x); println();}
	public void println(char[] x) {print(x); println();}
	public void println(double x) {print(x); println();}
	public void println(float x) {print(x); println();}
	public void println(int x) {print(x); println();}
	public void println(long x) {print(x); println();}
	public void println(Object x) {print(x); println();}
	public void println(String x) {print(x); println();}
	
	public void write(byte[] buf, int off, int len) {ps.write(buf,off,len); append(new String(buf).subSequence(off,off+len));}
	public void write(int b) {ps.write(b); append((char)b);}
}