package interpreter;

import java.util.*;
import byteCodes.*;

/**
 * The VirtualMachine class is respnosible for executing
 * the program.  It goes through each line of the program
 * running the corresponding byte codes execute method. It
 * keeps track of its place in the program my incrementing
 * the pc(program counter).  If the dump switch is on, it 
 * will display run time stack information after each line
 * of code executed.  It is also what the byte codes must
 * work with in order to manipulate the run time stack.
*/

public class VirtualMachine {
  protected RunTimeStack runStack;
  protected int pc;
  protected Stack<String> returnAddr;
  protected boolean isRunning;
  protected boolean dump;
  protected Program program;
  protected String currStack;
  protected String currCode;
  protected Stack<Integer> returnLocal;

  public VirtualMachine(Program prog) {
    returnLocal = new Stack<Integer>();
    program = prog;
    dump = false;
  }

  public void executeProgram() {
    pc = 0;
    runStack = new RunTimeStack();
    returnAddr = new Stack<String>();
    isRunning = true;
    while (isRunning) {
      ByteCode code = program.getCode(pc);
      code.execute(this);
      if(dump) {
        currStack = runStack.dump();
        if(!currCode.equals("dump")){
          System.out.println(currCode +"\n"+currStack);
        }
      }
      pc++;
    }
  }

  public void setCurrCode(String code) {
    currCode = code;
  }

  public void setNotRunning() {
    isRunning = false;
  }

  public boolean isDump() {
    return dump;
  }

  public void dumpOn() {
    dump = true;
  }

  public void dumpOff() {
    dump = false;
  }
  
  public void setPC(int newPC) {
    pc = newPC;
  }
  
  public int getPC() {
    return pc;
  }

  public int framePeek() {
    return runStack.framePeek();
  }

  public int runStackSize() {
    return runStack.runStackSize();
  }

  public int popFrame() {
    return runStack.popFrame();
  }
 
  public int runStackLoad(int offset) {
    return runStack.load(offset);
  }

  public int runStackStore(int offset) {
    return runStack.store(offset);
  }

  public Vector<Integer> getFrame() {
    return runStack.getFrame();
  }

  public void setReturn(int local) {
    returnLocal.push(local);
  }

  public int getReturn() {
    return returnLocal.pop();
  }
 
  public void runStackPush(int i) {
    runStack.push(i);
  }

  public void newRunStackFrame(int offset) {
    runStack.newFrameAt(offset);
  }

  public int runStackPop() {
    return runStack.pop();
  }

  public int runStackPeek() {
    return runStack.peek();
  }
}
