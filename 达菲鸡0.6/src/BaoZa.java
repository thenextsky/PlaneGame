import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;


public class BaoZa {
	
	static Toolkit tk = Toolkit.getDefaultToolkit();
	
	int x,y;
	
	static Image images[] = {
		tk.getImage(Sky.class.getClassLoader().getResource("images/0.gif")),
		tk.getImage(Sky.class.getClassLoader().getResource("images/1.gif")),
		tk.getImage(Sky.class.getClassLoader().getResource("images/2.gif")),
		tk.getImage(Sky.class.getClassLoader().getResource("images/3.gif")),
		tk.getImage(Sky.class.getClassLoader().getResource("images/4.gif")),
		tk.getImage(Sky.class.getClassLoader().getResource("images/5.gif")),
		tk.getImage(Sky.class.getClassLoader().getResource("images/6.gif")),
		tk.getImage(Sky.class.getClassLoader().getResource("images/7.gif")),
		tk.getImage(Sky.class.getClassLoader().getResource("images/8.gif")),
		tk.getImage(Sky.class.getClassLoader().getResource("images/9.gif")),
		tk.getImage(Sky.class.getClassLoader().getResource("images/10.gif")),
	};
	
	BaoZa(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	void draw(Graphics g) {
		
		for(int i=0;i<=10;i++){
			g.drawImage(images[i], x, y, null);
		}
		
	}
	
	

}
