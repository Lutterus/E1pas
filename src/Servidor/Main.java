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
			 * Aguarda o recebimento de uma conex�o. O servidor fica aguardando neste ponto
			 * at� que nova conex�o seja aceita.
			 */

			/* Quando aceita cria uma thread */
			Thread listen = new Thread(new ListenThread(welcomeSocket.accept(), adList));
			listen.start();

		}

	}

}
