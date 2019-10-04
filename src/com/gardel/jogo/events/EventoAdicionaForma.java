package com.gardel.jogo.events;

import com.gardel.jogo.formas.IForma;

public class EventoAdicionaForma {
	
	private IForma forma;

	public EventoAdicionaForma(IForma forma) {
		this.forma = forma;
	}

	public IForma getForma() {
		return forma;
	}
	
}
