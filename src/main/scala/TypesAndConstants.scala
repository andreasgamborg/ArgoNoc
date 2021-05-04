import chisel3._

object Const {
  val NORTH = 0
  val EAST = 1
  val SOUTH = 2
  val WEST = 3
  val LOCAL = 4
  val N_PORTS = 5
}

class DataChannel extends Bundle {
  //override def cloneType: this.type = new SingleChannel.asInstanceOf[this.type]
  val data = UInt(32.W)
  val address = data(31,16)
  val route = data(15,0)
  val VLD = Bool()
  val SOP = Bool()
  val EOP = Bool()
}

class channel_forward extends Bundle {
  val req = Bool()
  val data = UInt(32.W)
}

class channel_backward extends Bundle {
  val ack = Bool()
}
class Channel extends Bundle {
  val out = Output(new DataChannel)
  val in = Input(new DataChannel)
}

class RouterPorts extends Bundle {
  val ports = Vec(Const.N_PORTS, new Channel)
}
