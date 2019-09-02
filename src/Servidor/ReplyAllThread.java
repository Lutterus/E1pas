package Servidor;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ReplyAllThread implements Runnable {
	private String clientSentence;
	private Ad ad;

	public ReplyAllThread(String clientSentence, Ad ad) {
		this.ad = ad;
		this.clientSentence = clientSentence;
	}

	@Override
	public void run() {
		for (Socket socket : ad.getUsers()) {
			/* Envia mensagem para os clientes */
			sendMessage(socket, clientSentence);

			/* Determina o IP e Porta de origem */
			InetAddress IPAddress = socket.getInetAddress();
			int port = socket.getPort();

			/* Exibe, IP:port => msg */
			System.out.println(IPAddress.getHostAddress() + ":" + port + " => " + clientSentence);
		}

	}

	private void sendMessage(Socket socket, String clientSentence) {
		/* Adiciona o \n para que o cliente também possa ler usando readLine() */
		String echo = clientSentence + '\n';

		/* Cria uma stream de saída para enviar dados para o cliente */
		DataOutputStream outToClient = null;
		try {
			outToClient = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		/* Envia mensaqgem para o cliente */
		/* Envia mensagem para o cliente */
		try {
			outToClient.writeBytes(echo);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
