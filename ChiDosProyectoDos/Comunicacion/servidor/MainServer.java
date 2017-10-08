package servidor;

public class MainServer {

	private static lServer logica;

	public static void main(String[] args) {
		logica = new lServer();
		new Thread(logica).start();
	}

}
