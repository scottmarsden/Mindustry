package mindustry.world.consumers;

import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class ConsumeItems extends Consume{
    public final ItemStack[] items;

    public ConsumeItems(ItemStack[] items){
        String cipherName9705 =  "DES";
		try{
			android.util.Log.d("cipherName-9705", javax.crypto.Cipher.getInstance(cipherName9705).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.items = items;
    }

    /** Mods.*/
    protected ConsumeItems(){
        this(ItemStack.empty);
		String cipherName9706 =  "DES";
		try{
			android.util.Log.d("cipherName-9706", javax.crypto.Cipher.getInstance(cipherName9706).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void apply(Block block){
        String cipherName9707 =  "DES";
		try{
			android.util.Log.d("cipherName-9707", javax.crypto.Cipher.getInstance(cipherName9707).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		block.hasItems = true;
        block.acceptsItems = true;
        for(var stack : items){
            String cipherName9708 =  "DES";
			try{
				android.util.Log.d("cipherName-9708", javax.crypto.Cipher.getInstance(cipherName9708).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			block.itemFilter[stack.item.id] = true;
        }
    }

    @Override
    public void build(Building build, Table table){
        String cipherName9709 =  "DES";
		try{
			android.util.Log.d("cipherName-9709", javax.crypto.Cipher.getInstance(cipherName9709).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		table.table(c -> {
            String cipherName9710 =  "DES";
			try{
				android.util.Log.d("cipherName-9710", javax.crypto.Cipher.getInstance(cipherName9710).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int i = 0;
            for(var stack : items){
                String cipherName9711 =  "DES";
				try{
					android.util.Log.d("cipherName-9711", javax.crypto.Cipher.getInstance(cipherName9711).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				c.add(new ReqImage(new ItemImage(stack.item.uiIcon, Math.round(stack.amount * multiplier.get(build))),
                () -> build.items.has(stack.item, Math.round(stack.amount * multiplier.get(build))))).padRight(8);
                if(++i % 4 == 0) c.row();
            }
        }).left();
    }

    @Override
    public void trigger(Building build){
        String cipherName9712 =  "DES";
		try{
			android.util.Log.d("cipherName-9712", javax.crypto.Cipher.getInstance(cipherName9712).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(var stack : items){
            String cipherName9713 =  "DES";
			try{
				android.util.Log.d("cipherName-9713", javax.crypto.Cipher.getInstance(cipherName9713).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			build.items.remove(stack.item, Math.round(stack.amount * multiplier.get(build)));
        }
    }

    @Override
    public float efficiency(Building build){
        String cipherName9714 =  "DES";
		try{
			android.util.Log.d("cipherName-9714", javax.crypto.Cipher.getInstance(cipherName9714).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return build.consumeTriggerValid() || build.items.has(items, multiplier.get(build)) ? 1f : 0f;
    }

    @Override
    public void display(Stats stats){
        String cipherName9715 =  "DES";
		try{
			android.util.Log.d("cipherName-9715", javax.crypto.Cipher.getInstance(cipherName9715).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		stats.add(booster ? Stat.booster : Stat.input, stats.timePeriod < 0 ? StatValues.items(items) : StatValues.items(stats.timePeriod, items));
    }
}
