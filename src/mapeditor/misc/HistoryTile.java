package mapeditor.misc;

public class HistoryTile {

	private int id;
	private int tile_z;
	private int tile_x;
	private int tile_y;
	private int object_info;
	private int object_type;
	private int object_orientation;

	public HistoryTile(int id, int x, int y, int z, int info, int type, int face) {
		System.out.println("added history tile: "+id+", "+x+", "+y+", "+z+", "+type+", "+face);
		this.id = id;
		this.tile_x = x;
		this.tile_y = y;
		this.tile_z = z;
		this.object_info = info;
		this.object_type = type;
		this.object_orientation = face;
	}

	public int getId() {
		return id;
	}
	
	public int getZ() {
		return tile_z;
	}
	
	public int getX() {
		return tile_x;
	}

	public int getY() {
		return tile_y;
	}

}
