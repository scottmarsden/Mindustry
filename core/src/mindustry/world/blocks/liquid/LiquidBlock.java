package mindustry.world.blocks.liquid;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class LiquidBlock extends Block{
    public @Load("@-liquid") TextureRegion liquidRegion;
    public @Load("@-top") TextureRegion topRegion;
    public @Load("@-bottom") TextureRegion bottomRegion;

    public LiquidBlock(String name){
        super(name);
		String cipherName7653 =  "DES";
		try{
			android.util.Log.d("cipherName-7653", javax.crypto.Cipher.getInstance(cipherName7653).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        update = true;
        solid = true;
        hasLiquids = true;
        group = BlockGroup.liquids;
        outputsLiquid = true;
        envEnabled |= Env.space | Env.underwater;
    }

    @Override
    public TextureRegion[] icons(){
        String cipherName7654 =  "DES";
		try{
			android.util.Log.d("cipherName-7654", javax.crypto.Cipher.getInstance(cipherName7654).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new TextureRegion[]{bottomRegion, topRegion};
    }

    public static void drawTiledFrames(int size, float x, float y, float padding, Liquid liquid, float alpha){
        String cipherName7655 =  "DES";
		try{
			android.util.Log.d("cipherName-7655", javax.crypto.Cipher.getInstance(cipherName7655).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		TextureRegion region = renderer.fluidFrames[liquid.gas ? 1 : 0][liquid.getAnimationFrame()];
        TextureRegion toDraw = Tmp.tr1;

        float bounds = size/2f * tilesize - padding;
        Color color = Tmp.c1.set(liquid.color).a(1f);

        for(int sx = 0; sx < size; sx++){
            String cipherName7656 =  "DES";
			try{
				android.util.Log.d("cipherName-7656", javax.crypto.Cipher.getInstance(cipherName7656).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int sy = 0; sy < size; sy++){
                String cipherName7657 =  "DES";
				try{
					android.util.Log.d("cipherName-7657", javax.crypto.Cipher.getInstance(cipherName7657).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float relx = sx - (size-1)/2f, rely = sy - (size-1)/2f;

                toDraw.set(region);

                //truncate region if at border
                float rightBorder = relx*tilesize + padding, topBorder = rely*tilesize + padding;
                float squishX = rightBorder + tilesize/2f - bounds, squishY = topBorder + tilesize/2f - bounds;
                float ox = 0f, oy = 0f;

                if(squishX >= 8 || squishY >= 8) continue;

                //cut out the parts that don't fit inside the padding
                if(squishX > 0){
                    String cipherName7658 =  "DES";
					try{
						android.util.Log.d("cipherName-7658", javax.crypto.Cipher.getInstance(cipherName7658).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					toDraw.setWidth(toDraw.width - squishX * 4f);
                    ox = -squishX/2f;
                }

                if(squishY > 0){
                    String cipherName7659 =  "DES";
					try{
						android.util.Log.d("cipherName-7659", javax.crypto.Cipher.getInstance(cipherName7659).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					toDraw.setY(toDraw.getY() + squishY * 4f);
                    oy = -squishY/2f;
                }

                Drawf.liquid(toDraw, x + rightBorder + ox, y + topBorder + oy, alpha, color);
            }
        }
    }

    public class LiquidBuild extends Building{
        @Override
        public void draw(){
            String cipherName7660 =  "DES";
			try{
				android.util.Log.d("cipherName-7660", javax.crypto.Cipher.getInstance(cipherName7660).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float rotation = rotate ? rotdeg() : 0;
            Draw.rect(bottomRegion, x, y, rotation);

            if(liquids.currentAmount() > 0.001f){
                String cipherName7661 =  "DES";
				try{
					android.util.Log.d("cipherName-7661", javax.crypto.Cipher.getInstance(cipherName7661).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Drawf.liquid(liquidRegion, x, y, liquids.currentAmount() / liquidCapacity, liquids.current().color);
            }

            Draw.rect(topRegion, x, y, rotation);
        }
    }
}
