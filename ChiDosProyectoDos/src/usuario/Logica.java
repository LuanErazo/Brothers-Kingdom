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
import processing.core.PVector;
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

	private int minVal;
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
			minVal = 0;
		} else {
			numJugador = 2;
			minVal = 10;
		}

		player = new Jugador(app, numJugador);

		this.cliente.addObserver(this);
		this.cliente.addObserver(player);

		personasSueltas = new LinkedList<>();
		dragon = new Dragon(app);

	}

	public void draw() {
		timer();
		key();
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
		if (dragon.isTurno()) {
			dragon();
		}
	}

	public void click(float mx, float my) {
		if (!dragon.isTurno()) {
			jugador(mx, my);
		}

	}

	private void jugador(float mx, float my) {
		castillo = player.getCastillo();

		if (player.isTurnoPrincipal()) {
			player.click(mx, my);

			if (fondo.DerBoton(mx, my)) {
				PVector pPos = player.getPosLanzamiento();
				PVector pUno = Jugador.vectorCalculo(0);
				PVector pDos = Jugador.vectorCalculo(10);

				if (pPos.equals(dragon.getPos()) == false && pPos.equals(pUno) == false && pPos.equals(pDos) == false) {

					if (jugadorUno) {

						if (player.getPosLanzamiento().x > Jugador.calculoGeneralX(5)) {
							Personita suelta = player.disparar();
							personasSueltas.addLast(suelta);

							cliente.enviarMensaje(
									"enviopos " + suelta.getName() + " " + suelta.getPos().x + " " + suelta.getPos().y);
						} else {
							System.out.println("no puedes disparar aqui Valor Minimo");

						}

					} else {

						if (player.getPosLanzamiento().x < Jugador.calculoGeneralX(5)) {
							Personita suelta = player.disparar();
							personasSueltas.addLast(suelta);
							cliente.enviarMensaje(
									"enviopos " + suelta.getName() + " " + suelta.getPos().x + " " + suelta.getPos().y);

						} else {
							System.out.println("no puedes disparar aqui Valor Minimo");

						}
					}
				} else {
					System.out.println("no puedes disparar aqui Dragon");
				}
			}

			if (fondo.DerBoton(mx, my) || fondo.IzqBoton(mx, my)) {
				cliente.enviarMensaje("cambioTurno");
				// player.setTurnoPrincipal(false);
				// player.setTurnoConciliacion(true);

			}

		}

		if (player.isTurnoConciliacion()) {

			if (fondo.IzqBoton(mx, my)) {
				if (personasSueltas.size() > 0) {
					Personita pp = perMinimDist(Jugador.calculoGeneralX(minVal), Jugador.calculoGeneralY());
					if (player.getJugador() != pp.getJugador()) {
						Personita p = castillo.recibir(pp);
						personasSueltas.remove(p);
					}
				}
			}

		}
	}

	private Personita perMinimDist(float Xevalua, float Yevalua) {
		float distancias[] = new float[personasSueltas.size()];

		for (int i = 0; i < personasSueltas.size(); i++) {
			Personita p = personasSueltas.get(i);
			float dist = PApplet.dist(p.getPos().x, p.getPos().y, Xevalua, Yevalua);
			distancias[i] = dist;
		}

		for (int i = 0; i < personasSueltas.size(); i++) {
			Personita p = personasSueltas.get(i);
			float dist = PApplet.dist(p.getPos().x, p.getPos().y, Xevalua, Yevalua);

			float min = PApplet.min(distancias);
			System.out.println(min);
			if (min == dist) {
				return p;
			}

		}
		return null;
	}

	private void dragon() {
		if (personasSueltas.size() > 0) {

			if (dragon.getTimerDragon() == 1) {
				System.out.println("dragonturn");
				Personita p = perMinimDist(dragon.getPos().x, dragon.getPos().y);
				dragon.moverPersona(p);
				personasSueltas.remove(dragon.comer(p));
				dragon.setTimerDragon(0);
			}
		}

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

	public int timer() {

		if (app.millis() - cTime >= 1000) {
			cTime = app.millis();
			time++;
		}

		if (timing != time) {
			dragon.sumaTimerDragon();
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

	private void key() {
		if (app.keyPressed) {
			if (app.key == '1') {
				player.setTurnoPrincipal(false);
				player.setTurnoConciliacion(true);
				dragon.setTurno(false);

			}
			if (app.key == '2') {
				player.setTurnoPrincipal(true);
				player.setTurnoConciliacion(false);
				dragon.setTurno(false);

			}
			if (app.key == '3') {
				player.setTurnoPrincipal(false);
				player.setTurnoConciliacion(false);
				dragon.setTurno(true);

			}

		}
	}

}
