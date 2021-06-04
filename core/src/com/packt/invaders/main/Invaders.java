package com.packt.invaders.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.packt.invaders.screens.LoadingScreen;

public class Invaders extends Game {

	//Holds the UI images and sound files
	private final AssetManager assetManager = new AssetManager();

	/**
	 * Purpose: Tells the game what controls/information it should provide
	 */
	public Invaders(){}

	/**
	 * 	Purpose: Returns asset manager with all its data
	 * 	Output: Asset Manager
	 */
	public AssetManager getAssetManager() { return assetManager; }

	/**
	 Purpose: Starts the app
	 */
	@Override
	public void create () {
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		setScreen(new LoadingScreen(this));
	}

}
