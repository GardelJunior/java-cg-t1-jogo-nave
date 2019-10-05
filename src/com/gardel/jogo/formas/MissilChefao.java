package com.gardel.jogo.formas;

import static org.lwjgl.opengl.GL11.*;

import com.gardel.Jogo;
import com.gardel.jogo.collision.Collidable;
import com.gardel.jogo.manager.EntityManager;
import com.gardel.jogo.math.Mathf;
import com.gardel.jogo.texture.Texture;

public class MissilChefao extends Collidable implements IForma {
	
	private float angle = 0;
	
	private static final float tW = 32 * Texture.pxFactor2;
	private static final float tH = 32 * Texture.pxFactor2;
	private static final float pY = 668 * Texture.pxFactor2;
	private static final int MAX_FRAMES = 3;
	private static final int SIZE = 16;
	
	private float frame = 0;
	
	public MissilChefao(float x, float y, float angle) {
		setRaio(10);
		this.x = x;
		this.y = y;
		this.angle = angle;
	}
	
	@Override
	public IForma render() {
		float txPos = tW * (int)(frame);
		glColor3f(1, 1, 1);
		glPushMatrix();
			glTranslatef(x, y, 0);
			glRotatef(-angle, 0, 0, 1);
			glBegin(GL_QUADS);
			glTexCoord2f(txPos + tW,pY + tH);
				glVertex2f(-SIZE,  SIZE);
				glTexCoord2f(txPos,pY + tH);
				glVertex2f( SIZE,  SIZE);
				glTexCoord2f(txPos,pY);
				glVertex2f( SIZE, -SIZE);
				glTexCoord2f(txPos + tW,pY);
				glVertex2f(-SIZE, -SIZE);
			glEnd();
		glPopMatrix();
		
		return this;
	}

	public IForma update() {
		
		frame += 6.0f/12.0;
		
		if(frame >= MAX_FRAMES) {
			frame = 0;
		}
		
		x += Mathf.lengthdir_x(8, angle + 270);
		y += Mathf.lengthdir_y(8, angle + 270);
		
		if(y + 10 > Jogo.HEIGHT) {
			EntityManager.getInstance().remove(this);
		}
		
		return this;
	}
	
	public int getTextureSheet() {
		return 1;
	}
}
