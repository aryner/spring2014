package byteCodes;

import java.util.*;
import interpreter.*;

/**
 * ReadCode is responsible for prompting the user for an integer
 * and then storing it onto the run time stack
*/
public class ReadCode extends ByteCode {

  public void loadArgs(Vector<String> args){

  }

  public void execute(VirtualMachine vm) {
    System.out.print("Input an integer: ");
    Scanner in = new Scanner(System.in);
    int num = in.nextInt();
    vm.runStackPush(num);

    vm.setCurrCode(this.toString());
  }

  @Override
  public String toString() {
    return "Readcode";
  }
}
