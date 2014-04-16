package interpreter.debugger;

import interpreter.*;
import java.io.*;
import java.util.*;
import byteCodes.*;
import interpreter.debugger.ui.DebugUI;
import byteCodes.debuggerByteCodes.*;

public class DebugVM extends VirtualMachine {
  Vector<String> sourceLine;
  Vector<Boolean> isBreakptSet;
  Stack<FunctionEnvironmentRecord> funcEnvRecord;

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
    funcEnvRecord = new Stack<FunctionEnvironmentRecord>();
  }

  public void startUI() {
    DebugUI.start(this);
  }

  public void executeProgram() {
    while (isRunning) {
/*
      if(isBreakptSet.get(***********)) {
        DebugUI.menu(this);
        break;
      }
*/
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


  public String getName() {
    return funcEnvRecord.peek().getName(); 
  }

  public int getCurr() {
    return funcEnvRecord.peek().getCurr(); 
  }

  public int getEnd() {
    return funcEnvRecord.peek().getEnd(); 
  }

  public int getStart() {
    return funcEnvRecord.peek().getStart(); 
  }

  public void setName(String n) {
    funcEnvRecord.peek().setName(n);
  }

  public void setCurr(int n) {
    funcEnvRecord.peek().setCurr(n);
  }

  public void setEnd(int n) {
    funcEnvRecord.peek().setEnd(n);
  }

  public void setStart(int n) {
    funcEnvRecord.peek().setStart(n); 
  }

  public void lineFuncEnvRecord(int c) {
    funcEnvRecord.peek().line(c);
  }

  public void funcEnvRecordFunction(String n, int s, int e) {
    funcEnvRecord.peek().function(n,s,e);
  }

  public void popFuncEnvRecord(int n) {
    funcEnvRecord.peek().pop(n);
  }

  public void enterEnvRecord(String id, int value) {
    funcEnvRecord.peek().enter(id, value);
  }

  public void newFuncEnvRecord() {
    funcEnvRecord.push(new FunctionEnvironmentRecord());
  }

  public void removeFuncEnvRecord() {
    funcEnvRecord.pop();
  }

  public  void endScope() {
    funcEnvRecord.peek().endScope();
  }

  public void beginScope() {
    funcEnvRecord.peek().beginScope();
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
