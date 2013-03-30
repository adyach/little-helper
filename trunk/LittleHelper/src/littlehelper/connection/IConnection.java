package littlehelper.connection;

import java.io.IOException;

/**
 * @author Andrey Dyachkov
 */
public interface IConnection {
	void sendCommand(String command) throws IOException;

	String retrieveCommand() throws IOException;
}
