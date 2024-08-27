package Hex

import Hex.HexBoard.Board
import scala.annotation.tailrec

// case classe para definir o game state: tem o board, o jogador, ou seja, a cor que o user usa no jogo
// e tem o old para guardar a versão anterior do quadro caso seja feito undo
case class HexBoard(board: Board, player: Cells.Cell, old: Board) extends Serializable

//objeto com as funções para alterar o board
object HexBoard {

  type Board = List[List[Cells.Cell]]

  //cria um board com as cells todas empty do tamanho no lines
  //o hex segundo pesquisado tem sempre o mesmo número de linhas e colunas
  def createBoard(lines: Int): Board = {
    def emptyBoard(rows: Int, cols: Int): Board = {
      def emptyRows(cols: Int): List[Cells.Cell] = {
        def cell: Cells.Value = Cells.Empty

        if (cols == 1)
          List(cell)
        else
          cell :: emptyRows(cols - 1)
      }

      if (rows == 1)
        List(emptyRows(cols))
      else
        emptyRows(cols) :: emptyBoard(rows - 1, cols)
    }

    emptyBoard(lines, lines)
  }

  //T1
  @tailrec
  def randomMove(board: Board, rand: MyRandom): ((Int, Int), MyRandom) = {
    val (i1, state2) = rand.nextInt(board.head.length)
    val (i2, state3) = state2.nextInt(board.length)
    if (board(i1)(i2) != Cells.Empty) randomMove(board, state3) else ((i1, i2), state3)
  }

  //T2
  def play(board: Board, player: Cells.Cell, row: Int, col: Int): Board = {
    board match {
      case Nil => throw new Exception("board empty. cant play")
      case h :: t =>
        val aux = row
        if (aux != 0) h :: play(t, player, aux - 1, col) else if (h(col) == Cells.Empty) board.updated(0, h.updated(col, player)) else board;
    }
  }

}
