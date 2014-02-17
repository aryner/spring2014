import java.net.*;
import java.io.*;

public class DateServer {
  public static void main(String[] args) {
    try {
      ServerSocket sock = new ServerSocket(4000);
     
      while(true) {
        Socket client = sock.accept();

        PrintWriter pout = new PrintWriter(client.getOutputStream(), true);

        pout.println(new java.util.Date().toString());
        pout.println("workz!");

        client.close();
      }
    }
    catch(IOException ioe) {
      System.err.println(ioe);
    }
  }
}
