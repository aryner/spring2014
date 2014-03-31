package virtualMachine;

import byteCodes.*;
import byteCodeLoader.ByteCodeLoader;
import program.Program;
import runTimeStack.RunTimeStack;
import java.util.*;

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
  RunTimeStack runStack;
  int pc;
  Stack<String> returnAddr;
  boolean isRunning;
  boolean dump;
  Program program;
  Stack<Integer> returnLocal;

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
      pc++;
    }
  }

  public void runStackDump() {
    runStack.dump();
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
