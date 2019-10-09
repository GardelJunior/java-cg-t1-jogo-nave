package com.gardel.jogo.states;

import org.lwjgl.glfw.GLFW;

import com.gardel.Jogo;
import com.gardel.jogo.events.IKeyListener;
import com.gardel.jogo.formas.EndGameDerrota;
import com.gardel.jogo.formas.Planeta;
import com.gardel.jogo.formas.entidades.Explosao;
import com.gardel.jogo.manager.EntityManager;
import com.gardel.jogo.manager.StateManager;
import com.gardel.jogo.sound.SoundManager;

public class EstadoDerrota implements IState,IKeyListener {

	private int animationTimer;
	private Planeta planeta;
	private EntityManager entityManager;
	
	@Override
	public void init() {
		animationTimer = 220;
		entityManager = EntityManager.getInstance();
	}

	@Override
	public void update() {
		if(animationTimer >= 0) {
			if(animationTimer == 180 ) {
				entityManager.clear();
			} else if(animationTimer == 120) {
				planeta = new Planeta(Jogo.WIDTH/2, Jogo.HEIGHT/2);
				entityManager.add(planeta);
			} else if(animationTimer == 80) {
				entityManager.add(new Explosao(Jogo.WIDTH/2, Jogo.HEIGHT/2,4));
				entityManager.remove(planeta);
				planeta = null;
				SoundManager.SOUND_EXPLOSION_BIG.play();
			} else if(animationTimer == 30) {
				entityManager.add(new EndGameDerrota(Jogo.WIDTH/2, Jogo.HEIGHT/2));
			}
			animationTimer--;
		}
	}

	@Override
	public void onKeyEvent(int key, int action) {
		if(key == GLFW.GLFW_KEY_SPACE && action > 0) {
			if(animationTimer <= 0) {
				entityManager.getJogador().setX(Jogo.WIDTH/2);
				StateManager.getInstance().setState("inicial");
			}
		}
	}
}
