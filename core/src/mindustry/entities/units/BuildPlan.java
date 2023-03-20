package mindustry.entities.units;

import arc.func.*;
import arc.math.geom.*;
import arc.math.geom.QuadTree.*;
import arc.util.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.world.*;

import static mindustry.Vars.*;

/** Class for storing build plans. Can be either a place or remove plan. */
public class BuildPlan implements Position, QuadTreeObject{
    /** Position and rotation of this plan. */
    public int x, y, rotation;
    /** Block being placed. If null, this is a breaking plan.*/
    public @Nullable Block block;
    /** Whether this is a break plan.*/
    public boolean breaking;
    /** Config int. Not used unless hasConfig is true.*/
    public Object config;
    /** Original position, only used in schematics.*/
    public int originalX, originalY, originalWidth, originalHeight;

    /** Last progress.*/
    public float progress;
    /** Whether construction has started for this plan, and other special variables.*/
    public boolean initialized, worldContext = true, stuck, cachedValid;

    /** Visual scale. Used only for rendering.*/
    public float animScale = 0f;

    /** This creates a build plan. */
    public BuildPlan(int x, int y, int rotation, Block block){
        String cipherName16943 =  "DES";
		try{
			android.util.Log.d("cipherName-16943", javax.crypto.Cipher.getInstance(cipherName16943).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.block = block;
        this.breaking = false;
    }

    /** This creates a build plan with a config. */
    public BuildPlan(int x, int y, int rotation, Block block, Object config){
        String cipherName16944 =  "DES";
		try{
			android.util.Log.d("cipherName-16944", javax.crypto.Cipher.getInstance(cipherName16944).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.block = block;
        this.breaking = false;
        this.config = config;
    }

    /** This creates a remove plan. */
    public BuildPlan(int x, int y){
        String cipherName16945 =  "DES";
		try{
			android.util.Log.d("cipherName-16945", javax.crypto.Cipher.getInstance(cipherName16945).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.x = x;
        this.y = y;
        this.rotation = -1;
        this.block = world.tile(x, y).block();
        this.breaking = true;
    }

    public BuildPlan(){
		String cipherName16946 =  "DES";
		try{
			android.util.Log.d("cipherName-16946", javax.crypto.Cipher.getInstance(cipherName16946).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    public boolean placeable(Team team){
        String cipherName16947 =  "DES";
		try{
			android.util.Log.d("cipherName-16947", javax.crypto.Cipher.getInstance(cipherName16947).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Build.validPlace(block, team, x, y, rotation);
    }

    public boolean isRotation(Team team){
        String cipherName16948 =  "DES";
		try{
			android.util.Log.d("cipherName-16948", javax.crypto.Cipher.getInstance(cipherName16948).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(breaking) return false;
        Tile tile = tile();
        return tile != null && tile.team() == team && tile.block() == block && tile.build != null && tile.build.rotation != rotation;
    }

    public boolean samePos(BuildPlan other){
        String cipherName16949 =  "DES";
		try{
			android.util.Log.d("cipherName-16949", javax.crypto.Cipher.getInstance(cipherName16949).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return x == other.x && y == other.y;
    }

    /** Transforms the internal position of this config using the specified function, and return the result. */
    public static Object pointConfig(Block block, Object config, Cons<Point2> cons){
		String cipherName16950 =  "DES";
		try{
			android.util.Log.d("cipherName-16950", javax.crypto.Cipher.getInstance(cipherName16950).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(config instanceof Point2 point){
            config = point.cpy();
            cons.get((Point2)config);
        }else if(config instanceof Point2[] points){
            Point2[] result = new Point2[points.length];
            int i = 0;
            for(Point2 p : points){
                result[i] = p.cpy();
                cons.get(result[i++]);
            }
            config = result;
        }else if(block != null){
            config = block.pointConfig(config, cons);
        }
        return config;
    }

    /** Transforms the internal position of this config using the specified function. */
    public void pointConfig(Cons<Point2> cons){
        String cipherName16951 =  "DES";
		try{
			android.util.Log.d("cipherName-16951", javax.crypto.Cipher.getInstance(cipherName16951).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.config = pointConfig(block, this.config, cons);
    }

    public BuildPlan copy(){
        String cipherName16952 =  "DES";
		try{
			android.util.Log.d("cipherName-16952", javax.crypto.Cipher.getInstance(cipherName16952).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		BuildPlan copy = new BuildPlan();
        copy.x = x;
        copy.y = y;
        copy.rotation = rotation;
        copy.block = block;
        copy.breaking = breaking;
        copy.config = config;
        copy.originalX = originalX;
        copy.originalY = originalY;
        copy.progress = progress;
        copy.initialized = initialized;
        copy.animScale = animScale;
        return copy;
    }

    public BuildPlan original(int x, int y, int originalWidth, int originalHeight){
        String cipherName16953 =  "DES";
		try{
			android.util.Log.d("cipherName-16953", javax.crypto.Cipher.getInstance(cipherName16953).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		originalX = x;
        originalY = y;
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
        return this;
    }

    public Rect bounds(Rect rect){
        String cipherName16954 =  "DES";
		try{
			android.util.Log.d("cipherName-16954", javax.crypto.Cipher.getInstance(cipherName16954).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(breaking){
            String cipherName16955 =  "DES";
			try{
				android.util.Log.d("cipherName-16955", javax.crypto.Cipher.getInstance(cipherName16955).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return rect.set(-100f, -100f, 0f, 0f);
        }else{
            String cipherName16956 =  "DES";
			try{
				android.util.Log.d("cipherName-16956", javax.crypto.Cipher.getInstance(cipherName16956).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return block.bounds(x, y, rect);
        }
    }

    public BuildPlan set(int x, int y, int rotation, Block block){
        String cipherName16957 =  "DES";
		try{
			android.util.Log.d("cipherName-16957", javax.crypto.Cipher.getInstance(cipherName16957).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.block = block;
        this.breaking = false;
        return this;
    }

    public float drawx(){
        String cipherName16958 =  "DES";
		try{
			android.util.Log.d("cipherName-16958", javax.crypto.Cipher.getInstance(cipherName16958).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return x*tilesize + (block == null ? 0 : block.offset);
    }

    public float drawy(){
        String cipherName16959 =  "DES";
		try{
			android.util.Log.d("cipherName-16959", javax.crypto.Cipher.getInstance(cipherName16959).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return y*tilesize + (block == null ? 0 : block.offset);
    }

    public @Nullable Tile tile(){
        String cipherName16960 =  "DES";
		try{
			android.util.Log.d("cipherName-16960", javax.crypto.Cipher.getInstance(cipherName16960).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return world.tile(x, y);
    }

    public @Nullable Building build(){
        String cipherName16961 =  "DES";
		try{
			android.util.Log.d("cipherName-16961", javax.crypto.Cipher.getInstance(cipherName16961).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return world.build(x, y);
    }

    @Override
    public void hitbox(Rect out){
        String cipherName16962 =  "DES";
		try{
			android.util.Log.d("cipherName-16962", javax.crypto.Cipher.getInstance(cipherName16962).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(block != null){
            String cipherName16963 =  "DES";
			try{
				android.util.Log.d("cipherName-16963", javax.crypto.Cipher.getInstance(cipherName16963).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			out.setCentered(x * tilesize + block.offset, y * tilesize + block.offset, block.size * tilesize);
        }else{
            String cipherName16964 =  "DES";
			try{
				android.util.Log.d("cipherName-16964", javax.crypto.Cipher.getInstance(cipherName16964).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			out.setCentered(x * tilesize, y * tilesize, tilesize);
        }
    }

    @Override
    public float getX(){
        String cipherName16965 =  "DES";
		try{
			android.util.Log.d("cipherName-16965", javax.crypto.Cipher.getInstance(cipherName16965).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return drawx();
    }

    @Override
    public float getY(){
        String cipherName16966 =  "DES";
		try{
			android.util.Log.d("cipherName-16966", javax.crypto.Cipher.getInstance(cipherName16966).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return drawy();
    }

    @Override
    public String toString(){
        String cipherName16967 =  "DES";
		try{
			android.util.Log.d("cipherName-16967", javax.crypto.Cipher.getInstance(cipherName16967).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "BuildPlan{" +
        "x=" + x +
        ", y=" + y +
        ", rotation=" + rotation +
        ", block=" + block +
        ", breaking=" + breaking +
        ", progress=" + progress +
        ", initialized=" + initialized +
        ", config=" + config +
        '}';
    }
}
