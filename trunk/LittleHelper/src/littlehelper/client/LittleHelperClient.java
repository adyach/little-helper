package littlehelper.client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import littlehelper.connection.ConnectionController;
import littlehelper.connection.IConnection;

/**
 * @author Andrey Dyachkov
 */
public class LittleHelperClient implements Runnable, ActionListener {
	private static IConnection conn;
	JTextField textField = new JTextField("01");
	JTextArea textArea = new JTextArea("incoming command");

	public static void main(String[] args) throws Exception {
		// SerialSwitch main = new SerialSwitch();
		// main.initialize();
		conn = ConnectionController.getInstance();
		SwingUtilities.invokeLater(new LittleHelperClient());

	}

	private void createAndShowGui() {
		JFrame frame = createFrame();
		frame.setVisible(true);
	}

	private JFrame createFrame() {
		JFrame frame = new JFrame("LittleHelperClient");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(createPane());
		frame.pack();
		return frame;
	}

	private JPanel createPane() {
		JPanel pane = new JPanel();
		pane.add(createButton());
		pane.add(textField);
		textArea.setEditable(false);
		pane.add(textArea);

		return pane;
	}

	private JButton createButton() {
		JButton button = new JButton("Send");
		button.addActionListener(this);
		return button;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JButton button = (JButton) event.getSource();
		onButton(button);
		try {
			textArea.setText(conn.retrieveCommand());
			textArea.setForeground(Color.BLACK);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("CallToThreadDumpStack")
	private void onButton(JButton button) {
		try {
			conn.sendCommand(textField.getText());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		createAndShowGui();
	}
}
