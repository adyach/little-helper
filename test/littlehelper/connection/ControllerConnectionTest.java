package littlehelper.connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ControllerConnectionTest {
	IConnection conn = null;

	@Test
	public void testConnection() throws Exception {

		conn = ControllerConnection.getInstance();

		assertNotNull(conn);

		conn.sendCommand("00000011");
		conn.sendCommand("00000001");
		conn.sendCommand("00000001");

		assertEquals("00000001", conn.retrieveCommand());
	}
}
