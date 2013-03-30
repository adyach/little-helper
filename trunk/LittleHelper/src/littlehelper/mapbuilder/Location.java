package littlehelper.mapbuilder;

public class Location {

	private final double x;
	private final double y;

	public Location(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Location(String x, String y) {
		this.x = Double.parseDouble(x);
		this.y = Double.parseDouble(y);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}
