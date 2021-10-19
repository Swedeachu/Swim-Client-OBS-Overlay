import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;
import java.awt.Font;
import java.awt.Image;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.EventQueue;

public class Window extends JFrame implements NativeKeyListener, NativeMouseListener, ActionListener {

	private static final long serialVersionUID = -5114933480049319209L;

	private JPanel contentPane;

	public static JFrame frame = new JFrame("Swim v4");

	public static JLabel dPressed = new JLabel("D");
	public static JLabel aPressed = new JLabel("A");
	public static JLabel sPressed = new JLabel("S");
	public static JLabel wPressed = new JLabel("w");
	public static JLabel spacePressed = new JLabel("space");

	public static JLabel lmb = new JLabel("LMB");
	public static JLabel rmb = new JLabel("RMB");
	public static JLabel lmbPressed = new JLabel("LMB");
	public static JLabel rmbPressed = new JLabel("RMB");

	private static List<Long> clicks = new ArrayList<Long>();
	private long lastPressed;

	private static List<Long> rightClicks = new ArrayList<Long>();
	private long rightLastPressed;

	public static JLabel cpsLabel = new JLabel("0");
	public static JLabel rightCpsLabel = new JLabel("0");
	
	public static int mouseX = 0;
	public static int mouseY = 0;

	public static void main(String[] args)
			throws IOException, AWTException, UnsupportedAudioFileException, LineUnavailableException,
			ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, NullPointerException {
		// tapping into mouse input
		GlobalScreen.addNativeKeyListener(new Window());
		GlobalScreen.addNativeMouseListener(new Window());

		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
		logger.setUseParentHandlers(true);

		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException keyError) {
			keyError.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window frame = new Window();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		while (true) {
			final long time = System.currentTimeMillis();
			try {
				clicks.removeIf(aLong -> aLong + 1000 < time);
				cpsLabel.setText(String.valueOf(clicks.size()));
			} catch (ConcurrentModificationException | NullPointerException e) {
				e.printStackTrace();
			}
			try {
				rightClicks.removeIf(aLong2 -> aLong2 + 1000 < time);
				rightCpsLabel.setText(String.valueOf(rightClicks.size()));
			} catch (ConcurrentModificationException | NullPointerException e) {
				e.printStackTrace();
			}
		}

	}

	public Window() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException, IOException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		setTitle("Swim Keystrokes");
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 240, 300);
		setLocationRelativeTo(null);
		setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(Color.GRAY, 2));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
		setAlwaysOnTop(true);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
		panel.setBounds(0, 0, 240, 300);
		contentPane.add(panel);
		panel.setLayout(null);

		JButton close = new JButton("CLOSE");
		close.setFont(new Font("Tahoma", Font.PLAIN, 7));
		close.setVisible(false);
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		close.setBounds(10, 266, 53, 23);
		panel.add(close);
		
		panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				setLocation(getX() + e.getX() - mouseX, getY() + e.getY() - mouseY);
			}
		});
		
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});

		// UI

		cpsLabel.setForeground(Color.WHITE);
		cpsLabel.setFont(new Font("Tahoma", Font.PLAIN, 28));
		cpsLabel.setBounds(60, 140, 56, 50);
		panel.add(cpsLabel);

		rightCpsLabel.setForeground(Color.WHITE);
		rightCpsLabel.setFont(new Font("Tahoma", Font.PLAIN, 28));
		rightCpsLabel.setBounds(145, 140, 56, 50);
		panel.add(rightCpsLabel);

		// pressed mouse
		lmbPressed.setBackground(Color.BLUE);
		lmbPressed.setBounds(28, 146, 85, 40);
		BufferedImage lmbIMG2 = ImageIO.read(Window.class.getResource("/mouse/mouseButtonPressed.png"));
		Image lmbPIC2 = lmbIMG2.getScaledInstance(lmbPressed.getWidth(), lmbPressed.getHeight(), Image.SCALE_SMOOTH);
		lmbPressed.setIcon(new ImageIcon(lmbPIC2));
		panel.add(lmbPressed);

		rmbPressed.setBackground(Color.BLUE);
		rmbPressed.setBounds(113, 146, 85, 40);
		BufferedImage rmbIMG2 = ImageIO.read(Window.class.getResource("/mouse/mouseButtonPressed.png"));
		Image rmbPIC2 = rmbIMG2.getScaledInstance(rmbPressed.getWidth(), rmbPressed.getHeight(), Image.SCALE_SMOOTH);
		rmbPressed.setIcon(new ImageIcon(rmbPIC2));
		panel.add(rmbPressed);

		// pressed keys

		dPressed.setForeground(Color.BLUE);
		dPressed.setBackground(Color.BLUE);
		dPressed.setBounds(148, 84, 50, 50);
		BufferedImage pic1 = ImageIO.read(Window.class.getResource("/wasd/dKeyPressed.png"));
		Image dpic1 = pic1.getScaledInstance(dPressed.getWidth(), dPressed.getHeight(), Image.SCALE_SMOOTH);
		dPressed.setIcon(new ImageIcon(dpic1));
		panel.add(dPressed);

		aPressed.setForeground(Color.BLUE);
		aPressed.setBackground(Color.BLUE);
		aPressed.setBounds(28, 84, 50, 50);
		BufferedImage pic2 = ImageIO.read(Window.class.getResource("/wasd/aKeyPressed.png"));
		Image dpic2 = pic2.getScaledInstance(aPressed.getWidth(), aPressed.getHeight(), Image.SCALE_SMOOTH);
		aPressed.setIcon(new ImageIcon(dpic2));
		panel.add(aPressed);

		sPressed.setForeground(Color.BLUE);
		sPressed.setBackground(Color.BLUE);
		sPressed.setBounds(88, 84, 50, 50);
		BufferedImage pic3 = ImageIO.read(Window.class.getResource("/wasd/sKeyPressed.png"));
		Image dpic3 = pic3.getScaledInstance(sPressed.getWidth(), sPressed.getHeight(), Image.SCALE_SMOOTH);
		sPressed.setIcon(new ImageIcon(dpic3));
		panel.add(sPressed);

		wPressed.setForeground(Color.BLUE);
		wPressed.setBackground(Color.BLUE);
		wPressed.setBounds(88, 22, 50, 50);
		BufferedImage pic4 = ImageIO.read(Window.class.getResource("/wasd/wKeyPressed.png"));
		Image dpic4 = pic4.getScaledInstance(wPressed.getWidth(), wPressed.getHeight(), Image.SCALE_SMOOTH);
		wPressed.setIcon(new ImageIcon(dpic4));
		panel.add(wPressed);

		spacePressed.setBounds(28, 198, 170, 50);
		BufferedImage pic5 = ImageIO.read(Window.class.getResource("/space/spacePressed.png"));
		Image dpic5 = pic5.getScaledInstance(spacePressed.getWidth(), spacePressed.getHeight(), Image.SCALE_SMOOTH);
		spacePressed.setIcon(new ImageIcon(dpic5));
		panel.add(spacePressed);

		wPressed.setVisible(false);
		aPressed.setVisible(false);
		sPressed.setVisible(false);
		dPressed.setVisible(false);
		spacePressed.setVisible(false);
		lmbPressed.setVisible(false);
		rmbPressed.setVisible(false);
		// normal keys

		JLabel wLabel = new JLabel("W");
		wLabel.setForeground(Color.BLUE);
		wLabel.setBackground(Color.BLUE);
		wLabel.setBounds(88, 22, 50, 50);
		BufferedImage img = ImageIO.read(Window.class.getResource("/wasd/wKey.png"));
		Image dimg = img.getScaledInstance(wLabel.getWidth(), wLabel.getHeight(), Image.SCALE_SMOOTH);
		wLabel.setIcon(new ImageIcon(dimg));
		panel.add(wLabel);

		JLabel sLabel = new JLabel("S");
		sLabel.setForeground(Color.BLUE);
		sLabel.setBackground(Color.BLUE);
		sLabel.setBounds(88, 84, 50, 50);
		BufferedImage img2 = ImageIO.read(Window.class.getResource("/wasd/sKey.png"));
		Image dimg2 = img2.getScaledInstance(sLabel.getWidth(), sLabel.getHeight(), Image.SCALE_SMOOTH);
		sLabel.setIcon(new ImageIcon(dimg2));
		panel.add(sLabel);

		JLabel aLabel = new JLabel("A");
		aLabel.setForeground(Color.BLUE);
		aLabel.setBackground(Color.BLUE);
		aLabel.setBounds(28, 84, 50, 50);
		BufferedImage img3 = ImageIO.read(Window.class.getResource("/wasd/aKey.png"));
		Image dimg3 = img3.getScaledInstance(aLabel.getWidth(), aLabel.getHeight(), Image.SCALE_SMOOTH);
		aLabel.setIcon(new ImageIcon(dimg3));
		panel.add(aLabel);

		JLabel dLabel = new JLabel("D");
		dLabel.setForeground(Color.BLUE);
		dLabel.setBackground(Color.BLUE);
		dLabel.setBounds(148, 84, 50, 50);
		BufferedImage img4 = ImageIO.read(Window.class.getResource("/wasd/dKey.png"));
		Image dimg4 = img4.getScaledInstance(dLabel.getWidth(), dLabel.getHeight(), Image.SCALE_SMOOTH);
		dLabel.setIcon(new ImageIcon(dimg4));
		panel.add(dLabel);

		JLabel space = new JLabel("space");
		space.setBounds(28, 198, 170, 50);
		BufferedImage img5 = ImageIO.read(Window.class.getResource("/space/space.png"));
		Image dimg5 = img5.getScaledInstance(space.getWidth(), space.getHeight(), Image.SCALE_SMOOTH);
		space.setIcon(new ImageIcon(dimg5));
		panel.add(space);

		lmb.setBackground(Color.BLUE);
		lmb.setBounds(28, 146, 85, 40);
		BufferedImage lmbIMG = ImageIO.read(Window.class.getResource("/mouse/mouseButton.png"));
		Image lmbPIC = lmbIMG.getScaledInstance(lmb.getWidth(), lmb.getHeight(), Image.SCALE_SMOOTH);
		lmb.setIcon(new ImageIcon(lmbPIC));
		panel.add(lmb);

		rmb.setBackground(Color.BLUE);
		rmb.setBounds(113, 146, 85, 40);
		BufferedImage rmbIMG = ImageIO.read(Window.class.getResource("/mouse/mouseButton.png"));
		Image rmbPIC = rmbIMG.getScaledInstance(rmb.getWidth(), rmb.getHeight(), Image.SCALE_SMOOTH);
		rmb.setIcon(new ImageIcon(rmbPIC));
		panel.add(rmb);
	}

	public static BufferedImage loadImage(String str) throws IOException {
		BufferedImage image = ImageIO.read(Window.class.getResource(str));
		return image;
	}

	// INPUTS

	@Override
	public void nativeMousePressed(NativeMouseEvent mouseEvent) {
		if (mouseEvent.getButton() == 1) {
			this.lastPressed = System.currentTimeMillis();
			clicks.add(this.lastPressed);
		}

		if (mouseEvent.getButton() == 2) {
			this.rightLastPressed = System.currentTimeMillis();
			rightClicks.add(this.rightLastPressed);
		}

	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent key) {
		// System.out.println(key.getKeyCode());
		int keyNum = key.getKeyCode();
		if (keyNum == 17) {
			// System.out.println("W pressed");
			wPressed.setVisible(true);
		} else {
			if (keyNum == 30) {
				// System.out.println("A pressed");
				aPressed.setVisible(true);
			} else {
				if (keyNum == 31) {
					// System.out.println("S pressed");
					sPressed.setVisible(true);
				} else {
					if (keyNum == 32) {
						// System.out.println("D pressed");
						dPressed.setVisible(true);
					} else {
						if (keyNum == 57) {
							// System.out.println("Space pressed");
							spacePressed.setVisible(true);
						}
					}
				}
			}
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent key) {
		// System.out.println(key.getKeyCode());
		int keyNum = key.getKeyCode();
		if (keyNum == 17) {
			// System.out.println("W released");
			wPressed.setVisible(false);
		} else {
			if (keyNum == 30) {
				// System.out.println("A released");
				aPressed.setVisible(false);
			} else {
				if (keyNum == 31) {
					// System.out.println("S released");
					sPressed.setVisible(false);
				} else {
					if (keyNum == 32) {
						// System.out.println("D released");
						dPressed.setVisible(false);
					} else {
						if (keyNum == 57) {
							// System.out.println("Space released");
							spacePressed.setVisible(false);
						}
					}
				}
			}
		}
	}

	// unused but required methods

	@Override
	public void nativeKeyTyped(NativeKeyEvent key) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	@Override
	public void nativeMouseClicked(NativeMouseEvent mouseEvent) {

	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent mouseEvent) {

	}
}
