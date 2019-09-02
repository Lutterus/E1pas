package Servidor;

import java.net.Socket;
import java.util.ArrayList;

public class adList {
	private ArrayList<Ad> adList;
	private int num = 1;

	public adList() {
		adList = new ArrayList<Ad>();
		adList.add(new Ad("Leilao0", null));
	}

	public String addAuction(Socket connectionSocket) {
		String name = "leilao" + String.valueOf(num);
		num++;

		adList.add(new Ad(name, connectionSocket));

		return name;
	}

	public String getListNames() {
		String list = "";
		for (Ad ad : adList) {
			list = list + "'" + ad.getName() + "', ";
		}

		return list;
	}

	public boolean joinUser(String name, Socket user) {
		for (Ad ad : adList) {
			if (ad.getName().contentEquals(name)) {
				ad.addUser(user);
				return true;
			}
		}

		return false;
	}

	public Ad usersList(Socket connectionSocket) {
		for (Ad ad : adList) {
			for (Socket users : ad.getUsers()) {
				if (users == connectionSocket) {
					return ad;
				}
			}
		}
		return null;

	}

	public boolean isInServer(Socket connectionSocket) {
		for (Ad ad : adList) {
			for (Socket users : ad.getUsers()) {
				if (users == connectionSocket) {
					return true;
				}
			}
		}
		return false;
	}

	public void removeAuction(String name) {
		int index = 0;
		for (Ad ad : adList) {
			if (ad.getName() == name) {
				break;
			}
			index++;
		}

		for (Socket socket : adList.get(index).getUsers()) {
			Thread reply = new Thread(new ReplyOneThread(socket, "Este leilão foi encerrado"));
			reply.start();
		}
		
		adList.remove(index);
	}

	public boolean getTime(int time, String name) {
		for (Ad ad : adList) {
			if (ad.getName() == name) {
				if (time >= ad.getTime()) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	public int getValue(Socket connectionSocket) {
		for (Ad ad : adList) {
			for (Socket users : ad.getUsers()) {
				if (users == connectionSocket) {
					return ad.getValue();
				}
			}
		}
		return 0;
	}

	public void setValue(Socket connectionSocket, int result) {
		for (Ad ad : adList) {
			for (Socket users : ad.getUsers()) {
				if (users == connectionSocket) {
					ad.setValue(result);
				}
			}
		}

	}

	public boolean setName(Socket connectionSocket, String clientSentence) {
		for (Ad ad : adList) {
			for (Socket users : ad.getUsers()) {
				if (users == connectionSocket) {
					if (ad.getOwner() == connectionSocket) {
						ad.setName(clientSentence);
						return true;
					}

				}
			}
		}

		return false;

	}

	public boolean setDescrition(Socket connectionSocket, String clientSentence) {
		for (Ad ad : adList) {
			for (Socket users : ad.getUsers()) {
				if (users == connectionSocket) {
					if (ad.getOwner() == connectionSocket) {
						ad.setDescrition(clientSentence);
						return true;
					}

				}
			}
		}

		return false;
	}

	public void setNewValue(Socket connectionSocket, int result) {
		for (Ad ad : adList) {
			for (Socket users : ad.getUsers()) {
				if (users == connectionSocket) {
					if (ad.getOwner() == connectionSocket) {
						ad.setValue(result);
						break;
					}

				}
			}
		}

	}

	public boolean endAuction(Socket connectionSocket) {
		for (Ad ad : adList) {
			for (Socket users : ad.getUsers()) {
				if (users == connectionSocket) {
					if (ad.getOwner() == connectionSocket) {
						int value = ad.getValue();
						removeAuction(ad.getName());
						return true;
					}

				}
			}
		}
		return false;
	}

	public boolean setMoreTime(Socket connectionSocket, int result) {
		for (Ad ad : adList) {
			for (Socket users : ad.getUsers()) {
				if (users == connectionSocket) {
					if (ad.getOwner() == connectionSocket) {
						ad.setMoreTime(result);
						return true;
					}

				}
			}
		}
		return false;

	}

	public String getNameProduct(Socket connectionSocket) {
		for (Ad ad : adList) {
			for (Socket users : ad.getUsers()) {
				if (users == connectionSocket) {
					if(ad.getNameProduct()==null){
						return "Produto sem nome especificado";
					}else {
						return ad.getNameProduct();
					}
					
					
				}
			}
		}
		return "Produto sem nome especificado";
	}

	public String getDescription(Socket connectionSocket) {
		for (Ad ad : adList) {
			for (Socket users : ad.getUsers()) {
				if (users == connectionSocket) {
					if(ad.getDescription()==null){
						return "Produto sem descrição especificado";
					}else {
						return ad.getDescription();
					}
					
					
				}
			}
		}
		return "Produto sem descrição especificado";
	}

}
