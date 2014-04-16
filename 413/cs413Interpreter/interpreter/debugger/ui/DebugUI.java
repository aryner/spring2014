package interpreter.debugger.ui;

import interpreter.*;
import interpreter.debugger.*;
import java.util.*;

public class DebugUI {
  public static void start(DebugVM vm) {
    displaySource(vm.getSource(), vm.getBreaks());
    menu(vm);
  }

  public static void menu(DebugVM vm) {
    String command;
    Scanner input = new Scanner(System.in);
    System.out.println("Type ? for help");
    System.out.print("> ");
    command = input.nextLine();

    switch(command) {
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
        displaySource(vm.getSource(), vm.getBreaks());
        menu(vm);
        break;
      case "clear break points":
      case "cbp":
        clearBreaks(vm);
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

  private static void quit(DebugVM vm) {
    vm.quit();
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
      if(isValidBreak(i, vm)){
        breakPts[i] = Integer.parseInt(breaksArray[i]) - 1;
      }
      else {
        invalids++;
        if(invalids > 1) {
          System.out.print("The following lines are invalid for break points:");
        }
        System.out.print(" "+i);
      }
    }
    if(invalids > 0) {
      System.out.println();
    }
    vm.setBreaks(breakPts);
  }

  private static Boolean isValidBreak(int num, DebugVM vm) {
//stubbin
    return true;
  }

  private static void displaySource(Vector<String> src, Vector<Boolean> breaks) {
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
      System.out.println();
    }
  }
}
