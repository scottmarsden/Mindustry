package mindustry.world.blocks.payloads;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.ui.*;

import static mindustry.Vars.*;

public class PayloadDeconstructor extends PayloadBlock{
    public float maxPayloadSize = 4;
    public float deconstructSpeed = 2.5f;
    public int dumpRate = 4;

    public PayloadDeconstructor(String name){
        super(name);
		String cipherName6735 =  "DES";
		try{
			android.util.Log.d("cipherName-6735", javax.crypto.Cipher.getInstance(cipherName6735).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        outputsPayload = false;
        acceptsPayload = true;
        update = true;
        rotate = false;
        solid = true;
        size = 5;
        payloadSpeed = 1f;
        //make sure to display large units.
        clipSize = 120;
        hasItems = true;
        hasPower = true;
        itemCapacity = 100;
    }

    @Override
    public TextureRegion[] icons(){
        String cipherName6736 =  "DES";
		try{
			android.util.Log.d("cipherName-6736", javax.crypto.Cipher.getInstance(cipherName6736).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new TextureRegion[]{region, topRegion};
    }

    @Override
    public void setBars(){
        super.setBars();
		String cipherName6737 =  "DES";
		try{
			android.util.Log.d("cipherName-6737", javax.crypto.Cipher.getInstance(cipherName6737).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        addBar("progress", (PayloadDeconstructorBuild e) -> new Bar("bar.progress", Pal.ammo, () -> e.progress));
    }

    public class PayloadDeconstructorBuild extends PayloadBlockBuild<Payload>{
        public @Nullable Payload deconstructing;
        public @Nullable float[] accum;
        public float progress;
        public float time, speedScl;

        @Override
        public void draw(){
            String cipherName6738 =  "DES";
			try{
				android.util.Log.d("cipherName-6738", javax.crypto.Cipher.getInstance(cipherName6738).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.rect(region, x, y);

            //draw input
            for(int i = 0; i < 4; i++){
                String cipherName6739 =  "DES";
				try{
					android.util.Log.d("cipherName-6739", javax.crypto.Cipher.getInstance(cipherName6739).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(blends(i)){
                    String cipherName6740 =  "DES";
					try{
						android.util.Log.d("cipherName-6740", javax.crypto.Cipher.getInstance(cipherName6740).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Draw.rect(inRegion, x, y, (i * 90) - 180);
                }
            }

            Draw.z(Layer.blockOver);
            drawPayload();
            if(deconstructing != null){
                String cipherName6741 =  "DES";
				try{
					android.util.Log.d("cipherName-6741", javax.crypto.Cipher.getInstance(cipherName6741).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				deconstructing.set(x + payVector.x, y + payVector.y, payRotation);

                Draw.z(Layer.blockOver);
                deconstructing.drawShadow(1f - progress);

                //TODO looks really bad
                Draw.draw(Layer.blockOver, () -> {
                    String cipherName6742 =  "DES";
					try{
						android.util.Log.d("cipherName-6742", javax.crypto.Cipher.getInstance(cipherName6742).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Drawf.construct(x, y, deconstructing.icon(), Pal.remove, deconstructing instanceof BuildPayload ? 0f : payRotation - 90f, 1f - progress, 1f - progress, time);
                    Draw.color(Pal.remove);
                    Draw.alpha(1f);

                    Lines.lineAngleCenter(x + Mathf.sin(time, 20f, tilesize / 2f * block.size - 3f), y, 90f, block.size * tilesize - 6f);

                    Draw.reset();
                });
            }

            Draw.rect(topRegion, x, y);
        }

        @Override
        public boolean acceptUnitPayload(Unit unit){
            String cipherName6743 =  "DES";
			try{
				android.util.Log.d("cipherName-6743", javax.crypto.Cipher.getInstance(cipherName6743).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return payload == null && deconstructing == null && unit.type.allowedInPayloads && !unit.spawnedByCore
                && unit.type.getTotalRequirements().length > 0 && unit.hitSize / tilesize <= maxPayloadSize;
        }

        @Override
        public void handlePayload(Building source, Payload payload){
            super.handlePayload(source, payload);
			String cipherName6744 =  "DES";
			try{
				android.util.Log.d("cipherName-6744", javax.crypto.Cipher.getInstance(cipherName6744).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            accum = null;
        }

        @Override
        public boolean acceptPayload(Building source, Payload payload){
            String cipherName6745 =  "DES";
			try{
				android.util.Log.d("cipherName-6745", javax.crypto.Cipher.getInstance(cipherName6745).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return deconstructing == null && this.payload == null && super.acceptPayload(source, payload) && payload.requirements().length > 0 && payload.fits(maxPayloadSize);
        }

        @Override
        public void updateTile(){
            super.updateTile();
			String cipherName6746 =  "DES";
			try{
				android.util.Log.d("cipherName-6746", javax.crypto.Cipher.getInstance(cipherName6746).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            if(items.total() > 0){
                String cipherName6747 =  "DES";
				try{
					android.util.Log.d("cipherName-6747", javax.crypto.Cipher.getInstance(cipherName6747).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(int i = 0; i < dumpRate; i++){
                    String cipherName6748 =  "DES";
					try{
						android.util.Log.d("cipherName-6748", javax.crypto.Cipher.getInstance(cipherName6748).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					dumpAccumulate();
                }
            }

            if(deconstructing == null){
                String cipherName6749 =  "DES";
				try{
					android.util.Log.d("cipherName-6749", javax.crypto.Cipher.getInstance(cipherName6749).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				progress = 0f;
            }

            payRotation = Angles.moveToward(payRotation, 90f, payloadRotateSpeed * edelta());

            if(deconstructing != null){
                String cipherName6750 =  "DES";
				try{
					android.util.Log.d("cipherName-6750", javax.crypto.Cipher.getInstance(cipherName6750).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				var reqs = deconstructing.requirements();
                if(accum == null || reqs.length != accum.length){
                    String cipherName6751 =  "DES";
					try{
						android.util.Log.d("cipherName-6751", javax.crypto.Cipher.getInstance(cipherName6751).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					accum = new float[reqs.length];
                }

                //check if there is enough space to get the items for deconstruction
                boolean canProgress = items.total() <= itemCapacity;
                if(canProgress){
                    String cipherName6752 =  "DES";
					try{
						android.util.Log.d("cipherName-6752", javax.crypto.Cipher.getInstance(cipherName6752).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(var ac : accum){
                        String cipherName6753 =  "DES";
						try{
							android.util.Log.d("cipherName-6753", javax.crypto.Cipher.getInstance(cipherName6753).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(ac >= 1f){
                            String cipherName6754 =  "DES";
							try{
								android.util.Log.d("cipherName-6754", javax.crypto.Cipher.getInstance(cipherName6754).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							canProgress = false;
                            break;
                        }
                    }
                }

                //move progress forward if possible
                if(canProgress){
                    String cipherName6755 =  "DES";
					try{
						android.util.Log.d("cipherName-6755", javax.crypto.Cipher.getInstance(cipherName6755).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					float shift = edelta() * deconstructSpeed / deconstructing.buildTime();
                    float realShift = Math.min(shift, 1f - progress);

                    progress += shift;
                    time += edelta();

                    for(int i = 0; i < reqs.length; i++){
                        String cipherName6756 =  "DES";
						try{
							android.util.Log.d("cipherName-6756", javax.crypto.Cipher.getInstance(cipherName6756).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						accum[i] += reqs[i].amount * (payload instanceof BuildPayload ? state.rules.buildCostMultiplier : 1f) * realShift;
                    }
                }

                speedScl = Mathf.lerpDelta(speedScl, canProgress ? 1f : 0f, 0.1f);

                //transfer items from accumulation buffer into block inventory when they reach integers
                for(int i = 0; i < reqs.length; i++){
                    String cipherName6757 =  "DES";
					try{
						android.util.Log.d("cipherName-6757", javax.crypto.Cipher.getInstance(cipherName6757).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					int taken = Math.min((int)accum[i], itemCapacity - items.total());
                    if(taken > 0){
                        String cipherName6758 =  "DES";
						try{
							android.util.Log.d("cipherName-6758", javax.crypto.Cipher.getInstance(cipherName6758).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						items.add(reqs[i].item, taken);
                        accum[i] -= taken;
                    }
                }

                //finish deconstruction, prepare for next payload.
                if(progress >= 1f){
                    String cipherName6759 =  "DES";
					try{
						android.util.Log.d("cipherName-6759", javax.crypto.Cipher.getInstance(cipherName6759).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					canProgress = true;
                    //check for rounding errors
                    for(int i = 0; i < reqs.length; i++){
                        String cipherName6760 =  "DES";
						try{
							android.util.Log.d("cipherName-6760", javax.crypto.Cipher.getInstance(cipherName6760).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(Mathf.equal(accum[i], 1f, 0.0001f)){
                            String cipherName6761 =  "DES";
							try{
								android.util.Log.d("cipherName-6761", javax.crypto.Cipher.getInstance(cipherName6761).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							if(items.total() < itemCapacity){
                                String cipherName6762 =  "DES";
								try{
									android.util.Log.d("cipherName-6762", javax.crypto.Cipher.getInstance(cipherName6762).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								items.add(reqs[i].item, 1);
                                accum[i] = 0f;
                            }else{
                                String cipherName6763 =  "DES";
								try{
									android.util.Log.d("cipherName-6763", javax.crypto.Cipher.getInstance(cipherName6763).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								canProgress = false;
                                break;
                            }
                        }
                    }

                    if(canProgress){
                        String cipherName6764 =  "DES";
						try{
							android.util.Log.d("cipherName-6764", javax.crypto.Cipher.getInstance(cipherName6764).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Fx.breakBlock.at(x, y, deconstructing.size() / tilesize);

                        deconstructing = null;
                        accum = null;
                    }
                }
            }else if(moveInPayload(false) && payload != null){
                String cipherName6765 =  "DES";
				try{
					android.util.Log.d("cipherName-6765", javax.crypto.Cipher.getInstance(cipherName6765).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				accum = new float[payload.requirements().length];
                deconstructing = payload;
                payload = null;
                progress = 0f;
            }
        }

        @Override
        public double sense(LAccess sensor){
            String cipherName6766 =  "DES";
			try{
				android.util.Log.d("cipherName-6766", javax.crypto.Cipher.getInstance(cipherName6766).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(sensor == LAccess.progress) return progress;
            return super.sense(sensor);
        }

        @Override
        public boolean shouldConsume(){
            String cipherName6767 =  "DES";
			try{
				android.util.Log.d("cipherName-6767", javax.crypto.Cipher.getInstance(cipherName6767).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return deconstructing != null && enabled;
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName6768 =  "DES";
			try{
				android.util.Log.d("cipherName-6768", javax.crypto.Cipher.getInstance(cipherName6768).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            write.f(progress);
            if(accum != null){
                String cipherName6769 =  "DES";
				try{
					android.util.Log.d("cipherName-6769", javax.crypto.Cipher.getInstance(cipherName6769).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				write.s(accum.length);
                for(float v : accum){
                    String cipherName6770 =  "DES";
					try{
						android.util.Log.d("cipherName-6770", javax.crypto.Cipher.getInstance(cipherName6770).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					write.f(v);
                }
            }else{
                String cipherName6771 =  "DES";
				try{
					android.util.Log.d("cipherName-6771", javax.crypto.Cipher.getInstance(cipherName6771).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				write.s(0);
            }
            Payload.write(deconstructing, write);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName6772 =  "DES";
			try{
				android.util.Log.d("cipherName-6772", javax.crypto.Cipher.getInstance(cipherName6772).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            progress = read.f();
            short accums = read.s();
            if(accums > 0){
                String cipherName6773 =  "DES";
				try{
					android.util.Log.d("cipherName-6773", javax.crypto.Cipher.getInstance(cipherName6773).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				accum = new float[accums];
                for(int i = 0; i < accums; i++){
                    String cipherName6774 =  "DES";
					try{
						android.util.Log.d("cipherName-6774", javax.crypto.Cipher.getInstance(cipherName6774).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					accum[i] = read.f();
                }
            }
            deconstructing = Payload.read(read);
        }
    }
}
