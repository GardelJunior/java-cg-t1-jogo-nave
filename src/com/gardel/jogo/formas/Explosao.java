package com.gardel.jogo.formas;

import static org.lwjgl.opengl.GL11.*;

import com.gardel.jogo.manager.EntityManager;
import com.gardel.jogo.texture.Texture;

public class Explosao implements IForma {

	private float x,y;
	private float rotation;
	
	private static final float tX = 0;
	private static final float tY = 32 * Texture.pxFactor;
	
	private static final float sW = tX + 32 * Texture.pxFactor;
	private static final float sH = tY + 32 * Texture.pxFactor;
	
	private static final int MAX_FRAMES = 13; 

	private static final int SIZE = 60;
	
	private float frame = 0;
	
	private float scale = 1;
	
	public Explosao(float x, float y) {
		this.x = x;
		this.y = y;
		this.rotation = (float) (Math.random() * 360);
	}
	
	public Explosao(float x, float y, float s) {
		this.x = x;
		this.y = y;
		this.scale = s;
		this.rotation = (float) (Math.random() * 360);
	}
	
	@Override
	public IForma render() {
		float txPos = sW * (int)frame;
		glColor3f(1, 1, 1);
		glPushMatrix();
			glTranslatef(x, y, 0);
			glRotatef(rotation, 0, 0, 1);
			glScalef(scale, scale, 1);
			glBegin(GL_QUADS);
				glTexCoord2f(txPos, sH);
				glVertex2f(-SIZE,  SIZE);
				glTexCoord2f(sW + txPos, sH);
				glVertex2f( SIZE,  SIZE);
				glTexCoord2f(sW + txPos, tY);
				glVertex2f( SIZE, -SIZE);
				glTexCoord2f(txPos, tY);
				glVertex2f(-SIZE, -SIZE);
			glEnd();
		glPopMatrix();
		
		return this;
	}

	@Override
	public IForma update() {
		frame += 12/60.0;
		if(frame >= MAX_FRAMES) {
			EntityManager.getInstance().remove(this); //Remove do jogo
		}
		return this;
	}

	@Override
	public int getDepth() {
		return 1;
	}
}
