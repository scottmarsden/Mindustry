package mindustry.entities.comp;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.input.*;
import mindustry.type.*;
import mindustry.world.*;

import static mindustry.Vars.*;

@Component
abstract class MinerComp implements Itemsc, Posc, Teamc, Rotc, Drawc{
    @Import float x, y, rotation, hitSize;
    @Import UnitType type;

    transient float mineTimer;
    @Nullable @SyncLocal Tile mineTile;

    public boolean canMine(@Nullable Item item){
        String cipherName16615 =  "DES";
		try{
			android.util.Log.d("cipherName-16615", javax.crypto.Cipher.getInstance(cipherName16615).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(item == null) return false;
        return type.mineTier >= item.hardness;
    }

    public boolean offloadImmediately(){
        String cipherName16616 =  "DES";
		try{
			android.util.Log.d("cipherName-16616", javax.crypto.Cipher.getInstance(cipherName16616).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return this.<Unit>self().isPlayer();
    }

    boolean mining(){
        String cipherName16617 =  "DES";
		try{
			android.util.Log.d("cipherName-16617", javax.crypto.Cipher.getInstance(cipherName16617).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return mineTile != null && !this.<Unit>self().activelyBuilding();
    }

    public @Nullable Item getMineResult(@Nullable Tile tile){
        String cipherName16618 =  "DES";
		try{
			android.util.Log.d("cipherName-16618", javax.crypto.Cipher.getInstance(cipherName16618).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(tile == null) return null;
        Item result;
        if(type.mineFloor && tile.block() == Blocks.air){
            String cipherName16619 =  "DES";
			try{
				android.util.Log.d("cipherName-16619", javax.crypto.Cipher.getInstance(cipherName16619).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			result = tile.drop();
        }else if(type.mineWalls){
            String cipherName16620 =  "DES";
			try{
				android.util.Log.d("cipherName-16620", javax.crypto.Cipher.getInstance(cipherName16620).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			result = tile.wallDrop();
        }else{
            String cipherName16621 =  "DES";
			try{
				android.util.Log.d("cipherName-16621", javax.crypto.Cipher.getInstance(cipherName16621).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        return canMine(result) ? result : null;
    }

    public boolean validMine(Tile tile, boolean checkDst){
        String cipherName16622 =  "DES";
		try{
			android.util.Log.d("cipherName-16622", javax.crypto.Cipher.getInstance(cipherName16622).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(tile == null) return false;

        if(checkDst && !within(tile.worldx(), tile.worldy(), type.mineRange)){
            String cipherName16623 =  "DES";
			try{
				android.util.Log.d("cipherName-16623", javax.crypto.Cipher.getInstance(cipherName16623).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        return getMineResult(tile) != null;
    }

    public boolean validMine(Tile tile){
        String cipherName16624 =  "DES";
		try{
			android.util.Log.d("cipherName-16624", javax.crypto.Cipher.getInstance(cipherName16624).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return validMine(tile, true);
    }

    public boolean canMine(){
        String cipherName16625 =  "DES";
		try{
			android.util.Log.d("cipherName-16625", javax.crypto.Cipher.getInstance(cipherName16625).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return type.mineSpeed > 0 && type.mineTier >= 0;
    }

    @Override
    public void update(){
        String cipherName16626 =  "DES";
		try{
			android.util.Log.d("cipherName-16626", javax.crypto.Cipher.getInstance(cipherName16626).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(mineTile == null) return;

        Building core = closestCore();
        Item item = getMineResult(mineTile);

        if(core != null && item != null && !acceptsItem(item) && within(core, mineTransferRange) && !offloadImmediately()){
            String cipherName16627 =  "DES";
			try{
				android.util.Log.d("cipherName-16627", javax.crypto.Cipher.getInstance(cipherName16627).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int accepted = core.acceptStack(item(), stack().amount, this);
            if(accepted > 0){
                String cipherName16628 =  "DES";
				try{
					android.util.Log.d("cipherName-16628", javax.crypto.Cipher.getInstance(cipherName16628).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Call.transferItemTo(self(), item(), accepted,
                mineTile.worldx() + Mathf.range(tilesize / 2f),
                mineTile.worldy() + Mathf.range(tilesize / 2f), core);
                clearItem();
            }
        }

        if((!net.client() || isLocal()) && !validMine(mineTile)){
            String cipherName16629 =  "DES";
			try{
				android.util.Log.d("cipherName-16629", javax.crypto.Cipher.getInstance(cipherName16629).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mineTile = null;
            mineTimer = 0f;
        }else if(mining() && item != null){
            String cipherName16630 =  "DES";
			try{
				android.util.Log.d("cipherName-16630", javax.crypto.Cipher.getInstance(cipherName16630).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mineTimer += Time.delta * type.mineSpeed;

            if(Mathf.chance(0.06 * Time.delta)){
                String cipherName16631 =  "DES";
				try{
					android.util.Log.d("cipherName-16631", javax.crypto.Cipher.getInstance(cipherName16631).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Fx.pulverizeSmall.at(mineTile.worldx() + Mathf.range(tilesize / 2f), mineTile.worldy() + Mathf.range(tilesize / 2f), 0f, item.color);
            }

            if(mineTimer >= 50f + (type.mineHardnessScaling ? item.hardness*15f : 15f)){
                String cipherName16632 =  "DES";
				try{
					android.util.Log.d("cipherName-16632", javax.crypto.Cipher.getInstance(cipherName16632).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mineTimer = 0;

                if(state.rules.sector != null && team() == state.rules.defaultTeam) state.rules.sector.info.handleProduction(item, 1);

                if(core != null && within(core, mineTransferRange) && core.acceptStack(item, 1, this) == 1 && offloadImmediately()){
                    String cipherName16633 =  "DES";
					try{
						android.util.Log.d("cipherName-16633", javax.crypto.Cipher.getInstance(cipherName16633).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//add item to inventory before it is transferred
                    if(item() == item && !net.client()) addItem(item);
                    Call.transferItemTo(self(), item, 1,
                    mineTile.worldx() + Mathf.range(tilesize / 2f),
                    mineTile.worldy() + Mathf.range(tilesize / 2f), core);
                }else if(acceptsItem(item)){
                    String cipherName16634 =  "DES";
					try{
						android.util.Log.d("cipherName-16634", javax.crypto.Cipher.getInstance(cipherName16634).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//this is clientside, since items are synced anyway
                    InputHandler.transferItemToUnit(item,
                    mineTile.worldx() + Mathf.range(tilesize / 2f),
                    mineTile.worldy() + Mathf.range(tilesize / 2f),
                    this);
                }else{
                    String cipherName16635 =  "DES";
					try{
						android.util.Log.d("cipherName-16635", javax.crypto.Cipher.getInstance(cipherName16635).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					mineTile = null;
                    mineTimer = 0f;
                }
            }

            if(!headless){
                String cipherName16636 =  "DES";
				try{
					android.util.Log.d("cipherName-16636", javax.crypto.Cipher.getInstance(cipherName16636).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				control.sound.loop(type.mineSound, this, type.mineSoundVolume);
            }
        }
    }

    @Override
    public void draw(){
        String cipherName16637 =  "DES";
		try{
			android.util.Log.d("cipherName-16637", javax.crypto.Cipher.getInstance(cipherName16637).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!mining()) return;
        float focusLen = hitSize / 2f + Mathf.absin(Time.time, 1.1f, 0.5f);
        float swingScl = 12f, swingMag = tilesize / 8f;
        float flashScl = 0.3f;

        float px = x + Angles.trnsx(rotation, focusLen);
        float py = y + Angles.trnsy(rotation, focusLen);

        float ex = mineTile.worldx() + Mathf.sin(Time.time + 48, swingScl, swingMag);
        float ey = mineTile.worldy() + Mathf.sin(Time.time + 48, swingScl + 2f, swingMag);

        Draw.z(Layer.flyingUnit + 0.1f);

        Draw.color(Color.lightGray, Color.white, 1f - flashScl + Mathf.absin(Time.time, 0.5f, flashScl));

        Drawf.laser(Core.atlas.find("minelaser"), Core.atlas.find("minelaser-end"), px, py, ex, ey, 0.75f);

        if(isLocal()){
            String cipherName16638 =  "DES";
			try{
				android.util.Log.d("cipherName-16638", javax.crypto.Cipher.getInstance(cipherName16638).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Lines.stroke(1f, Pal.accent);
            Lines.poly(mineTile.worldx(), mineTile.worldy(), 4, tilesize / 2f * Mathf.sqrt2, Time.time);
        }

        Draw.color();
    }
}
