import Hex._
import Hex.HexBoard._
import Hex.Draw._
import Hex.Game.{getHistory, setHistory}
import scala.annotation.tailrec
import scala.collection.SortedMap
import scala.util.{Failure, Success}

object TUIClass extends App {

  // strings para a TUI
  private val str_menu = Console.WHITE + "------- MENU -------"
  private val str_game_menu = Console.WHITE + "----- MENU JOGO ----"
  private val str_novo_jogo = Console.WHITE + "----- NOVO JOGO ----"
  private val str_continuar_jogo = Console.WHITE + "-- CONTINUAR JOGO --"
  private val str_divider = Console.WHITE + "--------------------"
  private val str_invalid = "Opção Inválida!\n"
  private val str_invalid_position = "Posição inserida inválida! Tente novamente!"
  private val str_size = "tamanho do board"
  private val str_coords = "Por favor insira as coordenadas onde quer jogar:"
  private val str_row = "linha"
  private val str_col = "coluna"
  private val str_red = "Vermelho"
  private val str_blue = "Azul"
  private val str_new_game = "Novo Jogo"
  private val str_cont_game = "Continuar Jogo"
  private val str_next = "Próxima Jogada"
  private val str_undo = "Undo"
  private val str_go_back = "Voltar ao menu principal"
  private val str_exit = "SAIR"
  // seed e random
  private val seed = 10
  private val rand = MyRandom(seed)
  private val emptyBoard = List(): List[List[Cells.Cell]]
  private val gameState = HexBoard(emptyBoard, Cells.Empty, emptyBoard)
  // opções do loop do player
  private val choosePlayer = SortedMap[Int, CommandLineOption](
    elems =
      1 -> CommandLineOption(str_red, (gameState, _, history) => (gameState, history)),
    2 -> CommandLineOption(str_blue, (gameState, _, history) => (gameState, history)),
  )

  // loop para escolher qual a cor que o utilizador vai ser
  @tailrec
  private def playerLoop(): Int = {
    IO_Utils.optionPromptInt(choosePlayer) match {
      case x =>
        if (x == 1 || x == 2) {
          x
        } else {
          println(str_invalid + str_divider);
          playerLoop()
        }
      case _ => println(str_invalid + str_divider); playerLoop()
    }
  }

  //cria um novo jogo
  private def createGame(rand: MyRandom): (HexBoard, MyRandom) = {
    println(str_novo_jogo)
    val lines = IO_Utils.prompt(str_size)
    val player = playerLoop()
    println(str_divider)
    if (player == 1) {
      val create = createBoard(lines)
      draw(create)
      (HexBoard(create, Cells.Red, create), rand)
    } else {
      val create = createBoard(lines)
      val aux = HexBoard(create, Cells.Blue, create)
      val cp = computerTurn(aux, rand)
      draw(cp._1.board)
      (HexBoard(cp._1.board, Cells.Blue, cp._1.board), rand)
    }
  }

  //faz uma jogada do lado do utilizador
  private def userTurn(game: HexBoard, row: Int, col: Int): HexBoard = {
    val move = play(game.board, game.player, row, col)
    HexBoard(move, game.player, move)
  }

  //faz uma jogada do lado do computador
  private def computerTurn(game: HexBoard, rand: MyRandom): (HexBoard, MyRandom) = {
    val rnd = randomMove(game.board, rand)

    def computerPlayer(pc: Cells.Cell): (HexBoard, MyRandom) = {
      val move = play(game.board, pc, rnd._1._1, rnd._1._2)
      (HexBoard(move, game.player, move), rand)
    }

    if (game.player == Cells.Red) {
      computerPlayer(Cells.Blue)
    } else {
      computerPlayer(Cells.Red)
    }
  }

  //faz uma jogada "completa": uma jogada do utlizador seguida por uma do computador para o utlizador poder jogar novamente
  @tailrec
  private def fullPlay(game: HexBoard, rand: MyRandom): (HexBoard, MyRandom) = {
    println(str_divider)
    println(str_coords)
    val row = IO_Utils.prompt(str_row)
    val col = IO_Utils.prompt(str_col)
    if (row > game.board.length || col > game.board.length) {
      println(str_invalid_position)
      fullPlay(game, rand)
    } else {
      println(str_divider)
      val usr = userTurn(game, row - 1, col - 1)
      if (usr.board != game.board) {
        val cmp = computerTurn(usr, rand)
        draw(cmp._1.board)
        (HexBoard(cmp._1.board, game.player, game.board), cmp._2)
      } else {
        println(str_invalid_position)
        fullPlay(game, rand)
      }
    }
  }

  //T5
  //apos undo o old mantem-se para caso se peça undo novamente o board mantem-se
  private def undo(game: HexBoard): HexBoard = {
    val s = HexBoard(game.old, game.player, game.old)
    println(str_divider)
    draw(s.board)
    s
  }

  //pede ao jogador para escolher um dos jogos inacabados anteriormente e devolve esse jogo para poder continuar a jogar
  private def continueGame(rand: MyRandom): List[HexBoard] = {
    val history = getHistory
    println(str_continuar_jogo)
    val index = printHistory(history)
    val lst = (history foldRight List[HexBoard]())((x, b) => if (history.indexOf(x) != index) List(x) ++ b else b)
    gameLoop(history(index), rand, lst)
  }

  // escreve todos os jogos anteriores no terminal e devolve o indice do jogo que o jogador escolheu
  private def printHistory(history: List[HexBoard]): Int = {
    def getStrNum(hist: List[HexBoard], n: Int): List[(String, Int)] = {
      hist match {
        case Nil => List((str_divider, n))
        case x :: b =>
          val s = Console.WHITE + n + ")"
          println(s)
          val g = draw(x.board)
          (s + g, n) :: getStrNum(b, n + 1)
      }
    }

    @tailrec
    def getIndex: Int = {
      val len = history.length
      IO_Utils.getUserInputInt match {
        case Success(i) => if (i > 0 && i <= len) return i - 1 else println(str_invalid + str_divider); getIndex
        case Failure(_) => println(str_invalid + str_divider); getIndex
      }
    }

    getStrNum(history, 1)
    getIndex
  }

  //opções do menu do jogo
  private val gameMenu = SortedMap[Int, CommandLineOption](
    elems =
      1 -> CommandLineOption(str_next, (gameState, rand, history) => (fullPlay(gameState, rand)._1, history)),
    2 -> CommandLineOption(str_undo, (gameState, _, history) => (undo(gameState), history)),
    3 -> CommandLineOption(str_go_back, (gameState, _, history) => (gameState, history)),
    0 -> CommandLineOption(str_exit, (_, _, _) => sys.exit(0))
  )

  //menu de jogo
  @tailrec
  private def gameLoop(game: HexBoard, rand: MyRandom, history: List[HexBoard]): List[HexBoard] = {
    println(str_game_menu)
    IO_Utils.optionPrompt(gameMenu) match {
      case Some(opt) =>
        val aux = opt.exec(game, rand, history)
        if (opt.name != str_go_back) {
          gameLoop(aux._1, rand, aux._2)
        } else {
          history ++ List(aux._1)
        }
      case _ => println(str_invalid + str_divider); gameLoop(game, rand, history)
    }
  }

  // opções do menu principal
  private val mainMenu = SortedMap[Int, CommandLineOption](
    elems =
      1 -> CommandLineOption(str_new_game, (gameState, rand, history) => (gameState, gameLoop(createGame(rand)._1, rand, history))),
    2 -> CommandLineOption(str_cont_game, (gameState, rand, _) => (gameState, continueGame(rand))),
    0 -> CommandLineOption(str_exit, (_, _, _) => sys.exit(0))
  )

  // menu principal
  @tailrec
  private def mainLoop(rand: MyRandom, history: List[HexBoard]): List[HexBoard] = {
    println(str_menu)
    IO_Utils.optionPrompt(mainMenu) match {
      case Some(opt) =>
        val aux = opt.exec(gameState, rand, history)
        if (opt.name != str_exit) {
          mainLoop(rand, aux._2)
        } else {
          setHistory(history)
          history
        }
      case _ => println(str_invalid + str_divider); mainLoop(rand, history)
    }
  }

  mainLoop(rand, getHistory)

}