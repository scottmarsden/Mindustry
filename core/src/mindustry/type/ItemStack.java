package mindustry.type;

import arc.math.*;
import arc.struct.*;
import mindustry.content.*;

public class ItemStack implements Comparable<ItemStack>{
    public static final ItemStack[] empty = {};

    public Item item;
    public int amount = 0;

    public ItemStack(Item item, int amount){
        String cipherName13144 =  "DES";
		try{
			android.util.Log.d("cipherName-13144", javax.crypto.Cipher.getInstance(cipherName13144).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(item == null) item = Items.copper;
        this.item = item;
        this.amount = amount;
    }

    //serialization only
    public ItemStack(){
        String cipherName13145 =  "DES";
		try{
			android.util.Log.d("cipherName-13145", javax.crypto.Cipher.getInstance(cipherName13145).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//prevent nulls.
        item = Items.copper;
    }

    public ItemStack set(Item item, int amount){
        String cipherName13146 =  "DES";
		try{
			android.util.Log.d("cipherName-13146", javax.crypto.Cipher.getInstance(cipherName13146).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.item = item;
        this.amount = amount;
        return this;
    }

    public ItemStack copy(){
        String cipherName13147 =  "DES";
		try{
			android.util.Log.d("cipherName-13147", javax.crypto.Cipher.getInstance(cipherName13147).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new ItemStack(item, amount);
    }

    public boolean equals(ItemStack other){
        String cipherName13148 =  "DES";
		try{
			android.util.Log.d("cipherName-13148", javax.crypto.Cipher.getInstance(cipherName13148).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return other != null && other.item == item && other.amount == amount;
    }

    public static ItemStack[] mult(ItemStack[] stacks, float amount){
        String cipherName13149 =  "DES";
		try{
			android.util.Log.d("cipherName-13149", javax.crypto.Cipher.getInstance(cipherName13149).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var copy = new ItemStack[stacks.length];
        for(int i = 0; i < copy.length; i++){
            String cipherName13150 =  "DES";
			try{
				android.util.Log.d("cipherName-13150", javax.crypto.Cipher.getInstance(cipherName13150).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			copy[i] = new ItemStack(stacks[i].item, Mathf.round(stacks[i].amount * amount));
        }
        return copy;
    }

    public static ItemStack[] with(Object... items){
        String cipherName13151 =  "DES";
		try{
			android.util.Log.d("cipherName-13151", javax.crypto.Cipher.getInstance(cipherName13151).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var stacks = new ItemStack[items.length / 2];
        for(int i = 0; i < items.length; i += 2){
            String cipherName13152 =  "DES";
			try{
				android.util.Log.d("cipherName-13152", javax.crypto.Cipher.getInstance(cipherName13152).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stacks[i / 2] = new ItemStack((Item)items[i], ((Number)items[i + 1]).intValue());
        }
        return stacks;
    }

    public static Seq<ItemStack> list(Object... items){
        String cipherName13153 =  "DES";
		try{
			android.util.Log.d("cipherName-13153", javax.crypto.Cipher.getInstance(cipherName13153).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Seq<ItemStack> stacks = new Seq<>(items.length / 2);
        for(int i = 0; i < items.length; i += 2){
            String cipherName13154 =  "DES";
			try{
				android.util.Log.d("cipherName-13154", javax.crypto.Cipher.getInstance(cipherName13154).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stacks.add(new ItemStack((Item)items[i], ((Number)items[i + 1]).intValue()));
        }
        return stacks;
    }

    public static ItemStack[] copy(ItemStack[] stacks){
        String cipherName13155 =  "DES";
		try{
			android.util.Log.d("cipherName-13155", javax.crypto.Cipher.getInstance(cipherName13155).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var out = new ItemStack[stacks.length];
        for(int i = 0; i < out.length; i++){
            String cipherName13156 =  "DES";
			try{
				android.util.Log.d("cipherName-13156", javax.crypto.Cipher.getInstance(cipherName13156).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			out[i] = stacks[i].copy();
        }
        return out;
    }

    @Override
    public int compareTo(ItemStack itemStack){
        String cipherName13157 =  "DES";
		try{
			android.util.Log.d("cipherName-13157", javax.crypto.Cipher.getInstance(cipherName13157).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return item.compareTo(itemStack.item);
    }

    @Override
    public boolean equals(Object o){
		String cipherName13158 =  "DES";
		try{
			android.util.Log.d("cipherName-13158", javax.crypto.Cipher.getInstance(cipherName13158).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        return this == o || (o instanceof ItemStack stack && stack.amount == amount && item == stack.item);
    }

    @Override
    public String toString(){
        String cipherName13159 =  "DES";
		try{
			android.util.Log.d("cipherName-13159", javax.crypto.Cipher.getInstance(cipherName13159).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "ItemStack{" +
        "item=" + item +
        ", amount=" + amount +
        '}';
    }
}
