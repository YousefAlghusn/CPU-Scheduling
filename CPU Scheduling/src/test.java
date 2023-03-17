import java.util.Scanner;

public class test {
	static int askUser() {
		Scanner input = new Scanner(System.in);
		System.out.println("Enter Quantum for RR: ");
		return input.nextInt();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int quantum = askUser(); // ask user for quantum
		CPU intel = new CPU();
		
		intel.readPrio("D:\\kUniversity\\level 6\\CSC 227\\Project\\testdata1 for priortiy.txt"); // priority
		intel.readSRTF("D:\\\\kUniversity\\\\level 6\\\\CSC 227\\\\Project\\\\testdata1 for SRTF.txt"); // SRTF
		intel.readRR("D:\\kUniversity\\level 6\\CSC 227\\Project\\RR.txt"); // RR
		//intel.readPrem("D:\\kUniversity\\level 6\\CSC 227\\Project\\testdata1_Prem.txt");
		intel.priorityScheduling();
		intel.SRTF();
		intel.RR(quantum);
		//intel.prem();
		
		
		
	}

}
