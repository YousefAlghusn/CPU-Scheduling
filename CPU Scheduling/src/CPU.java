import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class CPU {

	ArrayList<Process> processesSRTF;
	ArrayList<Process> processesPrio;
	ArrayList<Process> processesRR;
	ArrayList<Process> processesPrem;

	public CPU() {
		processesSRTF = new ArrayList<>();
		processesPrio = new ArrayList<>();
		processesRR = new ArrayList<>();
		processesPrem = new ArrayList<>();
	}

	public void readPrio(String path) {

		try {
			FileReader fr = new FileReader(path); // reads the file
			BufferedReader br = new BufferedReader(fr); // creates a buffering character input stream

			String line;
			int count = 1;
			String name = "";

			while ((line = br.readLine()) != null) {
				if (count % 2 == 0) { // reading even lines
					line = line.replaceAll(" ", ""); // removing extra spaces to avoid issues in casting
					String n[] = line.split(",");
					double time = Integer.parseInt(n[0]);
					int priority = Integer.parseInt(n[1]);
					processesPrio.add(new Process(name, time, priority));
				} else { // odd line has only the name of processes
					name = line.replaceAll(" ", "");
				}
				count++;
			}
			fr.close(); // closes the stream and release the resources

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readPrem(String path) {

		try {

			FileReader fr = new FileReader(path); // reads the file
			BufferedReader br = new BufferedReader(fr); // creates a buffering character input stream
			String line;
			int count = 0;
			while ((line = br.readLine()) != null) {
				if (line.contains("/"))
					continue;
				line = line.replaceAll(" ", ""); // removing extra spaces to avoid issues in casting

				String n[] = line.split(",");
				int prio = Integer.parseInt(n[0]);
				int arrival = Integer.parseInt(n[1]);
				int burst = Integer.parseInt(n[2]);

				count++;
				processesPrem.add(new Process(count, prio, arrival, burst));
			}
			fr.close(); // closes the stream and release the resources
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readRR(String path) {

		try {
			FileReader fr = new FileReader(path); // reads the file
			BufferedReader br = new BufferedReader(fr); // creates a buffering character input stream
			String line;
			int count = 1;
			String name = "";
			while ((line = br.readLine()) != null) {
				if (count % 2 == 0) { // reading even lines
					line = line.replaceAll(" ", ""); // removing extra spaces to avoid issues in casting
					double time = Integer.parseInt(line);
					processesRR.add(new Process(name, time));
				} else { // odd line has only the name of processes
					name = line.replaceAll(" ", "");
				}
				count++;
			}
			fr.close(); // closes the stream and release the resources
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readSRTF(String path) {

		try {

			FileReader fr = new FileReader(path); // reads the file
			BufferedReader br = new BufferedReader(fr); // creates a buffering character input stream
			String line;

			while ((line = br.readLine()) != null) {
				if (line.contains("/"))
					continue;
				line = line.replaceAll(" ", ""); // removing extra spaces to avoid issues in casting

				String n[] = line.split(",");
				int id = Integer.parseInt(n[0]);
				int arrival = Integer.parseInt(n[1]);
				int burst = Integer.parseInt(n[2]);
				processesSRTF.add(new Process(id, arrival, burst));
			}
			fr.close(); // closes the stream and release the resources
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// preemptive
	public void SRTF() {

		PriorityQueue<Process> queue = new PriorityQueue<>(compBurst()); // PQ on remaining burst

		System.out.println("\nSRTF:");
		String underScore = "";
		String chart = "";
		String underLine = "";
		int formating = 0;

		processesSRTF.sort(comprive());
		Process s = processesSRTF.get(0);
		queue.add(s);

		for (Process e : processesSRTF)
			if (!queue.contains(e) && e.arrival == s.arrival)
				queue.add(e);

		chart = "|";
		underLine = String.valueOf(s.arrival);
		int count = 0;
		int i = 1;
		while (!queue.isEmpty()) {
			Process p = queue.poll();
			chart += p.name;

			formating = String.valueOf(count).length();
			underLine += dashing(p.name.length() - (formating - 1));
			while (p.remBurst > 0) {
				chart += " ";
				p.remBurst--;
				count++;
				if (formating-- <= 3)
					underLine += "—";
				for (Process e : processesSRTF) {
					if (e.arrival == count && !queue.contains(e)) { // new process has arrived
						queue.add(e);
						i++; // number of processes in queue
					}
				}
				Process p2 = queue.peek();
				if (p2 != null && p2.remBurst < p.remBurst) {
					queue.add(p);
					break; // interrupt if new process has less time
				}

			}
			chart += "|";
			underLine += count;
			if (p.remBurst <= 0) { // if remaining time == 0 that means the process has finished
				p.waiting = count - (p.burst + p.arrival);
				p.turnAround = count;
			}
		}
		underScore += under(chart.length());
		System.out.println(underScore);
		System.out.println(chart);
		System.out.println(underLine);
		double totalWaiting = 0, totalTurn = 0;
		for (Process e : processesSRTF) {
			totalWaiting += e.waiting;
			totalTurn += e.turnAround;
		}
		int length = processesSRTF.size();
		System.out.format("Average waiting: %.2f ms\n", totalWaiting / length);
		System.out.format("Average completion: %.2f ms\n", totalTurn / length);
	}

	// Non-preemptive
	public void priorityScheduling() {

		PriorityQueue<Process> queue = new PriorityQueue<>(comprio());

		System.out.println("\nPriority Scheduling:");
		String underScore = "";
		String chart = "";
		String underLine = "";
		int formating = 0;

		for (Process e : processesPrio) {
			queue.add(e);
		}

		chart = "|";
		underLine = "0";
		int count = 0;
		while (!queue.isEmpty()) {
			Process p = queue.poll();
			chart += p.name;
			underLine += dashing(p.name.length());

			formating = String.valueOf(count).length();
			while (p.remBurst > 0) {
				chart += " ";
				p.remBurst--;
				count++;
				if (formating-- <= 1)
					underLine += "—";
			}
			chart += "|";
			underLine += count;
			if (p.remBurst <= 0) {
				p.waiting = count - (p.burst + p.arrival);
				p.turnAround = count - p.arrival;
			}
		}
		underScore += under(chart.length());
		System.out.println(underScore);
		System.out.println(chart);
		System.out.println(underLine);

		double totalWaiting = 0, totalTurn = 0;
		for (Process e : processesPrio) {
			totalWaiting += e.waiting;
			totalTurn += e.turnAround;
		}
		int length = processesPrio.size();
		System.out.format("Average waiting: %.2f ms\n", totalWaiting / length);
		System.out.format("Average completion: %.2f ms\n", totalTurn / length);

	}

	public void RR(int quantum) {

		Queue<Process> queue = new LinkedList<>();

		System.out.println("\nRound Rubin:");
		String underScore = "";
		String chart = "";
		String underLine = "";

		int formating = 0;

		for (Process e : processesRR) {
			queue.add(e);
		}

		chart = "|";
		underLine = "0";
		int count = 0;
		while (!queue.isEmpty()) {
			Process p = queue.poll();
			chart += p.name;

			formating = String.valueOf(count).length();

			underLine += dashing(p.name.length() - (formating - 1));
			boolean yes = true;
			if (formating >= 2)
				yes = false;
			int repeat = quantum;
			while (repeat-- != 0 && p.remBurst > 0) { // leave if process have finished or qunatum has finished
				chart += " ";
				p.remBurst--;
				count++;
				// if(yes)
				underLine += "—";
				yes = true;

			}
			chart += "|";
			underLine += count;
			if (p.remBurst != 0) // if process has not finished put him back in queue
				queue.add(p);
			if (p.remBurst <= 0) {
				p.waiting = count - (p.burst + p.arrival);
				p.turnAround = count - p.arrival;
				
			}
		}
		underScore += under(chart.length());
		System.out.println(underScore);
		System.out.println(chart);
		System.out.println(underLine);

		double totalWaiting = 0, totalTurn = 0;
		for (Process e : processesRR) {
			totalWaiting += e.waiting;
			totalTurn += e.turnAround;
			System.out.println(e.name+": turnAround: "+e.turnAround);
		}
		int length = processesRR.size();
		System.out.format("Average waiting: %.2f ms\n", totalWaiting / length);
		System.out.format("Average completion: %.2f ms\n", totalTurn / length);

	}

	public void prem() {

		PriorityQueue<Process> queue = new PriorityQueue<>(comprio()); // PQ on remaining burst

		System.out.println("\nPriority Preemptive:");
		String underScore = "";
		String chart = "";
		String underLine = "";
		int formating = 0;

		processesPrem.sort(comprive());
		Process s = processesPrem.get(0);
		queue.add(s);

		for (Process e : processesPrem)
			if (!queue.contains(e) && e.arrival == s.arrival)
				queue.add(e);

		chart = "|";
		underLine = String.valueOf(s.arrival);
		int count = 0;
		int i = 1;
		while (!queue.isEmpty()) {
			Process p = queue.poll();
			chart += p.name;

			formating = String.valueOf(count).length();
			underLine += dashing(p.name.length() - (formating - 1));
			while (p.remBurst > 0) {
				chart += " ";
				p.remBurst--;
				count++;
				if (formating-- <= 3)
					underLine += "—";
				for (Process e : processesPrem) {
					if (e.arrival == count && !queue.contains(e)) { // new process has arrived
						queue.add(e);
						i++; // number of processes in queue
					}
				}
				Process p2 = queue.peek();
				if (p2 != null && p2.priority < p.priority) {
					queue.add(p);
					break; // interrupt if new process has more priority
				}

			}
			chart += "|";
			underLine += count;
			if (p.remBurst <= 0) { // if remaining time == 0 that means the process has finished
				p.waiting = count - (p.burst + p.arrival);
				p.turnAround = count;
				p.finish = count;
			}
		}
		underScore += under(chart.length());
		System.out.println(underScore);
		System.out.println(chart);
		System.out.println(underLine);
		double totalWaiting = 0, totalTurn = 0;
		for (Process e : processesPrem) {
			totalWaiting += e.waiting;
			totalTurn += e.turnAround;
			System.out.println(e.name + ": waiting: " + e.waiting + " finish: " + e.finish);
		}
		int length = processesPrem.size();
		System.out.format("Average waiting: %.2f ms\n", totalWaiting / length);
		System.out.format("Average completion: %.2f ms\n", totalTurn / length);
	}

	// getting certain String in n length
	private String dashing(int length) {
		String n = "";
		for (int i = 0; i < length; i++)
			n += "—";
		return n;
	}

	private String under(int length) {
		String n = "";
		for (int i = 0; i < length; i++)
			n += "_";
		return n;
	}

	// all are Comparators for the priority queues above
	private Comparator<Object> comprio() {
		return new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				// TODO Auto-generated method stub
				Process i = (Process) o1;
				Process j = (Process) o2;
				if (i.priority > j.priority)
					return 1;
				else if (i.priority < j.priority)
					return -1;
				else
					return 0;
			}
		};
	}

	private Comparator<Object> comprive() {
		return new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				// TODO Auto-generated method stub
				Process i = (Process) o1;
				Process j = (Process) o2;
				if (i.arrival > j.arrival)
					return 1;
				else if (i.arrival < j.arrival)
					return -1;
				else if (i.remBurst > j.remBurst)
					return 1;
				else if (i.remBurst < j.remBurst)
					return -1;
				else
					return 0;
			}
		};
	}

	private Comparator<Object> compBurst() {
		return new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				// TODO Auto-generated method stub
				Process i = (Process) o1;
				Process j = (Process) o2;
				if (i.remBurst > j.remBurst)
					return 1;
				else if (i.remBurst < j.remBurst)
					return -1;
				else
					return 0;
			}
		};
	}

	// end
}
