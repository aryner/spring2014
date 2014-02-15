import java.lang.*;
import java.util.*;

public class TowersOfHanoi {
  public static void main(String [] args) {
    GameBoard game;
    int numDisks;

    if(args.length > 0) {
      numDisks = Integer.parseInt(args[0]);
      game = new GameBoard(3, numDisks);
    }
    else {
      Scanner input = new Scanner(System.in);
      System.out.println("How many disks do you want?");
      numDisks = input.nextInt();
      game = new GameBoard(3, numDisks);
    }

    game.displayHeader();
    game.displayBoard();
    transferDisks(game, game.getPeg('A'), game.getPeg('B'), game.getPeg('C'), numDisks);

  }

  //function that takes as arguments, the game board, the peg containing disks to be moved, a utility peg,
  // a destination peg for the disks, and the number of disks to be moved from the source peg to the 
  //  desitination peg.  This function will move the disks from src to dst following the rules of towers of
  //   hanoi and display each move along the way.
  public static void transferDisks(GameBoard board, Peg src, Peg util, Peg dst, int numDisks){
    //if there is only one disk we simply
    // move it to its destination
    if(numDisks == 1) {
      board.moveDisk(src, dst);
      board.displayBoard();
    }
    else {
      //if there is more than one disk we need to move
      // all the disks above the bottom disk to the util
      //  peg to let us move the bottom disk to the dst
      transferDisks(board, src, dst, util, numDisks-1);
      board.moveDisk(src, dst);
      board.displayBoard();

      //if the number of disks is 2 or more then after
      // moving the bottom disk to dst we must move all
      //  the disks on the util peg to the dst peg
      if(numDisks > 1) {
        transferDisks(board, util, src, dst, numDisks-1);
      }
    }
  }
}

class GameBoard {
  private Peg [] pegs;
  private String lastMove;

  public GameBoard(int numPegs, int numDisks) {
    pegs = new Peg[numPegs];

    for(int i=0; i<numPegs; i++) {
      pegs[i] = new Peg();
    }
    
    //if we placed the disks directly into pegs they would 
    // be backwards so we put them in a temp stack first then
    //  put them into pegs in the proper order
    Stack<Disk> temp = new Stack<Disk>();
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

  public void moveDisk(Peg src, Peg dst) {
    dst.stackDisk(src.removeDisk());

    lastMove = dst.sizeOfTopDisk() + " from " + src.getName() + " to " + dst.getName();
  }

  public Peg getPeg(int index) {
    return pegs[index];
  }

  public Peg getPeg(char letter) {
    return pegs[letter - 'A'];
  }
}

class Peg {
  private Stack<Disk> disks;
  private static char nextName = 'A';
  private char name;

  public Peg() {
    disks = new Stack<Disk>();
    name = nextName;
    nextName++;
  }

  public char getName() {
    return name;
  }

  public int sizeOfTopDisk() {
    return disks.peek().getSize();
  }

  public Disk removeDisk() {
    return disks.pop();
  }

  public int diskCount() {
    return disks.size();
  }

  public void stackDisk(Disk newDisk) {
    disks.push(newDisk);
  }

  public void displayDisks() {
    Iterator<Disk> diskIterator = disks.iterator();
    while(diskIterator.hasNext()) {
      System.out.print(diskIterator.next().getSize());
    }
  }
}

class Disk {
  private static int nextSize = 1;
  private int size;

  public Disk() {
    size = nextSize;
    nextSize++;
  }

  public int getSize() {
    return size;
  }
}
