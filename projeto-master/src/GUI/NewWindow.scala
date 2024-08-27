package GUI

import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.scene.control.Button
import javafx.stage.Stage

object NewWindow {

  def openNextWindow(b: Button, str: String): Unit = {
    val curr_window = b.getScene.getWindow //gets current window
    val new_window = new FXMLLoader(getClass.getResource(str)) //gets new window
    val new_window_root: Parent = new_window.load() // loads new window
    val new_scene = new Scene(new_window_root) //makes new scene with the new window
    val new_stage = new Stage() // sets the new stage to show new window *
    new_stage.setScene(new_scene) // = *
    curr_window.hide() // closes current window
    new_stage.show() // opend new window
  }

}
