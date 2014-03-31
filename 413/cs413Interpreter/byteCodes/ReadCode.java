package byteCodes;

import java.util.*;
import virtualMachine.VirtualMachine;

public class ReadCode extends ByteCode {

  public void loadArgs(Vector<String> args){

  }

  public void execute(VirtualMachine vm) {
    System.out.print("Input an integer: ");
    Scanner in = new Scanner(System.in);
    int num = in.nextInt();
    vm.runStackPush(num);

    if(vm.isDump()) {
      System.out.println(this.toString());
    }
  }

  @Override
  public String toString() {
    return "Readcode";
  }
}
