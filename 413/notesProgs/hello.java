import java.lang.*;

public class hello{
  public static void main(String [] args) {
    if(args.length > 0) {
      System.out.print("hello ");
      for(int i=0; i<args.length; i++) {
        System.out.print(args[i] + " ");
      }
      System.out.println();
    }
    else {
      System.out.println("hello everybody");
    }
  }
}
