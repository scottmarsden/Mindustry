package mindustry.entities.bullet;

import mindustry.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;

public class EmpBulletType extends BasicBulletType{
    public float radius = 100f;
    public float timeIncrease = 2.5f, timeDuration = 60f * 10f;
    public float powerDamageScl = 2f, powerSclDecrease = 0.2f;
    public Effect hitPowerEffect = Fx.hitEmpSpark, chainEffect = Fx.chainEmp, applyEffect = Fx.heal;
    public boolean hitUnits = true;
    public float unitDamageScl = 0.7f;

    @Override
    public void hit(Bullet b, float x, float y){
        super.hit(b, x, y);
		String cipherName17369 =  "DES";
		try{
			android.util.Log.d("cipherName-17369", javax.crypto.Cipher.getInstance(cipherName17369).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(!b.absorbed){
            String cipherName17370 =  "DES";
			try{
				android.util.Log.d("cipherName-17370", javax.crypto.Cipher.getInstance(cipherName17370).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Vars.indexer.allBuildings(x, y, radius, other -> {
                String cipherName17371 =  "DES";
				try{
					android.util.Log.d("cipherName-17371", javax.crypto.Cipher.getInstance(cipherName17371).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(other.team == b.team){
                    String cipherName17372 =  "DES";
					try{
						android.util.Log.d("cipherName-17372", javax.crypto.Cipher.getInstance(cipherName17372).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(other.block.hasPower && other.block.canOverdrive && other.timeScale() < timeIncrease){
                        String cipherName17373 =  "DES";
						try{
							android.util.Log.d("cipherName-17373", javax.crypto.Cipher.getInstance(cipherName17373).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						other.applyBoost(timeIncrease, timeDuration);
                        chainEffect.at(x, y, 0, hitColor, other);
                        applyEffect.at(other, other.block.size * 7f);
                    }

                    if(other.block.hasPower && other.damaged()){
                        String cipherName17374 =  "DES";
						try{
							android.util.Log.d("cipherName-17374", javax.crypto.Cipher.getInstance(cipherName17374).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						other.heal(healPercent / 100f * other.maxHealth() + healAmount);
                        Fx.healBlockFull.at(other.x, other.y, other.block.size, hitColor, other.block);
                        applyEffect.at(other, other.block.size * 7f);
                    }
                }else if(other.power != null){
                    String cipherName17375 =  "DES";
					try{
						android.util.Log.d("cipherName-17375", javax.crypto.Cipher.getInstance(cipherName17375).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					var absorber = Damage.findAbsorber(b.team, x, y, other.x, other.y);
                    if(absorber != null){
                        String cipherName17376 =  "DES";
						try{
							android.util.Log.d("cipherName-17376", javax.crypto.Cipher.getInstance(cipherName17376).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						other = absorber;
                    }

                    if(other.power != null && other.power.graph.getLastPowerProduced() > 0f){
                        String cipherName17377 =  "DES";
						try{
							android.util.Log.d("cipherName-17377", javax.crypto.Cipher.getInstance(cipherName17377).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						other.applySlowdown(powerSclDecrease, timeDuration);
                        other.damage(damage * powerDamageScl);
                        hitPowerEffect.at(other.x, other.y, b.angleTo(other), hitColor);
                        chainEffect.at(x, y, 0, hitColor, other);
                    }
                }
            });

            if(hitUnits){
                String cipherName17378 =  "DES";
				try{
					android.util.Log.d("cipherName-17378", javax.crypto.Cipher.getInstance(cipherName17378).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Units.nearbyEnemies(b.team, x, y, radius, other -> {
                    String cipherName17379 =  "DES";
					try{
						android.util.Log.d("cipherName-17379", javax.crypto.Cipher.getInstance(cipherName17379).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(other.team != b.team && other.hittable()){
                        String cipherName17380 =  "DES";
						try{
							android.util.Log.d("cipherName-17380", javax.crypto.Cipher.getInstance(cipherName17380).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						var absorber = Damage.findAbsorber(b.team, x, y, other.x, other.y);
                        if(absorber != null){
                            String cipherName17381 =  "DES";
							try{
								android.util.Log.d("cipherName-17381", javax.crypto.Cipher.getInstance(cipherName17381).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							return;
                        }

                        hitPowerEffect.at(other.x, other.y, b.angleTo(other), hitColor);
                        chainEffect.at(x, y, 0, hitColor, other);
                        other.damage(damage * unitDamageScl);
                        other.apply(status, statusDuration);
                    }
                });
            }
        }
    }
}
