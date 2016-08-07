import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;


public class MyFire {

	static Image FireImage = Toolkit.getDefaultToolkit().getImage(
			Sky.class.getClassLoader().getResource("images/µ¼µ¯.PNG"));
	Color color = new Color(200,100,100);
	int x,y;
	int  speed = 20;
	Rectangle rt = null;
	boolean isdie = false;
	Sky sky = null;
	
	MyFire(int x, int y, Sky sky){
		this.x = x;
		this.y = y;
		rt = new Rectangle(x, y, 20, 20);
		this.sky = sky;
	}
	
	void draw(Graphics g) {
		if(isdie){
			return;
		}
		g.setColor(color);
		g.drawImage(FireImage, x, y, 20, 20, null);
//		g.fillOval(x, y, 20, 20);
		move1();
//		move2();
	}
	
	void move1(){
		this.y = this.y - speed;
		rt.setLocation(x, y);
	}
	
	void move2(){
		if(this.x>sky.mp.x){
			this.x -= 15; 
		}else if(this.x<sky.mp.x){
			this.x += 15;
		}
		move1();
	}
}
