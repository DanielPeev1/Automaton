
public class Arrow {
	
	private int from;
	private int to;
	private String val;
	
	public Arrow (int f, int t, String v) {
		from = f;
		to = t;
		val = v;
	}
	public int getFrom () {
		return from;
	}
	public int getTo () {
		return to;
	}
	public String getVal () {
		return val;
	}
	public String toString () {
		
		return String.format("%s %s%s", from, to, val);
	}
	
}
