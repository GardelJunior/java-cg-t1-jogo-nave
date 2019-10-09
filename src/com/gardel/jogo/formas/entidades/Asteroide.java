package com.gardel.jogo.formas.entidades;

import static org.lwjgl.opengl.GL11.*;

import com.gardel.jogo.collision.Collidable;
import com.gardel.jogo.formas.IForma;
import com.gardel.jogo.manager.EntityManager;
import com.gardel.jogo.math.Mathf;
import com.gardel.jogo.sound.SoundManager;
import com.gardel.jogo.texture.Texture;

public class Asteroide extends Collidable implements IForma {

	private float rotation;

	private static final float tX = 0;
	private static final float tY = 326 * Texture.pxFactor;

	private static final float sW = tX + 195 * Texture.pxFactor;
	private static final float sH = tY + 186 * Texture.pxFactor;

	private float yVel = 0;

	private static final int SIZE = 40;

	public Asteroide(float x, float y) {
		this.x = x;
		this.y = y;
		this.rotation = (float) (Math.random() * 360);
		this.yVel = Mathf.randomVal(2, 3, 4);
		setRaio(20);
	}

	@Override
	public IForma render() {
		glColor3f(1, 1, 1);
		glPushMatrix();
			glTranslatef(x, y, 0);
			glRotatef(rotation += yVel, 0, 0, 1);
			glBegin(GL_QUADS);
				glTexCoord2f(0, sH);
				glVertex2f(-SIZE, SIZE);
				glTexCoord2f(sW, sH);
				glVertex2f(SIZE, SIZE);
				glTexCoord2f(sW, tY);
				glVertex2f(SIZE, -SIZE);
				glTexCoord2f(0, tY);
				glVertex2f(-SIZE, -SIZE);
			glEnd();
		glPopMatrix();

		return this;
	}

	@Override
	public IForma update() {
		y += yVel;
		return this;
	}

	@Override
	public void onCollideWith(Collidable c) {
		if (c instanceof NaveInimiga) {
			EntityManager.getInstance().remove((IForma) c);
			EntityManager.getInstance().remove(this);
			EntityManager.getInstance().add(new Explosao(c.getX(), c.getY()));
			SoundManager.SOUND_EXPLOSION_2.play();
		}
	}

}
