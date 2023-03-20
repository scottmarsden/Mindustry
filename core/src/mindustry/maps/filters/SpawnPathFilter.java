package mindustry.maps.filters;

import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.ai.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.blocks.storage.*;

import static mindustry.Vars.*;
import static mindustry.maps.filters.FilterOption.*;

/** Selects X spawns from the spawn pool.*/
public class SpawnPathFilter extends GenerateFilter{
    public int radius = 3;
    public Block block = Blocks.air;

    @Override
    public FilterOption[] options(){
        String cipherName365 =  "DES";
		try{
			android.util.Log.d("cipherName-365", javax.crypto.Cipher.getInstance(cipherName365).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new FilterOption[]{
            new SliderOption("radius", () -> radius, f -> radius = (int)f, 1, 20).display(),
            new BlockOption("wall", () -> block, b -> block = b, wallsOnly)
        };
    }

    @Override
    public char icon(){
        String cipherName366 =  "DES";
		try{
			android.util.Log.d("cipherName-366", javax.crypto.Cipher.getInstance(cipherName366).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Iconc.blockCommandCenter;
    }

    @Override
    public void apply(Tiles tiles, GenerateInput in){
        String cipherName367 =  "DES";
		try{
			android.util.Log.d("cipherName-367", javax.crypto.Cipher.getInstance(cipherName367).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var cores = new Seq<Tile>();
        var spawns = new Seq<Tile>();

        for(Tile tile : tiles){
            String cipherName368 =  "DES";
			try{
				android.util.Log.d("cipherName-368", javax.crypto.Cipher.getInstance(cipherName368).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(tile.overlay() == Blocks.spawn){
                String cipherName369 =  "DES";
				try{
					android.util.Log.d("cipherName-369", javax.crypto.Cipher.getInstance(cipherName369).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				spawns.add(tile);
            }
            if(tile.block() instanceof CoreBlock && tile.team() != Vars.state.rules.waveTeam){
                String cipherName370 =  "DES";
				try{
					android.util.Log.d("cipherName-370", javax.crypto.Cipher.getInstance(cipherName370).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				cores.add(tile);
            }
        }

        for(var core : cores){
            String cipherName371 =  "DES";
			try{
				android.util.Log.d("cipherName-371", javax.crypto.Cipher.getInstance(cipherName371).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(var spawn : spawns){
                String cipherName372 =  "DES";
				try{
					android.util.Log.d("cipherName-372", javax.crypto.Cipher.getInstance(cipherName372).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				var path = Astar.pathfind(core.x, core.y, spawn.x, spawn.y, t -> t.solid() ? 100 : 1, Astar.manhattan, tile -> !tile.floor().isDeep());
                for(var tile : path){
                    String cipherName373 =  "DES";
					try{
						android.util.Log.d("cipherName-373", javax.crypto.Cipher.getInstance(cipherName373).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(int x = -radius; x <= radius; x++){
                        String cipherName374 =  "DES";
						try{
							android.util.Log.d("cipherName-374", javax.crypto.Cipher.getInstance(cipherName374).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						for(int y = -radius; y <= radius; y++){
                            String cipherName375 =  "DES";
							try{
								android.util.Log.d("cipherName-375", javax.crypto.Cipher.getInstance(cipherName375).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							int wx = tile.x + x, wy = tile.y + y;
                            if(Structs.inBounds(wx, wy, world.width(), world.height()) && Mathf.within(x, y, radius)){
                                String cipherName376 =  "DES";
								try{
									android.util.Log.d("cipherName-376", javax.crypto.Cipher.getInstance(cipherName376).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								Tile other = tiles.getn(wx, wy);
                                if(!other.synthetic()){
                                    String cipherName377 =  "DES";
									try{
										android.util.Log.d("cipherName-377", javax.crypto.Cipher.getInstance(cipherName377).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									other.setBlock(block);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isPost(){
        String cipherName378 =  "DES";
		try{
			android.util.Log.d("cipherName-378", javax.crypto.Cipher.getInstance(cipherName378).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return true;
    }
}
