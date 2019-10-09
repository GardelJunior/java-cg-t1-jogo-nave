package com.gardel.jogo.states;

import com.gardel.Jogo;
import com.gardel.jogo.events.EventoRemoveForma;
import com.gardel.jogo.formas.IForma;
import com.gardel.jogo.formas.entidades.ChefaoCorpo;
import com.gardel.jogo.formas.entidades.Jogador;
import com.gardel.jogo.manager.EntityManager;
import com.gardel.jogo.manager.StateManager;
import com.gardel.jogo.sound.SoundManager;

public class EstadoChefao implements IState {
	
	private ChefaoCorpo chefao;
	
	@Override
	public void init() {
		SoundManager.MUSIC_1.stop();
		SoundManager.MUSIC_2.stop();
		SoundManager.MUSIC_3.stop();
		
		EntityManager em = EntityManager.getInstance();
		
		em.deleteObservers();
		em.addObserver(em.getJogador());
		em.addObserver((o,evento)->{
			if(evento instanceof EventoRemoveForma) onFormaRemovida(((EventoRemoveForma)evento).getForma());
		});
		
		chefao = new ChefaoCorpo(Jogo.WIDTH/2, -120);
		em.add(chefao);
		
		SoundManager.MUSIC_2.play();
	}

	@Override
	public void update() {
		
	}
	
	private void onFormaRemovida(IForma forma) {
		if(forma instanceof ChefaoCorpo) {
			StateManager.getInstance().setState("vitoria");
		}else if(forma instanceof Jogador) {
			StateManager.getInstance().setState("derrota");
		}
	}
}
