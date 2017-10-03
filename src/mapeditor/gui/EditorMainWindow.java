package mapeditor.gui;

import info.clearthought.layout.TableLayout;
import mapeditor.Main;
import mapeditor.gui.dockables.EditorToolbar;
import mapeditor.gui.dockables.FloorEditorWindow;
import mapeditor.gui.dockables.FloorTypeSelection;
import mapeditor.gui.dockables.ObjectEditorPanel;
import mapeditor.gui.dockables.SettingsBrushEditor;
import mapeditor.gui.dockables.TileShaperPanel;
import mapeditor.gui.dockables.ToolSelectionBar;
import mapeditor.gui.renderer.GameViewPanel;
import mapeditor.gui.renderer.MapViewPanel;
import mapeditor.misc.ObjectscapeLogic;

import org.noos.xing.mydoggy.*;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;

import com.bulenkov.darcula.DarculaLaf;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicLookAndFeel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by IntelliJ IDEA. User: Peter Date: 6/24/11 Time: 5:27 PM Computer:
 * Peterbjornx-PC.rootdomain.asn.local (192.168.178.27)
 */
public class EditorMainWindow implements MouseWheelListener {
	private JFrame rootFrame;
	private ToolWindowManager toolWindowManager;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem openCacheMenuItem;
	private JMenuItem reRenderSceneItem;
	private JMenuItem openMapMenuItem;
	private JMenuItem saveAndPackFloorsItem;

	private JMenuItem saveFloorsItem;
	private JMenuItem saveAndPackObjectsItem;
	private JMenuItem saveObjectsItem;
	private JMenuItem exitMenuItem;
	private JMenuItem preferencesMenuItem;
	private JMenuItem overridePackItem;
	private JMenu editMenu;
	private JMenuItem copyTileMenuItem;
	private JMenuItem pasteTileMenuItem;

	private ObjectEditorPanel objectEditorPanel;
	private TileShaperPanel tileShaperPanel;

	private ToolSelectionBar toolSelectionBar;
	private FloorTypeSelection floorTypeSelectionWindow;
	private GameViewPanel gameViewPanel;
	private FloorEditorWindow floorEditorWindow;
	private SettingsBrushEditor settingsBrushEditorWindow;
	private MapViewPanel mapViewPanel;
	private EditorToolbar editorToolbar;
	private Main editor;

	public EditorMainWindow() {
		initGUI();
		rootFrame.addMouseWheelListener(this);

	}

	private void initGUI() {
		BasicLookAndFeel darcula = new DarculaLaf();
		try {
			UIManager.setLookAndFeel(darcula);
		} catch (UnsupportedLookAndFeelException e) {
			;
		}
		rootFrame = new JFrame("Map Editor - By Peterbjornx and Invision");
		rootFrame.setExtendedState(rootFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		rootFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initMenus();
		rootFrame.getContentPane().setLayout(new TableLayout(new double[][] { { 0, -1, 0 }, { 0, -1, 0 } }));
		initToolWindows();
		ContentManager contentManager = toolWindowManager.getContentManager();
		gameViewPanel = new GameViewPanel();
		Content content = contentManager.addContent("lol", "3D Viewport", null, gameViewPanel);
	}

	private void initToolWindows() {
		MyDoggyToolWindowManager myDoggyToolWindowManager = new MyDoggyToolWindowManager();
		toolWindowManager = myDoggyToolWindowManager;

		toolSelectionBar = new ToolSelectionBar();
		toolSelectionBar.getMainPane().setSize(toolSelectionBar.getMainPane().WIDTH, 60);
		toolWindowManager.registerToolWindow("Tools", null, null, toolSelectionBar.getMainPane(), ToolWindowAnchor.TOP);
		editorToolbar = new EditorToolbar();
		toolWindowManager.registerToolWindow("Toolbar", null, null, editorToolbar.getMainPane(),
				ToolWindowAnchor.BOTTOM);
		editorToolbar.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (editorToolbar.getHeightlevel() != editor.getHeightLevel())
					editor.setHeightLevel(editorToolbar.getHeightlevel());
				editor.setShowAllHLs(editorToolbar.getShowAllHLs());
				editor.setTargetHeight(editorToolbar.getTargetHeight());
				editorToolbar.updateHeight();
				if (e != null)
					editor.setRenderSettings(editorToolbar.getSettings());
			}
		});
		floorTypeSelectionWindow = new FloorTypeSelection();
		floorTypeSelectionWindow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((!floorEditorWindow.isDirty()) || JOptionPane.showConfirmDialog(rootFrame,
						"Are you sure you want to lose all changes to the edited floor?", "RuneScape Map Editor",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION)
					if (floorTypeSelectionWindow.getSelectedFloorId() != -1)
						floorEditorWindow.setCurrentFloor(floorTypeSelectionWindow.getSelectedFloor());
			}
		});
		toolWindowManager.registerToolWindow("Floors", "Floor type selection", null, floorTypeSelectionWindow,
				ToolWindowAnchor.LEFT);
		floorEditorWindow = new FloorEditorWindow();
		floorEditorWindow.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				floorTypeSelectionWindow.refresh();
			}
		});
		toolWindowManager.registerToolWindow("Floor Editor", "Floor editor", null, floorEditorWindow.getMainPane(),
				ToolWindowAnchor.RIGHT);
		mapViewPanel = new MapViewPanel();
		toolWindowManager.registerToolWindow("Minimap", null, null, mapViewPanel, ToolWindowAnchor.RIGHT);
		settingsBrushEditorWindow = new SettingsBrushEditor();
		toolWindowManager.registerToolWindow("Settings Editor", "Settings brush editor", null,
				settingsBrushEditorWindow.getMainPane(), ToolWindowAnchor.RIGHT);

		objectEditorPanel = new ObjectEditorPanel();
		toolWindowManager.registerToolWindow("Objects", "Object Editor", null, objectEditorPanel,
				ToolWindowAnchor.LEFT);

		tileShaperPanel = new TileShaperPanel();
		toolWindowManager.registerToolWindow("Shapes", "Tile Shaper", null, tileShaperPanel, ToolWindowAnchor.LEFT);

		for (ToolWindow window : toolWindowManager.getToolWindows())
			window.setAvailable(true);
		rootFrame.getContentPane().add(myDoggyToolWindowManager, "1,1,");

		loadWorkspace();
	}

	private void initMenus() {
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		reRenderSceneItem = new JMenuItem("Re-render scene");
		openMapMenuItem = new JMenuItem("Tele to coordinates");
		saveAndPackFloorsItem = new JMenuItem("Save and pack floor map");
		saveFloorsItem = new JMenuItem("Save floor map");
		saveAndPackObjectsItem = new JMenuItem("Save and pack object map");
		saveObjectsItem = new JMenuItem("Save object map");
		overridePackItem = new JMenuItem("Override pack");

		// ----
		openCacheMenuItem = new JMenuItem("Open cache...");
		// ----
		preferencesMenuItem = new JMenuItem("Preferences...");
		exitMenuItem = new JMenuItem("Exit");
		fileMenu.add(openMapMenuItem);
		fileMenu.add(saveAndPackFloorsItem);
		fileMenu.add(saveFloorsItem);
		fileMenu.add(saveAndPackObjectsItem);
		fileMenu.add(saveObjectsItem);
		fileMenu.add(overridePackItem);

		// fileMenu.add(openCacheMenuItem);
		// fileMenu.add(preferencesMenuItem);

		fileMenu.add(exitMenuItem);
		menuBar.add(fileMenu);

		editMenu = new JMenu("Edit");
		copyTileMenuItem = new JMenuItem("Copy tile");
		pasteTileMenuItem = new JMenuItem("Paste tile");

		editMenu.add(copyTileMenuItem);
		editMenu.add(pasteTileMenuItem);
		rootFrame.setJMenuBar(menuBar);

		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					storeWorkspace();
					System.exit(-1);
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Failed to save Workspace.", ":(", JOptionPane.ERROR_MESSAGE);
					System.exit(-1);
				}
			}
		});
		reRenderSceneItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// For now use a JOptionPane
				try {
					editor.drawScene();
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(rootFrame, "Invalid coordinates, use format (X,Y)",
							"Ghetto Online Map Editor", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		openMapMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// For now use a JOptionPane
				try {
					String[] s = JOptionPane.showInputDialog(rootFrame, "Coordinates?").split(",");
					editor.loadMap(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(rootFrame, "Invalid coordinates, use format (X,Y)",
							"Ghetto Online Map Editor", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		saveAndPackFloorsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// For now use a JOptionPane
				try {
					editor.saveMap();
					JOptionPane.showMessageDialog(null, "Successfully packed map to cache!", ":)",
							JOptionPane.INFORMATION_MESSAGE);

				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Failed to pack map to cache.", ":(",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		saveFloorsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					editor.saveMap2();
					JOptionPane.showMessageDialog(null, "Successfully exported map!", ":)",
							JOptionPane.INFORMATION_MESSAGE);

				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Failed to export map.", ":(", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		saveAndPackObjectsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// For now use a JOptionPane
				try {
					ObjectscapeLogic.packObjectscapes();

					JOptionPane.showMessageDialog(null, "Successfully packed map to cache!", ":)",
							JOptionPane.INFORMATION_MESSAGE);

				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Failed to pack map to cache.", ":(",
							JOptionPane.ERROR_MESSAGE);
					e2.printStackTrace();
				}
			}
		});
		saveObjectsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ObjectscapeLogic.exportObjectscape();

					JOptionPane.showMessageDialog(null, "Successfully exported map!", ":)",
							JOptionPane.INFORMATION_MESSAGE);

				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Failed to export map.", ":(", JOptionPane.ERROR_MESSAGE);
					e2.printStackTrace();
				}
			}
		});
		overridePackItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					editor.repackCacheIndex(4);
					JOptionPane.showMessageDialog(null, "Overriden packed maps with ./import/index4/", ":)",
							JOptionPane.INFORMATION_MESSAGE);

				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Failed to export map.", ":(", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

	}

	public void show() {
		rootFrame.setVisible(true);
	}

	public GameViewPanel getGameViewPanel() {
		return gameViewPanel;
	}

	public MapViewPanel getMapViewPanel() {
		return mapViewPanel;
	}

	public void editorStarted(Main editor) {
		this.editor = editor;
		floorTypeSelectionWindow.loadFloors();
		rootFrame.addWindowListener(editor);
	}

	public ToolSelectionBar getToolSelectionBar() {
		return toolSelectionBar;
	}

	public FloorTypeSelection getFloorTypeSelectionWindow() {
		return floorTypeSelectionWindow;
	}

	public SettingsBrushEditor getSettingsBrushEditorWindow() {
		return settingsBrushEditorWindow;
	}

	public FloorEditorWindow getFloorEditorWindow() {
		return floorEditorWindow;
	}

	public void storeWorkspace() {
		try {

			FileOutputStream fileOutputStream = new FileOutputStream("./data/workspace.xml");
			toolWindowManager.getPersistenceDelegate().save(fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(rootFrame, "Error storing workspace", "Runescape Map Editor",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void loadWorkspace() {
		try {
			FileInputStream fileInputStream = new FileInputStream("./data/workspace.xml");
			toolWindowManager.getPersistenceDelegate().apply(fileInputStream);
			fileInputStream.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(rootFrame, "Error loading workspace", "Runescape Map Editor",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public EditorToolbar getEditorToolbar() {
		return editorToolbar;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		int rotation = arg0.getWheelRotation();

		if (rotation > 0) {
			ObjectscapeLogic.placementOrientation++;
			ObjectscapeLogic.placementOrientation &= 3;
		} else {
			ObjectscapeLogic.placementOrientation--;
			ObjectscapeLogic.placementOrientation &= 3;
		}

		TileShaperPanel.orientation_label
				.setText("Orientation: " + ObjectscapeLogic.ORIENTATIONS[ObjectscapeLogic.placementOrientation]);
		ObjectEditorPanel.label_objOrientationField
				.setText("" + ObjectscapeLogic.ORIENTATIONS[ObjectscapeLogic.placementOrientation]);

	}
}
