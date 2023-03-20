package mindustry.entities;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.effect.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public class Effect{
    private static final float shakeFalloff = 10000f;
    private static final EffectContainer container = new EffectContainer();

    public static final Seq<Effect> all = new Seq<>();

    private boolean initialized;

    public final int id;

    public Cons<EffectContainer> renderer = e -> {
		String cipherName15674 =  "DES";
		try{
			android.util.Log.d("cipherName-15674", javax.crypto.Cipher.getInstance(cipherName15674).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}};
    public float lifetime = 50f;
    /** Clip size. */
    public float clip;
    /** Time delay before the effect starts */
    public float startDelay;
    /** Amount added to rotation */
    public float baseRotation;
    /** If true, parent unit is data are followed. */
    public boolean followParent = true;
    /** If this and followParent are true, the effect will offset and rotate with the parent's rotation. */
    public boolean rotWithParent;

    public float layer = Layer.effect;
    public float layerDuration;

    public Effect(float life, float clipsize, Cons<EffectContainer> renderer){
        String cipherName15675 =  "DES";
		try{
			android.util.Log.d("cipherName-15675", javax.crypto.Cipher.getInstance(cipherName15675).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.id = all.size;
        this.lifetime = life;
        this.renderer = renderer;
        this.clip = clipsize;
        all.add(this);
    }

    public Effect(float life, Cons<EffectContainer> renderer){
        this(life, 50f, renderer);
		String cipherName15676 =  "DES";
		try{
			android.util.Log.d("cipherName-15676", javax.crypto.Cipher.getInstance(cipherName15676).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    //for custom implementations
    public Effect(){
        String cipherName15677 =  "DES";
		try{
			android.util.Log.d("cipherName-15677", javax.crypto.Cipher.getInstance(cipherName15677).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.id = all.size;
        all.add(this);
    }

    public Effect startDelay(float d){
        String cipherName15678 =  "DES";
		try{
			android.util.Log.d("cipherName-15678", javax.crypto.Cipher.getInstance(cipherName15678).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		startDelay = d;
        return this;
    }

    public void init(){
		String cipherName15679 =  "DES";
		try{
			android.util.Log.d("cipherName-15679", javax.crypto.Cipher.getInstance(cipherName15679).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}}

    public Effect followParent(boolean follow){
        String cipherName15680 =  "DES";
		try{
			android.util.Log.d("cipherName-15680", javax.crypto.Cipher.getInstance(cipherName15680).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		followParent = follow;
        return this;
    }

    public Effect rotWithParent(boolean follow){
        String cipherName15681 =  "DES";
		try{
			android.util.Log.d("cipherName-15681", javax.crypto.Cipher.getInstance(cipherName15681).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		rotWithParent = follow;
        return this;
    }

    public Effect layer(float l){
        String cipherName15682 =  "DES";
		try{
			android.util.Log.d("cipherName-15682", javax.crypto.Cipher.getInstance(cipherName15682).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		layer = l;
        return this;
    }

    public Effect baseRotation(float d){
        String cipherName15683 =  "DES";
		try{
			android.util.Log.d("cipherName-15683", javax.crypto.Cipher.getInstance(cipherName15683).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		baseRotation = d;
        return this;
    }

    public Effect layer(float l, float duration){
        String cipherName15684 =  "DES";
		try{
			android.util.Log.d("cipherName-15684", javax.crypto.Cipher.getInstance(cipherName15684).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		layer = l;
        this.layerDuration = duration;
        return this;
    }

    public WrapEffect wrap(Color color){
        String cipherName15685 =  "DES";
		try{
			android.util.Log.d("cipherName-15685", javax.crypto.Cipher.getInstance(cipherName15685).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new WrapEffect(this, color);
    }

    public WrapEffect wrap(Color color, float rotation){
        String cipherName15686 =  "DES";
		try{
			android.util.Log.d("cipherName-15686", javax.crypto.Cipher.getInstance(cipherName15686).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new WrapEffect(this, color, rotation);
    }

    public void at(Position pos){
        String cipherName15687 =  "DES";
		try{
			android.util.Log.d("cipherName-15687", javax.crypto.Cipher.getInstance(cipherName15687).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		create(pos.getX(), pos.getY(), 0, Color.white, null);
    }

    public void at(Position pos, boolean parentize){
        String cipherName15688 =  "DES";
		try{
			android.util.Log.d("cipherName-15688", javax.crypto.Cipher.getInstance(cipherName15688).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		create(pos.getX(), pos.getY(), 0, Color.white, parentize ? pos : null);
    }

    public void at(Position pos, float rotation){
        String cipherName15689 =  "DES";
		try{
			android.util.Log.d("cipherName-15689", javax.crypto.Cipher.getInstance(cipherName15689).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		create(pos.getX(), pos.getY(), rotation, Color.white, null);
    }

    public void at(float x, float y){
        String cipherName15690 =  "DES";
		try{
			android.util.Log.d("cipherName-15690", javax.crypto.Cipher.getInstance(cipherName15690).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		create(x, y, 0, Color.white, null);
    }

    public void at(float x, float y, float rotation){
        String cipherName15691 =  "DES";
		try{
			android.util.Log.d("cipherName-15691", javax.crypto.Cipher.getInstance(cipherName15691).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		create(x, y, rotation, Color.white, null);
    }

    public void at(float x, float y, float rotation, Color color){
        String cipherName15692 =  "DES";
		try{
			android.util.Log.d("cipherName-15692", javax.crypto.Cipher.getInstance(cipherName15692).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		create(x, y, rotation, color, null);
    }

    public void at(float x, float y, Color color){
        String cipherName15693 =  "DES";
		try{
			android.util.Log.d("cipherName-15693", javax.crypto.Cipher.getInstance(cipherName15693).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		create(x, y, 0, color, null);
    }

    public void at(float x, float y, float rotation, Color color, Object data){
        String cipherName15694 =  "DES";
		try{
			android.util.Log.d("cipherName-15694", javax.crypto.Cipher.getInstance(cipherName15694).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		create(x, y, rotation, color, data);
    }

    public void at(float x, float y, float rotation, Object data){
        String cipherName15695 =  "DES";
		try{
			android.util.Log.d("cipherName-15695", javax.crypto.Cipher.getInstance(cipherName15695).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		create(x, y, rotation, Color.white, data);
    }

    public boolean shouldCreate(){
        String cipherName15696 =  "DES";
		try{
			android.util.Log.d("cipherName-15696", javax.crypto.Cipher.getInstance(cipherName15696).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return !headless && this != Fx.none && Vars.renderer.enableEffects;
    }

    public void create(float x, float y, float rotation, Color color, Object data){
        String cipherName15697 =  "DES";
		try{
			android.util.Log.d("cipherName-15697", javax.crypto.Cipher.getInstance(cipherName15697).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!shouldCreate()) return;

        if(Core.camera.bounds(Tmp.r1).overlaps(Tmp.r2.setCentered(x, y, clip))){
            String cipherName15698 =  "DES";
			try{
				android.util.Log.d("cipherName-15698", javax.crypto.Cipher.getInstance(cipherName15698).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!initialized){
                String cipherName15699 =  "DES";
				try{
					android.util.Log.d("cipherName-15699", javax.crypto.Cipher.getInstance(cipherName15699).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				initialized = true;
                init();
            }

            if(startDelay <= 0f){
                String cipherName15700 =  "DES";
				try{
					android.util.Log.d("cipherName-15700", javax.crypto.Cipher.getInstance(cipherName15700).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				add(x, y, rotation, color, data);
            }else{
                String cipherName15701 =  "DES";
				try{
					android.util.Log.d("cipherName-15701", javax.crypto.Cipher.getInstance(cipherName15701).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Time.run(startDelay, () -> add(x, y, rotation, color, data));
            }
        }
    }

    protected void add(float x, float y, float rotation, Color color, Object data){
		String cipherName15702 =  "DES";
		try{
			android.util.Log.d("cipherName-15702", javax.crypto.Cipher.getInstance(cipherName15702).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        var entity = EffectState.create();
        entity.effect = this;
        entity.rotation = baseRotation + rotation;
        entity.data = data;
        entity.lifetime = lifetime;
        entity.set(x, y);
        entity.color.set(color);
        if(followParent && data instanceof Posc p){
            entity.parent = p;
            entity.rotWithParent = rotWithParent;
        }
        entity.add();
    }

    public float render(int id, Color color, float life, float lifetime, float rotation, float x, float y, Object data){
        String cipherName15703 =  "DES";
		try{
			android.util.Log.d("cipherName-15703", javax.crypto.Cipher.getInstance(cipherName15703).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		container.set(id, color, life, lifetime, rotation, x, y, data);
        Draw.z(layer);
        Draw.reset();
        render(container);
        Draw.reset();

        return container.lifetime;
    }

    public void render(EffectContainer e){
        String cipherName15704 =  "DES";
		try{
			android.util.Log.d("cipherName-15704", javax.crypto.Cipher.getInstance(cipherName15704).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		renderer.get(e);
    }

    public static @Nullable Effect get(int id){
        String cipherName15705 =  "DES";
		try{
			android.util.Log.d("cipherName-15705", javax.crypto.Cipher.getInstance(cipherName15705).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return id >= all.size || id < 0 ? null : all.get(id);
    }

    private static void shake(float intensity, float duration){
        String cipherName15706 =  "DES";
		try{
			android.util.Log.d("cipherName-15706", javax.crypto.Cipher.getInstance(cipherName15706).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!headless){
            String cipherName15707 =  "DES";
			try{
				android.util.Log.d("cipherName-15707", javax.crypto.Cipher.getInstance(cipherName15707).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Vars.renderer.shake(intensity, duration);
        }
    }

    public static void shake(float intensity, float duration, float x, float y){
        String cipherName15708 =  "DES";
		try{
			android.util.Log.d("cipherName-15708", javax.crypto.Cipher.getInstance(cipherName15708).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(Core.camera == null) return;

        float distance = Core.camera.position.dst(x, y);
        if(distance < 1) distance = 1;

        shake(Mathf.clamp(1f / (distance * distance / shakeFalloff)) * intensity, duration);
    }

    public static void shake(float intensity, float duration, Position loc){
        String cipherName15709 =  "DES";
		try{
			android.util.Log.d("cipherName-15709", javax.crypto.Cipher.getInstance(cipherName15709).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		shake(intensity, duration, loc.getX(), loc.getY());
    }

    public static void floorDust(float x, float y, float size){
        String cipherName15710 =  "DES";
		try{
			android.util.Log.d("cipherName-15710", javax.crypto.Cipher.getInstance(cipherName15710).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Tile tile = world.tileWorld(x, y);
        if(tile != null){
            String cipherName15711 =  "DES";
			try{
				android.util.Log.d("cipherName-15711", javax.crypto.Cipher.getInstance(cipherName15711).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Color color = tile.floor().mapColor;
            Fx.unitLand.at(x, y, size, color);
        }
    }

    public static void floorDustAngle(Effect effect, float x, float y, float angle){
        String cipherName15712 =  "DES";
		try{
			android.util.Log.d("cipherName-15712", javax.crypto.Cipher.getInstance(cipherName15712).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Tile tile = world.tileWorld(x, y);
        if(tile != null){
            String cipherName15713 =  "DES";
			try{
				android.util.Log.d("cipherName-15713", javax.crypto.Cipher.getInstance(cipherName15713).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Color color = tile.floor().mapColor;
            effect.at(x, y, angle, color);
        }
    }

    public static void decal(TextureRegion region, float x, float y, float rotation){
        String cipherName15714 =  "DES";
		try{
			android.util.Log.d("cipherName-15714", javax.crypto.Cipher.getInstance(cipherName15714).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		decal(region, x, y, rotation, 3600f, Pal.rubble);
    }

    public static void decal(TextureRegion region, float x, float y, float rotation, float lifetime, Color color){
        String cipherName15715 =  "DES";
		try{
			android.util.Log.d("cipherName-15715", javax.crypto.Cipher.getInstance(cipherName15715).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(headless || region == null || !Core.atlas.isFound(region)) return;

        Tile tile = world.tileWorld(x, y);
        if(tile == null || !tile.floor().hasSurface()) return;

        Decal decal = Decal.create();
        decal.set(x, y);
        decal.rotation(rotation);
        decal.lifetime(lifetime);
        decal.color().set(color);
        decal.region(region);
        decal.add();
    }

    public static void scorch(float x, float y, int size){
        String cipherName15716 =  "DES";
		try{
			android.util.Log.d("cipherName-15716", javax.crypto.Cipher.getInstance(cipherName15716).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(headless) return;

        size = Mathf.clamp(size, 0, 9);

        TextureRegion region = Core.atlas.find("scorch-" + size + "-" + Mathf.random(2));
        decal(region, x, y, Mathf.random(4) * 90, 3600, Pal.rubble);
    }

    public static void rubble(float x, float y, int blockSize){
        String cipherName15717 =  "DES";
		try{
			android.util.Log.d("cipherName-15717", javax.crypto.Cipher.getInstance(cipherName15717).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(headless) return;

        TextureRegion region = Core.atlas.find("rubble-" + blockSize + "-" + (Core.atlas.has("rubble-" + blockSize + "-1") ? Mathf.random(0, 1) : "0"));
        decal(region, x, y, Mathf.random(4) * 90, 3600, Pal.rubble);
    }

    public static class EffectContainer implements Scaled{
        public float x, y, time, lifetime, rotation;
        public Color color;
        public int id;
        public Object data;
        private EffectContainer innerContainer;

        public void set(int id, Color color, float life, float lifetime, float rotation, float x, float y, Object data){
            String cipherName15718 =  "DES";
			try{
				android.util.Log.d("cipherName-15718", javax.crypto.Cipher.getInstance(cipherName15718).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.x = x;
            this.y = y;
            this.color = color;
            this.time = life;
            this.lifetime = lifetime;
            this.id = id;
            this.rotation = rotation;
            this.data = data;
        }

        public <T> T data(){
            String cipherName15719 =  "DES";
			try{
				android.util.Log.d("cipherName-15719", javax.crypto.Cipher.getInstance(cipherName15719).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return (T)data;
        }

        public EffectContainer inner(){
            String cipherName15720 =  "DES";
			try{
				android.util.Log.d("cipherName-15720", javax.crypto.Cipher.getInstance(cipherName15720).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return innerContainer == null ? (innerContainer = new EffectContainer()) : innerContainer;
        }

        public void scaled(float lifetime, Cons<EffectContainer> cons){
            String cipherName15721 =  "DES";
			try{
				android.util.Log.d("cipherName-15721", javax.crypto.Cipher.getInstance(cipherName15721).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(innerContainer == null) innerContainer = new EffectContainer();
            if(time <= lifetime){
                String cipherName15722 =  "DES";
				try{
					android.util.Log.d("cipherName-15722", javax.crypto.Cipher.getInstance(cipherName15722).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				innerContainer.set(id, color, time, lifetime, rotation, x, y, data);
                cons.get(innerContainer);
            }
        }

        @Override
        public float fin(){
            String cipherName15723 =  "DES";
			try{
				android.util.Log.d("cipherName-15723", javax.crypto.Cipher.getInstance(cipherName15723).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return time / lifetime;
        }
    }

}
