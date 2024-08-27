package Hex

import scala.annotation.tailrec
import scala.collection.SortedMap
import scala.io.StdIn.readLine
import scala.util.{Failure, Success, Try}

//objeto da ficha 6 para escrever os diversos menus
// inclui uma classe extra que é igual ao optionPrompt mas devolve um int em vez de uma Option[Hex.CommandLineOption]
object IO_Utils {

  private val str_divider = "--------------------"

  def getUserInputInt: Try[Int] = {
    print(Console.WHITE + "Selecione uma opção: ")
    Try(readLine.trim.toUpperCase.toInt)
  }

  def prompt(msg: String): Int = {
    print(msg + ": ")
    val aux = scala.io.StdIn.readLine()
    aux.toInt
  }

  @tailrec
  def optionPrompt(options: SortedMap[Int, CommandLineOption]): Option[CommandLineOption] = {
    options.toList map
      ((option: (Int, CommandLineOption)) => println(option._1 + ") " + option._2.name))

    getUserInputInt match {
      case Success(i) => options.get(i)
      case Failure(_) => println("Opção Inválida!\n" + str_divider); optionPrompt(options)
    }
  }

  @tailrec
  def optionPromptInt(options: SortedMap[Int, CommandLineOption]): Int = {
    options.toList map
      ((option: (Int, CommandLineOption)) => println(option._1 + ") " + option._2.name))

    getUserInputInt match {
      case Success(i) => i
      case Failure(_) => println("Opção Inválida!\n" + str_divider); optionPromptInt(options)
    }
  }
}
