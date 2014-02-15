import java.util.*;

public class Hanoi 
{

	public static void main (String[] args) {

		// init 3 pegs
		Peg a = new Peg("A", "source");
		Peg b = new Peg("B", "util");
		Peg c = new Peg("C", "dest");

		// fetch user input (n disks to be used)
		Scanner input = new Scanner(System.in);
		System.out.print("enter the number disks to be used: ");
		int numDisks = input.nextInt();

		// init src peg A
		for(int i = numDisks - 1; i >= 0; i--) {
			Disk d = new Disk(i+1);
			a.push(d);
		}

		// move disks from Peg A -> Peg C
		doTowers(numDisks, a, b, c);		
	}

	// recursive movement of disks
	// no larger disk is ever placed on a smaller disk
	static void doTowers(int disks, Peg src, Peg util, Peg dest) {
	  if (disks == 1) {      
	    dest.push(src.pop());
	    System.out.println("Disk "+ disks + " from " + src.getName() + " to " + dest.getName());
	  } else {
	    doTowers(disks - 1, src, dest, util);  
	   	dest.push(src.pop());
	    System.out.println("Diks "+ disks + " from " + src.getName() + " to " + dest.getName());
	    doTowers(disks - 1, util, src, dest);
	  }
	}

}

// size propery indicates the value of the disk
class Disk 
{
	private int size;

	public Disk() {
		size = 0;
	}

	public Disk(int s) {
		size = s;
	}

	public int getSize() {
		return size;
	}
}

// name property indicates the name of the peg
// type property indicates the type of peg
// stack ds used to add and remove disks to/from peg
class Peg 
{
	private String name;
	private String type;
	private Stack<Disk> disks = new Stack<Disk>();

	public Peg() {
		name = "";
		type = "";
	}

	public Peg(String n, String t) {
		name = n;
		type = t;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public Disk pop() {
		return disks.pop();
	}

	public void push(Disk d) {
		disks.push(d);
	}

	public Disk peek() {
		return disks.peek();
	}

	public boolean isEmpty() {
		return disks.empty();
	}

}

