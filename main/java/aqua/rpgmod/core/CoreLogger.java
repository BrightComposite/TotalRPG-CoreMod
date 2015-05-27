package aqua.rpgmod.core;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CoreLogger
{
	private static Logger logger = LogManager.getLogger("Total RPG Core Mod");
	
	public static void log(Level logLevel, String message) {
		logger.log(logLevel, message);
	}
}
