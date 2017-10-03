package mapeditor.jagex.rt3;

public class Animation {

	public static void initialize(int i) {
		aAnimationArray635 = new mapeditor.jagex.rt3.Animation[i + 1];
		aBooleanArray643 = new boolean[i + 1];
		for (int j = 0; j < i + 1; j++)
			aBooleanArray643[j] = true;
	}

	public static void load(byte[] abyte0) {
		try {
			Packet stream = new Packet(abyte0);
			ModelTransform modelTransform = new ModelTransform(stream);
			int k1 = stream.g2();
			int ai[] = new int[500];
			int ai1[] = new int[500];
			int ai2[] = new int[500];
			int ai3[] = new int[500];
			for (int l1 = 0; l1 < k1; l1++) {
				int i2 = stream.g2();
				Animation animationFrame = aAnimationArray635[i2] = new Animation();
				animationFrame.displayLength = stream.g1();
				animationFrame.myModelTransform = modelTransform;
				int j2 = stream.g1();
				int k2 = -1;
				int l2 = 0;
				for (int i3 = 0; i3 < j2; i3++) {
					int j3 = stream.g1();
					if (j3 > 0) {
						if (modelTransform.opcodes[i3] != 0) {
							for (int l3 = i3 - 1; l3 > k2; l3--) {
								if (modelTransform.opcodes[l3] != 0)
									continue;
								ai[l2] = l3;
								ai1[l2] = 0;
								ai2[l2] = 0;
								ai3[l2] = 0;
								l2++;
								break;
							}

						}
						ai[l2] = i3;
						char c = '\0';
						if (modelTransform.opcodes[i3] == 3)
							c = '\200';
						if ((j3 & 1) != 0)
							ai1[l2] = stream.readShort2();
						else
							ai1[l2] = c;
						if ((j3 & 2) != 0)
							ai2[l2] = stream.readShort2();
						else
							ai2[l2] = c;
						if ((j3 & 4) != 0)
							ai3[l2] = stream.readShort2();
						else
							ai3[l2] = c;
						k2 = i3;
						l2++;
						if (modelTransform.opcodes[i3] == 5)
							aBooleanArray643[i2] = false;
					}
				}

				animationFrame.stepCount = l2;
				animationFrame.opcodeLinkTable = new int[l2];
				animationFrame.xOffset = new int[l2];
				animationFrame.yOffset = new int[l2];
				animationFrame.zOffset = new int[l2];
				for (int k3 = 0; k3 < l2; k3++) {
					animationFrame.opcodeLinkTable[k3] = ai[k3];
					animationFrame.xOffset[k3] = ai1[k3];
					animationFrame.yOffset[k3] = ai2[k3];
					animationFrame.zOffset[k3] = ai3[k3];
				}

			}
		} catch (Exception e) {

		}
	}

	public static void clearCache() {
		aAnimationArray635 = null;
	}

	public static Animation forID(int j) {
		if (aAnimationArray635 == null)
			return null;
		else
			return aAnimationArray635[j];

	}

	public static boolean isNullFrame(int frame) {
		return frame == -1;
	}

	private Animation() {

	}

	public static byte[][] frameData = null;
	public static byte[][] skinData = null;
	private static Animation[] aAnimationArray635;
	public int displayLength;
	public ModelTransform myModelTransform;
	public int stepCount;
	public int opcodeLinkTable[];
	public int xOffset[];
	public int yOffset[];
	public int zOffset[];
	private static boolean[] aBooleanArray643;

}
