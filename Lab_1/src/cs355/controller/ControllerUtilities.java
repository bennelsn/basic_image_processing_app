/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs355.controller;

import cs355.model.drawing.Circle;
import cs355.model.drawing.Ellipse;
import cs355.model.drawing.Line;
import cs355.model.drawing.Rectangle;
import cs355.model.drawing.Shape;
import cs355.model.drawing.Square;
import cs355.model.drawing.Triangle;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import static java.lang.Math.abs;

/**
 *
 * @author BenNelson
 */
public class ControllerUtilities 
{
    public static Point2D.Double get2DPoint(Point p)
    {
        double x_pos = p.x;
        double y_pos = p.y;
       
        return new Point2D.Double(x_pos, y_pos);
    }
    
    public static Point2D.Double getWidthAndHeight(Point2D.Double origin, Point2D.Double destination)
    {
      
        double width = destination.x - origin.x;
        double height = destination.y - origin.y;
        double w = abs(width);
        double h = abs(height);
       
        return new Point2D.Double(w,h);
       
    }
    
    public static Shape createShape(Point2D.Double start, Point2D.Double end, String currentSelection, Color currentColor)
    {
        
        if(currentSelection.equals(MyController.LINE))
        {
            //using the point, make a line
            Shape s = new Line(currentColor, start, end);
            return s;
            
        }
        else if(currentSelection.equals(MyController.SQUARE))
        {
            return createSquare(start, end, currentColor);
        }
        else if(currentSelection.equals(MyController.RECTANGLE))
        {
            return createRectangle(start, end, currentColor);
        }
        else if(currentSelection.equals(MyController.CIRCLE))
        {
            return createCircle(start, end, currentColor);
        }
        else if(currentSelection.equals(MyController.ELLIPSE))
        {
            return createEllipse(start, end, currentColor);
        }
        else if(currentSelection.equals(MyController.TRIANGLE))
        {
            if(MyController.triCount == 3)
            {
                return createTriangle(currentColor);
            }
            else return null;
        }
        else
        {
            return null;
        }
  
    }
            
    public static Shape createRectangle(Point2D.Double start, Point2D.Double end, Color currentColor)
    {
        Point2D.Double wh = getWidthAndHeight(start,end);
            //Case Quadrant 4
            if((end.x > start.x) && (end.y > start.y))
            {
                //The current start is the upper left
                Shape s = new Rectangle(currentColor,start,wh.x,wh.y);
                return s;
            }
            //Case Quadrant 3
            else if((end.x < start.x) && (end.y > start.y))
            {
                //In this case the upperleft should be the x point of the end coords and the y of the start coords
               
                Point2D.Double upperleft = new Point2D.Double(end.x, start.y);
                Shape s = new Rectangle(currentColor,upperleft,wh.x,wh.y);
                return s;
                
            }
            //Case Quadrant 2
            else if((end.x < start.x) && (end.y < start.y))
            {
                //upperleft should be the end point in this case
                Shape s = new Rectangle(currentColor,end,wh.x,wh.y);
                return s;
            }
            else //Case Quadrant 1
            {
                Point2D.Double upperleft = new Point2D.Double(start.x, end.y);
                Shape s = new Rectangle(currentColor,upperleft,wh.x,wh.y);
                return s;
            }
    }

    private static Shape createEllipse(Point2D.Double start, Point2D.Double end, Color currentColor) 
    {
        Point2D.Double wh = getWidthAndHeight(start,end);
        double halfWidth = wh.x/2;
        double halfHeight = wh.y/2;
            //Case Quadrant 4
            if((end.x > start.x) && (end.y > start.y))
            {
                //The current start is the upper left
                Point2D.Double center = new Point2D.Double(start.x + halfWidth, start.y + halfHeight);
                Shape s = new Ellipse(currentColor,center,wh.x,wh.y);
                return s;
            }
            //Case Quadrant 3
            else if((end.x < start.x) && (end.y > start.y))
            {
                //In this case the upperleft should be the x point of the end coords and the y of the start coords
               
                Point2D.Double center = new Point2D.Double(start.x - halfWidth, start.y + halfHeight);
                Shape s = new Ellipse(currentColor,center,wh.x,wh.y);
                return s;
                
            }
            //Case Quadrant 2
            else if((end.x < start.x) && (end.y < start.y))
            {
                //upperleft should be the end point in this case
                Point2D.Double center = new Point2D.Double(start.x - halfWidth, start.y - halfHeight);
                Shape s = new Ellipse(currentColor,center,wh.x,wh.y);
                return s;
            }
            else //Case Quadrant 1
            {
                Point2D.Double center = new Point2D.Double(start.x + halfWidth, start.y - halfHeight);
                Point2D.Double upperleft = new Point2D.Double(start.x, end.y);
                Shape s = new Ellipse(currentColor,center,wh.x,wh.y);
                return s;
            }
    }

    private static Shape createSquare(Point2D.Double start, Point2D.Double end, Color currentColor) 
    {
        Point2D.Double wh = getWidthAndHeight(start,end);
        double squareSize = getSquareSize(wh);
            //Case Quadrant 4
            if((end.x > start.x) && (end.y > start.y))
            {
                //The current start is the upper left
                Shape s = new Square(currentColor,start,squareSize);
                return s;
            }
            //Case Quadrant 3
            else if((end.x < start.x) && (end.y > start.y))
            {
                //In this case the upperleft should be the x point of the end coords and the y of the start coords
               
                Point2D.Double upperleft = new Point2D.Double(start.x - squareSize, start.y);
                Shape s = new Square(currentColor,upperleft,squareSize);
                return s;
                
            }
            //Case Quadrant 2
            else if((end.x < start.x) && (end.y < start.y))
            {
                //upperleft should be the end point in this case
                Point2D.Double upperleft = new Point2D.Double(start.x - squareSize, start.y - squareSize);
                Shape s = new Square(currentColor,upperleft,squareSize);
                return s;
            }
            else //Case Quadrant 1
            {
                Point2D.Double upperleft = new Point2D.Double(start.x, start.y - squareSize);
                Shape s = new Square(currentColor,upperleft,squareSize);
                return s;
            }   
    }

    private static double getSquareSize(Point2D.Double widthAndHeight) 
    {
        if(widthAndHeight.x > widthAndHeight.y)
        {
            return widthAndHeight.y;
        }
        else
        {
            return widthAndHeight.x;
        }
    }

    private static Shape createCircle(Point2D.Double start, Point2D.Double end, Color currentColor) 
    {
        
        Point2D.Double wh = getWidthAndHeight(start,end);
        double squareSize = getSquareSize(wh);
        double radius = squareSize/2;
        
        
            //Case Quadrant 4
            if((end.x > start.x) && (end.y > start.y))
            {
                //The current start is the upper left
                Point2D.Double center = new Point2D.Double(start.x + radius, start.y + radius);
                Shape s = new Circle(currentColor,center,radius);
                return s;
            }
            //Case Quadrant 3
            else if((end.x < start.x) && (end.y > start.y))
            {
                //In this case the upperleft should be the x point of the end coords and the y of the start coords
               
                Point2D.Double upperleft = new Point2D.Double(start.x - squareSize, start.y);
                Point2D.Double center = new Point2D.Double(upperleft.x + radius, upperleft.y + radius);
                Shape s = new Circle(currentColor,center,radius);
                return s;
                
            }
            //Case Quadrant 2
            else if((end.x < start.x) && (end.y < start.y))
            {
                //upperleft should be the end point in this case
                Point2D.Double upperleft = new Point2D.Double(start.x - squareSize, start.y - squareSize);
                Point2D.Double center = new Point2D.Double(upperleft.x + radius, upperleft.y + radius);
                Shape s = new Circle(currentColor,center,radius);
                return s;
            }
            else //Case Quadrant 1
            {
                Point2D.Double upperleft = new Point2D.Double(start.x, start.y - squareSize);
                Point2D.Double center = new Point2D.Double(upperleft.x + radius, upperleft.y + radius);
                Shape s = new Circle(currentColor,center,radius);
                return s;
            }   
    }

    private static Shape createTriangle(Color currentColor) 
    {
        Shape s = new Triangle(currentColor, MyController.triA ,MyController.triB ,MyController.triC);
        MyController.triCount = 0;
        return s;
    }
}
