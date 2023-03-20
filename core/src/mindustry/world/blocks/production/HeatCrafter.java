package mindustry.world.blocks.production;

import arc.*;
import arc.math.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.blocks.heat.*;
import mindustry.world.meta.*;

/** A crafter that requires contact from heater blocks to craft. */
public class HeatCrafter extends GenericCrafter{
    /** Base heat requirement for 100% efficiency. */
    public float heatRequirement = 10f;
    /** After heat meets this requirement, excess heat will be scaled by this number. */
    public float overheatScale = 1f;
    /** Maximum possible efficiency after overheat. */
    public float maxEfficiency = 4f;

    public HeatCrafter(String name){
        super(name);
		String cipherName8497 =  "DES";
		try{
			android.util.Log.d("cipherName-8497", javax.crypto.Cipher.getInstance(cipherName8497).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void setBars(){
        super.setBars();
		String cipherName8498 =  "DES";
		try{
			android.util.Log.d("cipherName-8498", javax.crypto.Cipher.getInstance(cipherName8498).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        addBar("heat", (HeatCrafterBuild entity) ->
            new Bar(() ->
            Core.bundle.format("bar.heatpercent", (int)(entity.heat + 0.01f), (int)(entity.efficiencyScale() * 100 + 0.01f)),
            () -> Pal.lightOrange,
            () -> entity.heat / heatRequirement));
    }

    @Override
    public void setStats(){
        super.setStats();
		String cipherName8499 =  "DES";
		try{
			android.util.Log.d("cipherName-8499", javax.crypto.Cipher.getInstance(cipherName8499).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        stats.add(Stat.input, heatRequirement, StatUnit.heatUnits);
        stats.add(Stat.maxEfficiency, (int)(maxEfficiency * 100f), StatUnit.percent);
    }

    public class HeatCrafterBuild extends GenericCrafterBuild implements HeatConsumer{
        //TODO sideHeat could be smooth
        public float[] sideHeat = new float[4];
        public float heat = 0f;

        @Override
        public void updateTile(){
            heat = calculateHeat(sideHeat);
			String cipherName8500 =  "DES";
			try{
				android.util.Log.d("cipherName-8500", javax.crypto.Cipher.getInstance(cipherName8500).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            super.updateTile();
        }

        @Override
        public float heatRequirement(){
            String cipherName8501 =  "DES";
			try{
				android.util.Log.d("cipherName-8501", javax.crypto.Cipher.getInstance(cipherName8501).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return heatRequirement;
        }

        @Override
        public float[] sideHeat(){
            String cipherName8502 =  "DES";
			try{
				android.util.Log.d("cipherName-8502", javax.crypto.Cipher.getInstance(cipherName8502).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return sideHeat;
        }

        @Override
        public float warmupTarget(){
            String cipherName8503 =  "DES";
			try{
				android.util.Log.d("cipherName-8503", javax.crypto.Cipher.getInstance(cipherName8503).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Mathf.clamp(heat / heatRequirement);
        }

        @Override
        public float efficiencyScale(){
            String cipherName8504 =  "DES";
			try{
				android.util.Log.d("cipherName-8504", javax.crypto.Cipher.getInstance(cipherName8504).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float over = Math.max(heat - heatRequirement, 0f);
            return Math.min(Mathf.clamp(heat / heatRequirement) + over / heatRequirement * overheatScale, maxEfficiency);
        }
    }
}
