package com.gardel.jogo.renderer;

import java.util.Vector;

import com.gardel.jogo.formas.IForma;

public class Layer {
	
	private int z_order = 0;
	
	private Vector<IForma> formas = new Vector<IForma>();
	
	public boolean remove(IForma f) {
		return formas.remove(f);
	}
	
	public void clear() {
		formas.clear();
	}
	
	public void add(IForma f) {
		formas.add(f);
		formas.sort((IForma f1, IForma f2) -> f1.getTextureSheet()-f2.getTextureSheet());
	}
	
	public void updateAndRender() {
		for(int i = 0 ; i < formas.size() ; i++) {
			formas.get(i).update().bindTexture().render();
		}
	}

	public int getZ_order() {
		return z_order;
	}

	public void setZ_order(int z_order) {
		this.z_order = z_order;
	}
}
