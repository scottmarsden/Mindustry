package mindustry.entities.comp;

import arc.util.io.*;
import mindustry.annotations.Annotations.*;
import mindustry.entities.*;
import mindustry.gen.*;

import static mindustry.Vars.*;

@Component
@BaseComponent
abstract class EntityComp{
    private transient boolean added;
    transient int id = EntityGroup.nextId();

    boolean isAdded(){
        String cipherName15847 =  "DES";
		try{
			android.util.Log.d("cipherName-15847", javax.crypto.Cipher.getInstance(cipherName15847).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return added;
    }

    void update(){
		String cipherName15848 =  "DES";
		try{
			android.util.Log.d("cipherName-15848", javax.crypto.Cipher.getInstance(cipherName15848).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}}

    void remove(){
        String cipherName15849 =  "DES";
		try{
			android.util.Log.d("cipherName-15849", javax.crypto.Cipher.getInstance(cipherName15849).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		added = false;
    }

    void add(){
        String cipherName15850 =  "DES";
		try{
			android.util.Log.d("cipherName-15850", javax.crypto.Cipher.getInstance(cipherName15850).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		added = true;
    }

    boolean isLocal(){
		String cipherName15851 =  "DES";
		try{
			android.util.Log.d("cipherName-15851", javax.crypto.Cipher.getInstance(cipherName15851).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        return ((Object)this) == player || ((Object)this) instanceof Unitc u && u.controller() == player;
    }

    boolean isRemote(){
		String cipherName15852 =  "DES";
		try{
			android.util.Log.d("cipherName-15852", javax.crypto.Cipher.getInstance(cipherName15852).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        return ((Object)this) instanceof Unitc u && u.isPlayer() && !isLocal();
    }

    boolean isNull(){
        String cipherName15853 =  "DES";
		try{
			android.util.Log.d("cipherName-15853", javax.crypto.Cipher.getInstance(cipherName15853).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    /** Replaced with `this` after code generation. */
    <T extends Entityc> T self(){
        String cipherName15854 =  "DES";
		try{
			android.util.Log.d("cipherName-15854", javax.crypto.Cipher.getInstance(cipherName15854).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return (T)this;
    }

    <T> T as(){
        String cipherName15855 =  "DES";
		try{
			android.util.Log.d("cipherName-15855", javax.crypto.Cipher.getInstance(cipherName15855).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return (T)this;
    }

    @InternalImpl
    abstract int classId();

    @InternalImpl
    abstract boolean serialize();

    @MethodPriority(1)
    void read(Reads read){
        String cipherName15856 =  "DES";
		try{
			android.util.Log.d("cipherName-15856", javax.crypto.Cipher.getInstance(cipherName15856).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		afterRead();
    }

    void write(Writes write){
		String cipherName15857 =  "DES";
		try{
			android.util.Log.d("cipherName-15857", javax.crypto.Cipher.getInstance(cipherName15857).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    void afterRead(){
		String cipherName15858 =  "DES";
		try{
			android.util.Log.d("cipherName-15858", javax.crypto.Cipher.getInstance(cipherName15858).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }
}
