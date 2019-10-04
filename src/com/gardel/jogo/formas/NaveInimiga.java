package com.gardel.jogo.formas;

import static org.lwjgl.opengl.GL11.*;

import com.gardel.jogo.collision.Collidable;
import com.gardel.jogo.texture.Texture;

public class NaveInimiga extends Collidable implements IForma{
	
	private static final float tW = 60 * Texture.pxFactor2;
	private static final float tH = 60 * Texture.pxFactor2;
	private static final float pY = 701 * Texture.pxFactor2;
	private static final int MAX_FRAMES = 4;
	
	private static final int SIZE = 50;
	
	private float frame = 0;
	
	public NaveInimiga(float x, float y) {
		this.x = x;
		this.y = y;
		setRaio(20);
	}
	
	@Override
	public IForma render() {
		float txPos = tW * (int)(frame);
		glColor3f(1, 1, 1);
		glPushMatrix();
			glTranslatef(x, y, 0);
			glBegin(GL_QUADS);
				glTexCoord2f(txPos,pY);
				glVertex2f(-SIZE,  SIZE);
				glTexCoord2f(txPos + tW,pY);
				glVertex2f( SIZE,  SIZE);
				glTexCoord2f(txPos + tW,pY + tH);
				glVertex2f( SIZE, -SIZE);
				glTexCoord2f(txPos,pY + tH);
				glVertex2f(-SIZE, -SIZE);
			glEnd();
		glPopMatrix();
		
		return this;
	}

	public IForma update() {
		
		frame += 6.0f/18.0;
		
		if(frame >= MAX_FRAMES) {
			frame = 0;
		}
		
		return this;
	}
	
	public int getTextureSheet() {
		return 1;
	}
}
