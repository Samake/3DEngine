package samake.engine.logging;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Console {
	
	private static SimpleDateFormat sdfmt = new SimpleDateFormat("dd:MM:yyyy-hh:mm:ss:SSS");

	public enum LOGTYPE {
		OUTPUT, ERROR
	}
	
	public static void print(String line, LOGTYPE type, boolean timeStamp) {
		
		if (timeStamp) {
			line = sdfmt.format(new Date()) + " " + line;
		}
		
		switch (type) {
		case OUTPUT :
			System.out.println(line);
			break;
		case ERROR :
			System.err.println(line);
			break;
		}
	}
}
