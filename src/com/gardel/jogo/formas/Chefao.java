package com.gardel.jogo.formas;

import static org.lwjgl.opengl.GL11.*;

import com.gardel.jogo.collision.Collidable;
import com.gardel.jogo.manager.EntityManager;
import com.gardel.jogo.math.Mathf;
import com.gardel.jogo.sound.SoundManager;
import com.gardel.jogo.texture.Texture;

public class Chefao extends Collidable implements IForma {

	private static final float tW = 128 * Texture.pxFactor2;
	private static final float tH = 160 * Texture.pxFactor2;

	private static final int MAX_FRAMES = 32;

	private static final int SIZE = 186;
	
	private final int attackFrame = 13, endAttackFrame = 28;
	
	private int vida = 30;
	
	private float frame = 0;
	private int form = 3;
	private float speed = 6;
	private int attackDelay = 0;
	private int attackMode = 0;
	
	private EsferaChefao esferaEsq,esferaDir;
	private BarraVida barravida;
	
	private int deathAnimation = 240;
	private boolean dead = false;
	
	private float shake = 0;
	
	public Chefao(float x, float y, BarraVida barravida) {
		this.x = x;
		this.y = y;
		this.esferaEsq = new EsferaChefao(this, true);
		this.esferaDir = new EsferaChefao(this, false);
		this.barravida = barravida;
		setRaio(40);
	}
	
	@Override
	public IForma render() {
		float txPos = tW * ((int)(frame) % 8 );
		float tyPos = tH * ((int)(frame) / 8 );
		int rAng = Mathf.randomInRangei(0, 360);
		glColor3f(1, 1, 1);
		glPushMatrix();
			glTranslatef(x + Mathf.lengthdir_x(shake, rAng), y + Mathf.lengthdir_y(shake, rAng), 0);
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
		frame += speed/60.0;
		if(frame >= MAX_FRAMES) {
			frame = 0;
		}
		shake *= 0.9f;
		if(frame >= attackFrame && frame <= endAttackFrame) {
			if(attackDelay == 0) {
				if(form > 1) {
					Jogador j = EntityManager.getInstance().getJogador();
					attackDelay = (vida >= 10 && vida <= 20)? 20 : 45;
					int angle = Mathf.angle_between_points(x, y + 90, j.getX(), j.getY());
					EntityManager.getInstance().add(new MissilChefao(x, y + 90,angle + 90));
				}else {
					attackDelay = 30;
					if(attackMode == 0) {
						EntityManager.getInstance().add(new MissilChefao(x, y + 90,0));
						EntityManager.getInstance().add(new MissilChefao(x, y + 90,315));
						EntityManager.getInstance().add(new MissilChefao(x, y + 90,45));
						attackMode = 1;
					}else {
						EntityManager.getInstance().add(new MissilChefao(x, y + 90,10));
						EntityManager.getInstance().add(new MissilChefao(x, y + 90,45));
						EntityManager.getInstance().add(new MissilChefao(x, y + 90,315));
						EntityManager.getInstance().add(new MissilChefao(x, y + 90,350));
						attackMode = 0;
					}
				}
				SoundManager.SOUND_ATTACK.play();
			}else {
				attackDelay--;
			}
		}else {
			if(attackDelay > 0) attackDelay = 0;
		}
		
		if(vida == 0 && !dead) {
			frame = 0;
			speed = 0;
			dead = true;
			barravida.setVisible(false);
		}
		
		if(dead && deathAnimation > 0) {
			if(deathAnimation % 30 == 0) {
				int randomAngle = Mathf.randomInRangei(0, 360);
				float distance = Mathf.randomInRangef(20, 100);
				float dx = x + Mathf.lengthdir_x(distance, randomAngle);
				float dy = y + Mathf.lengthdir_y(distance, randomAngle);
				shake = 6;
				EntityManager.getInstance().add(new Explosao(dx, dy,1.4f));
				SoundManager.SOUND_EXPLOSION_2.play();
			}
			deathAnimation--;
			if(deathAnimation == 0) {
				EntityManager.getInstance().add(new Explosao(x, y,6));
				EntityManager.getInstance().remove(this);
				SoundManager.SOUND_EXPLOSION_BIG.play();
			}
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
	
	public void nextForm() {
		form--;
	}

	@Override
	public boolean isColliding(Collidable collidable) {
		return esferaDir.isColliding(collidable) || 
				esferaEsq.isColliding(collidable) || 
				(super.isColliding(collidable) && esferaDir.destruida && esferaEsq.destruida);
	}
	
	@Override
	public void onCollideWith(Collidable c) {
		if(c.isColliding(esferaDir)) {
			esferaDir.onCollideWith(c);
		}else if(c.isColliding(esferaEsq)){
			esferaEsq.onCollideWith(c);
		}else {
			if(frame >= attackFrame && frame <= endAttackFrame && vida>0) {
				shake = 3;
				SoundManager.SOUND_HIT.play();
				EntityManager.getInstance().remove((IForma)c);
				removerVida();
			}else {
				EntityManager.getInstance().remove((IForma)c);
			}
		}
	}

	public void removerVida() {
		this.vida--;
		this.barravida.setCurrent(vida);
	}
}
