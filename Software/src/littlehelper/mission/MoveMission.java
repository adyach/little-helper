package littlehelper.mission;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import littlehelper.command.AbstractCommand;
import littlehelper.mapbuilder.Location;

/**
 * 
 * Keeps the sequence of commands to make to get destination point.
 * 
 * @author Andrey Dyachkov
 */
public class MoveMission extends Mission {

	private final Location endLocation;
	private boolean isCompleted;
	private final List<AbstractCommand> commandsToExecute = new LinkedList<AbstractCommand>();
	Stack a;

	private MoveMission(int missionId, Location endLocation) {
		super(missionId);
		this.endLocation = endLocation;
	}

	String[] strings = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };

	public Mission createMission(String[] params) throws Exception {

		final int id = Integer.parseInt(params[0]);
		final String x = params[1];
		final String y = params[2];

		return new MoveMission(id, new Location(x, y));
	}

}
