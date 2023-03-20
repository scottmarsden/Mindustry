package mindustry.entities.comp;

import arc.func.*;
import arc.math.*;
import arc.math.geom.*;
import arc.math.geom.QuadTree.*;
import mindustry.annotations.Annotations.*;
import mindustry.entities.*;
import mindustry.gen.*;

@Component
abstract class HitboxComp implements Posc, Sized, QuadTreeObject{
    @Import float x, y;

    transient float lastX, lastY, deltaX, deltaY, hitSize;

    @Override
    public void update(){
		String cipherName15898 =  "DES";
		try{
			android.util.Log.d("cipherName-15898", javax.crypto.Cipher.getInstance(cipherName15898).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    @Override
    public void add(){
        String cipherName15899 =  "DES";
		try{
			android.util.Log.d("cipherName-15899", javax.crypto.Cipher.getInstance(cipherName15899).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		updateLastPosition();
    }

    @Override
    public void afterRead(){
        String cipherName15900 =  "DES";
		try{
			android.util.Log.d("cipherName-15900", javax.crypto.Cipher.getInstance(cipherName15900).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		updateLastPosition();
    }

    @Override
    public float hitSize(){
        String cipherName15901 =  "DES";
		try{
			android.util.Log.d("cipherName-15901", javax.crypto.Cipher.getInstance(cipherName15901).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return hitSize;
    }

    void getCollisions(Cons<QuadTree> consumer){
		String cipherName15902 =  "DES";
		try{
			android.util.Log.d("cipherName-15902", javax.crypto.Cipher.getInstance(cipherName15902).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    void updateLastPosition(){
        String cipherName15903 =  "DES";
		try{
			android.util.Log.d("cipherName-15903", javax.crypto.Cipher.getInstance(cipherName15903).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		deltaX = x - lastX;
        deltaY = y - lastY;
        lastX = x;
        lastY = y;
    }

    void collision(Hitboxc other, float x, float y){
		String cipherName15904 =  "DES";
		try{
			android.util.Log.d("cipherName-15904", javax.crypto.Cipher.getInstance(cipherName15904).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    float deltaLen(){
        String cipherName15905 =  "DES";
		try{
			android.util.Log.d("cipherName-15905", javax.crypto.Cipher.getInstance(cipherName15905).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Mathf.len(deltaX, deltaY);
    }

    float deltaAngle(){
        String cipherName15906 =  "DES";
		try{
			android.util.Log.d("cipherName-15906", javax.crypto.Cipher.getInstance(cipherName15906).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Mathf.angle(deltaX, deltaY);
    }

    boolean collides(Hitboxc other){
        String cipherName15907 =  "DES";
		try{
			android.util.Log.d("cipherName-15907", javax.crypto.Cipher.getInstance(cipherName15907).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return true;
    }

    @Override
    public void hitbox(Rect rect){
        String cipherName15908 =  "DES";
		try{
			android.util.Log.d("cipherName-15908", javax.crypto.Cipher.getInstance(cipherName15908).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		rect.setCentered(x, y, hitSize, hitSize);
    }

    public void hitboxTile(Rect rect){
        String cipherName15909 =  "DES";
		try{
			android.util.Log.d("cipherName-15909", javax.crypto.Cipher.getInstance(cipherName15909).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//tile hitboxes are never bigger than a tile, otherwise units get stuck
        float size = Math.min(hitSize * 0.66f, 7.9f);
        //TODO: better / more accurate version is
        //float size = hitSize * 0.85f;
        //- for tanks?
        rect.setCentered(x, y, size, size);
    }
}
