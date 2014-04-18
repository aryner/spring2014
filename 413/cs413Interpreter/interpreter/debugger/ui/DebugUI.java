package interpreter.debugger.ui;

import interpreter.*;
import interpreter.debugger.*;
import java.util.*;

public class DebugUI {
  public static void start(DebugVM vm) {
    displaySource(vm.getSource(), vm.getBreaks(), vm.getCurr());
    menu(vm);
  }

  public static void menu(DebugVM vm) {
    String command;
    Scanner input = new Scanner(System.in);
    System.out.println("Type ? for help");
    System.out.print("> ");
    command = input.nextLine();

    switch(command.trim()) {
      case "help":
      case "?":
        help();
        menu(vm);
        break;
      case "continue":
      case "cont":
        vm.executeProgram();
        break;
      case "set break":
      case "sb":
        setBreaks(vm);
        menu(vm);
        break;
      case "display source":
      case "ds":
        displaySource(vm.getSource(), vm.getBreaks(), vm.getCurr());
        menu(vm);
        break;
      case "clear break points":
      case "cbp":
        clearBreaks(vm);
        menu(vm);
        break;
      case "display variables":
      case "dv":
        displayVars(vm);
        menu(vm);
        break;
      case "display function":
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
    System.out.println("\tset break,  sb\t\t\tStart prompt for line numbers to set break points");
    System.out.println("\tdisplay source,  ds\t\tDisplay source program");
    System.out.println("\tclear break points,  cbp\tRemove all break points");
    System.out.println("\tdisplay variables,  dv\t\tDisplay current varaiables");
    System.out.println("\tdisplay function,  df\t\tDisplay source code of the current function");
    System.out.println("\tquit\t\t\t\tQuit exection of program");
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
      if(breaks.get(i+vm.getStart()+offS)) {
        System.out.print("*");
      }
      else if((i+vm.getStart()) == line) {
        System.out.print(">");
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

  private static void setBreaks(DebugVM vm) {
    Scanner input = new Scanner(System.in);
    System.out.println("Enter the line numbers you wish to add break points at seperated by spaces");
    String breaks = input.nextLine();
    String[] breaksArray = breaks.split("\\s");
    int[] breakPts = new int[breaksArray.length];
    int invalids = 0;
    for(int i=0; i<breaksArray.length; i++) {
      if(isValidBreak(Integer.parseInt(breaksArray[i]), vm)){
        breakPts[i] = Integer.parseInt(breaksArray[i]) - 1;
      }
      else {
        invalids++;
        if(invalids <= 1) {
          System.out.print("The following lines are invalid for break points:");
        }
        System.out.print(" "+breaksArray[i]);
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
      if(breaks.get(i)) {
        System.out.print("*");
      }
      else if((i+1) == line) {
        System.out.print(">");
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
