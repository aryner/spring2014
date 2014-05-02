package interpreter.debugger.ui;

import interpreter.*;
import interpreter.debugger.*;
import java.util.*;

public class DebugUI {
  DebugVM vm;

  public DebugUI(DebugVM dvm) {
    vm = dvm;
  }

  public void start() { 
    while(vm.isRunning()) {
      displayTrace();
      displayFunction();
      menu();
    }
    displayTrace();
  }

  public void menu() {
    String command;
    Scanner input = new Scanner(System.in);
    System.out.println();
    System.out.println("Type ? for help");
    System.out.print("> ");
    command = input.nextLine();
    StringTokenizer tokens = new StringTokenizer(command);

    switch(tokens.nextToken().trim()) {
      case "help":
      case "?":
        help();
        menu();
        break;
      case "continue":
      case "cont":
        vm.executeProgram();
        break;
      case "stepInto":
      case "si":
        stepInto();
        vm.executeProgram();
        break;
      case "stepOver":
      case "so":
        stepOver();
        vm.executeProgram();
        break;
      case "stepOut":
      case "sot":
        stepOut();
        vm.executeProgram();
        break;
      case "setBreak":
      case "sb":
        setBreaks(tokens);
        menu();
        break;
      case "listBreakPoints":
      case "lbp":
        listBreaks();
        menu();
        break;
      case "displaySource":
      case "ds":
        displaySource(vm.getSource(), vm.getBreaks(), vm.getCurr());
        menu();
        break;
      case "clearBreakBoints":
      case "cbp":
        clearBreaks(tokens);
        menu();
        break;
      case "displayVariables":
      case "dv":
        displayVars();
        menu();
        break;
      case "displayFunction":
      case "df":
        displayFunction();
        menu();
        break;
      case "viewStack":
      case "vs":
        viewStack();
        menu();
        break;
      case "halt":
        halt();
        break;
      case "setTraceOn":
      case "ton":
        trace(true);
        menu();
        break;
      case "setTraceOff":
      case "toff":
        trace(false);
        menu();
        break;
      default:
        System.out.println("Invalid command");
        menu();
        break;
    }
  }

  private void help() {
    System.out.println("\thelp,  ?\t\t\t-Display list of commands");
    System.out.println("\tcontinue, cont\t\t\t-Continue execution of program");
    System.out.println("\tsetBreak,  sb\t\t\t-Any line numbers seperated \n\t\t\t\t\t  by spaces after this command \n\t\t\t\t\t  will set breakpoints at those lines");
    System.out.println("\tstepInto,  si\t\t\t-Execute one line of code at a time");
    System.out.println("\tstepOver,  so\t\t\t-Step to the next line of code");
    System.out.println("\tstepOut,  sot\t\t\t-Step out of current function");
    System.out.println("\tdisplaySource,  ds\t\t-Display source program");
    System.out.println("\tclearBreakPoints,  cbp\t\t-Remove all break points if \n\t\t\t\t\t  no arguments are given or \n\t\t\t\t\t  clears specific points if line \n\t\t\t\t\t  numbers are given");
    System.out.println("\tlistBreakPoints,  lbp\t\t-List current break poitns");
    System.out.println("\tdisplayVariables,  dv\t\t-Display current varaiables");
    System.out.println("\tdisplayFunction,  df\t\t-Display source code of the\n\t\t\t\t\t  current function");
    System.out.println("\tviewStack,  vs\t\t\t-Displays the current runtime stack");
    System.out.println("\tsetTraceOn  ton\t\t\t-Turns function tracing on");
    System.out.println("\tsetTraceOff  toff\t\t-Turns function tracing off");
    System.out.println("\thalt\t\t\t\t-Quit exection of program");
  }

  private void displayTrace() {
    Vector<String> trace = vm.getTrace();
    if(trace.size() > 0)
      System.out.println();
    for(int i=0; i<trace.size(); i++) {
      System.out.println(trace.get(i));
    }
    if(trace.size() > 0)
      System.out.println();
    vm.clearTrace();
  }

  private void trace(boolean state) {
    vm.setTrace(state);
  }

  private void viewStack() {
    if(vm.getCurr() == 0) {
      return;
    }
    String[] stack = vm.getStack();
    for(int i=(stack.length-1); i>-1; i--) {
      System.out.println(stack[i]);
    }
  }

  private void listBreaks() {
    System.out.print("Break points are set at:");
    Vector<Integer> brks = vm.listBrkPts();
    for(int i=0; i< brks.size(); i++) {
      System.out.print(" "+brks.get(i));
    }
    System.out.println();
  }

  private void stepInto() {
    vm.setStepInto();
  }

  private void stepOver() {
    vm.setStepOver();
  }

  private void stepOut() {
    vm.setStepOut();
  }

  private void displayVars() {
    Vector<String> vars = vm.getVars();
    for(int i=0; i<vars.size(); i++) {
      System.out.println(vars.get(i));
    }
  }

  private void halt() {
    System.out.println("****Execution Halted****");
    vm.quit();
  }

  private void displayFunction() {
    int offS = 1;
    if(vm.getStart() < 1) {
      displaySource(vm.getSource(), vm.getBreaks(), vm.getCurr());
      return;
    }
    if(vm.getStart() > 1) {
      offS--;
    }
    
    if(vm.getCurr() == -1) {
      System.out.println("********"+vm.getName()+"**********");
      return;
    }

    Vector<String> src = vm.getCurrFunc();
    Vector<Boolean> breaks = vm.getCurrBreaks();
    int line = vm.getCurr();

    for(int i=0; i<src.size(); i++) {
      if(breaks.get(i+vm.getStart()+offS)) {
        System.out.print("*");
      }
      else{
        System.out.print(" ");
      }

      if((i+vm.getStart())<10) {
        System.out.print(" ");
      }
      System.out.print(i+vm.getStart()+".");

      System.out.print(src.get(i));
      if((i+vm.getStart()) == line) {
        System.out.print("\t<--------");
      }
      System.out.println();
    }
  }

  private void clearBreaks(StringTokenizer tokens) {
    Vector<Integer> clrBrks = new Vector<Integer>();
    while(tokens.hasMoreElements()) {
      clrBrks.add(Integer.parseInt(tokens.nextToken()));
    }
    vm.clearBreaks(clrBrks);
  }

  private void setBreaks(StringTokenizer tokens) {
    Vector<Integer> breaksArray = new Vector<Integer>();
    int invalids = 0;
    while(tokens.hasMoreElements()) {
      int num = Integer.parseInt(tokens.nextToken())-1;
      if(isValidBreak(num)) {
        breaksArray.add(num);
      }
      else {
        invalids++;
        if(invalids <= 1) {
          System.out.print("***Invalid breakpoints: ");
        }
        System.out.print(" "+(num+1));
      }
    }
    if(invalids > 0) {
      System.out.println();
    }
    vm.setBreaks(breaksArray);
/*
      breaksArray.add(Integer.parseInt(tokens.nextToken()));
    }
    int[] breakPts = new int[breaksArray.size()];
    int invalids = 0;
    for(int i=0; i<breaksArray.size(); i++) {
      if(isValidBreak(breaksArray.get(i))){
        breakPts[i] = breaksArray.get(i) - 1;
      }
      else {
        invalids++;
        if(invalids <= 1) {
          System.out.print("***Invalid breakpoints: ");
        }
        System.out.print(" "+breaksArray.get(i));
      }
    }
    if(invalids > 0) {
      System.out.println();
    }
    vm.setBreaks(breakPts);
*/
  }

  private Boolean isValidBreak(int num ) {
    return vm.isValidBreak(num);
  }

  private void displaySource(Vector<String> src, Vector<Boolean> breaks,int line) {
    if(vm.getCurr() == -1) {
      System.out.println("********"+vm.getName()+"**********");
      return;
    }
    for(int i=0; i<src.size(); i++) {
      if(breaks.get(i)) {
        System.out.print("*");
      }
      else{
        System.out.print(" ");
      }

      if(i<9) {
        System.out.print(" ");
      }
      System.out.print(i+1+".");

      System.out.print(src.get(i));
      if((i+1) == line) {
        System.out.print("\t<--------");
      }
      System.out.println();
    }
  }
}
