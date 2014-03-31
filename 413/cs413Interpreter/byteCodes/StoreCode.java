package byteCodes;

import java.util.*;
import virtualMachine.VirtualMachine;

public class StoreCode extends ByteCode {
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
    int result = vm.runStackStore(offset);
    
    if(vm.isDump()) {
      System.out.println(this.toString() + result);
      vm.runStackDump();
    }
  }

  @Override
  public String toString() {
    return "STORE " + offset  +" "+id+"\t" + id +" = ";
  }
}
