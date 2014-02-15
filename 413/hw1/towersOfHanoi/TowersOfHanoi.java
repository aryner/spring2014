package towersOfHanoi;

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
  public static void transferDisks(GameBoardInterface board, PegInterface src, PegInterface util, PegInterface dst, int numDisks){
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

