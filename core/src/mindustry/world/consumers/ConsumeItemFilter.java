package mindustry.world.consumers;

import arc.func.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class ConsumeItemFilter extends Consume{
    public Boolf<Item> filter = i -> false;

    public ConsumeItemFilter(Boolf<Item> item){
        String cipherName9681 =  "DES";
		try{
			android.util.Log.d("cipherName-9681", javax.crypto.Cipher.getInstance(cipherName9681).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.filter = item;
    }

    public ConsumeItemFilter(){
		String cipherName9682 =  "DES";
		try{
			android.util.Log.d("cipherName-9682", javax.crypto.Cipher.getInstance(cipherName9682).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void apply(Block block){
        String cipherName9683 =  "DES";
		try{
			android.util.Log.d("cipherName-9683", javax.crypto.Cipher.getInstance(cipherName9683).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		block.hasItems = true;
        block.acceptsItems = true;
        content.items().each(filter, item -> block.itemFilter[item.id] = true);
    }

    @Override
    public void build(Building build, Table table){
        String cipherName9684 =  "DES";
		try{
			android.util.Log.d("cipherName-9684", javax.crypto.Cipher.getInstance(cipherName9684).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		MultiReqImage image = new MultiReqImage();
        content.items().each(i -> filter.get(i) && i.unlockedNow(), item -> image.add(new ReqImage(new ItemImage(item.uiIcon, 1),
            () -> build.items.has(item))));

        table.add(image).size(8 * 4);
    }

    @Override
    public void update(Building build){
		String cipherName9685 =  "DES";
		try{
			android.util.Log.d("cipherName-9685", javax.crypto.Cipher.getInstance(cipherName9685).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void trigger(Building build){
        String cipherName9686 =  "DES";
		try{
			android.util.Log.d("cipherName-9686", javax.crypto.Cipher.getInstance(cipherName9686).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Item item = getConsumed(build);
        if(item != null){
            String cipherName9687 =  "DES";
			try{
				android.util.Log.d("cipherName-9687", javax.crypto.Cipher.getInstance(cipherName9687).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			build.items.remove(item, 1);
        }
    }

    @Override
    public float efficiency(Building build){
        String cipherName9688 =  "DES";
		try{
			android.util.Log.d("cipherName-9688", javax.crypto.Cipher.getInstance(cipherName9688).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return build.consumeTriggerValid() || getConsumed(build) != null ? 1f : 0f;
    }

    public @Nullable Item getConsumed(Building build){
        String cipherName9689 =  "DES";
		try{
			android.util.Log.d("cipherName-9689", javax.crypto.Cipher.getInstance(cipherName9689).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int i = 0; i < content.items().size; i++){
            String cipherName9690 =  "DES";
			try{
				android.util.Log.d("cipherName-9690", javax.crypto.Cipher.getInstance(cipherName9690).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Item item = content.item(i);
            if(build.items.has(item) && this.filter.get(item)){
                String cipherName9691 =  "DES";
				try{
					android.util.Log.d("cipherName-9691", javax.crypto.Cipher.getInstance(cipherName9691).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return item;
            }
        }
        return null;
    }

    @Override
    public void display(Stats stats){
        String cipherName9692 =  "DES";
		try{
			android.util.Log.d("cipherName-9692", javax.crypto.Cipher.getInstance(cipherName9692).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		stats.add(booster ? Stat.booster : Stat.input, stats.timePeriod < 0 ? StatValues.items(filter) : StatValues.items(stats.timePeriod, filter));
    }
}
