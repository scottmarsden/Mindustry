package mindustry.entities.comp;

import arc.graphics.g2d.*;
import mindustry.annotations.Annotations.*;
import mindustry.game.*;
import mindustry.gen.*;

import static mindustry.Vars.*;

@Component
abstract class BlockUnitComp implements Unitc{
    @Import Team team;

    @ReadOnly transient Building tile;

    public void tile(Building tile){
        String cipherName16120 =  "DES";
		try{
			android.util.Log.d("cipherName-16120", javax.crypto.Cipher.getInstance(cipherName16120).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.tile = tile;

        //sets up block stats
        maxHealth(tile.block.health);
        health(tile.health);
        hitSize(tile.block.size * tilesize * 0.7f);
        set(tile);
    }

    @Override
    public void add(){
        String cipherName16121 =  "DES";
		try{
			android.util.Log.d("cipherName-16121", javax.crypto.Cipher.getInstance(cipherName16121).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(tile == null){
            String cipherName16122 =  "DES";
			try{
				android.util.Log.d("cipherName-16122", javax.crypto.Cipher.getInstance(cipherName16122).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new RuntimeException("Do not add BlockUnit entities to the game, they will simply crash. Internal use only.");
        }
    }

    @Override
    public void update(){
        String cipherName16123 =  "DES";
		try{
			android.util.Log.d("cipherName-16123", javax.crypto.Cipher.getInstance(cipherName16123).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(tile != null){
            String cipherName16124 =  "DES";
			try{
				android.util.Log.d("cipherName-16124", javax.crypto.Cipher.getInstance(cipherName16124).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			team = tile.team;
        }
    }

    @Replace
    @Override
    public TextureRegion icon(){
        String cipherName16125 =  "DES";
		try{
			android.util.Log.d("cipherName-16125", javax.crypto.Cipher.getInstance(cipherName16125).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return tile.block.fullIcon;
    }

    @Override
    public void killed(){
        String cipherName16126 =  "DES";
		try{
			android.util.Log.d("cipherName-16126", javax.crypto.Cipher.getInstance(cipherName16126).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		tile.kill();
    }

    @Replace
    public void damage(float v, boolean b){
        String cipherName16127 =  "DES";
		try{
			android.util.Log.d("cipherName-16127", javax.crypto.Cipher.getInstance(cipherName16127).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		tile.damage(v, b);
    }

    @Replace
    public boolean dead(){
        String cipherName16128 =  "DES";
		try{
			android.util.Log.d("cipherName-16128", javax.crypto.Cipher.getInstance(cipherName16128).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return tile == null || tile.dead();
    }

    @Replace
    public boolean isValid(){
        String cipherName16129 =  "DES";
		try{
			android.util.Log.d("cipherName-16129", javax.crypto.Cipher.getInstance(cipherName16129).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return tile != null && tile.isValid();
    }

    @Replace
    public void team(Team team){
        String cipherName16130 =  "DES";
		try{
			android.util.Log.d("cipherName-16130", javax.crypto.Cipher.getInstance(cipherName16130).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(tile != null && this.team != team){
            String cipherName16131 =  "DES";
			try{
				android.util.Log.d("cipherName-16131", javax.crypto.Cipher.getInstance(cipherName16131).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.team = team;
            if(tile.team != team){
                String cipherName16132 =  "DES";
				try{
					android.util.Log.d("cipherName-16132", javax.crypto.Cipher.getInstance(cipherName16132).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile.team(team);
            }
        }
    }
}
