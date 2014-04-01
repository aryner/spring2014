package byteCodes;

import java.util.*;
import virtualMachine.VirtualMachine;

/**
 * The ByteCode abstract class is the parent of all byte code classes
 * and ensures that each one must have loadArgs, getArg, setArg, and
 * execute methods
*/
public abstract class ByteCode {
  public abstract void loadArgs(Vector<String> args);
  public String getArg() {return "";}
  public void setArg(String location){}
  public void execute(VirtualMachine vm){
    System.out.println(this.toString());
  }
}
