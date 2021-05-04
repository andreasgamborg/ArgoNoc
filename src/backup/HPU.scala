import chisel3._

class HPU extends Module {
  val io = IO(new Bundle {
    val ports = new Channel
    val sel = Output(UInt(4.W))
  })

  val selReg = RegInit(0.U(4.W))
  val selRegNext = WireDefault(0.U(4.W))

  val decSel = "b0001".U << io.ports.in.route(1,0).asUInt()

  selReg := selRegNext

  when (io.ports.in.SOP) {
    selRegNext := decSel
  }.elsewhen(io.ports.in.VLD || io.ports.in.EOP){
    selRegNext := selReg
  }.otherwise{
    selRegNext := 0.U
  }

  when (!io.ports.in.SOP && (io.ports.in.EOP || io.ports.in.VLD)) {
    io.sel := selReg
  } .otherwise{
    io.sel := selRegNext
  }

  when (io.ports.in.SOP){
    io.ports.out := io.ports.in
    io.ports.out.route := 0.U(2.W) ## io.ports.in.route(15,2)
  }.otherwise{
    io.ports.out := io.ports.in
  }
}