package mapeditor.jagex.rt3;


import mapeditor.jagex.rt3.util.collection.QueueNode;

public class OnDemandData extends QueueNode {

    public OnDemandData()
    {
        incomplete = true;
    }

    public int dataType;
    public byte[] buffer;
    public int ID;
    boolean incomplete;
    int loopCycle;
}
