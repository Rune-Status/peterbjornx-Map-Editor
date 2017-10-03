/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapeditor.gui.dockables;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Map.Entry;

import mapeditor.Main;
import mapeditor.jagex.rt3.ObjectDef;
import mapeditor.misc.CategoryData;
import mapeditor.misc.MapObject;
import mapeditor.misc.ObjectscapeLogic;

/**
 *
 * @author Owner
 */
public class ObjectEditorPanel extends javax.swing.JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4327648681019486072L;
	public static int selectedObjectID;
	public static int selectedObjectType;

	/**
	 * Creates new form NewJPanel
	 */
	public ObjectEditorPanel() {
		initComponents();
		initListeners();
		disableComponents();

		// some default stuff
		selectedObjectID = CategoryData.getCategoryItemIDs(0)[menu_objCategoryIDs.getSelectedIndex()];

	}

	public void initListeners() {
		ObjectEditorPanel.label_objOrientationField
				.setText("" + ObjectscapeLogic.ORIENTATIONS[ObjectscapeLogic.placementOrientation]);

		loadButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				final int mapId = ObjectscapeLogic.getObjectscapeID(Main.selectedGlobalX, Main.selectedGlobalY);
				boolean found = false;
				for (Entry<Integer, ArrayList<MapObject>> entry : ObjectscapeLogic.objectTables.entrySet()) {
					if (entry.getKey() == mapId) {
						ArrayList<MapObject> objects = entry.getValue();
						for (MapObject m : objects) {
							if (m.x == Main.selectedGlobalX && m.y == Main.selectedGlobalY
									&& m.plane == Main.heightLevel) {
								selectedObjectID = m.objectId;
								selectedObjectType = m.type;
								try {
									ObjectDef o = new ObjectDef();
									o = ObjectDef.forID(m.objectId);
									if (o != null)
										label_nameField.setText(o.name);
									else
										label_nameField.setText("none");

									System.out.print("Models: ");
									for (int i : o.object_model_ids)
										System.out.print(i + ", ");
									System.out.println();

								} catch (Exception e) {

								}
								ObjectscapeLogic.placementOrientation = m.face;
								label_objIDField.setText("" + selectedObjectID);
								label_objTypeField.setText("" + selectedObjectType);
								ObjectEditorPanel.label_objOrientationField.setText(
										"" + ObjectscapeLogic.ORIENTATIONS[ObjectscapeLogic.placementOrientation]);
								found = true;

							}
						}
					}
				}
				if (!found)
					System.out.println("No object exist at selected tile. ");
			}
		});

		menu_objCategories.setPreferredSize(new Dimension(160, 20));
		menu_objCategories.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// UPDATE CATEGORY FOR SELECTION
				int category = menu_objCategories.getSelectedIndex();
				menu_objCategoryIDs.setSelectedIndex(0);
				menu_objCategoryIDs
						.setModel(new javax.swing.DefaultComboBoxModel(CategoryData.getCategoryItemNames(category)));
				selectedObjectID = CategoryData.getCategoryItemIDs(category)[0];
				label_objIDField.setText("" + selectedObjectID);
				try {
					ObjectDef o = new ObjectDef();
					o = ObjectDef.forID(selectedObjectID);
					if (o != null)
						label_nameField.setText(o.name);
					else
						label_nameField.setText("none");

					System.out.print("Models: ");
					for (int i : o.object_model_ids)
						System.out.print(i + ", ");
					System.out.println();

				} catch (Exception e) {

				}
			}
		});

		menu_objCategoryIDs.setPreferredSize(new Dimension(160, 20));
		menu_objCategoryIDs.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int category = menu_objCategories.getSelectedIndex();
				selectedObjectID = CategoryData.getCategoryItemIDs(category)[menu_objCategoryIDs.getSelectedIndex()];
				label_objIDField.setText("" + selectedObjectID);
				try {
					ObjectDef o = new ObjectDef();
					o = ObjectDef.forID(selectedObjectID);
					if (o != null)
						label_nameField.setText(o.name);
					else
						label_nameField.setText("none");

					System.out.print("Models: ");
					for (int i : o.object_model_ids)
						System.out.print(i + ", ");
					System.out.println();

				} catch (Exception e) {

				}
			}

		});

		menu_objTypes.setPreferredSize(new Dimension(160, 20));
		menu_objTypes.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				selectedObjectType = menu_objTypes.getSelectedIndex();
				label_objTypeField.setText("" + selectedObjectType);

			}
		});

		input_objID.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				int inputID = 0;

				try {
					inputID = Integer.parseInt(input_objID.getText());
				} catch (NumberFormatException e) {
					System.out.println("Number format exception!");
				} catch (Exception e) {
					System.out.println("Unhandled exception!");
				}

				if (inputID > ObjectDef.objectLimit)
					return;

				selectedObjectID = inputID;
				label_objIDField.setText("" + selectedObjectID);

				try {
					ObjectDef o = new ObjectDef();
					o = ObjectDef.forID(selectedObjectID);
					if (o != null)
						label_objName.setText(o.name);
					else
						label_objName.setText("none");

					System.out.print("Models: ");
					for (int i : o.object_model_ids)
						System.out.print(i + ", ");
					System.out.println();

				} catch (Exception e) {

				}
			}
		});

		asButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				int inputID = 0;

				try {
					inputID = Integer.parseInt(input_objID.getText());
				} catch (NumberFormatException e) {
					System.out.println("Number format exception!");
				} catch (Exception e) {
					System.out.println("Unhandled exception!");
				}

				if (inputID > ObjectDef.objectLimit)
					return;

				selectedObjectID = inputID;
				label_objIDField.setText("" + selectedObjectID);
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void updateCategories(int categories) {
		menu_objCategoryIDs
				.setModel(new javax.swing.DefaultComboBoxModel(CategoryData.getCategoryItemNames(categories)));
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	public static void disableComponents() {
		label_objID.setEnabled(false);
		label_objType.setEnabled(false);
		label_objOrientation.setEnabled(false);
		label_objIDField.setEnabled(false);
		label_objTypeField.setEnabled(false);
		label_objOrientationField.setEnabled(false);
		label_objCategories.setEnabled(false);
		menu_objCategories.setEnabled(false);
		label_objCategoryIDs.setEnabled(false);
		menu_objCategoryIDs.setEnabled(false);
		label_objTypes.setEnabled(false);
		menu_objTypes.setEnabled(false);
		label_objIDInput.setEnabled(false);
		input_objID.setEnabled(false);
		label_objName.setEnabled(false);
		label_nameField.setEnabled(false);
	}

	public static void enableComponents() {
		label_objID.setEnabled(true);
		label_objType.setEnabled(true);
		label_objOrientation.setEnabled(true);
		label_objIDField.setEnabled(true);
		label_objTypeField.setEnabled(true);
		label_objOrientationField.setEnabled(true);
		label_objCategories.setEnabled(true);
		menu_objCategories.setEnabled(true);
		label_objCategoryIDs.setEnabled(true);
		menu_objCategoryIDs.setEnabled(true);
		label_objTypes.setEnabled(true);
		menu_objTypes.setEnabled(true);
		label_objIDInput.setEnabled(true);
		input_objID.setEnabled(true);
		label_objName.setEnabled(true);
		label_nameField.setEnabled(true);
	}

	private void initComponents() {

		loadButton = new javax.swing.JButton();
		asButton = new javax.swing.JButton();

		label_objID = new javax.swing.JLabel();
		label_objType = new javax.swing.JLabel();
		label_objOrientation = new javax.swing.JLabel();
		label_objIDField = new javax.swing.JLabel();
		label_objTypeField = new javax.swing.JLabel();
		label_objOrientationField = new javax.swing.JLabel();
		label_objCategories = new javax.swing.JLabel();
		menu_objCategories = new javax.swing.JComboBox();
		label_objCategoryIDs = new javax.swing.JLabel();
		menu_objCategoryIDs = new javax.swing.JComboBox();
		label_objTypes = new javax.swing.JLabel();
		menu_objTypes = new javax.swing.JComboBox();
		label_objIDInput = new javax.swing.JLabel();
		input_objID = new javax.swing.JTextField();
		label_objName = new javax.swing.JLabel();
		label_nameField = new javax.swing.JLabel();

		loadButton.setText("Load from selected tile");
		loadButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				loadButtonActionPerformed(evt);
			}
		});

		asButton.setText("Create Auto-Structure");
		asButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				asButtonActionPerformed(evt);
			}
		});

		label_objID.setText("Object ID:");

		label_objType.setText("Object Type:");

		label_objOrientation.setText("Object Orientation:");

		label_objIDField.setText("0");

		label_objTypeField.setText("0");

		label_objOrientationField.setText("SOUTH");

		label_objCategories.setText("Object categories:");

		menu_objCategories.setModel(new javax.swing.DefaultComboBoxModel(CategoryData.getCategories()));

		label_objCategoryIDs.setText("Object category items IDs:");

		menu_objCategoryIDs.setModel(new javax.swing.DefaultComboBoxModel(CategoryData.getCategoryItemNames(0)));

		label_objTypes.setText("Object types:");

		menu_objTypes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0:   STRAIGHT_WALLL",
				"1:   DIAGONAL_WALL", "2:   STRAIGHT_WALL_CORNER", "3:   DIAGONAL_WALL_CORNER",
				"4:   STRAIGHT_INSIDE_WALL_DECOR", "5:   STRAIGHT_OUTSIDE_WALL_DECOR",
				"6:   DIAGONAL_OUTSIDE_WALL_DECOR", "7:   DIAGONAL_INSIDE_WALL_DECOR",
				"8:   DIAGONAL_INSIDE_WALL_DECOR", "9:   DIAGONAL_WALLS_FENCES", "10:   WORLD_OBJECT",
				"11:   GROUND_DECOR", "12:   STRAIGHT_SLOPED_ROOF", "13:   DIAGONAL_SLOPED_ROOF",
				"14:   DIAGONAL_SLOPED_CORNER_CONNECTING_ROOF", "15:   STRAIGHT_SLOPED_CORNER_CONNECTING_ROOF",
				"16:   STRAIGHT_SLOPED_CORNER_ROOF", "17:   STRAIGHT_FLAT_TOP_ROOFS", "18:   STRAIGHT_BOTTOM_EDGE_ROOF",
				"19:   DIAGONAL_BOTTOM_EDGE_CONNECTING_ROOF", "20:   STRAIGHT_BOTTOM_EDGE_CONNECTING_ROOF",
				"21:   STRAIGHT_BOTTOM_EDGE_CONNECTING_CORNER_ROOF", "22:   GROUND_DECOR/MAP_SIGNS" }));

		label_objIDInput.setText("Object ID input:");

		input_objID.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		input_objID.setText("0");

		label_objName.setText("Object Cache Name:");

		label_nameField.setText("null");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								layout.createSequentialGroup().addContainerGap()
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(loadButton, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(menu_objCategoryIDs,
														javax.swing.GroupLayout.Alignment.TRAILING, 0,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(menu_objCategories, 0,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addGroup(layout
														.createSequentialGroup().addGroup(layout
																.createParallelGroup(
																		javax.swing.GroupLayout.Alignment.TRAILING)
																.addComponent(label_objID,
																		javax.swing.GroupLayout.Alignment.LEADING)
																.addComponent(label_objType,
																		javax.swing.GroupLayout.Alignment.LEADING)
																.addComponent(label_objCategories,
																		javax.swing.GroupLayout.Alignment.LEADING)
																.addComponent(label_objCategoryIDs,
																		javax.swing.GroupLayout.Alignment.LEADING)
																.addComponent(label_objTypes,
																		javax.swing.GroupLayout.Alignment.LEADING)
																.addComponent(label_objIDInput,
																		javax.swing.GroupLayout.Alignment.LEADING)
																.addComponent(
																		label_objName,
																		javax.swing.GroupLayout.Alignment.LEADING))
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42,
																Short.MAX_VALUE)
														.addGroup(layout
																.createParallelGroup(
																		javax.swing.GroupLayout.Alignment.LEADING)
																.addComponent(label_nameField,
																		javax.swing.GroupLayout.Alignment.TRAILING)
																.addComponent(label_objTypeField,
																		javax.swing.GroupLayout.Alignment.TRAILING)
																.addComponent(label_objIDField,
																		javax.swing.GroupLayout.Alignment.TRAILING)))
												.addGroup(layout.createSequentialGroup()
														.addComponent(label_objOrientation)
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(label_objOrientationField))
												.addComponent(menu_objTypes, 0, javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(input_objID).addComponent(asButton,
														javax.swing.GroupLayout.Alignment.TRAILING,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap().addComponent(loadButton)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(label_objID).addComponent(label_objIDField))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(label_objType).addComponent(label_objTypeField))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(label_objOrientation).addComponent(label_objOrientationField))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(label_objName).addComponent(label_nameField))
				.addGap(12, 12, 12).addComponent(label_objCategories)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(menu_objCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 20,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addGap(18, 18, 18).addComponent(label_objCategoryIDs)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(menu_objCategoryIDs, javax.swing.GroupLayout.PREFERRED_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addGap(18, 18, 18).addComponent(label_objTypes)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(menu_objTypes, javax.swing.GroupLayout.PREFERRED_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addGap(18, 18, 18).addComponent(label_objIDInput)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(input_objID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addGap(29, 29, 29).addComponent(asButton).addContainerGap(59, Short.MAX_VALUE)));
	}// </editor-fold>

	private void loadButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// AutoStructureFrame.init();

	}

	private void asButtonActionPerformed(java.awt.event.ActionEvent evt) {
		AutoStructureFrame.init();

	}

	// Variables declaration - do not modify
	private static javax.swing.JTextField input_objID;
	private static javax.swing.JLabel label_nameField;
	private static javax.swing.JLabel label_objCategories;
	private static javax.swing.JLabel label_objCategoryIDs;
	private static javax.swing.JLabel label_objID;
	private static javax.swing.JLabel label_objIDField;
	private static javax.swing.JLabel label_objIDInput;
	private static javax.swing.JLabel label_objName;
	private static javax.swing.JLabel label_objOrientation;
	public static javax.swing.JLabel label_objOrientationField;
	private static javax.swing.JLabel label_objType;
	private static javax.swing.JLabel label_objTypeField;
	private static javax.swing.JLabel label_objTypes;
	private static javax.swing.JButton loadButton;
	private static javax.swing.JButton asButton;

	@SuppressWarnings("rawtypes")
	private static javax.swing.JComboBox menu_objCategories;
	@SuppressWarnings("rawtypes")
	private static javax.swing.JComboBox menu_objCategoryIDs;
	@SuppressWarnings("rawtypes")
	private static javax.swing.JComboBox menu_objTypes;
	// End of variables declaration
}
