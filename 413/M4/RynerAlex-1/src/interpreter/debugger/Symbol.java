package interpreter.debugger;

import java.util.*;

public class Symbol {
  private String name;

  private Symbol(String n) {
    name = n;
  }

  private static HashMap<String, Symbol> symbols = new HashMap<String, Symbol>();

  public String toString() {
    return name;
  }

  public static Symbol symbol(String newSymbol) {
    Symbol s = symbols.get(newSymbol);
    if (s == null) {
      s = new Symbol(newSymbol);
      symbols.put(newSymbol, s);
    }
    return s;
  }
}
