/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs355.controller;

import cs355.GUIFunctions;

import cs355.model.drawing.MyModel;
import cs355.model.drawing.Shape;
import cs355.model.scene.CS355Scene;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.Iterator;

/**
 *
 * @author Ben Nelson
 */
public class MyController implements CS355Controller, MouseListener, MouseMotionListener
{
    private final MyModel model;
    public static CS355Scene scene3D;
    public static Camera camera;
    public static ZoomControl zoom;
    public static MyViewport viewport;
    private ScrollControl scroll;
    static Color current_color;

    public static String current_tool_button;

    //CLICKS
    static Point2D.Double current_click;
    static Point2D.Double current_start;

    //TRIANGLE POINTS
    static Point2D.Double triA;
    static Point2D.Double triB;
    static Point2D.Double triC;
    static int triCount;

    //Zoom
    static boolean justComingOutZoom25 = false;

    //OFFSETS
    static Point2D.Double selectionOffset = new Point2D.Double(0,0);
    static boolean selectionDistanceCalculated = false;

    //SELECTION
    static boolean startLineHandleSelected = false;
    static boolean endLineHandleSelected = false;
    static final int defaultLineTolerance = 4;

    //ROTATION
    public static boolean isRotateHandleSelected = false;
    static final int defaultRotateHandleRadius = 8;
    public static int rotateHandleRadius = defaultRotateHandleRadius;
    static final double defaultRotateHandleOffset = 30;
    public static double rotateHandleOffset = defaultRotateHandleOffset;
    public static double currentRotateHandleDistanceFromCenter = 0;

    //INDICES
    static int current_shape_index;
    public static int current_selected_shape_index;

    //"ENUMS"
    public static final int NO_SELECTION = -1;
    static final String LINE = "line";
    static final String SQUARE = "square";
    static final String RECTANGLE = "rectangle";
    static final String CIRCLE = "circle";
    static final String ELLIPSE = "ellipse";
    static final String TRIANGLE = "triangle";
    public static final String SELECTION_TOOL = "selection_tool";


    //3D House Stuff
    public static boolean SCENE3D = false;

    public MyController(MyModel model, ZoomControl zoom, MyViewport viewport)
    {
        this.model = model;
        this.zoom = zoom;
        this.viewport = viewport;
        current_tool_button = null;
        scroll = null;
        scene3D = new CS355Scene();
        camera = new Camera();
        
        //Set the initial color
        this.current_color = Color.WHITE;
        this.current_shape_index = 0;
        current_selected_shape_index = NO_SELECTION;
        selectionOffset = null;
        triCount = 0;
    }

    @Override
    public void colorButtonHit(Color c) {
        GUIFunctions.printf("Color Button Hit");
        current_color = c;
        GUIFunctions.changeSelectedColor(current_color);

        if(current_selected_shape_index != NO_SELECTION)
        {
            model.getShape(current_selected_shape_index).setColor(current_color);
            GUIFunctions.refresh();
        }
    }

    @Override
    public void lineButtonHit() {
        GUIFunctions.printf("Line Button Hit");
        current_tool_button = LINE;
        current_selected_shape_index = NO_SELECTION;
        GUIFunctions.refresh();
        triCount = 0;
    }

    @Override
    public void squareButtonHit() {
        GUIFunctions.printf("Square Button Hit");
        current_tool_button = SQUARE;
        current_selected_shape_index = NO_SELECTION;
        GUIFunctions.refresh();
        triCount = 0;
    }

    @Override
    public void rectangleButtonHit() {
        GUIFunctions.printf("Rectangle Button Hit");
        current_tool_button = RECTANGLE;
        current_selected_shape_index = NO_SELECTION;
        GUIFunctions.refresh();
        triCount = 0;
        
    }

    @Override
    public void circleButtonHit() {
        GUIFunctions.printf("Circle Button Hit");
        current_tool_button = CIRCLE;
        current_selected_shape_index = NO_SELECTION;
        GUIFunctions.refresh();
        triCount = 0;
        
    }

    @Override
    public void ellipseButtonHit() {
        GUIFunctions.printf("Ellipse Button Hit");
        current_tool_button = ELLIPSE;
        current_selected_shape_index = NO_SELECTION;
        GUIFunctions.refresh();
        triCount = 0;
    }

    @Override
    public void triangleButtonHit() {
        GUIFunctions.printf("Triangle Button Hit");
        current_tool_button = TRIANGLE;
        current_selected_shape_index = NO_SELECTION;
        GUIFunctions.refresh();
    }

    @Override
    public void selectButtonHit() {
        GUIFunctions.printf("Select Button Hit");
        current_tool_button = SELECTION_TOOL;
        current_selected_shape_index = NO_SELECTION;
        triCount = 0;
    }

    @Override
    public void zoomInButtonHit()
    {
        ZoomControl.zoomIn = true;
        ZoomControl.currentlyZooming = true;

        if(!(zoom.getCurrentZoom() == ZoomControl.zoom400))
        {
            if (zoom.getCurrentZoom() == ZoomControl.zoom25)
            {
                justComingOutZoom25 = true;
                viewport.setUpLeftX(viewport.previousUpL.x);
                viewport.setUpLeftY(viewport.previousUpL.y);
            }

            zoom.changeCurrentZoom(zoom.getCurrentZoom() * 2);
            viewport.accountForZoom(zoom.getCurrentZoom());
            scroll.changeScrollBars();
            RotateHandleUtilities.adjustRotateHandlesForZoom(zoom.getCurrentZoom());
            RotateHandleUtilities.setRotateHandleDistanceForZoom(model.getShapes());



            GUIFunctions.refresh();


        }

        triCount = 0;
        ZoomControl.zoomIn = false;
        ZoomControl.currentlyZooming = false;
        MyViewport.isAlreadyXBounded = false;
        MyViewport.isAlreadyYBounded = false;


        if(current_tool_button!= null) {
            if (ControllerUtilities.shapeIsSelected()) {
                ControllerUtilities.makeRotationHandle(model);
                GUIFunctions.refresh();
            }
        }

    }

    @Override
    public void zoomOutButtonHit()
    {
        ZoomControl.zoomOut = true;
        ZoomControl.currentlyZooming = true;

        if(!(zoom.getCurrentZoom() == ZoomControl.zoom25))
        {
            zoom.changeCurrentZoom(zoom.getCurrentZoom() / 2);
            viewport.accountForZoom(zoom.getCurrentZoom());
            scroll.changeScrollBars();
            RotateHandleUtilities.adjustRotateHandlesForZoom(zoom.getCurrentZoom());
            RotateHandleUtilities.setRotateHandleDistanceForZoom(model.getShapes());

            GUIFunctions.refresh();
        }

        triCount = 0;
        ZoomControl.zoomOut = false;
        ZoomControl.currentlyZooming = false;
        MyViewport.isAlreadyXBounded = false;
        MyViewport.isAlreadyYBounded = false;

        if(current_tool_button!= null) {
            if (ControllerUtilities.shapeIsSelected()) {
                ControllerUtilities.makeRotationHandle(model);
                GUIFunctions.refresh();
            }
        }
    }

    @Override
    public void hScrollbarChanged(int value)
    {
        if(!ZoomControl.currentlyZooming)
        {
            if (!justComingOutZoom25) {
                viewport.setUpLeftX(value);
                GUIFunctions.refresh();
            }
        }

    }

    @Override
    public void vScrollbarChanged(int value)
    {
        if(!ZoomControl.currentlyZooming)
        {
            if (!justComingOutZoom25) {
                viewport.setUpLeftY(value);
                GUIFunctions.refresh();
            }
        }
    }

    @Override
    public void openScene(File file)
    {
        if(SCENE3D) {
            scene3D.open(file);

            //now load the scene position into the camera
            camera.loadSceneCamera(scene3D.getCameraPosition(),scene3D.getCameraRotation());
            GUIFunctions.refresh();
        }
    }

    @Override
    public void toggle3DModelDisplay()
    {
        SCENE3D = !SCENE3D;
        GUIFunctions.refresh();
    }

    @Override
    public void keyPressed(Iterator<Integer> iterator)
    {

        if(SCENE3D)
        {
            KeyboardTools.accountForKeysPressed(iterator);
            GUIFunctions.refresh();
        }
    }

    @Override
    public void openImage(File file) 
    {
        
    }

    @Override
    public void saveImage(File file) 
    {
        
    }

    @Override
    public void toggleBackgroundDisplay()
    {

    }

    @Override
    public void saveDrawing(File file)
    {
        model.save(file);
    }

    @Override
    public void openDrawing(File file) 
    {
        model.open(file);
        current_selected_shape_index = NO_SELECTION;
        GUIFunctions.refresh();
    }

    @Override
    public void doDeleteShape()
    {
        if(ControllerUtilities.shapeIsSelected())
        {
            model.deleteShape(current_selected_shape_index);
            current_selected_shape_index = NO_SELECTION;
            GUIFunctions.refresh();
        }
    }

    @Override
    public void doEdgeDetection() {
    }

    @Override
    public void doSharpen() {
    }

    @Override
    public void doMedianBlur() {
    }

    @Override
    public void doUniformBlur() {
    }

    @Override
    public void doGrayscale() {
    }

    @Override
    public void doChangeContrast(int contrastAmountNum) {
    }

    @Override
    public void doChangeBrightness(int brightnessAmountNum) {
    }

    @Override
    public void doMoveForward()
    {
        if(ControllerUtilities.shapeIsSelected())
        {
            ControllerUtilities.moveForward(model);
        }
    }

    @Override
    public void doMoveBackward()
    {
        if(ControllerUtilities.shapeIsSelected())
        {
            ControllerUtilities.moveBackward(model);
        }
    }

    @Override
    public void doSendToFront()
    {
        if(ControllerUtilities.shapeIsSelected())
        {
            ControllerUtilities.moveToFront(model);
        }
    }

    @Override
    public void doSendtoBack()
    {
        if(ControllerUtilities.shapeIsSelected())
        {
            ControllerUtilities.moveToBack(model);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if(current_tool_button != null)
        {
            Point p = e.getPoint();
            current_start = ControllerUtilities.get2DPoint(p);
            current_click = ControllerUtilities.get2DPoint(p);

            ControllerUtilities.selectingShapes(model);

            ControllerUtilities.creatingShapes(e,model);
        }
 
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        //Drag ends so make sure selectionDrag flag is set to false
        selectionDistanceCalculated = false;
        isRotateHandleSelected = false;
        startLineHandleSelected = false;
        endLineHandleSelected = false;

    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        //GUIFunctions.printf("Mouse Dragging");
        //If the line tool selected, then just draw to the end point.

        if(current_tool_button != null)
        {
            Point p = e.getPoint();
            Point2D.Double current_end = ControllerUtilities.get2DPoint(p);

            if(ControllerUtilities.shapeIsSelected())
            {
                ControllerUtilities.translatingOrRotating(p,model);
            }
            else if(ControllerUtilities.shapeToolIsSet())
            {
                ControllerUtilities.updatingShapeBeingCreated(current_end,model);
            }

        }
    }

    @Override
    public void mouseMoved(MouseEvent e){
    }


    public void initializeScrollBars()
    {
        if(scroll == null)
        {
            scroll = new ScrollControl(zoom,viewport);
        }
    }

    public void initializeColor()
    {
        GUIFunctions.changeSelectedColor(current_color);
    }


}
