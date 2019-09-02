package Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ListenThread implements Runnable {
	private BufferedReader inFromClient;
	private Socket connectionSocket;
	private adList adList;

	public ListenThread(Socket connectionSocket, adList adList) throws IOException {
		/* Instancia variaveis */
		this.connectionSocket = connectionSocket;
		this.adList = adList;
		instantiate();
	}

	@Override
	public void run() {
		while (true) {
			/* Pega a mensagem do cliente */
			String clientSentence = getMessage();

			reservedWords(clientSentence);
		}

		/*
		 * Encerra socket do cliente try { connectionSocket.close(); } catch
		 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */

	}

	private void reservedWords(String clientSentence) {
		// clientSentence = clientSentence.replace("\n", "");
		if (clientSentence.contentEquals("/lisAnun")) {
			comands(1, null);

		} else if (clientSentence.contentEquals("/createAnun")) {
			comands(2, null);

		} else if (clientSentence.contains("/join") && wordCount(clientSentence) == 2) {
			comands(3, clientSentence);

		} else if (clientSentence.contains("/nome") && wordCount(clientSentence) > 1) {
			comands(4, clientSentence);

		} else if (clientSentence.contains("/descricao") && wordCount(clientSentence) > 1) {
			comands(5, clientSentence);

		} else if (clientSentence.contains("/valorInicial") && wordCount(clientSentence) == 2) {
			comands(6, clientSentence);

		} else if (clientSentence.contentEquals("/aceitarLance")) {
			comands(7, null);

		} else if (clientSentence.contains("/tempoLimite") && wordCount(clientSentence) == 2) {
			comands(8, clientSentence);

		} else if (clientSentence.contentEquals("/cancelarLeilao")) {
			comands(9, null);

		} else {
			comands(0, clientSentence);
		}

	}

	private void comands(int i, String clientSentence) {
		if (i == 1) {
			replyOne(adList.getListNames());

		} else if (i == 2) {
			String name = adList.addAuction(connectionSocket);
			Thread lifeSpan = new Thread(new TimerThread(adList, name));
			lifeSpan.start();
			replyOne("Anuncio criado");
			replyOne("Utilize o comando '/nome NOME' para dar o nome do produto");
			replyOne("Utilize o comando '/descricao' para dar uma descricao para o produto");
			replyOne("Utilize o comando '/valorInicial VALOR' para dar um valor inicial para o lance");
			replyOne("Utilize o comando '/aceitarLance' para aceitar o ultimo lance dado lance");
			replyOne("Utilize o comando '/tempoLimite NUMERO' para definir um tempo limite");
			replyOne("Utilize o comando '/cancelarLeilao' para cancelar o leilão");

		} else if (i == 3) {
			clientSentence = clientSentence.replace("/join ", "");
			if (adList.joinUser(clientSentence, connectionSocket)) {
				replyOne("Bem vindo ao leilão!");
				replyOne("Nome do produto: " + adList.getNameProduct(connectionSocket));
				replyOne("Descrição do leilão: " + adList.getDescription(connectionSocket));
				replyOne("Valor do lance atualmente: " + adList.getValue(connectionSocket));
				
			} else {
				replyOne("Leilão não encontrado");
			}

		} else if (i == 4) {
			clientSentence = clientSentence.replace("/nome ", "");
			if (adList.setName(connectionSocket, clientSentence) == true) {
				replyOne("Nome do leilão alterado com sucesso");
			} else {
				replyOne("ERRO! Somente o administrador pode executar este comando");
			}
		} else if (i == 5) {
			clientSentence = clientSentence.replace("/descricao ", "");
			if (adList.setDescrition(connectionSocket, clientSentence) == true) {
				replyOne("Descrição do leilão alterado com sucesso");
			} else {
				replyOne("ERRO! Somente o administrador pode executar este comando");
			}
		} else if (i == 6) {
			clientSentence = clientSentence.replace("/valorInicial ", "");
			try {
				int result = Integer.parseInt(clientSentence);
				if (result <= 0) {
					replyOne("Para alterar o valor, o mesmo deverá ser maior do que 0");
				} else {
					adList.setNewValue(connectionSocket, result);
					replyOne("Valor alterado para: " + result);
				}

			} catch (Exception e) {
				replyOne("ERRO! Informe um numero para o valor");
			}
		} else if (i == 7) {
			if (adList.endAuction(connectionSocket) == true) {
			} else {
				replyOne("ERRO! Somente o administrador pode executar este comando");
			}

		} else if (i == 8) {
			clientSentence = clientSentence.replace("/tempoLimite ", "");
			try {
				int result = Integer.parseInt(clientSentence);
				if (result <= 0) {
					replyOne("Para alterar o valor, o mesmo deverá ser maior do que 0");
				} else {
					if (adList.setMoreTime(connectionSocket, result) == true) {
						replyOne("Tempo aumentado em " + result + " segundos");
					} else {
						replyOne("ERRO! Somente o administrador pode executar este comando");
					}

				}

			} catch (Exception e) {
				replyOne("ERRO! Informe um numero para o valor");
			}

		} else if (i == 9) {
			if (adList.endAuction(connectionSocket) == true) {
			} else {
				replyOne("ERRO! Somente o administrador pode executar este comando");
			}
		} else {
			try {
				int result = Integer.parseInt(clientSentence);
				if (adList.isInServer(connectionSocket)) {
					int value = adList.getValue(connectionSocket);
					if (value >= result) {
						replyOne("Lance abaixo do valor atual, favor enviar um valor mais alto que: " + value);

					} else {
						adList.setValue(connectionSocket, result);
						replyAll("Lance dado! o valor agora está em: " + clientSentence, adList.usersList(connectionSocket));
					}

				} else {
					replyOne("Voce nao esta conectado em um leilão");
				}
			} catch (Exception e) {
				replyOne("Comando não encontrado, informe um valor para o leilão");
			}
		}

	}

	private void replyAll(String clientSentence, Ad ad) {
		Thread reply = new Thread(new ReplyAllThread(clientSentence, ad));
		reply.start();
		try {
			reply.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void replyOne(String clientSentence) {
		Thread reply = new Thread(new ReplyOneThread(connectionSocket, clientSentence));
		reply.start();
		try {
			reply.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String getMessage() {
		/*
		 * Aguarda o envio de uma mensagem do cliente. Esta mensagem deve ser terminada
		 * em \n ou \r Neste exemplo espera-se que a mensagem seja textual (string).
		 * Para ler dados não textuais tente a chamada read()
		 */
		String mess = null;
		try {
			mess = inFromClient.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mess;
	}

	private void instantiate() throws IOException {
		/* Cria uma stream de entrada para receber os dados do cliente */
		inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

	}

	private int wordCount(String string) {
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
