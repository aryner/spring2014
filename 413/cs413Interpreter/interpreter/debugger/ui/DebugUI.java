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
    displayFunction();
    menu();
    
    while(vm.isRunning()) {
      menu();
    }
  }

  public void menu() {
    String command;
    Scanner input = new Scanner(System.in);
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
        displayFunction();
        break;
      case "stepInto":
      case "si":
        stepInto();
        vm.executeProgram();
        displayFunction();
        break;
      case "stepOver":
      case "so":
        stepOver();
        vm.executeProgram();
        displayFunction();
        break;
      case "stepOut":
      case "sot":
        stepOut();
        vm.executeProgram();
        displayFunction();
        break;
      case "setBreak":
      case "sb":
        setBreaks(tokens);
        menu();
        break;
      case "listBreakPoints":
      case "lbpt":
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
      case "quit":
        quit();
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
    System.out.println("\tlistBreakPoints,  lbpt\t\t-List current break poitns");
    System.out.println("\tdisplayVariables,  dv\t\t-Display current varaiables");
    System.out.println("\tdisplayFunction,  df\t\t-Display source code of the\n\t\t\t\t\t  current function");
    System.out.println("\tquit\t\t\t\t-Quit exection of program");
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

  private void quit() {
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
    Vector<String> src = vm.getCurrFunc();
    Vector<Boolean> breaks = vm.getCurrBreaks();
    int line = vm.getCurr();

    for(int i=0; i<src.size(); i++) {
      if((i+vm.getStart()) == line) {
        System.out.print(">");
      }
      else if(breaks.get(i+vm.getStart()+offS)) {
        System.out.print("*");
      }
      else{
        System.out.print(" ");
      }

      if((i+vm.getStart())<9) {
        System.out.print(" ");
      }
      System.out.print(i+vm.getStart()+".");

      System.out.print(src.get(i));
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
    while(tokens.hasMoreElements()) {
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
          System.out.print("The following lines are invalid for break points:");
        }
        System.out.print(" "+breaksArray.get(i));
      }
    }
    if(invalids > 0) {
      System.out.println();
    }
    vm.setBreaks(breakPts);
  }

  private Boolean isValidBreak(int num ) {
    return vm.isValidBreak(num);
  }

  private void displaySource(Vector<String> src, Vector<Boolean> breaks,int line) {
    for(int i=0; i<src.size(); i++) {
      if((i+1) == line) {
        System.out.print(">");
      }
      else if(breaks.get(i)) {
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
      System.out.println();
    }
  }
}
