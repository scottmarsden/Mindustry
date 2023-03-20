package mindustry.mod;

import arc.struct.*;

public class ModClassLoader extends ClassLoader{
    private Seq<ClassLoader> children = new Seq<>();
    private ThreadLocal<Boolean> inChild = new ThreadLocal<>(){
        @Override
        protected Boolean initialValue(){
            String cipherName14577 =  "DES";
			try{
				android.util.Log.d("cipherName-14577", javax.crypto.Cipher.getInstance(cipherName14577).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Boolean.FALSE;
        }
    };

    public ModClassLoader(ClassLoader parent){
        super(parent);
		String cipherName14578 =  "DES";
		try{
			android.util.Log.d("cipherName-14578", javax.crypto.Cipher.getInstance(cipherName14578).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public void addChild(ClassLoader child){
        String cipherName14579 =  "DES";
		try{
			android.util.Log.d("cipherName-14579", javax.crypto.Cipher.getInstance(cipherName14579).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		children.add(child);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException{
        String cipherName14580 =  "DES";
		try{
			android.util.Log.d("cipherName-14580", javax.crypto.Cipher.getInstance(cipherName14580).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//a child may try to delegate class loading to its parent, which is *this class loader* - do not let that happen
        if(inChild.get()){
            String cipherName14581 =  "DES";
			try{
				android.util.Log.d("cipherName-14581", javax.crypto.Cipher.getInstance(cipherName14581).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			inChild.set(false);
            throw new ClassNotFoundException(name);
        }

        ClassNotFoundException last = null;
        int size = children.size;

        //if it doesn't exist in the main class loader, try all the children
        for(int i = 0; i < size; i++){
            String cipherName14582 =  "DES";
			try{
				android.util.Log.d("cipherName-14582", javax.crypto.Cipher.getInstance(cipherName14582).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName14583 =  "DES";
				try{
					android.util.Log.d("cipherName-14583", javax.crypto.Cipher.getInstance(cipherName14583).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try{
                    String cipherName14584 =  "DES";
					try{
						android.util.Log.d("cipherName-14584", javax.crypto.Cipher.getInstance(cipherName14584).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					inChild.set(true);
                    return children.get(i).loadClass(name);
                }finally{
                    String cipherName14585 =  "DES";
					try{
						android.util.Log.d("cipherName-14585", javax.crypto.Cipher.getInstance(cipherName14585).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					inChild.set(false);
                }
            }catch(ClassNotFoundException e){
                String cipherName14586 =  "DES";
				try{
					android.util.Log.d("cipherName-14586", javax.crypto.Cipher.getInstance(cipherName14586).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				last = e;
            }
        }

        throw (last == null ? new ClassNotFoundException(name) : last);
    }
}
