package byteCodes;

import java.util.*;
import virtualMachine.VirtualMachine;

public abstract class ByteCode {
  public abstract void loadArgs(Vector<String> args);
  public String getArg() {return "";}
  public void setArg(String location){}
  public void execute(VirtualMachine vm){
    System.out.println(this.toString());
  }
}
