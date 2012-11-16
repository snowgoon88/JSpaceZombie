


/**
 * 
 */

/**
 * Measure timer resolution.
 * 
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class TimerRes {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		sysTimeNano();
		sysTimeNano();
		sysTimeNano();
		sysTimeNano();
		sysTimeNano();
		sysTimeMilli();
		
	}
	
	private static void javaxManagementTimer() {
		java.util.Timer _timer1;
		javax.management.timer.Timer _timer2;
		
		
		
	}
	private static void sysTimeNano() {
		long total, count1, count2;
		
		count1 = System.nanoTime();
		count2 = System.nanoTime();
		while (count1 == count2) {
			count2 = System.nanoTime();
		}
		total = (count2 - count1);
		
		count1 = System.nanoTime();
		count2 = System.nanoTime();
		while (count1 == count2) {
			count2 = System.nanoTime();
		}
		total += (count2 - count1);
		
		count1 = System.nanoTime();
		count2 = System.nanoTime();
		while (count1 == count2) {
			count2 = System.nanoTime();
		}
		total += (count2 - count1);
		
		count1 = System.nanoTime();
		count2 = System.nanoTime();
		while (count1 == count2) {
			count2 = System.nanoTime();
		}
		total += (count2 - count1);
		
		System.out.println("Sytem.nanoTime: " + total/4 + " ns");
	}
	private static void sysTimeMilli() {
		long total, count1, count2;
		
		count1 = System.currentTimeMillis();
		count2 = System.currentTimeMillis();
		while (count1 == count2) {
			count2 = System.currentTimeMillis();
		}
		total = 1000L * (count2 - count1);
		
		count1 = System.currentTimeMillis();
		count2 = System.currentTimeMillis();
		while (count1 == count2) {
			count2 = System.currentTimeMillis();
		}
		total += 1000L * (count2 - count1);
		
		count1 = System.currentTimeMillis();
		count2 = System.currentTimeMillis();
		while (count1 == count2) {
			count2 = System.currentTimeMillis();
		}
		total += 1000L * (count2 - count1);
		
		count1 = System.currentTimeMillis();
		count2 = System.currentTimeMillis();
		while (count1 == count2) {
			count2 = System.currentTimeMillis();
		}
		total += 1000L * (count2 - count1);
		
		System.out.println("Sytem.currentTimeMillis: " + total/4 + " ms");
	}
}
