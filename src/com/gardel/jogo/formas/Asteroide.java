package com.gardel.jogo.formas;

import static org.lwjgl.opengl.GL11.*;

import com.gardel.jogo.collision.Collidable;
import com.gardel.jogo.texture.Texture;

public class Asteroide extends Collidable implements IForma {

	private float rotation;
	
	private static final float tX = 0;
	private static final float tY = 326 * Texture.pxFactor;
	
	private static final float sW = tX + 195 * Texture.pxFactor;
	private static final float sH = tY + 186 * Texture.pxFactor;
	
	private static final int SIZE = 40;
	
	public Asteroide(float x, float y) {
		this.x = x;
		this.y = y;
		this.rotation = (float) (Math.random() * 360);
		setRaio(20);
	}
	
	@Override
	public IForma render() {
		glColor3f(1, 1, 1);
		glPushMatrix();
			glTranslatef(x, y, 0);
			glRotatef(rotation += 1f, 0, 0, 1);
			glBegin(GL_QUADS);
				glTexCoord2f(0, sH);
				glVertex2f(-SIZE,  SIZE);
				glTexCoord2f(sW, sH);
				glVertex2f( SIZE,  SIZE);
				glTexCoord2f(sW, tY);
				glVertex2f( SIZE, -SIZE);
				glTexCoord2f(0, tY);
				glVertex2f(-SIZE, -SIZE);
			glEnd();
		glPopMatrix();
		
		return this;
	}

	@Override
	public IForma update() {
		return this;
	}

}
