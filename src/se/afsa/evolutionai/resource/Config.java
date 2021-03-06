package se.afsa.evolutionai.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Mattias J�nsson
 * Handler for the config files.
 *
 */
public class Config {
	
	private final static String propertiesLocation = "se/afsa/evolutionai/resource/data/config.properties";
	private static Properties properties = new Properties();
	private static boolean active = false;
	
	public Config() {
		if(!active) {
			active = true;
			try {
				InputStream is = getClass().getClassLoader().getResourceAsStream(propertiesLocation);
				properties.load(is);
				is.close();				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// Throw error event!!
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Get a value form the config.
	 * @param key - the key.
	 * @param defaultValue - a default value.
	 * @return The value if it exists, else the default value.
	 */
	public int getInt(String key, String defaultValue) {
		return Integer.parseInt(properties.getProperty(key, defaultValue));
	}
}
