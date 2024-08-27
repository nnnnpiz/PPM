package GUI

import GUI.NewWindow.openNextWindow
import Hex.Game._
import Hex.{Cells, MyRandom}
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.Polygon

class ControllerPlayWindows {

  @FXML
  private var redButton: Button = _ // new game window
  @FXML
  private var blueButton: Button = _ // new game window
  @FXML
  private var goBackButtonNew: Button = _ // new game window                     
  @FXML
  private var goBackButtonCont: Button = _ // cont game window
  @FXML
  private var hexagon11: Polygon = _ // boardWindow
  @FXML
  private var hexagon12: Polygon = _ // boardWindow
  @FXML
  private var hexagon13: Polygon = _ // boardWindow
  @FXML
  private var hexagon14: Polygon = _ // boardWindow
  @FXML
  private var hexagon15: Polygon = _ // boardWindow
  @FXML
  private var hexagon21: Polygon = _ // boardWindow
  @FXML
  private var hexagon22: Polygon = _ // boardWindow
  @FXML
  private var hexagon23: Polygon = _ // boardWindow
  @FXML
  private var hexagon24: Polygon = _ // boardWindow
  @FXML
  private var hexagon25: Polygon = _ // boardWindow
  @FXML
  private var hexagon31: Polygon = _ // boardWindow
  @FXML
  private var hexagon32: Polygon = _ // boardWindow
  @FXML
  private var hexagon33: Polygon = _ // boardWindow
  @FXML
  private var hexagon34: Polygon = _ // boardWindow
  @FXML
  private var hexagon35: Polygon = _ // boardWindow
  @FXML
  private var hexagon41: Polygon = _ // boardWindow
  @FXML
  private var hexagon42: Polygon = _ // boardWindow
  @FXML
  private var hexagon43: Polygon = _ // boardWindow
  @FXML
  private var hexagon44: Polygon = _ // boardWindow
  @FXML
  private var hexagon45: Polygon = _ // boardWindow
  @FXML
  private var hexagon51: Polygon = _ // boardWindow
  @FXML
  private var hexagon52: Polygon = _ // boardWindow
  @FXML
  private var hexagon53: Polygon = _ // boardWindow
  @FXML
  private var hexagon54: Polygon = _ // boardWindow
  @FXML
  private var hexagon55: Polygon = _ // boardWindow
  @FXML
  private var undoButton: Button = _ // boardWindow
  @FXML
  private var goBackBoardButton: Button = _ // boardWindow
  @FXML
  private var infoLabel: Label = _ // boardWindow
  // cores a usar nos hexagonos
  private val color_red = "#ff0000"
  private val color_blue = "#0070c0"
  private val color_empty = "#d7d7d7"
  private val color_hover = "#b9b9b9"
  // MyRandom and seed
  private val seed = 10
  private val rand = MyRandom(seed)

  // these 2 functions below are just for aesthetics
  def insideHexagon(event: MouseEvent): Unit = {
    val hexa = event.getSource.asInstanceOf[Polygon]
    if (!(hexa.getFill == Color.web(color_red) || hexa.getFill == Color.web(color_blue))) {
      hexa.setFill(Color.web(color_hover))
    }
  } // board window

  def outsideHexagon(event: MouseEvent): Unit = {
    val hexa = event.getSource.asInstanceOf[Polygon]
    if (!(hexa.getFill == Color.web(color_red) || hexa.getFill == Color.web(color_blue))) {
      hexa.setFill(Color.web(color_empty))
    }
  } // board window

  def goBackButtonNewPressed(): Unit = {
    openNextWindow(goBackButtonNew, "StartGameWindow.fxml")
  } // new game window

  def redButtonPressed(): Unit = {
    openNextWindow(redButton, "BoardWindow.fxml"); newGame(Cells.Red)
  } // new game window

  def blueButtonPressed(): Unit = {
    openNextWindow(blueButton, "BoardWindow.fxml"); newGame(Cells.Blue)
  } // new game window

  def goBackButtonContPressed(): Unit = {
    openNextWindow(goBackButtonCont, "StartGameWindow.fxml")
  } // cont game window

  def goBackBoardButtonPressed(): Unit = {
    openNextWindow(goBackBoardButton, "StartGameWindow.fxml")
    saveGame()
  } // board window

  def undoButtonPressed(): Unit = {
    infoLabel.setText("")
    val old_game = getGame.board
    undo()
    val new_game = getGame.board
    //TODO colours
  }

  def hexagonPressed(event: MouseEvent): Unit = {
    infoLabel.setText("")
    val hexa = event.getSource.asInstanceOf[Polygon]
    val id = hexa.getId
    val row = id.init.last.toInt - 48 // como é um char o número 1 é 49
    val col = id.last.toInt - 48
    if (hexa.getFill == Color.web(color_red) || hexa.getFill == Color.web(color_blue)) {
      infoLabel.setText("Posição já usada! Escolha outra")
    } else {
      val player = getGame.player
      userPlay(row, col)
      val pc = computerPlay(rand)
      if (player == Cells.Red) {
        hexa.setFill(Color.web(color_red))
        setColour(pc._1, pc._2, color_blue)
      } else {
        hexa.setFill(Color.web(color_blue))
        setColour(pc._1, pc._2, color_red)
      }

    }
  }

  private def setColour(x: Int, y: Int, color: String): Unit = {
    x match {
      case 1 => y match {
        case 1 => hexagon11.setFill(Color.web(color))
        case 2 => hexagon12.setFill(Color.web(color))
        case 3 => hexagon13.setFill(Color.web(color))
        case 4 => hexagon14.setFill(Color.web(color))
        case 5 => hexagon15.setFill(Color.web(color))
      }
      case 2 => y match {
        case 1 => hexagon21.setFill(Color.web(color))
        case 2 => hexagon22.setFill(Color.web(color))
        case 3 => hexagon23.setFill(Color.web(color))
        case 4 => hexagon24.setFill(Color.web(color))
        case 5 => hexagon25.setFill(Color.web(color))
      }
      case 3 => y match {
        case 1 => hexagon31.setFill(Color.web(color))
        case 2 => hexagon32.setFill(Color.web(color))
        case 3 => hexagon33.setFill(Color.web(color))
        case 4 => hexagon34.setFill(Color.web(color))
        case 5 => hexagon35.setFill(Color.web(color))
      }
      case 4 => y match {
        case 1 => hexagon41.setFill(Color.web(color))
        case 2 => hexagon42.setFill(Color.web(color))
        case 3 => hexagon43.setFill(Color.web(color))
        case 4 => hexagon44.setFill(Color.web(color))
        case 5 => hexagon45.setFill(Color.web(color))
      }
      case 5 => y match {
        case 1 => hexagon51.setFill(Color.web(color))
        case 2 => hexagon52.setFill(Color.web(color))
        case 3 => hexagon53.setFill(Color.web(color))
        case 4 => hexagon54.setFill(Color.web(color))
        case 5 => hexagon55.setFill(Color.web(color))
      }
    }
  }

}
