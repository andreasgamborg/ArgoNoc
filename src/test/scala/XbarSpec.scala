import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class XbarTest(dut: Xbar) extends PeekPokeTester(dut) {

  var func : Int = 0
  var data = new Array[Int](5)

  val r = scala.util.Random
  for (n <- 0 to 49) {
    for(j <- 0 to 4){
      func = func<<4 | 0x1<<r.nextInt(3)
      data(j) = r.nextInt()
    }

    //poke(dut.io.in, data)
    //poke(dut.io.func, func)

    //expect(dut.io.out, data)
  }

}

class XbarSpec extends FlatSpec with Matchers {
  "Xbar " should "pass" in {
    chisel3.iotesters.Driver(() => new Xbar) { c => new XbarTest(c)} should be (true)
  }
}
