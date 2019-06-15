/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs355.controller;

import cs355.GUIFunctions;

import cs355.model.drawing.MyModel;
import cs355.model.drawing.Shape;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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
    private Color current_color;

    public static String current_tool_button;

    //CLICKS
    static Point2D.Double current_click;
    private static Point2D.Double current_start;

    //TRIANGLE POINTS
    static Point2D.Double triA;
    static Point2D.Double triB;
    static Point2D.Double triC;
    static int triCount;

    //OFFSETS
    static Point2D.Double selectionOffset = new Point2D.Double(0,0);
    static boolean selectionDistanceCalculated = false;

    //SELECTION
    static boolean startLineHandleSelected = false;
    static boolean endLineHandleSelected = false;

    //ROTATION
    private static boolean isRotateHandleSelected = false;
    public static int rotateHandleRadius = 8;
    static double rotateHandleOffset = 30;
    public static double currentRotateHandleDistanceFromCenter = 0;

    //INDICES
    private int current_shape_index;
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

    public MyController(MyModel model)
    {
        this.model = model; 
        current_tool_button = null;
        
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
    public void zoomInButtonHit() {
        GUIFunctions.printf("Zoom In Button Hit");
        GUIFunctions.refresh();
        triCount = 0;
    }

    @Override
    public void zoomOutButtonHit() {
        GUIFunctions.printf("Zoom Out Button Hit");
        GUIFunctions.refresh();
        triCount = 0;
    }

    @Override
    public void hScrollbarChanged(int value) {
    }

    @Override
    public void vScrollbarChanged(int value) {
    }

    @Override
    public void openScene(File file) {
    }

    @Override
    public void toggle3DModelDisplay() {
    }

    @Override
    public void keyPressed(Iterator<Integer> iterator) {
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
    public void toggleBackgroundDisplay() {
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
    public void mouseClicked(MouseEvent e) 
    {
        //GUIFunctions.printf("Mouse clicked");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        GUIFunctions.printf("Mouse Pressed");
        //Get the first point where it is pressed as the start of the line.
        //Set the end of the line to the same spot.

        if(current_tool_button != null) {
            Point p = e.getPoint();
            current_start = ControllerUtilities.get2DPoint(p);
            current_click = ControllerUtilities.get2DPoint(p);

            //SHAPE IS ALREADY SELECTED_________________________________________________________________________________
            if(ControllerUtilities.shapeIsSelected())
            {
                Shape shape = model.getShape(current_selected_shape_index);
                //Test if the click is in the Rotate Handle
                if(ControllerUtilities.rotateHandleSelected(current_click, shape))
                {
                    isRotateHandleSelected = true;
                    GUIFunctions.refresh();
                }
                else
                {
                    ControllerUtilities.checkIfClickedInShape(model);
                }
            }//---------------------------------------------------------------------------------------------------------


            //TRY TO SELECT A SHAPE WHILE SELECTION TOOL IS ON__________________________________________________________
            else if(current_tool_button.equals(SELECTION_TOOL))
            {
                ControllerUtilities.checkIfClickedInShape(model);

            }//_________________________________________________________________________________________________________


            //FOR STARTING THE CREATION OF A SHAPE______________________________________________________________________
            if(ControllerUtilities.shapeToolIsSet() || current_tool_button.equals(MyController.TRIANGLE))
            {
                //SHAPES
                if (current_tool_button.equals(MyController.TRIANGLE)) {
                    recordTriangleClicks(e);
                }
                //Create a shape using that initial starting position
                Shape s = ControllerUtilities.createShape(current_start, current_start, current_tool_button, current_color);
                if (s != null) {
                    current_shape_index = model.addShape(s);
                }
            }//_________________________________________________________________________________________________________

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
    public void mouseEntered(MouseEvent e) {
        GUIFunctions.changeSelectedColor(current_color);
    }

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

            //FOR TRANSLATING OR ROTATING A SELECTED SHAPE______________________________________________________________
            if(ControllerUtilities.shapeIsSelected())
            {
                //get the shape and start moving it relative to the drag point.
                Shape currentShape = model.getShape(current_selected_shape_index);

                Point2D.Double endDragPoint = ControllerUtilities.get2DPoint(p);

                //For translations
                if(!isRotateHandleSelected || startLineHandleSelected || endLineHandleSelected)
                {
                    ControllerUtilities.translateShape(currentShape, current_click, endDragPoint);
                    GUIFunctions.refresh();
                }
                else //
                {
                    //Calculate the rotations
                    ControllerUtilities.rotateShape(currentShape, endDragPoint);
                    GUIFunctions.refresh();
                }

            }
            //----------------------------------------------------------------------------------------------------------
            //FOR CREATING SHAPES TO DRAW_______________________________________________________________________________
            else if(ControllerUtilities.shapeToolIsSet())
            {
                Shape s = ControllerUtilities.createShape(current_start, current_end, current_tool_button, current_color);
                model.deleteShape(current_shape_index);

                if (s != null)
                {
                    model.addShape(s);
                }

            }
            //__________________________________________________________________________________________________________
        }
    }

    @Override
    public void mouseMoved(MouseEvent e){
    }

    private void recordTriangleClicks(MouseEvent e) 
    {
        Point p = e.getPoint();
        if(triCount == 0)
        {
            triA = ControllerUtilities.get2DPoint(p);
            triCount++;
        }
        else if(triCount == 1)
        {
            triB = ControllerUtilities.get2DPoint(p);
            triCount++;
        }
        else if(triCount == 2)
        {
            triC = ControllerUtilities.get2DPoint(p); 
            triCount++;
        }
        else
        {
            triCount = 0;
        }
         
    }
}
