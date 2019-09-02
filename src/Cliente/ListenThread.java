package Cliente;

import java.io.BufferedReader;
import java.io.IOException;

public class ListenThread implements Runnable {
	private BufferedReader inFromServer;

	public ListenThread(BufferedReader inFromServer) throws IOException {
		this.inFromServer = inFromServer;
	}

	@Override
	public void run() {
		while (true) {
			/* Lê mensagem de resposta do servidor */
			String echo = null;
			try {
				echo = inFromServer.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("FROM SERVER: " + echo);
		}

	}

}

