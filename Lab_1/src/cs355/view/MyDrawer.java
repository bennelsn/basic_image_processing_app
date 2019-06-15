/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs355.view;

import cs355.model.drawing.Circle;
import cs355.model.drawing.Ellipse;
import cs355.model.drawing.Line;
import cs355.model.drawing.Rectangle;
import cs355.model.drawing.Shape;
import cs355.model.drawing.Square;
import cs355.model.drawing.Triangle;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

/**
 *
 * @author BenNelson
 */
public class MyDrawer {
    
    public static void drawShape(Shape shape, Graphics2D graphicsPad)
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
            drawLine(line, graphicsPad);
            return;  
        }
        if(shape instanceof Square)
        {
            Square s = (Square) shape;
            drawSquare(s, graphicsPad);
            return;
        }
        if(shape instanceof Rectangle)
        {
            Rectangle r = (Rectangle) shape;
            drawRectangle(r, graphicsPad);
            return;
        }
        if(shape instanceof Circle)
        {
            Circle c = (Circle) shape;
            drawCircle(c,graphicsPad);
            return;
        }
        if(shape instanceof Ellipse)
        {
            Ellipse e = (Ellipse) shape;
            drawEllipse(e, graphicsPad);
            return;
        }
        if(shape instanceof Triangle)
        {
            Triangle t = (Triangle) shape;
            drawTriangle(t, graphicsPad);
            return;
        }
    }

    private static void drawLine(Line line, Graphics2D graphicsPad) 
    {
        Point2D.Double start = line.getStart();
        Point2D.Double end = line.getEnd();
        
        graphicsPad.setColor(line.getColor());
        graphicsPad.drawLine((int)start.x, (int)start.y, (int)end.x, (int)end.y);
        
    }
    private static void drawSquare(Square square, Graphics2D gPad) 
    {
        Point2D.Double upperleft = square.getUpperLeft();
        int size = (int)square.getSize();

        gPad.setColor(square.getColor());
        gPad.fillRect((int)upperleft.x, (int)upperleft.y, size, size);
        
        
 
       
    }
    private static void drawRectangle(Rectangle rectangle, Graphics2D gPad) 
    {
        Point2D.Double ul = rectangle.getUpperLeft();
        int w = (int) rectangle.getWidth();
        int h = (int) rectangle.getHeight();
        
        gPad.setColor(rectangle.getColor());
        gPad.fillRect((int)ul.x, (int)ul.y, w, h);
    }
    private static void drawCircle(Circle circle, Graphics2D gPad) 
    {
      double centerX = circle.getCenter().x;
      double centerY = circle.getCenter().y;
      double radius = circle.getRadius();
      double sideLength = radius * 2;
      
      Point2D.Double upLeft = calculateUpperLeft(circle.getCenter(),sideLength,sideLength);
      
      gPad.setColor(circle.getColor());
      gPad.fillOval((int)upLeft.x, (int)upLeft.y, (int)sideLength , (int)sideLength);
       
       
        
    }
    private static void drawEllipse(Ellipse e, Graphics2D gPad) 
    {
      double centerX = e.getCenter().x;
      double centerY = e.getCenter().y;
      double w = e.getWidth();
      double h = e.getHeight();
      
      Point2D.Double upperLeft = calculateUpperLeft(e.getCenter(),w,h);
      
      gPad.setColor(e.getColor());
      gPad.fillOval((int)upperLeft.x, (int)upperLeft.y, (int)w ,(int)h);
    }
    private static void drawTriangle(Triangle t, Graphics2D gPad) 
    {
        int xA = (int) t.getA().x;
        int xB = (int) t.getB().x;
        int xC = (int) t.getC().x;
        
        int yA = (int) t.getA().y;
        int yB = (int) t.getB().y;
        int yC = (int) t.getC().y;
    
        int[] xCoords = {xA,xB,xC};
        int[] yCoords = {yA,yB,yC};
        int points = 3;
        
        gPad.setColor(t.getColor());
        gPad.fillPolygon(xCoords, yCoords, points);
    }

    private static Point2D.Double calculateUpperLeft(Point2D.Double center, double width, double height) 
    {
        double halfWidth = width/2;
        double halfHeight = height/2;
        
        return new Point2D.Double(center.x - halfWidth, center.y - halfHeight);
    }
    
}
