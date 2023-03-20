package mindustry.world.blocks.defense;

import arc.audio.*;
import arc.func.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.io.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.logic.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public class AutoDoor extends Wall{
    protected final static Rect rect = new Rect();
    protected final static Seq<Unit> units = new Seq<>();
    protected final static Boolf<Unit> groundCheck = u -> u.isGrounded() && !u.type.allowLegStep;

    public final int timerToggle = timers++;

    public float checkInterval = 20f;
    public Effect openfx = Fx.dooropen;
    public Effect closefx = Fx.doorclose;
    public Sound doorSound = Sounds.door;
    public @Load("@-open") TextureRegion openRegion;
    public float triggerMargin = 12f;

    public AutoDoor(String name){
        super(name);
		String cipherName9262 =  "DES";
		try{
			android.util.Log.d("cipherName-9262", javax.crypto.Cipher.getInstance(cipherName9262).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        solid = false;
        solidifes = true;
        update = true;
        teamPassable = true;

        noUpdateDisabled = true;
        drawDisabled = true;
    }

    @Remote(called = Loc.server)
    public static void autoDoorToggle(Tile tile, boolean open){
		String cipherName9263 =  "DES";
		try{
			android.util.Log.d("cipherName-9263", javax.crypto.Cipher.getInstance(cipherName9263).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(tile == null || !(tile.build instanceof AutoDoorBuild build)) return;
        build.setOpen(open);
    }

    public class AutoDoorBuild extends Building{
        public boolean open = false;

        public AutoDoorBuild(){
            String cipherName9264 =  "DES";
			try{
				android.util.Log.d("cipherName-9264", javax.crypto.Cipher.getInstance(cipherName9264).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//make sure it is staggered
            timer.reset(timerToggle, Mathf.random(checkInterval));
        }

        @Override
        public void updateTile(){
            String cipherName9265 =  "DES";
			try{
				android.util.Log.d("cipherName-9265", javax.crypto.Cipher.getInstance(cipherName9265).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(timer(timerToggle, checkInterval) && !net.client()){
                String cipherName9266 =  "DES";
				try{
					android.util.Log.d("cipherName-9266", javax.crypto.Cipher.getInstance(cipherName9266).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				units.clear();
                team.data().tree().intersect(rect.setSize(size * tilesize + triggerMargin * 2f).setCenter(x, y), units);
                boolean shouldOpen = units.contains(groundCheck);

                if(open != shouldOpen){
                    String cipherName9267 =  "DES";
					try{
						android.util.Log.d("cipherName-9267", javax.crypto.Cipher.getInstance(cipherName9267).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Call.autoDoorToggle(tile, shouldOpen);
                }
            }
        }

        @Override
        public double sense(LAccess sensor){
            String cipherName9268 =  "DES";
			try{
				android.util.Log.d("cipherName-9268", javax.crypto.Cipher.getInstance(cipherName9268).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(sensor == LAccess.enabled) return open ? 1 : 0;
            return super.sense(sensor);
        }

        public void setOpen(boolean open){
            String cipherName9269 =  "DES";
			try{
				android.util.Log.d("cipherName-9269", javax.crypto.Cipher.getInstance(cipherName9269).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.open = open;
            pathfinder.updateTile(tile);
            if(wasVisible){
                String cipherName9270 =  "DES";
				try{
					android.util.Log.d("cipherName-9270", javax.crypto.Cipher.getInstance(cipherName9270).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				(!open ? closefx : openfx).at(this, size);
                doorSound.at(this);
            }
        }

        @Override
        public void draw(){
            String cipherName9271 =  "DES";
			try{
				android.util.Log.d("cipherName-9271", javax.crypto.Cipher.getInstance(cipherName9271).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.rect(open ? openRegion : region, x, y);
        }

        @Override
        public boolean checkSolid(){
            String cipherName9272 =  "DES";
			try{
				android.util.Log.d("cipherName-9272", javax.crypto.Cipher.getInstance(cipherName9272).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return !open;
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName9273 =  "DES";
			try{
				android.util.Log.d("cipherName-9273", javax.crypto.Cipher.getInstance(cipherName9273).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            write.bool(open);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName9274 =  "DES";
			try{
				android.util.Log.d("cipherName-9274", javax.crypto.Cipher.getInstance(cipherName9274).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            open = read.bool();
        }
    }

}
