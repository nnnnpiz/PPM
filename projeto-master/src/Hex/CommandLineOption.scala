package Hex

// classe para o executar as funçoes do main, como a classe dada na ficha 6
case class CommandLineOption(name: String, exec: (HexBoard, MyRandom, List[HexBoard]) => (HexBoard, List[HexBoard]))
