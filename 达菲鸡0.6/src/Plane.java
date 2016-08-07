import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;


public class Plane {
	
	Image PlaneImage = Toolkit.getDefaultToolkit().getImage(
			Sky.class.getClassLoader().getResource("images/µÐ»ú.PNG"));
	static Image OtherFireImage = Toolkit.getDefaultToolkit().getImage(
			Sky.class.getClassLoader().getResource("images/MissileU.gif"));
	Color color = Color.green;
	List<OtherFire> otherfires = new ArrayList<OtherFire>();
	int x = 10;
	int y = 10;
	Rectangle rt = null;
	boolean isdie = false;
	Sky sky = null;
	OtherFire of = null;
	int speed = (int)(Math.random()*5+10);
	int fireTimeCount = 0;
	
	Plane(int x, int y, Sky sky){
		this.x = x;
		this.y = y;
		rt = new Rectangle(x, y, 35, 35);
		this.sky = sky;
	}
	
	void checkDie(Graphics g){
		for(int i=0;i<sky.mp.fires.size();i++){
			MyFire f = sky.mp.fires.get(i);
			if(f.isdie==false&&this.isdie==false&&f!=null&&rt.intersects(f.rt)){
				isdie = true;
				sky.mp.dieCount++;
				sky.mp.score++;
				createBaoZha(g);//±¬Õ¨
				f.isdie = true;
				f = null;
				Sky.beep();
			}
		}
		if(this.y>=sky.H){
			isdie = true;
		}
	}
	
	void move(){
		this.y = this.y+speed;//(int)(Math.random()*15+1);
		rt.setLocation(x, y);
	}
	
	void draw(Graphics g) {
		fireTimeCount++;
		if(fireTimeCount%10==0){
			this.otherfires.add(new OtherFire(this.x,this.y,sky));
		}
		if(fireTimeCount==10000){
			fireTimeCount = 0;
		}
		checkDie(g);
		if(isdie){
			return;
		}
		g.drawImage(PlaneImage, x, y, 40, 40, null);
		/*g.setColor(Color.black);
		g.fillOval(x, y, 40, 40);
		g.setColor(Color.green);
		g.fillRect(x+5, y+5, 30, 30);
		g.fillRect(x+18, y+20, 4, 20);*/
	}
	
	void createBaoZha(Graphics g){
		new BaoZa(x,y).draw(g);
	}
	
	void drawOtherFires(Graphics g){
		
		for(int i = 0;i<otherfires.size();i++){
			of = otherfires.get(i);
			if(of!=null){
				if(of.y<=0||of.x<0||of.x>sky.W){
					otherfires.remove(i);
					i--;
					of = null;
					continue;
				}
				of.draw(g);
			}
		}
	}
	
	class OtherFire extends MyFire{

		@Override
		void draw(Graphics g) {
			// TODO Auto-generated method stub
//			super.draw(g);
			if(isdie){
				return;
			}
			g.setColor(color);
//			g.fillOval(x, y, 20, 20);
			g.drawImage(OtherFireImage, x, y, 20, 20, null);
			move2();
		}

		@Override
		void move1() {
			this.y = this.y + 20;
			rt.setLocation(x, y);
		}
		
		@Override
		void move2() {
			if(this.x>sky.mp.x){
				this.x -= 10; 
			}else if(this.x<sky.mp.x){
				this.x += 10;
			}
			move1();
		}

		OtherFire(int x, int y, Sky sky) {
			super(x, y, sky);
			
		}
		
	}
	
}
