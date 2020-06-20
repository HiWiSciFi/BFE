package bfeditor.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class ImageButton extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private boolean pressed = false;
	
	private Image defaultImage;
	private Image pressedImage;
	
	public ImageButton(String path) throws IOException {
		super();
		try {
			defaultImage = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		SetPressedImage(defaultImage);
		
		addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            	for (ImageButtonListener c : clickEvents) {
            		c.Preform();
            	}
            	pressed = true;
            	repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            	for (ImageButtonListener c : releaseEvents) {
            		c.Preform();
            	}
            	pressed = false;
            	repaint();
            }
        });
	}
	
	public void SetPressedImage(Image image) {
		pressedImage = image;
	}
	
	public void SetPressedImage(String path) throws IOException {
		defaultImage = ImageIO.read(new File(path));
	}
	
	private ArrayList<ImageButtonListener> clickEvents = new ArrayList<ImageButtonListener>();
	private ArrayList<ImageButtonListener> releaseEvents = new ArrayList<ImageButtonListener>();
	
	public void AddClickListener(ImageButtonListener func) {
		clickEvents.add(func);
	}
	
	public void AddReleaseListener(ImageButtonListener func) {
		releaseEvents.add(func);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension size = getPreferredSize();
		Image df = defaultImage.getScaledInstance(size.width, size.height, Image.SCALE_DEFAULT);
		Image ps = pressedImage.getScaledInstance(size.width, size.height, Image.SCALE_DEFAULT);
		g.drawImage(pressed ? df : ps, 0, 0, size.width, size.height, this.getBackground(), null);
	}
}
