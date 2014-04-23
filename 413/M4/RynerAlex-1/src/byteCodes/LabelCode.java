package byteCodes;

import java.util.*;
import interpreter.*;

/**
 * LabelCode class is responsible for holding the postion
 * of any branches to the label
*/
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
    vm.setCurrCode(this.toString());
  }

  @Override
  public String toString() {
    return "LABEL " + arg;
  }
}
