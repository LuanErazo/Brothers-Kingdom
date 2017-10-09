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
    
    public Dragon(PApplet app) {
    	this.app = app;
    		float x1 = Jugador.calculoGeneralX(5);
    		float y1 = Jugador.calculoGeneralY();
    		
    		app.rect(x1+50, y1+50, 10, 10);	    				
    		pos = new PVector(x1, y1-71);
		
    	image = app.loadImage("../data/img/Dragoncito.png");
	}
    
    public void pintar(){
    	app.image(image, pos.x, pos.y);
    	
    }
    
    public void setTurno(boolean turno) {
		this.turno = turno;
	}
    
    
    
    public void mover(PVector nPos){
    	pos = nPos;
    	
    }
    public Personita comer(Personita p){
    	
    	return p;
    }
    
    public boolean isTurno() {
		return turno;
	}
    
    public PVector getPos() {
		return pos;
	}
}
