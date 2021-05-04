import chisel3._

class HPU extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(35.W))
    val out = Output(UInt(35.W))
    val sel = Output(UInt(4.W))
  })

  val out = WireDefault(0.U(35.W))
  val VLD: Bool = io.in(34)
  val SOP: Bool = io.in(33)
  val EOP: Bool = io.in(32)

  val sel = WireDefault(0.U(4.W))
  val selReg = RegInit(0.U(4.W))
  val selRegNext = WireDefault(0.U(4.W))

  val decSel = "b0001".U << io.in(1,0)

  selReg := selRegNext

  when (SOP) {
    selRegNext := decSel
  }.elsewhen(VLD || EOP){
    selRegNext := selReg
  }.otherwise{
    selRegNext := 0.U(35.W)
  }

  when (!SOP && (EOP || VLD)) {
    sel := selReg
  } .otherwise{
    sel := selRegNext
  }

  when (SOP){
    out := io.in(34,16) ## 0.U(2.W) ## io.in(15,2)
  }.otherwise{
    out := io.in
  }

  io.out := out
  io.sel := sel
}