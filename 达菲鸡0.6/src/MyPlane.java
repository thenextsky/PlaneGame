import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

public class MyPlane {

	List<MyFire> fires = new ArrayList<MyFire>();

	Image MyPlaneImage = Toolkit.getDefaultToolkit().getImage(
			Sky.class.getClassLoader().getResource("images/我的飞机.PNG"));
	boolean isFocused = false;
	int fireCount = 0;
	int dieCount = 0;
	int score = 0;
	int x = 300;
	int y = 550;
	Rectangle rt = null;
	Color color = new Color(100, 100, 255);
	MyFire f = null;
	boolean isdie = false;
	Sky sky = null;
	boolean canBigFire = true;

	MyPlane(int x, int y, Sky sky) {
		this.x = x;
		this.y = y;
		rt = new Rectangle(x, y, 45, 45);
		this.sky = sky;
	}

	void drawFires(Graphics g) {

		for (int i = 0; i < fires.size(); i++) {
			f = fires.get(i);
			if (f != null) {
				if (f.y <= 0 || f.x < 0 || f.x > sky.W) {
					fires.remove(i);
					i--;
					f = null;
					continue;
				}
				f.draw(g);
			}
		}
	}

	void draw(Graphics g) {

		g.setColor(color);
		g.drawImage(MyPlaneImage, x, y, 50, 50, sky);//抛异常
		/*
		 * g.fillRect(x, y, 50, 50); g.setColor(Color.red); g.fillOval(x+15,
		 * y+15, 20, 20);
		 */
		g.drawString("使用子弹数:" + fireCount, 10, 10);
		g.drawString("消灭敌机数:" + dieCount, 10, 30);
		if (fireCount == 0) {
			g.drawString("技能系数:0", 10, 50);
		} else {
			g.drawString("技能系数:"
					+ ((int) ((dieCount / (0.0 + fireCount)) * 1000)) / 1000.0,
					10, 50);
		}
		if (isdie) {
			gameOver(g);
			return;
		}
	}

	void move(int dir) {
		if (dir == 0 && this.x >= 0) {
			this.x = this.x - 25;
		} else if (dir == 1 && this.x <= 600 - 50) {
			this.x = this.x + 25;
		} else if (dir == 2 && this.y >= 15) {// 向上
			this.y = this.y - 25;
		} else if (dir == 3 && this.y <= sky.H - 50) {// 向下
			this.y = this.y + 25;
		}

		rt.setLocation(x, y);// 在鼠标拖动时，这个也要弄啊啊啊
	}

	void gameOver(Graphics g) {
		g.setColor(Color.red);
		g.setFont(new Font("Helvetica", Font.PLAIN, 100));
		g.drawString("游戏结束！", 100, 300);
		sky.gameOver = true;
	}

	void checkDie() {
		for (int i = 0; i < sky.pList.size(); i++) {
			Plane p = sky.pList.get(i);
			if (p != null) {
				for (int j = 0; j < p.otherfires.size(); j++) {
					Plane.OtherFire of = p.otherfires.get(j);
					if (of.isdie == false && this.isdie == false && of != null
							&& rt.intersects(of.rt)) {
						isdie = true;
						sky.mp.dieCount++;
						sky.mp.score++;
						of.isdie = true;
						of = null;
						Sky.beep();
					}
				}
			}
			if (p != null && p.isdie == false && rt.intersects(p.rt)) {
				isdie = true;
				p.isdie = true;
				p = null;
			}
		}
	}

	void fire() {
		fires.add(new MyFire(x + 15, y, sky));
		fireCount++;
		if (dieCount % 30 == 0) {
			canBigFire = true;
		}
	}

	void bigFire() {
		fireCount += 11;
		fires.add(new MyFire(x - 15, y, sky));
		fires.add(new MyFire(x - 75, y, sky));
		fires.add(new MyFire(x - 135, y, sky));
		fires.add(new MyFire(x - 195, y, sky));
		fires.add(new MyFire(x - 255, y, sky));
		fires.add(new MyFire(x - 315, y, sky));
		fires.add(new MyFire(x - 375, y, sky));
		fires.add(new MyFire(x - 435, y, sky));
		fires.add(new MyFire(x - 495, y, sky));
		fires.add(new MyFire(x - 555, y, sky));

		fires.add(new MyFire(x + 15, y, sky));
		fires.add(new MyFire(x + 75, y, sky));
		fires.add(new MyFire(x + 135, y, sky));
		fires.add(new MyFire(x + 195, y, sky));
		fires.add(new MyFire(x + 255, y, sky));
		fires.add(new MyFire(x + 315, y, sky));
		fires.add(new MyFire(x + 375, y, sky));
		fires.add(new MyFire(x + 435, y, sky));
		fires.add(new MyFire(x + 495, y, sky));
		fires.add(new MyFire(x + 555, y, sky));
		canBigFire = false;
	}

}
