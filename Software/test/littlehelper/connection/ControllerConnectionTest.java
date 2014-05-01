package littlehelper.connection;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import littlehelper.lhutils.LHUtils;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import org.apache.log4j.Logger;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Andrey Dyachkov
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ SerialPort.class, CommPortIdentifier.class })
public class ControllerConnectionTest {
//	private static Logger log = Logger.getLogger(ControllerConnectionTest.class);

	
	IConnection conn = null;
	

	/**
	 * @throws Exception
	 */
	@Test
	public void testConnectionOneCommand() throws Exception {

		byte[] buf = new byte[]{1,1,1,1,1,1,1,1,1,1};
		final InputStream inputStream = new ByteArrayInputStream(buf);
		final OutputStream outputStream = new ByteArrayOutputStream();
		
		SerialPort mockedSerialPort = PowerMock.createMock(SerialPort.class);
		CommPortIdentifier mockedCommPortIdentifier = PowerMock.createMock(CommPortIdentifier.class);
		PowerMock.mockStatic(CommPortIdentifier.class);

		EasyMock.expect(CommPortIdentifier.getPortIdentifier("/dev/ttyACM0")).andReturn(mockedCommPortIdentifier);
		EasyMock.expect(mockedCommPortIdentifier.open("littlehelper.connection.ConnectionController", 5000)).andReturn(
				mockedSerialPort);
		mockedSerialPort
				.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		EasyMock.expectLastCall();
		EasyMock.expect(mockedSerialPort.getOutputStream()).andReturn(outputStream);
		EasyMock.expect(mockedSerialPort.getInputStream()).andReturn(inputStream);
		mockedSerialPort.notifyOnDataAvailable(true);
		EasyMock.expectLastCall();
		
		PowerMock.replayAll();

		conn = ConnectionController.getInstance();
		
		conn.sendCommand(new byte[]{0,0,0,0,0,0,0,0,0,0});
		byte[] cmd = conn.retrieveCommand();
		System.out.println(cmd);
		LHUtils.printCommand(cmd);
		
		assertNotNull(conn);

		PowerMock.verifyAll();

		//
		// conn.sendCommand(ByteCommand.STRAIGHT_COMMAND);
		//
		// assertEquals("00000001", conn.retrieveCommand());
	}
}
