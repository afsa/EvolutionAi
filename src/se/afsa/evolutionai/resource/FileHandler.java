package se.afsa.evolutionai.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Mattias Jönsson
 * This opens and saves binary files.
 *
 */
public class FileHandler {
	
	public FileHandler() {
		
	}
	
	/**
	 * Save a binary file.
	 * @param file - filename.
	 * @param object - object to save, must implement Serializable.
	 * @return If save was successful.
	 */
	public boolean save(File file, Object object) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(object);
			objectOutputStream.flush();
			objectOutputStream.close();
			fileOutputStream.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
	
	public Object load(File file) {
		if(file.exists() && file.isFile()) {
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				Object output = objectInputStream.readObject();
				objectInputStream.close();
				fileInputStream.close();
				return output;
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}