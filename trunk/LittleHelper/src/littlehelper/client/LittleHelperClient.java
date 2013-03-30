package littlehelper.client;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import littlehelper.connection.ConnectionController;
import littlehelper.connection.IConnection;

import org.apache.log4j.Logger;

/**
 * @author Andrey Dyachkov
 */
public class LittleHelperClient implements Runnable, ActionListener {
	private static Logger log = Logger.getLogger("LittleHelperClient");
	private static IConnection conn;
	private JTextField textField = new JTextField("00001");
	private JTextArea textArea = new JTextArea("incoming command");
	private static MouseCommander mouseListener;

	public static void main(String[] args) throws Exception {
		// SerialSwitch main = new SerialSwitch();
		// main.initialize();
		mouseListener = new LittleHelperClient().new MouseCommander();
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
		frame.addMouseMotionListener(mouseListener);

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

	private class MouseCommander implements MouseMotionListener {

		private Point oldPoint = new Point(0, 0);
		private String command;

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseMoved(MouseEvent e) {
			Point newPoint = e.getPoint();

			if (!newPoint.equals(oldPoint)) {
				log.debug("newPoint: " + newPoint);
				log.debug("oldPoint: " + oldPoint);
				if (newPoint.getX() > oldPoint.getX()) {
					command = "00010";
				} else if (newPoint.getX() < oldPoint.getX()) {
					command = "00011";
				}

				if (newPoint.getY() > oldPoint.getY()) {
					command = "00001";
				} else if (newPoint.getY() < oldPoint.getY()) {
					command = "00000";
				}

				try {
					conn.sendCommand(mouseListener.command);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				oldPoint = newPoint;
			}

		}
	}

}
