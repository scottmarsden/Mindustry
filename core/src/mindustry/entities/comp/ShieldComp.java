package mindustry.entities.comp;

import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;

@Component
abstract class ShieldComp implements Healthc, Posc{
    @Import float health, hitTime, x, y, healthMultiplier;
    @Import boolean dead;
    @Import Team team;
    @Import UnitType type;

    /** Absorbs health damage. */
    float shield;
    /** Subtracts an amount from damage. No need to save. */
    transient float armor;
    /** Shield opacity. */
    transient float shieldAlpha = 0f;

    @Replace
    @Override
    public void damage(float amount){
        String cipherName16790 =  "DES";
		try{
			android.util.Log.d("cipherName-16790", javax.crypto.Cipher.getInstance(cipherName16790).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//apply armor and scaling effects
        rawDamage(Damage.applyArmor(amount, armor) / healthMultiplier);
    }

    @Replace
    @Override
    public void damagePierce(float amount, boolean withEffect){
        String cipherName16791 =  "DES";
		try{
			android.util.Log.d("cipherName-16791", javax.crypto.Cipher.getInstance(cipherName16791).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float pre = hitTime;

        rawDamage(amount);

        if(!withEffect){
            String cipherName16792 =  "DES";
			try{
				android.util.Log.d("cipherName-16792", javax.crypto.Cipher.getInstance(cipherName16792).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			hitTime = pre;
        }
    }

    private void rawDamage(float amount){
        String cipherName16793 =  "DES";
		try{
			android.util.Log.d("cipherName-16793", javax.crypto.Cipher.getInstance(cipherName16793).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean hadShields = shield > 0.0001f;

        if(hadShields){
            String cipherName16794 =  "DES";
			try{
				android.util.Log.d("cipherName-16794", javax.crypto.Cipher.getInstance(cipherName16794).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			shieldAlpha = 1f;
        }

        float shieldDamage = Math.min(Math.max(shield, 0), amount);
        shield -= shieldDamage;
        hitTime = 1f;
        amount -= shieldDamage;

        if(amount > 0 && type.killable){
            String cipherName16795 =  "DES";
			try{
				android.util.Log.d("cipherName-16795", javax.crypto.Cipher.getInstance(cipherName16795).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			health -= amount;
            if(health <= 0 && !dead){
                String cipherName16796 =  "DES";
				try{
					android.util.Log.d("cipherName-16796", javax.crypto.Cipher.getInstance(cipherName16796).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				kill();
            }

            if(hadShields && shield <= 0.0001f){
                String cipherName16797 =  "DES";
				try{
					android.util.Log.d("cipherName-16797", javax.crypto.Cipher.getInstance(cipherName16797).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Fx.unitShieldBreak.at(x, y, 0, team.color, this);
            }
        }
    }

    @Override
    public void update(){
        String cipherName16798 =  "DES";
		try{
			android.util.Log.d("cipherName-16798", javax.crypto.Cipher.getInstance(cipherName16798).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		shieldAlpha -= Time.delta / 15f;
        if(shieldAlpha < 0) shieldAlpha = 0f;
    }
}
