package elementos;

import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import usuario.Carga;
import usuario.DatosGuardar;
import usuario.Jugador;

public class Castillo {

	private PApplet app;
	private PImage image;
	private PVector pos;
	private LinkedList<Personita> personasVivas;
	private DatosGuardar datos;
	private String lanzandoA;
	private String texto[];
	
	private PVector prepos;

	public Castillo(PApplet app, int player) {
		this.app = app;
		personasVivas = new LinkedList<>();
		if (player == 1) {
			texto = Carga.textoPjOne;
			pos = new PVector(50, app.height / 2);
			for (int i = 0; i < texto.length; i++) {
				personasVivas.add(new Personita(app, texto[i], player, pos));
			}
		} else {
			texto = Carga.textoPjTwo;
			pos = new PVector(app.width - 50, app.height / 2);

			for (int i = 0; i < texto.length; i++) {
				personasVivas.add(new Personita(app, texto[i], player, pos));

			}
		}

		prepos = pos.copy();
	}

	public void pintar() {
		// app.rectMode(PConstants.CENTER);
		// app.rect(pos.x, pos.y, 100, 100);

		for (Personita personita : personasVivas) {
			personita.pintar();
		}
	}

	public Personita recibir(Personita p) {
		p.setPos(prepos);
		personasVivas.addLast(p);
		return p;
	}

	public LinkedList<Personita> getPersonasVivas() {
		return personasVivas;
	}

	public Personita disparar(PVector pos) {
		personasVivas.getFirst().setNumPosGen(Jugador.posicionTabla());
		Personita p = personasVivas.getFirst();
		personasVivas.removeFirst();

	
		p.setPos(pos);
		return p;
	}
	
	public String nombrePersonaEnviada(){
		return personasVivas.getFirst().getName();
	}
	
	public String relacionPersonaEnviada(){
		return personasVivas.getFirst().getRelacion();
	}

	public Personita comer(Personita p) {
		return null;
	}

}
