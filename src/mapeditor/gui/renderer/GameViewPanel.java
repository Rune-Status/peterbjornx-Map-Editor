package mapeditor.gui.renderer;

import javax.swing.*;

import mapeditor.jagex.rt3.GameShell;

import java.awt.*;

/**
 * Created by IntelliJ IDEA. User: Peter Date: 6/24/11 Time: 7:15 PM Computer:
 * Peterbjornx-PC.rootdomain.asn.local (192.168.178.27) Our swing component
 * version of RSFrame
 */
public class GameViewPanel extends Canvas {

	public GameViewPanel() {
	}

	public void setGameShell(GameShell shell) {
		gameShell = shell;
		repaint();
	}

	/**
	 * Invoked by Swing to draw components. Applications should not invoke
	 * <code>paint</code> directly, but should instead use the <code>repaint</code>
	 * method to schedule the component for redrawing.
	 * <p/>
	 * This method actually delegates the work of painting to three protected
	 * methods: <code>paintComponent</code>, <code>paintBorder</code>, and
	 * <code>paintChildren</code>. They're called in the order listed to ensure that
	 * children appear on top of component itself. Generally speaking, the component
	 * and its children should not paint in the insets area allocated to the border.
	 * Subclasses can just override this method, as always. A subclass that just
	 * wants to specialize the UI (look and feel) delegate's <code>paint</code>
	 * method should just override <code>paintComponent</code>.
	 *
	 * @param g
	 *            the <code>Graphics</code> context in which to paint
	 * @see #paintComponent
	 * @see #paintBorder
	 * @see #paintChildren
	 * @see #getComponentGraphics
	 * @see #repaint
	 */
	@Override
	public void paint(Graphics g) {
		if (gameShell == null) {
			g.setColor(Color.red);
			g.fillRect(0, 0, getWidth(), getHeight());
		} else
			try {
				gameShell.paint(g);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
	}

	/**
	 * Calls <code>paint</code>. Doesn't clear the background but see
	 * <code>ComponentUI.update</code>, which is called by
	 * <code>paintComponent</code>.
	 *
	 * @param g
	 *            the <code>Graphics</code> context in which to paint
	 * @see #paint
	 * @see #paintComponent
	 * @see javax.swing.plaf.ComponentUI
	 */
	@Override
	public void update(Graphics g) {
		if (gameShell == null) {
			g.setColor(Color.red);
			g.fillRect(0, 0, getWidth(), getHeight());
		} else
			gameShell.update(g);
	}

	private GameShell gameShell = null;

	/**
	 * Creates a graphics context for this component. This method will return
	 * <code>null</code> if this component is currently not displayable.
	 *
	 * @return a graphics context for this component, or <code>null</code> if it has
	 *         none
	 * @see #paint
	 * @since JDK1.0
	 */
	@Override
	public Graphics getGraphics() {
		Graphics g = super.getGraphics();
		return g;
	}
}
