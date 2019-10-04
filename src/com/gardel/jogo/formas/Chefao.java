package com.gardel.jogo.formas;

import static org.lwjgl.opengl.GL11.*;

import com.gardel.jogo.collision.Collidable;
import com.gardel.jogo.texture.Texture;

public class Chefao extends Collidable implements IForma {

	private static final float tW = 128 * Texture.pxFactor2;
	private static final float tH = 160 * Texture.pxFactor2;

	private static final int MAX_FRAMES = 32;

	private static final int SIZE = 186;
	
	private float frame = 0;
	
	private EsferaChefao esferaEsq,esferaDir;
	
	public Chefao(float x, float y) {
		this.x = x;
		this.y = y;
		esferaEsq = new EsferaChefao(this, true);
		esferaDir = new EsferaChefao(this, false);
	}
	
	@Override
	public IForma render() {
		float txPos = tW * ((int)(frame) % 8 );
		float tyPos = tH * ((int)(frame) / 8 );
		glColor3f(1, 1, 1);
		glPushMatrix();
			glTranslatef(x, y, 0);
			glBegin(GL_QUADS);
			glTexCoord2f(txPos + tW,tyPos + tH);
				glVertex2f(-SIZE,  SIZE);
				glTexCoord2f(txPos,tyPos + tH);
				glVertex2f( SIZE,  SIZE);
				glTexCoord2f(txPos,tyPos);
				glVertex2f( SIZE, -SIZE);
				glTexCoord2f(txPos + tW,tyPos);
				glVertex2f(-SIZE, -SIZE);
			glEnd();
		glPopMatrix();
		
		esferaEsq.render();
		esferaDir.render();
		
		return this;
	}

	public IForma update() {
		frame += 6.0/60.0;
		if(frame >= MAX_FRAMES) {
			frame = 0;
		}
		esferaEsq.update();
		esferaDir.update();
		return this;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public static int getSize() {
		return SIZE;
	}
	
	public int getTextureSheet() {
		return 1;
	}

	@Override
	public boolean isColliding(Collidable collidable) {
		return esferaDir.isColliding(collidable) || esferaEsq.isColliding(collidable);
	}
	
	
}
