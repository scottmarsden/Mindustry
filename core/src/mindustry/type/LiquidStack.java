package mindustry.type;

import arc.struct.*;
import mindustry.content.*;

public class LiquidStack implements Comparable<LiquidStack>{
    public static final LiquidStack[] empty = {};

    public Liquid liquid;
    public float amount;

    public LiquidStack(Liquid liquid, float amount){
        String cipherName12979 =  "DES";
		try{
			android.util.Log.d("cipherName-12979", javax.crypto.Cipher.getInstance(cipherName12979).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.liquid = liquid;
        this.amount = amount;
    }

    /** serialization only*/
    protected LiquidStack(){
        String cipherName12980 =  "DES";
		try{
			android.util.Log.d("cipherName-12980", javax.crypto.Cipher.getInstance(cipherName12980).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//prevent nulls.
        liquid = Liquids.water;
    }

    public LiquidStack set(Liquid liquid, float amount){
        String cipherName12981 =  "DES";
		try{
			android.util.Log.d("cipherName-12981", javax.crypto.Cipher.getInstance(cipherName12981).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.liquid = liquid;
        this.amount = amount;
        return this;
    }

    public LiquidStack copy(){
        String cipherName12982 =  "DES";
		try{
			android.util.Log.d("cipherName-12982", javax.crypto.Cipher.getInstance(cipherName12982).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new LiquidStack(liquid, amount);
    }

    public boolean equals(LiquidStack other){
        String cipherName12983 =  "DES";
		try{
			android.util.Log.d("cipherName-12983", javax.crypto.Cipher.getInstance(cipherName12983).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return other != null && other.liquid == liquid && other.amount == amount;
    }

    public static LiquidStack[] mult(LiquidStack[] stacks, float amount){
        String cipherName12984 =  "DES";
		try{
			android.util.Log.d("cipherName-12984", javax.crypto.Cipher.getInstance(cipherName12984).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		LiquidStack[] copy = new LiquidStack[stacks.length];
        for(int i = 0; i < copy.length; i++){
            String cipherName12985 =  "DES";
			try{
				android.util.Log.d("cipherName-12985", javax.crypto.Cipher.getInstance(cipherName12985).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			copy[i] = new LiquidStack(stacks[i].liquid, stacks[i].amount * amount);
        }
        return copy;
    }

    public static LiquidStack[] with(Object... items){
        String cipherName12986 =  "DES";
		try{
			android.util.Log.d("cipherName-12986", javax.crypto.Cipher.getInstance(cipherName12986).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		LiquidStack[] stacks = new LiquidStack[items.length / 2];
        for(int i = 0; i < items.length; i += 2){
            String cipherName12987 =  "DES";
			try{
				android.util.Log.d("cipherName-12987", javax.crypto.Cipher.getInstance(cipherName12987).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stacks[i / 2] = new LiquidStack((Liquid)items[i], ((Number)items[i + 1]).floatValue());
        }
        return stacks;
    }

    public static Seq<LiquidStack> list(Object... items){
        String cipherName12988 =  "DES";
		try{
			android.util.Log.d("cipherName-12988", javax.crypto.Cipher.getInstance(cipherName12988).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Seq<LiquidStack> stacks = new Seq<>(items.length / 2);
        for(int i = 0; i < items.length; i += 2){
            String cipherName12989 =  "DES";
			try{
				android.util.Log.d("cipherName-12989", javax.crypto.Cipher.getInstance(cipherName12989).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stacks.add(new LiquidStack((Liquid)items[i], ((Number)items[i + 1]).floatValue()));
        }
        return stacks;
    }

    @Override
    public int compareTo(LiquidStack liquidStack){
        String cipherName12990 =  "DES";
		try{
			android.util.Log.d("cipherName-12990", javax.crypto.Cipher.getInstance(cipherName12990).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return liquid.compareTo(liquidStack.liquid);
    }

    @Override
    public boolean equals(Object o){
		String cipherName12991 =  "DES";
		try{
			android.util.Log.d("cipherName-12991", javax.crypto.Cipher.getInstance(cipherName12991).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(this == o) return true;
        if(!(o instanceof LiquidStack stack)) return false;
        return amount == stack.amount && liquid == stack.liquid;
    }

    @Override
    public String toString(){
        String cipherName12992 =  "DES";
		try{
			android.util.Log.d("cipherName-12992", javax.crypto.Cipher.getInstance(cipherName12992).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "LiquidStack{" +
        "liquid=" + liquid +
        ", amount=" + amount +
        '}';
    }
}
