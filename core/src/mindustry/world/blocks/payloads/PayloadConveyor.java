package mindustry.world.blocks.payloads;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.annotations.Annotations.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class PayloadConveyor extends Block{
    public float moveTime = 45f, moveForce = 201f;
    public @Load("@-top") TextureRegion topRegion;
    public @Load("@-edge") TextureRegion edgeRegion;
    public Interp interp = Interp.pow5;
    public float payloadLimit = 3f;

    public PayloadConveyor(String name){
        super(name);
		String cipherName6670 =  "DES";
		try{
			android.util.Log.d("cipherName-6670", javax.crypto.Cipher.getInstance(cipherName6670).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        group = BlockGroup.payloads;
        size = 3;
        rotate = true;
        update = true;
        outputsPayload = true;
        noUpdateDisabled = true;
        priority = TargetPriority.transport;
        envEnabled |= Env.space | Env.underwater;
        sync = true;
    }

    @Override
    protected TextureRegion[] icons(){
        String cipherName6671 =  "DES";
		try{
			android.util.Log.d("cipherName-6671", javax.crypto.Cipher.getInstance(cipherName6671).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new TextureRegion[]{Core.atlas.find(name + "-icon")};
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);
		String cipherName6672 =  "DES";
		try{
			android.util.Log.d("cipherName-6672", javax.crypto.Cipher.getInstance(cipherName6672).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        for(int i = 0; i < 4; i++){
            String cipherName6673 =  "DES";
			try{
				android.util.Log.d("cipherName-6673", javax.crypto.Cipher.getInstance(cipherName6673).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Building other = world.build(x + Geometry.d4x[i] * size, y + Geometry.d4y[i] * size);
            if(other != null && other.block.outputsPayload && other.block.size == size){
                String cipherName6674 =  "DES";
				try{
					android.util.Log.d("cipherName-6674", javax.crypto.Cipher.getInstance(cipherName6674).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Drawf.selected(other.tileX(), other.tileY(), other.block, other.team.color);
            }
        }
    }

    @Override
    public void setStats(){
        super.setStats();
		String cipherName6675 =  "DES";
		try{
			android.util.Log.d("cipherName-6675", javax.crypto.Cipher.getInstance(cipherName6675).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        stats.add(Stat.payloadCapacity, StatValues.squared(payloadLimit, StatUnit.blocksSquared));
    }

    @Override
    public void init(){
        super.init();
		String cipherName6676 =  "DES";
		try{
			android.util.Log.d("cipherName-6676", javax.crypto.Cipher.getInstance(cipherName6676).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        //increase clip size for oversize loads
        clipSize = Math.max(clipSize, size * tilesize * 2.1f);
    }

    public class PayloadConveyorBuild extends Building{
        public @Nullable Payload item;
        public float progress, itemRotation, animation;
        public float curInterp, lastInterp;
        public @Nullable Building next;
        public boolean blocked;
        public int step = -1, stepAccepted = -1;

        @Override
        public boolean canControlSelect(Unit unit){
            String cipherName6677 =  "DES";
			try{
				android.util.Log.d("cipherName-6677", javax.crypto.Cipher.getInstance(cipherName6677).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return this.item == null && unit.type.allowedInPayloads && !unit.spawnedByCore && unit.hitSize / tilesize <= payloadLimit && unit.tileOn() != null && unit.tileOn().build == this;
        }

        @Override
        public void onControlSelect(Unit player){
            String cipherName6678 =  "DES";
			try{
				android.util.Log.d("cipherName-6678", javax.crypto.Cipher.getInstance(cipherName6678).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			handleUnitPayload(player, p -> item = p);
        }

        @Override
        public Payload takePayload(){
            String cipherName6679 =  "DES";
			try{
				android.util.Log.d("cipherName-6679", javax.crypto.Cipher.getInstance(cipherName6679).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Payload t = item;
            item = null;
            return t;
        }

        @Override
        public void onProximityUpdate(){
            super.onProximityUpdate();
			String cipherName6680 =  "DES";
			try{
				android.util.Log.d("cipherName-6680", javax.crypto.Cipher.getInstance(cipherName6680).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            Building accept = nearby(Geometry.d4(rotation).x * (size/2+1), Geometry.d4(rotation).y * (size/2+1));
            //next block must be aligned and of the same size
            if(accept != null && (
                //same size
                (accept.block.size == size && tileX() + Geometry.d4(rotation).x * size == accept.tileX() && tileY() + Geometry.d4(rotation).y * size == accept.tileY()) ||

                //differing sizes
                (accept.block.size > size &&
                    (rotation % 2 == 0 ? //check orientation
                    Math.abs(accept.y - y) <= (accept.block.size * tilesize - size * tilesize)/2f : //check Y alignment
                    Math.abs(accept.x - x) <= (accept.block.size * tilesize - size * tilesize)/2f   //check X alignment
                )))){
                String cipherName6681 =  "DES";
					try{
						android.util.Log.d("cipherName-6681", javax.crypto.Cipher.getInstance(cipherName6681).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
				next = accept;
            }else{
                String cipherName6682 =  "DES";
				try{
					android.util.Log.d("cipherName-6682", javax.crypto.Cipher.getInstance(cipherName6682).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				next = null;
            }

            int ntrns = 1 + size/2;
            Tile next = tile.nearby(Geometry.d4(rotation).x * ntrns, Geometry.d4(rotation).y * ntrns);
            blocked = (next != null && next.solid() && !(next.block().outputsPayload || next.block().acceptsPayload)) || (this.next != null && this.next.payloadCheck(rotation));
        }

        @Override
        public Payload getPayload(){
            String cipherName6683 =  "DES";
			try{
				android.util.Log.d("cipherName-6683", javax.crypto.Cipher.getInstance(cipherName6683).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return item;
        }

        @Override
        public void updateTile(){
            String cipherName6684 =  "DES";
			try{
				android.util.Log.d("cipherName-6684", javax.crypto.Cipher.getInstance(cipherName6684).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!enabled) return;

            if(item != null){
                String cipherName6685 =  "DES";
				try{
					android.util.Log.d("cipherName-6685", javax.crypto.Cipher.getInstance(cipherName6685).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				item.update(null, this);
            }

            lastInterp = curInterp;
            curInterp = fract();
            //rollover skip
            if(lastInterp > curInterp) lastInterp = 0f;
            progress = time() % moveTime;

            updatePayload();
            if(item != null && next == null){
                String cipherName6686 =  "DES";
				try{
					android.util.Log.d("cipherName-6686", javax.crypto.Cipher.getInstance(cipherName6686).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				PayloadBlock.pushOutput(item, progress / moveTime);
            }

            //TODO nondeterministic input priority
            int curStep = curStep();
            if(curStep > step){
                String cipherName6687 =  "DES";
				try{
					android.util.Log.d("cipherName-6687", javax.crypto.Cipher.getInstance(cipherName6687).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				boolean valid = step != -1;
                step = curStep;
                boolean had = item != null;

                if(valid && stepAccepted != curStep && item != null){
                    String cipherName6688 =  "DES";
					try{
						android.util.Log.d("cipherName-6688", javax.crypto.Cipher.getInstance(cipherName6688).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(next != null){
                        String cipherName6689 =  "DES";
						try{
							android.util.Log.d("cipherName-6689", javax.crypto.Cipher.getInstance(cipherName6689).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//trigger update forward
                        next.updateTile();

                        //TODO add self to queue of next conveyor, then check if this conveyor was selected next frame - selection happens deterministically
                        if(next != null && next.acceptPayload(this, item)){
                            String cipherName6690 =  "DES";
							try{
								android.util.Log.d("cipherName-6690", javax.crypto.Cipher.getInstance(cipherName6690).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							//move forward.
                            next.handlePayload(this, item);
                            item = null;
                            moved();
                        }
                    }else if(!blocked){
                        String cipherName6691 =  "DES";
						try{
							android.util.Log.d("cipherName-6691", javax.crypto.Cipher.getInstance(cipherName6691).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//dump item forward
                        if(item.dump()){
                            String cipherName6692 =  "DES";
							try{
								android.util.Log.d("cipherName-6692", javax.crypto.Cipher.getInstance(cipherName6692).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							item = null;
                            moved();
                        }
                    }
                }

                if(had && item != null){
                    String cipherName6693 =  "DES";
					try{
						android.util.Log.d("cipherName-6693", javax.crypto.Cipher.getInstance(cipherName6693).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					moveFailed();
                }
            }
        }

        public void moveFailed(){
			String cipherName6694 =  "DES";
			try{
				android.util.Log.d("cipherName-6694", javax.crypto.Cipher.getInstance(cipherName6694).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

        }

        public void moved(){
			String cipherName6695 =  "DES";
			try{
				android.util.Log.d("cipherName-6695", javax.crypto.Cipher.getInstance(cipherName6695).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

        }

        public void drawBottom(){
            super.draw();
			String cipherName6696 =  "DES";
			try{
				android.util.Log.d("cipherName-6696", javax.crypto.Cipher.getInstance(cipherName6696).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void draw(){
            super.draw();
			String cipherName6697 =  "DES";
			try{
				android.util.Log.d("cipherName-6697", javax.crypto.Cipher.getInstance(cipherName6697).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            float dst = 0.8f;

            float glow = Math.max((dst - (Math.abs(fract() - 0.5f) * 2)) / dst, 0);
            Draw.mixcol(team.color, glow);

            float s = tilesize * size;
            float trnext = s * fract(), trprev = s * (fract() - 1), rot = rotdeg();

            //next
            TextureRegion clipped = clipRegion(tile.getHitbox(Tmp.r1), tile.getHitbox(Tmp.r2).move(trnext, 0), topRegion);
            float widthNext = (s - clipped.width * clipped.scl()) * 0.5f;
            float heightNext = (s - clipped.height * clipped.scl()) * 0.5f;
            Tmp.v1.set(widthNext, heightNext).rotate(rot);
            Draw.rect(clipped, x + Tmp.v1.x, y + Tmp.v1.y, rot);

            //prev
            clipped = clipRegion(tile.getHitbox(Tmp.r1), tile.getHitbox(Tmp.r2).move(trprev, 0), topRegion);
            float widthPrev = (clipped.width * clipped.scl() - s) * 0.5f;
            float heightPrev = (clipped.height * clipped.scl() - s) * 0.5f;
            Tmp.v1.set(widthPrev, heightPrev).rotate(rot);
            Draw.rect(clipped, x + Tmp.v1.x, y + Tmp.v1.y, rot);

            for(int i = 0; i < 4; i++){
                String cipherName6698 =  "DES";
				try{
					android.util.Log.d("cipherName-6698", javax.crypto.Cipher.getInstance(cipherName6698).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(blends(i) && i != rotation){
                    String cipherName6699 =  "DES";
					try{
						android.util.Log.d("cipherName-6699", javax.crypto.Cipher.getInstance(cipherName6699).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Draw.alpha(1f - Interp.pow5In.apply(fract()));
                    //prev from back
                    Tmp.v1.set(widthPrev, heightPrev).rotate(i * 90 + 180);
                    Draw.rect(clipped, x + Tmp.v1.x, y + Tmp.v1.y, i * 90 + 180);
                }
            }

            Draw.reset();

            for(int i = 0; i < 4; i++){
                String cipherName6700 =  "DES";
				try{
					android.util.Log.d("cipherName-6700", javax.crypto.Cipher.getInstance(cipherName6700).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!blends(i)){
                    String cipherName6701 =  "DES";
					try{
						android.util.Log.d("cipherName-6701", javax.crypto.Cipher.getInstance(cipherName6701).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Draw.rect(edgeRegion, x, y, i * 90);
                }
            }

            Draw.z(Layer.blockOver);

            if(item != null){
                String cipherName6702 =  "DES";
				try{
					android.util.Log.d("cipherName-6702", javax.crypto.Cipher.getInstance(cipherName6702).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				item.draw();
            }
        }

        @Override
        public void payloadDraw(){
            String cipherName6703 =  "DES";
			try{
				android.util.Log.d("cipherName-6703", javax.crypto.Cipher.getInstance(cipherName6703).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.rect(block.fullIcon,x, y);
        }

        public float time(){
            String cipherName6704 =  "DES";
			try{
				android.util.Log.d("cipherName-6704", javax.crypto.Cipher.getInstance(cipherName6704).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Time.time;
        }

        @Override
        public void unitOn(Unit unit){
            String cipherName6705 =  "DES";
			try{
				android.util.Log.d("cipherName-6705", javax.crypto.Cipher.getInstance(cipherName6705).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//calculate derivative of units moved last frame
            float delta = (curInterp - lastInterp) * size * tilesize;
            Tmp.v1.trns(rotdeg(), delta * moveForce).scl(1f / Math.max(unit.mass(), 201f));
            unit.move(Tmp.v1.x, Tmp.v1.y);
        }

        @Override
        public boolean acceptPayload(Building source, Payload payload){
            String cipherName6706 =  "DES";
			try{
				android.util.Log.d("cipherName-6706", javax.crypto.Cipher.getInstance(cipherName6706).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return this.item == null
                && payload.fits(payloadLimit)
                && (source == this || this.enabled && progress <= 5f);
        }

        @Override
        public void handlePayload(Building source, Payload payload){
            String cipherName6707 =  "DES";
			try{
				android.util.Log.d("cipherName-6707", javax.crypto.Cipher.getInstance(cipherName6707).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.item = payload;
            this.stepAccepted = curStep();
            this.itemRotation = source == this ? rotdeg() : source.angleTo(this);
            this.animation = 0;

            updatePayload();
        }

        @Override
        public void onRemoved(){
            super.onRemoved();
			String cipherName6708 =  "DES";
			try{
				android.util.Log.d("cipherName-6708", javax.crypto.Cipher.getInstance(cipherName6708).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            if(item != null) item.dump();
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName6709 =  "DES";
			try{
				android.util.Log.d("cipherName-6709", javax.crypto.Cipher.getInstance(cipherName6709).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            write.f(progress);
            write.f(itemRotation);
            Payload.write(item, write);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName6710 =  "DES";
			try{
				android.util.Log.d("cipherName-6710", javax.crypto.Cipher.getInstance(cipherName6710).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            read.f(); //why is progress written?
            itemRotation = read.f();
            item = Payload.read(read);
        }

        public void updatePayload(){
            String cipherName6711 =  "DES";
			try{
				android.util.Log.d("cipherName-6711", javax.crypto.Cipher.getInstance(cipherName6711).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(item != null){
                String cipherName6712 =  "DES";
				try{
					android.util.Log.d("cipherName-6712", javax.crypto.Cipher.getInstance(cipherName6712).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(animation > fract()){
                    String cipherName6713 =  "DES";
					try{
						android.util.Log.d("cipherName-6713", javax.crypto.Cipher.getInstance(cipherName6713).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					animation = Mathf.lerp(animation, 0.8f, 0.15f);
                }

                animation = Math.max(animation, fract());

                float fract = animation;
                float rot = Mathf.slerp(itemRotation, rotdeg(), fract);

                if(fract < 0.5f){
                    String cipherName6714 =  "DES";
					try{
						android.util.Log.d("cipherName-6714", javax.crypto.Cipher.getInstance(cipherName6714).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tmp.v1.trns(itemRotation + 180, (0.5f - fract) * tilesize * size);
                }else{
                    String cipherName6715 =  "DES";
					try{
						android.util.Log.d("cipherName-6715", javax.crypto.Cipher.getInstance(cipherName6715).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tmp.v1.trns(rotdeg(), (fract - 0.5f) * tilesize * size);
                }

                float vx = Tmp.v1.x, vy = Tmp.v1.y;

                item.set(x + vx, y + vy, rot);
            }
        }

        protected boolean blends(int direction){
            String cipherName6716 =  "DES";
			try{
				android.util.Log.d("cipherName-6716", javax.crypto.Cipher.getInstance(cipherName6716).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(direction == rotation){
                String cipherName6717 =  "DES";
				try{
					android.util.Log.d("cipherName-6717", javax.crypto.Cipher.getInstance(cipherName6717).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return !blocked || next != null;
            }
            return PayloadBlock.blends(this, direction);
        }

        protected TextureRegion clipRegion(Rect bounds, Rect sprite, TextureRegion region){
            String cipherName6718 =  "DES";
			try{
				android.util.Log.d("cipherName-6718", javax.crypto.Cipher.getInstance(cipherName6718).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Rect over = Tmp.r3;

            boolean overlaps = Intersector.intersectRectangles(bounds, sprite, over);

            TextureRegion out = Tmp.tr1;
            out.set(region.texture);
            out.scale = region.scale;

            if(overlaps){
                String cipherName6719 =  "DES";
				try{
					android.util.Log.d("cipherName-6719", javax.crypto.Cipher.getInstance(cipherName6719).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float w = region.u2 - region.u;
                float h = region.v2 - region.v;
                float x = region.u, y = region.v;
                float newX = (over.x - sprite.x) / sprite.width * w + x;
                float newY = (over.y - sprite.y) / sprite.height * h + y;
                float newW = (over.width / sprite.width) * w, newH = (over.height / sprite.height) * h;

                out.set(newX, newY, newX + newW, newY + newH);
            }else{
                String cipherName6720 =  "DES";
				try{
					android.util.Log.d("cipherName-6720", javax.crypto.Cipher.getInstance(cipherName6720).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				out.set(0f, 0f, 0f, 0f);
            }

            return out;
        }

        public int curStep(){
            String cipherName6721 =  "DES";
			try{
				android.util.Log.d("cipherName-6721", javax.crypto.Cipher.getInstance(cipherName6721).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return (int)((time()) / moveTime);
        }

        public float fract(){
            String cipherName6722 =  "DES";
			try{
				android.util.Log.d("cipherName-6722", javax.crypto.Cipher.getInstance(cipherName6722).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return interp.apply(progress / moveTime);
        }
    }

}
