package usuario;

import processing.core.PApplet;
import processing.core.PImage;

public class Carga {
	private PApplet app;
	public static PImage mujerOne;
	public static PImage mujerTwo;
	public static PImage mujerThree;
	public static PImage hombreOne;
	public static PImage hombreTwo;
	public static PImage hombreThree;
	public static String textoPjOne[];
	public static String textoPjTwo[];
	
	public static PImage chatBubbleUno;
	public static PImage chatBubbleDos;
	
	
	public static PImage pGanar; 
	public static PImage pPerder;
	public static PImage pInicio;


	public Carga(PApplet app) {
		this.app = app;

		mujerOne = app.loadImage("../data/img/pj/PersonFive.png");
		mujerTwo = app.loadImage("../data/img/pj/PersonFour.png");
		mujerThree = app.loadImage("../data/img/pj/PersonTwo.png");
		hombreOne = app.loadImage("../data/img/pj/PersonOne.png");
		hombreTwo = app.loadImage("../data/img/pj/PersonSix.png");
		hombreThree = app.loadImage("../data/img/pj/PersonThree.png");
		chatBubbleUno = app.loadImage("../data/img/ChatBubbleLeft.png");
		chatBubbleDos = app.loadImage("../data/img/ChatBubbleRight.png");
		
		pGanar = app.loadImage("../data/img/pantallas/youWin.png");
		pPerder = app.loadImage("../data/img/pantallas/youLose.png");
		pInicio = app.loadImage("../data/img/pantallas/StartScreen.png");

		textoPjOne = app.loadStrings("../data/nombres jugador uno");
		textoPjTwo = app.loadStrings("../data/nombres jugador dos");
	}

}
