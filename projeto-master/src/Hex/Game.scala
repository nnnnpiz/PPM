package Hex
import Hex.Cells.Cell
import Hex.HexBoard._
import java.io.{File, FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

object Game {

  private val curr_path = "CurrentHexBoard.obj"
  private val curr_hist_path = "CurrentHistory.obj"
  private val emptyBoard = List(): List[List[Cells.Cell]]

  def getGame: HexBoard = {
    // ver se o ficheiro existe e não está vazio
    val file = new File(curr_path)
    if (!(file.exists() && file.length() > 0)) {
      // se estiver vazio ou não existir é retorna nada
      HexBoard(emptyBoard, Cells.Empty, emptyBoard)
    } else {
      // se é retorna o que está guardado no ficheiro
      //abrir stream
      val file_input = new FileInputStream(curr_path)
      val game_input = new ObjectInputStream(file_input)
      // current game
      val game = game_input.readObject().asInstanceOf[HexBoard]
      // fechar stream
      game_input.close()
      file_input.close()
      game
    }
  }

  def setGame(game: HexBoard): Unit = {
    // open output stream
    val file_output = new FileOutputStream(curr_path)
    val game_output = new ObjectOutputStream(file_output)
    // create and write object
    game_output.writeObject(game)
    //close stream
    game_output.close()
    file_output.close()
  }

  def getHistory: List[HexBoard] = {
    // ver se o ficheiro existe e não está vazio
    val file = new File(curr_hist_path)
    if (!(file.exists() && file.length() > 0)) {
      // se estiver vazio ou não existir é retorna nada
      List[HexBoard]()
    } else {
      // se é retorna o que está guardado no ficheiro
      //abrir stream
      val file_input = new FileInputStream(curr_hist_path)
      val history_input = new ObjectInputStream(file_input)
      // current history
      val history = history_input.readObject().asInstanceOf[List[HexBoard]]
      // fechar stream
      history_input.close()
      file_input.close()
      history
    }
  }

  def setHistory(history: List[HexBoard]): Unit = {
    // open output stream
    val file_output = new FileOutputStream(curr_hist_path)
    val history_output = new ObjectOutputStream(file_output)
    // create and write object
    history_output.writeObject(history)
    //close stream
    history_output.close()
    file_output.close()
  }

  def saveGame(): Unit = {
    val history = getHistory
    val game = getGame
    val new_history = history ++ List(game)
    setHistory(new_history)
    newGame(Cells.Empty)
  }

  //cria um gamestate com um board empty e a cor escolhida pelo jogador
  def newGame(player: Cell): Unit = {
    val size = 5
    val new_game = createBoard(size)
    val empty_game = HexBoard(new_game, player, new_game)
    setGame(empty_game)
  }

  //faz uma jogada do lado do utilizador
  def userPlay(row: Int, col: Int): Unit = {
    val game = getGame
    val move = play(game.board, game.player, row - 1, col - 1)
    val res = HexBoard(move, game.player, game.board)
    setGame(res)
  }

  //faz uma jogada do lado do computador
  def computerPlay(rand: MyRandom): (Int, Int, MyRandom) = {
    def aux(game: HexBoard, row: Int, col: Int, pc_player: Cell): Unit = {
      val move = play(game.board, pc_player, row, col)
      val res = HexBoard(move, game.player, game.board)
      setGame(res)
    }

    val game = getGame
    val rand_coords = randomMove(game.board, rand)

    if (game.player == Cells.Red) {
      aux(game, rand_coords._1._1, rand_coords._1._2, Cells.Blue)
    } else {
      aux(game, rand_coords._1._1, rand_coords._1._2, Cells.Red)
    }

    (rand_coords._1._1, rand_coords._1._2, rand_coords._2)
  }

  //T5
  //apos undo o old mantem-se para caso se peça undo novamente o board mantem-se
  def undo(): Unit = {
    val game = getGame
    val new_game = HexBoard(game.old, game.player, game.old)
    setGame(new_game)
  }

}
