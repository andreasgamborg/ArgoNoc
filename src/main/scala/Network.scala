import chisel3._

class Network(n: Int) extends Module {
  val io = IO(new Bundle {
    val in = Input(Vec(n*n, UInt(35.W)))
    val out = Output(Vec(n*n, UInt(35.W)))
  })

  val net = new Array[Router](n*n)
  val north = 0
  val east = 1
  val south = 2
  val west = 3
  val local = 4

  for (i <- 0 until n * n) {
    net(i) = Module(new Router())
    net(i).io.in(local) := io.in(i)
    io.out(i) := net(i).io.out(local)
  }

  for (i <- 0 until n) {
    for (j <- 0 until n) {
      val r = i * n + j
      connect(r, east, i * n + (j + 1) % n, west)
      connect(r, south, (i + 1) % n * n + j, north)
    }
  }
  def connect(r1: Int, p1: Int, r2: Int, p2: Int): Unit = {
    net(r1).io.in(p1) := net(r2).io.out(p2)
    net(r2).io.in(p2) := net(r1).io.out(p1)
  }
}
