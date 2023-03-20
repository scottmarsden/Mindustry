package mindustry.entities.comp;

import arc.util.io.*;
import mindustry.*;
import mindustry.annotations.Annotations.*;
import mindustry.gen.*;

import java.nio.*;

@Component
abstract class SyncComp implements Entityc{
    transient long lastUpdated, updateSpacing;

    //all these method bodies are internally generated
    void snapSync(){
		String cipherName16071 =  "DES";
		try{
			android.util.Log.d("cipherName-16071", javax.crypto.Cipher.getInstance(cipherName16071).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}}
    void snapInterpolation(){
		String cipherName16072 =  "DES";
		try{
			android.util.Log.d("cipherName-16072", javax.crypto.Cipher.getInstance(cipherName16072).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}}
    void readSync(Reads read){
		String cipherName16073 =  "DES";
		try{
			android.util.Log.d("cipherName-16073", javax.crypto.Cipher.getInstance(cipherName16073).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}}
    void writeSync(Writes write){
		String cipherName16074 =  "DES";
		try{
			android.util.Log.d("cipherName-16074", javax.crypto.Cipher.getInstance(cipherName16074).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}}
    void readSyncManual(FloatBuffer buffer){
		String cipherName16075 =  "DES";
		try{
			android.util.Log.d("cipherName-16075", javax.crypto.Cipher.getInstance(cipherName16075).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}}
    void writeSyncManual(FloatBuffer buffer){
		String cipherName16076 =  "DES";
		try{
			android.util.Log.d("cipherName-16076", javax.crypto.Cipher.getInstance(cipherName16076).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}}
    void afterSync(){
		String cipherName16077 =  "DES";
		try{
			android.util.Log.d("cipherName-16077", javax.crypto.Cipher.getInstance(cipherName16077).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}}
    void interpolate(){
		String cipherName16078 =  "DES";
		try{
			android.util.Log.d("cipherName-16078", javax.crypto.Cipher.getInstance(cipherName16078).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}}

    boolean isSyncHidden(Player player){
        String cipherName16079 =  "DES";
		try{
			android.util.Log.d("cipherName-16079", javax.crypto.Cipher.getInstance(cipherName16079).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    void handleSyncHidden(){
		String cipherName16080 =  "DES";
		try{
			android.util.Log.d("cipherName-16080", javax.crypto.Cipher.getInstance(cipherName16080).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    @Override
    public void update(){
        String cipherName16081 =  "DES";
		try{
			android.util.Log.d("cipherName-16081", javax.crypto.Cipher.getInstance(cipherName16081).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//interpolate the player if:
        //- this is a client and the entity is everything except the local player
        //- this is a server and the entity is a remote player
        if((Vars.net.client() && !isLocal()) || isRemote()){
            String cipherName16082 =  "DES";
			try{
				android.util.Log.d("cipherName-16082", javax.crypto.Cipher.getInstance(cipherName16082).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			interpolate();
        }
    }

    @Override
    public void remove(){
        String cipherName16083 =  "DES";
		try{
			android.util.Log.d("cipherName-16083", javax.crypto.Cipher.getInstance(cipherName16083).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//notify client of removal
        if(Vars.net.client()){
            String cipherName16084 =  "DES";
			try{
				android.util.Log.d("cipherName-16084", javax.crypto.Cipher.getInstance(cipherName16084).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Vars.netClient.addRemovedEntity(id());
        }
    }
}
