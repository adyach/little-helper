package littlehelper.connection;

/**
 * Commands definitions.
 * 
 * @author Andrey Dyachkov
 */

public class ByteCommand {

	public static final byte[] STRAIGHT = 		new byte[] { 0x01, 0, 0, 0, 0, 0 };
	public static final byte[] RIGHT = 			new byte[] { 0x02, 0, 0, 0, 0, 0 };
	public static final byte[] LEFT = 			new byte[] { 0x03, 0, 0, 0, 0, 0 };
	public static final byte[] STOP = 			new byte[] { 0x04, 0, 0, 0, 0, 0 };

//	public static final byte[] SLOW_STRAIGHT = 	new byte[] { 0, 4, 0, 0, 0, 0, 0, 0, 0, 0 };
//	public static final byte[] SLOW_LEFT = 		new byte[] { 0, 5, 0, 0, 0, 0, 0, 0, 0, 0 };
//	public static final byte[] SLOW_RIGHT = 	new byte[] { 0, 6, 0, 0, 0, 0, 0, 0, 0, 0 };
//
//	public static final byte[] INC_SPEED = 		new byte[] { 0, 7, 0, 0, 0, 0, 0, 0, 0, 0 };
//	public static final byte[] DEC_SPEED = 		new byte[] { 0, 8, 0, 0, 0, 0, 0, 0, 0, 0 };
//
	public static final byte[] EMPTY = 			new byte[0];

}
