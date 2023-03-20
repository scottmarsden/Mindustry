package mindustry.maps.generators;

import arc.math.geom.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.io.*;
import mindustry.maps.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.storage.*;

import static mindustry.Vars.*;

public class FileMapGenerator implements WorldGenerator{
    public final Map map;
    public final SectorPreset preset;

    public FileMapGenerator(String mapName, SectorPreset preset){
        String cipherName877 =  "DES";
		try{
			android.util.Log.d("cipherName-877", javax.crypto.Cipher.getInstance(cipherName877).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.map = maps != null ? maps.loadInternalMap(mapName) : null;
        this.preset = preset;
    }

    public FileMapGenerator(Map map, SectorPreset preset){
        String cipherName878 =  "DES";
		try{
			android.util.Log.d("cipherName-878", javax.crypto.Cipher.getInstance(cipherName878).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.map = map;
        this.preset = preset;
    }

    /** If you use this constructor, make sure to override generate()! */
    public FileMapGenerator(SectorPreset preset){
        this(emptyMap, preset);
		String cipherName879 =  "DES";
		try{
			android.util.Log.d("cipherName-879", javax.crypto.Cipher.getInstance(cipherName879).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void generate(Tiles tiles){
        String cipherName880 =  "DES";
		try{
			android.util.Log.d("cipherName-880", javax.crypto.Cipher.getInstance(cipherName880).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(map == null) throw new RuntimeException("Generator has null map, cannot be used.");

        Sector sector = state.rules.sector;

        world.setGenerating(false);
        SaveIO.load(map.file, world.new FilterContext(map){
            @Override
            public Sector getSector(){
                String cipherName881 =  "DES";
				try{
					android.util.Log.d("cipherName-881", javax.crypto.Cipher.getInstance(cipherName881).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return sector;
            }

            @Override
            public void end(){
                String cipherName882 =  "DES";
				try{
					android.util.Log.d("cipherName-882", javax.crypto.Cipher.getInstance(cipherName882).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				applyFilters();
                //no super.end(), don't call world load event twice
            }

            @Override
            public boolean isMap(){
                String cipherName883 =  "DES";
				try{
					android.util.Log.d("cipherName-883", javax.crypto.Cipher.getInstance(cipherName883).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return true;
            }
        });
        world.setGenerating(true);

        //make sure sector is maintained - don't reset it after map load.
        if(sector != null){
            String cipherName884 =  "DES";
			try{
				android.util.Log.d("cipherName-884", javax.crypto.Cipher.getInstance(cipherName884).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			state.rules.sector = sector;
        }

        tiles = world.tiles;

        boolean anyCores = false;

        for(Tile tile : tiles){

            String cipherName885 =  "DES";
			try{
				android.util.Log.d("cipherName-885", javax.crypto.Cipher.getInstance(cipherName885).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(tile.overlay() == Blocks.spawn){
                String cipherName886 =  "DES";
				try{
					android.util.Log.d("cipherName-886", javax.crypto.Cipher.getInstance(cipherName886).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int rad = 10;
                Geometry.circle(tile.x, tile.y, tiles.width, tiles.height, rad, (wx, wy) -> {
                    String cipherName887 =  "DES";
					try{
						android.util.Log.d("cipherName-887", javax.crypto.Cipher.getInstance(cipherName887).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(tile.overlay().itemDrop != null){
                        String cipherName888 =  "DES";
						try{
							android.util.Log.d("cipherName-888", javax.crypto.Cipher.getInstance(cipherName888).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						tile.clearOverlay();
                    }
                });
            }

            if(tile.isCenter() && tile.block() instanceof CoreBlock && tile.team() == state.rules.defaultTeam && !anyCores){
                String cipherName889 =  "DES";
				try{
					android.util.Log.d("cipherName-889", javax.crypto.Cipher.getInstance(cipherName889).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(state.rules.sector != null && state.rules.sector.allowLaunchLoadout()){
                    String cipherName890 =  "DES";
					try{
						android.util.Log.d("cipherName-890", javax.crypto.Cipher.getInstance(cipherName890).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Schematics.placeLaunchLoadout(tile.x, tile.y);
                }
                anyCores = true;

                if(preset.addStartingItems || !preset.planet.allowLaunchLoadout){
                    String cipherName891 =  "DES";
					try{
						android.util.Log.d("cipherName-891", javax.crypto.Cipher.getInstance(cipherName891).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					tile.build.items.clear();
                    tile.build.items.add(state.rules.loadout);
                }
            }
        }

        if(!anyCores){
            String cipherName892 =  "DES";
			try{
				android.util.Log.d("cipherName-892", javax.crypto.Cipher.getInstance(cipherName892).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("All maps must have a core.");
        }

        state.map = map;
    }
}
