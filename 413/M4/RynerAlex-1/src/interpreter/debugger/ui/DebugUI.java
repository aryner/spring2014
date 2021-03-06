package interpreter.debugger.ui;

import interpreter.*;
import interpreter.debugger.*;
import java.util.*;

public class DebugUI {
  public static void start(DebugVM vm) {
    displayFunction(vm);
    menu(vm);
  }

  public static void menu(DebugVM vm) {
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
        menu(vm);
        break;
      case "continue":
      case "cont":
        vm.executeProgram();
        break;
      case "stepOut":
      case "so":
        stepOut(vm);
        vm.executeProgram();
        break;
      case "setBreak":
      case "sb":
        setBreaks(vm, tokens);
        menu(vm);
        break;
      case "displaySource":
      case "ds":
        displaySource(vm.getSource(), vm.getBreaks(), vm.getCurr());
        menu(vm);
        break;
      case "clearBreakBoints":
      case "cbp":
        clearBreaks(vm);
        menu(vm);
        break;
      case "displayVariables":
      case "dv":
        displayVars(vm);
        menu(vm);
        break;
      case "displayFunction":
      case "df":
        displayFunction(vm);
        menu(vm);
        break;
      case "quit":
        quit(vm);
        break;
      default:
        System.out.println("Invalid command");
        menu(vm);
        break;
    }
  }

  private static void help() {
    System.out.println("\thelp,  ?\t\t\tDisplay list of commands");
    System.out.println("\tcontinue, cont\t\t\tContinue execution of program");
    System.out.println("\tsetBreak,  sb\t\t\tStart prompt for line numbers to set break points");
    System.out.println("\tstepOut,  so\t\t\tStep out of current function");
    System.out.println("\tdisplaySource,  ds\t\tDisplay source program");
    System.out.println("\tclearBreakPoints,  cbp\t\ttRemove all break points");
    System.out.println("\tdisplayVariables,  dv\t\tDisplay current varaiables");
    System.out.println("\tdisplayFunction,  df\t\tDisplay source code of the current function");
    System.out.println("\tquit\t\t\t\tQuit exection of program");
  }

  private static void stepOut(DebugVM vm) {
    vm.setStepOut();
  }

  private static void displayVars(DebugVM vm) {
    Vector<String> vars = vm.getVars();
    for(int i=0; i<vars.size(); i++) {
      System.out.println(vars.get(i));
    }
  }

  private static void quit(DebugVM vm) {
    vm.quit();
  }

  private static void displayFunction(DebugVM vm) {
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

  private static void clearBreaks(DebugVM vm) {
    vm.clearBreaks();
  }

  private static void setBreaks(DebugVM vm, StringTokenizer tokens) {
    Vector<Integer> breaksArray = new Vector<Integer>();
    while(tokens.hasMoreElements()) {
      breaksArray.add(Integer.parseInt(tokens.nextToken()));
    }
    int[] breakPts = new int[breaksArray.size()];
    int invalids = 0;
    for(int i=0; i<breaksArray.size(); i++) {
      if(isValidBreak(breaksArray.get(i), vm)){
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

  private static Boolean isValidBreak(int num, DebugVM vm) {
    return vm.isValidBreak(num);
  }

  private static void displaySource(Vector<String> src, Vector<Boolean> breaks,int line) {
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
