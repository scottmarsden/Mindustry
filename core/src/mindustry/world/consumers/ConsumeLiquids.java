package mindustry.world.consumers;

import arc.scene.ui.layout.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class ConsumeLiquids extends Consume{
    public final LiquidStack[] liquids;

    public ConsumeLiquids(LiquidStack[] liquids){
        String cipherName9638 =  "DES";
		try{
			android.util.Log.d("cipherName-9638", javax.crypto.Cipher.getInstance(cipherName9638).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.liquids = liquids;
    }

    /** Mods.*/
    protected ConsumeLiquids(){
        this(LiquidStack.empty);
		String cipherName9639 =  "DES";
		try{
			android.util.Log.d("cipherName-9639", javax.crypto.Cipher.getInstance(cipherName9639).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void apply(Block block){
        String cipherName9640 =  "DES";
		try{
			android.util.Log.d("cipherName-9640", javax.crypto.Cipher.getInstance(cipherName9640).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		block.hasLiquids = true;
        for(var stack : liquids){
            String cipherName9641 =  "DES";
			try{
				android.util.Log.d("cipherName-9641", javax.crypto.Cipher.getInstance(cipherName9641).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			block.liquidFilter[stack.liquid.id] = true;
        }
    }

    @Override
    public void build(Building build, Table table){
        String cipherName9642 =  "DES";
		try{
			android.util.Log.d("cipherName-9642", javax.crypto.Cipher.getInstance(cipherName9642).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		table.table(c -> {
            String cipherName9643 =  "DES";
			try{
				android.util.Log.d("cipherName-9643", javax.crypto.Cipher.getInstance(cipherName9643).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int i = 0;
            for(var stack : liquids){
                String cipherName9644 =  "DES";
				try{
					android.util.Log.d("cipherName-9644", javax.crypto.Cipher.getInstance(cipherName9644).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				c.add(new ReqImage(stack.liquid.uiIcon,
                () -> build.liquids.get(stack.liquid) > 0)).size(Vars.iconMed).padRight(8);
                if(++i % 4 == 0) c.row();
            }
        }).left();
    }

    @Override
    public void update(Building build){
        String cipherName9645 =  "DES";
		try{
			android.util.Log.d("cipherName-9645", javax.crypto.Cipher.getInstance(cipherName9645).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float mult = multiplier.get(build);
        for(var stack : liquids){
            String cipherName9646 =  "DES";
			try{
				android.util.Log.d("cipherName-9646", javax.crypto.Cipher.getInstance(cipherName9646).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			build.liquids.remove(stack.liquid, stack.amount * build.edelta() * mult);
        }
    }

    @Override
    public float efficiency(Building build){
        String cipherName9647 =  "DES";
		try{
			android.util.Log.d("cipherName-9647", javax.crypto.Cipher.getInstance(cipherName9647).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float mult = multiplier.get(build);
        float ed = build.edelta();
        if(ed <= 0.00000001f) return 0f;
        float min = 1f;
        for(var stack : liquids){
            String cipherName9648 =  "DES";
			try{
				android.util.Log.d("cipherName-9648", javax.crypto.Cipher.getInstance(cipherName9648).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			min = Math.min(build.liquids.get(stack.liquid) / (stack.amount * ed * mult), min);
        }
        return min;
    }

    @Override
    public void display(Stats stats){
        String cipherName9649 =  "DES";
		try{
			android.util.Log.d("cipherName-9649", javax.crypto.Cipher.getInstance(cipherName9649).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		stats.add(booster ? Stat.booster : Stat.input, StatValues.liquids(1f, true, liquids));
    }

}
