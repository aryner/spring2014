package towersOfHanoi;

import java.lang.*;
import java.util.*;

class GameBoard implements GameBoardInterface {
  private PegInterface [] pegs;
  private String lastMove;

  public GameBoard(int numPegs, int numDisks) {
    pegs = new Peg[numPegs];

    for(int i=0; i<numPegs; i++) {
      pegs[i] = new Peg();
    }
    
    //if we placed the disks directly into pegs they would 
    // be backwards so we put them in a temp stack first then
    //  put them into pegs in the proper order
    Stack<DiskInterface> temp = new Stack<DiskInterface>();
    for(int i=0; i<numDisks; i++) {
      temp.push(new Disk());
    }

    while(temp.size() > 0) {
      pegs[0].stackDisk(temp.pop());
    }

    lastMove = "init";
  }

  public void displayHeader() {
    System.out.println("Move\t\tPeg Configuration");
    System.out.print("\t\t");

    for(int i=0; i<pegs.length; i++) {
      System.out.print(pegs[i].getName() + "\t\t");
    }
   
    System.out.println();
  }

  public void displayBoard() {
    System.out.print(lastMove + "\t");
    if(lastMove == "init") {
      System.out.print("\t");
    }
   
    for(int i=0; i<pegs.length; i++) {
      pegs[i].displayDisks();
      System.out.print("\t");
      if(pegs[i].diskCount() < 8) {
        System.out.print("\t");
      }
    }

    System.out.println();
  }

  public void moveDisk(PegInterface src, PegInterface dst) {
    dst.stackDisk(src.removeDisk());

    lastMove = dst.sizeOfTopDisk() + " from " + src.getName() + " to " + dst.getName();
  }

  public PegInterface getPeg(int index) {
    return pegs[index];
  }

  public PegInterface getPeg(char letter) {
    return pegs[letter - 'A'];
  }
}
