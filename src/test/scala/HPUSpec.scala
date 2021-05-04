import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class HPUTest(dut: HPU) extends PeekPokeTester(dut) {

  val sctrl = 6L
  val vctrl = 4L
  val ectrl = 5L

  var address = 1L;
  var route = 1L;
  var payload1 = 1L;
  var payload2 = 1L;

  val r = scala.util.Random
  for (n <- 0 to 50) {
    address = r.nextInt(2^16-1)
    route = r.nextInt(2^16-1)
    payload1 = r.nextInt(2^32-1)
    payload2 = r.nextInt(2^32-1)

    val packet1: Long = (sctrl<<32) | (address<<16) | route
    val packet2: Long = (vctrl<<32) | payload1
    val packet3: Long = (ectrl<<32) | payload2
    val packet1s: Long = (sctrl<<32) | (address<<16) | (route>>2)

    poke(dut.io.in, packet1)
    expect(dut.io.out, packet1s)
    expect(dut.io.sel, 1<<(route & 0x3).toInt)
    step(1)
    poke(dut.io.in, packet2)
    expect(dut.io.out, packet2)
    expect(dut.io.sel, 1<<(route & 0x3).toInt)
    step(1)
    poke(dut.io.in, packet3)
    expect(dut.io.out, packet3)
    expect(dut.io.sel, 1<<(route & 0x3).toInt)
    step(1)

  }

}

class HPUSpec extends FlatSpec with Matchers {
  "HPU " should "pass" in {
    chisel3.iotesters.Driver(() => new HPU) { c => new HPUTest(c)} should be (true)
  }
}
