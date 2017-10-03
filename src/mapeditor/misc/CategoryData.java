package mapeditor.misc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map.Entry;

public class CategoryData {

	public static HashMap<Integer, CategoryData> categoryData = new HashMap<Integer, CategoryData>();

	public String name;
	public int defaultType;
	public int[] objectIds;
	public String[] objectNames;
	public int id;

	public CategoryData(String name, int id, int[] objectIds, String[] objectNames) {
		this.id = id;
		this.name = name;
		this.objectIds = objectIds;
		this.objectNames = objectNames;
	}

	public static boolean load() {
		System.out.println("Loading category data...");
		boolean pass = true;
		try {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader("./data/categories.txt"));

			String line;
			int iterator2 = 0;
			while ((line = br.readLine()) != null) {
				int iterator = 0;
				boolean updateName = true;
				String categoryName = "";
				int[] objectIds = new int[55];
				String[] objectNames = new String[55];
				while (true) {
					if (updateName) {
						categoryName = line;
					}
					updateName = false;
					String _line = br.readLine();
					if (_line == null) {
						return true;// escape
					}
					try {
						try {
							String[] splitLine = _line.split("\t");

							objectIds[iterator] = Integer.parseInt(splitLine[0]);
							objectNames[iterator] = splitLine[1];
							iterator++;

						} catch (NullPointerException e) {
							pass = false;
							// EoF or fucked up format
						}

					} catch (NumberFormatException e) {
						updateName = true;
						CategoryData c = new CategoryData(categoryName, iterator2, objectIds, objectNames);
						categoryData.put(iterator2, c);
						// printCategoryData(c);
						objectIds = new int[55];
						objectNames = new String[55];
						iterator2++;
						break;
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			pass = false;
		} // or write your own exceptions
		return pass;
	}

	public static int[] deflateArray_int(int[] array) {
		int[] temp = array.clone();
		int trimIndex = 0;
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] != 0) {
				trimIndex++;
			} else {
				try {
					if (temp[i + 1] == 0) {
					} else {
						trimIndex++;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
		int[] temp2 = new int[trimIndex];
		for (int i = 0; i < temp2.length; i++)
			temp2[i] = temp[i];
		return temp2;
	}

	@SuppressWarnings("unlikely-arg-type")
	public static String[] deflateArray(String[] array) {
		String[] temp = array.clone();
		int trimIndex = 0;
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] != null) {
				trimIndex++;
			} else {
				try {
					if (temp[i + 1] == null) {
					} else {
						trimIndex++;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
		String[] temp2 = new String[trimIndex];
		for (int i = 0; i < temp2.length; i++)
			temp2[i] = temp[i];
		for (int i = 0; i < temp2.length; i++)
			if (temp2[i] == null || temp2[i].equals("") || temp2.equals(" ") || temp2.equals("null"))
				temp2[i] = "hidden";
		return temp2;
	}

	public static void printCategoryData(CategoryData C) {
		C.objectIds = deflateArray_int(C.objectIds);
		C.objectNames = deflateArray(C.objectNames);
		System.out.println("CATEGORY NAME: " + C.name + ", " + C.id);
		for (int i = 0; i < C.objectIds.length; i++) {
			try {
				System.out.println("\tCATEOGRY ENTRY " + C.objectIds[i] + "\t" + C.objectNames[i]);
			} catch (ArrayIndexOutOfBoundsException e) {
				// who cares
			}

		}

	}

	public static String[] getWallsArray() {
		String[] menuNames = new String[255];
		menuNames[0] = "NONE";
		for (Entry<Integer, CategoryData> entry : categoryData.entrySet()) {
			CategoryData C = entry.getValue();
			if (C.name.trim().toLowerCase().startsWith("walls")) {
				for (int i = 1; i < C.objectIds.length; i++) {
					if (C.objectIds[i] == 0)
						continue;
					menuNames[i] = C.objectIds[i] + " - " + C.objectNames[i];
				}
			}
		}
		return deflateArray(menuNames);
	}

	public static String[] getCategories() {
		String[] categoryList = new String[255];
		int iterator = 0;
		for (Entry<Integer, CategoryData> entry : categoryData.entrySet()) {
			CategoryData C = entry.getValue();
			categoryList[iterator] = C.name;
			iterator++;
		}
		return deflateArray(categoryList);
	}

	public static int[] getCategoryItemIDs(int category) {
		int[] categoryItemIDs = new int[255];
		CategoryData C = categoryData.get(category);
		for (int i = 0; i < C.objectIds.length; i++) {
			categoryItemIDs[i] = C.objectIds[i];
		}
		categoryItemIDs = deflateArray_int(categoryItemIDs);
		return categoryItemIDs;

	}

	public static String[] getCategoryItemNames(int category) {
		String[] categoryItemNames = new String[255];
		CategoryData C = categoryData.get(category);
		for (int i = 0; i < C.objectNames.length; i++) {
			categoryItemNames[i] = C.objectIds[i] + " - " + C.objectNames[i];
		}
		return deflateArray(categoryItemNames);

	}
}