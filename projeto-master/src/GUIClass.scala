import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

class GUIClass extends Application {
  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("Hex Game App")
    val fxmlLoader =
      new FXMLLoader(getClass.getResource("GUI/GameWindow.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    primaryStage.setScene(scene)
    primaryStage.show()
  }
}

object FxApp {
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[GUIClass], args: _*)
  }

}

