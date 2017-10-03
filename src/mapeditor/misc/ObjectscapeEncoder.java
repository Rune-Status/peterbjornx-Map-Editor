package mapeditor.misc;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Logger;

import mapeditor.Constants;
import mapeditor.jagex.rt3.GZIPWrapper;

public class ObjectscapeEncoder {

	protected static final Logger logger = Logger.getLogger(ObjectscapeEncoder.class.getName());

	@SuppressWarnings({ "unchecked", "unused" })
	public static void encodeTextMaps() {

		try {

			File folder = new File(Constants.TEMP_FILE_PATH);
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {

				// for (File file : new File("./data/mapmaker/textmaps").listFiles()) {
				String name = listOfFiles[i].getName();
				BufferedReader in = new BufferedReader(new FileReader(Constants.TEMP_FILE_PATH + name));
				// System.out.println("tound file: "+name);
				String line = null;
				LinkedList<EncodedProp> objects = new LinkedList<EncodedProp>();
				int regionId = 0;
				while ((line = in.readLine()) != null) {
					// line = "id: 202 x: 3222 y: 3111 z: 3 type: 4 rotation: 5";

					if (!line.contains("x:") && !line.contains("y:")) {
						continue;
					}
					String[] info = line.split(" ");
					regionId = ((((Integer.parseInt(info[3]) >> 3) / 8) << 8) | ((Integer.parseInt(info[5]) >> 3) / 8));
					objects.add(new EncodedProp(Integer.parseInt(info[1]), Integer.parseInt(info[3]),
							Integer.parseInt(info[5]), Integer.parseInt(info[7]), Integer.parseInt(info[11]),
							Integer.parseInt(info[9])));
					// public GameObject(int id, int x, int y, int z, int type, int rotation) {

				}
				int MAX_OBJECT_ID = 0;
				for (EncodedProp object : objects) {
					if (MAX_OBJECT_ID < object.getId()) {
						MAX_OBJECT_ID = object.getId();
					}
				}
				Map<Integer, LinkedList<EncodedProp>> OBJECT_GROUPS = new HashMap<Integer, LinkedList<EncodedProp>>();
				for (EncodedProp object : objects) {
					if (OBJECT_GROUPS.containsKey(object.getId())) {
						LinkedList<EncodedProp> list = OBJECT_GROUPS.get(object.getId());
						list.add(object);
					} else {
						OBJECT_GROUPS.put(object.getId(), new LinkedList<EncodedProp>());
						LinkedList<EncodedProp> list = OBJECT_GROUPS.get(object.getId());
						list.add(object);
					}
				}
				LinkedList<Object[]> sorted_map = new LinkedList<Object[]>();
				for (int id = 0; id < MAX_OBJECT_ID + 1; id++) {
					if (OBJECT_GROUPS.containsKey(id)) {
						LinkedList<EncodedProp> list = OBJECT_GROUPS.get(id);
						Map<Integer, Integer> OBJECT_COORDS = new HashMap<Integer, Integer>();
						LinkedList<int[]> OBJECT_SORTED = new LinkedList<int[]>();
						int MAX_COORDS = 0;
						boolean ignore = false;
						System.out.println("list len: " + list.size());
						for (EncodedProp object : list) {
							int objX = -1;
							int objY = -1;
							int objZ = -1;
							objX = object.getX();
							objY = object.getY();
							objZ = object.getZ();// cached object coords

							OBJECT_COORDS.put(object.getLocation(), object.getData());
							if (MAX_COORDS < object.getLocation()) {
								MAX_COORDS = object.getLocation();
							}

						}
						for (int coord = 0; coord < MAX_COORDS + 1; coord++) {
							if (OBJECT_COORDS.containsKey(coord)) {
								OBJECT_SORTED.add(new int[] { coord, OBJECT_COORDS.get(coord) });
							}
						}
						sorted_map.add(new Object[] { id, OBJECT_SORTED });

					}
				}
				Buffer buffer = new Buffer();
				int prev_id = -1;
				while (!sorted_map.isEmpty()) {
					Object[] object = sorted_map.remove(0);
					int id_current = (Integer) object[0];
					// System.out.println("writing objectid "+id_current);
					LinkedList<int[]> coords = (LinkedList<int[]>) object[1];
					int id = id_current - prev_id;
					prev_id = id_current;
					buffer.putSmart(id);
					int prev_coord = 0;
					while (!coords.isEmpty()) {
						int[] coord = coords.remove(0);
						int loc = (coord[0] - prev_coord) + 1;
						prev_coord = coord[0];
						buffer.putSmart(loc);
						buffer.put((byte) coord[1]);
					}
					buffer.putSmart(0);
				}
				buffer.putSmart(0);

				RandomAccessFile raf = new RandomAccessFile(
						new File(Constants.EXPORT_OBJECTSCAPE_PATH + name.replace(".txt", "") + ".dat"), "rw");
				byte[] data = new byte[buffer.getBuffer().position()];
				((ByteBuffer) buffer.getBuffer().flip()).get(data);
				raf.write(data);
				int count = 0;
				int regionX = ((regionId >> 8) * 64);
				int regionY = ((regionId & 0xff) * 64);
				try {
					Buffer objectmap = new Buffer(data);
					int objectId = -1;
					int incr;
					while ((incr = objectmap.getSmart()) != 0) {
						objectId += incr;
						int location = 0;
						int incr2;
						while ((incr2 = objectmap.getSmart()) != 0) {
							location += incr2 - 1;
							int x = (location >> 6 & 0x3f);
							int y = (location & 0x3f);
							int z = location >> 12;
							int objectData = objectmap.get() & 0xff;
							int type = objectData >> 2;
							int rotation = objectData & 0x3;
							if (z >= 0 && z <= 3) {
								count++;
							}
							System.out.println("ID: " + objectId + ", X: " + x + ", Y: " + y);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("Encoded " + count + " objects to map " + name.replace(".txt", "") + ".dat");
				in.close();
				raf.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public static void encodeObjects() {
		try {
			// iterate through every map id in the table...
			for (Entry<Integer, ArrayList<MapObject>> entry : ObjectscapeLogic.objectTables.entrySet()) {
				int objectMapId = entry.getKey();
				System.out.println("retrieved key " + objectMapId);
				String name = "" + objectMapId;

				LinkedList<EncodedProp> objects = new LinkedList<EncodedProp>();
				int regionHash = 0; // no longer relevant.
				// ((((objectX >> 3) / 8) << 8) | ((objectY >> 3) / 8));

				for (MapObject o : entry.getValue()) {
					objects.add(new EncodedProp(o.objectId, o.x, o.y, o.plane, o.type, o.face));
				}
				if (objects.size() == 0) {
					continue;
				}
				System.out.print("Writing " + objects.size() + " objects to Map " + objectMapId + "... ");

				int MAX_OBJECT_ID = 0;
				for (EncodedProp object : objects) {
					System.out.println(object.getId());
					if (MAX_OBJECT_ID < object.getId()) {
						MAX_OBJECT_ID = object.getId();
					}
				}
				Map<Integer, LinkedList<EncodedProp>> OBJECT_GROUPS = new HashMap<Integer, LinkedList<EncodedProp>>();
				for (EncodedProp object : objects) {
					if (OBJECT_GROUPS.containsKey(object.getId())) {
						LinkedList<EncodedProp> list = OBJECT_GROUPS.get(object.getId());
						list.add(object);
					} else {
						OBJECT_GROUPS.put(object.getId(), new LinkedList<EncodedProp>());
						LinkedList<EncodedProp> list = OBJECT_GROUPS.get(object.getId());
						list.add(object);
					}
				}
				LinkedList<Object[]> sorted_map = new LinkedList<Object[]>();
				for (int id = 0; id < MAX_OBJECT_ID + 1; id++) {
					if (OBJECT_GROUPS.containsKey(id)) {
						LinkedList<EncodedProp> list = OBJECT_GROUPS.get(id);
						Map<Integer, Integer> OBJECT_COORDS = new HashMap<Integer, Integer>();
						LinkedList<int[]> OBJECT_SORTED = new LinkedList<int[]>();
						int MAX_COORDS = 0;
						for (EncodedProp object : list) {
							OBJECT_COORDS.put(object.getLocation(), object.getData());
							if (MAX_COORDS < object.getLocation()) {
								MAX_COORDS = object.getLocation();
							}
						}
						for (int coord = 0; coord < MAX_COORDS + 1; coord++) {
							if (OBJECT_COORDS.containsKey(coord)) {
								OBJECT_SORTED.add(new int[] { coord, OBJECT_COORDS.get(coord) });
							}
						}
						sorted_map.add(new Object[] { id, OBJECT_SORTED });
					}
				}
				Buffer buffer = new Buffer();
				int prev_id = -1;
				while (!sorted_map.isEmpty()) {
					Object[] object = sorted_map.remove(0);
					int id_current = (Integer) object[0];
					if (id_current == 982)
						id_current = 6600;

					// System.out.println("writing objectid "+id_current);
					LinkedList<int[]> coords = (LinkedList<int[]>) object[1];
					int id = id_current - prev_id;

					prev_id = id_current;
					buffer.putSmart(id);
					int prev_coord = 0;
					while (!coords.isEmpty()) {
						int[] coord = coords.remove(0);
						int loc = (coord[0] - prev_coord) + 1;
						prev_coord = coord[0];
						buffer.putSmart(loc);
						buffer.put((byte) coord[1]);
					}
					buffer.putSmart(0);
				}
				buffer.putSmart(0);

				RandomAccessFile raf = new RandomAccessFile(
						new File(Constants.EXPORT_OBJECTSCAPE_PATH + objectMapId + ".gz"), "rw");
				byte[] data = new byte[buffer.getBuffer().position()];
				((ByteBuffer) buffer.getBuffer().flip()).get(data);

				/*
				 * gzip
				 */
				data = GZIPWrapper.compress(data);

				raf.write(data);
				int count = 0;
				int regionX = ((regionHash >> 8) * 64);
				int regionY = ((regionHash & 0xff) * 64);
				try {
					Buffer objectmap = new Buffer(data);
					int objectId1 = -1;
					int incr;
					while ((incr = objectmap.getSmart()) != 0) {
						objectId1 += incr;
						int location = 0;
						int incr2;
						while ((incr2 = objectmap.getSmart()) != 0) {
							location += incr2 - 1;
							int x = (location >> 6 & 0x3f);
							int y = (location & 0x3f);
							int z = location >> 12;
							int objectData = objectmap.get() & 0xff;
							int type = objectData >> 2;
							int rotation = objectData & 0x3;
							if (z >= 0 && z <= 3) {
								count++;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				raf.close();
				System.out.println(" done.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	private static class EncodedProp {
		private final int id, encodedLocation, data, x, y, z;

		@SuppressWarnings("unused")
		public EncodedProp(int id, int x, int y, int z, int type, int rotation) {
			this.id = id;
			int mapRegionId = ((((x >> 3) / 8) << 8) | ((y >> 3) / 8));
			int mapRegionX = ((mapRegionId >> 8) * 64);
			int mapRegionY = ((mapRegionId & 0xff) * 64);
			this.x = x;
			;
			this.y = y;
			this.z = z;
			int encodedLocation = 0;
			encodedLocation |= y & 0x3f;
			encodedLocation |= (x & 0x3f) << 6;
			encodedLocation |= (z & 0x3) << 12;
			this.encodedLocation = encodedLocation;
			int data = 0;
			data |= rotation & 0x3;
			data |= type << 2;
			this.data = data;
		}

		public int getId() {
			return id;
		}

		public int getLocation() {
			return encodedLocation;
		}

		public int getData() {
			return data;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getZ() {
			return z;
		}
	}

	private static class Buffer {

		private ByteBuffer buffer;

		public Buffer() {
			buffer = ByteBuffer.allocateDirect(500000);
		}

		public Buffer(byte[] data) {
			buffer = ByteBuffer.wrap(data);
		}

		public byte get() {
			return buffer.get();
		}

		@SuppressWarnings("unused")
		public int getShort() {
			return buffer.getShort();
		}

		public int getSmart() {
			int val = buffer.get() & 0xff;
			buffer.position(buffer.position() - 1);
			if (val < 128) {
				return (buffer.get() & 0xff);
			}
			return -32768 + (buffer.getShort() & 0xffff);
		}

		public void put(byte b) {
			buffer.put(b);
		}

		public void putShort(short s) {
			buffer.putShort(s);
		}

		public void putSmart(int val) {
			if (val >= 128) {
				putShort((short) (val + 32768));
			} else {
				put((byte) val);
			}
		}

		public ByteBuffer getBuffer() {
			return buffer;
		}
	}
}