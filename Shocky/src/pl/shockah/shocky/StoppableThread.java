package pl.shockah.shocky;

public abstract class StoppableThread extends Thread {
	protected volatile boolean running = false;
	
	public final void run() {
		running = true;
		onRun();
		running = false;
	}
	public abstract void onRun();
	public void onEnd() {}
	
	public final boolean isRunning() {
		return running;
	}
	public final void end() {
		onEnd();
		running = false;
	}
}