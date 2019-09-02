package Servidor;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ReplyOneThread implements Runnable {
	private String clientSentence;
	private Socket connectionSocket;

	public ReplyOneThread(Socket connectionSocket, String clientSentence) {
		this.connectionSocket = connectionSocket;
		this.clientSentence = clientSentence;

	}

	@Override
	public void run() {
		/* Envia mensagem para o cliente */
		sendMessage(clientSentence);
		
		/* Determina o IP e Porta de origem */
		InetAddress IPAddress = connectionSocket.getInetAddress();
		int port = connectionSocket.getPort();

		/* Exibe, IP:port => msg */
		System.out.println(IPAddress.getHostAddress() + ":" + port + " => " + clientSentence);

	}

	private void sendMessage(String clientSentence) {
		/* Adiciona o \n para que o cliente também possa ler usando readLine() */
		String echo = clientSentence + '\n';

		/* Cria uma stream de saída para enviar dados para o cliente */
		DataOutputStream outToClient = null;
		try {
			outToClient = new DataOutputStream(connectionSocket.getOutputStream());
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
