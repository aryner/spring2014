package byteCodes;

import java.util.*;
import virtualMachine.VirtualMachine;

public class LabelCode extends ByteCode {
  String arg;
  int location;

  public void loadArgs(Vector<String> args){
    arg = args.get(0);
  }
 
  public String getArg() {
    return arg;
  }
 
  public int getLocation() {
    return location;
  }

  public void setLoaction(int local) {
    location = local;
  }

  public void execute(VirtualMachine vm) {
    if(vm.isDump()) {
      System.out.println(this.toString());
      vm.runStackDump();
    }
  }

  @Override
  public String toString() {
    return "LABEL " + arg;
  }
}
