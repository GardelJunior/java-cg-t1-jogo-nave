package com.gardel.jogo.formas;

import static org.lwjgl.opengl.GL11.*;

import com.gardel.jogo.collision.Collidable;
import com.gardel.jogo.manager.EntityManager;
import com.gardel.jogo.math.Mathf;
import com.gardel.jogo.sound.SoundManager;
import com.gardel.jogo.texture.Texture;

public class ChefaoEscudo implements IForma{

	private static final int SIZE = 40;
	
	private static final float tX = 65 * Texture.pxFactor;
	private static final float tY = 127 * Texture.pxFactor;
	
	private static final float sW = tX + 128 * Texture.pxFactor;
	private static final float sH = tY + 128 * Texture.pxFactor;
	
	private float alpha = 1;
	private float shake;
	
	private float x,y;
	
	public ChefaoEscudo(float x, float y, float shake) {
		this.x = x;
		this.y = y;
		this.shake = shake;
	}
	
	@Override
	public IForma render() {
		int rAng = Mathf.randomInRangei(0, 360);
		glPushMatrix();
			glColor4f(1, 1, 1,alpha);
			glTranslatef(x + Mathf.lengthdir_x(shake, rAng), y + Mathf.lengthdir_y(shake, rAng), 0);
			glScalef(5, 5, 5);
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
		if(alpha <= 0f) {
			EntityManager.getInstance().remove(this);
		}else {
			alpha *= 0.95555f;
		}
		shake *= 0.9f;
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

	@Override
	public int getDepth() {
		return 1;
	}

	public void onCollideWith(Collidable c) {
		if(c instanceof Asteroide || c instanceof NaveInimiga) {
			EntityManager.getInstance().remove((IForma)c);
			EntityManager.getInstance().remove(this);
			EntityManager.getInstance().add(new Explosao(c.getX(), c.getY()));
			SoundManager.SOUND_EXPLOSION_2.play();
		}
	}
}
