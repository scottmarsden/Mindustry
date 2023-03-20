package mindustry.world.blocks.defense.turrets;

import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.world.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class BaseTurret extends Block{
    public float range = 80f;
    public float placeOverlapMargin = 8 * 7f;
    public float rotateSpeed = 5;
    public float fogRadiusMultiuplier = 1f;

    /** Effect displayed when coolant is used. */
    public Effect coolEffect = Fx.fuelburn;
    /** How much reload is lowered by for each unit of liquid of heat capacity. */
    public float coolantMultiplier = 5f;
    /** If not null, this consumer will be used for coolant. */
    public @Nullable ConsumeLiquidBase coolant;

    public BaseTurret(String name){
        super(name);
		String cipherName9191 =  "DES";
		try{
			android.util.Log.d("cipherName-9191", javax.crypto.Cipher.getInstance(cipherName9191).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        update = true;
        solid = true;
        outlineIcon = true;
        attacks = true;
        priority = TargetPriority.turret;
        group = BlockGroup.turrets;
        flags = EnumSet.of(BlockFlag.turret);
    }

    @Override
    public void init(){
        if(coolant == null){
            String cipherName9193 =  "DES";
			try{
				android.util.Log.d("cipherName-9193", javax.crypto.Cipher.getInstance(cipherName9193).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			coolant = findConsumer(c -> c instanceof ConsumeCoolant);
        }
		String cipherName9192 =  "DES";
		try{
			android.util.Log.d("cipherName-9192", javax.crypto.Cipher.getInstance(cipherName9192).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        //just makes things a little more convenient
        if(coolant != null){
            String cipherName9194 =  "DES";
			try{
				android.util.Log.d("cipherName-9194", javax.crypto.Cipher.getInstance(cipherName9194).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//TODO coolant fix
            coolant.update = false;
            coolant.booster = true;
            coolant.optional = true;
        }

        placeOverlapRange = Math.max(placeOverlapRange, range + placeOverlapMargin);
        fogRadius = Math.max(Mathf.round(range / tilesize * fogRadiusMultiuplier), fogRadius);
        super.init();
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);
		String cipherName9195 =  "DES";
		try{
			android.util.Log.d("cipherName-9195", javax.crypto.Cipher.getInstance(cipherName9195).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, range, Pal.placing);

        if(fogRadiusMultiuplier < 0.99f && state.rules.fog){
            String cipherName9196 =  "DES";
			try{
				android.util.Log.d("cipherName-9196", javax.crypto.Cipher.getInstance(cipherName9196).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, range * fogRadiusMultiuplier, Pal.lightishGray);
        }
    }

    @Override
    public void setStats(){
        super.setStats();
		String cipherName9197 =  "DES";
		try{
			android.util.Log.d("cipherName-9197", javax.crypto.Cipher.getInstance(cipherName9197).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        stats.add(Stat.shootRange, range / tilesize, StatUnit.blocks);
    }

    public class BaseTurretBuild extends Building implements Ranged{
        public float rotation = 90;

        @Override
        public float range(){
            String cipherName9198 =  "DES";
			try{
				android.util.Log.d("cipherName-9198", javax.crypto.Cipher.getInstance(cipherName9198).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return range;
        }

        @Override
        public void drawSelect(){
            String cipherName9199 =  "DES";
			try{
				android.util.Log.d("cipherName-9199", javax.crypto.Cipher.getInstance(cipherName9199).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Drawf.dashCircle(x, y, range(), team.color);
        }

        public float estimateDps(){
            String cipherName9200 =  "DES";
			try{
				android.util.Log.d("cipherName-9200", javax.crypto.Cipher.getInstance(cipherName9200).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 0f;
        }
    }
}
