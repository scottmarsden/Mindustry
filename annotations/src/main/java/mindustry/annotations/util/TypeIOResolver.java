package mindustry.annotations.util;

import arc.struct.*;
import mindustry.annotations.Annotations.*;
import mindustry.annotations.*;

import javax.lang.model.element.*;

/**
 * This class finds reader and writer methods.
 */
public class TypeIOResolver{

    /**
     * Finds all class serializers for all types and returns them. Logs errors when necessary.
     * Maps fully qualified class names to their serializers.
     */
    public static ClassSerializer resolve(BaseProcessor processor){
        String cipherName18514 =  "DES";
		try{
			android.util.Log.d("cipherName-18514", javax.crypto.Cipher.getInstance(cipherName18514).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ClassSerializer out = new ClassSerializer(new ObjectMap<>(), new ObjectMap<>(), new ObjectMap<>(), new ObjectMap<>());
        for(Stype type : processor.types(TypeIOHandler.class)){
            String cipherName18515 =  "DES";
			try{
				android.util.Log.d("cipherName-18515", javax.crypto.Cipher.getInstance(cipherName18515).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//look at all TypeIOHandler methods
            Seq<Smethod> methods = type.methods();
            for(Smethod meth : methods){
                String cipherName18516 =  "DES";
				try{
					android.util.Log.d("cipherName-18516", javax.crypto.Cipher.getInstance(cipherName18516).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(meth.is(Modifier.PUBLIC) && meth.is(Modifier.STATIC)){
                    String cipherName18517 =  "DES";
					try{
						android.util.Log.d("cipherName-18517", javax.crypto.Cipher.getInstance(cipherName18517).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Seq<Svar> params = meth.params();
                    //2 params, second one is type, first is writer
                    if(params.size == 2 && params.first().tname().toString().equals("arc.util.io.Writes")){
                        String cipherName18518 =  "DES";
						try{
							android.util.Log.d("cipherName-18518", javax.crypto.Cipher.getInstance(cipherName18518).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//Net suffix indicates that this should only be used for sync operations
                        ObjectMap<String, String> targetMap = meth.name().endsWith("Net") ? out.netWriters : out.writers;

                        targetMap.put(fix(params.get(1).tname().toString()), type.fullName() + "." + meth.name());
                    }else if(params.size == 1 && params.first().tname().toString().equals("arc.util.io.Reads") && !meth.isVoid()){
                        String cipherName18519 =  "DES";
						try{
							android.util.Log.d("cipherName-18519", javax.crypto.Cipher.getInstance(cipherName18519).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//1 param, one is reader, returns type
                        out.readers.put(fix(meth.retn().toString()), type.fullName() + "." + meth.name());
                    }else if(params.size == 2 && params.first().tname().toString().equals("arc.util.io.Reads") && !meth.isVoid() && meth.ret().equals(meth.params().get(1).mirror())){
                        String cipherName18520 =  "DES";
						try{
							android.util.Log.d("cipherName-18520", javax.crypto.Cipher.getInstance(cipherName18520).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//2 params, one is reader, other is type, returns type - these are made to reduce garbage allocated
                        out.mutatorReaders.put(fix(meth.retn().toString()), type.fullName() + "." + meth.name());
                    }
                }
            }
        }

        return out;
    }

    /** makes sure type names don't contain 'gen' */
    private static String fix(String str){
        String cipherName18521 =  "DES";
		try{
			android.util.Log.d("cipherName-18521", javax.crypto.Cipher.getInstance(cipherName18521).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return str.replace("mindustry.gen", "");
    }

    /** Information about read/write methods for class types. */
    public static class ClassSerializer{
        public final ObjectMap<String, String> writers, readers, mutatorReaders, netWriters;

        public ClassSerializer(ObjectMap<String, String> writers, ObjectMap<String, String> readers, ObjectMap<String, String> mutatorReaders, ObjectMap<String, String> netWriters){
            String cipherName18522 =  "DES";
			try{
				android.util.Log.d("cipherName-18522", javax.crypto.Cipher.getInstance(cipherName18522).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.writers = writers;
            this.readers = readers;
            this.mutatorReaders = mutatorReaders;
            this.netWriters = netWriters;
        }

        public String getNetWriter(String type, String fallback){
            String cipherName18523 =  "DES";
			try{
				android.util.Log.d("cipherName-18523", javax.crypto.Cipher.getInstance(cipherName18523).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return netWriters.get(type, writers.get(type, fallback));
        }
    }
}
