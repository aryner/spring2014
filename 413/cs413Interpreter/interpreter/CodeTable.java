package interpreter;

import java.lang.*;
import java.io.*;
import java.util.*;

/**
 * The CodeTable class contains the hash map for each byte code
 * to its byte code class so we can use reflections to instantiate
 * byte code classes.
*/
public class CodeTable {
  private static java.util.HashMap<String, String> codes = new java.util.HashMap<String, String>();

  private CodeTable() {
    codes.put("ARGS", "ArgsCode");
    codes.put("BOP", "BopCode");
    codes.put("CALL", "CallCode");
    codes.put("DUMP", "DumpCode");
    codes.put("FALSEBRANCH", "FalseBranchCode");
    codes.put("GOTO", "GoToCode");
    codes.put("HALT", "HaltCode");
    codes.put("LABEL", "LabelCode");
    codes.put("LIT", "LitCode");
    codes.put("LOAD", "LoadCode");
    codes.put("POP", "PopCode");
    codes.put("READ", "ReadCode");
    codes.put("RETURN", "ReturnCode");
    codes.put("STORE", "StoreCode");
    codes.put("WRITE", "WriteCode");
  }

  public static void init() {
    new CodeTable();
  }
  
  public static String get(String code) {
    return codes.get(code);
  }

}

  
