/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs355.controller;

import cs355.GUIFunctions;
import cs355.model.drawing.*;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static cs355.controller.MyController.*;
import static java.lang.Math.abs;

/**
 *
 * @author BenNelson
 */
class ControllerUtilities
{
    static Point2D.Double get2DPoint(Point p)
    {
        double x_pos = p.x;
        double y_pos = p.y;
       
        return new Point2D.Double(x_pos, y_pos);
    }
    
    private static Point2D.Double getWidthAndHeight(Point2D.Double origin, Point2D.Double destination)
    {
      
        double width = destination.x - origin.x;
        double height = destination.y - origin.y;
        double w = abs(width);
        double h = abs(height);
       
        return new Point2D.Double(w,h);
       
    }
    
    static Shape createShape(Point2D.Double start, Point2D.Double end, String currentTool, Color currentColor)
    {
        //Convert the click points from View to World COORDINATES
        start = convertViewCoordsToWorldCoords(start);
        end = convertViewCoordsToWorldCoords(end);

        if(currentTool.equals(MyController.LINE))
        {
            Point2D.Double endObj = convertWorldCoordsToObjectCoords(start,end);
            return new Line(currentColor, start, endObj);

        }
        else if(currentTool.equals(SQUARE))
        {
            return createSquare(start, end, currentColor);
        }
        else if(currentTool.equals(RECTANGLE))
        {
            return createRectangle(start, end, currentColor);
        }
        else if(currentTool.equals(CIRCLE))
        {
            return createCircle(start, end, currentColor);
        }
        else if(currentTool.equals(ELLIPSE))
        {
            return createEllipse(start, end, currentColor);
        }
        else if(currentTool.equals(TRIANGLE))
        {
            if(triCount == 3)
            {
                return createTriangle(currentColor);
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
  
    }

    private static Point2D.Double convertViewCoordsToWorldCoords(Point2D.Double pt)
    {
        //the given pt will be in View Coords.
        //We normally scale and then translate in order to go view to world, however, because of JAVA let's translate then scale

        Point2D.Double p = viewport.getUpL();

        double m00 = 1;  double m01 = 0;  double m02 = p.x;
        double m10 = 0;  double m11 = 1;  double m12 = p.y;

        AffineTransform viewToWorld = new AffineTransform(m00,m10, m01, m11,m02,m12);

        double f = zoom.getCurrentZoom();

        m00 = (1/f); m01 = 0; m02 = 0;
        m10 = 0; m11 = (1/f); m12 = 0;

        AffineTransform scaleMatrix = new AffineTransform(m00,m10, m01, m11,m02,m12);

        viewToWorld.concatenate(scaleMatrix);

        Point2D.Double ptInWorldCoords = new Point2D.Double();
        viewToWorld.transform(pt, ptInWorldCoords);

        return ptInWorldCoords;

    }

    private static Shape createRectangle(Point2D.Double start, Point2D.Double end, Color currentColor)
    {
        Point2D.Double wh = getWidthAndHeight(start,end);
            //Case Quadrant 4
            if((end.x > start.x) && (end.y > start.y))
            {
                //The current start is the upper left
                Point2D.Double centerPoint = convertUpLeftToCenter(start, wh);
                Shape s = new Rectangle(currentColor,centerPoint,wh.x,wh.y);
                return s;
            }
            //Case Quadrant 3
            else if((end.x < start.x) && (end.y > start.y))
            {
                //In this case the upperleft should be the x point of the end coords and the y of the start coords
               
                Point2D.Double upperleft = new Point2D.Double(end.x, start.y);
                Point2D.Double centerPoint = convertUpLeftToCenter(upperleft, wh);

                Shape s = new Rectangle(currentColor,centerPoint,wh.x,wh.y);
                return s;
                
            }
            //Case Quadrant 2
            else if((end.x < start.x) && (end.y < start.y))
            {
                //upperleft should be the end point in this case
                Point2D.Double centerPoint = convertUpLeftToCenter(end, wh);

                Shape s = new Rectangle(currentColor,centerPoint,wh.x,wh.y);
                return s;
            }
            else //Case Quadrant 1
            {
                Point2D.Double upperleft = new Point2D.Double(start.x, end.y);
                Point2D.Double centerPoint = convertUpLeftToCenter(upperleft, wh);

                Shape s = new Rectangle(currentColor,centerPoint,wh.x,wh.y);
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
                return new Ellipse(currentColor,center,wh.x,wh.y);
            }
            //Case Quadrant 3
            else if((end.x < start.x) && (end.y > start.y))
            {
                //In this case the upperleft should be the x point of the end coords and the y of the start coords
               
                Point2D.Double center = new Point2D.Double(start.x - halfWidth, start.y + halfHeight);
                return new Ellipse(currentColor,center,wh.x,wh.y);
                
            }
            //Case Quadrant 2
            else if((end.x < start.x) && (end.y < start.y))
            {
                //upperleft should be the end point in this case
                Point2D.Double center = new Point2D.Double(start.x - halfWidth, start.y - halfHeight);
                return new Ellipse(currentColor,center,wh.x,wh.y);
            }
            else //Case Quadrant 1
            {
                Point2D.Double center = new Point2D.Double(start.x + halfWidth, start.y - halfHeight);
                Point2D.Double upperleft = new Point2D.Double(start.x, end.y);
                return new Ellipse(currentColor,center,wh.x,wh.y);
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
                Point2D.Double center = convertUpLeftToCenter(start,new Point2D.Double(squareSize,squareSize));
                Shape s = new Square(currentColor,center,squareSize);
                return s;
            }
            //Case Quadrant 3
            else if((end.x < start.x) && (end.y > start.y))
            {
                //In this case the upperleft should be the x point of the end coords and the y of the start coords
               
                Point2D.Double upperleft = new Point2D.Double(start.x - squareSize, start.y);
                Point2D.Double center = convertUpLeftToCenter(upperleft,new Point2D.Double(squareSize,squareSize));
                Shape s = new Square(currentColor,center,squareSize);
                return s;
                
            }
            //Case Quadrant 2
            else if((end.x < start.x) && (end.y < start.y))
            {
                //upperleft should be the end point in this case
                Point2D.Double upperleft = new Point2D.Double(start.x - squareSize, start.y - squareSize);
                Point2D.Double center = convertUpLeftToCenter(upperleft,new Point2D.Double(squareSize,squareSize));
                Shape s = new Square(currentColor,center,squareSize);
                return s;
            }
            else //Case Quadrant 1
            {
                Point2D.Double upperleft = new Point2D.Double(start.x, start.y - squareSize);
                Point2D.Double center = convertUpLeftToCenter(upperleft,new Point2D.Double(squareSize,squareSize));
                Shape s = new Square(currentColor,center,squareSize);
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
        //Convert the clicks from view to world
        MyController.triA = convertViewCoordsToWorldCoords(MyController.triA);
        MyController.triB = convertViewCoordsToWorldCoords(MyController.triB);
        MyController.triC = convertViewCoordsToWorldCoords(MyController.triC);

        Point2D.Double center = computeCentroid(MyController.triA, MyController.triB, MyController.triC);

        //Convert the clicks form world to object
        Point2D.Double a = convertWorldCoordsToObjectCoords(center, MyController.triA);
        Point2D.Double b = convertWorldCoordsToObjectCoords(center, MyController.triB);
        Point2D.Double c = convertWorldCoordsToObjectCoords(center, MyController.triC);

        MyController.triCount = 0;

        return new Triangle(currentColor, center, a ,b ,c);
    }


    private static Point2D.Double convertUpLeftToCenter(Point2D.Double upL, Point2D.Double widthAndHeight)
    {
        double halfWidth = widthAndHeight.x/2;
        double halfHeight = widthAndHeight.y/2;

        return new Point2D.Double(upL.x + halfWidth, upL.y + halfHeight);

    }

    private static Point2D.Double computeCentroid(Point2D.Double pA, Point2D.Double pB, Point2D.Double pC)
    {
        int numOfPoints = 3;
        double x = (pA.x + pB.x + pC.x)/numOfPoints;
        double y = (pA.y + pB.y + pC.y)/numOfPoints;

        return new Point2D.Double(x,y);
    }


    private static int selectShapes(ArrayList<Shape> shapesList, Point2D.Double current_click)
    {
        for(int index = shapesList.size() - 1; index >= 0; index--)
        {
            Shape shape = shapesList.get(index);
            //LINE
            //SQUARE
            //RECTANGLE
            //CIRCLE
            //ELLIPSE
            //TRIANGLE
            //check if that shape lies in the selection

            if(shape instanceof Line)
            {
                Line l = (Line) shape;
                double tolerance = getCurrentLineToleranceBasedOnZoom();
                boolean isInShape = l.pointInShape(current_click,tolerance);
                if(isInShape)
                {
                    changeColor(shape);
                    return index;
                }
            }
            if(shape instanceof Square)
            {
                Square s = (Square) shape;
                boolean isInShape = s.pointInShape(current_click,0);
                if(isInShape)
                {
                    changeColor(shape);
                    return index;
                }

            }
            if(shape instanceof Rectangle)
            {
                Rectangle r = (Rectangle) shape;
                boolean isInShape = r.pointInShape(current_click,0);
                if(isInShape)
                {
                    changeColor(shape);
                    return index;
                }
            }
            if(shape instanceof Circle)
            {
                Circle c = (Circle) shape;
                boolean isInShape = c.pointInShape(current_click,0);
                if(isInShape)
                {
                    changeColor(shape);
                    return index;
                }
            }
            if(shape instanceof Ellipse)
            {
                Ellipse e = (Ellipse) shape;
                boolean isInShape = e.pointInShape(current_click,0);
                if(isInShape)
                {
                    changeColor(shape);
                    return index;
                }
            }
            if(shape instanceof Triangle)
            {
                Triangle t = (Triangle) shape;
                boolean isInShape = t.pointInShape(current_click,0);
                if(isInShape)
                {
                    changeColor(shape);
                    return index;
                }
            }

        }

        return NO_SELECTION;

    }

    private static void changeColor(Shape shape)
    {
        current_color = shape.getColor();
        GUIFunctions.changeSelectedColor(current_color);
    }


    private static Point2D.Double convertWorldCoordsToObjectCoords (Point2D.Double centerWC, Point2D.Double pointWC)
    {
        return new Point2D.Double(pointWC.x - centerWC.x, pointWC.y - centerWC.y);
    }

    private static Point2D.Double convertObjectCoordsToWorldCoords (Point2D.Double centerWC, Point2D.Double pointOC)
    {
        return new Point2D.Double(pointOC.x + centerWC.x, pointOC.y + centerWC.y);
    }


    static void translateShape(Shape currentShape, Point2D.Double start, Point2D.Double end)
    {
        // we want the center to be the end point, but just added to it is the distance of the initial point to the center

        //Convert these click points from view to world
        start = convertViewCoordsToWorldCoords(start);
        end = convertViewCoordsToWorldCoords(end);

        calculateSelectionDistance(currentShape, start);
        Point2D.Double newCenter = new Point2D.Double(end.x - selectionOffset.x, end.y - selectionOffset.y);

            if((currentShape instanceof Line) && (startLineHandleSelected || endLineHandleSelected))
            {
                Line line = (Line) currentShape;
                //DRAG LINE ENDPOINTS
                if(startLineHandleSelected)
                {
                    //We need to get the current start point
                    //end is the current drag point
                    Point2D.Double lineStart = line.getCenter();
                    Point2D.Double changeForEndPoint = getChangeToAddToLineEnd(lineStart, end);

                    //Update the startpoint in world coords
                    line.setCenter(end);

                    //Now update the endpoint in object coords
                    Point2D.Double currentEndPoint = line.getEnd();
                    Point2D.Double newEndPoint = new Point2D.Double((currentEndPoint.x + changeForEndPoint.x),(currentEndPoint.y + changeForEndPoint.y));
                    line.setEnd(newEndPoint);

                }
                else //end point
                {
                    //Simply update the endpoint
                    Point2D.Double endObjCoords = convertWorldCoordsToObjectCoords(currentShape.getCenter(),end);
                    line.setEnd(endObjCoords);
                }
            }
            else
            {
                if(!(currentShape instanceof Line)) {
                    currentShape.setCenter(newCenter);
                }
            }



    }

    private static Point2D.Double getChangeToAddToLineEnd(Point2D.Double startPt, Point2D.Double dragPt)
    {
        double sX = startPt.x;
        double sY = startPt.y;

        double dX = dragPt.x;
        double dY = dragPt.y;

        double finalX = dX - sX;
        double finalY = dY - sY;

        //if finalX is positive, that means the start drag point is going to move up, so the end point needs to move down,
        //so we need the opposite sign of finalX to go into the change for the end point we are creating, and same for finalY

        return new Point2D.Double(-finalX,-finalY);

    }

    private static void calculateSelectionDistance(Shape currentShape, Point2D.Double start)
    {
        if(!selectionDistanceCalculated)
        {

            double cX = currentShape.getCenter().x;
            double cY = currentShape.getCenter().y;

            double iX = start.x;
            double iY = start.y;

            double distanceX = iX - cX;
            double distanceY = iY - cY;

            selectionDistanceCalculated = true;

            selectionOffset = new Point2D.Double(distanceX,distanceY);

        }

    }

    static boolean shapeToolIsSet()
    {
        if((current_tool_button.equals(LINE)) || (current_tool_button.equals(SQUARE)) || (current_tool_button.equals(RECTANGLE))
                || (current_tool_button.equals(CIRCLE)) || (current_tool_button.equals(ELLIPSE)))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    static boolean shapeIsSelected()
    {
        if((current_tool_button.equals(SELECTION_TOOL)) && (current_selected_shape_index != NO_SELECTION))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    static void moveForward(MyModel model)
    {
        ArrayList shapes = (ArrayList) model.getShapes();
        Shape s = (Shape) shapes.get(current_selected_shape_index);
        shapes.remove(current_selected_shape_index);
        current_selected_shape_index++;

        int sizeOfList = shapes.size();

        if(current_selected_shape_index > sizeOfList)
        {
            current_selected_shape_index--;
        }
        shapes.add(current_selected_shape_index,s);

        GUIFunctions.refresh();
    }

    static void moveBackward(MyModel model)
    {
        ArrayList shapes = (ArrayList) model.getShapes();
        Shape s = (Shape) shapes.get(current_selected_shape_index);
        shapes.remove(current_selected_shape_index);
        current_selected_shape_index--;

        if(current_selected_shape_index < 0)
        {
            current_selected_shape_index++;
        }
        shapes.add(current_selected_shape_index,s);

        GUIFunctions.refresh();
    }

    static void moveToFront(MyModel model)
    {
        ArrayList shapes = (ArrayList) model.getShapes();
        Shape s = (Shape) shapes.get(current_selected_shape_index);
        shapes.remove(current_selected_shape_index);
        shapes.add(s);

        current_selected_shape_index = shapes.size() - 1;

        GUIFunctions.refresh();
    }

    static void moveToBack(MyModel model)
    {
        ArrayList shapes = (ArrayList) model.getShapes();
        Shape s = (Shape) shapes.get(current_selected_shape_index);
        shapes.remove(current_selected_shape_index);
        current_selected_shape_index = 0;

        shapes.add(current_selected_shape_index,s);

        GUIFunctions.refresh();
    }

    static boolean rotateHandleSelected(Point2D.Double clickWCoords, Shape shape)
    {
        Point2D.Double pt = ShapeUtilities.getClickedPointInObjectCoords(clickWCoords,shape);
        //Is pt in the current rotate handle?
        if(shape instanceof Line)
        {
            return isClickInLineSelectionHandle(pt, shape);
        }
        if(isClickInRotateHandle(pt))
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    private static boolean isClickInLineSelectionHandle(Point2D.Double q, Shape shape)
    {
        if(shape instanceof Line)
        {
            Line line = (Line) shape;
            //WE need to test if it is at the start point or the end point.
            Point2D.Double start = new Point2D.Double(0,0);
            Point2D.Double end = line.getEnd(); // The end is already stored in object coords.


            boolean isInStart = isClickInALineHandle(start, q);
            boolean isInEnd = isClickInALineHandle(end,q);

            if(isInStart)
            {
                startLineHandleSelected = true;
                endLineHandleSelected = false;
                return true;
            }
            else if(isInEnd)
            {
                startLineHandleSelected = false;
                endLineHandleSelected = true;
                return true;
            }
            else
            {
                return false;
            }

        }
        else
        {
            return false;
        }
    }

    private static boolean isClickInALineHandle(Point2D.Double c, Point2D.Double q)
    {
        if(((q.x - c.x) * (q.x - c.x)) + ((q.y - c.y) * (q.y - c.y))  <= (rotateHandleRadius*rotateHandleRadius))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private static boolean isClickInRotateHandle(Point2D.Double q)
    {
        //So the rotate hand should always have an x value of 0 and a y value currentRotateHandleDistanceFromCenter
        Point2D.Double c = new Point2D.Double(0, -currentRotateHandleDistanceFromCenter);

        if(((q.x - c.x) * (q.x - c.x)) + ((q.y - c.y) * (q.y - c.y))  <= (rotateHandleRadius*rotateHandleRadius))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private static Circle squareRotateHandle(Square square)
    {
        //We want to make this circle relative to the shape it is connected to
        //It's center is going to be relative to the object coords of the current square, not the world
        double size = square.getSize();
        double yOffsetValue = ((size/2) + rotateHandleOffset);

        Circle c = new Circle(square.getColor(), new Point2D.Double(0, -yOffsetValue), rotateHandleRadius);

        return c;
    }


    static void checkIfClickedInShape(MyModel model)
    {
        ArrayList<Shape> shapesList = (ArrayList<Shape>) model.getShapes(); //GET SHAPES LIST
        current_selected_shape_index = selectShapes(shapesList, current_click); //SELECT THE SHAPE IF CLICK IS IN IT

        //IF A SHAPE IS SELECTED FROM THE CLICK, THEN MAKE A ROTATION HANDLE FROM IT____________________________________
        if(current_selected_shape_index != NO_SELECTION)
        {
            makeRotationHandle(model);

        }//_____________________________________________________________________________________________________________

        GUIFunctions.refresh();
    }

    static void makeRotationHandle(MyModel model)
    {
        Shape currentShape = model.getShape(current_selected_shape_index);
        setRotateHandleDistanceFromCenter(currentShape);
        if(currentShape instanceof Line)
        {

            Point2D.Double q = convertViewCoordsToWorldCoords(current_click);
            Point2D.Double pt = convertWorldCoordsToObjectCoords(currentShape.getCenter(),q);
            //check if it is selecting a handle
            isClickInLineSelectionHandle(pt, currentShape);
        }
    }

    static void rotateShape(Shape currentShape, Point2D.Double dragPoint)
    {
        //Use the current shape and adjust the dragPoint from view to world to object coords

        dragPoint = convertViewCoordsToWorldCoords(dragPoint);
        //Now the dragpoint is in world coords

        //Convert the drag point to object centered coords
        if(!(currentShape instanceof Line))
        {
            Point2D.Double convertedDragPoint = convertWorldCoordsToObjectCoords(currentShape.getCenter(), dragPoint);
            double theta = Math.atan2(convertedDragPoint.y, convertedDragPoint.x);
            double degrees = Math.toDegrees(theta);
            currentShape.setRotation(degrees + 90); //WHY ADD 90? I HAVE NO IDEA!!!
        }
    }


    public static void setRotateHandleDistanceFromCenter(Shape shape)
    {
        //Square
        //Rectangle
        //Ellipse
        //Triangle
        if(shape instanceof Square)
        {
            setRHForSquare((Square)shape);
        }
        if(shape instanceof Rectangle)
        {
            setRHForRectAndEllipse(shape);
        }
        if(shape instanceof Ellipse)
        {
            setRHForRectAndEllipse(shape);
        }
        if(shape instanceof Triangle)
        {
            setRHForTriangle((Triangle)shape);
        }

    }

    private static void setRHForSquare(Square square)
    {
        double size = square.getSize();

        currentRotateHandleDistanceFromCenter = (size/2) + rotateHandleOffset;
    }

    private static void setRHForRectAndEllipse(Shape shape)
    {
        if(shape instanceof Rectangle)
        {
            Rectangle rect = (Rectangle) shape;
            double height = rect.getHeight();
            currentRotateHandleDistanceFromCenter = (height / 2) + rotateHandleOffset;
        }
        else
        {
            Ellipse ellipse = (Ellipse) shape;
            double height = ellipse.getHeight();
            currentRotateHandleDistanceFromCenter = (height / 2) + rotateHandleOffset;
        }
    }

    public static void setRHForTriangle(Triangle t)
    {
        double shortestDistance = calculateShortestDistanceFromCentroid(t);
        double longestDistance = calculateLongestDistanceFromCentroid(t);

        if(!((longestDistance/2) > shortestDistance))
        {
            currentRotateHandleDistanceFromCenter = longestDistance + rotateHandleOffset;
        }
        else
        {
            currentRotateHandleDistanceFromCenter = shortestDistance + rotateHandleOffset;
        }

    }

    private static double calculateShortestDistanceFromCentroid(Triangle t)
    {
        //Since the points on the triangle are already in object coords, we can make the center (0,0)
        //Point2D.Double center = new Point2D.Double(0,0);

        Point2D.Double a = t.getA();
        Point2D.Double b = t.getB();
        Point2D.Double c = t.getC();

        double distanceOne =  (a.x * a.x) + (a.y * a.y);
        double distanceTwo = (b.x * b.x) + (b.y * b.y);
        double distanceThree = (c.x * c.x) + (c.y * c.y);

        if((distanceOne <= distanceTwo) && (distanceOne <= distanceThree))
        {
            return Math.sqrt(distanceOne);
        }
        else if((distanceTwo <= distanceOne) && (distanceTwo <= distanceThree))
        {
            return Math.sqrt(distanceTwo);
        }
        else
        {
            return Math.sqrt(distanceThree);
        }

    }

    private static double calculateLongestDistanceFromCentroid(Triangle t)
    {
        //Since the points on the triangle are already in object coords, we can make the center (0,0)
        //Point2D.Double center = new Point2D.Double(0,0);

        Point2D.Double a = t.getA();
        Point2D.Double b = t.getB();
        Point2D.Double c = t.getC();

        double distanceOne =  (a.x * a.x) + (a.y * a.y);
        double distanceTwo = (b.x * b.x) + (b.y * b.y);
        double distanceThree = (c.x * c.x) + (c.y * c.y);

        if((distanceOne >= distanceTwo) && (distanceOne >= distanceThree))
        {
            return Math.sqrt(distanceOne);
        }
        else if((distanceTwo >= distanceOne) && (distanceTwo >= distanceThree))
        {
            return Math.sqrt(distanceTwo);
        }
        else
        {
            return Math.sqrt(distanceThree);
        }

    }

    private static double getCurrentLineToleranceBasedOnZoom()
    {
        double zoomLevel = zoom.getCurrentZoom();

        if(zoomLevel == ZoomControl.zoom25)
        {
            return defaultLineTolerance * 4;
        }
        else if(zoomLevel == ZoomControl.zoom50)
        {
            return defaultLineTolerance * 2;
        }
        else if(zoomLevel == ZoomControl.zoom100)
        {
            return defaultLineTolerance;
        }
        else if(zoomLevel == ZoomControl.zoom200)
        {
           return defaultLineTolerance/2;
        }
        else// if(zoomLevel == ZoomControl.zoom400)
        {
            return defaultLineTolerance/4;
        }
    }

    public static void selectingShapes(MyModel model)
    {
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

    }

    public static void creatingShapes(MouseEvent e, MyModel model)
    {
        if(shapeToolIsSet() || current_tool_button.equals(TRIANGLE))
        {
            if (current_tool_button.equals(TRIANGLE)) {
                recordTriangleClicks(e);
            }

            Shape s = createShape(current_start, current_start, current_tool_button, current_color);
            if (s != null) {
                current_shape_index = model.addShape(s);
            }
        }
    }


    private static void recordTriangleClicks(MouseEvent e)
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

    public static void translatingOrRotating(Point p, MyModel model )
    {
        //get the shape and start moving it relative to the drag point.
        Shape currentShape = model.getShape(current_selected_shape_index);

        Point2D.Double endDragPoint = get2DPoint(p);

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

    public static void updatingShapeBeingCreated(Point2D.Double current_end, MyModel model)
    {
        Shape s = ControllerUtilities.createShape(current_start, current_end, current_tool_button, current_color);
        model.deleteShape(current_shape_index);

        if (s != null)
        {
            model.addShape(s);
        }
    }
}
