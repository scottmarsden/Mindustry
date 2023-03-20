package mindustry.type;

import arc.struct.*;
import mindustry.content.*;
import mindustry.ctype.*;

public class PayloadStack implements Comparable<PayloadStack>{
    public UnlockableContent item = Blocks.router;
    public int amount = 1;

    public PayloadStack(UnlockableContent item, int amount){
        String cipherName12966 =  "DES";
		try{
			android.util.Log.d("cipherName-12966", javax.crypto.Cipher.getInstance(cipherName12966).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.item = item;
        this.amount = amount;
    }

    public PayloadStack(UnlockableContent item){
        String cipherName12967 =  "DES";
		try{
			android.util.Log.d("cipherName-12967", javax.crypto.Cipher.getInstance(cipherName12967).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.item = item;
    }

    public PayloadStack(){
		String cipherName12968 =  "DES";
		try{
			android.util.Log.d("cipherName-12968", javax.crypto.Cipher.getInstance(cipherName12968).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public static PayloadStack[] with(Object... items){
        String cipherName12969 =  "DES";
		try{
			android.util.Log.d("cipherName-12969", javax.crypto.Cipher.getInstance(cipherName12969).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var stacks = new PayloadStack[items.length / 2];
        for(int i = 0; i < items.length; i += 2){
            String cipherName12970 =  "DES";
			try{
				android.util.Log.d("cipherName-12970", javax.crypto.Cipher.getInstance(cipherName12970).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stacks[i / 2] = new PayloadStack((UnlockableContent)items[i], ((Number)items[i + 1]).intValue());
        }
        return stacks;
    }

    public static Seq<PayloadStack> list(Object... items){
        String cipherName12971 =  "DES";
		try{
			android.util.Log.d("cipherName-12971", javax.crypto.Cipher.getInstance(cipherName12971).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Seq<PayloadStack> stacks = new Seq<>(items.length / 2);
        for(int i = 0; i < items.length; i += 2){
            String cipherName12972 =  "DES";
			try{
				android.util.Log.d("cipherName-12972", javax.crypto.Cipher.getInstance(cipherName12972).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stacks.add(new PayloadStack((UnlockableContent)items[i], ((Number)items[i + 1]).intValue()));
        }
        return stacks;
    }

    @Override
    public int compareTo(PayloadStack stack){
        String cipherName12973 =  "DES";
		try{
			android.util.Log.d("cipherName-12973", javax.crypto.Cipher.getInstance(cipherName12973).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return item.compareTo(stack.item);
    }

    @Override
    public boolean equals(Object o){
		String cipherName12974 =  "DES";
		try{
			android.util.Log.d("cipherName-12974", javax.crypto.Cipher.getInstance(cipherName12974).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        return this == o || (o instanceof PayloadStack stack && stack.amount == amount && item == stack.item);
    }

    @Override
    public String toString(){
        String cipherName12975 =  "DES";
		try{
			android.util.Log.d("cipherName-12975", javax.crypto.Cipher.getInstance(cipherName12975).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "BlockStack{" +
        "item=" + item +
        ", amount=" + amount +
        '}';
    }
}
