package runTimeStack;

import java.util.*;

/**
 * The RunTimeStack class is responsible for storing the
 * information contained in the programs run time stack.
 * This entails keeping track of each frame and the variables
 * stored in each frame.  Also being able create new frames
 * and move back to old frames.
*/

public class RunTimeStack {
  Stack<Integer> framePointers;
  Vector<Integer> runStack;

  public RunTimeStack() {
    framePointers = new Stack<Integer>();
    runStack = new Vector<Integer>();
    framePointers.push(0);
  }

 /**
 * Prints the contents of each frame between brakets [].
 * Each frame is separated by a space.
*/
  public void dump() {
    Iterator stackIt = framePointers.iterator();
    int frameEnd;
    int old;
    int pos = 0;
    frameEnd = (int)stackIt.next();
    if(stackIt.hasNext()) {
      frameEnd = (int)stackIt.next();
    }
    else {
      frameEnd = -1;
    }
    while(pos < runStack.size()) {
      System.out.print("[");
      if(frameEnd == -1) {
        while(pos < runStack.size()-1) {
          System.out.print(runStack.get(pos) + ",");
          pos++;
        }
        System.out.print(runStack.get(pos)+"] ");
        pos++;
      }
      else {
        while(pos < frameEnd-1) {
          System.out.print(runStack.get(pos) + ",");
          pos++;
        }
        if(pos < frameEnd){
          System.out.print(runStack.get(pos));
          pos++;
        }
        System.out.print("] ");
      }
      if(stackIt.hasNext()) {
        frameEnd = (int)stackIt.next();
      }
      else {
        frameEnd = -1;
      }
    }
    System.out.println();
  }

  public int peek() {
    return runStack.get(runStack.size()-1);
  }

  public int pop() {
    return runStack.remove(runStack.size()-1);
  }

  public int push(int i) {
    runStack.add(i);
    return i;
  }

  public int framePeek() {
    return framePointers.peek();
  }

  public int runStackSize() {
    return runStack.size();
  }

/**
 * Starts a new frame, taking some of its
 * contents from the previous frame.
 * @param offset is the number of elements taken from the previous frame.
*/  
  public void newFrameAt(int offset) {
    framePointers.push(runStack.size()-offset);
  }

/**
 * @return a vector containing the current frames contents
*/
  public Vector<Integer> getFrame() {
    Vector<Integer> result = new Vector<Integer>();
    for(int i = framePointers.peek(); i< runStack.size(); i++) {
      result.add(runStack.get(i));
    }
    return result;
  }

/**
 * Empties the contents of the current frame and removes the 
 * current frame.
 * @return the top of the frame to be removed
*/
  public int popFrame() {
    int result = runStack.remove(runStack.size()-1);
    int top = framePointers.pop();
    while(runStack.size() > top) {
      runStack.remove(runStack.size()-1);
    }
    runStack.add(result);
    return result;
  }

/**
 * Stores the top of the stack into the stack position
 * dictated by the offset.
 * @param offset is the offset from the current frame pointer.
*/
  public int store(int offset) {
    int result = runStack.get(runStack.size()-1);
    runStack.set(framePointers.peek()+offset, runStack.remove(runStack.size()-1));
    return result;
  }

/**
 * Takes the contents of a stack position given by the offset
 * and places its value on top of the stack.
 * @param offset is the offset from the current frame pointer
*/
  public int load(int offset) {
    runStack.add(runStack.get(framePointers.peek()+offset));
    return runStack.get(runStack.size()-1);
  }

  public Integer push(Integer i) {
    runStack.add(i);
    return i;
  }
}
