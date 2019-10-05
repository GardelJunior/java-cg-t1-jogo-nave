package com.gardel.jogo.formas;

import com.gardel.jogo.manager.TextureManager;

public interface IForma {
	public IForma render();
	public IForma update();
	
	default public int getTextureSheet() {
		return 0;
	}
	
	default public int getDepth() {
		return 0;
	}
	
	default public IForma bindTexture() {
		TextureManager.getInstance().getTexture(getTextureSheet()).bind();
		return this;
	}
}
