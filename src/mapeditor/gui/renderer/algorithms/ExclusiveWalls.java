package mapeditor.gui.renderer.algorithms;

import mapeditor.Main;
import mapeditor.misc.MapObject;
import mapeditor.misc.ObjectscapeLogic;

public class ExclusiveWalls {

	/**
	 * Adds a rectangular exclusive structure to objectMap
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
	public static void createExclusiveWalls(int rsx, int rsy, int width, int height, int id, int plane) {
		rsx -= 1;
		rsy -= 1;
		int x = 1;
		int y = 1;
		for (int w = x - 1; w < width + 2; w++) {
			for (int h = y - 1; h < height + 2; h++) {

				if (ObjectscapeLogic.objectExistAt(w + rsx, h + rsy, plane))
					ObjectscapeLogic.deleteAllObjects(w + rsx, h + rsy, plane);

				if (h == y - 1) {
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 1, (byte) 0, (byte) plane);
					// s
				}
				if (h == height + 1) {
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 3, (byte) 0, (byte) plane);
					// n
				} else if (w == x - 1) {
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 2, (byte) 0, (byte) plane);
					// w
				} else if (w == width + 1) {
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 0, (byte) 0, (byte) plane);
					// e
				}

			}
		}

		for (int w = x - 1; w < width + 2; w++) {
			for (int h = y - 1; h < height + 2; h++) {

				if (h == y - 1 && w == x - 1) {

					if (ObjectscapeLogic.objectExistAt(w + rsx, h + rsy, plane))
						ObjectscapeLogic.deleteAllObjects(w + rsx, h + rsy, plane);
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 1, (byte) 3, (byte) plane);
				} // sw
				if (h == y - 1 && w == width + 1) {

					if (ObjectscapeLogic.objectExistAt(w + rsx, h + rsy, plane))
						ObjectscapeLogic.deleteAllObjects(w + rsx, h + rsy, plane);
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 0, (byte) 3, (byte) plane);
				} // se
				if (h == height + 1 && w == x - 1) {

					if (ObjectscapeLogic.objectExistAt(w + rsx, h + rsy, plane))
						ObjectscapeLogic.deleteAllObjects(w + rsx, h + rsy, plane);
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 2, (byte) 3, (byte) plane);
				} // ne
				if (h == height + 1 && w == width + 1) {

					if (ObjectscapeLogic.objectExistAt(w + rsx, h + rsy, plane))
						ObjectscapeLogic.deleteAllObjects(w + rsx, h + rsy, plane);
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 3, (byte) 3, (byte) plane);
				} // se

			}
		}

	}

}
