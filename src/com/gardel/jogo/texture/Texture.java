package com.gardel.jogo.texture;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class Texture {

	private int texture;
	
	private static int current_bind = 0;
	
	private int width, height;
	public static final float pxFactor = 1.0f / 512.0f;
	public static final float pxFactor2 = 1.0f / 1024.0f;
	
	public static final float SIZE_32 = pxFactor * 32;

	public Texture(String file) {
		try {
			glEnable(GL_TEXTURE_2D);

			texture = glGenTextures();
			BufferedImage raw = ImageIO.read(new File("./resources/textures" + file));
			
			width = raw.getWidth();
			height = raw.getHeight();
			
			int pixels[] = new int[width * height];
			raw.getRGB(0, 0, width, height, pixels, 0, width);
			
			try (MemoryStack ms = MemoryStack.stackPush()){
				ByteBuffer colors = MemoryUtil.memCalloc(pixels.length * 4);
	
				for (int pixel : pixels) {
					Color c = new Color(pixel, true);
					colors.put((byte) c.getRed());
					colors.put((byte) c.getGreen());
					colors.put((byte) c.getBlue());
					colors.put((byte) c.getAlpha());
				}
	
				colors.flip();
	
				glBindTexture(GL_TEXTURE_2D, texture);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, colors);
				glBindTexture(GL_TEXTURE_2D, 0);
				
				MemoryUtil.memFree(colors);
			}
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void bind() {
		if(current_bind != texture) {
			glBindTexture(GL_TEXTURE_2D, texture);
			current_bind = texture;
		}
	}

	public static void unbind() {
		if(current_bind != 0) {
			current_bind = 0;
			glBindTexture(GL_TEXTURE_2D, 0);
		}
	}
}
