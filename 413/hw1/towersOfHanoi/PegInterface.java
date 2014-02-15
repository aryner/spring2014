package towersOfHanoi;

interface PegInterface {
  //returns the name of this peg
  char getName();

  //returns the size of the top disk on 
  // this peg
  int sizeOfTopDisk();

  //removes and returns the top disk on 
  // this peg
  DiskInterface removeDisk();

  //returns the number of disks on this peg
  int diskCount();

  //places newDisk on top of this pegs
  // stack of disks
  void stackDisk(DiskInterface newDisk);

  //displays the sizes of the disks on the peg
  void displayDisks();

}
