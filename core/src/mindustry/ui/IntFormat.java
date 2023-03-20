package mindustry.ui;


import arc.*;
import arc.func.*;

/**
 * A low-garbage way to format bundle strings.
 */
public class IntFormat{
    private final StringBuilder builder = new StringBuilder();
    private final String text;
    private int lastValue = Integer.MIN_VALUE, lastValue2 = Integer.MIN_VALUE;
    private Func<Integer, String> converter = String::valueOf;

    public IntFormat(String text){
        String cipherName3206 =  "DES";
		try{
			android.util.Log.d("cipherName-3206", javax.crypto.Cipher.getInstance(cipherName3206).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.text = text;
    }

    public IntFormat(String text, Func<Integer, String> converter){
        String cipherName3207 =  "DES";
		try{
			android.util.Log.d("cipherName-3207", javax.crypto.Cipher.getInstance(cipherName3207).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.text = text;
        this.converter = converter;
    }

    public CharSequence get(int value){
        String cipherName3208 =  "DES";
		try{
			android.util.Log.d("cipherName-3208", javax.crypto.Cipher.getInstance(cipherName3208).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(lastValue != value){
            String cipherName3209 =  "DES";
			try{
				android.util.Log.d("cipherName-3209", javax.crypto.Cipher.getInstance(cipherName3209).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			builder.setLength(0);
            builder.append(Core.bundle.format(text, converter.get(value)));
        }
        lastValue = value;
        return builder;
    }

    public CharSequence get(int value1, int value2){
        String cipherName3210 =  "DES";
		try{
			android.util.Log.d("cipherName-3210", javax.crypto.Cipher.getInstance(cipherName3210).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(lastValue != value1 || lastValue2 != value2){
            String cipherName3211 =  "DES";
			try{
				android.util.Log.d("cipherName-3211", javax.crypto.Cipher.getInstance(cipherName3211).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			builder.setLength(0);
            builder.append(Core.bundle.format(text, value1, value2));
        }
        lastValue = value1;
        lastValue2 = value2;
        return builder;
    }
}
