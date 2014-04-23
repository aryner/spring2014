package byteCodes;

import java.util.*;
import interpreter.*;

/**
 * StoreCode stores the contents of the top of the stack into
 * the contents of a location in the stack based off of the current
 * frame pointer and an offset
*/
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
    
    vm.setCurrCode(this.toString() + result);
  }

  @Override
  public String toString() {
    return "STORE " + offset  +" "+id+"\t" + id +" = ";
  }
}
