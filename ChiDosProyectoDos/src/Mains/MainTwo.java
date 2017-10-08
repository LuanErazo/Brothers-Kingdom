package Mains;

import cliente.ComunicacionCliente;
import processing.core.PApplet;
import usuario.Logica;

public class MainTwo extends PApplet{
	
	private String texto;

	public static void main(String[] args) {
		main("Mains.MainTwo");
	}
	
	private static PApplet app;
//	private LogicaTwo log;
	private Logica loggi;
	private ComunicacionCliente cliente;
	
	@Override
	public void settings() {
		size(1200,700);
	}
	
	@Override
	public void setup() {
		app = this;
		cliente = new ComunicacionCliente();
		loggi = new Logica(this, false, cliente);
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
