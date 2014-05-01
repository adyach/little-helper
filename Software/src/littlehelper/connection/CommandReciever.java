package littlehelper.connection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * Cmd fromat:
 * 
 * Header
 * 	- 2 Byte
 * Body
 * 	- 4 Byte
 * 
 * @author Andrey Dyachkov
 */
class CommandReciever implements Runnable {

	private static Logger log = Logger.getLogger(CommandReciever.class);

	private static final int CMD_LENGTH = 6;
	private final InputStream input;

	private BlockingQueue<byte[]> responsesQueue = new ArrayBlockingQueue<byte[]>(50);

	private boolean isStarted;

	CommandReciever(InputStream input) {
		this.input = input;
	}

	@Override
	public void run() {
		isStarted = true;
		int ch;
		byte[] command;
		while (true) {
			try {
				if (input.available() >= CMD_LENGTH) {
					command = new byte[CMD_LENGTH];
					ch = input.read(command);
					if (ch != -1) {
						responsesQueue.put(command);
						log.debug("Robot responses: " + Arrays.toString(command));
					}
				}
			} catch (InterruptedException e) {
				log.debug("Empty response was got from controller");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	byte[] getNextAnswer() throws InterruptedException {
		final byte[] command = responsesQueue.poll(100, TimeUnit.MILLISECONDS);
		if (command == null) {
			return ByteCommand.EMPTY;
		}
		return command;
	}

	public boolean isStarted() {
		return isStarted;
	}
}