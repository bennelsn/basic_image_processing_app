/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs355.view;

import cs355.model.drawing.*;
import cs355.model.drawing.Rectangle;
import cs355.model.drawing.Shape;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;


import static cs355.controller.MyController.currentRotateHandleDistanceFromCenter;
import static cs355.controller.MyController.rotateHandleRadius;

/**
 *
 * @author BenNelson
 */
class MyDrawer {

    private static Color highlightColor = Color.RED;
    
    static void drawShape(Shape shape, Graphics2D graphicsPad, boolean dontDrawSelection)
    {
        //LINE
        //SQUARE
        //RECTANGLE
        //CIRCLE
        //ELLIPSE
        //TRIANGLE
        if(shape instanceof Line)
        {
            Line line = (Line) shape;
            drawLine(line, graphicsPad, dontDrawSelection);

            return;  
        }
        if(shape instanceof Square)
        {
            Square s = (Square) shape;
            drawSquare(s, graphicsPad, dontDrawSelection);
            return;
        }
        if(shape instanceof Rectangle)
        {
            Rectangle r = (Rectangle) shape;
            drawRectangle(r, graphicsPad, dontDrawSelection);
            return;
        }
        if(shape instanceof Circle)
        {
            Circle c = (Circle) shape;
            drawCircle(c, graphicsPad, dontDrawSelection);
            return;
        }
        if(shape instanceof Ellipse)
        {
            Ellipse e = (Ellipse) shape;
            drawEllipse(e, graphicsPad, dontDrawSelection);
            return;
        }
        if(shape instanceof Triangle)
        {
            Triangle t = (Triangle) shape;
            drawTriangle(t, graphicsPad, dontDrawSelection);

        }
    }
    private static void drawLine(Line line, Graphics2D gPad, boolean forSelectionDrawing)
    {
        Point2D.Double start = new Point2D.Double(0,0);
        Point2D.Double end = line.getEnd();
        transformAndRotateShape(gPad,line);

        if(!forSelectionDrawing) {
            gPad.setColor(line.getColor());
            gPad.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
            resetGraphicsPad(gPad);
        }
        else {
            gPad.setColor(highlightColor);
            drawSelectionHandlesForLine(gPad, start, end);
            resetGraphicsPad(gPad);
        }

    }

    private static void drawSelectionHandlesForLine(Graphics2D gPad, Point2D.Double start, Point2D.Double end) {

        //Draw First circle
        gPad.drawOval((int)(start.x - rotateHandleRadius),(int)(start.y - rotateHandleRadius),
                (rotateHandleRadius*2),(rotateHandleRadius*2));
        //Draw Second circle
        gPad.drawOval((int)(end.x - rotateHandleRadius),(int)(end.y - rotateHandleRadius),
                (rotateHandleRadius*2),(rotateHandleRadius*2));

    }

    private static void drawSquare(Square square, Graphics2D gPad, boolean forSelectionDrawing)
    {
        double size = square.getSize();

        if(!forSelectionDrawing) //DRAWING SHAPE
        {
            //Set color
            gPad.setColor(square.getColor());

            transformAndRotateShape(gPad, square);

            //DRAW
            gPad.fillRect((int)-size/2, (int)-size/2, (int) size, (int) size);

            //RESET THE PAD
            resetGraphicsPad(gPad);

        }
        else // DRAWING SELECTION LINES AND CIRCLE
        {
            gPad.setColor(highlightColor);

            transformAndRotateShape(gPad, square);

            //DRAW SELECTION BOUNDING BOX
            gPad.drawRect((int) -size/2,(int) -size/2,(int) size,(int) size);

            //DRAW ROTATE HANDLE
            drawRotateHandle(gPad);

            resetGraphicsPad(gPad);
        }

    }
    private static void drawRotateHandle(Graphics2D gPad)
    {
        gPad.drawOval(-rotateHandleRadius,(int)-(rotateHandleRadius + currentRotateHandleDistanceFromCenter),
                (rotateHandleRadius*2),(rotateHandleRadius*2));

    }
    private static void drawRectangle(Rectangle rectangle, Graphics2D gPad, boolean forSelectionDrawing)
    {
        double w = rectangle.getWidth();
        double h = rectangle.getHeight();

        if(!forSelectionDrawing) //DRAWING SHAPE
        {
            gPad.setColor(rectangle.getColor());
            transformAndRotateShape(gPad, rectangle);
            //DRAW
            gPad.fillRect((int)-w/2, (int)-h/2, (int) w, (int) h);
            //RESET THE PAD
            resetGraphicsPad(gPad);
        }
        else // DRAWING SELECTION LINES AND CIRCLE
        {
            gPad.setColor(highlightColor);
            transformAndRotateShape(gPad, rectangle);
            //DRAW SELECTION BOUNDING BOX
            gPad.drawRect((int) -w/2,(int) -h/2,(int) w,(int) h);
            drawRotateHandle(gPad);
            resetGraphicsPad(gPad);
        }

    }
    private static void drawCircle(Circle circle, Graphics2D gPad, boolean forSelectionDrawing)
    {
        int radius = (int) circle.getRadius();

        if(!forSelectionDrawing) {
            gPad.setColor(circle.getColor());

            transformAndRotateShape(gPad, circle);

            //DRAW
            gPad.fillOval(-radius, -radius, radius * 2, radius * 2);
            //RESET THE PAD
            resetGraphicsPad(gPad);
        }
        else  //for selection drawing
        {
            gPad.setColor(highlightColor);

            transformAndRotateShape(gPad, circle);

            gPad.drawRect(-radius, (int) -radius, radius * 2, radius * 2);

            resetGraphicsPad(gPad);
        }








    }
    private static void drawEllipse(Ellipse e, Graphics2D gPad, boolean forSelectionDrawing)
    {
        double w = e.getWidth();
        double h = e.getHeight();

        if(!forSelectionDrawing) //DRAWING SHAPE
        {
            gPad.setColor(e.getColor());
            transformAndRotateShape(gPad, e);
            //DRAW
            gPad.fillOval((int)-w/2, (int)-h/2, (int) w, (int) h);
            //RESET THE PAD
            resetGraphicsPad(gPad);
        }
        else // DRAWING SELECTION LINES AND CIRCLE
        {
            gPad.setColor(highlightColor);
            transformAndRotateShape(gPad, e);
            //DRAW SELECTION BOUNDING BOX
            gPad.drawRect((int) -w/2,(int) -h/2,(int) w,(int) h);
            drawRotateHandle(gPad);
            resetGraphicsPad(gPad);
        }

    }
    private static void drawTriangle(Triangle t, Graphics2D gPad, boolean forDrawingSelection)
    {
        Point2D.Double center = t.getCenter();

        //Point2D.Double a = convertObjectCoordsToWorldCoords(center, t.getA());
        //Point2D.Double b = convertObjectCoordsToWorldCoords(center, t.getB());
        //Point2D.Double c = convertObjectCoordsToWorldCoords(center, t.getC());
        Point2D.Double a = t.getA();
        Point2D.Double b = t.getB();
        Point2D.Double c = t.getC();


        int xA = (int) a.x;
        int xB = (int) b.x;
        int xC = (int) c.x;
        
        int yA = (int) a.y;
        int yB = (int) b.y;
        int yC = (int) c.y;
    
        int[] xCoords = {xA,xB,xC};
        int[] yCoords = {yA,yB,yC};
        int points = 3;

        transformAndRotateShape(gPad,t);

        if(!forDrawingSelection)
        {
            gPad.setColor(t.getColor());
            gPad.fillPolygon(xCoords, yCoords, points);
            resetGraphicsPad(gPad);
        }
        else //DRAW SELECTION BOUNDS AND ROTATE HANDLE
        {
            gPad.setColor(highlightColor);
            gPad.drawPolygon(xCoords, yCoords, points);
            drawRotateHandle(gPad);
            resetGraphicsPad(gPad);
        }

    }
    private static Point2D.Double calculateUpperLeft(Point2D.Double center, double width, double height) 
    {
        double halfWidth = width/2;
        double halfHeight = height/2;
        
        return new Point2D.Double(center.x - halfWidth, center.y - halfHeight);
    }
    private static Point2D.Double convertObjectCoordsToWorldCoords (Point2D.Double centerWC, Point2D.Double pointOC)
    {
        return new Point2D.Double(pointOC.x + centerWC.x, pointOC.y + centerWC.y);


    }
    private static Point2D.Double convertWorldCoordsToObjectCoords (Point2D.Double centerWC, Point2D.Double pointWC)
    {
        return new Point2D.Double(pointWC.x - centerWC.x, pointWC.y - centerWC.y);
    }
    static void drawShapeSelection(Shape shape, Graphics2D g2d, boolean drawSelection)
    {
        //LINE
        //SQUARE
        //RECTANGLE
        //CIRCLE
        //ELLIPSE
        //TRIANGLE
        if(shape instanceof Line)
        {
            Line line = (Line) shape;
            drawLine(line,g2d,drawSelection);

            return;
        }
        if(shape instanceof Square)
        {
            Square s = (Square) shape;
            drawSquare(s, g2d, drawSelection);
            return;
        }
        if(shape instanceof Rectangle)
        {
            Rectangle r = (Rectangle) shape;
            drawRectangle(r,g2d,drawSelection);

            return;
        }
        if(shape instanceof Circle)
        {
            Circle c = (Circle) shape;
            drawCircle(c,g2d,drawSelection);

            return;
        }
        if(shape instanceof Ellipse)
        {
            Ellipse e = (Ellipse) shape;
            drawEllipse(e,g2d,drawSelection);
            return;
        }
        if(shape instanceof Triangle)
        {
            Triangle t = (Triangle) shape;
            drawTriangle(t, g2d, drawSelection);

        }
    }
    private static void transformAndRotateShape(Graphics2D gPad, Shape shape)
    {
        // create a new transformation (defaults to identity)
        AffineTransform objToWorld = new AffineTransform();

        double rotRadians = Math.toRadians(shape.getRotation());

        objToWorld.translate(shape.getCenter().x, shape.getCenter().y);
        objToWorld.rotate(rotRadians);

        gPad.setTransform(objToWorld);

    }
    private static void resetGraphicsPad(Graphics2D gPad)
    {
        AffineTransform identity = new AffineTransform();
        gPad.setTransform(identity);
    }


}
