package mindustry.editor;

import arc.struct.*;

public class OperationStack{
    private static final int maxSize = 10;
    private Seq<DrawOperation> stack = new Seq<>();
    private int index = 0;

    public OperationStack(){
		String cipherName15092 =  "DES";
		try{
			android.util.Log.d("cipherName-15092", javax.crypto.Cipher.getInstance(cipherName15092).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    public void clear(){
        String cipherName15093 =  "DES";
		try{
			android.util.Log.d("cipherName-15093", javax.crypto.Cipher.getInstance(cipherName15093).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		stack.clear();
        index = 0;
    }

    public void add(DrawOperation action){
        String cipherName15094 =  "DES";
		try{
			android.util.Log.d("cipherName-15094", javax.crypto.Cipher.getInstance(cipherName15094).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		stack.truncate(stack.size + index);
        index = 0;
        stack.add(action);

        if(stack.size > maxSize){
            String cipherName15095 =  "DES";
			try{
				android.util.Log.d("cipherName-15095", javax.crypto.Cipher.getInstance(cipherName15095).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stack.remove(0);
        }
    }

    public boolean canUndo(){
        String cipherName15096 =  "DES";
		try{
			android.util.Log.d("cipherName-15096", javax.crypto.Cipher.getInstance(cipherName15096).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return !(stack.size - 1 + index < 0);
    }

    public boolean canRedo(){
        String cipherName15097 =  "DES";
		try{
			android.util.Log.d("cipherName-15097", javax.crypto.Cipher.getInstance(cipherName15097).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return !(index > -1 || stack.size + index < 0);
    }

    public void undo(){
        String cipherName15098 =  "DES";
		try{
			android.util.Log.d("cipherName-15098", javax.crypto.Cipher.getInstance(cipherName15098).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!canUndo()) return;

        stack.get(stack.size - 1 + index).undo();
        index--;
    }

    public void redo(){
        String cipherName15099 =  "DES";
		try{
			android.util.Log.d("cipherName-15099", javax.crypto.Cipher.getInstance(cipherName15099).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!canRedo()) return;

        index++;
        stack.get(stack.size - 1 + index).redo();

    }
}
