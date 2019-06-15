package cs355.solution;

import cs355.GUIFunctions;
import cs355.controller.MyController;
import cs355.controller.MyViewport;
import cs355.controller.ZoomControl;
import cs355.model.drawing.MyModel;
import cs355.view.MyViewer;

/**
 * This is the main class. The program starts here.
 * Make you add code below to initialize your model,
 * view, and controller and give them to the app.
 */
public class CS355 {

	/**
	 * This is where it starts.
	 * @param args = the command line arguments
	 */
	public static void main(String[] args) {

		// Fill in the parameters below with your controller and view.
		//Make an instance of the model
		ZoomControl zoom = new ZoomControl();
		MyModel model = new MyModel();
		MyViewport viewport = new MyViewport();


		MyController controller = new MyController(model,zoom,viewport);
		MyViewer viewer = new MyViewer(model, controller);
              
		GUIFunctions.createCS355Frame(controller, viewer);

		controller.initializeScrollBars();
		controller.initializeColor();

		GUIFunctions.refresh();
	}
}
