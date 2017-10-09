package usuario;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public class FondoAnimations {

	private PApplet app;
	private PImage fondo;
	private PImage nubeUno;
	private PImage nubeDos;
	private PImage nubeTres;
	private PImage passButton;
	private PImage shotButton;
	private PImage letIn;
	private PImage letOut;
	private PImage flecha;

	private float x = 340;
	private float y = 600;
	// variables de posicion de la nube Uno
	private float posXnUno = 0;
	private float posYnUno = 20;
	// variables de posicion de la nube Dos
	private float posXnDos = 200;
	private float posYnDos = 100;
	// variables de posicion de la nube Tres
	private float posXnTres = 0;
	private float posYnTres = 0;

	private PImage chat;

	public FondoAnimations(PApplet app) {
		this.app = app;
		fondo = app.loadImage("../data/img/pantallas/Fondo.png");
		nubeUno = app.loadImage("../data/img/nubes/NubeUno.png");
		nubeDos = app.loadImage("../data/img/nubes/NubeDos.png");
		nubeTres = app.loadImage("../data/img/nubes/NubeTres.png");
		passButton = app.loadImage("../data/img/botones/PassButton.png");
		shotButton = app.loadImage("../data/img/botones/ShotButton.png");
		flecha = app.loadImage("../data/img/flecha.png");
		letIn = app.loadImage("../data/img/botones/letThemIn.png");
		letOut = app.loadImage("../data/img/botones/leftOut.png");

	}

	public void drawPintar(Jugador player) {
		app.imageMode(PConstants.CORNER);
		app.image(fondo, 0, 0);
		// animacion de la nube uno

		posXnUno += 1;

		if (posXnUno >= 1270) {
			posXnUno = -330;
		}

		// animacion de la nube dos

		posXnDos += 1.5;
		if (posXnDos >= 1270) {
			posXnDos = -330;
		}

		// animacion de la nube tres

		posXnTres += 2.5;
		if (posXnTres <= 1270) {
			posXnTres = -330;
		}

		app.image(nubeUno, posXnUno, posYnUno);
		app.image(nubeDos, posXnDos, posYnDos);
		app.image(nubeTres, posXnTres, posYnTres);

		app.imageMode(PConstants.CENTER);

		if (player.isTurnoPrincipal()) {
			app.image(passButton, x, y);
			app.image(shotButton, x + 550, y);
		}

		if (player.isTurnoConciliacion()) {
			app.image(letIn, x, y);
			app.image(letOut, x + 550, y);

		}

	}

	public void textos(Jugador player) {
		String isTurnoPUno = "Turno Jugador uno";
		String isTurnoPDos = "Turno Jugador dos";
		String turnoLocal = "";
		String turnoOtro = "";
		String nameRelated = "enviando a ";
		app.textSize(15);
		app.fill(0);

		nameRelated = nameRelated.concat(player.getCastillo().nombrePersonaEnviada() + ", ");
		nameRelated = nameRelated.concat(player.getCastillo().relacionPersonaEnviada());

		if (player.getJugador() == 1) {
			chat = Carga.chatBubbleUno;
			app.image(chat, 200, 300);
			turnoLocal = isTurnoPUno;
			turnoOtro = isTurnoPDos;
			app.text(nameRelated, 200, 300);

		}
		if (player.getJugador() == 2) {
			chat = Carga.chatBubbleDos;

			turnoLocal = isTurnoPDos;
			turnoOtro = isTurnoPUno;
			app.image(chat, 1000, 300);
			app.text(nameRelated, 800, 300);

		}
		app.fill(255);
		app.textAlign(PConstants.CENTER);
		app.textSize(60);

		if (player.isTurnoPrincipal()) {
			app.text(turnoLocal, app.width / 2, (app.height / 2) - 200);
		} else if (!player.isTurnoPrincipal() && !player.isTurnoConciliacion()) {
			app.text(turnoOtro, app.width / 2, (app.height / 2) - 200);
		}

		if (player.isTurnoConciliacion()) {
			app.text("Take a decision", app.width / 2, (app.height / 2) - 200);

		}

	}

	public void flecha(Jugador player) {
		if (player.isTurnoPrincipal()) {
			app.image(flecha, Jugador.calculoGeneralX(), Jugador.calculoGeneralY() - 100);
		}
	}

	public boolean IzqBoton(float mx, float my) {
		float distOne = PApplet.dist(x - 65, y, mx, my);
		float distTwo = PApplet.dist(x + 65, y, mx, my);

		if (mx > 274 && my > 554 & mx < 405 && my < 647) {
			return true;
		}
		if (distOne < 275 - 237 || distTwo < 275 - 237) {
			return true;
		}
		return false;
	}

	public boolean DerBoton(float mx, float my) {
		float distOne = PApplet.dist((x + 550) - 65, y, mx, my);
		float distTwo = PApplet.dist((x + 550) + 65, y, mx, my);

		if (mx > 822 && my > 554 & mx < 957 && my < 647) {
			return true;
		}
		if (distOne < 275 - 237 || distTwo < 275 - 237) {
			return true;
		}
		return false;
	}

}
