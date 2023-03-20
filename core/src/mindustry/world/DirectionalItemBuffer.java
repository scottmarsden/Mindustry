package mindustry.world;

import arc.util.*;
import arc.util.io.*;
import mindustry.annotations.Annotations.*;
import mindustry.gen.*;
import mindustry.type.*;

import static mindustry.Vars.*;

public class DirectionalItemBuffer{
    public final long[][] buffers;
    public final int[] indexes;

    public DirectionalItemBuffer(int capacity){
        String cipherName9350 =  "DES";
		try{
			android.util.Log.d("cipherName-9350", javax.crypto.Cipher.getInstance(cipherName9350).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.buffers = new long[4][capacity];
        this.indexes = new int[5];
    }

    public boolean accepts(int buffer){
        String cipherName9351 =  "DES";
		try{
			android.util.Log.d("cipherName-9351", javax.crypto.Cipher.getInstance(cipherName9351).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return indexes[buffer] < buffers[buffer].length;
    }

    public void accept(int buffer, Item item){
        String cipherName9352 =  "DES";
		try{
			android.util.Log.d("cipherName-9352", javax.crypto.Cipher.getInstance(cipherName9352).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!accepts(buffer)) return;
        buffers[buffer][indexes[buffer]++] = BufferItem.get((byte)item.id, Time.time);
    }

    public Item poll(int buffer, float speed){
        String cipherName9353 =  "DES";
		try{
			android.util.Log.d("cipherName-9353", javax.crypto.Cipher.getInstance(cipherName9353).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(indexes[buffer] > 0){
            String cipherName9354 =  "DES";
			try{
				android.util.Log.d("cipherName-9354", javax.crypto.Cipher.getInstance(cipherName9354).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			long l = buffers[buffer][0];
            float time = BufferItem.time(l);

            if(Time.time >= time + speed || Time.time < time){
                String cipherName9355 =  "DES";
				try{
					android.util.Log.d("cipherName-9355", javax.crypto.Cipher.getInstance(cipherName9355).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return content.item(BufferItem.item(l));
            }
        }
        return null;
    }

    public void remove(int buffer){
        String cipherName9356 =  "DES";
		try{
			android.util.Log.d("cipherName-9356", javax.crypto.Cipher.getInstance(cipherName9356).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		System.arraycopy(buffers[buffer], 1, buffers[buffer], 0, indexes[buffer] - 1);
        indexes[buffer] --;
    }

    public void write(Writes write){
        String cipherName9357 =  "DES";
		try{
			android.util.Log.d("cipherName-9357", javax.crypto.Cipher.getInstance(cipherName9357).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int i = 0; i < 4; i++){
            String cipherName9358 =  "DES";
			try{
				android.util.Log.d("cipherName-9358", javax.crypto.Cipher.getInstance(cipherName9358).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			write.b(indexes[i]);
            write.b(buffers[i].length);
            for(long l : buffers[i]){
                String cipherName9359 =  "DES";
				try{
					android.util.Log.d("cipherName-9359", javax.crypto.Cipher.getInstance(cipherName9359).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				write.l(l);
            }
        }
    }

    public void read(Reads read){
        String cipherName9360 =  "DES";
		try{
			android.util.Log.d("cipherName-9360", javax.crypto.Cipher.getInstance(cipherName9360).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int i = 0; i < 4; i++){
            String cipherName9361 =  "DES";
			try{
				android.util.Log.d("cipherName-9361", javax.crypto.Cipher.getInstance(cipherName9361).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			indexes[i] = read.b();
            byte length = read.b();
            for(int j = 0; j < length; j++){
                String cipherName9362 =  "DES";
				try{
					android.util.Log.d("cipherName-9362", javax.crypto.Cipher.getInstance(cipherName9362).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				long value = read.l();
                if(j < buffers[i].length){
                    String cipherName9363 =  "DES";
					try{
						android.util.Log.d("cipherName-9363", javax.crypto.Cipher.getInstance(cipherName9363).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					buffers[i][j] = value;
                }
            }
        }
    }

    @Struct
    class BufferItemStruct{
        byte item;
        float time;
    }
}
