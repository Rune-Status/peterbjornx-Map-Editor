package mapeditor.gui.renderer.algorithms;

import mapeditor.misc.MapObject;
import mapeditor.misc.ObjectscapeLogic;

public class Roof {

	public static void generateRoof(int rsx, int rsy, int width, int height, int id, int id2, int plane) {
		rsx--;
		rsy--;

		int x = 1;
		int y = 1;
		for (int w = x - 1; w < width + 2; w++) {
			for (int h = y - 1; h < height + 2; h++) {

				if (h == y - 1) {
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 1, (byte) 18, (byte) plane);
					// s
				} else if (h == height + 1) {
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 3, (byte) 18, (byte) plane);

					// n
				} else if (w == x - 1) {
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 2, (byte) 18, (byte) plane);

					// w
				} else if (w == width + 1) {
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 0, (byte) 18, (byte) plane);
					// e
				}

				if (h == y - 1 && w == x - 1) {
					if (ObjectscapeLogic.objectExistAt(w + rsx, h + rsy, plane))
						ObjectscapeLogic.deleteAllObjects(w + rsx, h + rsy, plane);

				} // sw
				if (h == y - 1 && w == width + 1) {
					if (ObjectscapeLogic.objectExistAt(w + rsx, h + rsy, plane))
						ObjectscapeLogic.deleteAllObjects(w + rsx, h + rsy, plane);

				} // se
				if (h == height + 1 && w == x - 1) {
					if (ObjectscapeLogic.objectExistAt(w + rsx, h + rsy, plane))
						ObjectscapeLogic.deleteAllObjects(w + rsx, h + rsy, plane);

				} // ne
				if (h == height + 1 && w == width + 1) {
					if (ObjectscapeLogic.objectExistAt(w + rsx, h + rsy, plane))
						ObjectscapeLogic.deleteAllObjects(w + rsx, h + rsy, plane);

				} // se

			}
		}

		for (int w = x - 1; w < width + 2; w++) {
			for (int h = y - 1; h < height + 2; h++) {

				if (h == y - 1 && w == x - 1) {
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 1, (byte) 19, (byte) plane);

				} // sw
				if (h == y - 1 && w == width + 1) {
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 0, (byte) 19, (byte) plane);

				} // se
				if (h == height + 1 && w == x - 1) {
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 2, (byte) 19, (byte) plane);

				} // ne
				if (h == height + 1 && w == width + 1) {
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 3, (byte) 19, (byte) plane);

				} // se

			}
		}

		createInnerBox(rsx + 1, rsy + 1, width - 2, height - 2, id2, plane);
	}

	private static void createInnerBox(int rsx, int rsy, int width, int height, int id, int plane) {

		int x = 1;
		int y = 1;
		for (int w = x - 1; w < width + 2; w++) {
			for (int h = y - 1; h < height + 2; h++) {

				if (!(h == y - 1 && w == x - 1) && !(h == y - 1 && w == width + 1) && !(h == height + 1 && w == x - 1)
						&& !(h == height + 1 && w == width + 1)) {

					if (h == y - 1) {
						ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 1, (byte) 12, (byte) plane);
						// s
					}
					if (h == height + 1) {
						ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 3, (byte) 12, (byte) plane);

						// n
					} else if (w == x - 1) {
						ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 2, (byte) 12, (byte) plane);

						// w
					} else if (w == width + 1) {
						ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 0, (byte) 12, (byte) plane);

						// e
					}

				}
				if (h == y - 1 && w == x - 1) {
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 1, (byte) 16, (byte) plane);

				} // sw
				if (h == y - 1 && w == width + 1) {
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 0, (byte) 16, (byte) plane);

				} // se
				if (h == height + 1 && w == x - 1) {
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 2, (byte) 16, (byte) plane);

				} // ne
				if (h == height + 1 && w == width + 1) {
					ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 3, (byte) 16, (byte) plane);

				} // se
			}
		}
		fill(rsx + 1, rsy + 1, width - 2, height - 2, id, plane);
	}

	private static void fill(int rsx, int rsy, int width, int height, int id, int plane) {
		int x = 1;
		int y = 1;
		for (int w = x - 1; w < width + 2; w++) {
			for (int h = y - 1; h < height + 2; h++) {
				ObjectscapeLogic.addObject(id, w + rsx, h + rsy, (byte) 0, (byte) 17, (byte) plane);

			}
		}
	}

}
