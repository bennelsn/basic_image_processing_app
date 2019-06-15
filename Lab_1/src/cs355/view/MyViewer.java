/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs355.view;

import cs355.GUIFunctions;
import cs355.model.drawing.MyModel;
import cs355.model.drawing.Shape;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Observable;

/**
 *
 * @author BenNelson
 */
public class MyViewer implements ViewRefresher
{
    private final MyModel model;
    public MyViewer (MyModel model)
    {
        this.model = model;
        model.addObserver(this);
    }

    @Override
    public void refreshView(Graphics2D g2d) {
        
       //Draw on the g2d variable
       ArrayList<Shape> shapes = (ArrayList<Shape>) model.getShapes();
       for(int i =0; i < shapes.size(); i++)
       {
           Shape shape = shapes.get(i);
           MyDrawer.drawShape(shape, g2d);
       }
    }

    @Override
    public void update(Observable o, Object arg) {
        GUIFunctions.refresh();
    }
    
}
