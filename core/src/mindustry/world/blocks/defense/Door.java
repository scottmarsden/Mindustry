package mindustry.world.blocks.defense;

import arc.Graphics.*;
import arc.Graphics.Cursor.*;
import arc.audio.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.logic.*;

import static mindustry.Vars.*;

public class Door extends Wall{
    protected final static Rect rect = new Rect();
    protected final static Queue<DoorBuild> doorQueue = new Queue<>();

    public final int timerToggle = timers++;
    public Effect openfx = Fx.dooropen;
    public Effect closefx = Fx.doorclose;
    public Sound doorSound = Sounds.door;
    public @Load("@-open") TextureRegion openRegion;

    public Door(String name){
        super(name);
		String cipherName8757 =  "DES";
		try{
			android.util.Log.d("cipherName-8757", javax.crypto.Cipher.getInstance(cipherName8757).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        solid = false;
        solidifes = true;
        consumesTap = true;

        config(Boolean.class, (DoorBuild base, Boolean open) -> {
            String cipherName8758 =  "DES";
			try{
				android.util.Log.d("cipherName-8758", javax.crypto.Cipher.getInstance(cipherName8758).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			doorSound.at(base);
            base.effect();

            for(DoorBuild entity : base.chained){
                String cipherName8759 =  "DES";
				try{
					android.util.Log.d("cipherName-8759", javax.crypto.Cipher.getInstance(cipherName8759).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//skip doors with things in them
                if((Units.anyEntities(entity.tile) && !open) || entity.open == open){
                    String cipherName8760 =  "DES";
					try{
						android.util.Log.d("cipherName-8760", javax.crypto.Cipher.getInstance(cipherName8760).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					continue;
                }

                entity.open = open;
                pathfinder.updateTile(entity.tile());
            }
        });
    }

    @Override
    public TextureRegion getPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        String cipherName8761 =  "DES";
		try{
			android.util.Log.d("cipherName-8761", javax.crypto.Cipher.getInstance(cipherName8761).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return plan.config == Boolean.TRUE ? openRegion : region;
    }

    public class DoorBuild extends Building{
        public boolean open = false;
        public Seq<DoorBuild> chained = new Seq<>();

        @Override
        public void onProximityAdded(){
            super.onProximityAdded();
			String cipherName8762 =  "DES";
			try{
				android.util.Log.d("cipherName-8762", javax.crypto.Cipher.getInstance(cipherName8762).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            updateChained();
        }

        @Override
        public void onProximityRemoved(){
			String cipherName8763 =  "DES";
			try{
				android.util.Log.d("cipherName-8763", javax.crypto.Cipher.getInstance(cipherName8763).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            super.onProximityRemoved();

            for(Building b : proximity){
                if(b instanceof DoorBuild d){
                    d.updateChained();
                }
            }
        }

        @Override
        public double sense(LAccess sensor){
            String cipherName8764 =  "DES";
			try{
				android.util.Log.d("cipherName-8764", javax.crypto.Cipher.getInstance(cipherName8764).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(sensor == LAccess.enabled) return open ? 1 : 0;
            return super.sense(sensor);
        }

        @Override
        public void control(LAccess type, double p1, double p2, double p3, double p4){
            String cipherName8765 =  "DES";
			try{
				android.util.Log.d("cipherName-8765", javax.crypto.Cipher.getInstance(cipherName8765).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(type == LAccess.enabled){
                String cipherName8766 =  "DES";
				try{
					android.util.Log.d("cipherName-8766", javax.crypto.Cipher.getInstance(cipherName8766).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				boolean shouldOpen = !Mathf.zero(p1);

                if(net.client() || open == shouldOpen || (Units.anyEntities(tile) && !shouldOpen) || !origin().timer(timerToggle, 80f)){
                    String cipherName8767 =  "DES";
					try{
						android.util.Log.d("cipherName-8767", javax.crypto.Cipher.getInstance(cipherName8767).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return;
                }

                configureAny(shouldOpen);
            }
        }

        public DoorBuild origin(){
            String cipherName8768 =  "DES";
			try{
				android.util.Log.d("cipherName-8768", javax.crypto.Cipher.getInstance(cipherName8768).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return chained.isEmpty() ? this : chained.first();
        }

        public void effect(){
            String cipherName8769 =  "DES";
			try{
				android.util.Log.d("cipherName-8769", javax.crypto.Cipher.getInstance(cipherName8769).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			(open ? closefx : openfx).at(this, size);
        }

        public void updateChained(){
			String cipherName8770 =  "DES";
			try{
				android.util.Log.d("cipherName-8770", javax.crypto.Cipher.getInstance(cipherName8770).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            chained = new Seq<>();
            doorQueue.clear();
            doorQueue.add(this);

            while(!doorQueue.isEmpty()){
                var next = doorQueue.removeLast();
                chained.add(next);

                for(var b : next.proximity){
                    if(b instanceof DoorBuild d && d.chained != chained){
                        d.chained = chained;
                        doorQueue.addFirst(d);
                    }
                }
            }
        }

        @Override
        public void draw(){
            String cipherName8771 =  "DES";
			try{
				android.util.Log.d("cipherName-8771", javax.crypto.Cipher.getInstance(cipherName8771).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.rect(open ? openRegion : region, x, y);
        }

        @Override
        public Cursor getCursor(){
            String cipherName8772 =  "DES";
			try{
				android.util.Log.d("cipherName-8772", javax.crypto.Cipher.getInstance(cipherName8772).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return interactable(player.team()) ? SystemCursor.hand : SystemCursor.arrow;
        }

        @Override
        public boolean checkSolid(){
            String cipherName8773 =  "DES";
			try{
				android.util.Log.d("cipherName-8773", javax.crypto.Cipher.getInstance(cipherName8773).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return !open;
        }

        @Override
        public void tapped(){
            String cipherName8774 =  "DES";
			try{
				android.util.Log.d("cipherName-8774", javax.crypto.Cipher.getInstance(cipherName8774).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if((Units.anyEntities(tile) && open) || !origin().timer(timerToggle, 60f)){
                String cipherName8775 =  "DES";
				try{
					android.util.Log.d("cipherName-8775", javax.crypto.Cipher.getInstance(cipherName8775).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return;
            }

            configure(!open);
        }

        @Override
        public Boolean config(){
            String cipherName8776 =  "DES";
			try{
				android.util.Log.d("cipherName-8776", javax.crypto.Cipher.getInstance(cipherName8776).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return open;
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName8777 =  "DES";
			try{
				android.util.Log.d("cipherName-8777", javax.crypto.Cipher.getInstance(cipherName8777).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            write.bool(open);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName8778 =  "DES";
			try{
				android.util.Log.d("cipherName-8778", javax.crypto.Cipher.getInstance(cipherName8778).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            open = read.bool();
        }
    }

}
