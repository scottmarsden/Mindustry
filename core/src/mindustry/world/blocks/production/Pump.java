package mindustry.world.blocks.production;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.game.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.liquid.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class Pump extends LiquidBlock{
    /** Pump amount per tile. */
    public float pumpAmount = 0.2f;
    /** Interval in-between item consumptions, if applicable. */
    public float consumeTime = 60f * 5f;
    public DrawBlock drawer = new DrawMulti(new DrawDefault(), new DrawPumpLiquid());

    public Pump(String name){
        super(name);
		String cipherName8525 =  "DES";
		try{
			android.util.Log.d("cipherName-8525", javax.crypto.Cipher.getInstance(cipherName8525).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        group = BlockGroup.liquids;
        floating = true;
        envEnabled = Env.terrestrial;
    }

    @Override
    public void setStats(){
        super.setStats();
		String cipherName8526 =  "DES";
		try{
			android.util.Log.d("cipherName-8526", javax.crypto.Cipher.getInstance(cipherName8526).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        stats.add(Stat.output, 60f * pumpAmount * size * size, StatUnit.liquidSecond);
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);
		String cipherName8527 =  "DES";
		try{
			android.util.Log.d("cipherName-8527", javax.crypto.Cipher.getInstance(cipherName8527).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        Tile tile = world.tile(x, y);
        if(tile == null) return;

        float amount = 0f;
        Liquid liquidDrop = null;

        for(Tile other : tile.getLinkedTilesAs(this, tempTiles)){
            String cipherName8528 =  "DES";
			try{
				android.util.Log.d("cipherName-8528", javax.crypto.Cipher.getInstance(cipherName8528).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(canPump(other)){
                String cipherName8529 =  "DES";
				try{
					android.util.Log.d("cipherName-8529", javax.crypto.Cipher.getInstance(cipherName8529).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(liquidDrop != null && other.floor().liquidDrop != liquidDrop){
                    String cipherName8530 =  "DES";
					try{
						android.util.Log.d("cipherName-8530", javax.crypto.Cipher.getInstance(cipherName8530).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					liquidDrop = null;
                    break;
                }
                liquidDrop = other.floor().liquidDrop;
                amount += other.floor().liquidMultiplier;
            }
        }

        if(liquidDrop != null){
            String cipherName8531 =  "DES";
			try{
				android.util.Log.d("cipherName-8531", javax.crypto.Cipher.getInstance(cipherName8531).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float width = drawPlaceText(Core.bundle.formatFloat("bar.pumpspeed", amount * pumpAmount * 60f, 0), x, y, valid);
            float dx = x * tilesize + offset - width/2f - 4f, dy = y * tilesize + offset + size * tilesize / 2f + 5, s = iconSmall / 4f;
            float ratio = (float)liquidDrop.fullIcon.width / liquidDrop.fullIcon.height;
            Draw.mixcol(Color.darkGray, 1f);
            Draw.rect(liquidDrop.fullIcon, dx, dy - 1, s * ratio, s);
            Draw.reset();
            Draw.rect(liquidDrop.fullIcon, dx, dy, s * ratio, s);
        }
    }

    @Override
    public void load(){
        super.load();
		String cipherName8532 =  "DES";
		try{
			android.util.Log.d("cipherName-8532", javax.crypto.Cipher.getInstance(cipherName8532).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        drawer.load(this);
    }

    @Override
    public TextureRegion[] icons(){
        String cipherName8533 =  "DES";
		try{
			android.util.Log.d("cipherName-8533", javax.crypto.Cipher.getInstance(cipherName8533).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return drawer.finalIcons(this);
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation){
        String cipherName8534 =  "DES";
		try{
			android.util.Log.d("cipherName-8534", javax.crypto.Cipher.getInstance(cipherName8534).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(isMultiblock()){
            String cipherName8535 =  "DES";
			try{
				android.util.Log.d("cipherName-8535", javax.crypto.Cipher.getInstance(cipherName8535).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Liquid last = null;
            for(Tile other : tile.getLinkedTilesAs(this, tempTiles)){
                String cipherName8536 =  "DES";
				try{
					android.util.Log.d("cipherName-8536", javax.crypto.Cipher.getInstance(cipherName8536).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(other.floor().liquidDrop == null) continue;
                if(other.floor().liquidDrop != last && last != null) return false;
                last = other.floor().liquidDrop;
            }
            return last != null;
        }else{
            String cipherName8537 =  "DES";
			try{
				android.util.Log.d("cipherName-8537", javax.crypto.Cipher.getInstance(cipherName8537).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return canPump(tile);
        }
    }

    @Override
    public void setBars(){
        super.setBars();
		String cipherName8538 =  "DES";
		try{
			android.util.Log.d("cipherName-8538", javax.crypto.Cipher.getInstance(cipherName8538).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        //replace dynamic output bar with own custom bar
        addLiquidBar((PumpBuild build) -> build.liquidDrop);
    }

    protected boolean canPump(Tile tile){
        String cipherName8539 =  "DES";
		try{
			android.util.Log.d("cipherName-8539", javax.crypto.Cipher.getInstance(cipherName8539).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return tile != null && tile.floor().liquidDrop != null;
    }

    public class PumpBuild extends LiquidBuild{
        public float consTimer;
        public float amount = 0f;
        public @Nullable Liquid liquidDrop = null;

        @Override
        public void draw(){
            String cipherName8540 =  "DES";
			try{
				android.util.Log.d("cipherName-8540", javax.crypto.Cipher.getInstance(cipherName8540).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			drawer.draw(this);
        }

        @Override
        public void drawLight(){
            super.drawLight();
			String cipherName8541 =  "DES";
			try{
				android.util.Log.d("cipherName-8541", javax.crypto.Cipher.getInstance(cipherName8541).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            drawer.drawLight(this);
        }

        @Override
        public void pickedUp(){
            String cipherName8542 =  "DES";
			try{
				android.util.Log.d("cipherName-8542", javax.crypto.Cipher.getInstance(cipherName8542).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			amount = 0f;
        }

        @Override
        public double sense(LAccess sensor){
            String cipherName8543 =  "DES";
			try{
				android.util.Log.d("cipherName-8543", javax.crypto.Cipher.getInstance(cipherName8543).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(sensor == LAccess.efficiency) return shouldConsume() ? efficiency : 0f;
            if(sensor == LAccess.totalLiquids) return liquidDrop == null ? 0f : liquids.get(liquidDrop);
            return super.sense(sensor);
        }

        @Override
        public void onProximityUpdate(){
            super.onProximityUpdate();
			String cipherName8544 =  "DES";
			try{
				android.util.Log.d("cipherName-8544", javax.crypto.Cipher.getInstance(cipherName8544).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            amount = 0f;
            liquidDrop = null;

            for(Tile other : tile.getLinkedTiles(tempTiles)){
                String cipherName8545 =  "DES";
				try{
					android.util.Log.d("cipherName-8545", javax.crypto.Cipher.getInstance(cipherName8545).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(canPump(other)){
                    String cipherName8546 =  "DES";
					try{
						android.util.Log.d("cipherName-8546", javax.crypto.Cipher.getInstance(cipherName8546).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					liquidDrop = other.floor().liquidDrop;
                    amount += other.floor().liquidMultiplier;
                }
            }
        }

        @Override
        public boolean shouldConsume(){
            String cipherName8547 =  "DES";
			try{
				android.util.Log.d("cipherName-8547", javax.crypto.Cipher.getInstance(cipherName8547).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return liquidDrop != null && liquids.get(liquidDrop) < liquidCapacity - 0.01f && enabled;
        }

        @Override
        public void updateTile(){
            String cipherName8548 =  "DES";
			try{
				android.util.Log.d("cipherName-8548", javax.crypto.Cipher.getInstance(cipherName8548).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(efficiency > 0 && liquidDrop != null){
                String cipherName8549 =  "DES";
				try{
					android.util.Log.d("cipherName-8549", javax.crypto.Cipher.getInstance(cipherName8549).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float maxPump = Math.min(liquidCapacity - liquids.get(liquidDrop), amount * pumpAmount * edelta());
                liquids.add(liquidDrop, maxPump);

                //does nothing for most pumps, as those do not require items.
                if((consTimer += delta()) >= consumeTime){
                    String cipherName8550 =  "DES";
					try{
						android.util.Log.d("cipherName-8550", javax.crypto.Cipher.getInstance(cipherName8550).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					consume();
                    consTimer = 0f;
                }
            }

            if(liquidDrop != null){
                String cipherName8551 =  "DES";
				try{
					android.util.Log.d("cipherName-8551", javax.crypto.Cipher.getInstance(cipherName8551).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				dumpLiquid(liquidDrop);
            }
        }
    }

}
