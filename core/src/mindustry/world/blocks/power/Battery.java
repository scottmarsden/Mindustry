package mindustry.world.blocks.power;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import mindustry.annotations.Annotations.*;
import mindustry.gen.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class Battery extends PowerDistributor{
    public @Load("@-top") TextureRegion topRegion;

    public Color emptyLightColor = Color.valueOf("f8c266");
    public Color fullLightColor = Color.valueOf("fb9567");

    public Battery(String name){
        super(name);
		String cipherName6352 =  "DES";
		try{
			android.util.Log.d("cipherName-6352", javax.crypto.Cipher.getInstance(cipherName6352).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        outputsPower = true;
        consumesPower = true;
        canOverdrive = false;
        flags = EnumSet.of(BlockFlag.battery);
        //TODO could be supported everywhere...
        envEnabled |= Env.space;
        destructible = true;
        //batteries don't need to update
        update = false;
    }

    public class BatteryBuild extends Building{
        @Override
        public void draw(){
            String cipherName6353 =  "DES";
			try{
				android.util.Log.d("cipherName-6353", javax.crypto.Cipher.getInstance(cipherName6353).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.color(emptyLightColor, fullLightColor, power.status);
            Fill.square(x, y, (tilesize * size / 2f - 1) * Draw.xscl);
            Draw.color();

            Draw.rect(topRegion, x, y);
        }

        @Override
        public void overwrote(Seq<Building> previous){
            String cipherName6354 =  "DES";
			try{
				android.util.Log.d("cipherName-6354", javax.crypto.Cipher.getInstance(cipherName6354).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(Building other : previous){
                String cipherName6355 =  "DES";
				try{
					android.util.Log.d("cipherName-6355", javax.crypto.Cipher.getInstance(cipherName6355).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(other.power != null && other.block.consPower != null && other.block.consPower.buffered){
                    String cipherName6356 =  "DES";
					try{
						android.util.Log.d("cipherName-6356", javax.crypto.Cipher.getInstance(cipherName6356).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					float amount = other.block.consPower.capacity * other.power.status;
                    power.status = Mathf.clamp(power.status + amount / consPower.capacity);
                }
            }
        }

        @Override
        public BlockStatus status(){
            String cipherName6357 =  "DES";
			try{
				android.util.Log.d("cipherName-6357", javax.crypto.Cipher.getInstance(cipherName6357).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(Mathf.equal(power.status, 0f, 0.001f)) return BlockStatus.noInput;
            if(Mathf.equal(power.status, 1f, 0.001f)) return BlockStatus.active;
            return BlockStatus.noOutput;
        }
    }
}
