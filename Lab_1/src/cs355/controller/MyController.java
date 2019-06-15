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
    private String current_button;
    private Color current_color;
    public static Point2D.Double current_start;
    public static Point2D.Double current_end;
    public static Point2D.Double triA;
    public static Point2D.Double triB;
    public static Point2D.Double triC;
    public static int triCount;
    private int current_shape_index;
    public static final String LINE = "line";
    public static final String SQUARE = "square";
    public static final String RECTANGLE = "rectangle";
    public static final String CIRCLE = "circle";
    public static final String ELLIPSE = "ellipse";
    public static final String TRIANGLE = "triangle";
    
 
    
    public MyController(MyModel model)
    {
        this.model = model; 
        this.current_button = null;
        
        //Set the initial color
        this.current_color = Color.WHITE;
        this.current_shape_index = 0;
        this.triCount = 0;
        
    }

    @Override
    public void colorButtonHit(Color c) {
        GUIFunctions.printf("Color Button Hit");
        current_color = c;
        GUIFunctions.changeSelectedColor(current_color);
    }

    @Override
    public void lineButtonHit() {
        GUIFunctions.printf("Line Button Hit");
        current_button = LINE;
        triCount = 0;
        //Point2D.Double first = new Point2D.Double(3.0, 6.0);
        //Point2D.Double second = new Point2D.Double(9.0, 36.0);
        
        
    }

    @Override
    public void squareButtonHit() {
        GUIFunctions.printf("Square Button Hit");
        current_button = SQUARE;
        triCount = 0;
    }

    @Override
    public void rectangleButtonHit() {
        GUIFunctions.printf("Rectangle Button Hit");
        current_button = RECTANGLE;
        triCount = 0;
        
    }

    @Override
    public void circleButtonHit() {
        GUIFunctions.printf("Circle Button Hit");
        current_button = CIRCLE;
        triCount = 0;
        
    }

    @Override
    public void ellipseButtonHit() {
        GUIFunctions.printf("Ellipse Button Hit");
        current_button = ELLIPSE;
        triCount = 0;
    }

    @Override
    public void triangleButtonHit() {
        GUIFunctions.printf("Triangle Button Hit");
        current_button = TRIANGLE;
    }

    @Override
    public void selectButtonHit() {
        GUIFunctions.printf("Select Button Hit");
        triCount = 0;
    }

    @Override
    public void zoomInButtonHit() {
        GUIFunctions.printf("Zoom In Button Hit");
        triCount = 0;
    }

    @Override
    public void zoomOutButtonHit() {
        GUIFunctions.printf("Zoom Out Button Hit");
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
    }

    @Override
    public void doDeleteShape() {
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
    public void doMoveForward() {
    }

    @Override
    public void doMoveBackward() {
    }

    @Override
    public void doSendToFront() {
    }

    @Override
    public void doSendtoBack() {
    }

    @Override
    public void mouseClicked(MouseEvent e) 
    {
        GUIFunctions.printf("Mouse clicked");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        GUIFunctions.printf("Mouse Pressed");
        //Get the first point where it is pressed as the start of the line.
        //Set the end of the line to the same spot.
       
        Point p = e.getPoint();
        current_start  = ControllerUtilities.get2DPoint(p);
        
        if(current_button.equals(MyController.TRIANGLE))
        {
            recordTriangleClicks(e);
        }
        //Create a shape using that initial starting position
        Shape s = ControllerUtilities.createShape(current_start , current_start , current_button, current_color);
        if(s != null)
        {
           current_shape_index = model.addShape(s); 
        }
 
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        GUIFunctions.printf("Mouse Released");
        //If the line tool is selected, then make note of the release point
            //Store the line as a draw initial point and a release point in the model
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        GUIFunctions.changeSelectedColor(current_color);
        GUIFunctions.printf("Mouse Entered");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        GUIFunctions.printf("Mouse Excited");
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        GUIFunctions.printf("Mouse Dragging");
        //If the line tool selected, then just draw to the end point.
        
        Point p = e.getPoint();
        current_end = ControllerUtilities.get2DPoint(p);
        
        //Create a shape using that initial starting position and now end position
        Shape s = ControllerUtilities.createShape(current_start, current_end, current_button, current_color);
        
        //get the shape on the top we are trying to edit, delete it and then add a new one
       
        if(!(current_button.equals(TRIANGLE)))
        {
            model.deleteShape(current_shape_index);
        }
        if(s != null)
        {
            model.addShape(s);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        GUIFunctions.printf("Mouse Moved");
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
