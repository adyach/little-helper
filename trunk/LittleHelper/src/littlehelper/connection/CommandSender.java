package littlehelper.connection;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;

/**
 * @author Andrey Dyachkov
 */
class CommandSender implements Runnable {

	private static Logger log = Logger.getLogger(CommandSender.class);
	private String command;
	private final OutputStream output;

	CommandSender(OutputStream output) {
		this.output = output;
	}

	@Override
	public void run() {
		log.debug("Server sends: " + command);
		
		try {
			output.write(command.getBytes());
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void setCommand(String command) {
		this.command = command;
	}
}

// try {
// writer.write(command, 0, command.length());
// writer.flush();
// bout.writeTo(output);
// bout.reset();
// } catch (IOException e) {
// e.printStackTrace();
// }
