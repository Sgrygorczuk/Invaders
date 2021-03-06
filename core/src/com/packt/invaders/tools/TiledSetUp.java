package com.packt.invaders.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.packt.invaders.Const.WORLD_HEIGHT;
import static com.packt.invaders.Const.WORLD_WIDTH;


public class TiledSetUp {

    //================================= Variables ==================================================
    SpriteBatch batch;
    Camera camera;
    Viewport viewport;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    //====================================== Constructor ===========================================
    public TiledSetUp(AssetManager tiledManager, SpriteBatch batch, String mapName){
        this.batch = batch;

        camera = new OrthographicCamera();                                          //Sets a 2D view
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);           //Places the camera in the center of the view port
        camera.update();                                                            //Updates the camera
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);          //Stretches the image to fit the screen
        viewport.apply();

        showTiled(tiledManager, mapName);
    }

    //========================================= Methods ============================================
    /**
     * Purpose: Collects that data necessary for drawing and extracting stuff from Tiled
     * @param tiledManager the asset manager we will pull the map from
     * @param mapName name of the map which we will refer to
     */
    private void showTiled(AssetManager tiledManager, String mapName){
        //Gets the map
        tiledMap = tiledManager.get(mapName);
        //Makes it into a drawing that we can call
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);
        //Center the drawing based on the camera
        orthogonalTiledMapRenderer.setView((OrthographicCamera) camera);

    }

    /**
     * Purpose: Allow user to get coordinates of all the objects in that layer
     * @param layerName tells us the name of the layer we want to pull from
     * @return a Vector2 of coordinates
     */
    public Array<Vector2> getLayerCoordinates(String layerName) {
        //Grab the layer from tiled map
        MapLayer mapLayer = tiledMap.getLayers().get(layerName);
        Array<Vector2> coordinates = new Array<>();

        //Grabs the coordinates for each instance of that object in the map
        for (MapObject mapObject : mapLayer.getObjects()) {
            Vector2 coordinate = new Vector2(mapObject.getProperties().get("x", Float.class),
                    mapObject.getProperties().get("y", Float.class));
            coordinates.add(coordinate);
        }
        return coordinates;
    }

    /**
     * Purpose: Allow user to get coordinates of all the objects in that layer
     * @param layerName tells us the name of the layer we want to pull from
     * @return a String
     */
    public Array<String> getLayerNames(String layerName) {
        //Grab the layer from tiled map
        MapLayer mapLayer = tiledMap.getLayers().get(layerName);
        Array<String> names = new Array<>();

        //Grabs the coordinates for each instance of that object in the map
        for (MapObject mapObject : mapLayer.getObjects()) {
            names.add(mapObject.getName());
        }
        return names;
    }

}
