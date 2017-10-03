package mapeditor.misc;

public class MapObject {
	public int objectId = -1;
	public int x = -1;
	public int y = -1;
	public byte face = -1;
	public byte type = -1;
	public byte plane = -1;

	public MapObject(int objectId, int x, int y, byte face, byte type, byte plane) {
		this.objectId = objectId;
		this.x = x;
		this.y = y;
		this.face = face;
		this.type = type;
		this.plane = plane;
	}

	public void print() {
		System.out.println("objectId = " + objectId + ", x = " + x + ", y = " + y + ", face = " + face + ", type = " + type + ", plane = " + plane);
	}
}
