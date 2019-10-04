package com.gardel.jogo.formas;

public interface IForma {
	public IForma render();
	public IForma update();
	
	default public int getTextureSheet() {
		return 0;
	}
}
