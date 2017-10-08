package usuario;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import elementos.Castillo;
import elementos.Personita;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Jugador implements Observer {

	private PApplet app;
	private int jugador;
	private Castillo castillo;

	private boolean turnoPrincipal;
	private boolean turnoConciliacion;

	private PVector posLanzamiento;
	private PVector posLanzamientoLlegada;

	private Personita suelta;
	private String mensajeJugador;

	private static float mouseX;
	private static float mouseY;

	public Jugador(PApplet app, int jugador) {
		this.app = app;
		posLanzamiento = new PVector();
		posLanzamientoLlegada = new PVector();
		this.jugador = jugador;

		castillo = new Castillo(app, jugador);

		if (jugador == 1) {
			turnoPrincipal = true;
		} else {
			turnoPrincipal = false;
		}

	}

	public void pintar() {
		castillo.pintar();

	}

	public void click(float mx, float my) {

		if (turnoPrincipal) {
			mouseX = mx;
			mouseY = my;

			for (int i = 0; i < 12; i++) {
				float x1 = calculoPosX(i);
				float x2 = x1;
				float y1 = 394;
				float y2 = y1 + 85;

				if (mx > x1 && my > y1 && mx < x2 + 100 && my < y2) {
					posLanzamiento.set(x1 + 51, y1 - 30);
				}

			}

		}

	}
	
	

	private static float calculoPosX(int i) {
		float x1 = (26 + (104.5f * i));
		return x1;
	}

	public static float calculoGeneralX() {
		int i = posicionTabla();
		return calculoPosX(i)+50;

	}
	
	public static float calculoGeneralY() {
		return 394+((394+85)-394)/2;

	}

	private static int posicionTabla() {
		int ret = 0;
		for (int i = 0; i < 12; i++) {
			float x1 = calculoPosX(i);
			float x2 = x1;
			float y1 = 394;
			float y2 = y1 + 85;

			if (mouseX > x1 && mouseY > y1 && mouseX < x2 + 100 && mouseY < y2) {
				ret = i;
			}
		}
		return ret;
	}

	@Override
	public void update(Observable o, Object arg) {
		String mensaje = (String) arg;

	}

	public Personita cambiosCastillo() {
		PVector posN = posLanzamientoLlegada.copy();
		Personita p = suelta;
		return castillo.dispararLLegada(p, posN);
	}

	public Personita disparar() {
		PVector posN = new PVector(posLanzamiento.x, posLanzamiento.y);
		return castillo.disparar(posN);
	}

	public void setSuelta(Personita suelta) {
		this.suelta = suelta;
	}

	public Personita getSuelta() {
		return suelta;
	}

	public Castillo getCastillo() {
		return castillo;
	}

	public PVector getPos() {
		return posLanzamiento;
	}

	public PVector getPosLanzamientoLlegada() {
		return posLanzamientoLlegada;
	}

	public int getJugador() {
		return jugador;
	}

	public boolean isTurnoPrincipal() {
		return turnoPrincipal;
	}

	public void setTurnoPrincipal(boolean turno) {
		this.turnoPrincipal = turno;
	}

	public boolean isTurnoConciliacion() {
		return turnoConciliacion;
	}

	public void setTurnoConciliacion(boolean turnoConciliacion) {
		this.turnoConciliacion = turnoConciliacion;
	}

}
