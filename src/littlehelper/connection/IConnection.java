package littlehelper.connection;

import java.io.IOException;

public interface IConnection {
	void sendCommand(String command) throws IOException;
	
	String retrieveCommand() throws IOException;
}
