package towersOfHanoi;

import java.util.*;
import java.lang.*;

class Peg implements PegInterface {
  private Stack<DiskInterface> disks;
  private static char nextName = 'A';
  private char name;

  public Peg() {
    disks = new Stack<DiskInterface>();
    name = nextName;
    nextName++;
  }

  public char getName() {
    return name;
  }

  public int sizeOfTopDisk() {
    return disks.peek().getSize();
  }

  public DiskInterface removeDisk() {
    return disks.pop();
  }

  public int diskCount() {
    return disks.size();
  }

  public void stackDisk(DiskInterface newDisk) {
    disks.push(newDisk);
  }

  public void displayDisks() {
    Iterator<DiskInterface> diskIterator = disks.iterator();
    while(diskIterator.hasNext()) {
      System.out.print(diskIterator.next().getSize());
    }
  }
}
