package littlehelper.command;

import littlehelper.mapbuilder.Location;

/**
 * @author Andrey Dyachkov
 */
public class MoveStraightCommand extends AbstractCommand {

	private final int commandId;
	private final static String MOVE_STRAIGHT = "00001";
	private final Location endLocation;

	private MoveStraightCommand(int commandId, Location location) {
		this.commandId = commandId;
		this.endLocation = location;
	}

	public static String getCommand() {

		return MOVE_STRAIGHT;
	}

	public AbstractCommand valueOf(String[] params) {

		try {
			final int id = Integer.parseInt(params[0]);
			final String x = params[1];
			final String y = params[2];

			return new MoveStraightCommand(id, new Location(x, y));
		} catch (NumberFormatException e) {
			log.error("The MoveStraightCommand was not created");
		}

		return new ErrorCommand();
	}

	public int getCommandId() {
		return commandId;
	}

}
