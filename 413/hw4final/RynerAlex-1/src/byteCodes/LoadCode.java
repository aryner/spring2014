package byteCodes;

import java.util.*;
import virtualMachine.VirtualMachine;

public class LoadCode extends ByteCode {
  int offset;
  String id;

  public void loadArgs(Vector<String> args){
    offset = Integer.parseInt(args.get(0));
    id = args.get(1);
  }

  public int getOffset() {
    return offset;
  }

  public String getId() {
    return id;
  }

  public void execute(VirtualMachine vm) {
    int top = vm.runStackLoad(offset);
    vm.setCurrCode(this.toString() + "<load "+id+">");
  }

  @Override
  public String toString() {
    return "LOAD " + offset + " " + id +"\t";
  }
}
