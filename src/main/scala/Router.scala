import chisel3._

class Router extends Module {
  val io = IO(new Bundle {
    val in = Input(Vec(5, UInt(35.W)))
    val out = Output(Vec(5, UInt(35.W)))
  })

  val HPUs = new Array[HPU](5)
  val Xbar = Module(new Xbar())
  val dataNext = Wire(Vec(5, UInt(35.W)))
  val selNext = Wire(Vec(5, UInt(4.W)))


  for (n <- 0 to 4) {
    HPUs(n) = Module(new HPU())
    HPUs(n).io.in := io.in(n)
    dataNext(n) := HPUs(n).io.out
    selNext(n) := HPUs(n).io.sel
  }

  Xbar.io.in := (RegNext(dataNext))
  Xbar.io.func := (RegNext(selNext))

  io.out := RegNext(Xbar.io.out)
}