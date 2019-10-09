package com.gardel.jogo.manager;

import java.util.Vector;

import com.gardel.jogo.formas.IForma;
import com.gardel.jogo.renderer.Layer;

public class RenderManager {
	private static RenderManager instance;

	public static RenderManager getInstance() {
		if (instance == null) {
			instance = new RenderManager();
		}
		return instance;
	}
	
	private Vector<Layer> layers = new Vector<Layer>();
	private Vector<Integer> layers_z = new Vector<Integer>();
	
	public void add(IForma forma) {
		int indexOfDepth = layers_z.indexOf(forma.getDepth());
		
		if (indexOfDepth == -1) {
			layers_z.add(forma.getDepth());
			layers.add(new Layer());
			zOrderLayers();
			indexOfDepth = layers_z.indexOf(forma.getDepth());
		}
		
		layers.get(indexOfDepth).add(forma);
	}
	
	public boolean remove(IForma forma) {
		int index = layers_z.indexOf(forma.getDepth());
		if (index != -1) {
			return layers.get(index).remove(forma);
		}
		return false;
	}
	
	public void clear() {
		layers.forEach(layer -> layer.clear());
	}
	
	private void zOrderLayers() {
		layers.sort((Layer l1, Layer l2) -> Integer.compare(l1.getZ_order(), l2.getZ_order()));
		layers_z.sort((Integer z1, Integer z2) -> Integer.compare(z1, z2));
	}
	
	public void updateAndRender() {
		for (int i = 0; i < layers.size(); i++) {
			layers.get(i).updateAndRender();
		}
	}
}
