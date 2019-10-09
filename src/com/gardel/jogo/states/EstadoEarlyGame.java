package com.gardel.jogo.states;

import com.gardel.Jogo;
import com.gardel.jogo.events.EventoRemoveForma;
import com.gardel.jogo.formas.IForma;
import com.gardel.jogo.formas.entidades.Asteroide;
import com.gardel.jogo.formas.entidades.Jogador;
import com.gardel.jogo.formas.entidades.NaveInimiga;
import com.gardel.jogo.manager.EntityManager;
import com.gardel.jogo.manager.StateManager;
import com.gardel.jogo.math.Mathf;
import com.gardel.jogo.sound.SoundManager;

public class EstadoEarlyGame implements IState{

	private int earyGameTimer;
	private int entity_spawn_interval;
	private int enemies_on_screen;
	
	@Override
	public void init() {
		
		earyGameTimer = 3200;
		entity_spawn_interval = 90;
		enemies_on_screen = 0;
		
		SoundManager.MUSIC_1.stop();
		SoundManager.MUSIC_2.stop();
		SoundManager.MUSIC_3.stop();
		
		EntityManager em = EntityManager.getInstance();
		
		em.clear();
		em.addObserver(em.getJogador());
		
		em.add(EntityManager.getInstance().getJogador());
		
		em.addObserver((o,action) -> {
			if(action instanceof EventoRemoveForma) onFormaRemovida(((EventoRemoveForma)action).getForma());
		});
		
		SoundManager.MUSIC_1.play();
	}

	@Override
	public void update() {
		if (entity_spawn_interval == 0) {
			entity_spawn_interval = 60;
			if (Mathf.randomInRangef(0, 100) < 50 && earyGameTimer > 0) {
				if (Mathf.randomInRangef(0, 100) < 50) {
					EntityManager.getInstance().add(new NaveInimiga(Mathf.randomInRangef(50, Jogo.WIDTH - 50), -20));
				} else {
					EntityManager.getInstance().add(new Asteroide(Mathf.randomInRangef(50, Jogo.WIDTH - 50), -20));
				}
				enemies_on_screen++;
			}
		} else {
			entity_spawn_interval--;
		}
		if (earyGameTimer == 0) {
			if(enemies_on_screen == 0) {
				StateManager.getInstance().setState("chefao");
			}
		}else {
			earyGameTimer--;
		}
	}

	private void onFormaRemovida(IForma forma) {
		if(forma instanceof NaveInimiga || forma instanceof Asteroide) {
			enemies_on_screen--;
		}else if(forma instanceof Jogador) {
			StateManager.getInstance().setState("derrota");
		}
	}
}
