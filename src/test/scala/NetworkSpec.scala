import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class NetworkTest(dut: Network,n: Int) extends PeekPokeTester(dut) {
  // Network tester for n = 2, 3 or 4
  // The test will send a package from every router to itself
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

  val rand = scala.util.Random

  for (r <- 0 until n*n) {
    val r = 0
    address = rand.nextInt(100)
    route = 0x09999 >> ((4-n)*4)
    payload1 = rand.nextInt(100)
    payload2 = rand.nextInt(100)

    packet1 = (sctrl << 32) | (address << 16) | route
    packet2 = (vctrl << 32) | payload1
    packet3 = (ectrl << 32) | payload2
    packet1s = (sctrl << 32) | (address << 16) | (route >> 2)

    step(10)
    for (n <- 0 until n*n) {
        poke(dut.io.in(n), 0L)
      }
    step(10)
      poke(dut.io.in(r), packet1);   step(1)
      poke(dut.io.in(r), packet2);   step(1)
      poke(dut.io.in(r), packet3);   step(1)
      poke(dut.io.in(r), 0L); step(1)
      step(4*n-1)
      //for(i <- 1 to 10){
      //  print(peek(dut.io.out));println()
      //  step(1)
      //}
      expect(dut.io.out(r), packet2);   step(1)
      expect(dut.io.out(r), packet3);   step(1)
  }
}

class Network2Spec extends FlatSpec with Matchers {
  "Network " should "pass" in {
    chisel3.iotesters.Driver(() => new Network(2)) { c => new NetworkTest(c,2)} should be (true)
  }
}
class Network3Spec extends FlatSpec with Matchers {
  "Network " should "pass" in {
    chisel3.iotesters.Driver(() => new Network(3)) { c => new NetworkTest(c,3)} should be (true)
  }
}
class Network4Spec extends FlatSpec with Matchers {
  "Network " should "pass" in {
    chisel3.iotesters.Driver(() => new Network(4)) { c => new NetworkTest(c,4)} should be (true)
  }
}