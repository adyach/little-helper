package littlehelper.lhutils;

import org.apache.log4j.Logger;

public class LHUtils {
	
	private static Logger log = Logger.getLogger(LHUtils.class);

	public static void printCommand(byte[] cmd) {
		StringBuilder sb = new StringBuilder();
		for (byte b: cmd) {
				sb.append(b);
		}
		
		log.debug(sb.toString());
	}
}
