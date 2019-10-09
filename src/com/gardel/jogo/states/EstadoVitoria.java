package com.gardel.jogo.states;

import com.gardel.Jogo;
import com.gardel.jogo.formas.EndGameVitoria;
import com.gardel.jogo.formas.EndGameVitoria2;
import com.gardel.jogo.formas.Estrelas;
import com.gardel.jogo.formas.Planeta;
import com.gardel.jogo.formas.entidades.Jogador;
import com.gardel.jogo.manager.EntityManager;
import com.gardel.jogo.math.Mathf;

public class EstadoVitoria implements IState {

	private int animation_timer;
	private boolean planeta;
	private Jogador jogador;
	private Estrelas estrelas;
	
	@Override
	public void init() {
		EntityManager.getInstance().deleteObservers();
		
		animation_timer = 220;
		planeta = false;
		
		estrelas = EntityManager.getInstance().getEstrelas();
		jogador = EntityManager.getInstance().getJogador();
	}

	@Override
	public void update() {
		if (animation_timer > 0) {
			if(jogador.getAngle() == 180) {
				estrelas.setVelocity(Mathf.max(Mathf.abs(estrelas.getVelocity()) * -1.05f, -50));
				jogador.setyScale(Mathf.min(jogador.getyScale() * 1.001f, 3));
				animation_timer--;
			}else {
				jogador.setAngle(jogador.getAngle()+2);
			}
			jogador.setX(Mathf.lerp(jogador.getX(), Jogo.WIDTH / 2, 0.05f));
		} else if (animation_timer == 0) {
			if(jogador.getAngle() == 0) {
				animation_timer = -1;
				EntityManager.getInstance().add(new EndGameVitoria(Jogo.WIDTH / 2, Jogo.HEIGHT / 4));
				EntityManager.getInstance().add(new EndGameVitoria2(Jogo.WIDTH / 2, Jogo.HEIGHT / 2));
			}else {
				jogador.setyScale(1);
				estrelas.setVelocity(1);
				jogador.setAngle(jogador.getAngle()-2);
				if(!planeta) {
					planeta = true;
					EntityManager.getInstance().add(new Planeta(Jogo.WIDTH / 2, Jogo.HEIGHT / 2));
				}
			}
		}
	}
}
