package usuario;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import cliente.ComunicacionCliente;
import elementos.Castillo;
import elementos.Dragon;
import elementos.Personita;
import processing.core.PApplet;
import processing.core.PConstants;
import usuario.Carga;
import usuario.FondoAnimations;
import usuario.Jugador;

public class Logica implements Observer {

	private static PApplet app;
	private static int time, cTime, clock, timing;
	private int tiempoIn;
	private LinkedList<Personita> personasSueltas;
	private Dragon dragon;
	private Jugador player;
	private FondoAnimations fondo;

	private ComunicacionCliente cliente;

	private Carga carga;
	private Castillo castillo;
	private boolean jugadorUno;

	public Logica(PApplet app, boolean jugadorUno, ComunicacionCliente cliente) {
		this.app = app;
		this.jugadorUno = jugadorUno;
		carga = new Carga(app);
		fondo = new FondoAnimations(app);
		this.cliente = cliente;

		int numJugador = 0;

		if (jugadorUno) {
			numJugador = 1;
		} else {
			numJugador = 2;
		}
		player = new Jugador(app, numJugador);

		this.cliente.addObserver(this);
		this.cliente.addObserver(player);

		personasSueltas = new LinkedList<>();
		dragon = new Dragon(app);

	}

	public void draw() {
		fondo.drawPintar(player);
		fondo.textos(player);

		app.imageMode(PConstants.CENTER);
		dragon.pintar();
		pintarObjects();

		player.pintar();

		for (Personita personita : personasSueltas) {
			personita.pintar();
		}
		
		
		fondo.flecha(player);

	}

	public void click(float mx, float my) {

		jugador(mx, my);
	}

	private void jugador(float mx, float my) {
		if (player.isTurnoPrincipal()) {

			player.click(mx, my);

			if (fondo.DerBoton(mx, my)) {
				castillo = player.getCastillo();
				Personita suelta = player.disparar();
				personasSueltas.addLast(suelta);

				cliente.enviarMensaje(
						"enviopos " + suelta.getName() + " " + suelta.getPos().x + " " + suelta.getPos().y);

			}

			if (fondo.DerBoton(mx, my) || fondo.IzqBoton(mx, my)) {
				cliente.enviarMensaje("cambioTurno");
				player.setTurnoPrincipal(false);
				player.setTurnoConciliacion(true);

			}

		}
	}

	private void dragon() {

	}

	@Override
	public void update(Observable o, Object arg) {
		String mensaje = (String) arg;

		if (mensaje.contains("activar")) {

		}
		if (mensaje.contains("dragon")) {
			dragon.setTurno(true);
		}

	}

	public static int timer() {

		if (app.millis() - cTime >= 1000) {
			cTime = app.millis();
			time++;
		}

		if (timing != time) {
			timing = time;
			clock++;
		}
		return clock;
	}

	private void cambioTurno() {

	}

	private void pintarObjects() {

		for (int i = 0; i < 13; i++) {
			float x1 = (float) (26 + (104.5 * i));
			float x2 = x1;
			float y1 = 394;
			float y2 = y1 + 85;

			app.rect(x1 + 50, y1 + 50, 10, 10);

			app.line(x1, y1, x2, y2);
		}

	}

}
