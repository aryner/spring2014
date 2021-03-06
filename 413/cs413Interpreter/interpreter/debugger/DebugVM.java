package interpreter.debugger;

import interpreter.*;
import java.io.*;
import java.util.*;
import byteCodes.*;
import interpreter.debugger.ui.DebugUI;
import byteCodes.debuggerByteCodes.*;

public class DebugVM extends VirtualMachine {
  Vector<String> tracedFunctions;
  Vector<String> sourceLine;
  Vector<Boolean> isBreakptSet;
  Stack<FunctionEnvironmentRecord> funcEnvRecord;
  LineCode lineCheck;
  Vector<Integer> lines;
  int argCount;
  int stepOut;
  int stepOverPos;
  int stepOverLine;
  boolean stepOver;
  int stepIntoLine;
  boolean stepInto;
  boolean trace;
  boolean newLine;

  public DebugVM(Program prog, String src) throws FileNotFoundException{
    super(prog);
    pc = 0;
    runStack = new RunTimeStack();
    returnAddr = new Stack<String>();
    isBreakptSet = new Vector<Boolean>();
    sourceLine = new Vector<String>();
    tracedFunctions = new Vector<String>();
    BufferedReader file = new BufferedReader(new FileReader(src));  
    loadSource(file, false);
    funcEnvRecord = new Stack<FunctionEnvironmentRecord>();
    stepOut = -1;
    stepOverPos = -1;
    stepOverLine = -1;
    stepOver = false;
    stepIntoLine = -1;
    stepInto = false;
    trace = false;
    newLine = false;
    newFuncEnvRecord();
    lineCheck = new LineCode();
    lines = loadLines();
  }

  private Vector<Integer> loadLines() {
    Vector<Integer> result = new Vector<Integer>();
    for(int i=0; i<super.program.size(); i++) {
      ByteCode temp = super.program.getCode(i);
      if(lineCheck.getClass().isInstance(temp)) {
        result.add(Integer.parseInt(temp.getArg()));
      }
    }
    return result;
  }

  public Boolean isValidBreak(int num) {
    Boolean result = false;
    for(int i=0; i<lines.size() && !result; i++) {
      if(lines.get(i) == num) {
        result = true;
      }
    }
    return result;
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

      if(newLine && getCurr() > 0 && isBreakptSet.get(getCurr()-1) && getName() != null) {
        if(argCount > 0) {
          continue;
        }
        stepOut = -1;
        stepOver = false;
        stepInto = false;
        newLine = false;
        break;
      }
      if(funcEnvRecord.size() == stepOut) {
        stepOut = -1;
        break;
      }
      if(stepOver && funcEnvRecord.size() <= stepOverPos && stepOverLine != getCurr()) {
        stepOver = false;
        break;
      }
      if(stepInto && stepIntoLine != getCurr() && getName() != null) {
        if(argCount > 0) {
          continue;
        }
        stepInto = false;
        break;
      }
    } 
  }

  public void setArgCount(int num) {
    argCount = num;
  }

  public void decArgCount() {
    argCount--;
  }

  public void setNewLine(boolean state) {
    newLine = state;
  }

  public void setTrace(boolean state) {
    trace = state;
  }

  public void clearTrace() {
    tracedFunctions.clear();
  }

  public void pushTraceReturn() {
    String name = getName();
    int end = name.indexOf("<<");
    String func;
    if(end > -1) {
      func = name.substring(0,end);
    }
    else {
      func = name;
    }
    func = "exit: " + func + ": " + super.runStack.get(super.runStack.runStackSize()-1);
    String spaces = "";
    for(int i=0; i<funcEnvRecord.size(); i++) {
      spaces += " ";
    }
    tracedFunctions.add(spaces + func );
  }

  public void pushTraceFunc(String func) {
    String spaces = "";
    for(int i=0; i<funcEnvRecord.size(); i++) {
      spaces += " ";
    }
    int index = func.indexOf("<<");
    if(index > -1) {
      func = func.substring(0, index);
    }
    func += "(";
    for(int i=super.framePeek(); i<super.runStackSize(); i++) {
      func += super.getOffset(i);
      if(i<super.runStackSize()-1) {
        func += ", ";
      }
    }
    func += ")";
    tracedFunctions.add(spaces + func);
  }

  public Vector<String> getTrace() {
    return tracedFunctions;
  }

  public boolean isTrace() {
    return trace;
  }

  public String[] getStack() {
    String[] result = new String[funcEnvRecord.size()];
    Stack<FunctionEnvironmentRecord> temp = new Stack<FunctionEnvironmentRecord>();
    while(funcEnvRecord.size() > 0) {
      temp.push(funcEnvRecord.pop());
    }
    int i=0;
    while(temp.size() > 0) {
      String funcName = temp.peek().getName();
      int index = funcName.indexOf("<<");
      if(index > -1) {
        funcName = funcName.substring(0, index);
      }
      result[i] = funcName + ":  " + temp.peek().getCurr();
      funcEnvRecord.push(temp.pop()).getName();
      i++;
    }
    return result;
  }

  public boolean isRunning() {
    return isRunning;
  }

  public void setStepInto() {
    stepInto = true;
    stepIntoLine = getCurr();
  }

  public void setStepOver() {
    stepOver = true;
    stepOverPos = funcEnvRecord.size();
    stepOverLine = getCurr();
  }

  public Vector<Integer> listBrkPts() {
    Vector<Integer> brkList = new Vector<Integer>();
    for(int i=0; i<isBreakptSet.size(); i++) {
      if(isBreakptSet.get(i)) {
        brkList.add(i+1);
      }
    }
    return brkList;
  }

  public void setStepOut() {
    stepOut = funcEnvRecord.size() -1;
  }

  public Vector<String> getVars() {
    Vector<String> result = funcEnvRecord.peek().getIDs();
    Vector<Integer> offset = funcEnvRecord.peek().getOffset();
    int offS = -1;
    if (super.framePeek() > 0) {
      offS++;
    }
    for(int i=0; i<result.size(); i++) {
      result.set(i, result.get(i)+":"+super.runStack.get(offset.get(i)+offS+super.framePeek()));
    }
    return result;
  }

  public FunctionEnvironmentRecord getRecord() {
    return funcEnvRecord.peek();
  }

  public Vector<Boolean> getCurrBreaks() {
    Vector<Boolean> result = new Vector<Boolean>();
    result.add(false);
    result.add(false);
    for(int i=getStart()-1; i<getEnd(); i++) {
      result.add(isBreakptSet.get(i));
    }
    return result;
  }

  public Vector<String> getCurrFunc() {
    Vector<String> result = new Vector<String>();
    for(int i=getStart()-1; i<getEnd(); i++) {
      result.add(sourceLine.get(i));
    }
    return result;
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
    funcEnvRecord.pop();
  }

  public void beginScope() {
    funcEnvRecord.push(new FunctionEnvironmentRecord());
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

  public void clearBreaks(Vector<Integer> clrBrks) {
    if(clrBrks.size() == 0) {
      for(int i=0; i<isBreakptSet.size(); i++) {
        isBreakptSet.set(i, false);
      }
    }
    else {
      
      for(int i=0; i<clrBrks.size(); i++) {
        if(clrBrks.get(i) <= lines.get(lines.size()-1)) {
          isBreakptSet.set(clrBrks.get(i)-1, false);
        }
      }
    }
  }

  public Vector<String> getSource() {
    return sourceLine;
  }
  public Vector<Boolean> getBreaks() {
    return isBreakptSet;
  }

  public void setBreaks(Vector<Integer> brks) {
    for(int i=0; i<brks.size(); i++) {
      isBreakptSet.set(brks.get(i), true);
    }
  }
}
