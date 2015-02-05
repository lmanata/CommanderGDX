package com.afonsobordado.CommanderGDX.handlers;

import java.util.Iterator;

import com.afonsobordado.CommanderGDX.vars.B2DVars;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public abstract class TiledMapImporter {
	private static float tileSize;
	public static void create(TiledMap tm, World world){
		tileSize = (int) tm.getProperties().get("tilewidth");
		
		for(int i =0; i < tm.getLayers().getCount(); i++){
			MapLayer l = tm.getLayers().get(i);
			if(l.getProperties().get("object") == null){
				TiledMapTileLayer layer = (TiledMapTileLayer) l;
				if(tm.getLayers().get(i).getProperties().containsKey("ground")){
					createLayer(layer, B2DVars.BIT_GROUND, world);
				}else{
					createLayer(layer, (short) 0, world);
				}
				
			}else{
				colisionLayer(l,B2DVars.BIT_GROUND,world);
			}

		}
	}
	

	private static void colisionLayer(MapLayer layer, short bits, World world){
		FixtureDef fdef = new FixtureDef();
		fdef.filter.categoryBits = bits; 
		fdef.filter.maskBits = B2DVars.BIT_PLAYER;
        fdef.density = 1.0f;
        fdef.friction = 0.8f;
        fdef.restitution = 0.0f;
	 	
	    Iterator<MapObject> iterator = layer.getObjects().iterator();
	    while (iterator.hasNext()){
	    	BodyDef bdef  = new BodyDef();
	    	bdef.type = BodyDef.BodyType.StaticBody;
	    	MapObject object = iterator.next();
	        Shape shape = null; 
	        if (object instanceof PolylineMapObject){
	           PolylineMapObject poly = (PolylineMapObject)object;
	 	      float[] vertices = poly.getPolyline().getTransformedVertices();
		      Vector2[] worldVertices = new Vector2[vertices.length / 2];
		   
		      for (int i = 0; i < vertices.length / 2; ++i){
		         worldVertices[i] = new Vector2();
		         worldVertices[i].x = vertices[i * 2] / B2DVars.PPM;
		         worldVertices[i].y = vertices[i * 2 + 1] / B2DVars.PPM;
		      }
		   
		      ChainShape chain = new ChainShape();
		      chain.createChain(worldVertices);
		      shape = chain;
	        }
	        
	        if(object instanceof RectangleMapObject){
	        		RectangleMapObject rect = (RectangleMapObject) object;
	        		bdef.position.set((rect.getRectangle().x + (rect.getRectangle().width / 2)) / B2DVars.PPM,
	        						  (rect.getRectangle().y + (rect.getRectangle().height / 2)) / B2DVars.PPM);
	        		ChainShape cs = new ChainShape();
					Vector2[] v = new Vector2[4];
					v[0] = new Vector2(
							-rect.getRectangle().width / 2 / B2DVars.PPM,
							-rect.getRectangle().height / 2 / B2DVars.PPM);
					v[1] = new Vector2(
							-rect.getRectangle().width / 2 / B2DVars.PPM,
							rect.getRectangle().height / 2 / B2DVars.PPM);
					v[2] = new Vector2(
							rect.getRectangle().width / 2 / B2DVars.PPM,
							rect.getRectangle().height / 2 / B2DVars.PPM);
					v[3] = new Vector2(
							rect.getRectangle().width / 2 / B2DVars.PPM,
							-rect.getRectangle().height / 2 / B2DVars.PPM);
					cs.createLoop(v);
					shape = cs;
	        		
	        }
	        
	        if(object instanceof PolygonMapObject){
				PolygonMapObject poly = (PolygonMapObject) object;
				float[] vertices = poly.getPolygon().getTransformedVertices();
				Vector2[] worldVertices = new Vector2[vertices.length / 2];
					   
				for (int i = 0; i < vertices.length / 2; ++i){
					worldVertices[i] = new Vector2();
					worldVertices[i].x = vertices[i * 2] / B2DVars.PPM;
					worldVertices[i].y = vertices[i * 2 + 1] / B2DVars.PPM;
				}
   
				ChainShape chain = new ChainShape();
				chain.createLoop(worldVertices);
				shape = chain;
	        }
	        
	        if(object instanceof EllipseMapObject){
	        	EllipseMapObject emo = (EllipseMapObject) object;
	        	if(emo.getEllipse().height == emo.getEllipse().width){
	        		System.out.println("got Circled");
		        	CircleShape cs = new CircleShape();
		        	cs.setRadius(emo.getEllipse().height / 2 / B2DVars.PPM);
		        	cs.setPosition(new Vector2(emo.getEllipse().x / B2DVars.PPM,
		        						       emo.getEllipse().y / B2DVars.PPM));
		        	shape = cs;
	        	}
	        	
	        	
	        }
	        
	        if(shape != null){
	        	fdef.shape = shape;
	            world.createBody(bdef).createFixture(fdef);
	            fdef.shape = null;
	            shape.dispose();
	        }
	    }
	}
	private static void createLayer(TiledMapTileLayer layer, short bits, World world){
		//go trough all cells in the layer
		BodyDef bdef  = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		
			for(int row = 0; row < layer.getHeight(); row++){
				for(int col = 0;col < layer.getWidth(); col++){
					
					//get cell
					Cell cell = layer.getCell(col, row);
					//check for a valid tile
					if(cell == null || cell.getTile() == null) continue;
					//create cell body
					bdef.type = BodyType.StaticBody;
					bdef.position.set(
							(col + 0.5f) * tileSize / B2DVars.PPM,
							(row + 0.5f) * tileSize / B2DVars.PPM); 
					
					
					ChainShape cs = new ChainShape();
					Vector2[] v = new Vector2[4];
					v[0] = new Vector2(
							-tileSize / 2 / B2DVars.PPM,
							-tileSize / 2 / B2DVars.PPM);
					v[1] = new Vector2(
							-tileSize / 2 / B2DVars.PPM,
							tileSize / 2 / B2DVars.PPM);
					v[2] = new Vector2(
							tileSize / 2 / B2DVars.PPM,
							tileSize / 2 / B2DVars.PPM);
					v[3] = new Vector2(
							tileSize / 2 / B2DVars.PPM,
							-tileSize / 2 / B2DVars.PPM);

					cs.createLoop(v);
					fdef.friction = 0.2f;
					fdef.shape = cs;
					fdef.filter.categoryBits = bits; 
					fdef.filter.maskBits = B2DVars.BIT_PLAYER; 
					fdef.isSensor = false;
					world.createBody(bdef).createFixture(fdef);
					
					
				}
			}
			
	}
	
}
