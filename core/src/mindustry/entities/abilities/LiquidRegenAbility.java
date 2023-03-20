package mindustry.entities.abilities;

import arc.math.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public class LiquidRegenAbility extends Ability{
    public Liquid liquid;
    public float slurpSpeed = 9f;
    public float regenPerSlurp = 2.9f;
    public float slurpEffectChance = 0.4f;
    public Effect slurpEffect = Fx.heal;

    @Override
    public void update(Unit unit){
        //TODO timer?

        String cipherName16849 =  "DES";
		try{
			android.util.Log.d("cipherName-16849", javax.crypto.Cipher.getInstance(cipherName16849).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//TODO effects?
        if(unit.damaged()){
            String cipherName16850 =  "DES";
			try{
				android.util.Log.d("cipherName-16850", javax.crypto.Cipher.getInstance(cipherName16850).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			boolean healed = false;
            int tx = unit.tileX(), ty = unit.tileY();
            int rad = Math.max((int)(unit.hitSize / tilesize * 0.6f), 1);
            for(int x = -rad; x <= rad; x++){
                String cipherName16851 =  "DES";
				try{
					android.util.Log.d("cipherName-16851", javax.crypto.Cipher.getInstance(cipherName16851).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(int y = -rad; y <= rad; y++){
                    String cipherName16852 =  "DES";
					try{
						android.util.Log.d("cipherName-16852", javax.crypto.Cipher.getInstance(cipherName16852).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(x*x + y*y <= rad*rad){

                        String cipherName16853 =  "DES";
						try{
							android.util.Log.d("cipherName-16853", javax.crypto.Cipher.getInstance(cipherName16853).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Tile tile = world.tile(tx + x, ty + y);
                        if(tile != null){
                            String cipherName16854 =  "DES";
							try{
								android.util.Log.d("cipherName-16854", javax.crypto.Cipher.getInstance(cipherName16854).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							Puddle puddle = Puddles.get(tile);
                            if(puddle != null && puddle.liquid == liquid){
                                String cipherName16855 =  "DES";
								try{
									android.util.Log.d("cipherName-16855", javax.crypto.Cipher.getInstance(cipherName16855).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								float fractionTaken = Math.min(puddle.amount, (slurpSpeed * Time.delta));
                                puddle.amount -= Math.min(puddle.amount, slurpSpeed * Time.delta);
                                unit.heal(fractionTaken * regenPerSlurp);
                                healed = true;
                            }
                        }
                    }
                }
            }

            if(healed && Mathf.chanceDelta(slurpEffectChance)){
                String cipherName16856 =  "DES";
				try{
					android.util.Log.d("cipherName-16856", javax.crypto.Cipher.getInstance(cipherName16856).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Tmp.v1.rnd(Mathf.random(unit.hitSize/2f));
                slurpEffect.at(unit.x + Tmp.v1.x, unit.y + Tmp.v1.y, unit.rotation, unit);
            }
        }
    }
}
