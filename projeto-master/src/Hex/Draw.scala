package Hex

import Hex.HexBoard._

object Draw {

  private val num_spaces = "   "
  private val blueSE = Console.BLUE + "*   "
  private val redStart = Console.RED + "* "
  private val redEnd = Console.RED + " *"
  private val middle = Console.WHITE + " - "
  private val left = Console.WHITE + "\\ "
  private val right = Console.WHITE + "/ "
  private val empty = Console.WHITE + "."
  private val blue = Console.BLUE + "0"
  private val red = Console.RED + "X"
  private val espaco = " "
  private val emptylst = List()

  //devolve a string correspondente ao valor da cell (empty, red, blue)
  private def printCell(cells: Cells.Cell): String = {
    if (cells == Cells.Empty)
      empty
    else if (cells == Cells.Red)
      red
    else
      blue
  }

  //pega numa string e repete-a x vezes ex: "*" x = 5 => "*****"
  private def repeatString(s: String, x: Int): String = {
    def addxTimes(s: String, x: Int, lst: List[String]): List[String] = {
      if (x == 0)
        lst
      else
        List(s) ++ addxTimes(s, x - 1, lst)
    }

    val lst = addxTimes(s, x, emptylst)
    (lst foldRight "")(_ + _)
  }

  //desenha o numero de espaços iniciais necessários para o board ficar na diagonal como no enunciado
  private def drawSpaces(row: Int, con: Boolean): String = {
    if (con)
      repeatString(espaco, 2 * row + 3)
    else
      repeatString(espaco, 2 * row)
  }

  //desenha uma row ou seja * . - ... - . *
  private def drawRow(lst: List[Cells.Cell]): String = {
    lst match {
      case Nil => ""
      case x :: b =>
        if (lst.indexOf(x) == lst.length - 1)
          printCell(x)
        else
          printCell(x) + middle + drawRow(b)
    }
  }

  //desenha a linha de conecção ( \ / \ ...) e junta-a à linha de cima, exceto quando é a última
  private def drawConnectedRow(lst: List[Cells.Cell], index: Int, len: Int): (String, String) = {
    val aux = index + 1
    val start = Console.WHITE + aux + drawSpaces(index, con = false) + redStart
    val cells = drawRow(lst)
    val connectline = drawSpaces(index, con = true) + repeatString(left + right, len - 1) + left
    (start + cells + redEnd, connectline)
  }

  // desenha todas as rows do board
  private def drawRows(board: Board, index: Int): List[String] = {
    val len = board.head.length
    board match {
      case Nil => List("")
      case x :: Nil =>
        val aux = drawConnectedRow(x, index, len)
        List(aux._1)
      case x :: b =>
        val aux = drawConnectedRow(x, index, len)
        aux._1 :: aux._2 :: drawRows(b, index + 1)
    }
  }

  // desenha os números das linhas em cima da 1ª linha
  private def drawNumLine(lst: List[Cells.Cell], index: Int): String = {
    lst match {
      case Nil => ""
      case x :: b =>
        val aux = lst.indexOf(x) + index
        aux + num_spaces + drawNumLine(b, aux + 1)
    }
  }

  // desenha o quadro to.do com as linhas azuis iniciais e finais
  private def drawBoard(board: Board): List[String] = {
    val cols = board.head.length
    val rows = board.length
    val bluerow = repeatString(blueSE, cols)
    val start = List(espaco + drawNumLine(board.head, 1), espaco + bluerow)
    val end = List(drawSpaces(rows + 1, con = false) + bluerow)
    start ++ drawRows(board, 0) ++ end
  }

  def draw(board: Board): String = {
    val str = drawBoard(board)
    str map (x => println(x))
    (str foldRight "")(_ + "\n" + _)
  }

}
