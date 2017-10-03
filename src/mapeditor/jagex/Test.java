package mapeditor.jagex;

//import com.jhlabs.image.Gradient;

import mapeditor.jagex.rt3.GraphicsBuffer;
import mapeditor.jagex.rt4.Class26_Sub2_Sub1;
import sun.java2d.SunGraphicsEnvironment;

import java.awt.*;

/**
 * Created by IntelliJ IDEA. User: Administrator Date: 7/31/11 Time: 11:42 PM To
 * change this template use File | Ssettings | File Templates.
 */
public class Test extends Frame {
	static Class26_Sub2_Sub1 t = new Class26_Sub2_Sub1();
	static GraphicsBuffer g;

	public static void main(String[] args) {
		Test tt = new Test();
		g = new GraphicsBuffer(128 * 7, 128 * 7, tt);
		tt.show();
		t.method1033(128, 128, 32);
		int x = 0;
		int y = 0;
		for (int zz = 0; zz < 128 * 7 * 7 * 128; zz++)
			g.componentPixels[zz] = 0x00FF00;
		for (int z = 0; z < 32; z++) {
			for (int _x = 0; _x < 128; _x++) {
				for (int _y = 0; _y < 128; _y++) {
					int rg = t.data[((_x + (_y * 128) + (z * 128 * 128)) * 2) + 1];
					g.componentPixels[((x * 129) + _x) + ((y * 129) + _y) * g.canvasWidth] = rg << 16 | rg << 8 | rg;
				}
			}
			if ((z % 6) == 5) {
				x = 0;
				y++;
			} else
				x++;
		}
	}

	public void paint(Graphics g) {
		Test.g.drawGraphics(0, g, 0);
	}
}
