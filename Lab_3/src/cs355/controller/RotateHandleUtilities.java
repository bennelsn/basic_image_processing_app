package cs355.controller;

import cs355.GUIFunctions;
import cs355.model.drawing.Shape;

import java.util.List;

/**
 * Created by BenNelson on 2/23/17.
 *
 */
public class RotateHandleUtilities
{

    public static void adjustRotateHandlesForZoom(double zoomLevel)
    {

        if(zoomLevel == ZoomControl.zoom25)
        {
            //here the rotate handle needs to be 4 times bigger
            MyController.rotateHandleRadius = (MyController.defaultRotateHandleRadius * 4);

            MyController.rotateHandleOffset = (MyController.defaultRotateHandleOffset * 4);
        }
        else if(zoomLevel == ZoomControl.zoom50)
        {
            //here the rotate handle needs to be 2 times bigger
            MyController.rotateHandleRadius = (MyController.defaultRotateHandleRadius * 2);

            MyController.rotateHandleOffset = (MyController.defaultRotateHandleOffset * 2);
        }
        else if(zoomLevel == ZoomControl.zoom100)
        {
            //here the rotate handle needs to be default
            MyController.rotateHandleRadius = (MyController.defaultRotateHandleRadius);

            MyController.rotateHandleOffset = (MyController.defaultRotateHandleOffset);
        }
        else if(zoomLevel == ZoomControl.zoom200)
        {
            //here the rotate handle needs to be 1/2 the size of default
            MyController.rotateHandleRadius = (MyController.defaultRotateHandleRadius/2);

            MyController.rotateHandleOffset = (MyController.defaultRotateHandleOffset/2);
        }
        else// if(zoomLevel == ZoomControl.zoom400)
        {
            //here the rotate handle needs to be 1/4 the size of default
            MyController.rotateHandleRadius = (MyController.defaultRotateHandleRadius/4);

            MyController.rotateHandleOffset = (MyController.defaultRotateHandleOffset/4);
        }


    }

    public static void setRotateHandleDistanceForZoom(List<Shape> shapes)
    {
        for(Shape shape : shapes)
        {
            ControllerUtilities.setRotateHandleDistanceFromCenter(shape);
        }
    }
}
