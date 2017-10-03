package mapeditor.gui.renderer.algorithms;

import java.util.ArrayList;

import mapeditor.misc.MapObject;
import mapeditor.misc.ObjectscapeLogic;

public class InclusiveWalls {

	/**
	 * Adds a rectangular inclusive structure to objectMap
	 * 
	 * @param rsx
	 *            - coordinate x
	 * @param rsy
	 *            - coordinate y
	 * @param width
	 *            - width of structure
	 * @param height
	 *            - height of structure
	 * @param id
	 *            - object id
	 * @param plane
	 *            - plane
	 */

	public static void createInclusiveWalls(int rsx, int rsy, int width, int height, int id, int plane) {
		// width += 1; width += 1;
		rsx -= 1;
		rsy -= 1;
		int x = 1;
		int y = 1;
		for (int w = x; w < width + 1; w++) {
			for (int h = y; h < height + 1; h++) {

				if (ObjectscapeLogic.objectExistAt(w + rsx, h + rsy, plane))
					ObjectscapeLogic.deleteAllObjects(w + rsx, h + rsy, plane);

				if (h == y) {
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 3, (byte) 0, (byte) plane);
					// w
				}
				if (h == height) {
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 1, (byte) 0, (byte) plane);
					// e
				} else if (w == x) {
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 0, (byte) 0, (byte) plane);
					// n
				} else if (w == width) {
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 2, (byte) 0, (byte) plane);
					// s
				}
			}
		}

		for (int w = x; w < width + 1; w++) {
			for (int h = y; h < height + 1; h++) {
				if (h == y && w == x) {
					if (ObjectscapeLogic.objectExistAt(w + rsx, h + rsy, plane))
						ObjectscapeLogic.deleteAllObjects(w + rsx, h + rsy, plane);
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 3, (byte) 2, (byte) plane);
				} // nw
				if (h == y && w == width) {
					if (ObjectscapeLogic.objectExistAt(w + rsx, h + rsy, plane))
						ObjectscapeLogic.deleteAllObjects(w + rsx, h + rsy, plane);
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 2, (byte) 2, (byte) plane);
				}
				if (h == height && w == x) {
					if (ObjectscapeLogic.objectExistAt(w + rsx, h + rsy, plane))
						ObjectscapeLogic.deleteAllObjects(w + rsx, h + rsy, plane);
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 0, (byte) 2, (byte) plane);
				} // ne
				if (h == height && w == width) {
					if (ObjectscapeLogic.objectExistAt(w + rsx, h + rsy, plane))
						ObjectscapeLogic.deleteAllObjects(w + rsx, h + rsy, plane);
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 1, (byte) 2, (byte) plane);
				} // se
			}
		}
	}

	public static void del(int rsx, int rsy, int width, int height, int id, int plane) {
		// width += 1; width += 1;
		rsx -= 1;
		rsy -= 1;
		int x = 1;
		int y = 1;
		for (int w = x; w < width + 1; w++) {
			for (int h = y; h < height + 1; h++) {
				if (ObjectscapeLogic.objectExistAt(w + rsx, h + rsy, 0))
					ObjectscapeLogic.deleteAllObjects(w + rsx, h + rsy, 0);
				if (ObjectscapeLogic.objectExistAt(w + rsx, h + rsy, 1))
					ObjectscapeLogic.deleteAllObjects(w + rsx, h + rsy, 1);
				if (ObjectscapeLogic.objectExistAt(w + rsx, h + rsy, 2))
					ObjectscapeLogic.deleteAllObjects(w + rsx, h + rsy, 2);
				if (ObjectscapeLogic.objectExistAt(w + rsx, h + rsy, 3))
					ObjectscapeLogic.deleteAllObjects(w + rsx, h + rsy, 3);

			}

		}
	}

}
