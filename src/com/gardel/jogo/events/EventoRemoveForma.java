package com.gardel.jogo.events;

import com.gardel.jogo.formas.IForma;

public class EventoRemoveForma {
	
	private IForma forma;

	public EventoRemoveForma(IForma forma) {
		this.forma = forma;
	}

	public IForma getForma() {
		return forma;
	}
}
