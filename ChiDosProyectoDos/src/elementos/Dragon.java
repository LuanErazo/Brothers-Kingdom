package elementos;

import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import usuario.DatosGuardar;
import usuario.Jugador;

public class Dragon {

	private PApplet app;
	private PImage image;
	private PVector pos;
	private LinkedList<Personita> personasComidas;
	private DatosGuardar datos;
	private boolean turno;
	private int movpos;
	private int timerDragon;

	public Dragon(PApplet app) {
		this.app = app;
		personasComidas = new LinkedList<>();
		float x1 = Jugador.calculoGeneralX(5);
		float y1 = Jugador.calculoGeneralY();

		app.rect(x1 + 50, y1 + 50, 10, 10);
		pos = new PVector(x1, y1 - 71);

		movpos = 5;
		image = app.loadImage("../data/img/Dragoncito.png");
	}

	public void pintar() {
		app.image(image, pos.x, pos.y - 51);

		if (!turno) {
			timerDragon = 0;
		}

	}

	public void setTurno(boolean turno) {
		this.turno = turno;
	}

	public int getTimerDragon() {
		return timerDragon;
	}

	public void sumaTimerDragon() {
		this.timerDragon++;
	}

	public void setTimerDragon(int timerDragon) {
		this.timerDragon = timerDragon;
	}

	public boolean moverRan() {
		PVector ran[] = new PVector[2];
		
		PVector antesPos = pos.copy();
		float post = 25;
		antesPos.set(((pos.x)-Jugador.calculoPosXSolo())+post, pos.y);

		PVector postPos = pos.copy();
		postPos.set(((pos.x)+Jugador.calculoPosXSolo())-post, pos.y);
		ran[0] = antesPos;
		ran[1] = postPos;
		pos = ran[(int) app.random(2)];
		return false;
	}

	public void moverPersona(Personita p) {
		if (turno) {
			System.out.println("dragon siguio");
			PVector nPos = p.getPos().copy();
			pos = nPos;
		}

	}

	public void setMovpos(int movpos) {
		this.movpos = movpos;
	}
	
	public void setPos(PVector pos) {
		this.pos = pos;
	}

	public Personita comer(Personita p) {
		if (pos.equals(p.getPos())) {
			personasComidas.add(p);
		}
		return p;
	}

	public boolean isTurno() {
		return turno;
	}

	public PVector getPos() {
		return pos;
	}
}
