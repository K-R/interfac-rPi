
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;

//defines the buttons the see the status of the different ports
public class Bouton extends JButton{
	private String name;
	private Image img;
	
	public Bouton(String str, boolean statut){
		super(str);
		try {
			if(statut==false){//if the port is off the button is red and named "OFF"
				img = ImageIO.read(getClass().getResource("images/red_button.png"));
				this.name = "OFF";
			}
			else{//if the port is on the button is greeb and named "ON"
				img = ImageIO.read(getClass().getResource("images/green_button.png"));
				this.name = "ON";
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		setBorderPainted(false);
		setContentAreaFilled(false);
	}

	//style of the button itself and what is written in it
	public void paintComponent(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
		g2d.setColor(Color.black);
		if(this.name=="loading" || this.name=="waiting"){
			g2d.drawString(this.name, this.getWidth() / 2 - (this.getWidth() /  2 /4)-16, (this.getHeight() / 2) + 5);
		}
		else{
			g2d.drawString(this.name, this.getWidth() / 2 - (this.getWidth() /  2 /4)-1, (this.getHeight() / 2) + 5);
		}
	}

	//makes the button green and named "ON"
	public void start(){
		try {
			img = ImageIO.read(getClass().getResource("images/green_button.png"));
			this.name="ON";
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	//makes the button red and named "OFF"
	public void stop(){
		try {
			img = ImageIO.read(getClass().getResource("images/red_button.png"));
			this.name="OFF";
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	//makes the button yellow and named "loading"
	public void enCours(){
		try {
			img = ImageIO.read(getClass().getResource("images/yellow_button.png"));
			this.name="loading";
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	//makes the button yellow and named "waiting"
	public void enAttente(){
		try {
			img = ImageIO.read(getClass().getResource("images/yellow_button.png"));
			this.name="waiting";
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}

