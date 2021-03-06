package mapeditor.gui.renderer.pgle;

import com.sun.corba.se.spi.orbutil.fsm.Input;

import mapeditor.jagex.rt3.MapRegion;
import mapeditor.jagex.rt3.SceneGraph;
import mapeditor.jagex.rt4.AthmosphericEffects;
import mapeditor.jagex.rt4.Class7_Sub1;
import mapeditor.jagex.rt4.OpenGLManager;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.peterbjornx.pgl2.camera.Camera;
import org.peterbjornx.pgl2.camera.controls.FogRenderControl;
//import org.peterbjornx.pgl2.geom.SkyBoxNode;
import org.peterbjornx.pgl2.input.cameracontrol.FirstPersonCamera;
import org.peterbjornx.pgl2.light.OpenGLLightManager;
import org.peterbjornx.pgl2.math.VectorMath;
import org.peterbjornx.pgl2.model.Node;
import org.peterbjornx.pgl2.terrain.Terrain;
import org.peterbjornx.pgl2.util.ServerMemoryManager;

import javax.swing.*;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by IntelliJ IDEA.
 * User: Peter
 * Date: 6/17/11
 * Time: 8:08 PM
 * Computer: Peterbjornx-PC.rootdomain.asn.local (192.168.178.27)
 */
public class PglWrapper {

    public Node scene;
    protected Camera camera;
    private FirstPersonCamera firstPersonCamera;
    private Terrain rsTerrain;
    private boolean running = false;
    private RsTerrainSource rsTerrainSource;
    private RsTileManager rsTileManager;
    private OpenGLLightManager lightManager;
    private PglSun sun;
    private PglOverlayNode[] overlayNodes = new PglOverlayNode[4];

    public void initLighting(){
        glEnable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE);
        glEnable(GL_LIGHTING);
        glShadeModel(GL_SMOOTH);
        lightManager = new OpenGLLightManager();
        rsTileManager.setOpenGLLightManager(lightManager);

        sun = new PglSun();
        sun.setSunColor(new Color(0xFF, 0xFF, 0xFF, 0xff), 0.7f, 0.7f, 0.4f);
        sun.setSunPosition(new Vector3f(-30, -50, -30));
        AthmosphericEffects.setupLighting();
        glLightf(GL_LIGHT2, GL_CONSTANT_ATTENUATION, 1.0f);
        glLightf(GL_LIGHT2, GL_LINEAR_ATTENUATION, 0.0015625f/5f);
        glLightf(GL_LIGHT2, GL_QUADRATIC_ATTENUATION, 0);//.000625f);
    }

    public void merge(){
          /*  int width = 765;
            int height = 504;
            Canvas c = new Canvas() ;
            SceneGraph.clientInstance.gameFrame.add(c);
            c.setSize(width, height);
            c.setLocation(4, 24);
            c.addMouseListener(SceneGraph.clientInstance);
            c.addMouseMotionListener(SceneGraph.clientInstance);
            c.addKeyListener(SceneGraph.clientInstance);
            c.addFocusListener(SceneGraph.clientInstance);
            c.setFocusable(false);
            JPanel p = new JPanel();
            p.setLocation(4, 24);
            p.addMouseListener(SceneGraph.clientInstance);
            p.addMouseMotionListener(SceneGraph.clientInstance);
            p.addKeyListener(SceneGraph.clientInstance);
            p.addFocusListener(SceneGraph.clientInstance);
            SceneGraph.clientInstance.gameFrame.add(p);
            p.setSize(width, height);  

        try {
            Display.setParent(c);
        } catch (LWJGLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }   */       
    }

    /**
     * Initialize PGLEngine
     */
    public void initJgle() {
        try {
            int width = 765;
            int height = 504;
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.create();
            merge();
            scene = new Node();
            camera = new Camera();
            camera.setViewport(new Vector2f(0,0),new Vector2f(width,height));//new Vector2f(4, height - 334 - 4), new Vector2f(512, 334));
            camera.setNearClip(1f);
            camera.setFarClip(128 * 30.0f);
            camera.setActive(true);
            camera.setClearColor(new Color(0xC8,0xA8,0xC0,0xff));
            FogRenderControl fog = new FogRenderControl();
            fog.setColour(new Color(0xC8,0xC0,0xA8));
            fog.setDensity(0.95F);
            int fogEnd = 3584;
            int fogStart = fogEnd - 512;
            if (fogStart < 50)
                fogStart = 50;
            fog.setStart((float) fogStart);
            fog.setMode(9729);     //linear
            fog.setHint(4353);     //fastest
            fog.setEnd((float) (fogEnd - 256));
            camera.addRenderControl(fog);
            scene.add(camera);
            //scene.add(new SkyBoxNode(4,"./skybox/Sunset",".jpg"));
            rsTileManager = new RsTileManager();
            scene.add(rsTileManager);
            firstPersonCamera = new FirstPersonCamera();
            glEnable(GL_DEPTH_TEST);
            if (Class7_Sub1.useLighting)
                initLighting();
            running = true;
        } catch (LWJGLException e) {

            e.printStackTrace();
            running = false;
            //System.exit(0);
        }
    }

    public void loadNewRegion(MapRegion mapRegion){
        if (rsTerrain != null)
            clearRegion();
        rsTerrainSource = new RsTerrainSource(mapRegion);
        rsTerrainSource.updateMap();
        rsTerrain = new Terrain(rsTerrainSource);
        scene.add(rsTerrain);
        for (int i = 0;i < 4;i++){
            overlayNodes[i] = new PglOverlayNode(mapRegion,i);
            scene.add(overlayNodes[i]);
        }
    }

    public void setCameraPosition(int x,int z,int y){
        camera.setPosition(new Vector3f(x,-y,z));
    }
    /**
         * The entry point for this SimpleApplication
         */
        public void process2D() {
            if (!running)
                return;
            if (!Display.isCloseRequested()) {
                try {
                    Display.makeCurrent();
                } catch (LWJGLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                try {
                    Display.swapBuffers();
                } catch (LWJGLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                Display.processMessages();
                try {
                    ServerMemoryManager.processQueues();
                } catch (Exception e){e.printStackTrace();
                    System.out.println("ServerMemoryManager exception :O");
                }
            } else {
                Display.destroy();
                System.err.println("Closing window");
                running = false;
            }

        }

    /**
     * The entry point for this SimpleApplication
     */
    public void process() {
        if (!running)
            return;
        if (!Display.isCloseRequested()) {
            try {
                Display.makeCurrent();
            } catch (LWJGLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            preRender();
            lightManager.startLighting(new Camera());
            scene.render(null);
          //  if (SceneGraph.clientInstance != null)
               // SceneGraph.clientInstance.draw2DToGL();
            lightManager.stopLighting();
            try {
                Display.swapBuffers();
            } catch (LWJGLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            Display.processMessages();
            try {
                ServerMemoryManager.processQueues();
            } catch (Exception e){e.printStackTrace();
                System.out.println("ServerMemoryManager exception :O");
            }
        } else {
            Display.destroy();
            System.err.println("Closing window");
            running = false;
        }

    }

    public void doLighting(){
        //if (Class7_Sub1.useLighting)
        //    sun.activateNoManager();
        AthmosphericEffects.loadLightPositions();
    }

    public void preRender() {
        firstPersonCamera.handleInput(camera);
    }

    public RsTileManager getRsTileManager() {
        return rsTileManager;
    }

    public void clearRegion() {
        if (rsTerrain != null){
            scene.remove(rsTerrain);
            rsTerrain = null;
            rsTerrainSource = null;
            for (int i = 0;i < 4;i++)
                scene.remove(overlayNodes[i]);
        }
        System.gc();
    }

    public void doCamTransform() {
        camera.loadViewMatrix();
    }

    public void setCameraRotation(int xCameraCurve, int yCameraCurve, int i) {
        camera.setRotation(VectorMath.eulerAnglesToQuaternion(new Vector3f(xCameraCurve * 0.17578125f,yCameraCurve * 0.17578125f,i * 0.17578125f)));
    }

    public PglSun getSun() {
        return sun;
    }
}
