import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Sky extends Frame {

	private static final long serialVersionUID = 1L;

	static InputStream sound_system_is = null;
	Image BackgroundImage = Toolkit.getDefaultToolkit().getImage(
			Sky.class.getClassLoader().getResource("images/飞机大战背景.jpg"));

	List<Plane> pList = new ArrayList<Plane>();
	MyPlane mp = null;
	Title tt = null;
	public int W = 600;
	public int H = 600;
	boolean gameOver = false;
	Color backgroundColor = new Color(200, 200, 200);
	Color xColor = Color.blue;

	boolean keyLdown = false;
	boolean keyRdown = false;
	boolean keyUdown = false;
	boolean keyDdown = false;

	Image offScreenImage = null;

	public static void main(String args[]) {
		Sky sky = new Sky();
		sky.launch();

	}

	private void doFacial() {

		this.setUndecorated(true);
		this.setLocation(300, 100);
		this.setSize(W, H);
		this.setBackground(backgroundColor);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.setVisible(true);
		tt = new Title();
		tt.launch();
	}

	private void launch() {
		doFacial();
		拖动窗口(this);
		拖动窗口(tt);
		mp = new MyPlane(300, 550, this);
		new Thread(new PaintThread()).start();
		this.addKeyListener(new MyKeyListener());
		/*
		 * this.addWindowListener(new WindowAdapter(){
		 * 
		 * @Override public void windowClosing(WindowEvent e) { dispose(); } });
		 */
	}

	void newPlanes() {// 制造敌机们
		pList.add(new Plane(10, 10, this));
		pList.add(new Plane(110, 10, this));
		pList.add(new Plane(210, 10, this));
		pList.add(new Plane(310, 10, this));
		pList.add(new Plane(410, 10, this));
		pList.add(new Plane(510, 10, this));
	}

	class MyKeyListener extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			int kc = e.getKeyCode();
			switch (kc) {
			case KeyEvent.VK_LEFT:
				keyLdown = false;
				break;
			case KeyEvent.VK_RIGHT:
				keyRdown = false;
				break;
			case KeyEvent.VK_UP:
				keyUdown = false;
				break;
			case KeyEvent.VK_DOWN:
				keyDdown = false;
				break;
			case KeyEvent.VK_SPACE:
				mp.fire();
				break;
			case KeyEvent.VK_CONTROL:
				if (mp.canBigFire) {
					mp.bigFire();
				}
				break;
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			int kc = e.getKeyCode();
			switch (kc) {
			case KeyEvent.VK_LEFT:
				keyLdown = true;
				break;
			case KeyEvent.VK_RIGHT:
				keyRdown = true;
				break;
			case KeyEvent.VK_UP:
				keyUdown = true;
				break;
			case KeyEvent.VK_DOWN:
				keyDdown = true;
				break;
			}
		}
	}

	public void 拖动窗口(Component cpn) {
		final Point point = new Point();
		cpn.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				point.x = e.getX();
				point.y = e.getY();
				if (point.x >= mp.x && point.x <= mp.x + 50 && point.y >= mp.y
						&& point.y <= mp.y + 50) {
					mp.isFocused = true;

					return;
				}
				mp.isFocused = false;
				tt.requestFocus();
				Sky.this.requestFocus();
			}
		});

		cpn.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (mp.isFocused) {
					mp.x = e.getX() - 25;
					mp.y = e.getY() - 25;
					if (mp.x <= 0) {
						mp.x = 0;
					}
					if (mp.x >= Sky.this.W - 50) {
						mp.x = Sky.this.W - 50;
					}
					if (mp.y <= 0) {
						mp.y = 0;
					}
					if (mp.y >= Sky.this.H - 50) {
						mp.y = Sky.this.H - 50;
					}
					mp.rt.setLocation(mp.x, mp.y);
					return;
				}
				Point p = Sky.this.getLocation();
				Sky.this.setLocation(p.x + e.getX() - point.x, p.y + e.getY()
						- point.y);
				tt.setLocation(getLocation().x + 601, getLocation().y);
			}
		});
	}

	public void update(Graphics g) {// super.update(g);
		if (offScreenImage == null) {
			offScreenImage = this.createImage(Sky.this.W, Sky.this.H);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(backgroundColor);
		gOffScreen.fillRect(0, 0, Sky.this.W, Sky.this.H);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	int i = 0;
	int LR = 1;

	@Override
	public void paint(Graphics g) {

		if (keyLdown) {
			mp.move(0);
		}
		if (keyRdown) {
			mp.move(1);
		}
		if (keyUdown) {// 上
			mp.move(2);
		}
		if (keyDdown) {// 下
			mp.move(3);
		}
		g.drawImage(BackgroundImage, 0, 0, this.W, this.H, this);
		if (mp != null && mp.canBigFire) {
			g.setColor(Color.red);
			g.drawString("您有一颗超级导弹！按ctrl发弹", W - 200, 50);
		}

		g.setColor(Color.black);
		if (LR == 1 && i <= 300) {
			i += 2;
			g.drawString("空格键发送导弹，键盘方向键或鼠标控制飞机移动", i, 65);
		} else if (LR == 0 && i >= 20) {
			i -= 2;
			g.drawString("空格键发送导弹，键盘方向键或鼠标控制飞机移动", i, 65);
		} else if (LR == 1 && i > 300) {
			LR = 0;
			i = 300;
		} else if (LR == 0 && i < 20) {
			LR = 1;
			i = 20;
		}

		for (int i = 0; i < pList.size(); i++) {// 画敌机和敌机的导弹
			Plane p = pList.get(i);
			if (p.isdie == false) {
				p.draw(g);
				p.move();
				p.drawOtherFires(g);
			} else {
				pList.remove(i--);
			}
		}

		if (pList.size() == 0) {// 生敌机
			newPlanes();
		}

		if (mp != null) {// 画我的飞机和子弹
			mp.checkDie();
			mp.draw(g);
			mp.drawFires(g);
		}
	}

	class PaintThread implements Runnable {
		@Override
		public void run() {
			while (gameOver == false) {
				repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void beep() {// 发声
		AudioStream as = null;
		try {
			sound_system_is = Sky.class.getClassLoader().getResourceAsStream("sounds/tweet.wav");
			as = new AudioStream(sound_system_is);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		AudioPlayer.player.start(as);
	}

	class Title extends Frame {// 右边的东西

		Image offScreenImage;

		private static final long serialVersionUID = 1L;

		void launch() {
			this.setUndecorated(true);
			this.setBackground(new Color(104, 33, 122));
			this.setLocation(901, 100);
			this.setSize(100, Sky.this.H);
			this.setVisible(true);
			/*
			 * this.addWindowListener(new WindowAdapter(){
			 * 
			 * @Override public void windowClosing(WindowEvent e) { //
			 * System.exit(0); dispose(); } });
			 */
			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int x = e.getX();
					int y = e.getY();
					if (x >= 30 && x <= 70 && y >= 25 && y <= 60) {
						System.exit(0);
					}
				}
			});

			addMouseMotionListener(new MouseAdapter() {
				@Override
				public void mouseMoved(MouseEvent e) {
					int x = e.getX();
					int y = e.getY();
					if (x >= 30 && x <= 70 && y >= 25 && y <= 60) {
						xColor = Color.red;
					} else {
						xColor = Color.blue;
					}
				}
			});

			new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						repaint();
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();

			this.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					Sky.this.requestFocus();
				}

			});

		}

		public void update(Graphics g) {
			if (offScreenImage == null) {
				offScreenImage = this.createImage(100, Sky.this.H);
			}
			Graphics gOffScreen = offScreenImage.getGraphics();
			Color c = gOffScreen.getColor();
			gOffScreen.setColor(new Color(104, 33, 122));
			gOffScreen.fillRect(0, 0, 100, Sky.this.H);
			gOffScreen.setColor(c);
			paint(gOffScreen);
			g.drawImage(offScreenImage, 0, 0, null);
		}

		@Override
		public void paint(Graphics g) {
			g.setFont(new Font("Helvetica", Font.PLAIN, 100));
			g.setColor(xColor);
			g.drawString("×", 20, 80);
			g.setFont(new Font("Helvetica", Font.PLAIN, 50));
			g.setColor(Color.green);
			g.drawString("贵", 25, 120);
			g.drawString("子", 25, 200);
			g.drawString("版", 25, 280);
			g.drawString("达", 25, 360);
			g.drawString("菲", 25, 440);
			g.drawString("鸡", 25, 520);
			drawScore(g, mp.score);
		}

		public void drawScore(Graphics g, int score) {
			g.setFont(new Font("Helvetica", Font.PLAIN, 15));
			g.setColor(Color.red);
			g.drawString("分数:" + score * 500, 0, 570);
		}
	}

}
