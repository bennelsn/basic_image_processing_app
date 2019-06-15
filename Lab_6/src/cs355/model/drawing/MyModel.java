/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs355.model.drawing;

import cs355.GUIFunctions;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author BenNelson
 */
public class MyModel extends CS355Drawing {
    
    private ArrayList<Shape> shapes;
    
    public MyModel()
    {
        this.shapes = new ArrayList();
    }

    @Override
    public Shape getShape(int index)
    {
        return shapes.get(index);
    }

    @Override
    public int addShape(Shape s) {
        GUIFunctions.printf("Just added a shape");
        shapes.add(s);
        
        //NOTIFY THE VIEWER THAT A CHANGE HAS been made.
        setChanged();
        notifyObservers();
        
        int index = shapes.size() - 1; // 1 because the array starts at position 0 not 1.
        return index;
    }

    @Override
    public void deleteShape(int index) {
        shapes.remove(index);
    }

    @Override
    public void moveToFront(int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void movetoBack(int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void moveForward(int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void moveBackward(int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Shape> getShapes() {
        return shapes;
    }

    @Override
    public List<Shape> getShapesReversed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setShapes(List<Shape> shapes) 
    {
        ArrayList<Shape> incomingShapes = (ArrayList<Shape>) shapes;
        this.shapes = incomingShapes;
        
    }
    
}
