package elementos;

import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import usuario.Carga;

public class Personita {

	private PApplet app;
	private PVector pos;
	private PImage image;
	private String name;
	private String relacion;
	private int jugador;
	private int numPosGen;

	public Personita(PApplet app, String info, int jugador, PVector pos) {
		this.jugador = jugador;
		this.app = app;
		this.pos = pos;

		String infor[] = info.split(",");

		name = infor[0];
		relacion = infor[1];

		PImage hombre[] = new PImage[3];
		PImage mujer[] = new PImage[3];

		hombre[0] = Carga.hombreOne;
		hombre[1] = Carga.hombreTwo;
		hombre[2] = Carga.hombreThree;

		mujer[0] = Carga.mujerOne;
		mujer[1] = Carga.mujerTwo;
		mujer[2] = Carga.mujerThree;

		if (infor[2].contains("mujer")) {
			image = mujer[(int) Math.random() * 3];
		} else {
			image = hombre[(int) Math.random() * 3];
		}
		numPosGen = 2;

	}

	public Personita(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Personita other = (Personita) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public void setPos(PVector pos) {
		this.pos = pos;
	}

	public PVector getPos() {
		return pos;
	}

	public void pintarPos() {
		System.out.println(pos.x + " : " + pos.y);
	}

	public String getName() {
		return name;
	}

	public String getRelacion() {
		return relacion;
	}

	public void setNumPosGen(int numPosGen) {
		this.numPosGen = numPosGen;
	}

	public int getNumPosGen() {
		return numPosGen;
	}

	public void pintar() {

		app.image(image, pos.x, pos.y);

	}

	public int getJugador() {
		return jugador;
	}

}
