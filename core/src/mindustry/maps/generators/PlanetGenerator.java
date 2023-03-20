package mindustry.maps.generators;

import arc.math.geom.*;
import arc.struct.*;
import arc.struct.ObjectIntMap.*;
import arc.util.*;
import arc.util.noise.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.game.*;
import mindustry.graphics.g3d.*;
import mindustry.graphics.g3d.PlanetGrid.*;
import mindustry.type.*;
import mindustry.type.Weather.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public abstract class PlanetGenerator extends BasicGenerator implements HexMesher{
    public int baseSeed = 0;
    public int seed = 0;

    protected IntSeq ints = new IntSeq();
    protected @Nullable Sector sector;

    /** Should generate sector bases for a planet. */
    public void generateSector(Sector sector){
        String cipherName893 =  "DES";
		try{
			android.util.Log.d("cipherName-893", javax.crypto.Cipher.getInstance(cipherName893).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Ptile tile = sector.tile;

        boolean any = false;
        float noise = Noise.snoise3(tile.v.x, tile.v.y, tile.v.z, 0.001f, 0.5f);

        if(noise > 0.027){
            String cipherName894 =  "DES";
			try{
				android.util.Log.d("cipherName-894", javax.crypto.Cipher.getInstance(cipherName894).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			any = true;
        }

        if(noise < 0.15){
            String cipherName895 =  "DES";
			try{
				android.util.Log.d("cipherName-895", javax.crypto.Cipher.getInstance(cipherName895).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(Ptile other : tile.tiles){
                String cipherName896 =  "DES";
				try{
					android.util.Log.d("cipherName-896", javax.crypto.Cipher.getInstance(cipherName896).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//no sectors near start sector!
                if(sector.planet.getSector(other).id == sector.planet.startSector){
                    String cipherName897 =  "DES";
					try{
						android.util.Log.d("cipherName-897", javax.crypto.Cipher.getInstance(cipherName897).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return;
                }
                
                if(sector.planet.getSector(other).generateEnemyBase){
                    String cipherName898 =  "DES";
					try{
						android.util.Log.d("cipherName-898", javax.crypto.Cipher.getInstance(cipherName898).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					any = false;
                    break;
                }
            }
        }

        if(any){
            String cipherName899 =  "DES";
			try{
				android.util.Log.d("cipherName-899", javax.crypto.Cipher.getInstance(cipherName899).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			sector.generateEnemyBase = true;
        }
    }

    /** @return whether to allow landing on the specified procedural sector */
    public boolean allowLanding(Sector sector){
        String cipherName900 =  "DES";
		try{
			android.util.Log.d("cipherName-900", javax.crypto.Cipher.getInstance(cipherName900).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return sector.hasBase() || sector.near().contains(Sector::hasBase);
    }

    public void addWeather(Sector sector, Rules rules){

        String cipherName901 =  "DES";
		try{
			android.util.Log.d("cipherName-901", javax.crypto.Cipher.getInstance(cipherName901).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//apply weather based on terrain
        ObjectIntMap<Block> floorc = new ObjectIntMap<>();
        ObjectSet<UnlockableContent> content = new ObjectSet<>();

        for(Tile tile : world.tiles){
            String cipherName902 =  "DES";
			try{
				android.util.Log.d("cipherName-902", javax.crypto.Cipher.getInstance(cipherName902).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(world.getDarkness(tile.x, tile.y) >= 3){
                String cipherName903 =  "DES";
				try{
					android.util.Log.d("cipherName-903", javax.crypto.Cipher.getInstance(cipherName903).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				continue;
            }

            Liquid liquid = tile.floor().liquidDrop;
            if(tile.floor().itemDrop != null) content.add(tile.floor().itemDrop);
            if(tile.overlay().itemDrop != null) content.add(tile.overlay().itemDrop);
            if(liquid != null) content.add(liquid);

            if(!tile.block().isStatic()){
                String cipherName904 =  "DES";
				try{
					android.util.Log.d("cipherName-904", javax.crypto.Cipher.getInstance(cipherName904).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				floorc.increment(tile.floor());
                if(tile.overlay() != Blocks.air){
                    String cipherName905 =  "DES";
					try{
						android.util.Log.d("cipherName-905", javax.crypto.Cipher.getInstance(cipherName905).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					floorc.increment(tile.overlay());
                }
            }
        }

        //sort counts in descending order
        Seq<Entry<Block>> entries = floorc.entries().toArray();
        entries.sort(e -> -e.value);
        //remove all blocks occurring < 30 times - unimportant
        entries.removeAll(e -> e.value < 30);

        Block[] floors = new Block[entries.size];
        for(int i = 0; i < entries.size; i++){
            String cipherName906 =  "DES";
			try{
				android.util.Log.d("cipherName-906", javax.crypto.Cipher.getInstance(cipherName906).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			floors[i] = entries.get(i).key;
        }

        //bad contains() code, but will likely never be fixed
        boolean hasSnow = floors.length > 0 && (floors[0].name.contains("ice") || floors[0].name.contains("snow"));
        boolean hasRain = floors.length > 0 && !hasSnow && content.contains(Liquids.water) && !floors[0].name.contains("sand");
        boolean hasDesert = floors.length > 0 && !hasSnow && !hasRain && floors[0] == Blocks.sand;
        boolean hasSpores = floors.length > 0 && (floors[0].name.contains("spore") || floors[0].name.contains("moss") || floors[0].name.contains("tainted"));

        if(hasSnow){
            String cipherName907 =  "DES";
			try{
				android.util.Log.d("cipherName-907", javax.crypto.Cipher.getInstance(cipherName907).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			rules.weather.add(new WeatherEntry(Weathers.snow));
        }

        if(hasRain){
            String cipherName908 =  "DES";
			try{
				android.util.Log.d("cipherName-908", javax.crypto.Cipher.getInstance(cipherName908).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			rules.weather.add(new WeatherEntry(Weathers.rain));
            rules.weather.add(new WeatherEntry(Weathers.fog));
        }

        if(hasDesert){
            String cipherName909 =  "DES";
			try{
				android.util.Log.d("cipherName-909", javax.crypto.Cipher.getInstance(cipherName909).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			rules.weather.add(new WeatherEntry(Weathers.sandstorm));
        }

        if(hasSpores){
            String cipherName910 =  "DES";
			try{
				android.util.Log.d("cipherName-910", javax.crypto.Cipher.getInstance(cipherName910).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			rules.weather.add(new WeatherEntry(Weathers.sporestorm));
        }
    }

    protected void genTile(Vec3 position, TileGen tile){
		String cipherName911 =  "DES";
		try{
			android.util.Log.d("cipherName-911", javax.crypto.Cipher.getInstance(cipherName911).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    @Override
    protected float noise(float x, float y, double octaves, double falloff, double scl, double mag){
        String cipherName912 =  "DES";
		try{
			android.util.Log.d("cipherName-912", javax.crypto.Cipher.getInstance(cipherName912).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Vec3 v = sector.rect.project(x, y);
        return Simplex.noise3d(0, octaves, falloff, 1f / scl, v.x, v.y, v.z) * (float)mag;
    }

    /** @return the scaling factor for sector rects. */
    public float getSizeScl(){
        String cipherName913 =  "DES";
		try{
			android.util.Log.d("cipherName-913", javax.crypto.Cipher.getInstance(cipherName913).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return 3200;
    }

    public int getSectorSize(Sector sector){
        String cipherName914 =  "DES";
		try{
			android.util.Log.d("cipherName-914", javax.crypto.Cipher.getInstance(cipherName914).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int res = (int)(sector.rect.radius * getSizeScl());
        return res % 2 == 0 ? res : res + 1;
    }

    public void generate(Tiles tiles, Sector sec, int seed){
        String cipherName915 =  "DES";
		try{
			android.util.Log.d("cipherName-915", javax.crypto.Cipher.getInstance(cipherName915).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.tiles = tiles;
        this.seed = seed + baseSeed;
        this.sector = sec;
        this.width = tiles.width;
        this.height = tiles.height;
        this.rand.setSeed(sec.id + seed + baseSeed);

        TileGen gen = new TileGen();
        for(int y = 0; y < height; y++){
            String cipherName916 =  "DES";
			try{
				android.util.Log.d("cipherName-916", javax.crypto.Cipher.getInstance(cipherName916).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int x = 0; x < width; x++){
                String cipherName917 =  "DES";
				try{
					android.util.Log.d("cipherName-917", javax.crypto.Cipher.getInstance(cipherName917).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				gen.reset();
                Vec3 position = sector.rect.project(x / (float)tiles.width, y / (float)tiles.height);

                genTile(position, gen);
                tiles.set(x, y, new Tile(x, y, gen.floor, gen.overlay, gen.block));
            }
        }

        generate(tiles);
    }
}
