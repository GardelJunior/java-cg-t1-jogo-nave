package com.gardel.jogo.formas;

import static org.lwjgl.opengl.GL11.*;

import com.gardel.jogo.collision.Collidable;
import com.gardel.jogo.manager.EntityManager;
import com.gardel.jogo.math.Mathf;
import com.gardel.jogo.texture.Texture;

public class EsferaChefao extends Collidable implements IForma {
	
	private static final float tW = 26 * Texture.pxFactor2;
	private static final float tH = 26 * Texture.pxFactor2;
	private static final float pY = 641 * Texture.pxFactor2;
	private static final int MAX_FRAMES = 8;
	private static final int SIZE = 48;
	
	private float vida = 10;
	
	private float speed = 6;
	
	private float xScale = 1;
	
	private float frame = 0;
	
	private int deathAnimation = 240;
	
	private Chefao chefao;
	
	private float shake = 0;
	
	public boolean destruida = false;
	
	public EsferaChefao(Chefao chefao, boolean esq) {
		setRaio(20);
		this.chefao = chefao;
		if(esq) {
			this.x = chefao.getX() - Chefao.getSize();
			this.xScale = -1;
		}else {
			this.x = chefao.getX() + Chefao.getSize();
		}
		this.y = chefao.getY() + SIZE/2;
	}
	
	@Override
	public IForma render() {
		if(destruida && deathAnimation <= 0) return this;
		float txPos = tW * (int)(frame);
		
		float percVida = 1 - vida/10.0f;
		
		int rAng = Mathf.randomInRangei(0, 360);
		
		glColor3f(1, 1 - percVida, 1-percVida);
		glPushMatrix();
			glTranslatef(x + Mathf.lengthdir_x(shake, rAng), y + Mathf.lengthdir_y(shake, rAng), 0);
			glScalef(xScale, 1, 1);
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
		
		frame += speed/120.0;
		
		shake *= 0.9f;
		
		if(frame >= MAX_FRAMES) {
			frame = 0;
		}
		
		this.y = chefao.getY() + SIZE/2;
		
		if(destruida && deathAnimation > 0) {
			if(deathAnimation % 30 == 0) {
				int randomAngle = Mathf.randomInRangei(0, 360);
				float distance = Mathf.randomInRangef(0, 20);
				float dx = x + Mathf.lengthdir_x(distance, randomAngle);
				float dy = y + Mathf.lengthdir_y(distance, randomAngle);
				shake = 6;
				EntityManager.getInstance().add(new Explosao(dx, dy,0.6f));
			}
			
			deathAnimation--;
			
			if(deathAnimation == 0) {
				EntityManager.getInstance().add(new Explosao(x, y,2));
				chefao.setVida(chefao.getVida()-1);
			}
		}
		
		return this;
	}
	
	@Override
	public void onCollideWith(Collidable c) {
		if(c instanceof LaserPlayer) {
			if(vida <= 0 && !destruida) {
				speed = 0;
				destruida = true;
			}else if(!destruida){
				vida--;
				chefao.setVida(chefao.getVida()-1);
				shake = 4;
				EntityManager.getInstance().remove((IForma)c);
			}
		}
	}

	public int getTextureSheet() {
		return 1;
	}
}
