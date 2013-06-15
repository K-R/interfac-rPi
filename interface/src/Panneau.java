import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

//defines the background
public class Panneau extends JPanel {
	public void paintComponent(Graphics g){
		try {
			//make appears the image of the switch
			Image img = ImageIO.read(getClass().getResource("images/netgear.jpg"));
			g.drawImage(img, 0, 0, 880, 175, this);
		}
		catch (IOException e) {
			System.out.println("bug fenetre");
			e.printStackTrace();
		}               
	}              
}
