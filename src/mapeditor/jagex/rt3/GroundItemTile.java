package mapeditor.jagex.rt3;

import org.peterbjornx.pgl2.model.Node;

import mapeditor.jagex.rt3.model.entity.Entity;

//Object4
//rs2.GroundItemTile: a tile with a groundItem on it.
public class GroundItemTile {

    public Node secondNode;
    public Node firstNode;
    public Node thirdNode;

    GroundItemTile()
    {
    }

    int zPos;//45
    int xPos;//46
    int yPos;//47
    Entity firstGroundItem;//30_2_4_48
    Entity secondGroundItem;
    Entity thirdGroundItem;
    int uid;
    int anInt52;//todo - what is this O_O -- top item model?
}
