/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapeditor.gui.dockables;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import mapeditor.Main;
import mapeditor.gui.renderer.algorithms.ExclusiveWalls;
import mapeditor.gui.renderer.algorithms.InclusiveWalls;
import mapeditor.gui.renderer.algorithms.Roof;
import mapeditor.misc.CategoryData;

/**
 *
 * @author Owner
 */
public class AutoStructureFrame extends javax.swing.JFrame {

	public static int autoWallsID = 0;
	public static int autoFloors = 1;
	public static boolean inclusive = false;
	public static int autoStructureWidth = 0;
	public static int autoStructureHeight = 0;

	public static void main(String[] args) {
		CategoryData.load();
		init();
	}

	public static void init() {

		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(AutoStructureFrame.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(AutoStructureFrame.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(AutoStructureFrame.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(AutoStructureFrame.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		}
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new AutoStructureFrame().setVisible(true);
			}
		});
	}

	/**
	 * Creates new form AutoStructureFrame
	 */
	public AutoStructureFrame() {
		initComponents();
		initData();
		initListeners();
	}

	public void create() {
		System.out.println("WALL: " + autoWallsID);
		System.out.println("ROOF: " + selectedRoofData[0] + ", " + selectedRoofData[1]);
		System.out.println("FLOORS: " + autoFloors);

		if (autoStructureWidth < 1 || autoStructureHeight < 1 || autoStructureWidth > 100
				|| autoStructureHeight > 100) {
			JOptionPane.showMessageDialog(null, "Illegal area size.", "", JOptionPane.ERROR_MESSAGE);
			return;
		}

		/*
		 * if (EditorMain.heightLevel != 0) { JOptionPane.showMessageDialog(null,
		 * "Must place from HL 0.", "", JOptionPane.ERROR_MESSAGE); return; }
		 */

		int hl = Main.heightLevel;

		int coordX = Main.selectedGlobalX;
		int coordY = Main.selectedGlobalY;
		if (coordX <= 1 || coordY <= 1) {
			JOptionPane.showMessageDialog(null, "Must select a base tile.", "", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (checkbox_inclusive.isSelected()) {
			if (autoWallsID == 10053) {
				InclusiveWalls.del(coordX, coordY, autoStructureWidth, autoStructureHeight, autoWallsID, hl);
				System.out.println("Doing deletion");
				setVisible(false);
				dispose();
				return;
			}
			/**
			 * Inclusive structures
			 */
			System.out.println(coordX + ", " + coordY + ", " + autoStructureWidth + ", " + autoStructureHeight + ", "
					+ autoWallsID + selectedRoofData[0] + ", " + selectedRoofData[1]);
			if (autoFloors == 1) {
				InclusiveWalls.createInclusiveWalls(coordX, coordY, autoStructureWidth, autoStructureHeight,
						autoWallsID, hl);

				if (autoWallsID == 6590) {
					InclusiveWalls.createInclusiveWalls(coordX, coordY, autoStructureWidth, autoStructureHeight, 6600,
							1);
				}
				if (selectedRoofData[0] != -1) {
					Roof.generateRoof(coordX, coordY, autoStructureWidth, autoStructureHeight, selectedRoofData[1],
							selectedRoofData[0], hl + 1);
				}
			} else if (autoFloors == 2) {
				InclusiveWalls.createInclusiveWalls(coordX, coordY, autoStructureWidth, autoStructureHeight,
						autoWallsID, hl);
				InclusiveWalls.createInclusiveWalls(coordX, coordY, autoStructureWidth, autoStructureHeight,
						autoWallsID, hl + 1);
				if (selectedRoofData[0] != -1 && hl <= 2) {
					Roof.generateRoof(coordX, coordY, autoStructureWidth, autoStructureHeight, selectedRoofData[1],
							selectedRoofData[0], hl + 2);
				}
			} else if (autoFloors == 3) {
				InclusiveWalls.createInclusiveWalls(coordX, coordY, autoStructureWidth, autoStructureHeight,
						autoWallsID, hl);
				InclusiveWalls.createInclusiveWalls(coordX, coordY, autoStructureWidth, autoStructureHeight,
						autoWallsID, hl + 1);
				InclusiveWalls.createInclusiveWalls(coordX, coordY, autoStructureWidth, autoStructureHeight,
						autoWallsID, hl + 2);
				if (selectedRoofData[0] != -1 && hl <= 1) {
					Roof.generateRoof(coordX, coordY, autoStructureWidth, autoStructureHeight, selectedRoofData[1],
							selectedRoofData[0], hl + 3);
				}
			}
		} else {
			/**
			 * Exclusive structures
			 */
			System.out.println(coordX + ", " + coordY + ", " + autoStructureWidth + ", " + autoStructureHeight + ", "
					+ autoWallsID + selectedRoofData[0] + ", " + selectedRoofData[1]);
			if (autoFloors == 1) {
				ExclusiveWalls.createExclusiveWalls(coordX, coordY, autoStructureWidth, autoStructureHeight,
						autoWallsID, 0);
				if (selectedRoofData[0] != -1) {
					Roof.generateRoof(coordX, coordY, autoStructureWidth, autoStructureHeight, selectedRoofData[1],
							selectedRoofData[0], 1);
				}
			} else if (autoFloors == 2) {
				ExclusiveWalls.createExclusiveWalls(coordX, coordY, autoStructureWidth, autoStructureHeight,
						autoWallsID, 0);
				ExclusiveWalls.createExclusiveWalls(coordX, coordY, autoStructureWidth, autoStructureHeight,
						autoWallsID, 1);
				if (selectedRoofData[0] != -1) {
					Roof.generateRoof(coordX, coordY, autoStructureWidth, autoStructureHeight, selectedRoofData[1],
							selectedRoofData[0], 2);
				}
			} else if (autoFloors == 3) {
				ExclusiveWalls.createExclusiveWalls(coordX, coordY, autoStructureWidth, autoStructureHeight,
						autoWallsID, 0);
				ExclusiveWalls.createExclusiveWalls(coordX, coordY, autoStructureWidth, autoStructureHeight,
						autoWallsID, 1);
				ExclusiveWalls.createExclusiveWalls(coordX, coordY, autoStructureWidth, autoStructureHeight,
						autoWallsID, 2);
				if (selectedRoofData[0] != -1) {
					Roof.generateRoof(coordX, coordY, autoStructureWidth, autoStructureHeight, selectedRoofData[1],
							selectedRoofData[0], 3);
				}
			}

		}
		setVisible(false);
		dispose();
	}

	/**
	 * Initialize the data for the menus
	 */
	public static String[] wallOptions = new String[255];
	public static String[] roofOptions = { "NONE", "WOODEN ROOF", "SHINGLE ROOF", "LIGHT SHINGLE ROOF", "CONCRETE ROOF",
			"STRAW ROOF" };

	public static int selectedRoofData[] = new int[2];

	public void initData() {
		wallOptions = CategoryData.getWallsArray();
		menu_roofType.setModel(new javax.swing.DefaultComboBoxModel(roofOptions));
		menu_wallType.setModel(new javax.swing.DefaultComboBoxModel(wallOptions));

		int width = Math.abs(Main.selectedGlobalX2 - Main.selectedGlobalX) + 1;
		int height = Math.abs(Main.selectedGlobalY2 - Main.selectedGlobalY) + 1;

		if (width < 0 || height < 0) {
		}
		input_structureWidth.setText("" + width);
		input_structureHeight.setText("" + height);

		autoStructureWidth = Integer.parseInt(input_structureWidth.getText());
		autoStructureHeight = Integer.parseInt(input_structureHeight.getText());

	}

	public void initListeners() {
		menu_roofType.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int selection = menu_roofType.getSelectedIndex();
				switch (selection) {
				// they all have a edge id and a fill id
				case 0:
					selectedRoofData[0] = -1;
					selectedRoofData[1] = -1;
					break;

				case 1:
					selectedRoofData[0] = 1933;
					selectedRoofData[1] = 1793;
					break;

				case 2:
					selectedRoofData[0] = 1924;
					selectedRoofData[1] = 1790;
					break;

				case 3:
					selectedRoofData[0] = 1925;
					selectedRoofData[1] = 1791;
					break;

				case 4:
					selectedRoofData[0] = 1604;
					selectedRoofData[1] = 1792;
					break;

				case 5:
					selectedRoofData[0] = 3690;
					selectedRoofData[1] = 4242;
					break;
				}
				System.out.println("Data: " + selectedRoofData[0] + ", " + selectedRoofData[1]);
			}

		});
		menu_wallType.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int selection = menu_wallType.getSelectedIndex();
				if (selection == 0) {
					autoWallsID = -1;
				} else {
					/**
					 * STUPID WAY OF DOING IT BUT WHATEVER LOL
					 */
					String params[] = wallOptions[selection].split(" - ");
					try {
						autoWallsID = Integer.parseInt(params[0]);
					} catch (Exception e) {
						System.out.println("Unable to determine ID.");
					}
				}
			}

		});

		menu_floors.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				autoFloors = menu_floors.getSelectedIndex() + 1;
			}

		});

		input_structureWidth.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				autoStructureWidth = Integer.parseInt(input_structureWidth.getText());
			}

		});

		input_structureHeight.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				autoStructureHeight = Integer.parseInt(input_structureWidth.getText());
			}

		});

		button_create.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				create();
			}

		});

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		label_wallType = new javax.swing.JLabel();
		menu_wallType = new javax.swing.JComboBox();
		menu_roofType = new javax.swing.JComboBox();
		label_structureWidth = new javax.swing.JLabel();
		input_structureWidth = new javax.swing.JTextField();
		input_height = new javax.swing.JLabel();
		input_structureHeight = new javax.swing.JTextField();
		button_create = new javax.swing.JButton();
		checkbox_inclusive = new javax.swing.JCheckBox();
		menu_floors = new javax.swing.JComboBox();
		label_roofType1 = new javax.swing.JLabel();
		label_roofType3 = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		label_wallType.setText("Wall type:");

		menu_wallType.setModel(
				new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

		menu_roofType.setModel(
				new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

		label_structureWidth.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		label_structureWidth.setText("Structure width:");

		input_structureWidth.setHorizontalAlignment(javax.swing.JTextField.CENTER);
		input_structureWidth.setText("0");

		input_height.setText("Structure height:");

		input_structureHeight.setHorizontalAlignment(javax.swing.JTextField.CENTER);
		input_structureHeight.setText("0");

		button_create.setText("Create");

		checkbox_inclusive.setText("Inclusive");

		menu_floors.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4" }));

		label_roofType1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		label_roofType1.setText("Floors:");

		label_roofType3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		label_roofType3.setText("Roof type:");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
						.createSequentialGroup()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(label_wallType))
								.addGroup(
										layout.createSequentialGroup().addContainerGap().addComponent(label_roofType3))
								.addGroup(
										layout.createSequentialGroup().addContainerGap().addComponent(label_roofType1))
								.addGroup(layout.createSequentialGroup().addContainerGap()
										.addComponent(label_structureWidth))
								.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(input_height)))
						.addGap(0, 117, Short.MAX_VALUE))
						.addGroup(layout.createSequentialGroup().addContainerGap()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(layout.createSequentialGroup().addComponent(checkbox_inclusive)
												.addGap(0, 0, Short.MAX_VALUE))
										.addComponent(input_structureHeight, javax.swing.GroupLayout.Alignment.TRAILING)
										.addComponent(input_structureWidth, javax.swing.GroupLayout.Alignment.TRAILING)
										.addComponent(menu_floors, javax.swing.GroupLayout.Alignment.TRAILING, 0,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(menu_wallType, javax.swing.GroupLayout.Alignment.TRAILING, 0,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(menu_roofType, javax.swing.GroupLayout.Alignment.TRAILING, 0,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(button_create, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
				.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap().addComponent(label_wallType).addGap(1, 1, 1)
				.addComponent(menu_wallType, javax.swing.GroupLayout.PREFERRED_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(label_roofType3)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(menu_roofType, javax.swing.GroupLayout.PREFERRED_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(label_roofType1)
				.addGap(4, 4, 4)
				.addComponent(menu_floors, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addComponent(label_structureWidth).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(input_structureWidth, javax.swing.GroupLayout.PREFERRED_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(input_height)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(input_structureHeight, javax.swing.GroupLayout.PREFERRED_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(checkbox_inclusive)
				.addGap(18, 18, 18).addComponent(button_create).addContainerGap(50, Short.MAX_VALUE)));

		pack();
	}// </editor-fold>

	// Variables declaration - do not modify
	private javax.swing.JButton button_create;
	private static javax.swing.JCheckBox checkbox_inclusive;
	private javax.swing.JLabel input_height;
	private javax.swing.JTextField input_structureHeight;
	private javax.swing.JTextField input_structureWidth;
	private javax.swing.JLabel label_roofType1;
	private javax.swing.JLabel label_roofType3;
	private javax.swing.JLabel label_structureWidth;
	private javax.swing.JLabel label_wallType;
	private javax.swing.JComboBox menu_floors;
	private javax.swing.JComboBox menu_roofType;
	private javax.swing.JComboBox menu_wallType;
	// End of variables declaration
}
