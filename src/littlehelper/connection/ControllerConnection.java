package littlehelper.connection;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.Enumeration;

import org.apache.log4j.Logger;

public class ControllerConnection implements IConnection {
	private static Logger log = Logger.getLogger(ControllerConnection.class);

	private static IConnection instance;
	private static final String PORT_NAMES[] = { "COM14" };

	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;

	private SerialPort serialPort;
	// private OutputStream output;
	// private InputStream input;
	// private ByteArrayOutputStream bout;
	// private OutputStreamWriter writer;
	// private ByteArrayInputStream bin;
	// private InputStreamReader reader;
	// private byte[] buffer = new byte[8];
	private CommandReciever commandReciever;
	private CommandSender commandSender;

	private ControllerConnection() {
		// bout = new ByteArrayOutputStream();
		// writer = new OutputStreamWriter(bout);
		// reader = new InputStreamReader(input);
		// bin = new ByteArrayInputStream(buffer);
		// reader = new InputStreamReader(bin);

		open();
	}

	public synchronized static IConnection getInstance() {
		if (instance == null) {
			instance = new ControllerConnection();
		}

		return instance;
	}

	@Override
	public void sendCommand(String command) throws IOException {
		commandSender.addCommand(command);
		new Thread(commandSender).start();
	}

	@Override
	public String retrieveCommand() throws IOException {
		try {

			Thread reciever = new Thread(commandReciever);
			reciever.start();
			reciever.join();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return commandReciever.getFirstAnswer();
	}

	private void open() {
		CommPortIdentifier portId = findPortId();
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			// output = serialPort.getOutputStream();
			commandSender = new CommandSender(serialPort.getOutputStream());
			commandReciever = new CommandReciever(serialPort.getInputStream());
			serialPort.notifyOnDataAvailable(true);
		} catch (PortInUseException | UnsupportedCommOperationException | IOException e) {
			e.printStackTrace();
		}
	}

	private synchronized void close() {
		if (serialPort == null) {
			return;
		}
		serialPort.close();
	}

	private CommPortIdentifier findPortId() {
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		return portId;
	}
}
