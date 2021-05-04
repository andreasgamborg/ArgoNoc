import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class RouterTest(dut: Router) extends PeekPokeTester(dut) {

  val sctrl = 6L
  val vctrl = 4L
  val ectrl = 5L

  var address = 1L;
  var route = 2L;
  var payload1 = 1L;
  var payload2 = 1L;
  var out = 0
  var in = 0

  var packet1 = 0L
  var packet2 = 0L
  var packet3 = 0L
  var packet1s = 0L

  val r = scala.util.Random

  for (n <- 0 to 49) {
    address = r.nextInt(100)
    route = r.nextInt(100)
    payload1 = r.nextInt(100)
    payload2 = r.nextInt(100)

    packet1 = (sctrl << 32) | (address << 16) | route
    packet2 = (vctrl << 32) | payload1
    packet3 = (ectrl << 32) | payload2
    packet1s = (sctrl << 32) | (address << 16) | (route >> 2)

    out = route & 0x3
    in = r.nextInt(5)
    for (n <- 0 to 4) {
      poke(dut.io.in(n), 0L)
    }
    if (out != in) {
      printf("From %d To %d\n", in, out)
        step(10)
      poke(dut.io.in(in), packet1)
        step(1)
      poke(dut.io.in(in), packet2)
        step(1)
      expect(dut.io.out(out), packet1s)
      poke(dut.io.in(in), packet3)
        step(1)
      expect(dut.io.out(out), packet2)
        step(1)
      expect(dut.io.out(out), packet3)
    }
  }
}

class RouterSpec extends FlatSpec with Matchers {
  "Router " should "pass" in {
    chisel3.iotesters.Driver(() => new Router) { c => new RouterTest(c)} should be (true)
  }
}
