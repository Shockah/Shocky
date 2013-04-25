package pl.shockah.shocky;

public abstract class StoppableThread extends Thread {
	protected volatile boolean running = false;
	
	public final void run() {
		running = true;
		onRun();
		running = false;
	}
	public abstract void onRun();
	
	public boolean isRunning() {
		return running;
	}
	public void end() {
		running = false;
	}
}