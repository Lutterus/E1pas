package Servidor;

import java.net.*;

class Main {

	public static void main(String argv[]) throws Exception {
		/* Cria socket do servidor */
		ServerSocket welcomeSocket = new ServerSocket(6790);

		/* Cria estrutura da lista de anuncios */
		adList adList = new adList();

		while (true) {
			/*
			 * Aguarda o recebimento de uma conexão. O servidor fica aguardando neste ponto
			 * até que nova conexão seja aceita.
			 */

			/* Quando aceita cria uma thread */
			Thread listen = new Thread(new ListenThread(welcomeSocket.accept(), adList));
			listen.start();

		}

	}

}
