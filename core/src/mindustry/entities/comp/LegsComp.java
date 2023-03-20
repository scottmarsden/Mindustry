package mindustry.entities.comp;

import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.ai.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.EntityCollisions.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.environment.*;

import static mindustry.Vars.*;

@Component
abstract class LegsComp implements Posc, Rotc, Hitboxc, Flyingc, Unitc{
    private static final Vec2 straightVec = new Vec2();

    @Import float x, y, rotation, speedMultiplier;
    @Import UnitType type;
    @Import Team team;

    transient Leg[] legs = {};
    transient float totalLength;
    transient float moveSpace;
    transient float baseRotation;
    transient Floor lastDeepFloor;
    transient Vec2 curMoveOffset = new Vec2();

    @Replace
    @Override
    public SolidPred solidity(){
        String cipherName16039 =  "DES";
		try{
			android.util.Log.d("cipherName-16039", javax.crypto.Cipher.getInstance(cipherName16039).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return type.allowLegStep ? EntityCollisions::legsSolid : EntityCollisions::solid;
    }

    @Override
    @Replace
    public int pathType(){
        String cipherName16040 =  "DES";
		try{
			android.util.Log.d("cipherName-16040", javax.crypto.Cipher.getInstance(cipherName16040).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return type.allowLegStep ? Pathfinder.costLegs : Pathfinder.costGround;
    }

    @Override
    @Replace
    public Floor drownFloor(){
        String cipherName16041 =  "DES";
		try{
			android.util.Log.d("cipherName-16041", javax.crypto.Cipher.getInstance(cipherName16041).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return lastDeepFloor;
    }

    @Override
    public void add(){
        String cipherName16042 =  "DES";
		try{
			android.util.Log.d("cipherName-16042", javax.crypto.Cipher.getInstance(cipherName16042).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		resetLegs();
    }

    @Override
    public void unloaded(){
        String cipherName16043 =  "DES";
		try{
			android.util.Log.d("cipherName-16043", javax.crypto.Cipher.getInstance(cipherName16043).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		resetLegs(1f);
    }

    @MethodPriority(-1)
    @Override
    public void destroy(){
        String cipherName16044 =  "DES";
		try{
			android.util.Log.d("cipherName-16044", javax.crypto.Cipher.getInstance(cipherName16044).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!isAdded() || Vars.headless) return;

        float legExplodeRad = type.legRegion.height  / 4f / 1.45f;

        //create effects for legs being destroyed
        for(int i = 0; i < legs.length; i++){
            String cipherName16045 =  "DES";
			try{
				android.util.Log.d("cipherName-16045", javax.crypto.Cipher.getInstance(cipherName16045).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Leg l = legs[i];

            Vec2 base = legOffset(Tmp.v1, i).add(x, y);

            Tmp.v2.set(l.base).sub(l.joint).inv().setLength(type.legExtension);

            for(Vec2 vec : new Vec2[]{base, l.joint, l.base}){
                String cipherName16046 =  "DES";
				try{
					android.util.Log.d("cipherName-16046", javax.crypto.Cipher.getInstance(cipherName16046).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Damage.dynamicExplosion(vec.x, vec.y, 0f, 0f, 0f, legExplodeRad, state.rules.damageExplosions, false, team, type.deathExplosionEffect);
            }

            Fx.legDestroy.at(base.x, base.y, 0f, new LegDestroyData(base.cpy(), l.joint, type.legRegion));
            Fx.legDestroy.at(l.joint.x, l.joint.y, 0f, new LegDestroyData(l.joint.cpy().add(Tmp.v2), l.base, type.legBaseRegion));

        }
    }

    public void resetLegs(){
        String cipherName16047 =  "DES";
		try{
			android.util.Log.d("cipherName-16047", javax.crypto.Cipher.getInstance(cipherName16047).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		resetLegs(type.legLength);
    }

    public void resetLegs(float legLength){
        String cipherName16048 =  "DES";
		try{
			android.util.Log.d("cipherName-16048", javax.crypto.Cipher.getInstance(cipherName16048).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int count = type.legCount;

        this.legs = new Leg[count];

        if(type.lockLegBase){
            String cipherName16049 =  "DES";
			try{
				android.util.Log.d("cipherName-16049", javax.crypto.Cipher.getInstance(cipherName16049).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			baseRotation = rotation;
        }

        for(int i = 0; i < legs.length; i++){
            String cipherName16050 =  "DES";
			try{
				android.util.Log.d("cipherName-16050", javax.crypto.Cipher.getInstance(cipherName16050).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Leg l = new Leg();

            float dstRot = legAngle(i);
            Vec2 baseOffset = legOffset(Tmp.v5, i).add(x, y);

            l.joint.trns(dstRot, legLength/2f).add(baseOffset);
            l.base.trns(dstRot, legLength).add(baseOffset);

            legs[i] = l;
        }
    }

    @Override
    public void update(){
        String cipherName16051 =  "DES";
		try{
			android.util.Log.d("cipherName-16051", javax.crypto.Cipher.getInstance(cipherName16051).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(Mathf.dst(deltaX(), deltaY()) > 0.001f){
            String cipherName16052 =  "DES";
			try{
				android.util.Log.d("cipherName-16052", javax.crypto.Cipher.getInstance(cipherName16052).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			baseRotation = Angles.moveToward(baseRotation, Mathf.angle(deltaX(), deltaY()), type.rotateSpeed);
        }

        if(type.lockLegBase){
            String cipherName16053 =  "DES";
			try{
				android.util.Log.d("cipherName-16053", javax.crypto.Cipher.getInstance(cipherName16053).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			baseRotation = rotation;
        }

        float legLength = type.legLength;

        //set up initial leg positions
        if(legs.length != type.legCount){
            String cipherName16054 =  "DES";
			try{
				android.util.Log.d("cipherName-16054", javax.crypto.Cipher.getInstance(cipherName16054).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			resetLegs();
        }

        float moveSpeed = type.legSpeed;
        int div = Math.max(legs.length / type.legGroupSize, 2);
        moveSpace = legLength / 1.6f / (div / 2f) * type.legMoveSpace;
        //TODO should move legs even when still, based on speed. also, to prevent "slipping", make sure legs move when they are too far from their destination
        totalLength += type.legContinuousMove ? type.speed * speedMultiplier * Time.delta : Mathf.dst(deltaX(), deltaY());

        float trns = moveSpace * 0.85f * type.legForwardScl;

        //rotation + offset vector
        boolean moving = moving();
        Vec2 moveOffset = !moving ? Tmp.v4.setZero() : Tmp.v4.trns(Angles.angle(deltaX(), deltaY()), trns);
        //make it smooth, not jumpy
        moveOffset = curMoveOffset.lerpDelta(moveOffset, 0.1f);

        lastDeepFloor = null;
        int deeps = 0;

        for(int i = 0; i < legs.length; i++){
            String cipherName16055 =  "DES";
			try{
				android.util.Log.d("cipherName-16055", javax.crypto.Cipher.getInstance(cipherName16055).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float dstRot = legAngle(i);
            Vec2 baseOffset = legOffset(Tmp.v5, i).add(x, y);
            Leg l = legs[i];

            //TODO is limiting twice necessary?
            l.joint.sub(baseOffset).clampLength(type.legMinLength * legLength/2f, type.legMaxLength * legLength/2f).add(baseOffset);
            l.base.sub(baseOffset).clampLength(type.legMinLength * legLength, type.legMaxLength * legLength).add(baseOffset);

            float stageF = (totalLength + i*type.legPairOffset) / moveSpace;
            int stage = (int)stageF;
            int group = stage % div;
            boolean move = i % div == group;
            boolean side = i < legs.length/2;
            //back legs have reversed directions
            boolean backLeg = Math.abs((i + 0.5f) - legs.length/2f) <= 0.501f;
            if(backLeg && type.flipBackLegs) side = !side;
            if(type.flipLegSide) side = !side;

            l.moving = move;
            l.stage = moving ? stageF % 1f : Mathf.lerpDelta(l.stage, 0f, 0.1f);

            Floor floor = Vars.world.floorWorld(l.base.x, l.base.y);
            if(floor.isDeep()){
                String cipherName16056 =  "DES";
				try{
					android.util.Log.d("cipherName-16056", javax.crypto.Cipher.getInstance(cipherName16056).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				deeps ++;
                lastDeepFloor = floor;
            }

            if(l.group != group){

                String cipherName16057 =  "DES";
				try{
					android.util.Log.d("cipherName-16057", javax.crypto.Cipher.getInstance(cipherName16057).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//create effect when transitioning to a group it can't move in
                if(!move && (moving || !type.legContinuousMove) && i % div == l.group){
                    String cipherName16058 =  "DES";
					try{
						android.util.Log.d("cipherName-16058", javax.crypto.Cipher.getInstance(cipherName16058).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(!headless && !inFogTo(player.team())){
                        String cipherName16059 =  "DES";
						try{
							android.util.Log.d("cipherName-16059", javax.crypto.Cipher.getInstance(cipherName16059).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(floor.isLiquid){
                            String cipherName16060 =  "DES";
							try{
								android.util.Log.d("cipherName-16060", javax.crypto.Cipher.getInstance(cipherName16060).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							floor.walkEffect.at(l.base.x, l.base.y, type.rippleScale, floor.mapColor);
                            floor.walkSound.at(x, y, 1f, floor.walkSoundVolume);
                        }else{
                            String cipherName16061 =  "DES";
							try{
								android.util.Log.d("cipherName-16061", javax.crypto.Cipher.getInstance(cipherName16061).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							Fx.unitLandSmall.at(l.base.x, l.base.y, type.rippleScale, floor.mapColor);
                        }

                        //shake when legs contact ground
                        if(type.stepShake > 0){
                            String cipherName16062 =  "DES";
							try{
								android.util.Log.d("cipherName-16062", javax.crypto.Cipher.getInstance(cipherName16062).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							Effect.shake(type.stepShake, type.stepShake, l.base);
                        }
                    }

                    if(type.legSplashDamage > 0){
                        String cipherName16063 =  "DES";
						try{
							android.util.Log.d("cipherName-16063", javax.crypto.Cipher.getInstance(cipherName16063).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Damage.damage(team, l.base.x, l.base.y, type.legSplashRange, type.legSplashDamage * state.rules.unitDamage(team), false, true);
                    }
                }

                l.group = group;
            }

            //leg destination
            Vec2 legDest = Tmp.v1.trns(dstRot, legLength * type.legLengthScl).add(baseOffset).add(moveOffset);
            //join destination
            Vec2 jointDest = Tmp.v2;
            InverseKinematics.solve(legLength/2f, legLength/2f, Tmp.v6.set(l.base).sub(baseOffset), side, jointDest);
            jointDest.add(baseOffset);
            Tmp.v6.set(baseOffset).lerp(l.base, 0.5f);

            if(move){
                String cipherName16064 =  "DES";
				try{
					android.util.Log.d("cipherName-16064", javax.crypto.Cipher.getInstance(cipherName16064).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float moveFract = stageF % 1f;

                l.base.lerpDelta(legDest, moveFract);
                l.joint.lerpDelta(jointDest, moveFract / 2f);
            }

            l.joint.lerpDelta(jointDest, moveSpeed / 4f);

            //limit again after updating
            l.joint.sub(baseOffset).clampLength(type.legMinLength * legLength/2f, type.legMaxLength * legLength/2f).add(baseOffset);
            l.base.sub(baseOffset).clampLength(type.legMinLength * legLength, type.legMaxLength * legLength).add(baseOffset);
        }

        //when at least 1 leg is touching land, it can't drown
        if(deeps != legs.length || !floorOn().isDeep()){
            String cipherName16065 =  "DES";
			try{
				android.util.Log.d("cipherName-16065", javax.crypto.Cipher.getInstance(cipherName16065).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			lastDeepFloor = null;
        }
    }

    Vec2 legOffset(Vec2 out, int index){
        String cipherName16066 =  "DES";
		try{
			android.util.Log.d("cipherName-16066", javax.crypto.Cipher.getInstance(cipherName16066).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		out.trns(defaultLegAngle(index), type.legBaseOffset);

        if(type.legStraightness > 0){
            String cipherName16067 =  "DES";
			try{
				android.util.Log.d("cipherName-16067", javax.crypto.Cipher.getInstance(cipherName16067).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			straightVec.trns(defaultLegAngle(index) - baseRotation, type.legBaseOffset);
            straightVec.y = Mathf.sign(straightVec.y) * type.legBaseOffset * type.legStraightLength;
            straightVec.rotate(baseRotation);
            out.lerp(straightVec, type.baseLegStraightness);
        }

        return out;
    }

    /** @return outwards facing angle of leg at the specified index. */
    float legAngle(int index){
        String cipherName16068 =  "DES";
		try{
			android.util.Log.d("cipherName-16068", javax.crypto.Cipher.getInstance(cipherName16068).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(type.legStraightness > 0){
            String cipherName16069 =  "DES";
			try{
				android.util.Log.d("cipherName-16069", javax.crypto.Cipher.getInstance(cipherName16069).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Mathf.slerp(defaultLegAngle(index), (index >= legs.length/2 ? -90 : 90f) + baseRotation, type.legStraightness);
        }
        return defaultLegAngle(index);
    }

    float defaultLegAngle(int index){
        String cipherName16070 =  "DES";
		try{
			android.util.Log.d("cipherName-16070", javax.crypto.Cipher.getInstance(cipherName16070).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return baseRotation + 360f / legs.length * index + (360f / legs.length / 2f);
    }

}
