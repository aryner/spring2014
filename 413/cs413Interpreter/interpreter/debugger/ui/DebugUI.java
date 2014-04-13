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
      default:
        System.out.println("Invalid command");
        menu(vm);
        break;
    }
  }

  private static void displaySource(Vector<String> src, Vector<Boolean> breaks) {
    for(int i=0; i<src.size(); i++) {
      if(i<9) {
        System.out.print(" ");
      }
      System.out.print(i+1+".");

      System.out.print(src.get(i));
      if(breaks.get(i)) {
        System.out.print("\t***");
      }
      System.out.println();
    }
  }
}
