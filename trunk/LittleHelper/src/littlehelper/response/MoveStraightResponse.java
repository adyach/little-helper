package littlehelper.response;

import littlehelper.mapbuilder.Location;

public class MoveStraightResponse extends AbstractResponse {

	private final int responseId;
	private final Location location;
	private final boolean isMoveDone;

	private MoveStraightResponse(int responseId, Location location, boolean isMoveDone) {
		this.responseId = responseId;
		this.location = location;
		this.isMoveDone = isMoveDone;
	}

	public static AbstractResponse valueOf(String[] params) {

		final int id = Integer.parseInt(params[0]);
		final String x = params[1];
		final String y = params[2];
		final boolean isMoveDone = Boolean.valueOf(params[3]);

		return new MoveStraightResponse(id, new Location(x, y), isMoveDone);
	}

	public int getResponseId() {
		return responseId;
	}

	public Location getLocation() {
		return location;
	}

	public boolean isMoveDone() {
		return isMoveDone;
	}
}
