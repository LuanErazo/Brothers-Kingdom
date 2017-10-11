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

	// Cosas de control
	private int contadorConciliaciones;
	private Boolean conciliacion[] = new Boolean[2];
	private Integer playerNumCon[] = new Integer[2];

	private ArrayList<Comunicacion> clientes = new ArrayList<>();

	public lServer() {
		conciliacion[0] = null;
		conciliacion[1] = null;

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

			if (mensaje.equals("cambioTurno")) {
				reenviarMensaje(mensaje, controladorCliente);
			}
			if (mensaje.equals("cambioTurnoDragon")) {
				reenviarMensajeTodos(mensaje);
			}
			if (mensaje.equals("conciliacion")) {
				reenviarMensajeTodos(mensaje);
			}
			if (mensaje.contains("enviopos")) {
				reenviarMensaje(mensaje, controladorCliente);
			}
			if (mensaje.contains("dragon")) {
				reenviarMensajeTodos(mensaje);
			}

			if (mensaje.contains("ConciliacionHecha:")) {
				contadorConciliaciones++;
				String data[] = mensaje.split(":");

				if (conciliacion[0] == null) {
					conciliacion[0] = new Boolean(data[1]);
					playerNumCon[0] = new Integer(Integer.parseInt(data[2]));
				} else {
					conciliacion[1] = new Boolean(data[1]);
					playerNumCon[1] = new Integer(Integer.parseInt(data[2]));

				}
				String m = "ConciliacionHecha:";
				String c = "ConciliacionUpdate:";

				if (contadorConciliaciones == 2) {
					for (int i = 0; i < playerNumCon.length; i++) {

						if (playerNumCon[i].intValue() == 1) {
							reenviarMensaje(m + conciliacion[i], clientes.get(1));
						} else {
							reenviarMensaje(c + conciliacion[i], clientes.get(1));

						}
						if (playerNumCon[i].intValue() == 2) {
							reenviarMensaje(m + conciliacion[i], clientes.get(0));
						} else {
							reenviarMensaje(c + conciliacion[i], clientes.get(0));
						}

					}
					conciliacion[0] = null;
					playerNumCon[0] = null;
					conciliacion[1] = null;
					playerNumCon[1] = null;
					contadorConciliaciones = 0;
				}

			}

			if (mensaje.contains("posDragon")) {
				reenviarMensaje(mensaje, controladorCliente);

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
		for (Iterator<Comunicacion> iterator = clientes.iterator(); iterator.hasNext();) {
			Comunicacion com = (Comunicacion) iterator.next();
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
