package littlehelper.connection;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * Cmd fromat:
 * 
 * 00 00 000000000 \r(code name) (command code) (data) - 16
 * 
 * @author Andrey Dyachkov
 */
class CommandReciever implements Runnable {

	private static Logger log = Logger.getLogger(CommandReciever.class);

	private final byte DATA_SIZE = 9;
	private final InputStream input;

	private BlockingQueue<String> responsesQueue = new ArrayBlockingQueue<String>(10);

	private boolean isStarted;

	CommandReciever(InputStream input) {

		this.input = input;
	}

	@Override
	public void run() {
		String responseCommand;
		isStarted = true;

		int ch;
		// char[] response = new char[DATA_SIZE];

		byte[] response = new byte[DATA_SIZE];

		while (true) {
			try {
				// ch = 0;
				// response = new char[DATA_SIZE];
				//
				// for (int i = 0; i < DATA_SIZE; i++) {
				// if ((ch = input.read()) != -1) {
				// response[i] = (char) ch;
				// }
				// }
				response = new byte[DATA_SIZE];
				if (input.available() > 8) {
					ch = input.read(response);

					if (ch != -1) {
						responseCommand = new String(response).trim();

						if (responseCommand.isEmpty()) {
							throw new InterruptedException();
						}

						responsesQueue.put(responseCommand);

						log.debug("Controller responses: " + responseCommand);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				log.debug("Empty response was got from controller");
			}
		}
	}

	String getNextAnswer() throws InterruptedException {
		return responsesQueue.poll(50, TimeUnit.MILLISECONDS);
	}

	public boolean isStarted() {
		return isStarted;
	}
}