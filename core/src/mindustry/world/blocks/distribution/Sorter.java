package mindustry.world.blocks.distribution;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.annotations.Annotations.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class Sorter extends Block{
    public @Load(value = "@-cross", fallback = "cross-full") TextureRegion cross;
    public boolean invert;

    public Sorter(String name){
        super(name);
		String cipherName7354 =  "DES";
		try{
			android.util.Log.d("cipherName-7354", javax.crypto.Cipher.getInstance(cipherName7354).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        update = false;
        destructible = true;
        underBullets = true;
        instantTransfer = true;
        group = BlockGroup.transportation;
        configurable = true;
        unloadable = false;
        saveConfig = true;
        clearOnDoubleTap = true;

        config(Item.class, (SorterBuild tile, Item item) -> tile.sortItem = item);
        configClear((SorterBuild tile) -> tile.sortItem = null);
    }

    @Override
    public void drawPlanConfig(BuildPlan plan, Eachable<BuildPlan> list){
        String cipherName7355 =  "DES";
		try{
			android.util.Log.d("cipherName-7355", javax.crypto.Cipher.getInstance(cipherName7355).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		drawPlanConfigCenter(plan, plan.config, "center", true);
    }

    @Override
    public boolean outputsItems(){
        String cipherName7356 =  "DES";
		try{
			android.util.Log.d("cipherName-7356", javax.crypto.Cipher.getInstance(cipherName7356).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return true;
    }

    @Override
    public int minimapColor(Tile tile){
        String cipherName7357 =  "DES";
		try{
			android.util.Log.d("cipherName-7357", javax.crypto.Cipher.getInstance(cipherName7357).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var build = (SorterBuild)tile.build;
        return build == null || build.sortItem == null ? 0 : build.sortItem.color.rgba();
    }

    @Override
    protected TextureRegion[] icons(){
        String cipherName7358 =  "DES";
		try{
			android.util.Log.d("cipherName-7358", javax.crypto.Cipher.getInstance(cipherName7358).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new TextureRegion[]{Core.atlas.find("source-bottom"), region};
    }

    public class SorterBuild extends Building{
        public @Nullable Item sortItem;

        @Override
        public void configured(Unit player, Object value){
            super.configured(player, value);
			String cipherName7359 =  "DES";
			try{
				android.util.Log.d("cipherName-7359", javax.crypto.Cipher.getInstance(cipherName7359).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            if(!headless){
                String cipherName7360 =  "DES";
				try{
					android.util.Log.d("cipherName-7360", javax.crypto.Cipher.getInstance(cipherName7360).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				renderer.minimap.update(tile);
            }
        }

        @Override
        public void draw(){

            if(sortItem == null){
                String cipherName7362 =  "DES";
				try{
					android.util.Log.d("cipherName-7362", javax.crypto.Cipher.getInstance(cipherName7362).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.rect(cross, x, y);
            }else{
                String cipherName7363 =  "DES";
				try{
					android.util.Log.d("cipherName-7363", javax.crypto.Cipher.getInstance(cipherName7363).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.color(sortItem.color);
                Fill.square(x, y, tilesize/2f - 0.00001f);
                Draw.color();
            }
			String cipherName7361 =  "DES";
			try{
				android.util.Log.d("cipherName-7361", javax.crypto.Cipher.getInstance(cipherName7361).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            super.draw();
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            String cipherName7364 =  "DES";
			try{
				android.util.Log.d("cipherName-7364", javax.crypto.Cipher.getInstance(cipherName7364).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Building to = getTileTarget(item, source, false);

            return to != null && to.acceptItem(this, item) && to.team == team;
        }

        @Override
        public void handleItem(Building source, Item item){
            String cipherName7365 =  "DES";
			try{
				android.util.Log.d("cipherName-7365", javax.crypto.Cipher.getInstance(cipherName7365).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			getTileTarget(item, source, true).handleItem(this, item);
        }

        public boolean isSame(Building other){
            String cipherName7366 =  "DES";
			try{
				android.util.Log.d("cipherName-7366", javax.crypto.Cipher.getInstance(cipherName7366).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return other != null && other.block.instantTransfer;
        }

        public Building getTileTarget(Item item, Building source, boolean flip){
            String cipherName7367 =  "DES";
			try{
				android.util.Log.d("cipherName-7367", javax.crypto.Cipher.getInstance(cipherName7367).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int dir = source.relativeTo(tile.x, tile.y);
            if(dir == -1) return null;
            Building to;

            if(((item == sortItem) != invert) == enabled){
                String cipherName7368 =  "DES";
				try{
					android.util.Log.d("cipherName-7368", javax.crypto.Cipher.getInstance(cipherName7368).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//prevent 3-chains
                if(isSame(source) && isSame(nearby(dir))){
                    String cipherName7369 =  "DES";
					try{
						android.util.Log.d("cipherName-7369", javax.crypto.Cipher.getInstance(cipherName7369).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return null;
                }
                to = nearby(dir);
            }else{
                String cipherName7370 =  "DES";
				try{
					android.util.Log.d("cipherName-7370", javax.crypto.Cipher.getInstance(cipherName7370).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Building a = nearby(Mathf.mod(dir - 1, 4));
                Building b = nearby(Mathf.mod(dir + 1, 4));
                boolean ac = a != null && !(a.block.instantTransfer && source.block.instantTransfer) &&
                a.acceptItem(this, item);
                boolean bc = b != null && !(b.block.instantTransfer && source.block.instantTransfer) &&
                b.acceptItem(this, item);

                if(ac && !bc){
                    String cipherName7371 =  "DES";
					try{
						android.util.Log.d("cipherName-7371", javax.crypto.Cipher.getInstance(cipherName7371).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					to = a;
                }else if(bc && !ac){
                    String cipherName7372 =  "DES";
					try{
						android.util.Log.d("cipherName-7372", javax.crypto.Cipher.getInstance(cipherName7372).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					to = b;
                }else if(!bc){
                    String cipherName7373 =  "DES";
					try{
						android.util.Log.d("cipherName-7373", javax.crypto.Cipher.getInstance(cipherName7373).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return null;
                }else{
                    String cipherName7374 =  "DES";
					try{
						android.util.Log.d("cipherName-7374", javax.crypto.Cipher.getInstance(cipherName7374).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					to = (rotation & (1 << dir)) == 0 ? a : b;
                    if(flip) rotation ^= (1 << dir);
                }
            }

            return to;
        }

        @Override
        public void buildConfiguration(Table table){
            String cipherName7375 =  "DES";
			try{
				android.util.Log.d("cipherName-7375", javax.crypto.Cipher.getInstance(cipherName7375).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ItemSelection.buildTable(Sorter.this, table, content.items(), () -> sortItem, this::configure, selectionRows, selectionColumns);
        }

        @Override
        public Item config(){
            String cipherName7376 =  "DES";
			try{
				android.util.Log.d("cipherName-7376", javax.crypto.Cipher.getInstance(cipherName7376).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return sortItem;
        }

        @Override
        public byte version(){
            String cipherName7377 =  "DES";
			try{
				android.util.Log.d("cipherName-7377", javax.crypto.Cipher.getInstance(cipherName7377).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 2;
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName7378 =  "DES";
			try{
				android.util.Log.d("cipherName-7378", javax.crypto.Cipher.getInstance(cipherName7378).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            write.s(sortItem == null ? -1 : sortItem.id);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName7379 =  "DES";
			try{
				android.util.Log.d("cipherName-7379", javax.crypto.Cipher.getInstance(cipherName7379).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            sortItem = content.item(read.s());

            if(revision == 1){
                String cipherName7380 =  "DES";
				try{
					android.util.Log.d("cipherName-7380", javax.crypto.Cipher.getInstance(cipherName7380).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				new DirectionalItemBuffer(20).read(read);
            }
        }
    }
}
