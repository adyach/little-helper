package littlehelper.connection;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

/**
 * @author Andrey Dyachkov
 */
public class ConnectionController implements IConnection {
	private static Logger log = Logger.getLogger(ConnectionController.class);

	private static IConnection instance;
	private static final String PORT_NAMES[] = { "COM10" };

	private static final int TIME_OUT = 5000;
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
	private ExecutorService executor = Executors.newFixedThreadPool(5);

	private ConnectionController() {
		// bout = new ByteArrayOutputStream();
		// writer = new OutputStreamWriter(bout);
		// reader = new InputStreamReader(input);
		// bin = new ByteArrayInputStream(buffer);
		// reader = new InputStreamReader(bin);

		open();
	}

	public synchronized static IConnection getInstance() {
		if (instance == null) {
			instance = new ConnectionController();
		}

		return instance;
	}

	@Override
	public void sendCommand(String command) throws IOException {
		// try {
		commandSender.setCommand(command + '\n');
		executor.submit(commandSender);
		// executor.awaitTermination(500, TimeUnit.MILLISECONDS);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// commandSender.addCommand(command);
		// new Thread(commandSender).start();
	}

	@Override
	public String retrieveCommand() throws IOException {
		try {
			if (!commandReciever.isStarted()) {
				executor.submit(commandReciever);
			}

			// executor.awaitTermination(500, TimeUnit.MILLISECONDS);
			// Thread reciever = new Thread(commandReciever);
			// reciever.start();
			// reciever.join();
			return commandReciever.getNextAnswer();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return "END";
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
