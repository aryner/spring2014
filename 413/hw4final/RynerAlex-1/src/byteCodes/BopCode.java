package byteCodes;

import java.util.*;
import virtualMachine.VirtualMachine;

/**
 * The BopCode class is responsible for taking the top
 * two spots on the run time stack and preforming a binary
 * operation on them, placing the result in their place
 * on the stack
*/
public class BopCode extends ByteCode {
  String arg;

  public void loadArgs(Vector<String> args){
    arg = args.get(0);
  }

  public void execute(VirtualMachine vm) {
    int two = vm.runStackPop();
    int one = vm.runStackPop();
    int result = 0;
    boolean bool;
    
    switch(arg) {
      case "+":
        result = one + two;
        break;
      case "-":
        result = one - two;
        break;
      case "/":
        result = one / two;
        break;
      case "*":
        result = one * two;
        break;
      case "==":
        bool = one == two;
        if(bool) {
          result = 1;
        }
        break;
      case "!=":
        bool = one != two;
        if(bool) {
          result = 1;
        }
        break;
      case "<=":
        bool = one <= two;
        if(bool) {
          result = 1;
        }
        break;
      case ">":
        bool = one > two;
        if(bool) {
          result = 1;
        }
        break;
      case ">=":
        bool = one >= two;
        if(bool) {
          result = 1;
        }
        break;
      case "<":
        bool = one < two;
        if(bool) {
          result = 1;
        }
        break;
      case "|":
        if(one != 0 || two != 0) {
          result = 1;
        }
        break;
      case "&":
        if(one != 0 && two != 0) {
          result = 1;
        }
        break;
      default:
        break;
    }   
    vm.runStackPush(result);
    vm.setCurrCode(this.toString());
  }

  @Override
  public String toString() {
    return "BOP " + arg;
  }
}
