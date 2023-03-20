package mindustry.world.consumers;

import arc.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

/** Causes a block to explode when explosive items are moved into it. */
public class ConsumeItemExplode extends ConsumeItemFilter{
    public float damage = 4f;
    public float threshold, baseChance = 0.06f;
    public Effect explodeEffect = Fx.generatespark;

    public ConsumeItemExplode(float threshold){
        String cipherName9742 =  "DES";
		try{
			android.util.Log.d("cipherName-9742", javax.crypto.Cipher.getInstance(cipherName9742).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.filter = item -> item.explosiveness >= this.threshold;
        this.threshold = threshold;
    }

    public ConsumeItemExplode(){
        this(0.5f);
		String cipherName9743 =  "DES";
		try{
			android.util.Log.d("cipherName-9743", javax.crypto.Cipher.getInstance(cipherName9743).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void update(Building build){
        String cipherName9744 =  "DES";
		try{
			android.util.Log.d("cipherName-9744", javax.crypto.Cipher.getInstance(cipherName9744).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var item = getConsumed(build);

        if(item != null){
            String cipherName9745 =  "DES";
			try{
				android.util.Log.d("cipherName-9745", javax.crypto.Cipher.getInstance(cipherName9745).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(Vars.state.rules.reactorExplosions && Mathf.chance(build.delta() * baseChance * Mathf.clamp(item.explosiveness - threshold))){
                String cipherName9746 =  "DES";
				try{
					android.util.Log.d("cipherName-9746", javax.crypto.Cipher.getInstance(cipherName9746).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				build.damage(damage);
                explodeEffect.at(build.x + Mathf.range(build.block.size * tilesize / 2f), build.y + Mathf.range(build.block.size * tilesize / 2f));
                Events.fire(Trigger.blastGenerator);
            }
        }
    }

    //as this consumer doesn't actually consume anything, all methods below are empty

    @Override
    public void build(Building build, Table table){
		String cipherName9747 =  "DES";
		try{
			android.util.Log.d("cipherName-9747", javax.crypto.Cipher.getInstance(cipherName9747).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}}

    @Override
    public void trigger(Building build){
		String cipherName9748 =  "DES";
		try{
			android.util.Log.d("cipherName-9748", javax.crypto.Cipher.getInstance(cipherName9748).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}}

    @Override
    public void display(Stats stats){
		String cipherName9749 =  "DES";
		try{
			android.util.Log.d("cipherName-9749", javax.crypto.Cipher.getInstance(cipherName9749).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}}

    @Override
    public void apply(Block block){
		String cipherName9750 =  "DES";
		try{
			android.util.Log.d("cipherName-9750", javax.crypto.Cipher.getInstance(cipherName9750).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}}

    @Override
    public float efficiency(Building build){
        String cipherName9751 =  "DES";
		try{
			android.util.Log.d("cipherName-9751", javax.crypto.Cipher.getInstance(cipherName9751).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return 1f;
    }
}
