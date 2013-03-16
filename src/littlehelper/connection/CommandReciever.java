package littlehelper.connection;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

class CommandReciever implements Runnable {

	private static Logger log = Logger.getLogger(CommandReciever.class);

	private final byte DATA_SIZE = 8;
	private final InputStream input;
	private String responseCommand;

	CommandReciever(InputStream input) {

		this.input = input;
	}

	@Override
	public void run() {

		int ch = 0;
		char[] response = new char[8];

		try {
			for (int i = 0; i < DATA_SIZE; i++) {
				if ((ch = input.read()) != -1) {
					response[i] = (char) ch;
				}
			}

			responseCommand = new String(response).trim();

			if (responseCommand.isEmpty()) {
				responseCommand = "NOP";
			}

			log.debug("Response from controller: " + responseCommand);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	String getFirstAnswer() {
		return responseCommand;
	}
}