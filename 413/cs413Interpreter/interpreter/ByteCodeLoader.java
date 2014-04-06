package interpreter;

import byteCodes.*;
import java.lang.*;
import java.io.*;
import java.util.*;

/**
 * The ByteCodeLoader class reads the file that contains the byte code
 * program and uses the codeTable to generate its byte codes and then
 * load them into the program
*/
public class ByteCodeLoader {
  Boolean EOF;
  BufferedReader codeFile;
  ByteCode currCode;
  Vector<String> args;
  Program program;
  StringTokenizer tokens;
  String code;

  public ByteCodeLoader(String file) throws FileNotFoundException{
    codeFile = new BufferedReader(new FileReader(file));
    args = new Vector<String>();
    program = new Program();
    EOF = false;
  }

  public Program loadCodes() {
    while(!EOF) {
      try{
        args.clear();
        tokens = new StringTokenizer(codeFile.readLine());
        code = CodeTable.get(tokens.nextToken());
        currCode = (ByteCode)(Class.forName("byteCodes."+code).newInstance());
        while(tokens.hasMoreTokens()) {
          args.add(tokens.nextToken());
        }
        if (!args.isEmpty()) {
          currCode.loadArgs(args);
        }
        program.loadLine(currCode);
      }
      catch (Exception e) {
        EOF = true;
      }
    }
    program.resolveAddresses();

    return program;
  }
}
