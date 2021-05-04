import chisel3._
import chisel3.util.ImplicitConversions.intToUInt
import chisel3.util._

class Xbar extends Module {
  val io = IO(new Bundle {
    val func = Input(Vec(5, UInt(4.W)))
    val in = Input(Vec(5, UInt(35.W)))
    val out = Output(Vec(5, UInt(35.W)))
  })

  val out = Wire(Vec(5, UInt(35.W)))
  val sel = io.func

  when(       sel(1)(0) ){  out(0) := io.in(1)  }
  .elsewhen(  sel(2)(0) ){  out(0) := io.in(2)  }
  .elsewhen(  sel(3)(0) ){  out(0) := io.in(3)  }
  .elsewhen(  sel(4)(0) ){  out(0) := io.in(4)  }
  .otherwise             {  out(0) := 0.U}

  when(       sel(0)(1) ){  out(1) := io.in(0)  }
  .elsewhen(  sel(2)(1) ){  out(1) := io.in(2)  }
  .elsewhen(  sel(3)(1) ){  out(1) := io.in(3)  }
  .elsewhen(  sel(4)(1) ){  out(1) := io.in(4)  }
  .otherwise             {  out(1) := 0.U}

  when(       sel(0)(2) ){  out(2) := io.in(0)  }
  .elsewhen(  sel(1)(2) ){  out(2) := io.in(1)  }
  .elsewhen(  sel(3)(2) ){  out(2) := io.in(3)  }
  .elsewhen(  sel(4)(2) ){  out(2) := io.in(4)  }
  .otherwise             {  out(2) := 0.U}

  when(       sel(0)(3) ){  out(3) := io.in(0)  }
  .elsewhen(  sel(1)(3) ){  out(3) := io.in(1)  }
  .elsewhen(  sel(2)(3) ){  out(3) := io.in(2)  }
  .elsewhen(  sel(4)(3) ){  out(3) := io.in(4)  }
  .otherwise             {  out(3) := 0.U}

  when(       sel(0)(0) ){  out(4) := io.in(0)  }
  .elsewhen(  sel(1)(1) ){  out(4) := io.in(1)  }
  .elsewhen(  sel(2)(2) ){  out(4) := io.in(2)  }
  .elsewhen(  sel(3)(3) ){  out(4) := io.in(3)  }
  .otherwise             {  out(4) := 0.U}

  io.out := out
}