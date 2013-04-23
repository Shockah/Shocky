package pl.shockah;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTools {
	public static final Pattern unicodePattern = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
	public static final Pattern htmlTagPattern = Pattern.compile("<([^>]+)>");
	public static final Pattern htmlEscapePattern = Pattern.compile("&(([A-Za-z]+)|#([0-9]+));");
	
	public static String getFilenameStrippedWindows(String fname) {
		fname = fname.replace('\\','-');
		fname = fname.replace('/','-');
		fname = fname.replace(": "," - ");
		fname = fname.replace(':','-');
		fname = fname.replace("*","");
		fname = fname.replace("?","");
		fname = fname.replace('"','\'');
		fname = fname.replace('<','[');
		fname = fname.replace('>',']');
		fname = fname.replace("|"," - ");
		return fname;
	}

	public static String stripHTMLTags(CharSequence s) {
		Matcher m = htmlTagPattern.matcher(s);
		StringBuffer sb = new StringBuffer();
		while (m.find()) m.appendReplacement(sb, "");
		m.appendTail(sb);
		return sb.toString();
	}
	public static String unicodeParse(CharSequence s) {
		Matcher m = unicodePattern.matcher(s);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			int hex = Integer.valueOf(m.group(1),16);
			m.appendReplacement(sb, Character.toString((char)hex));
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	public static boolean isNumber(CharSequence s) {
		boolean ret = true;
		for (int i = 0; ret && i < s.length(); i++) {
			Character c = s.charAt(i);
			if (i == 0 && c == '-') continue;
			if (!Character.isDigit(c)) ret = false;
		}
		return ret;
	}
	
	public static boolean isBoolean(String s) {
		return (s.equalsIgnoreCase("true")||s.equalsIgnoreCase("false"));
	}
	
    public static String deleteWhitespace(CharSequence str) {
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
        	char c = str.charAt(i);
            if (i > 0 && Character.isWhitespace(c) && Character.isWhitespace(str.charAt(i-1))) continue;
            if (c == '\r' || c == '\n') {
            	sb.append(' ');
            	continue;
            }
            sb.append(c);
        }
        return sb.toString();
    }
}