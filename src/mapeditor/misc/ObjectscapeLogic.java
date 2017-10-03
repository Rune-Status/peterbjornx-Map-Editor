package mapeditor.misc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import mapeditor.Constants;
import mapeditor.Main;
import mapeditor.gui.dockables.ObjectEditorPanel;
import mapeditor.jagex.rt3.FileOperations;
import mapeditor.jagex.rt3.GZIPWrapper;
import mapeditor.jagex.rt3.InteractiveObject;
import mapeditor.jagex.rt3.MapRegion;
import mapeditor.jagex.rt3.SceneGraph;
import mapeditor.jagex.rt3.TileSetting;

public class ObjectscapeLogic {

	/**
	 * < Map ID, List of objects in map region >
	 */
	public static HashMap<Integer, ArrayList<MapObject>> objectTables = new HashMap<Integer, ArrayList<MapObject>>();

	/**
	 * Placement orientation for tiles and objects
	 */
	public static int placementOrientation;
	public static String[] ORIENTATIONS = { "WEST", "NORTH", "EAST", "SOUTH" };

	int wallIterator = 0;
	public static int[] allWalls = { 65, 66, 987, 1415, 1416, 1488, 1535, 1538, 1583, 1584, 1585, 1586, 1587, 1588,
			1602, 1603, 1620, 1621, 1622, 1624, 1625, 1627, 1631, 1664, 1687, 1688, 1689, 1691, 1692, 1693, 1694, 1695,
			1696, 1769, 1770, 1806, 1808, 1809, 1902, 1904, 1905, 1906, 1907, 1908, 1909, 1910, 1911, 1912, 2001, 2003,
			2070, 2117, 2341, 2518, 2606, 2629, 2854, 2855, 2926, 3435, 3438, 3524, 3586, 3595, 3596, 3620, 3626, 3638,
			3744, 4409, 4445, 4502, 4503, 4504, 4573, 4635, 4673, 5052, 5053, 5190, 5191, 5192, 5361, 5362, 5440, 5452,
			5510, 5634, 5636, 5828, 6145, 6209, 6342, 6343, 6590, 6591, 6592, 6600, 6965, 7199, 7358, 7381, 9139, 9150,
			9153, 9154, 9473, 9596, 9598, 10333, 10339, 10340, 10341, 10531, 10643, 10644, 11109, 11512, 11536, 11652,
			11683, 11686, 11702, 11772, 11776, 11818, 11819, 11820, 11828, 11831, 11881, 11884, 11887, 12123, 12662,
			12668, 12671, 12674, 12696, 12700, 12821, 12834, 12851, 12913, 14036, 14336, 14543, 14544, 14622, 14723,
			14770

	};

	/**
	 * Layers: 0 - interactive objects 1 - wall decor 2 - ground decorra 3 - wall
	 * objects
	 */
	public static int getLayerForType(int type) {
		switch (type) {
		case 10:
			return 0;

		case 4:
		case 5:
		case 6:
		case 7:
			return 1;

		case 11:
		case 22:
			return 2;

		default:
			return 3;
		}
	}

	/**
	 * Handles object adding
	 */
	public static void addObject(int id, int rsx, int rsy, int face, int type, int z) {
		final int mapId = getObjectscapeID(rsx, rsy);
		System.out.println("Placed object " + id + ", at " + rsx + "," + rsy + "," + z + ", with face " + face
				+ ", type " + type + " -> ");
		int x = Math.abs(rsx - (Main.loadedMapX * 64));// global coords
		int y = Math.abs(rsy - (Main.loadedMapZ * 64));// to local...

		final int layer = getLayerForType(type);
		if (Main.objectExistAt[x][y][z][layer]) {

			// return;
		}
		// drawing it on the scene...
		Main.objectExistAt[x][y][z][layer] = true;
		Main.mapRegion.add_object(id, type, face, z, x, y, null, Main.sceneGraph);

		// adding it to the object table
		for (Entry<Integer, ArrayList<MapObject>> entry : objectTables.entrySet()) {
			if (entry.getKey() == mapId) {
				ArrayList<MapObject> objects = entry.getValue();
				objects.add(new MapObject(id, rsx, rsy, (byte) face, (byte) type, (byte) z));
				objectTables.put(mapId, objects);
			}
		}
	}

	public static int localizeX(int globalX) {
		return Math.abs(globalX - (Main.loadedMapX * 64));
	}

	public static int localizeY(int globalY) {
		return Math.abs(globalY - (Main.loadedMapZ * 64));
	}

	public static int globalizeX(int localX) {
		return (Main.loadedMapX * 64) + localX;
	}

	public static int globalizeY(int localY) {
		return (Main.loadedMapZ * 64) + localY;
	}

	public static boolean objectExistAt(int globalX, int globalY, int z) {
		final int mapId = getObjectscapeID(globalX, globalY);
		for (Entry<Integer, ArrayList<MapObject>> entry : objectTables.entrySet()) {
			if (entry.getKey() == mapId) {
				ArrayList<MapObject> objects = entry.getValue();
				for (MapObject m : objects) {
					if (m.x == globalX && m.y == globalY && m.plane == z)
						return true;
				}

			}
		}
		return false;
	}

	public static void deleteAllObjects(int globalX, int globalY, int z) {
		try {
			final int localX = localizeX(globalX);// global coords
			final int localY = localizeY(globalY);// to local...
			final int mapId = getObjectscapeID(globalX, globalY);
			for (Entry<Integer, ArrayList<MapObject>> entry : objectTables.entrySet()) {
				if (entry.getKey() == mapId) {
					ArrayList<MapObject> objects = entry.getValue();
					boolean found = false;
					for (Iterator<MapObject> iter = objects.iterator(); iter.hasNext();) {
						MapObject obj = iter.next();
						if (obj.x == globalX && obj.y == globalY && obj.plane == z) {
							found = true;
							iter.remove();
							SceneGraph.tileArray[z][localX][localY].interactiveObjects = new InteractiveObject[5];
							SceneGraph.tileArray[z][localX][localY].wallDecoration = null;
							SceneGraph.tileArray[z][localX][localY].groundDecoration = null;
							SceneGraph.tileArray[z][localX][localY].wallObject = null;

						}
					}
					objectTables.put(mapId, objects);
				}
			}
		} catch (NullPointerException e) {

		}
	}

	/**
	 * Handles object adding
	 */
	public static void addObject(int z, int x, int y) {

		final int layer = getLayerForType(ObjectEditorPanel.selectedObjectType);
		if (Main.objectExistAt[x][y][z][layer]) {
			System.out.println("Object already exist.");
			return;
		}
		// drawing it on the scene...
		Main.objectExistAt[x][y][z][layer] = true;
		Main.mapRegion.add_object(ObjectEditorPanel.selectedObjectID, ObjectEditorPanel.selectedObjectType,
				ObjectscapeLogic.placementOrientation, z, x, y, null, Main.sceneGraph);

		final int globalX = (Main.loadedMapX * 64) + Main.hoverX;
		final int globalY = (Main.loadedMapZ * 64) + Main.hoverY;
		final int mapId = getObjectscapeID(globalX, globalY);
		System.out.println("Placed object at " + globalX + "," + globalX + "," + globalY + ", onto map " + mapId);
		boolean flag = false;

		for (Entry<Integer, ArrayList<MapObject>> entry : objectTables.entrySet()) {
			if (entry.getKey() == mapId) {
				ArrayList<MapObject> objects = entry.getValue();
				objects.add(new MapObject(ObjectEditorPanel.selectedObjectID, globalX, globalY,
						(byte) ObjectscapeLogic.placementOrientation, (byte) ObjectEditorPanel.selectedObjectType,
						(byte) Main.editorMainWindow.getEditorToolbar().getTargetHL()));
				objectTables.put(mapId, objects);
				flag = true;
			}
		}

		if (!flag) {
			System.out.println("Creating new entry...");
			ObjectscapeLogic.objectTables.put(mapId, new ArrayList<MapObject>());
			for (Entry<Integer, ArrayList<MapObject>> entry : objectTables.entrySet()) {
				if (entry.getKey() == mapId) {
					ArrayList<MapObject> objects = entry.getValue();
					objects.add(new MapObject(ObjectEditorPanel.selectedObjectID, globalX, globalY,
							(byte) ObjectscapeLogic.placementOrientation, (byte) ObjectEditorPanel.selectedObjectType,
							(byte) Main.editorMainWindow.getEditorToolbar().getTargetHL()));
					objectTables.put(mapId, objects);
				}
			}
		}

		// ground decor orientation change thingy
		if (ObjectEditorPanel.selectedObjectType == 11
				|| ObjectEditorPanel.selectedObjectType == 22 && ObjectEditorPanel.selectedObjectID > 1000) {

			if (ObjectEditorPanel.selectedObjectID == 9541) {
				return;
			}

			ObjectscapeLogic.placementOrientation++;
			ObjectscapeLogic.placementOrientation &= 3;

			if (ObjectEditorPanel.selectedObjectID == 4736) {
				Random r = new Random();

				int r2 = r.nextInt(3);
				switch (r2) {
				case 0:
					ObjectEditorPanel.selectedObjectID = 4736;
					break;

				case 1:
					ObjectEditorPanel.selectedObjectID = 4738;
					break;

				case 2:
					ObjectEditorPanel.selectedObjectID = 5534;
					break;
				}

			}

		}

		// System.out.println("Placing object "+ObjectEditorPanel.selectedObjectType+"
		// at "+globalX+", "+globalY+", "+heightLevel+", ");
	}

	public static void addObject2(int z, int x, int y, int id, int face, int type) {

		final int layer = getLayerForType(id);
		if (Main.objectExistAt[x][y][z][layer]) {
			System.out.println("Object already exist.");
			return;
		}
		// drawing it on the scene...
		Main.objectExistAt[x][y][z][layer] = true;
		Main.mapRegion.add_object(id, type, face, z, x, y, null, Main.sceneGraph);

		final int globalX = (Main.loadedMapX * 64) + x;
		final int globalY = (Main.loadedMapZ * 64) + y;
		final int mapId = getObjectscapeID(globalX, globalY);
		System.out.println("Placed object at " + globalX + "," + globalX + "," + globalY + ", onto map " + mapId);
		boolean flag = false;

		for (Entry<Integer, ArrayList<MapObject>> entry : objectTables.entrySet()) {
			if (entry.getKey() == mapId) {
				ArrayList<MapObject> objects = entry.getValue();
				objects.add(new MapObject(id, globalX, globalY, (byte) face, (byte) type, (byte) z));
				objectTables.put(mapId, objects);
				flag = true;
			}
		}

		if (!flag) {
			System.out.println("Creating new entry...");
			ObjectscapeLogic.objectTables.put(mapId, new ArrayList<MapObject>());
			for (Entry<Integer, ArrayList<MapObject>> entry : objectTables.entrySet()) {
				if (entry.getKey() == mapId) {
					ArrayList<MapObject> objects = entry.getValue();
					objects.add(new MapObject(id, globalX, globalY, (byte) face, (byte) type, (byte) z));
					objectTables.put(mapId, objects);
				}
			}
		}

		// System.out.println("Placing object "+ObjectEditorPanel.selectedObjectType+"
		// at "+globalX+", "+globalY+", "+heightLevel+", ");
	}

	/**
	 * Handles object removing
	 */
	public static void deleteObject(int z, int x, int y) {
		for (int layer = 0; layer < 4; layer++) {
			Main.objectExistAt[x][y][z][layer] = false;
		}

		// deleting it off the object table
		final int globalX = (Main.loadedMapX * 64) + Main.hoverX;
		final int globalY = (Main.loadedMapZ * 64) + Main.hoverY;
		final int mapId = getObjectscapeID(globalX, globalY);
		for (Entry<Integer, ArrayList<MapObject>> entry : objectTables.entrySet()) {
			if (entry.getKey() == mapId) {
				ArrayList<MapObject> objects = entry.getValue();
				Collections.reverse(objects);
				boolean found = false;
				for (Iterator<MapObject> iter = objects.iterator(); iter.hasNext();) {
					MapObject obj = iter.next();
					if (obj.x == globalX && obj.y == globalY
							&& obj.plane == Main.editorMainWindow.getEditorToolbar().getTargetHL()) {
						// remove from table
						found = true;
						iter.remove();

						// remove from scene
						int entityCount = SceneGraph.tileArray[z][x][y].entityCount;

						/*
						 * if (layer == 0) UID = sceneGraph.getWallObjectUID(y, x, z); if (layer == 1)
						 * UID = sceneGraph.getWallDecorationUID(y, x, z); if (layer == 2) UID =
						 * sceneGraph.getInteractableObjectUID(y, x, z); if (layer == 3) UID =
						 * sceneGraph.getGroundDecorationUID(y, x, z);
						 */
						switch (getLayerForType(obj.type)) {
						case 0:
							SceneGraph.tileArray[z][x][y].interactiveObjects = new InteractiveObject[5];
							if (entityCount > 0)
								entityCount--;
							break;

						case 1:
							SceneGraph.tileArray[z][x][y].wallDecoration = null;
							if (entityCount > 0)
								entityCount--;
							break;

						case 2:
							SceneGraph.tileArray[z][x][y].groundDecoration = null;
							if (entityCount > 0)
								entityCount--;
							break;

						case 3:
							SceneGraph.tileArray[z][x][y].wallObject = null;
							if (entityCount > 0)
								entityCount--;
							break;

						}
						return; // execute once, to delete 1 layer at a time
					}
				}
				Collections.reverse(objects);
				objectTables.put(mapId, objects);
				if (!found)
					System.out.println("Unable to find object."); // debug
			}
		}

		// System.out.println("Placing object "+ObjectEditorPanel.selectedObjectType+"
		// at "+globalX+", "+globalY+", "+heightLevel+", ");
	}

	/**
	 * Exports the objectscape file(s)
	 */
	public static void exportObjectscape() {
		ObjectscapeEncoder.encodeObjects();
	}

	/**
	 * Exports and repacks objectscape file(s)
	 */
	public static void packObjectscapes() {
		/*
		 * System.out.println("Exporting objectscapes..."); purgeListDuplicates();
		 * writeObjectMap(); ObjectscapeEncoder.encodeTextMaps(); try {
		 * FileUtils.cleanDirectory(new File(Constants.TEMP_FILE_PATH)); } catch
		 * (IOException e1) { e1.printStackTrace(); }
		 */

		// Cache packing
		File folder = new File(Constants.EXPORT_OBJECTSCAPE_PATH);
		File[] listOfFiles = folder.listFiles();
		int objectScapeIdx = -1;
		int counter = 0;
		for (int i = 0; i < listOfFiles.length; i++) {
			File objectScape = listOfFiles[i];
			try {
				objectScapeIdx = Integer.parseInt(truncateExtension(objectScape.getName()));
			} catch (NumberFormatException e) {
				System.out.println("File: " + objectScape.getName() + " has invalid name, Skipping...");
				continue;
			}
			byte[] objectScapeData;
			if (objectScape.getName().endsWith(".gz")) {
				// the file is already gzipped
				objectScapeData = FileOperations.ReadFile(listOfFiles[i].getAbsolutePath());
			} else if (objectScape.getName().endsWith(".dat")) {
				// the file need to be gzipped
				objectScapeData = FileOperations.ReadFile(listOfFiles[i].getAbsolutePath());
				objectScapeData = GZIPWrapper.compress(objectScapeData);
			} else {
				System.out.println("File: " + objectScape.getName() + " has invalid extension, Skipping...");
				continue;
			}
			Main.jagexFileStores[4].put(objectScapeData.length, objectScapeData, objectScapeIdx);
			counter++;
		}
		System.out.println("Packed " + counter + "/" + listOfFiles.length + " objectscapes to cache.");
	}

	public static String truncateExtension(String fileName) {
		File tmpFile = new File(fileName);
		tmpFile.getName();
		int whereDot = tmpFile.getName().lastIndexOf('.');
		if (0 < whereDot && whereDot <= tmpFile.getName().length() - 2) {
			return tmpFile.getName().substring(0, whereDot);
		}
		return "";
	}

	public static int getObjectscapeID(int globalX, int globalY) {
		// generate a region hash
		int regionHash = ((((globalX >> 3) / 8) << 8) | ((globalY >> 3) / 8));
		// search map_index for the region hash
		int result = Main.onDemandFetcher.getObjectscapeID(regionHash);
		return result;

	}

	public static int getLandscapeID(int globalX, int globalY) {
		// generate a region hash
		int regionHash = ((((globalX >> 3) / 8) << 8) | ((globalY >> 3) / 8));
		// search map_index for the region hash
		int result = Main.onDemandFetcher.getLandscapeID(regionHash);
		return result;

	}

}
