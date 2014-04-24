package interpreter;

import java.io.*;
import byteCodes.*;
import interpreter.debugger.*;

/**
 * <pre>
 * 
 *  
 *   
 *     Interpreter class runs the interpreter:
 *     1. Perform all initializations
 *     2. Load the bytecodes from file
 *     3. Run the virtual machine
 *     
 *   
 *  
 * </pre>
 */
public class Interpreter {

	ByteCodeLoader bcl;
        String src;

	public Interpreter(String codeFile) {
		try {
			CodeTable.init();
			bcl = new ByteCodeLoader(codeFile);
		} catch (IOException e) {
			System.out.println("**** " + e);
		}
	}

	public Interpreter(String codeFile, String sourceFile) {
		try {
			CodeTable.debugInit();
			bcl = new ByteCodeLoader(codeFile);
                        src = sourceFile;
		} catch (IOException e) {
			System.out.println("**** " + e);
		}
	}

	void run() {
		Program program = bcl.loadCodes();
		VirtualMachine vm = new VirtualMachine(program);
		vm.executeProgram();
	}

        void debug() {
          try{
            Program program = bcl.loadCodes();
            DebugVM vm = new DebugVM(program, src);
            vm.executeProgram();
          }
          catch(IOException e) {
	    System.out.println("**** " + e);
          }
        }

	public static void main(String args[]) {
		if (args.length == 0) {
			System.out.println("***Incorrect usage, try: java interpreter.Interpreter <file>");
			System.exit(1);
		}
                if(args[0].equals("-d")) {
                  (new Interpreter(args[1]+".x.cod",args[1]+".x")).debug();
                }
                else {
		  (new Interpreter(args[0])).run();
                }
	}
}
