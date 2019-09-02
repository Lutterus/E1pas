package Servidor;

public class TimerThread implements Runnable {
	private adList adList;
	private String name;
	private int time = 0;

	public TimerThread(adList adList, String name) {
		this.adList = new adList();
		this.name = name;

	}

	@Override
	public void run() {
		if (adList.getTime(time, name)) {
			adList.removeAuction(name);
		} else {
			try {
				Thread.sleep(2000); // 2 segundos
				time = time + 2;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
