/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs355.view;

import cs355.GUIFunctions;
import cs355.controller.MyController;
import cs355.controller.MyViewport;
import cs355.controller.ZoomControl;
import cs355.model.drawing.MyModel;
import cs355.model.drawing.Shape;

import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;

/**
 *
 * @author BenNelson
 */
public class MyViewer implements ViewRefresher
{
    private final MyModel model;
    public static MyController controller;
    private MyDrawer drawer;
    public static BasicStroke lineThickness;
    public MyViewer(MyModel model, MyController controller)
    {
        this.model = model;
        MyViewer.controller = controller;
        this.drawer = new MyDrawer(controller);
        lineThickness = new BasicStroke(1);
        model.addObserver(this);
    }

    @Override
    public void refreshView(Graphics2D g2d) {
        
       //DRAWS ALL THE SHAPES IN THE MODEL______________________________________________________________________________
       ArrayList<Shape> shapes = (ArrayList<Shape>) model.getShapes();
        for (Shape shape : shapes)
        {
            drawer.drawShape(shape, g2d, false);
        }//______________________________________________________________________________________________________________


       //DRAWS SELECTIONS BOUNDS AND ROTATE HANDLES ON SHAPES___________________________________________________________
        String tool_button = MyController.current_tool_button;
       int shapeIndex = MyController.current_selected_shape_index;

       if(tool_button != null)
       {
           if ((tool_button.equals(MyController.SELECTION_TOOL)) && shapeIndex != MyController.NO_SELECTION)
           {
               //get what shape we need to select and draw the selection borders
               Shape shape = shapes.get(shapeIndex);
               drawer.drawShapeSelection(shape, g2d, true);
           }
       }//______________________________________________________________________________________________________________
    }

    @Override
    public void update(Observable o, Object arg) {
        GUIFunctions.refresh();
    }
    
}
