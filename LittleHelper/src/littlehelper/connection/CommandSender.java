package littlehelper.connection;

import java.io.IOException;
import java.io.OutputStream;

class CommandSender implements Runnable {

	private String command;
	private final OutputStream output;

	CommandSender(OutputStream output) {
		this.output = output;
	}

	@Override
	public void run() {
		while (true) {
			try {
				output.write(command.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
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
	}

	void addCommand(String command) {
		this.command = command;
	}
}
