package usuario;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.text.FlowView.FlowStrategy;

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
	private Jugador playerSec;

	private FondoAnimations fondo;

	private int minVal;
	private ComunicacionCliente cliente;

	private Carga carga;
	private Castillo castillo;
	private boolean jugadorUno;
	private int turnos;

	public Logica(PApplet app, boolean jugadorUno) {
		cliente = new ComunicacionCliente();
		new Thread(cliente).start();
		cliente.addObserver(this);

		this.app = app;
		this.jugadorUno = jugadorUno;
		carga = new Carga(app);
		fondo = new FondoAnimations(app);

		int numJugador = 0;
		int numJugadorSec = 0;

		if (jugadorUno) {
			numJugador = 1;
			numJugadorSec = 2;
			minVal = 0;
		} else {
			numJugador = 2;
			numJugadorSec = 1;
			minVal = 10;
		}

		player = new Jugador(app, numJugador);
		playerSec = new Jugador(app, numJugadorSec);

		this.cliente.addObserver(player);

		personasSueltas = new LinkedList<>();
		dragon = new Dragon(app);
		turnos = 1;

	}

	public void draw() {
		timer();
		key();
		playerSec.setTurnoPrincipal(!player.isTurnoPrincipal());
		fondo.drawPintar(player);
		fondo.textos(player, playerSec, turnos);
		app.imageMode(PConstants.CENTER);
		dragon.pintar();
		pintarObjects();

		// player.pintar();
		// playerSec.pintar();

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

			Personita por = null;
			if (fondo.DerBoton(mx, my)) {
				PVector pPos = player.getPosLanzamiento();
				PVector pUno = Jugador.vectorCalculo(0);
				PVector pDos = Jugador.vectorCalculo(10);

				if (pPos.equals(dragon.getPos()) == false && pPos.equals(pUno) == false && pPos.equals(pDos) == false) {

					if (jugadorUno) {

						if (player.getPosLanzamiento().x > Jugador.calculoGeneralX(5)) {
							Personita suelta = player.disparar();
							por = suelta;
							personasSueltas.addLast(suelta);
							System.out.println("se crearon " + personasSueltas.size());
							cliente.enviarMensaje(
									"enviopos " + suelta.getName() + " " + suelta.getPos().x + " " + suelta.getPos().y);
						} else {
							System.out.println("no puedes disparar aqui Valor Minimo");

						}

					} else {

						if (player.getPosLanzamiento().x < Jugador.calculoGeneralX(5)) {
							Personita suelta = player.disparar();
							por = suelta;
							personasSueltas.addLast(suelta);
							System.out.println("se crearon " + personasSueltas.size());

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

			if (fondo.DerBoton(mx, my)) {
				if (por != null) {
					cliente.enviarMensaje("cambioTurno");
					player.setTurnoPrincipal(false);
					player.setTurnoConciliacion(false);
				}

			}
			if (fondo.IzqBoton(mx, my)) {
				cliente.enviarMensaje("cambioTurno");
				player.setTurnoPrincipal(false);
				player.setTurnoConciliacion(false);

			}

		}

		if (player.isTurnoConciliacion()) {

			if (fondo.IzqBoton(mx, my)) {
			}

			if (fondo.DerBoton(mx, my) || fondo.IzqBoton(mx, my)) {
				if (fondo.DerBoton(mx, my)) {
					cliente.enviarMensaje("ConciliacionHecha:false:" + player.getJugador());

				} else {
					cliente.enviarMensaje("ConciliacionHecha:true:" + player.getJugador());
				}
				player.setTurnoPrincipal(false);
				player.setTurnoConciliacion(false);

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
			if (min == dist) {
				return p;
			}

		}
		return null;
	}

	private void dragon() {
		if (!jugadorUno) {
			if (personasSueltas.size() > 0) {
				System.out.println("personas suelstas tam: " + personasSueltas.size());
				if (dragon.getTimerDragon() == 1) {
					Personita p = perMinimDist(dragon.getPos().x, dragon.getPos().y);
					dragon.moverPersona(p);
					personasSueltas.remove(dragon.comer(p));
					cliente.enviarMensaje("posDragon " + dragon.getPos().x + " " + dragon.getPos().y);
					app.delay(500);
					dragon.setTimerDragon(0);
				}
			} else { // no hay personas dentro del arreglo general
				dragon.setTurno(dragon.moverRan());
				cliente.enviarMensaje("posDragon " + dragon.getPos().x + " " + dragon.getPos().y);
				cliente.enviarMensaje("cambioTurnoDragon");
			}

		}

	}

	@Override
	public void update(Observable o, Object arg) {
		String mensaje = (String) arg;
		if (mensaje.contains("ConciliacionHecha:")) {
			String data[] = mensaje.split(":");
			boolean bol = Boolean.parseBoolean(data[1]);

			if (bol) {
				if (personasSueltas.size() > 0) {
					Personita pp = perMinimDist(Jugador.calculoGeneralX(minVal), Jugador.calculoGeneralY());
					if (player.getJugador() != pp.getJugador()) {
						Personita p = castillo.recibir(pp);
						personasSueltas.remove(p);
					}
				}
			}
			if (!jugadorUno) {
				cliente.enviarMensaje("dragon");
			}

		}

		if (mensaje.contains("ConciliacionUpdate:")) {
			// System.out.println("CONCILIACION UPDATE");
			String data[] = mensaje.split(":");
			boolean bol = Boolean.parseBoolean(data[1]);

			if (bol) {

				int minvalin = 10;

				if (!jugadorUno) {
					minvalin = 0;
				}
				if (personasSueltas.size() > 0) {
					Personita pp = perMinimDist(Jugador.calculoGeneralX(minvalin), Jugador.calculoGeneralY());
					System.out.println(playerSec.getJugador() + "comparacion con " + pp.getJugador());
					if (playerSec.getJugador() != pp.getJugador()) {
						Personita p = playerSec.getCastillo().recibir(pp);
						personasSueltas.remove(p);
					}
				}
			}
		}

		if (mensaje.contains("posDragon")) {
			String data[] = mensaje.split(" ");
			if (jugadorUno && dragon.isTurno()) {
				dragon.setPos(new PVector(Float.parseFloat(data[1]), Float.parseFloat(data[2])));
				if (personasSueltas.size() > 0) {
					for (int i = 0; i < personasSueltas.size(); i++) {
						Personita p = personasSueltas.get(i);
						if (dragon.getPos().equals(p.getPos())) {
							personasSueltas.remove(dragon.comer(p));
						}
					}
				}
			}

		}

		if (mensaje.equals("dragon")) {
			dragon.setTurno(true);

		}
		if (mensaje.equals("cambioTurno")) {
			if (jugadorUno) {
				cliente.enviarMensaje("conciliacion");
			} else {
				player.setTurnoPrincipal(true);

			}
		}
		if (mensaje.equals("conciliacion")) {
			player.setTurnoConciliacion(true);
		}

		if (mensaje.equals("cambioTurnoDragon")) {
			if (jugadorUno) {
				player.setTurnoPrincipal(true);
			}
			dragon.setTurno(false);
			turnos++;
		}

		if (mensaje.contains("enviopos")) {
			String datos[] = mensaje.split(" ");
			Personita otro = new Personita(datos[1]);
			for (int i = 0; i < playerSec.getCastillo().getPersonasVivas().size(); i++) {
				Personita p = playerSec.getCastillo().getPersonasVivas().get(i);
				if (p.equals(otro) && p.getJugador() != player.getJugador()) {
					System.out.println("son iguales");
					PVector pos = new PVector(Float.parseFloat(datos[2]), Float.parseFloat(datos[3]));
					p.setPos(pos);
					personasSueltas.add(p);
					playerSec.getCastillo().getPersonasVivas().remove(p);
				}
			}
			// "enviopos " + suelta.getName() + " " + suelta.getPos().x + " " +
			// suelta.getPos().y);

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
			if (app.key == '9') {
				System.out.println("existen estas personas " + personasSueltas.size());
				for (Personita personita : personasSueltas) {
					System.out.println("se ecuentran personas del castilo " + personita.getJugador());
				}
			}
			dragon.key();
		}
	}

}
