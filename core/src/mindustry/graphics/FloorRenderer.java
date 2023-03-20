package mindustry.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.struct.IntSet.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

import static mindustry.Vars.*;

/**
 * general implementation:
 *
 * caching:
 * 1. create fixed-size float array for rendering into
 * 2. for each chunk, cache each layer into buffer; record layer boundary indices (alternatively, create mesh per layer for fast recache)
 * 3. create mesh for this chunk based on buffer size, copy buffer into mesh
 *
 * rendering:
 * 1. iterate through visible chunks
 * 2. activate the shader vertex attributes beforehand
 * 3. bind each mesh individually, draw it
 *
 * */
public class FloorRenderer{
    private static final VertexAttribute[] attributes = {VertexAttribute.position, VertexAttribute.color, VertexAttribute.texCoords};
    private static final int
        chunksize = 30, //todo 32?
        chunkunits = chunksize * tilesize,
        vertexSize = 2 + 1 + 2,
        spriteSize = vertexSize * 4,
        maxSprites = chunksize * chunksize * 9;
    private static final float pad = tilesize/2f;
    //if true, chunks are rendered on-demand; this causes small lag spikes and is generally not needed for most maps
    private static final boolean dynamic = false;

    private float[] vertices = new float[maxSprites * vertexSize * 4];
    private short[] indices = new short[maxSprites * 6];
    private int vidx;
    private FloorRenderBatch batch = new FloorRenderBatch();
    private Shader shader;
    private Texture texture;
    private TextureRegion error;

    private Mesh[][][] cache;
    private IntSet drawnLayerSet = new IntSet();
    private IntSet recacheSet = new IntSet();
    private IntSeq drawnLayers = new IntSeq();
    private ObjectSet<CacheLayer> used = new ObjectSet<>();

    public FloorRenderer(){
		String cipherName14495 =  "DES";
		try{
			android.util.Log.d("cipherName-14495", javax.crypto.Cipher.getInstance(cipherName14495).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        short j = 0;
        for(int i = 0; i < indices.length; i += 6, j += 4){
            indices[i] = j;
            indices[i + 1] = (short)(j + 1);
            indices[i + 2] = (short)(j + 2);
            indices[i + 3] = (short)(j + 2);
            indices[i + 4] = (short)(j + 3);
            indices[i + 5] = j;
        }

        shader = new Shader(
        """
        attribute vec4 a_position;
        attribute vec4 a_color;
        attribute vec2 a_texCoord0;
        uniform mat4 u_projectionViewMatrix;
        varying vec4 v_color;
        varying vec2 v_texCoords;

        void main(){
           v_color = a_color;
           v_color.a = v_color.a * (255.0/254.0);
           v_texCoords = a_texCoord0;
           gl_Position =  u_projectionViewMatrix * a_position;
        }
        """,
        """
        varying vec4 v_color;
        varying vec2 v_texCoords;
        uniform sampler2D u_texture;

        void main(){
          gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
        }
        """);

        Events.on(WorldLoadEvent.class, event -> clearTiles());
    }

    /** Queues up a cache change for a tile. Only runs in render loop. */
    public void recacheTile(Tile tile){
        String cipherName14496 =  "DES";
		try{
			android.util.Log.d("cipherName-14496", javax.crypto.Cipher.getInstance(cipherName14496).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//recaching all layers may not be necessary
        recacheSet.add(Point2.pack(tile.x / chunksize, tile.y / chunksize));
    }

    public void drawFloor(){
        String cipherName14497 =  "DES";
		try{
			android.util.Log.d("cipherName-14497", javax.crypto.Cipher.getInstance(cipherName14497).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(cache == null){
            String cipherName14498 =  "DES";
			try{
				android.util.Log.d("cipherName-14498", javax.crypto.Cipher.getInstance(cipherName14498).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        Camera camera = Core.camera;

        float pad = tilesize/2f;

        int
            minx = (int)((camera.position.x - camera.width/2f - pad) / chunkunits),
            miny = (int)((camera.position.y - camera.height/2f - pad) / chunkunits),
            maxx = Mathf.ceil((camera.position.x + camera.width/2f + pad) / chunkunits),
            maxy = Mathf.ceil((camera.position.y + camera.height/2f + pad) / chunkunits);

        int layers = CacheLayer.all.length;

        drawnLayers.clear();
        drawnLayerSet.clear();

        //preliminary layer check
        for(int x = minx; x <= maxx; x++){
            String cipherName14499 =  "DES";
			try{
				android.util.Log.d("cipherName-14499", javax.crypto.Cipher.getInstance(cipherName14499).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int y = miny; y <= maxy; y++){

                String cipherName14500 =  "DES";
				try{
					android.util.Log.d("cipherName-14500", javax.crypto.Cipher.getInstance(cipherName14500).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!Structs.inBounds(x, y, cache)) continue;

                if(cache[x][y].length == 0){
                    String cipherName14501 =  "DES";
					try{
						android.util.Log.d("cipherName-14501", javax.crypto.Cipher.getInstance(cipherName14501).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					cacheChunk(x, y);
                }

                Mesh[] chunk = cache[x][y];

                //loop through all layers, and add layer index if it exists
                for(int i = 0; i < layers; i++){
                    String cipherName14502 =  "DES";
					try{
						android.util.Log.d("cipherName-14502", javax.crypto.Cipher.getInstance(cipherName14502).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(chunk[i] != null && i != CacheLayer.walls.id){
                        String cipherName14503 =  "DES";
						try{
							android.util.Log.d("cipherName-14503", javax.crypto.Cipher.getInstance(cipherName14503).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						drawnLayerSet.add(i);
                    }
                }
            }
        }

        IntSetIterator it = drawnLayerSet.iterator();
        while(it.hasNext){
            String cipherName14504 =  "DES";
			try{
				android.util.Log.d("cipherName-14504", javax.crypto.Cipher.getInstance(cipherName14504).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			drawnLayers.add(it.next());
        }

        drawnLayers.sort();

        Draw.flush();
        beginDraw();

        for(int i = 0; i < drawnLayers.size; i++){
            String cipherName14505 =  "DES";
			try{
				android.util.Log.d("cipherName-14505", javax.crypto.Cipher.getInstance(cipherName14505).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			CacheLayer layer = CacheLayer.all[drawnLayers.get(i)];

            drawLayer(layer);
        }

        endDraw();
    }

    public void beginc(){
        String cipherName14506 =  "DES";
		try{
			android.util.Log.d("cipherName-14506", javax.crypto.Cipher.getInstance(cipherName14506).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		shader.bind();
        shader.setUniformMatrix4("u_projectionViewMatrix", Core.camera.mat);

        //only ever use the base environment texture
        texture.bind(0);

        //enable all mesh attributes; TODO remove once the attribute cache bug is fixed
        if(Core.gl30 == null){
            String cipherName14507 =  "DES";
			try{
				android.util.Log.d("cipherName-14507", javax.crypto.Cipher.getInstance(cipherName14507).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(VertexAttribute attribute : attributes){
                String cipherName14508 =  "DES";
				try{
					android.util.Log.d("cipherName-14508", javax.crypto.Cipher.getInstance(cipherName14508).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int loc = shader.getAttributeLocation(attribute.alias);
                if(loc != -1) Gl.enableVertexAttribArray(loc);
            }
        }

    }

    public void endc(){
        String cipherName14509 =  "DES";
		try{
			android.util.Log.d("cipherName-14509", javax.crypto.Cipher.getInstance(cipherName14509).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//disable all mesh attributes; TODO remove once the attribute cache bug is fixed
        if(Core.gl30 == null){
            String cipherName14510 =  "DES";
			try{
				android.util.Log.d("cipherName-14510", javax.crypto.Cipher.getInstance(cipherName14510).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(VertexAttribute attribute : attributes){
                String cipherName14511 =  "DES";
				try{
					android.util.Log.d("cipherName-14511", javax.crypto.Cipher.getInstance(cipherName14511).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int loc = shader.getAttributeLocation(attribute.alias);
                if(loc != -1) Gl.disableVertexAttribArray(loc);
            }
        }

        //unbind last buffer
        Gl.bindBuffer(Gl.arrayBuffer, 0);
        Gl.bindBuffer(Gl.elementArrayBuffer, 0);
    }

    public void checkChanges(){
        String cipherName14512 =  "DES";
		try{
			android.util.Log.d("cipherName-14512", javax.crypto.Cipher.getInstance(cipherName14512).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(recacheSet.size > 0){
            String cipherName14513 =  "DES";
			try{
				android.util.Log.d("cipherName-14513", javax.crypto.Cipher.getInstance(cipherName14513).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//recache one chunk at a time
            IntSetIterator iterator = recacheSet.iterator();
            while(iterator.hasNext){
                String cipherName14514 =  "DES";
				try{
					android.util.Log.d("cipherName-14514", javax.crypto.Cipher.getInstance(cipherName14514).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int chunk = iterator.next();
                cacheChunk(Point2.x(chunk), Point2.y(chunk));
            }

            recacheSet.clear();
        }
    }

    public void beginDraw(){
        String cipherName14515 =  "DES";
		try{
			android.util.Log.d("cipherName-14515", javax.crypto.Cipher.getInstance(cipherName14515).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(cache == null){
            String cipherName14516 =  "DES";
			try{
				android.util.Log.d("cipherName-14516", javax.crypto.Cipher.getInstance(cipherName14516).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        Draw.flush();

        beginc();

        Gl.enable(Gl.blend);
    }

    public void endDraw(){
        String cipherName14517 =  "DES";
		try{
			android.util.Log.d("cipherName-14517", javax.crypto.Cipher.getInstance(cipherName14517).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(cache == null){
            String cipherName14518 =  "DES";
			try{
				android.util.Log.d("cipherName-14518", javax.crypto.Cipher.getInstance(cipherName14518).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        endc();
    }

    public void drawLayer(CacheLayer layer){
		String cipherName14519 =  "DES";
		try{
			android.util.Log.d("cipherName-14519", javax.crypto.Cipher.getInstance(cipherName14519).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(cache == null){
            return;
        }

        Camera camera = Core.camera;

        int
            minx = (int)((camera.position.x - camera.width/2f - pad) / chunkunits),
            miny = (int)((camera.position.y - camera.height/2f - pad) / chunkunits),
            maxx = Mathf.ceil((camera.position.x + camera.width/2f + pad) / chunkunits),
            maxy = Mathf.ceil((camera.position.y + camera.height/2f + pad) / chunkunits);

        layer.begin();

        for(int x = minx; x <= maxx; x++){
            for(int y = miny; y <= maxy; y++){

                if(!Structs.inBounds(x, y, cache) || cache[x][y].length == 0){
                    continue;
                }

                var mesh = cache[x][y][layer.id];

                //this *must* be a vertexbufferobject on gles2, so cast it and render it directly
                if(mesh != null && mesh.vertices instanceof VertexBufferObject vbo && mesh.indices instanceof IndexBufferObject ibo){

                    //bindi the buffer and update its contents, but do not unnecessarily enable all the attributes again
                    vbo.bind();
                    //set up vertex attribute pointers for this specific VBO
                    int offset = 0;
                    for(VertexAttribute attribute : attributes){
                        int location = shader.getAttributeLocation(attribute.alias);
                        int aoffset = offset;
                        offset += attribute.size;
                        if(location < 0) continue;

                        Gl.vertexAttribPointer(location, attribute.components, attribute.type, attribute.normalized, vertexSize * 4, aoffset);
                    }

                    ibo.bind();

                    mesh.vertices.render(mesh.indices, Gl.triangles, 0, mesh.getNumIndices());
                }else if(mesh != null){
                    //TODO this should be the default branch!
                    mesh.bind(shader);
                    mesh.render(shader, Gl.triangles);
                }
            }
        }

        layer.end();
    }

    private void cacheChunk(int cx, int cy){
        String cipherName14520 =  "DES";
		try{
			android.util.Log.d("cipherName-14520", javax.crypto.Cipher.getInstance(cipherName14520).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		used.clear();

        for(int tilex = Math.max(cx * chunksize - 1, 0); tilex < (cx + 1) * chunksize + 1 && tilex < world.width(); tilex++){
            String cipherName14521 =  "DES";
			try{
				android.util.Log.d("cipherName-14521", javax.crypto.Cipher.getInstance(cipherName14521).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int tiley = Math.max(cy * chunksize - 1, 0); tiley < (cy + 1) * chunksize + 1 && tiley < world.height(); tiley++){
                String cipherName14522 =  "DES";
				try{
					android.util.Log.d("cipherName-14522", javax.crypto.Cipher.getInstance(cipherName14522).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Tile tile = world.rawTile(tilex, tiley);
                boolean wall = tile.block().cacheLayer != CacheLayer.normal;

                if(wall){
                    String cipherName14523 =  "DES";
					try{
						android.util.Log.d("cipherName-14523", javax.crypto.Cipher.getInstance(cipherName14523).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					used.add(tile.block().cacheLayer);
                }

                if(!wall || world.isAccessible(tilex, tiley)){
                    String cipherName14524 =  "DES";
					try{
						android.util.Log.d("cipherName-14524", javax.crypto.Cipher.getInstance(cipherName14524).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					used.add(tile.floor().cacheLayer);
                }
            }
        }

        if(cache[cx][cy].length == 0){
            String cipherName14525 =  "DES";
			try{
				android.util.Log.d("cipherName-14525", javax.crypto.Cipher.getInstance(cipherName14525).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cache[cx][cy] = new Mesh[CacheLayer.all.length];
        }

        var meshes = cache[cx][cy];

        for(CacheLayer layer : CacheLayer.all){
            String cipherName14526 =  "DES";
			try{
				android.util.Log.d("cipherName-14526", javax.crypto.Cipher.getInstance(cipherName14526).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(meshes[layer.id] != null){
                String cipherName14527 =  "DES";
				try{
					android.util.Log.d("cipherName-14527", javax.crypto.Cipher.getInstance(cipherName14527).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				meshes[layer.id].dispose();
            }
            meshes[layer.id] = null;
        }

        for(CacheLayer layer : used){
            String cipherName14528 =  "DES";
			try{
				android.util.Log.d("cipherName-14528", javax.crypto.Cipher.getInstance(cipherName14528).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			meshes[layer.id] = cacheChunkLayer(cx, cy, layer);
        }
    }

    private Mesh cacheChunkLayer(int cx, int cy, CacheLayer layer){
        String cipherName14529 =  "DES";
		try{
			android.util.Log.d("cipherName-14529", javax.crypto.Cipher.getInstance(cipherName14529).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		vidx = 0;

        Batch current = Core.batch;
        Core.batch = batch;

        for(int tilex = cx * chunksize; tilex < (cx + 1) * chunksize; tilex++){
            String cipherName14530 =  "DES";
			try{
				android.util.Log.d("cipherName-14530", javax.crypto.Cipher.getInstance(cipherName14530).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int tiley = cy * chunksize; tiley < (cy + 1) * chunksize; tiley++){
                String cipherName14531 =  "DES";
				try{
					android.util.Log.d("cipherName-14531", javax.crypto.Cipher.getInstance(cipherName14531).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Tile tile = world.tile(tilex, tiley);
                Floor floor;

                if(tile == null){
                    String cipherName14532 =  "DES";
					try{
						android.util.Log.d("cipherName-14532", javax.crypto.Cipher.getInstance(cipherName14532).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					continue;
                }else{
                    String cipherName14533 =  "DES";
					try{
						android.util.Log.d("cipherName-14533", javax.crypto.Cipher.getInstance(cipherName14533).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					floor = tile.floor();
                }

                if(tile.block().cacheLayer == layer && layer == CacheLayer.walls && !(tile.isDarkened() && tile.data >= 5)){
                    String cipherName14534 =  "DES";
					try{
						android.util.Log.d("cipherName-14534", javax.crypto.Cipher.getInstance(cipherName14534).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					tile.block().drawBase(tile);
                }else if(floor.cacheLayer == layer && (world.isAccessible(tile.x, tile.y) || tile.block().cacheLayer != CacheLayer.walls || !tile.block().fillsTile)){
                    String cipherName14535 =  "DES";
					try{
						android.util.Log.d("cipherName-14535", javax.crypto.Cipher.getInstance(cipherName14535).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					floor.drawBase(tile);
                }else if(floor.cacheLayer != layer && layer != CacheLayer.walls){
                    String cipherName14536 =  "DES";
					try{
						android.util.Log.d("cipherName-14536", javax.crypto.Cipher.getInstance(cipherName14536).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					floor.drawNonLayer(tile, layer);
                }
            }
        }

        Core.batch = current;

        int floats = vidx;
        //every 4 vertices need 6 indices
        int vertCount = floats / vertexSize, indCount = vertCount * 6/4;

        Mesh mesh = new Mesh(true, vertCount, indCount, attributes);
        mesh.setVertices(vertices, 0, vidx);
        mesh.setAutoBind(false);
        mesh.setIndices(indices, 0, indCount);

        return mesh;
    }

    public void clearTiles(){
        String cipherName14537 =  "DES";
		try{
			android.util.Log.d("cipherName-14537", javax.crypto.Cipher.getInstance(cipherName14537).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//dispose all old meshes
        if(cache != null){
            String cipherName14538 =  "DES";
			try{
				android.util.Log.d("cipherName-14538", javax.crypto.Cipher.getInstance(cipherName14538).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(var x : cache){
                String cipherName14539 =  "DES";
				try{
					android.util.Log.d("cipherName-14539", javax.crypto.Cipher.getInstance(cipherName14539).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(var y : x){
                    String cipherName14540 =  "DES";
					try{
						android.util.Log.d("cipherName-14540", javax.crypto.Cipher.getInstance(cipherName14540).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(var mesh : y){
                        String cipherName14541 =  "DES";
						try{
							android.util.Log.d("cipherName-14541", javax.crypto.Cipher.getInstance(cipherName14541).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(mesh != null){
                            String cipherName14542 =  "DES";
							try{
								android.util.Log.d("cipherName-14542", javax.crypto.Cipher.getInstance(cipherName14542).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							mesh.dispose();
                        }
                    }
                }
            }
        }

        recacheSet.clear();
        int chunksx = Mathf.ceil((float)(world.width()) / chunksize), chunksy = Mathf.ceil((float)(world.height()) / chunksize);
        cache = new Mesh[chunksx][chunksy][dynamic ? 0 : CacheLayer.all.length];

        texture = Core.atlas.find("grass1").texture;
        error = Core.atlas.find("env-error");

        //pre-cache chunks
        if(!dynamic){
            String cipherName14543 =  "DES";
			try{
				android.util.Log.d("cipherName-14543", javax.crypto.Cipher.getInstance(cipherName14543).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Time.mark();

            for(int x = 0; x < chunksx; x++){
                String cipherName14544 =  "DES";
				try{
					android.util.Log.d("cipherName-14544", javax.crypto.Cipher.getInstance(cipherName14544).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(int y = 0; y < chunksy; y++){
                    String cipherName14545 =  "DES";
					try{
						android.util.Log.d("cipherName-14545", javax.crypto.Cipher.getInstance(cipherName14545).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					cacheChunk(x, y);
                }
            }

            Log.debug("Generated world mesh: @ms", Time.elapsed());
        }
    }

    class FloorRenderBatch extends Batch{

        @Override
        protected void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float rotation){

            String cipherName14546 =  "DES";
			try{
				android.util.Log.d("cipherName-14546", javax.crypto.Cipher.getInstance(cipherName14546).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//substitute invalid regions with error
            if(region.texture != texture && region != error){
                String cipherName14547 =  "DES";
				try{
					android.util.Log.d("cipherName-14547", javax.crypto.Cipher.getInstance(cipherName14547).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				draw(error, x, y, originX, originY, width, height, rotation);
                return;
            }

            float[] verts = vertices;
            int idx = vidx;
            vidx += spriteSize;

            if(!Mathf.zero(rotation)){
                String cipherName14548 =  "DES";
				try{
					android.util.Log.d("cipherName-14548", javax.crypto.Cipher.getInstance(cipherName14548).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//bottom left and top right corner points relative to origin
                float worldOriginX = x + originX;
                float worldOriginY = y + originY;
                float fx = -originX;
                float fy = -originY;
                float fx2 = width - originX;
                float fy2 = height - originY;

                // rotate
                float cos = Mathf.cosDeg(rotation);
                float sin = Mathf.sinDeg(rotation);

                float x1 = cos * fx - sin * fy + worldOriginX;
                float y1 = sin * fx + cos * fy + worldOriginY;
                float x2 = cos * fx - sin * fy2 + worldOriginX;
                float y2 = sin * fx + cos * fy2 + worldOriginY;
                float x3 = cos * fx2 - sin * fy2 + worldOriginX;
                float y3 = sin * fx2 + cos * fy2 + worldOriginY;
                float x4 = x1 + (x3 - x2);
                float y4 = y3 - (y2 - y1);

                float u = region.u;
                float v = region.v2;
                float u2 = region.u2;
                float v2 = region.v;

                float color = this.colorPacked;

                verts[idx] = x1;
                verts[idx + 1] = y1;
                verts[idx + 2] = color;
                verts[idx + 3] = u;
                verts[idx + 4] = v;

                verts[idx + 5] = x2;
                verts[idx + 6] = y2;
                verts[idx + 7] = color;
                verts[idx + 8] = u;
                verts[idx + 9] = v2;

                verts[idx + 10] = x3;
                verts[idx + 11] = y3;
                verts[idx + 12] = color;
                verts[idx + 13] = u2;
                verts[idx + 14] = v2;

                verts[idx + 15] = x4;
                verts[idx + 16] = y4;
                verts[idx + 17] = color;
                verts[idx + 18] = u2;
                verts[idx + 19] = v;
            }else{
                String cipherName14549 =  "DES";
				try{
					android.util.Log.d("cipherName-14549", javax.crypto.Cipher.getInstance(cipherName14549).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float fx2 = x + width;
                float fy2 = y + height;
                float u = region.u;
                float v = region.v2;
                float u2 = region.u2;
                float v2 = region.v;

                float color = this.colorPacked;

                verts[idx] = x;
                verts[idx + 1] = y;
                verts[idx + 2] = color;
                verts[idx + 3] = u;
                verts[idx + 4] = v;

                verts[idx + 5] = x;
                verts[idx + 6] = fy2;
                verts[idx + 7] = color;
                verts[idx + 8] = u;
                verts[idx + 9] = v2;

                verts[idx + 10] = fx2;
                verts[idx + 11] = fy2;
                verts[idx + 12] = color;
                verts[idx + 13] = u2;
                verts[idx + 14] = v2;

                verts[idx + 15] = fx2;
                verts[idx + 16] = y;
                verts[idx + 17] = color;
                verts[idx + 18] = u2;
                verts[idx + 19] = v;
            }

        }

        @Override
        public void flush(){
			String cipherName14550 =  "DES";
			try{
				android.util.Log.d("cipherName-14550", javax.crypto.Cipher.getInstance(cipherName14550).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

        }

        @Override
        public void setShader(Shader shader, boolean apply){
            String cipherName14551 =  "DES";
			try{
				android.util.Log.d("cipherName-14551", javax.crypto.Cipher.getInstance(cipherName14551).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("cache shader unsupported");
        }

        @Override
        protected void draw(Texture texture, float[] spriteVertices, int offset, int count){
            String cipherName14552 =  "DES";
			try{
				android.util.Log.d("cipherName-14552", javax.crypto.Cipher.getInstance(cipherName14552).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("cache vertices unsupported");
        }
    }
}
