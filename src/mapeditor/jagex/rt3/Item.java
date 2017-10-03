package mapeditor.jagex.rt3;


import mapeditor.jagex.rt3.config.ItemDef;
import mapeditor.jagex.rt3.model.entity.Entity;

public class Item extends Entity {

    public final Model getRotatedModel()
    {
        ItemDef itemDef = ItemDef.forID(ID);
            return itemDef.getModel(itemCount);
    }

    public Item()
    {
    }

    public int ID;
    public int x;
	public int y;
	public int itemCount;
}
