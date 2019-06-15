package cs355.model.drawing;

import cs355.GUIFunctions;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Map;

import static java.lang.Math.abs;

/**
 * Created by BenNelson on 2/7/17.
 *
 */
public class ShapeUtilities
{

    public static Point2D.Double getClickedPointInObjectCoords(Point2D.Double pt, Shape shape)
    {

        //Check if the given pt is within the shape

        AffineTransform worldToObj = new AffineTransform();
        double shapeRotationInRadians = Math.toRadians(shape.rotation);

        worldToObj.rotate(-shapeRotationInRadians);
        worldToObj.translate(-shape.center.x, -shape.center.y);

        Point2D.Double convertedPoint = new Point2D.Double();
        worldToObj.transform(pt, convertedPoint);

        return convertedPoint;

    }

    public static boolean isClickedPointInSquare(Point2D.Double clickPoint, double size)
    {
        Point2D.Double upLeft = new Point2D.Double((-size/2),(-size/2));
        Point2D.Double botRight = new Point2D.Double((size/2),(size/2));

        if((clickPoint.x >= upLeft.x) && (clickPoint.y >= upLeft.y)
                && (clickPoint.x <= botRight.x) && (clickPoint.y <= botRight.y))
        {
            //then we know it is in the square
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean isClickedPointInCircle(Point2D.Double p, double radius)
    {
        // || p - c || ^2 must be less that r^2
        // since c is always at (0,0) in our case, we can just say
        // ||p||^2 must be less than r^2

        if(((p.x * p.x) + (p.y * p.y)) < (radius * radius))
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    public static boolean isClickedPointInEllipse(Point2D.Double q, double width, double height)
    {
        //   if  ((qx - cx)/(width/2))^2 + ((qy - cy)/(height/2))^2) <= 1 then we are good

        //   Since we are in object COORDS right now, the cx and cy = 0
        //So we dont need them in the equation
        Point2D.Double c = new Point2D.Double(0,0);

        double value = (((q.x - c.x)/(width/2)) * ((q.x - c.x)/(width/2))) + (((q.y - c.y)/(height/2)) * ((q.y - c.y)/(height/2)));

        if(value <= 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean isClickedPointInRectangle(Point2D.Double clickPoint, double width, double height)
    {
        Point2D.Double upLeft = new Point2D.Double((-width/2),(-height/2));
        Point2D.Double botRight = new Point2D.Double((width/2),(height/2));

        if((clickPoint.x >= upLeft.x) && (clickPoint.y >= upLeft.y)
                && (clickPoint.x <= botRight.x) && (clickPoint.y <= botRight.y))
        {
            //then we know it is in the rectangle
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean isClickedPointInTriangle(Point2D.Double q, Triangle triangle)
    {
        /*
        (q - p1) dot (p2 - p1)ORTH  = result1
        (q - p2) dot (p3 - p2)ORTH  = result2
        (q - p3) dot (p1 - p3)ORTH  = result3

        if results are ALL negative or ALL positve, then return true, else false;
         */
        Point2D.Double p1 = triangle.getA();
        Point2D.Double p2 = triangle.getB();
        Point2D.Double p3 = triangle.getC();

        //(q - p1) dot (p2 - p1)ORTH  = result1________________________________
        Point2D.Double a1 = new Point2D.Double((q.x - p1.x),(q.y - p1.y));
        Point2D.Double b1 = new Point2D.Double((p2.x - p1.x),(p2.y - p1.y));
        Point2D.Double b1_ORTHO = orthogonalize(b1);

        double resultOne = dotProductOf(a1,b1_ORTHO);
        //_____________________________________________________________________

        //(q - p2) dot (p3 - p2)ORTH  = result2________________________________
        Point2D.Double a2 = new Point2D.Double((q.x - p2.x),(q.y - p2.y));
        Point2D.Double b2 = new Point2D.Double((p3.x - p2.x),(p3.y - p2.y));
        Point2D.Double b2_ORTHO = orthogonalize(b2);

        double resultTwo = dotProductOf(a2,b2_ORTHO);
        //_____________________________________________________________________

        //(q - p3) dot (p1 - p3)ORTH  = result3________________________________
        Point2D.Double a3 = new Point2D.Double((q.x - p3.x),(q.y - p3.y));
        Point2D.Double b3 = new Point2D.Double((p1.x - p3.x),(p1.y - p3.y));
        Point2D.Double b3_ORTHO = orthogonalize(b3);

        double resultThree = dotProductOf(a3,b3_ORTHO);
        //_____________________________________________________________________


        if(allResultsArePosOrNeg(resultOne,resultTwo,resultThree))
        {
            return true;
        }
        else
        {
            return false;
        }


    }

    private static boolean allResultsArePosOrNeg(double resultOne, double resultTwo, double resultThree)
    {
        if(    ((resultOne > 0) && (resultTwo > 0) && (resultThree > 0))    ||
                ((resultOne < 0) && (resultTwo < 0) && (resultThree < 0)) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private static double dotProductOf(Point2D.Double v1, Point2D.Double v2)
    {
        return  (v1.x * v2.x) + (v1.y * v2.y);
    }

    private static Point2D.Double orthogonalize(Point2D.Double pt)
    {
        double x = pt.x;
        double y = pt.y;

        return new Point2D.Double(-y,x);
    }

    public static boolean isClickedPointInLine(Point2D.Double q, Point2D.Double p2, double tolerance)
    {

        Point2D.Double p1 = new Point2D.Double(0,0);

        boolean withInLineSegment = checkIfClickPointIsWithinLineSegment(q, p1, p2, tolerance);


        if(!withInLineSegment)
        {
            return false;
        }

        //Find n of the line
        Point2D.Double p1Minusp2 = new Point2D.Double((p1.x - p2.x), (p1.y - p2.y));
        Point2D.Double p1Minusp2Ortho = orthogonalize(p1Minusp2);

        double squaredlength = dotProductOf(p1Minusp2Ortho,p1Minusp2Ortho);

        double length = Math.sqrt(squaredlength);

        Point2D.Double n = new Point2D.Double((p1Minusp2Ortho.x/length),(p1Minusp2Ortho.y/length));
        // a point on the line, p dotted with n equals the distance d
        double d = dotProductOf(p1, n);

        // |q dot n - d|  gives us the distance of q(the clicked point) from the line

        double distanceOfClickFromLine = (dotProductOf(q,n)) - d;
        distanceOfClickFromLine = Math.abs(distanceOfClickFromLine);



        if(distanceOfClickFromLine <= tolerance)
        {
            //it is in the line
            return true;
        }
        else
        {
            return false;
        }


    }

    private static boolean checkIfClickPointIsWithinLineSegment(Point2D.Double q, Point2D.Double p1, Point2D.Double p2, double tolerance)
    {
        //QUADRANT 1
        //if end is x+ and y+
        //p1 should be (0,0) here
        if((p2.x >= 0) && (p2.y >= 0))
        {
            if((q.x >= (p1.x- tolerance)) && (q.y >= (p1.y - tolerance)) && (q.x <= (p2.x + tolerance)) && (q.y <= (p2.y + tolerance)))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        //QUADRANT 3
        //if end is x- and y-
        else if((p2.x < 0) && (p2.y < 0))
        {
            if((q.x <= (p1.x + tolerance)) && (q.y <= (p1.y + tolerance)) && (q.x >= (p2.x - tolerance)) && (q.y >= (p2.y - tolerance)))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        //QUADRANT 2
        //if end is x- and y+
        else if((p2.x < 0) && (p2.y >=0))
        {
            if((q.x <= (p1.x + tolerance)) && (q.y >= (p1.y - tolerance)) && (q.x >= (p2.x - tolerance)) && (q.y <= (p2.y + tolerance)))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        //QUADRANT 4
        //if end is x+ and y-
        else
        {
            if((q.x >= (p1.x - tolerance)) && (q.y <= (p1.y + tolerance)) && (q.x <= (p2.x + tolerance)) && (q.y >= (p2.y - tolerance)))
            {
                return true;
            }
            else
            {
                return false;
            }

        }

    }

}
