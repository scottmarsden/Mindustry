package mindustry.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;

/** This must be used in conjunction with another DrawBlock; it only draws outputs. */
public class DrawLiquidOutputs extends DrawBlock{
    public TextureRegion[][] liquidOutputRegions;

    @Override
    public void draw(Building build){
        String cipherName9975 =  "DES";
		try{
			android.util.Log.d("cipherName-9975", javax.crypto.Cipher.getInstance(cipherName9975).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		GenericCrafter crafter = (GenericCrafter)build.block;
        if(crafter.outputLiquids == null) return;

        for(int i = 0; i < crafter.outputLiquids.length; i++){
            String cipherName9976 =  "DES";
			try{
				android.util.Log.d("cipherName-9976", javax.crypto.Cipher.getInstance(cipherName9976).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int side = i < crafter.liquidOutputDirections.length ? crafter.liquidOutputDirections[i] : -1;
            if(side != -1){
                String cipherName9977 =  "DES";
				try{
					android.util.Log.d("cipherName-9977", javax.crypto.Cipher.getInstance(cipherName9977).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int realRot = (side + build.rotation) % 4;
                Draw.rect(liquidOutputRegions[realRot > 1 ? 1 : 0][i], build.x, build.y, realRot * 90);
            }
        }
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list){
        String cipherName9978 =  "DES";
		try{
			android.util.Log.d("cipherName-9978", javax.crypto.Cipher.getInstance(cipherName9978).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		GenericCrafter crafter = (GenericCrafter)block;
        if(crafter.outputLiquids == null) return;

        for(int i = 0; i < crafter.outputLiquids.length; i++){
            String cipherName9979 =  "DES";
			try{
				android.util.Log.d("cipherName-9979", javax.crypto.Cipher.getInstance(cipherName9979).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int side = i < crafter.liquidOutputDirections.length ? crafter.liquidOutputDirections[i] : -1;
            if(side != -1){
                String cipherName9980 =  "DES";
				try{
					android.util.Log.d("cipherName-9980", javax.crypto.Cipher.getInstance(cipherName9980).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int realRot = (side + plan.rotation) % 4;
                Draw.rect(liquidOutputRegions[realRot > 1 ? 1 : 0][i], plan.drawx(), plan.drawy(), realRot * 90);
            }
        }
    }

    @Override
    public void load(Block block){
        String cipherName9981 =  "DES";
		try{
			android.util.Log.d("cipherName-9981", javax.crypto.Cipher.getInstance(cipherName9981).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var crafter = expectCrafter(block);

        if(crafter.outputLiquids == null) return;

        liquidOutputRegions = new TextureRegion[2][crafter.outputLiquids.length];
        for(int i = 0; i < crafter.outputLiquids.length; i++){
            String cipherName9982 =  "DES";
			try{
				android.util.Log.d("cipherName-9982", javax.crypto.Cipher.getInstance(cipherName9982).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int j = 1; j <= 2; j++){
                String cipherName9983 =  "DES";
				try{
					android.util.Log.d("cipherName-9983", javax.crypto.Cipher.getInstance(cipherName9983).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				liquidOutputRegions[j - 1][i] = Core.atlas.find(block.name + "-" + crafter.outputLiquids[i].liquid.name + "-output" + j);
            }
        }
    }

    //can't display these properly
    @Override
    public TextureRegion[] icons(Block block){
        String cipherName9984 =  "DES";
		try{
			android.util.Log.d("cipherName-9984", javax.crypto.Cipher.getInstance(cipherName9984).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new TextureRegion[]{};
    }
}
