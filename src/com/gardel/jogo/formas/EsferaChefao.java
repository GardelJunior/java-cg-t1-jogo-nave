package com.gardel.jogo.formas;

import static org.lwjgl.opengl.GL11.*;

import com.gardel.jogo.collision.Collidable;
import com.gardel.jogo.texture.Texture;

public class EsferaChefao extends Collidable implements IForma {
	
	private static final float tW = 26 * Texture.pxFactor2;
	private static final float tH = 26 * Texture.pxFactor2;
	private static final float pY = 641 * Texture.pxFactor2;
	private static final int MAX_FRAMES = 8;
	private static final int SIZE = 48;
	
	private float vida = 20;
	
	private float speed = 120.0f;
	
	private float xScale = 1;
	
	private float frame = 0;
	
	private Chefao chefao;
	
	public EsferaChefao(Chefao chefao, boolean esq) {
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
		float txPos = tW * (int)(frame);
		float percVida = 1 - vida/20.0f;
		glColor3f(1, 1 - percVida, 1-percVida);
		glPushMatrix();
			glTranslatef(x, y, 0);
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
		
		frame += 6.0f/speed;
		
		if(frame >= MAX_FRAMES) {
			frame = 0;
		}
		
		this.y = chefao.getY() + SIZE/2;
		
		return this;
	}

	public int getTextureSheet() {
		return 1;
	}
}
