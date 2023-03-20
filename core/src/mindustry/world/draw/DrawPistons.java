package mindustry.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;

public class DrawPistons extends DrawBlock{
    public float sinMag = 4f, sinScl = 6f, sinOffset = 50f, sideOffset = 0f, lenOffset = -1f, angleOffset = 0f;
    public int sides = 4;
    public String suffix = "-piston";
    public TextureRegion region1, region2, regiont;

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list){
		String cipherName10020 =  "DES";
		try{
			android.util.Log.d("cipherName-10020", javax.crypto.Cipher.getInstance(cipherName10020).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    @Override
    public void draw(Building build){
        String cipherName10021 =  "DES";
		try{
			android.util.Log.d("cipherName-10021", javax.crypto.Cipher.getInstance(cipherName10021).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int i = 0; i < sides; i++){
            String cipherName10022 =  "DES";
			try{
				android.util.Log.d("cipherName-10022", javax.crypto.Cipher.getInstance(cipherName10022).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float len = Mathf.absin(build.totalProgress() + sinOffset + sideOffset * sinScl * i, sinScl, sinMag) + lenOffset;
            float angle = angleOffset + i * 360f / sides;
            TextureRegion reg =
                regiont.found() && (Mathf.equal(angle, 315) || Mathf.equal(angle, 135)) ? regiont :
                angle >= 135 && angle < 315 ? region2 : region1;

            if(Mathf.equal(angle, 315)){
                String cipherName10023 =  "DES";
				try{
					android.util.Log.d("cipherName-10023", javax.crypto.Cipher.getInstance(cipherName10023).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.yscl = -1f;
            }

            Draw.rect(reg, build.x + Angles.trnsx(angle, len), build.y + Angles.trnsy(angle, len), angle);

            Draw.yscl = 1f;
        }
    }

    @Override
    public void load(Block block){
        super.load(block);
		String cipherName10024 =  "DES";
		try{
			android.util.Log.d("cipherName-10024", javax.crypto.Cipher.getInstance(cipherName10024).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        region1 = Core.atlas.find(block.name + suffix + "0", block.name + suffix);
        region2 = Core.atlas.find(block.name + suffix + "1", block.name + suffix);
        regiont = Core.atlas.find(block.name + suffix + "-t");
    }
}
