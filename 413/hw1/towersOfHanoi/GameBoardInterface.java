package towersOfHanoi;

interface GameBoardInterface {
  //displays the most recent move and the
  // status of the board after making that
  //  move
  void displayBoard();

  //displays headers for the table of moves
  void displayHeader();

  //moves the top disk from src peg to dst peg
  // throws an exception if disk is to big to 
  //  be placed on the dst peg
  void moveDisk(PegInterface src, PegInterface dst);

  //returns the peg, 0 is the peg which initially
  // holds all the disks and the last index is the
  //  destination peg
  PegInterface getPeg(int index);

  //returns the peg, A is the peg which initially
  // holds all the disks and the last index is the
  //  destination peg
  PegInterface getPeg(char letter);
}
