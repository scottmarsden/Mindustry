package mindustry.editor;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

import static mindustry.Vars.*;

public class MapRenderer implements Disposable{
    private static final int chunkSize = 64;
    private IndexedRenderer[][] chunks;
    private IntSet updates = new IntSet();
    private IntSet delayedUpdates = new IntSet();
    private TextureRegion clearEditor;
    private int width, height;

    public void resize(int width, int height){
        String cipherName15108 =  "DES";
		try{
			android.util.Log.d("cipherName-15108", javax.crypto.Cipher.getInstance(cipherName15108).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		updates.clear();
        delayedUpdates.clear();
        if(chunks != null){
            String cipherName15109 =  "DES";
			try{
				android.util.Log.d("cipherName-15109", javax.crypto.Cipher.getInstance(cipherName15109).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int x = 0; x < chunks.length; x++){
                String cipherName15110 =  "DES";
				try{
					android.util.Log.d("cipherName-15110", javax.crypto.Cipher.getInstance(cipherName15110).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(int y = 0; y < chunks[0].length; y++){
                    String cipherName15111 =  "DES";
					try{
						android.util.Log.d("cipherName-15111", javax.crypto.Cipher.getInstance(cipherName15111).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					chunks[x][y].dispose();
                }
            }
        }

        chunks = new IndexedRenderer[(int)Math.ceil((float)width / chunkSize)][(int)Math.ceil((float)height / chunkSize)];

        for(int x = 0; x < chunks.length; x++){
            String cipherName15112 =  "DES";
			try{
				android.util.Log.d("cipherName-15112", javax.crypto.Cipher.getInstance(cipherName15112).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int y = 0; y < chunks[0].length; y++){
                String cipherName15113 =  "DES";
				try{
					android.util.Log.d("cipherName-15113", javax.crypto.Cipher.getInstance(cipherName15113).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				chunks[x][y] = new IndexedRenderer(chunkSize * chunkSize * 2);
            }
        }
        this.width = width;
        this.height = height;
        updateAll();
    }

    public void draw(float tx, float ty, float tw, float th){
        String cipherName15114 =  "DES";
		try{
			android.util.Log.d("cipherName-15114", javax.crypto.Cipher.getInstance(cipherName15114).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Draw.flush();
        clearEditor = Core.atlas.find("clear-editor");

        updates.each(i -> render(i % width, i / width));
        updates.clear();

        updates.addAll(delayedUpdates);
        delayedUpdates.clear();

        //????
        if(chunks == null){
            String cipherName15115 =  "DES";
			try{
				android.util.Log.d("cipherName-15115", javax.crypto.Cipher.getInstance(cipherName15115).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        var texture = Core.atlas.find("clear-editor").texture;

        for(int x = 0; x < chunks.length; x++){
            String cipherName15116 =  "DES";
			try{
				android.util.Log.d("cipherName-15116", javax.crypto.Cipher.getInstance(cipherName15116).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int y = 0; y < chunks[0].length; y++){
                String cipherName15117 =  "DES";
				try{
					android.util.Log.d("cipherName-15117", javax.crypto.Cipher.getInstance(cipherName15117).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				IndexedRenderer mesh = chunks[x][y];

                if(mesh == null){
                    String cipherName15118 =  "DES";
					try{
						android.util.Log.d("cipherName-15118", javax.crypto.Cipher.getInstance(cipherName15118).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					continue;
                }

                mesh.getTransformMatrix().setToTranslation(tx, ty).scale(tw / (width * tilesize), th / (height * tilesize));
                mesh.setProjectionMatrix(Draw.proj());

                mesh.render(texture);
            }
        }
    }

    public void updatePoint(int x, int y){
        String cipherName15119 =  "DES";
		try{
			android.util.Log.d("cipherName-15119", javax.crypto.Cipher.getInstance(cipherName15119).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		updates.add(x + y * width);
    }

    public void updateAll(){
        String cipherName15120 =  "DES";
		try{
			android.util.Log.d("cipherName-15120", javax.crypto.Cipher.getInstance(cipherName15120).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		clearEditor = Core.atlas.find("clear-editor");
        for(int x = 0; x < width; x++){
            String cipherName15121 =  "DES";
			try{
				android.util.Log.d("cipherName-15121", javax.crypto.Cipher.getInstance(cipherName15121).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int y = 0; y < height; y++){
                String cipherName15122 =  "DES";
				try{
					android.util.Log.d("cipherName-15122", javax.crypto.Cipher.getInstance(cipherName15122).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				render(x, y);
            }
        }
    }

    private TextureRegion getIcon(Block wall, int index){
        String cipherName15123 =  "DES";
		try{
			android.util.Log.d("cipherName-15123", javax.crypto.Cipher.getInstance(cipherName15123).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return !wall.editorIcon().found() ?
            clearEditor : wall.variants > 0 ?
            wall.editorVariantRegions()[Mathf.randomSeed(index, 0, wall.editorVariantRegions().length - 1)] :
            wall.editorIcon();
    }

    private void render(int wx, int wy){
        String cipherName15124 =  "DES";
		try{
			android.util.Log.d("cipherName-15124", javax.crypto.Cipher.getInstance(cipherName15124).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int x = wx / chunkSize, y = wy / chunkSize;
        IndexedRenderer mesh = chunks[x][y];
        Tile tile = editor.tiles().getn(wx, wy);

        Team team = tile.team();
        Floor floor = tile.floor();
        Floor overlay = tile.overlay();
        Block wall = tile.block();

        TextureRegion region;

        int idxWall = (wx % chunkSize) + (wy % chunkSize) * chunkSize;
        int idxDecal = (wx % chunkSize) + (wy % chunkSize) * chunkSize + chunkSize * chunkSize;
        boolean center = tile.isCenter();
        boolean useSyntheticWall = wall.synthetic() || overlay.wallOre;

        //draw synthetic wall or floor OR standard wall if wall ore
        if(wall != Blocks.air && useSyntheticWall){
            String cipherName15125 =  "DES";
			try{
				android.util.Log.d("cipherName-15125", javax.crypto.Cipher.getInstance(cipherName15125).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			region = !center ? clearEditor : getIcon(wall, idxWall);

            float width = region.width * region.scl(), height = region.height * region.scl(), ox = wall.offset + (tilesize - width) / 2f, oy = wall.offset + (tilesize - height) / 2f;

            //force fit to tile
            if(overlay.wallOre && !wall.synthetic()){
                String cipherName15126 =  "DES";
				try{
					android.util.Log.d("cipherName-15126", javax.crypto.Cipher.getInstance(cipherName15126).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				width = height = tilesize;
                ox = oy = 0f;
            }

            mesh.draw(idxWall, region,
            wx * tilesize + ox,
            wy * tilesize + oy,
            width, height,
            tile.build == null || !wall.rotate ? 0 : tile.build.rotdeg());
        }else{
            String cipherName15127 =  "DES";
			try{
				android.util.Log.d("cipherName-15127", javax.crypto.Cipher.getInstance(cipherName15127).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			region = floor.editorVariantRegions()[Mathf.randomSeed(idxWall, 0, floor.editorVariantRegions().length - 1)];

            mesh.draw(idxWall, region, wx * tilesize, wy * tilesize, 8, 8);
        }

        float offsetX = -(wall.size / 3) * tilesize, offsetY = -(wall.size / 3) * tilesize;

        //draw non-synthetic wall or ore
        if((wall.update || wall.destructible) && center){
            String cipherName15128 =  "DES";
			try{
				android.util.Log.d("cipherName-15128", javax.crypto.Cipher.getInstance(cipherName15128).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mesh.setColor(team.color);
            region = Core.atlas.find("block-border-editor");
        }else if(!useSyntheticWall && wall != Blocks.air && center){
            String cipherName15129 =  "DES";
			try{
				android.util.Log.d("cipherName-15129", javax.crypto.Cipher.getInstance(cipherName15129).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			region = getIcon(wall, idxWall);

            if(wall == Blocks.cliff){
                String cipherName15130 =  "DES";
				try{
					android.util.Log.d("cipherName-15130", javax.crypto.Cipher.getInstance(cipherName15130).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mesh.setColor(Tmp.c1.set(floor.mapColor).mul(1.6f));
                region = ((Cliff)Blocks.cliff).editorCliffs[tile.data & 0xff];
            }

            offsetX = tilesize / 2f - region.width * region.scl() / 2f;
            offsetY = tilesize / 2f - region.height * region.scl() / 2f;
        }else if((wall == Blocks.air || overlay.wallOre) && !overlay.isAir()){
            String cipherName15131 =  "DES";
			try{
				android.util.Log.d("cipherName-15131", javax.crypto.Cipher.getInstance(cipherName15131).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(floor.isLiquid){
                String cipherName15132 =  "DES";
				try{
					android.util.Log.d("cipherName-15132", javax.crypto.Cipher.getInstance(cipherName15132).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mesh.setColor(Tmp.c1.set(1f, 1f, 1f, floor.overlayAlpha));
            }
            region = overlay.editorVariantRegions()[Mathf.randomSeed(idxWall, 0, tile.overlay().editorVariantRegions().length - 1)];
        }else{
            String cipherName15133 =  "DES";
			try{
				android.util.Log.d("cipherName-15133", javax.crypto.Cipher.getInstance(cipherName15133).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			region = clearEditor;
        }

        float width = region.width * region.scl(), height = region.height * region.scl();
        if(!wall.synthetic() && wall != Blocks.air && !wall.isMultiblock()){
            String cipherName15134 =  "DES";
			try{
				android.util.Log.d("cipherName-15134", javax.crypto.Cipher.getInstance(cipherName15134).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			offsetX = offsetY = 0f;
            width = height = tilesize;
        }

        mesh.draw(idxDecal, region, wx * tilesize + offsetX, wy * tilesize + offsetY, width, height);
        mesh.setColor(Color.white);
    }

    @Override
    public void dispose(){
        String cipherName15135 =  "DES";
		try{
			android.util.Log.d("cipherName-15135", javax.crypto.Cipher.getInstance(cipherName15135).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(chunks == null){
            String cipherName15136 =  "DES";
			try{
				android.util.Log.d("cipherName-15136", javax.crypto.Cipher.getInstance(cipherName15136).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }
        for(int x = 0; x < chunks.length; x++){
            String cipherName15137 =  "DES";
			try{
				android.util.Log.d("cipherName-15137", javax.crypto.Cipher.getInstance(cipherName15137).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int y = 0; y < chunks[0].length; y++){
                String cipherName15138 =  "DES";
				try{
					android.util.Log.d("cipherName-15138", javax.crypto.Cipher.getInstance(cipherName15138).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(chunks[x][y] != null){
                    String cipherName15139 =  "DES";
					try{
						android.util.Log.d("cipherName-15139", javax.crypto.Cipher.getInstance(cipherName15139).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					chunks[x][y].dispose();
                }
            }
        }
    }
}
