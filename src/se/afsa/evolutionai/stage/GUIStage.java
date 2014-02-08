package se.afsa.evolutionai.stage;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import se.afsa.evolutionai.entities.Entity;
import se.afsa.evolutionai.entities.Player;
import se.afsa.evolutionai.ui.Drawable;

public class GUIStage extends Stage {
	/**
	 * A class that takes care of the entities in the game. This stage supports GUI.
	 * @see Stage
	 */
	private static final long serialVersionUID = 4373102162005375965L;
	private JFrame frame = new JFrame("Evolution AI");
	private final String quit = "ActionQuit";
	private List<Drawable> nonEntityGraphics = new ArrayList<>(); 
	
	/**
	 * Create a new GUI stage that handles the entities and the graphics of the game.
	 * Escape is set as default as close button.
	 */
	public GUIStage() {
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), quit);
		getActionMap().put(quit, new AbstractAction(quit) {
			private static final long serialVersionUID = -1703701059829872525L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
	}
	
	/**
	 * Display the stage for the player, this hides the cursor and is closed by escape.
	 */
	public void init() {
		GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = graphics.getDefaultScreenDevice();
		frame.setUndecorated(true);
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		device.setFullScreenWindow(frame);
		hideCursor();
	}
	
	/**
	 * Add a non entity drawable object to the list of objects to be drawn. Objects in this list will be updated every frame.
	 * The method draw() will then be called.
	 * @param drawable - the drawable object.
	 */
	public void addGraphicalItem(Drawable drawable) {
		nonEntityGraphics.add(drawable);
	}
	
	// Run on repaint (every frame). Makes calls to all drawable objects.
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		
		Graphics2D graphics2d = (Graphics2D) graphics;
		
		RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		graphics2d.setRenderingHints(renderingHints);
		
		List<Entity> entities = getEntities();
		
		for (int i = 0; i < entities.size(); i++) {
			Entity tempEntity = entities.get(i);
			if(tempEntity.isAlive()) {
				tempEntity.draw(graphics2d);
			}
		}
		
		for (int i = 0; i < nonEntityGraphics.size(); i++) {
			nonEntityGraphics.get(i).draw(graphics2d);
		}
	}
	
	// Like the method in super class, but when players are added they become added to the key listener.
	@Override
	public void addEntity(Entity entity) {
		super.addEntity(entity);
		if(entity instanceof Player) {
			addKeyListener((Player) entity);
		}
		repaint();
	}
	
	/**
	 * Hide the cursor.
	 */
	private void hideCursor() {
		Cursor noCursor = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "NoCursor");
		frame.getContentPane().setCursor(noCursor);
	}
}