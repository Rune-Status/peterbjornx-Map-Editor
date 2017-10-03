package mapeditor.jagex.rt3.model;


import mapeditor.jagex.rt3.*;
import mapeditor.jagex.rt3.util.collection.Node;

public class Tile extends Node {

	public boolean selected = false;
    public boolean highlighted = false;
	public int anInt1310;

    public Tile(int z, int x, int y)
    {
        interactiveObjects = new InteractiveObject[5];
        anIntArray1319 = new int[5];
        tileZ = this.anInt1310 = this.tileZ = z;
        tileX = x;
        tileY = y;
    }

    public final int tileX;
    public final int tileY;
    public int tileZ;
    		
    public PlainTile myPlainTile;
    public ShapedTile shapedTile;
    public WallObject wallObject;
    public WallDecoration wallDecoration;
    public GroundDecoration groundDecoration;
    public GroundItemTile groundItemTile;
    public int entityCount;
    public InteractiveObject[] interactiveObjects;
    public final int[] anIntArray1319;
    public int anInt1320;
    public int logicHeight;
    public boolean aBoolean1322;
    public boolean aBoolean1323;
    public boolean aBoolean1324;
    public int anInt1325;
    public int anInt1326;
    public int anInt1327;
    public int anInt1328;
    public Tile tileBelow0;
}
