package mindustry.world.draw;

import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;

/** combined several DrawBlocks into one */
public class DrawMulti extends DrawBlock{
    public DrawBlock[] drawers = {};

    public DrawMulti(){
		String cipherName10025 =  "DES";
		try{
			android.util.Log.d("cipherName-10025", javax.crypto.Cipher.getInstance(cipherName10025).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public DrawMulti(DrawBlock... drawers){
        String cipherName10026 =  "DES";
		try{
			android.util.Log.d("cipherName-10026", javax.crypto.Cipher.getInstance(cipherName10026).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.drawers = drawers;
    }

    public DrawMulti(Seq<DrawBlock> drawers){
        String cipherName10027 =  "DES";
		try{
			android.util.Log.d("cipherName-10027", javax.crypto.Cipher.getInstance(cipherName10027).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.drawers = drawers.toArray(DrawBlock.class);
    }

    @Override
    public void getRegionsToOutline(Block block, Seq<TextureRegion> out){
        String cipherName10028 =  "DES";
		try{
			android.util.Log.d("cipherName-10028", javax.crypto.Cipher.getInstance(cipherName10028).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(var draw : drawers){
            String cipherName10029 =  "DES";
			try{
				android.util.Log.d("cipherName-10029", javax.crypto.Cipher.getInstance(cipherName10029).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			draw.getRegionsToOutline(block, out);
        }
    }

    @Override
    public void draw(Building build){
        String cipherName10030 =  "DES";
		try{
			android.util.Log.d("cipherName-10030", javax.crypto.Cipher.getInstance(cipherName10030).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(var draw : drawers){
            String cipherName10031 =  "DES";
			try{
				android.util.Log.d("cipherName-10031", javax.crypto.Cipher.getInstance(cipherName10031).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			draw.draw(build);
        }
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list){
        String cipherName10032 =  "DES";
		try{
			android.util.Log.d("cipherName-10032", javax.crypto.Cipher.getInstance(cipherName10032).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(var draw : drawers){
            String cipherName10033 =  "DES";
			try{
				android.util.Log.d("cipherName-10033", javax.crypto.Cipher.getInstance(cipherName10033).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			draw.drawPlan(block, plan, list);
        }
    }

    @Override
    public void drawLight(Building build){
        String cipherName10034 =  "DES";
		try{
			android.util.Log.d("cipherName-10034", javax.crypto.Cipher.getInstance(cipherName10034).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(var draw : drawers){
            String cipherName10035 =  "DES";
			try{
				android.util.Log.d("cipherName-10035", javax.crypto.Cipher.getInstance(cipherName10035).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			draw.drawLight(build);
        }
    }

    @Override
    public void load(Block block){
        String cipherName10036 =  "DES";
		try{
			android.util.Log.d("cipherName-10036", javax.crypto.Cipher.getInstance(cipherName10036).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(var draw : drawers){
            String cipherName10037 =  "DES";
			try{
				android.util.Log.d("cipherName-10037", javax.crypto.Cipher.getInstance(cipherName10037).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			draw.load(block);
        }
    }

    @Override
    public TextureRegion[] icons(Block block){
        String cipherName10038 =  "DES";
		try{
			android.util.Log.d("cipherName-10038", javax.crypto.Cipher.getInstance(cipherName10038).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var result = new Seq<TextureRegion>();
        for(var draw : drawers){
            String cipherName10039 =  "DES";
			try{
				android.util.Log.d("cipherName-10039", javax.crypto.Cipher.getInstance(cipherName10039).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			result.addAll(draw.icons(block));
        }
        return result.toArray(TextureRegion.class);
    }
}
