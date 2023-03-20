package mindustry.world.consumers;

import arc.scene.ui.layout.*;
import arc.struct.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.meta.*;

public class ConsumePayloads extends Consume{
    public Seq<PayloadStack> payloads;

    public ConsumePayloads(Seq<PayloadStack> payloads){
        String cipherName9667 =  "DES";
		try{
			android.util.Log.d("cipherName-9667", javax.crypto.Cipher.getInstance(cipherName9667).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.payloads = payloads;
    }

    @Override
    public float efficiency(Building build){
        String cipherName9668 =  "DES";
		try{
			android.util.Log.d("cipherName-9668", javax.crypto.Cipher.getInstance(cipherName9668).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float mult = multiplier.get(build);
        for(PayloadStack stack : payloads){
            String cipherName9669 =  "DES";
			try{
				android.util.Log.d("cipherName-9669", javax.crypto.Cipher.getInstance(cipherName9669).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!build.getPayloads().contains(stack.item, Math.round(stack.amount * mult))){
                String cipherName9670 =  "DES";
				try{
					android.util.Log.d("cipherName-9670", javax.crypto.Cipher.getInstance(cipherName9670).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return 0f;
            }
        }
        return 1f;
    }

    @Override
    public void trigger(Building build){
        String cipherName9671 =  "DES";
		try{
			android.util.Log.d("cipherName-9671", javax.crypto.Cipher.getInstance(cipherName9671).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float mult = multiplier.get(build);
        for(PayloadStack stack : payloads){
            String cipherName9672 =  "DES";
			try{
				android.util.Log.d("cipherName-9672", javax.crypto.Cipher.getInstance(cipherName9672).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			build.getPayloads().remove(stack.item, Math.round(stack.amount * mult));
        }
    }

    @Override
    public void display(Stats stats){

        String cipherName9673 =  "DES";
		try{
			android.util.Log.d("cipherName-9673", javax.crypto.Cipher.getInstance(cipherName9673).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(var stack : payloads){
            String cipherName9674 =  "DES";
			try{
				android.util.Log.d("cipherName-9674", javax.crypto.Cipher.getInstance(cipherName9674).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stats.add(Stat.input, t -> {
                String cipherName9675 =  "DES";
				try{
					android.util.Log.d("cipherName-9675", javax.crypto.Cipher.getInstance(cipherName9675).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				t.add(new ItemImage(stack));
                t.add(stack.item.localizedName).padLeft(4).padRight(4);
            });
        }
    }

    @Override
    public void build(Building build, Table table){
        String cipherName9676 =  "DES";
		try{
			android.util.Log.d("cipherName-9676", javax.crypto.Cipher.getInstance(cipherName9676).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var inv = build.getPayloads();

        table.table(c -> {
            String cipherName9677 =  "DES";
			try{
				android.util.Log.d("cipherName-9677", javax.crypto.Cipher.getInstance(cipherName9677).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int i = 0;
            for(var stack : payloads){
                String cipherName9678 =  "DES";
				try{
					android.util.Log.d("cipherName-9678", javax.crypto.Cipher.getInstance(cipherName9678).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				c.add(new ReqImage(new ItemImage(stack.item.uiIcon, Math.round(stack.amount * multiplier.get(build))),
                () -> inv.contains(stack.item, Math.round(stack.amount * multiplier.get(build))))).padRight(8);
                if(++i % 4 == 0) c.row();
            }
        }).left();
    }
}
