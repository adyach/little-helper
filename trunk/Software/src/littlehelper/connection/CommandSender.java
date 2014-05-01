package littlehelper.connection;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import org.apache.log4j.Logger;

/**
 * Cmd fromat:
 * HEADER
 * 	- 2 Byte
 * DATA
 * 	- 4 Byte
 * 
 * @author Andrey Dyachkov
 */

class CommandSender implements Runnable {

	private static Logger log = Logger.getLogger(CommandSender.class);
	private static final int CMD_LENGTH = 6;
	private byte[] command;
	private final OutputStream output;
//	private ByteArrayOutputStream bout;
//	private OutputStreamWriter writer;

	CommandSender(OutputStream output) {
		this.output = output;
//		bout = new ByteArrayOutputStream();
//		writer = new OutputStreamWriter(bout);
		command = new byte[CMD_LENGTH];
	}

	@Override
	public void run() {
		log.debug("Server sends cmd: " + Arrays.toString(command));
//		LHUtils.printCommand(command);

		try {
			 output.write(command);
			 output.flush();
			 // FIXME Hardware Controller bug!
			 output.write(command);
			 output.flush();
//			writer.write(command);
//			writer.flush();
//			bout.writeTo(output);
//			bout.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void setCommand(byte[] command) {
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
