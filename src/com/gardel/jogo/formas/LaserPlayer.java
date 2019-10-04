package com.gardel.jogo.formas;

import static org.lwjgl.opengl.GL11.*;

import com.gardel.jogo.collision.Collidable;
import com.gardel.jogo.manager.EntityManager;
import com.gardel.jogo.texture.Texture;

public class LaserPlayer extends Collidable implements IForma{

	private static final int SIZE = 40;
	
	private static final float tX = 129 * Texture.pxFactor;
	private static final float tY = 0;
	
	private static final float sW = tX + 32 * Texture.pxFactor;
	private static final float sH = 32 * Texture.pxFactor;
	
	private float vy = -10f;
	
	public LaserPlayer(float x, float y) {
		this.x = x;
		this.y = y;
		setRaio(20);
	}
	
	@Override
	public IForma render() {
		glColor3f(0, 0, 1);
		glPushMatrix();
			glTranslatef(x+3, y, 0);
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
		this.y += vy;
		if(this.y + SIZE < 0) {
			EntityManager.getInstance().remove(this);
		}
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

	public void onCollideWith(Collidable c) {
		if(c instanceof Asteroide || c instanceof NaveInimiga) {
			EntityManager.getInstance().remove((IForma)c);
			EntityManager.getInstance().remove(this);
			EntityManager.getInstance().add(new Explosao(c.getX(), c.getY()));
		} else if(c instanceof EsferaChefao) {
			//TODO: Checar invencibilidade e diminuir vida do chefao
		}
	}
}
