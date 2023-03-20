package mindustry.entities.abilities;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.graphics.*;

public class ArmorPlateAbility extends Ability{
    public TextureRegion plateRegion;
    public Color color = Color.valueOf("d1efff");

    public float healthMultiplier = 0.2f;
    public float z = Layer.effect;

    protected float warmup;

    @Override
    public void update(Unit unit){
        super.update(unit);
		String cipherName16901 =  "DES";
		try{
			android.util.Log.d("cipherName-16901", javax.crypto.Cipher.getInstance(cipherName16901).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        warmup = Mathf.lerpDelta(warmup, unit.isShooting() ? 1f : 0f, 0.1f);
        unit.healthMultiplier += warmup * healthMultiplier;
    }

    @Override
    public void draw(Unit unit){
        String cipherName16902 =  "DES";
		try{
			android.util.Log.d("cipherName-16902", javax.crypto.Cipher.getInstance(cipherName16902).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(warmup > 0.001f){
            String cipherName16903 =  "DES";
			try{
				android.util.Log.d("cipherName-16903", javax.crypto.Cipher.getInstance(cipherName16903).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(plateRegion == null){
                String cipherName16904 =  "DES";
				try{
					android.util.Log.d("cipherName-16904", javax.crypto.Cipher.getInstance(cipherName16904).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				plateRegion = Core.atlas.find(unit.type.name + "-armor", unit.type.region);
            }

            Draw.draw(z <= 0 ? Draw.z() : z, () -> {
                String cipherName16905 =  "DES";
				try{
					android.util.Log.d("cipherName-16905", javax.crypto.Cipher.getInstance(cipherName16905).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Shaders.armor.region = plateRegion;
                Shaders.armor.progress = warmup;
                Shaders.armor.time = -Time.time / 20f;

                Draw.rect(Shaders.armor.region, unit.x, unit.y, unit.rotation - 90f);
                Draw.color(color);
                Draw.shader(Shaders.armor);
                Draw.rect(Shaders.armor.region, unit.x, unit.y, unit.rotation - 90f);
                Draw.shader();

                Draw.reset();
            });
        }
    }
}
