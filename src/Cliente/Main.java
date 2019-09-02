package Cliente;

import java.io.*;
import java.net.*;

class Main {
	
	private static Socket clientSocket;
	private static DataOutputStream outToServer;

	public static void main(String argv[]) throws Exception {

		/* Instancia variaveis */
		instantiate();
		
		/* Lista opcoes */
		Options();
		
		/* Cria uma stream de entrada para receber os dados do servidor */
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		Thread listen = new Thread(new ListenThread(inFromServer));
		listen.start();

		while (true) {
			/* Pega a mensagem do cliente */
			String sentence = getMessage();

			if (sentence == "/quit") {
				break;
			}

			/* Processa informacoes da opcao */
			sentence = ProcessOption(sentence);

			/* Envia mensagem para o servidor */
			sendMessage(sentence);
		}

		/* Encerra conexão */
		clientSocket.close();

	}

	private static String ProcessOption(String sentence) {
		if (sentence.contentEquals("/criarLeilao")) {
			return "/createAnun";
		} else if (sentence.contentEquals("/mostrarLeiloes")) {
			return "/lisAnun";
		}

		return sentence;

	}

	private static void Options() {
		System.out.println("*Digite '/quit' para encerrar a conexao*");
		System.out.println("/criarLeilao - Para criar o anuncio de um produto");
		System.out.println("/mostrarLeiloes - Para mostrar os leiloes acontecendo agora");

		System.out.println("'/join nomeDoServidor' - Para entrar em um leilão'");

	}

	private static void sendMessage(String sentence) throws IOException {
		/*
		 * Envia para o servidor. Não esquecer do \n no final para permitir que o
		 * servidor use readLine
		 */
		outToServer.writeBytes(sentence + '\n');

	}

	private static String getMessage() throws IOException {
		/* Permite leitura de dados do teclado */
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

		/* Lê uma mensagem digitada pelo usuário */
		System.out.print("Digite uma menssagem: ");
		String sentence = inFromUser.readLine();
		return sentence;
	}

	private static void instantiate() throws UnknownHostException, IOException {

		/* Cria o socket cliente indicando o IP e porta de destino. */
		clientSocket = new Socket("127.0.0.1", 6790);

		/* Cria uma stream de saída para enviar dados para o servidor */
		outToServer = new DataOutputStream(clientSocket.getOutputStream());

	}

	private static int wordCount(String string) {
		int count = 0;

		char ch[] = new char[string.length()];
		for (int i = 0; i < string.length(); i++) {
			ch[i] = string.charAt(i);
			if (((i > 0) && (ch[i] != ' ') && (ch[i - 1] == ' ')) || ((ch[0] != ' ') && (i == 0)))
				count++;
		}
		return count;
	}
}
