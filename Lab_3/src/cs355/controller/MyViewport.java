package cs355.controller;

import cs355.GUIFunctions;

import java.awt.geom.Point2D;

/**
 * Created by BenNelson on 2/20/17.
 *
 */
public class MyViewport
{
    private Point2D.Double upL; //Upper Left Corner
    public Point2D.Double previousUpL;
    public int viewPortWidth = 512;
    public int viewPortHeight = 512;
    public static boolean isAlreadyXBounded = false;
    public static boolean isAlreadyYBounded = false;


    public MyViewport()
    {
        this.upL = new Point2D.Double(300,300);
        this.previousUpL = upL;//new Point2D.Double(0,0);

    }
    public Point2D.Double getUpL() {
        return upL;
    }
    public void setUpL(Point2D.Double upL) {
        this.upL = upL;
    }
    public void setUpLeftX(double x)
    {
        upL = new Point2D.Double(x, this.upL.y);
    }
    public void setUpLeftY(double y)
    {
        upL = new Point2D.Double(this.upL.x, y);
    }


    public void accountForZoom(double currentZoom)
    {
        int boundLimit = 0;
        if(currentZoom == ZoomControl.zoom400)
        {
            //WONT EVER BE HERE
        }
        else if(currentZoom == ZoomControl.zoom200)
        {
            //NOW if the zoom has just been switched here, then the viewport upleft corner cant be higher than scrollMax - knob200
            boundLimit = ScrollControl.scrollMax - ScrollControl.knob200;
            adjustViewportUpLeftCorner(boundLimit, currentZoom);

        }
        else if(currentZoom == ZoomControl.zoom100)
        {
            //NOW if the zoom has just been switched here, then the viewport upleft corner cant be higher than scrollMax - knob100
            boundLimit = ScrollControl.scrollMax - ScrollControl.knob100;
            adjustViewportUpLeftCorner(boundLimit, currentZoom);

        }
        else if(currentZoom == ZoomControl.zoom50)
        {
            //NOW if the zoom has just been switched here, then the viewport upleft corner cant be higher than scrollMax - knob50
            boundLimit = ScrollControl.scrollMax - ScrollControl.knob50;
            adjustViewportUpLeftCorner(boundLimit, currentZoom);
        }
        else // current zoom is now zoom25
        {
            //Save the zoom before full world view
            previousUpL = new Point2D.Double(upL.x,upL.y);

            //NOW if the zoom has just been switched here, then the viewport upleft corner cant be higher than scrollMax - knob25
            boundLimit = ScrollControl.scrollMax - ScrollControl.knob25;
            adjustViewportUpLeftCorner(boundLimit, currentZoom);
        }
    }

    private void adjustViewportUpLeftCorner(int boundLimit, double currentZoom)
    {
        if(currentZoom == ZoomControl.zoom25)
        {
            setUpLeftX(0);
            setUpLeftY(0);

        }
        else
        {
            if (this.upL.x > boundLimit) {
                setUpLeftX(boundLimit);
                isAlreadyXBounded = true;
            }

            if (this.upL.y > boundLimit) {
                setUpLeftY(boundLimit);
                isAlreadyYBounded = true;
            }
        }
    }


    public void printUpLeftValues()
    {
        String x = Double.toString(upL.x);
        String y = Double.toString(upL.y);
        GUIFunctions.printf("VP: X = " + x +" and Y = " + y);
    }
}
