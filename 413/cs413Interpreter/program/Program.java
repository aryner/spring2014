package program;

import java.lang.*;
import java.util.*;
import byteCodes.*;
import codeTable.CodeTable;

/**
 * The Program class holds the programs byte codes and
 * resolve branch addresses.
*/
public class Program {
  Vector<ByteCode> program;
  Vector<String> labels;
  Vector<String> branches;
  LabelCode labelCheck;
  FalseBranchCode fbcCheck;
  GoToCode goToCheck;
  CallCode callCheck;
  ReturnCode returnCheck;
  int location;
  int pc;

  public Program() {
    program = new Vector<ByteCode>();
    labels = new Vector<String>();
    branches = new Vector<String>();
    labelCheck = new LabelCode();
    fbcCheck = new FalseBranchCode();
    goToCheck = new GoToCode(); 
    callCheck = new CallCode();
    returnCheck = new ReturnCode();
    pc = 0;
  }

/**
 * Adds byte codes to the program vector while loading the byte codes
 * arguments.  Also keeps a record of branches and labels to later 
 * resolve address locations
 * @param newLine is the byte code to be added
*/
  public void loadLine(ByteCode newLine) {
    if(labelCheck.getClass().isInstance(newLine)) {
      labels.add(newLine.getArg());
      labels.add(""+pc);
    }
    else if (fbcCheck.getClass().isInstance(newLine) || callCheck.getClass().isInstance(newLine)
             || returnCheck.getClass().isInstance(newLine) || goToCheck.getClass().isInstance(newLine)) 
    {
      branches.add(""+pc);
      branches.add(newLine.getArg());
    }

    program.add(newLine);
    pc++;
  }

/**
 * Resolves the address of each branch instruction
*/
  public void resolveAddresses() {
    while(!branches.isEmpty()) {
      int location = labels.indexOf(branches.remove(branches.size()-1));
      program.get(Integer.parseInt(branches.remove(branches.size()-1))).setArg(labels.get(location+1)+"");
    }
  }

  public ByteCode getCode(int lineNum) {
    return program.get(lineNum);
  }

}
