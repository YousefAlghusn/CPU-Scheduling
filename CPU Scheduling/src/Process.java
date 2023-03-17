
public class Process {
	
	String name;
	double burst;
	double remBurst;
	int priority;
	int arrival;
	boolean hasPriority = false;
	
	double waiting = 0;
	double turnAround = 0;
	
	double finish = -1;
	
	public Process(String name , double burst) {
		this.name = name;
		this.burst = burst;
		this.remBurst = burst;
	}
	
	
	public Process(String name, double burst , int priority) {
		this.name = name;
		this.burst = remBurst = burst;
		this.priority = priority;
		hasPriority = true;
	}
	public Process(int id, int arrival , double burst) {
		this.name = "P"+id;
		this.burst = remBurst = burst;
		this.arrival = arrival;
		
	}
	public Process(int id ,int prio, int arrival , double burst) {
		this.name = "P"+id;
		this.priority = prio;
		this.burst = remBurst = burst;
		this.arrival = arrival;
		
	}
}
