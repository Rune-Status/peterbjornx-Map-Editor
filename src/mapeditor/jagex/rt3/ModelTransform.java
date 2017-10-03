package mapeditor.jagex.rt3;

public class ModelTransform {

	public ModelTransform(Packet stream) {

		int amt = stream.g2();
		opcodes = new int[amt];
		skinList = new int[amt][];
		for (int j = 0; j < amt; j++)
			opcodes[j] = stream.g2();
		
		for (int j = 0; j < amt; j++)
			skinList[j] = new int[stream.g2()];
		
		for (int j = 0; j < amt; j++)
			for (int l = 0; l < skinList[j].length; l++)
				skinList[j][l] = stream.g2();
	}

	public final int[] opcodes;
	public final int[][] skinList;
}
