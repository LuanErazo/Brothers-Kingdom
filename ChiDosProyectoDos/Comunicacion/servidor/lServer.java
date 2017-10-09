package servidor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

public class lServer implements Observer, Runnable {

	private final int PORT = 5000;

	private ServerSocket socketServidor;

	private boolean conectado;
	private boolean moviendo;

	private ArrayList<Comunicacion> clientes = new ArrayList<>();

	public lServer() {

		try {
			socketServidor = new ServerSocket(PORT);
			conectado = true;
			System.out.println("[Servidor]: Atendiendo en " + InetAddress.getLocalHost().getHostAddress().toString()
					+ ":" + this.PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		moviendo = true;
	}

	@Override
	public void run() {
		while (conectado) {
			try {

				// Esperar a que un cliente se conecte
				Socket s = socketServidor.accept();

				Comunicacion com = new Comunicacion(s, clientes.size());

				// Agregar el gestor como observador
				com.addObserver(this);

				// Comenzar el hilo de ejecuciÃ³n del contrlador
				new Thread(com).start();

				// Agregar a la colecciÃ³n de clientes
				clientes.add(com);
				System.out.println("[Servidor] Tenemos: " + clientes.size() + " clientes");
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	@Override
	public void update(Observable o, Object msn) {
		Comunicacion controladorCliente = (Comunicacion) o;

		if (msn instanceof String) {
			String mensaje = (String) msn;

			System.out.println("SERVER_MENSAJE_LLEGADO: " + mensaje);

			if (mensaje.equalsIgnoreCase("cliente desconectado")) {
				clientes.remove(controladorCliente);
				System.out.println("[Servidor] Tenemos: " + clientes.size() + " clientes");
			}

			if (mensaje.contains("values")) {
				controladorCliente.enviarMensaje("id:" + clientes.size());
				reenviarMensaje("mas", controladorCliente);
			}
			
			if (mensaje.contains("cambioTurno")) {
				reenviarMensajeTodos(mensaje);
			}
		}

	}

	private void reenviarMensaje(String mensaje, Comunicacion remitente) {
		int reenvios = 0;
		for (Iterator<Comunicacion> iterator = clientes.iterator(); iterator.hasNext();) {
			Comunicacion com = (Comunicacion) iterator.next();
			if (!com.equals(remitente)) {
				com.enviarMensaje(mensaje);
				reenvios++;
			}

		}
		System.out.println("[Servidor] Se reenvia la nota a : " + reenvios + " clientes");
	}
	
	private void reenviarMensajeTodos(String mensaje) {
		int reenvios = 0;
//		for (Iterator<Comunicacion> iterator = clientes.iterator(); iterator.hasNext();) {
//			Comunicacion com = (Comunicacion) iterator.next();
//				com.enviarMensaje(mensaje);
//				reenvios++;
//
//		}
		for (int i = 0; i < clientes.size(); i++) {
			Comunicacion com = (Comunicacion) clientes.get(i);
			com.enviarMensaje(mensaje);
			reenvios++;
			
		}
		System.out.println("[Servidor] Se reenvia la nota a : " + reenvios + " clientes");
	}

	// ESTO NO SE USARÃ� :v
	private void rechazarCliente(Socket s) {

		try (ObjectOutputStream salida = new ObjectOutputStream(s.getOutputStream())) {
			String mensaje = "No se aceptan mÃ¡s clientes en el momento";
			salida.writeObject(mensaje);
			System.out.println("[Servidor] Se enviÃ³ el mensaje: " + mensaje);
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
