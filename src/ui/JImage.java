package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class JImage extends JPanel 
{
	private static final long serialVersionUID = -1667467354112273118L;
	private BufferedImage img;
	private Image finalImg;
	
	public JImage(InputStream stream, Dimension size)
	{
		try 
		{
			img = ImageIO.read(stream);	
			finalImg = img.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
		} 
		catch (IOException e) 
		{
			img = new BufferedImage(0, 0, 0);
			System.out.println(e.getMessage());	
		}
		
		setPreferredSize(new Dimension(finalImg.getWidth(null), finalImg.getHeight(null)));
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(finalImg, 0, 0, null);
	}
}
