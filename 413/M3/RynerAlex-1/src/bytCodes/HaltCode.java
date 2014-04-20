package byteCodes;

import java.util.*;
import interpreter.*;

/**
 * The HaltCode class is responsible for setting the virtualMachine
 * to no longer running when the program is finished
*/
public class HaltCode extends ByteCode {

  public void loadArgs(Vector<String> args){

  }

  public void execute(VirtualMachine vm) {
    vm.setNotRunning();
    vm.setCurrCode(this.toString());
  }

  @Override
  public String toString() {
    return "HALT";
  }
}
