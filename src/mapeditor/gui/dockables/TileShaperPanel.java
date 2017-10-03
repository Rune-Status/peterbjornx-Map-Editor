package mapeditor.gui.dockables;

import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class TileShaperPanel extends javax.swing.JPanel {

	private static final long serialVersionUID = -7653550669202053234L;

	@SuppressWarnings("rawtypes")
	public static JList list = new JList();

	public static JLabel orientation_label = new JLabel();

	private final Map<String, ImageIcon> imageMap;

	public TileShaperPanel() {
		String[] nameList = new String[12];

		for (int i = 0; i < nameList.length; i++) {
			nameList[i] = "Shape " + i;

		}
		imageMap = createImageMap(nameList);
		list = new JList(nameList);
		list.setCellRenderer(new TileShapeRenderer());

		JScrollPane scroll = new JScrollPane(list);
		scroll.setPreferredSize(new Dimension(200, 500));
		list.setSelectedIndex(0);
		orientation_label.setText("Orientation: NORTH");
		orientation_label.setHorizontalAlignment(javax.swing.JTextField.CENTER);
		this.add(orientation_label);
		this.add(scroll);
		this.setVisible(true);
		disableComponenets();
	}

	public class TileShapeRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			label.setIcon(imageMap.get((String) value));
			label.setHorizontalTextPosition(JLabel.RIGHT);
			return label;
		}
	}

	private Map<String, ImageIcon> createImageMap(String[] list) {
		Map<String, ImageIcon> map = new HashMap<>();
		try {
			for (int i = 0; i < 12; i++) {
				map.put("Shape " + i, new ImageIcon("./data/shapes/" + i + ".jpg"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map;
	}

	public static void disableComponenets() {
		list.setEnabled(false);
		orientation_label.setEnabled(false);
	}

	public static void enableComponents() {
		list.setEnabled(true);
		orientation_label.setEnabled(true);
	}

	public static int getShape() {
		return list.getSelectedIndex();
	}

}