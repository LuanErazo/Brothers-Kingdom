package usuario;

import java.time.temporal.JulianFields;

import elementos.Castillo;
import elementos.Dragon;
import elementos.Personita;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

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

	private PImage pGanar;
	private PImage pInicio;
	private PImage pPerder;

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
	private float posYnTres = 50;

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

		pInicio = Carga.pInicio;
		pGanar = Carga.pGanar;
		pPerder = Carga.pPerder;

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

	public void pantallaFinal(int turnos, Jugador player, Jugador playerSec, Dragon dragon) {
		app.pushStyle();
		app.imageMode(PConstants.CORNER);
		if (turnos > 5) {
			if (dragon.getPersonasComidas().size() == 0) {
				app.image(pPerder, 0, 0);
			} else {
				int playerInt = 0;
				int playerSecInt = 0;

				for (int i = 0; i < dragon.getPersonasComidas().size(); i++) {
					Personita p = dragon.getPersonasComidas().get(i);
					if (p.getJugador() == 1) {
						playerInt++;
					}
					if (p.getJugador() == 2) {
						playerSecInt++;
					}
				}
				if (playerInt == playerSecInt) {
					app.image(pGanar, 0, 0);
				} else if (playerInt > playerSecInt) {
					app.image(pGanar, 0, 0);

				} else if(playerInt < playerSecInt){
					app.image(pPerder, 0, 0);
				}

			}
			app.popStyle();
		}

		PVector finalPos = new PVector();
		PVector finalPosSec = new PVector();

		if (player.getJugador() == 1) {
			finalPos.set(Jugador.calculoGeneralX(0), Jugador.calculoGeneralY());
			finalPosSec.set(Jugador.calculoGeneralX(10), Jugador.calculoGeneralY());

		}
		if (player.getJugador() == 1) {
			finalPos = new PVector(Jugador.calculoGeneralX(10), Jugador.calculoGeneralY());
			finalPosSec = new PVector(Jugador.calculoGeneralX(0), Jugador.calculoGeneralY());

		}
		if (dragon.getPos().equals(finalPos)) {
			app.image(pPerder, 0, 0);
		}
		if (dragon.getPos().equals(finalPosSec)) {
			app.image(pGanar, 0, 0);
		}
		app.image(pInicio, 0, 0);
		
	}

	private void textoLocal(Jugador player, int turnos) {
		Castillo c = player.getCastillo();

		String isTurnoPUno = "Turno Jugador uno";
		String isTurnoPDos = "Turno Jugador dos";
		String isTuTurno = "Te toca escojer, disparas o pasas";
		String turnoLocal = "";
		String turnoOtro = "";
		String nameRelated = "enviando a ";

		nameRelated = nameRelated.concat(player.getCastillo().nombrePersonaEnviada() + ", ");
		nameRelated = nameRelated.concat(player.getCastillo().relacionPersonaEnviada());
		int posX = 0;
		int posY = 0;

		if (player.getJugador() == 1) {
			chat = Carga.chatBubbleUno;
			posX = 200;
			posY = 300;
			turnoLocal = isTuTurno;
			turnoOtro = isTurnoPDos;

		}
		if (player.getJugador() == 2) {
			chat = Carga.chatBubbleDos;
			posX = 1000;
			posY = 300;
			turnoLocal = isTuTurno;
			turnoOtro = isTurnoPUno;

		}

		if (player.isTurnoPrincipal()) {
			app.image(chat, posX, posY);
			app.pushStyle();
			app.textSize(15);
			app.fill(0);
			app.text(nameRelated, posX, posY);
			app.popStyle();
			app.text(turnoLocal, app.width / 2, (app.height / 2) - 200);
		} else if (!player.isTurnoPrincipal() && !player.isTurnoConciliacion()) {
			app.text(turnoOtro, app.width / 2, (app.height / 2) - 200);
		}

		float y = 200;
		float yy = y - 20;

		app.pushStyle();
		app.textSize(15);
		app.text("ciudadanos", c.getPos().x, c.getPos().y + y);
		app.text("turno", app.width / 2, (app.height / 2) + 230);
		app.popStyle();

		app.text(c.getPersonasVivas().size(), c.getPos().x, c.getPos().y + yy);

		app.pushStyle();
		app.textSize(100);
		app.text(turnos, app.width / 2, (app.height / 2) + 210);
		app.popStyle();

	}

	private void textoConexion(Jugador playerSec) {
		Castillo c = playerSec.getCastillo();

		String turnoLocal = "";
		String turnoOtro = "";
		String nameRelated = "enviando a ";

		nameRelated = nameRelated.concat(playerSec.getCastillo().nombrePersonaEnviada() + ", ");
		nameRelated = nameRelated.concat(playerSec.getCastillo().relacionPersonaEnviada());

		int posXOtrin = 0;
		int posYOtrin = 0;

		if (playerSec.getJugador() == 1) {
			chat = Carga.chatBubbleUno;
			posXOtrin = 200;
			posYOtrin = 300;

		}
		if (playerSec.getJugador() == 2) {
			chat = Carga.chatBubbleDos;
			posXOtrin = 1000;
			posYOtrin = 300;
		}

		app.fill(255);
		app.textAlign(PConstants.CENTER);
		app.textSize(60);

		if (playerSec.isTurnoPrincipal()) {
			app.image(chat, posXOtrin, posYOtrin);
			app.pushStyle();
			app.textSize(15);
			app.fill(0);
			app.text(nameRelated, posXOtrin, posYOtrin);
			app.popStyle();
			app.text(turnoLocal, app.width / 2, (app.height / 2) - 200);
		} else if (!playerSec.isTurnoPrincipal() && !playerSec.isTurnoConciliacion()) {
			app.text(turnoOtro, app.width / 2, (app.height / 2) - 200);
		}

		if (playerSec.isTurnoConciliacion()) {
			app.text("Take a decition", app.width / 2, (app.height / 2) - 200);

		}
		float y = 200;
		float yy = y - 20;

		app.pushStyle();
		app.textSize(15);
		app.text("ciudadanos", c.getPos().x, c.getPos().y + y);
		app.popStyle();

		app.text(c.getPersonasVivas().size(), c.getPos().x, c.getPos().y + yy);

	}

	public void textos(Jugador player, Jugador playerSec, int turnos) {
		textoLocal(player, turnos);
		textoConexion(playerSec);
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
