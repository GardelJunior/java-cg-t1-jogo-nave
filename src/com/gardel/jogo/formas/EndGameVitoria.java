package com.gardel.jogo.formas;

import static org.lwjgl.opengl.GL11.*;

import com.gardel.jogo.texture.Texture;

public class EndGameVitoria implements IForma {

	private float x,y;
	
	private static final int SIZE = 120;
	
	private static final float tX = 0;
	private static final float tY = 128 * Texture.pxFactor;
	
	private static final float sW = tX + 64 * Texture.pxFactor;
	private static final float sH = tY + 64 * Texture.pxFactor;
	
	
	private float visible = 0;
	
	public EndGameVitoria(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public IForma render() {
		glColor4f(1, 1, 1,visible);
		glPushMatrix();
			glTranslatef(x, y, 0);
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
		if(visible < 1) {
			visible += 0.005f;
		}
		return this;
	}

	@Override
	public int getDepth() {
		return 1;
	}
}
