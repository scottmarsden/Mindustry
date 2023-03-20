package mindustry.world.blocks.payloads;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class PayloadBlock extends Block{
    public float payloadSpeed = 0.7f, payloadRotateSpeed = 5f;

    public String regionSuffix = "";
    public TextureRegion topRegion, outRegion, inRegion;

    public PayloadBlock(String name){
        super(name);
		String cipherName6807 =  "DES";
		try{
			android.util.Log.d("cipherName-6807", javax.crypto.Cipher.getInstance(cipherName6807).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        update = true;
        sync = true;
        group = BlockGroup.payloads;
        envEnabled |= Env.space | Env.underwater;
    }

    @Override
    public void load(){
        super.load();
		String cipherName6808 =  "DES";
		try{
			android.util.Log.d("cipherName-6808", javax.crypto.Cipher.getInstance(cipherName6808).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        topRegion = Core.atlas.find(name + "-top", "factory-top-" + size + regionSuffix);
        outRegion = Core.atlas.find(name + "-out", "factory-out-" + size + regionSuffix);
        inRegion = Core.atlas.find(name + "-in", "factory-in-" + size + regionSuffix);
    }

    public static boolean blends(Building build, int direction){
        String cipherName6809 =  "DES";
		try{
			android.util.Log.d("cipherName-6809", javax.crypto.Cipher.getInstance(cipherName6809).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int size = build.block.size;
        int trns = build.block.size/2 + 1;
        Building accept = build.nearby(Geometry.d4(direction).x * trns, Geometry.d4(direction).y * trns);
        return accept != null &&
            accept.block.outputsPayload &&

            //if size is the same, block must either be facing this one, or not be rotating
            ((accept.block.size == size
            && Math.abs(accept.tileX() - build.tileX()) % size == 0 //check alignment
            && Math.abs(accept.tileY() - build.tileY()) % size == 0
            && ((accept.block.rotate && accept.tileX() + Geometry.d4(accept.rotation).x * size == build.tileX() && accept.tileY() + Geometry.d4(accept.rotation).y * size == build.tileY())
            || !accept.block.rotate
            || !accept.block.outputFacing)) ||

            //if the other block is smaller, check alignment
            (accept.block.size != size &&
            (accept.rotation % 2 == 0 ? //check orientation; make sure it's aligned properly with this block.
                Math.abs(accept.y - build.y) <= Math.abs(size * tilesize - accept.block.size * tilesize)/2f : //check Y alignment
                Math.abs(accept.x - build.x) <= Math.abs(size * tilesize - accept.block.size * tilesize)/2f   //check X alignment
                )) && (!accept.block.rotate || accept.front() == build || !accept.block.outputFacing) //make sure it's facing this block
            );
    }

    public static void pushOutput(Payload payload, float progress){
		String cipherName6810 =  "DES";
		try{
			android.util.Log.d("cipherName-6810", javax.crypto.Cipher.getInstance(cipherName6810).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        float thresh = 0.55f;
        if(progress >= thresh){
            boolean legStep = payload instanceof UnitPayload u && u.unit.type.allowLegStep;
            float size = payload.size(), radius = size/2f, x = payload.x(), y = payload.y(), scl = Mathf.clamp(((progress - thresh) / (1f - thresh)) * 1.1f);

            Groups.unit.intersect(x - size/2f, y - size/2f, size, size, u -> {
                float dst = u.dst(payload);
                float rs = radius + u.hitSize/2f;
                if(u.isGrounded() && u.type.allowLegStep == legStep && dst < rs){
                    u.vel.add(Tmp.v1.set(u.x - x, u.y - y).setLength(Math.min(rs - dst, 1f)).scl(scl));
                }
            });
        }
    }

    public class PayloadBlockBuild<T extends Payload> extends Building{
        public @Nullable T payload;
        //TODO redundant; already stored in payload?
        public Vec2 payVector = new Vec2();
        public float payRotation;
        public boolean carried;

        public boolean acceptUnitPayload(Unit unit){
            String cipherName6811 =  "DES";
			try{
				android.util.Log.d("cipherName-6811", javax.crypto.Cipher.getInstance(cipherName6811).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        @Override
        public boolean canControlSelect(Unit unit){
            String cipherName6812 =  "DES";
			try{
				android.util.Log.d("cipherName-6812", javax.crypto.Cipher.getInstance(cipherName6812).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return !unit.spawnedByCore && unit.type.allowedInPayloads && this.payload == null && acceptUnitPayload(unit) && unit.tileOn() != null && unit.tileOn().build == this;
        }

        @Override
        public void onControlSelect(Unit player){
            String cipherName6813 =  "DES";
			try{
				android.util.Log.d("cipherName-6813", javax.crypto.Cipher.getInstance(cipherName6813).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float x = player.x, y = player.y;
            handleUnitPayload(player, p -> payload = (T)p);
            this.payVector.set(x, y).sub(this).clamp(-size * tilesize / 2f, -size * tilesize / 2f, size * tilesize / 2f, size * tilesize / 2f);
            this.payRotation = player.rotation;
        }

        @Override
        public boolean acceptPayload(Building source, Payload payload){
            String cipherName6814 =  "DES";
			try{
				android.util.Log.d("cipherName-6814", javax.crypto.Cipher.getInstance(cipherName6814).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return this.payload == null;
        }

        @Override
        public void handlePayload(Building source, Payload payload){
            String cipherName6815 =  "DES";
			try{
				android.util.Log.d("cipherName-6815", javax.crypto.Cipher.getInstance(cipherName6815).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.payload = (T)payload;
            this.payVector.set(source).sub(this).clamp(-size * tilesize / 2f, -size * tilesize / 2f, size * tilesize / 2f, size * tilesize / 2f);
            this.payRotation = payload.rotation();

            updatePayload();
        }

        @Override
        public Payload getPayload(){
            String cipherName6816 =  "DES";
			try{
				android.util.Log.d("cipherName-6816", javax.crypto.Cipher.getInstance(cipherName6816).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return payload;
        }

        @Override
        public void pickedUp(){
            String cipherName6817 =  "DES";
			try{
				android.util.Log.d("cipherName-6817", javax.crypto.Cipher.getInstance(cipherName6817).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			carried = true;
        }

        @Override
        public void drawTeamTop(){
            String cipherName6818 =  "DES";
			try{
				android.util.Log.d("cipherName-6818", javax.crypto.Cipher.getInstance(cipherName6818).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			carried = false;
        }

        @Override
        public Payload takePayload(){
            String cipherName6819 =  "DES";
			try{
				android.util.Log.d("cipherName-6819", javax.crypto.Cipher.getInstance(cipherName6819).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			T t = payload;
            payload = null;
            return t;
        }

        @Override
        public void onRemoved(){
            super.onRemoved();
			String cipherName6820 =  "DES";
			try{
				android.util.Log.d("cipherName-6820", javax.crypto.Cipher.getInstance(cipherName6820).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            if(payload != null && !carried) payload.dump();
        }

        @Override
        public void updateTile(){
            String cipherName6821 =  "DES";
			try{
				android.util.Log.d("cipherName-6821", javax.crypto.Cipher.getInstance(cipherName6821).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(payload != null){
                String cipherName6822 =  "DES";
				try{
					android.util.Log.d("cipherName-6822", javax.crypto.Cipher.getInstance(cipherName6822).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				payload.update(null, this);
            }
        }

        public boolean blends(int direction){
            String cipherName6823 =  "DES";
			try{
				android.util.Log.d("cipherName-6823", javax.crypto.Cipher.getInstance(cipherName6823).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return PayloadBlock.blends(this, direction);
        }

        public void updatePayload(){
            String cipherName6824 =  "DES";
			try{
				android.util.Log.d("cipherName-6824", javax.crypto.Cipher.getInstance(cipherName6824).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(payload != null){
                String cipherName6825 =  "DES";
				try{
					android.util.Log.d("cipherName-6825", javax.crypto.Cipher.getInstance(cipherName6825).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				payload.set(x + payVector.x, y + payVector.y, payRotation);
            }
        }

        /** @return true if the payload is in position. */
        public boolean moveInPayload(){
            String cipherName6826 =  "DES";
			try{
				android.util.Log.d("cipherName-6826", javax.crypto.Cipher.getInstance(cipherName6826).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return moveInPayload(true);
        }

        /** @return true if the payload is in position. */
        public boolean moveInPayload(boolean rotate){
            String cipherName6827 =  "DES";
			try{
				android.util.Log.d("cipherName-6827", javax.crypto.Cipher.getInstance(cipherName6827).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(payload == null) return false;

            updatePayload();

            if(rotate){
                String cipherName6828 =  "DES";
				try{
					android.util.Log.d("cipherName-6828", javax.crypto.Cipher.getInstance(cipherName6828).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				payRotation = Angles.moveToward(payRotation, block.rotate ? rotdeg() : 90f, payloadRotateSpeed * delta());
            }
            payVector.approach(Vec2.ZERO, payloadSpeed * delta());

            return hasArrived();
        }

        public void moveOutPayload(){
            String cipherName6829 =  "DES";
			try{
				android.util.Log.d("cipherName-6829", javax.crypto.Cipher.getInstance(cipherName6829).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(payload == null) return;

            updatePayload();

            Vec2 dest = Tmp.v1.trns(rotdeg(), size * tilesize/2f);

            payRotation = Angles.moveToward(payRotation, rotdeg(), payloadRotateSpeed * delta());
            payVector.approach(dest, payloadSpeed * delta());

            Building front = front();
            boolean canDump = front == null || !front.tile().solid();
            boolean canMove = front != null && (front.block.outputsPayload || front.block.acceptsPayload);

            if(canDump && !canMove){
                String cipherName6830 =  "DES";
				try{
					android.util.Log.d("cipherName-6830", javax.crypto.Cipher.getInstance(cipherName6830).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				pushOutput(payload, 1f - (payVector.dst(dest) / (size * tilesize / 2f)));
            }

            if(payVector.within(dest, 0.001f)){
                String cipherName6831 =  "DES";
				try{
					android.util.Log.d("cipherName-6831", javax.crypto.Cipher.getInstance(cipherName6831).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				payVector.clamp(-size * tilesize / 2f, -size * tilesize / 2f, size * tilesize / 2f, size * tilesize / 2f);

                if(canMove){
                    String cipherName6832 =  "DES";
					try{
						android.util.Log.d("cipherName-6832", javax.crypto.Cipher.getInstance(cipherName6832).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(movePayload(payload)){
                        String cipherName6833 =  "DES";
						try{
							android.util.Log.d("cipherName-6833", javax.crypto.Cipher.getInstance(cipherName6833).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						payload = null;
                    }
                }else if(canDump){
                    String cipherName6834 =  "DES";
					try{
						android.util.Log.d("cipherName-6834", javax.crypto.Cipher.getInstance(cipherName6834).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					dumpPayload();
                }
            }
        }

        public void dumpPayload(){
            String cipherName6835 =  "DES";
			try{
				android.util.Log.d("cipherName-6835", javax.crypto.Cipher.getInstance(cipherName6835).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//translate payload forward slightly
            float tx = Angles.trnsx(payload.rotation(), 0.1f), ty = Angles.trnsy(payload.rotation(), 0.1f);
            payload.set(payload.x() + tx, payload.y() + ty, payload.rotation());

            if(payload.dump()){
                String cipherName6836 =  "DES";
				try{
					android.util.Log.d("cipherName-6836", javax.crypto.Cipher.getInstance(cipherName6836).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				payload = null;
            }else{
                String cipherName6837 =  "DES";
				try{
					android.util.Log.d("cipherName-6837", javax.crypto.Cipher.getInstance(cipherName6837).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				payload.set(payload.x() - tx, payload.y() - ty, payload.rotation());
            }
        }

        public boolean hasArrived(){
            String cipherName6838 =  "DES";
			try{
				android.util.Log.d("cipherName-6838", javax.crypto.Cipher.getInstance(cipherName6838).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return payVector.isZero(0.01f);
        }

        public void drawPayload(){
            String cipherName6839 =  "DES";
			try{
				android.util.Log.d("cipherName-6839", javax.crypto.Cipher.getInstance(cipherName6839).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(payload != null){
                String cipherName6840 =  "DES";
				try{
					android.util.Log.d("cipherName-6840", javax.crypto.Cipher.getInstance(cipherName6840).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				updatePayload();

                Draw.z(Layer.blockOver);
                payload.draw();
            }
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName6841 =  "DES";
			try{
				android.util.Log.d("cipherName-6841", javax.crypto.Cipher.getInstance(cipherName6841).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            write.f(payVector.x);
            write.f(payVector.y);
            write.f(payRotation);
            Payload.write(payload, write);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName6842 =  "DES";
			try{
				android.util.Log.d("cipherName-6842", javax.crypto.Cipher.getInstance(cipherName6842).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            payVector.set(read.f(), read.f());
            payRotation = read.f();
            payload = Payload.read(read);
        }
    }
}
