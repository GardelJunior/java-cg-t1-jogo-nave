package com.gardel.jogo.formas.entidades;

import static org.lwjgl.opengl.GL11.*;

import com.gardel.Jogo;
import com.gardel.jogo.collision.Collidable;
import com.gardel.jogo.formas.IForma;
import com.gardel.jogo.manager.EntityManager;
import com.gardel.jogo.math.Mathf;
import com.gardel.jogo.sound.SoundManager;
import com.gardel.jogo.texture.Texture;

public class NaveInimiga extends Collidable implements IForma{
	
	private static final float tW = 60 * Texture.pxFactor2;
	private static final float tH = 60 * Texture.pxFactor2;
	private static final float pY = 701 * Texture.pxFactor2;
	private static final int MAX_FRAMES = 4;
	
	private static final int SIZE = 50;
	
	private float dx = 0;
	private float frame = 0;
	
	private int shoot_delay = 0;
	
	public NaveInimiga(float x, float y) {
		this.x = x;
		this.y = y;
		this.dx = Mathf.randomVal(-2f,2f);
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
		y+=2;
		if(x + dx - SIZE < 0 || x + dx + SIZE > Jogo.WIDTH) {
			dx = -dx;
		}
		x+=dx;
		frame += 6.0f/18.0;
		
		if(frame >= MAX_FRAMES) {
			frame = 0;
		}
		
		float px = EntityManager.getInstance().getJogador().getX();
		
		if(x > px - SIZE && x <= px + SIZE) {
			if(shoot_delay == 0) {
				shoot_delay = 120;
				SoundManager.SOUND_LASER.play();
				EntityManager.getInstance().add(new LaserInimigo(x, y + SIZE));
			}
		}
		
		if(shoot_delay > 0) {
			shoot_delay--;
		}
		
		if(this.y - SIZE > Jogo.HEIGHT) {
			EntityManager.getInstance().remove(this);
		}
		
		return this;
	}
	
	public int getTextureSheet() {
		return 1;
	}

	@Override
	public void onCollideWith(Collidable c) {
		if(c instanceof NaveInimiga) {
			EntityManager.getInstance().remove((IForma) c);
			EntityManager.getInstance().remove(this);
			EntityManager.getInstance().add(new Explosao(c.getX(), c.getY()));
			EntityManager.getInstance().add(new Explosao(x, y));
			SoundManager.SOUND_EXPLOSION_2.play();
		}
	}
}
