package com.gardel.jogo.formas;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.Observable;
import java.util.Observer;

import com.gardel.Jogo;
import com.gardel.jogo.collision.Collidable;
import com.gardel.jogo.events.EventoRemoveForma;
import com.gardel.jogo.events.IKeyListener;
import com.gardel.jogo.manager.EntityManager;
import com.gardel.jogo.math.Mathf;
import com.gardel.jogo.sound.SoundManager;
import com.gardel.jogo.texture.Texture;

public class Jogador extends Collidable implements IForma,IKeyListener,Observer{
	
	private int k_left,k_right,k_space;
	private float velX = 0;
	
	private int angle = 0;
	
	private float yKnockBack = 0;
	private int available_shoots = 3;
	private float shoot_cooldown = 0;
	private float yScale = 1;
	
	private static final float sW = 32 * Texture.pxFactor;
	private static final float sH = 32 * Texture.pxFactor;
	
	private static final int MAX_FRAMES = 4;
	private float frame = 0;
	
	private static final int BOX_SIZE = 40;
	
	public Jogador(float x, float y) {
		setRaio(30);
		this.x = x;
		this.y = y;
	}
	
	@Override
	public IForma render() {
		/* Calculo da coordenada na textura para animação */
		float txPos = sW * (int)frame;
		
		/* Renderização */
		glColor3f(1, 1, 1);
		glPushMatrix();
			glTranslatef(x, y + yKnockBack, 0);
			glScalef(1, yScale, 1);
			glRotatef(angle, 0, 0, 1);
			glBegin(GL_QUADS);
				glTexCoord2f(txPos, sH);
				glVertex2f(-BOX_SIZE,  BOX_SIZE);
				glTexCoord2f(sW + txPos, sH);
				glVertex2f( BOX_SIZE,  BOX_SIZE);
				glTexCoord2f(sW + txPos, 0);
				glVertex2f( BOX_SIZE, -BOX_SIZE);
				glTexCoord2f(txPos, 0);
				glVertex2f(-BOX_SIZE, -BOX_SIZE);
			glEnd();
		glPopMatrix();
		
		return this;
	}

	@Override
	public IForma update() {
		
		/* Atualização da animação */
		frame += 12/60.0;
		if(frame >= MAX_FRAMES) {
			frame = 0;
		}
		
		/* Velocidade suave e colisão com as bordas da tela */
		velX = Mathf.clamp(velX + (k_right - k_left), -6f, 6f);
		if(x + velX - BOX_SIZE < 0 || x + velX + BOX_SIZE >= Jogo.WIDTH) {
			velX = 0;
		}
		x += velX;
		
		/* Fricção, para desacelerar ao soltar os botões */
		velX *= 0.8f;
		yKnockBack *= 0.8f;
		
		/* Resfriamento do tiro, só atira se o resfriamento = 0 e os tiros na tela < 3 */
		if(shoot_cooldown > 0) {
			shoot_cooldown -= 2;
		}else if(k_space > 0 && shoot_cooldown <= 0f && available_shoots > 0) {
			available_shoots--;
			shoot_cooldown = 60;
			yKnockBack = 4;
			SoundManager.SOUND_LASER.play();
			EntityManager.getInstance().add(new LaserPlayer(x, y - 20));
		}
		return this;
	}

	@Override
	public void onKeyEvent(int key, int action) {
		/* Evento de tecla pressionada, modifica as flags */
		switch(key) {
			case GLFW_KEY_A:
			case GLFW_KEY_LEFT: k_left = action; break;
			case GLFW_KEY_D:
			case GLFW_KEY_RIGHT: k_right = action; break;
			case GLFW_KEY_SPACE: k_space = action; break;
		}
	}
	
	@Override
	public void onCollideWith(Collidable c) {
		/* Evento de colisão */
		if(c instanceof MissilChefao || c instanceof LaserInimigo || c instanceof Asteroide ) {
			EntityManager.getInstance().remove(this);
			EntityManager.getInstance().remove((IForma)c);
			EntityManager.getInstance().add(new Explosao(x, y));
			SoundManager.SOUND_EXPLOSION_P.play();
		}
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
	public void update(Observable o, Object update) {
		// Sistema de eventos, ao adicionar um tiro ele remove do máximo de tiros que pode dar
		if(update instanceof EventoRemoveForma) {
			if(((EventoRemoveForma)update).getForma() instanceof LaserPlayer) {
				available_shoots++;
			} 
		}
	}

	public float getyScale() {
		return yScale;
	}

	public void setyScale(float yScale) {
		this.yScale = yScale;
	}

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}
}
