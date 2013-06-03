package pl.shockah.shocky.console;

public interface ITextInputFilter {
	public static class NumberInt implements ITextInputFilter {
		public boolean acceptText(String s) {
			if (s.isEmpty()) return true;
			try {
				Integer.parseInt(s);
				return true;
			} catch (Exception e) {return false;}
		}
	}
	
	public static class NumberDouble implements ITextInputFilter {
		public boolean acceptText(String s) {
			if (s.isEmpty()) return true;
			try {
				Double.parseDouble(s);
				return true;
			} catch (Exception e) {return false;}
		}
	}
	
	public static class YesNo implements ITextInputFilter {
		public boolean acceptText(String s) {
			if (s.isEmpty()) return true;
			s = s.toLowerCase();
			return s.equals("y") || s.equals("n");
		}
	}
	
	public boolean acceptText(String s);
}