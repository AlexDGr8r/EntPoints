package net.entcraft.entpoints;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.util.Vector;

public class BlockMemory {
	
	private List<Vector> placements = new ArrayList<Vector>();
	private String filePath;
	
	public BlockMemory(String path) {
		filePath = path;
	}
	
	@SuppressWarnings("unchecked")
	public void load() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath));
			Object result = ois.readObject();
			List<Map<String, Object>> data = (List<Map<String, Object>>)result;
			placements = new ArrayList<Vector>();
			for (int i = 0; i < data.size(); i++) {
				placements.add(Vector.deserialize(data.get(i)));
			}
		} catch (FileNotFoundException e ) {
			// Do nothing, normal
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		try {
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < placements.size(); i++) {
				data.add(placements.get(i).serialize());
			}
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath));
			oos.writeObject(data);
			oos.flush();
			oos.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void put(Vector v) {
		placements.add(v);
	}
	
	public boolean blockPlaced(Vector v) {
		return placements.contains(v);
	}
	
	public void remove(Vector v) {
		if (blockPlaced(v)) {
			placements.remove(v);
		}
	}

}
