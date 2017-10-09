package Mains;

import cliente.ComunicacionCliente;
import processing.core.PApplet;
import usuario.Logica;

public class MainOne extends PApplet{
	
	private String texto;

	public static void main(String[] args) {
		main("Mains.MainOne");
	}
	
	private static PApplet app;
	private ComunicacionCliente cliente;

	private Logica loggi;
	
	@Override
	public void settings() {
		size(1200,700);
	}
	
	@Override
	public void setup() {
		app = this;
		
//		log = new LogicaOne();
		loggi = new Logica(this, true);
	}
	
	@Override
	public void draw() {
		background(255);
//		log.draw();
		loggi.draw();
	}
	
	@Override
	public void mouseClicked() {
//		log.click(mouseX, mouseY);
		loggi.click(mouseX, mouseY);
	}

	public static PApplet getReference(){
		return app;
	}
	
}
