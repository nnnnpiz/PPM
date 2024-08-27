package GUI

import javafx.fxml.FXML
import javafx.scene.control.Button
import GUI.NewWindow._

class ControllerMenuWindows {

  @FXML
  private var playButton: Button = _ //game window
  @FXML
  private var goBackButtonStart: Button = _ //start game window
  @FXML
  private var newGameButton: Button = _ //start game window
  @FXML
  private var contGameButton: Button = _ //start game window

  def playButtonPressed(): Unit = {
    openNextWindow(playButton, "StartGameWindow.fxml")
  } //game window

  def newGameButtonPressed(): Unit = {
    openNextWindow(newGameButton, "NewGameWindow.fxml")
  } //start game window

  def contGameButtonPressed(): Unit = {
    openNextWindow(contGameButton, "ContGameWindow.fxml")
  } //start game window

  def goBackButtonStartPressed(): Unit = {
    openNextWindow(goBackButtonStart, "GameWindow.fxml")
  } //start game window

}