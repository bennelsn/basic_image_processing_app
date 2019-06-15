package cs355.controller;

/**
 * Created by BenNelson on 2/20/17.
 *
 */
public class ZoomControl
{
    public static final double zoom400 = 4;
    public static final double zoom200 = 2;
    public static final double zoom100 = 1;
    public static final double zoom50 = .5;
    public static final double zoom25 = .25;
    public static boolean zoomIn = false;
    public static boolean zoomOut = false;
    public static boolean currentlyZooming = false;

    public static double currentZoom;


    public ZoomControl()
    {
        //Default zoom level
        currentZoom = zoom100;
    }

    public double getCurrentZoom()
    {
        return currentZoom;
    }

    public void setCurrentZoom(double currentZoom)
    {
        this.currentZoom = currentZoom;
    }

    public void changeCurrentZoom(double value)
    {
        if(value > zoom400)
        {
            this.currentZoom = zoom400;
        }
        else if(value < zoom25)
        {
            this.currentZoom = zoom25;
        }
        else
        {
            this.currentZoom = value;
        }
    }
}
