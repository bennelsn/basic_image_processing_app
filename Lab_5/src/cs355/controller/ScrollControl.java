package cs355.controller;

import cs355.GUIFunctions;

import java.awt.geom.Point2D;

/**
 * Created by BenNelson on 2/20/17.
 *
 */
public class ScrollControl
{
    public static final int scrollMin = 0;
    public static final int scrollMax = 2048;
    public static final int knob25 = 2048;
    public static final int knob50 = 1024;
    public static final int knob100 = 512;
    public static final int knob200 = 256;
    public static final int knob400 = 128;
    private ZoomControl zoom;
    private MyViewport viewport;
    private final String HORIZONTAL = "horizontal";
    private final String VERTICAL = "vertical";


    public ScrollControl(ZoomControl zoom, MyViewport viewport)
    {
        this.zoom = zoom;
        this.viewport = viewport;

        GUIFunctions.setHScrollBarMin(scrollMin);
        GUIFunctions.setVScrollBarMin(scrollMin);
        GUIFunctions.setHScrollBarMax(scrollMax);
        GUIFunctions.setVScrollBarMax(scrollMax);

        changeScrollBars();
    }

    /*
    private int setBarPos(String axis)
    {
        int knobValue = getCurrentZoom();
        int pos = 0;


        if(axis.equals(HORIZONTAL))
        {
            pos = (int) viewport.getUpL().getX();
        }
        else
        {
            pos = (int) viewport.getUpL().getY();
        }

        return pos - (knobValue/2);
    }
    */

    private int getCurrentZoom()
    {
        double currentZoom = zoom.getCurrentZoom();

        if(currentZoom == zoom.zoom400)
        {
            return knob400;
        }
        else if(currentZoom == zoom.zoom200)
        {
            return knob200;
        }
        else if(currentZoom == zoom.zoom100)
        {
            return knob100;
        }
        else if(currentZoom == zoom.zoom50)
        {
            return knob50;
        }
        else // its zoom25
        {
            return knob25;
        }
    }

    public void changeScrollBars()
    {
        if(MyController.justComingOutZoom25) {

            int viewPortSize = viewport.viewPortWidth;
            int halfWorldSize = scrollMax/2;

            double centerOfWorld = halfWorldSize - viewPortSize;

            GUIFunctions.setHScrollBarPosit((int)centerOfWorld); //SETS WHERE THE KNOB WILL GO
            GUIFunctions.setVScrollBarPosit((int)centerOfWorld);
            GUIFunctions.setVScrollBarKnob(getCurrentZoom()); //SETS HOW BIG THE KNOB IS
            GUIFunctions.setHScrollBarKnob(getCurrentZoom());

            //Have to do it again for the correctness without refreshing
            GUIFunctions.setHScrollBarPosit((int)centerOfWorld); //SETS WHERE THE KNOB WILL GO
            GUIFunctions.setVScrollBarPosit((int)centerOfWorld);
            GUIFunctions.setVScrollBarKnob(getCurrentZoom()); //SETS HOW BIG THE KNOB IS
            GUIFunctions.setHScrollBarKnob(getCurrentZoom());
            viewport.setUpLeftX((int)centerOfWorld);
            viewport.setUpLeftY((int)centerOfWorld);
            MyController.justComingOutZoom25 = false;
        }
        else
        {
            double value = zoom.getCurrentZoom();

            int offset = 0;



            if(ZoomControl.zoomIn)
            {

                offset = getOffsetZoomingIn(value);

                this.viewport.setUpLeftX(viewport.getUpL().x + offset);
                this.viewport.setUpLeftY(viewport.getUpL().y + offset);
            }
            else
            {
                offset = getOffsetZoomingOut(value);

                if(!MyViewport.isAlreadyXBounded)
                {
                    this.viewport.setUpLeftX(viewport.getUpL().x - offset);
                }
                if(!MyViewport.isAlreadyYBounded)
                {
                    this.viewport.setUpLeftY(viewport.getUpL().y - offset);
                }
                accountForNegativeValueViewport();

            }

            GUIFunctions.setHScrollBarPosit(((int) this.viewport.getUpL().x));
            GUIFunctions.setVScrollBarPosit(((int) this.viewport.getUpL().y));
            GUIFunctions.setVScrollBarKnob(getCurrentZoom());
            GUIFunctions.setHScrollBarKnob(getCurrentZoom());

        }

    }

    private void accountForNegativeValueViewport() {
        if(viewport.getUpL().x < 0)
        {
            viewport.setUpLeftX(0);
        }
        if(viewport.getUpL().y < 0)
        {
            viewport.setUpLeftY(0);
        }
    }


    public int getOffsetZoomingOut(double value)
    {

        if(value == zoom.zoom200)
        {
            return knob400/2;
        }
        if(value == zoom.zoom100)
        {
            return knob400;
        }
        if(value == zoom.zoom50)
        {
            return knob200;
        }

        return 0;
    }

    public int getOffsetZoomingIn(double value)
    {
        if(value == zoom.zoom400)
        {
            return knob400/2;
        }
        if(value == zoom.zoom200)
        {
            return knob400;
        }
        if(value == zoom.zoom100)
        {
            return knob200;
        }
        if(value == zoom.zoom50)
        {
            return knob200;
        }

        return 0;
    }
}
