package com.gardel.jogo.formas;

import static org.lwjgl.opengl.GL11.*;

import com.gardel.Jogo;
import com.gardel.jogo.math.Mathf;
import com.gardel.jogo.texture.Texture;

public class Planeta implements IForma {

	private float x,y = Jogo.HEIGHT + 100, rotation = 0;
	
	private float yTo;
	
	private static final int SIZE = 120;
	
	private static final float tX = 196 * Texture.pxFactor;
	private static final float tY = 325 * Texture.pxFactor;
	
	private static final float sW = tX + 75 * Texture.pxFactor;
	private static final float sH = tY + 75 * Texture.pxFactor;
	
	public Planeta(float x, float y) {
		this.x = x;
		this.yTo = y;
	}
	
	@Override
	public IForma render() {
		glPushMatrix();
			glColor3f(1, 1, 1);
			glTranslatef(x, y, 0);
			glRotatef(rotation += 0.1f, 0, 0, 1);
			glBegin(GL_QUADS);
				glTexCoord2f(tX, sH);
				glVertex2f(-SIZE,  SIZE);
				glTexCoord2f(sW, sH);
				glVertex2f( SIZE,  SIZE);
				glTexCoord2f(sW, tY);
				glVertex2f( SIZE, -SIZE);
				glTexCoord2f(tX, tY);
				glVertex2f(-SIZE, -SIZE);
			glEnd();
		glPopMatrix();
		
		return this;
	}

	@Override
	public IForma update() {
		y = Mathf.lerp(y, yTo, 0.4f);
		return this;
	}

	@Override
	public int getDepth() {
		return 1;
	}
}
