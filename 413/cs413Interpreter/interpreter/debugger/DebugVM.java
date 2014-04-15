package interpreter.debugger;

import interpreter.*;
import java.io.*;
import java.util.*;
import byteCodes.*;
import interpreter.debugger.ui.DebugUI;
//import byteCodes.debuggerByteCodes.*;

public class DebugVM extends VirtualMachine {
  Vector<String> sourceLine;
  Vector<Boolean> isBreakptSet;

  public DebugVM(Program prog, String src) throws FileNotFoundException{
    super(prog);
    pc = 0;
    runStack = new RunTimeStack();
    returnAddr = new Stack<String>();
    isRunning = true;
    isBreakptSet = new Vector<Boolean>();
    sourceLine = new Vector<String>();
    BufferedReader file = new BufferedReader(new FileReader(src));  
    loadSource(file, false);
  }

  public void startUI() {
    DebugUI.start(this);
  }

  public void executeProgram() {

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

  public void quit() {
    isRunning = false;
  }

  private void loadSource(BufferedReader file, Boolean EOF) {
    while(!EOF) {
      try{
        sourceLine.add(file.readLine());
        if(sourceLine.lastElement() == null) {
          sourceLine.remove(sourceLine.size()-1);
          break;
        }
        isBreakptSet.add(false);
      }
      catch(Exception e) {
        EOF = true;
      }
    }
  }

  public void clearBreaks() {
    for(int i=0; i<isBreakptSet.size(); i++) {
      isBreakptSet.set(i, false);
    }
  }

  public Vector<String> getSource() {
    return sourceLine;
  }
  public Vector<Boolean> getBreaks() {
    return isBreakptSet;
  }

  public void setBreaks(int[] brks) {
    for(int i=0; i<brks.length; i++) {
      isBreakptSet.set(brks[i], true);
    }
  }
}
