package mapeditor.jagex.rt4;

import java.nio.ByteBuffer;

public class VertexBufferPointer {
	public boolean upToDate = false;
	public ByteBuffer array;
	public int offset = 0;
	public int stride = 0;
	public VertexBuffer vertexBuffer;
}
