package littlehelper.client;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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
public class LittleHelperClient implements Runnable { // , ActionListener {
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
		frame.setSize(500, 500);
		frame.setContentPane(createPane());
		frame.addMouseMotionListener(mouseListener);
		frame.addMouseWheelListener(mouseListener);
		frame.addMouseListener(mouseListener);

		return frame;
	}

	private JPanel createPane() {
		JPanel pane = new JPanel();
		pane.setSize(500, 500);

		JButton send = new JButton("Send");
		send.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					log.debug("textField.getText(): " + textField.getText());
					conn.sendCommand(textField.getText());
					textArea.setText(conn.retrieveCommand());
					textArea.setForeground(Color.BLACK);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});
		pane.add(send);
		JButton exit = new JButton("Exit");
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				conn.close();
				System.exit(0);
			}
		});
		pane.add(exit);
		pane.add(textField);
		textArea.setEditable(false);
		pane.add(textArea);

		return pane;
	}

	// private JButton createButton() {
	// JButton button = new JButton("Send");
	// button.addActionListener(this);
	// return button;
	// }

	// @Override
	// public void actionPerformed(ActionEvent event) {
	// JButton button = (JButton) event.getSource();
	//
	// if (button.getName().equals("Send")) {
	// onButton(button);
	// try {
	// textArea.setText(conn.retrieveCommand());
	// textArea.setForeground(Color.BLACK);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// if (button.getName().equals("Exit")) {
	//
	// }
	//
	// }

	// @SuppressWarnings("CallToThreadDumpStack")
	// private void onButton(JButton button) {
	// try {
	// log.debug("textField.getText(): " + textField.getText());
	// conn.sendCommand(textField.getText());
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	@Override
	public void run() {
		createAndShowGui();
	}

	private class MouseCommander implements MouseMotionListener, MouseWheelListener, MouseListener {

		private Point oldPoint = new Point(0, 0);
		private String command;

		@Override
		public void mouseDragged(MouseEvent e) {
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			Point newPoint = e.getPoint();

			if (!newPoint.equals(oldPoint)) {
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

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (e.getWheelRotation() > 0) {
				try {
					conn.sendCommand("10001");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				try {
					conn.sendCommand("00000");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		}

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {
			try {

				if (e.getButton() == MouseEvent.BUTTON1) {
					conn.sendCommand("10011");
				} else {
					conn.sendCommand("10010");
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			try {
				conn.sendCommand("00000");
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	}

}
