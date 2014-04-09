package interpreter.debugger;

import java.util.*;

class Binder {
  private Object value;
  private Symbol prevtop;
  private Binder tail;

  Binder(Object v, Symbol p, Binder t) {
    value = v;
    prevtop = p;
    tail = t;
  }

  Object getValue() { return value; }
  Symbol getPrevtop() { return prevtop; }
  Binder getTail() { return tail; }
}

public class SymbolTable {
  private HashMap<Symbol, Binder> symbols = new HashMap<Symbol, Binder>();
  private Symbol top;
  private Binder marks;

  public Object get(Symbol key) {
    Binder e = symbols.get(key);
    return e.getValue();
  }

  public void enter(String id, int offset) {
    put(Symbol.symbol(id), offset);
  }

  public void pop(int n) {
    for(int i=0; i<n; i++) {
      Binder e = symbols.get(top);
      if(e.getTail() != null) {
        symbols.put(top, e.getTail());
      }
      else {
        symbols.removetop();
      }
      top = e.getPrevTop();
    }
  }

  public void put(Symbol key, Object value) {
    symbols.put(key, new Binder(value, top, symbols.get(key)));
    top = key;
  }

  public void beginScope() {
    marks = new Binder(null, top, marks);
    top = null;
  }

  public void endScope() {
    while (top!=null) {
      Binder e = symbols.get(top);
      if (e.getTail()!=null) {
        symbols.put(top, e.getTail());
      }
      else {
        symbols.removetop();
      }
      top = e.getPrevtop();
    }
    top = marks.getPrevtop();
    marks = marks.getTail();
  }

  public Set<Symbol> keys() {
    return symbols.keySet();
  }
}
