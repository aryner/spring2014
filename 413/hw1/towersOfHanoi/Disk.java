package towersOfHanoi;

class Disk implements DiskInterface {
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
