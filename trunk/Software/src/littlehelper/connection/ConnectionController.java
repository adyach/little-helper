package littlehelper.connection;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * @author Andrey Dyachkov
 */
public class ConnectionController implements IConnection {
	private static Logger log = Logger.getLogger(ConnectionController.class);

	private static IConnection instance;
	private static final String PORT = "/dev/rfcomm0";

	private static final int TIME_OUT = 5000;
	private static final int DATA_RATE = 9600;

	private SerialPort serialPort;
	private CommandReciever commandReciever;
	private CommandSender commandSender;
	private ExecutorService executor = Executors.newFixedThreadPool(5);

	private ConnectionController() {
		
		open();
	}

	public synchronized static IConnection getInstance() {
		if (instance == null) {
			instance = new ConnectionController();
		}

		return instance;
	}

	@Override
	public void sendCommand(byte[] command) throws IOException {
		// try {
		commandSender.setCommand(command);
		executor.submit(commandSender);
		// executor.awaitTermination(500, TimeUnit.MILLISECONDS);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// commandSender.addCommand(command);
		// new Thread(commandSender).start();
	}

	@Override
	public byte[] retrieveCommand() throws IOException {
		try {
			// if (!commandReciever.isStarted()) {
			executor.submit(commandReciever);
			// }

			// executor.awaitTermination(500, TimeUnit.MILLISECONDS);
			// Thread reciever = new Thread(commandReciever);
			// reciever.start();
			// reciever.join();
			return commandReciever.getNextAnswer();

		} catch (InterruptedException e) {
			throw new IOException(e);
		}
	}

	private void open() {
		// CommPortIdentifier portId = findPortId();
		// if (portId == null) {
		// System.out.println("Could not find COM port.");
		// return;
		// }

		CommPortIdentifier portId;
		try {
			portId = CommPortIdentifier.getPortIdentifier(PORT);
		} catch (NoSuchPortException e1) {
			e1.printStackTrace();
			return;
		}

		try {
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			// output = serialPort.getOutputStream();
			commandSender = new CommandSender(serialPort.getOutputStream());
			commandReciever = new CommandReciever(serialPort.getInputStream());
			serialPort.notifyOnDataAvailable(true);
			log.debug("--- Connection wtih LH was established---");
		} catch (PortInUseException | UnsupportedCommOperationException
				| IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		if (serialPort == null) {
			return;
		}
		executor.shutdown();
		try {
			executor.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		serialPort.close();

		log.debug(" --- Conenction closed --- ");
	}

//	private CommPortIdentifier findPortId() {
//		log.debug("findPortId()");
//
//		CommPortIdentifier portId = null;
//		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
//		while (portEnum.hasMoreElements()) {
//			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum
//					.nextElement();
//			log.debug("currPortId: " + currPortId.getName());
//			for (String portName : PORT_NAMES) {
//				if (currPortId.getName().equals(portName)) {
//					portId = currPortId;
//					break;
//				}
//			}
//		}
//		return portId;
//	}
}
