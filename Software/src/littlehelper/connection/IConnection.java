package littlehelper.connection;

import java.io.IOException;

/**
 * @author Andrey Dyachkov
 */
public interface IConnection {
	void sendCommand(byte[] command) throws IOException;

	byte[] retrieveCommand() throws IOException;

	void close();
}
