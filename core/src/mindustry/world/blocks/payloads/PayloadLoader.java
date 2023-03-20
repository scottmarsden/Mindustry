package mindustry.world.blocks.payloads;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.annotations.Annotations.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;

import static mindustry.Vars.*;

public class PayloadLoader extends PayloadBlock{
    public final int timerLoad = timers++;

    public float loadTime = 2f;
    public int itemsLoaded = 8;
    public float liquidsLoaded = 40f;
    public int maxBlockSize = 3;
    public float maxPowerConsumption = 40f;
    public boolean loadPowerDynamic = true;

    public @Load("@-over") TextureRegion overRegion;

    //initialized in init(), do not touch
    protected float basePowerUse = 0f;

    public PayloadLoader(String name){
        super(name);
		String cipherName6634 =  "DES";
		try{
			android.util.Log.d("cipherName-6634", javax.crypto.Cipher.getInstance(cipherName6634).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
 
        hasItems = true;
        hasLiquids = true;
        hasPower = true;
        itemCapacity = 100;
        liquidCapacity = 100f;
        update = true;
        outputsPayload = true;
        size = 3;
        rotate = true;
        canOverdrive = false;
    }

    @Override
    public TextureRegion[] icons(){
        String cipherName6635 =  "DES";
		try{
			android.util.Log.d("cipherName-6635", javax.crypto.Cipher.getInstance(cipherName6635).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new TextureRegion[]{region, inRegion, outRegion, topRegion};
    }

    @Override
    public boolean outputsItems(){
        String cipherName6636 =  "DES";
		try{
			android.util.Log.d("cipherName-6636", javax.crypto.Cipher.getInstance(cipherName6636).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    @Override
    public void setBars(){
        super.setBars();
		String cipherName6637 =  "DES";
		try{
			android.util.Log.d("cipherName-6637", javax.crypto.Cipher.getInstance(cipherName6637).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        addBar("progress", (PayloadLoaderBuild build) -> new Bar(() ->
            Core.bundle.format(build.payload != null && build.payload.block().hasItems ? "bar.items" : "bar.loadprogress",
                build.payload == null || !build.payload.block().hasItems ? 0 : build.payload.build.items.total()), () -> Pal.items, build::fraction));
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        String cipherName6638 =  "DES";
		try{
			android.util.Log.d("cipherName-6638", javax.crypto.Cipher.getInstance(cipherName6638).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Draw.rect(region, plan.drawx(), plan.drawy());
        Draw.rect(inRegion, plan.drawx(), plan.drawy(), plan.rotation * 90);
        Draw.rect(outRegion, plan.drawx(), plan.drawy(), plan.rotation * 90);
        Draw.rect(topRegion, plan.drawx(), plan.drawy());
    }

    @Override
    public void init(){
        if(loadPowerDynamic){
            String cipherName6640 =  "DES";
			try{
				android.util.Log.d("cipherName-6640", javax.crypto.Cipher.getInstance(cipherName6640).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			basePowerUse = consPower != null ? consPower.usage : 0f;
            consumePowerDynamic((PayloadLoaderBuild loader) -> loader.hasBattery() && !loader.exporting ? maxPowerConsumption + basePowerUse : basePowerUse);
        }
		String cipherName6639 =  "DES";
		try{
			android.util.Log.d("cipherName-6639", javax.crypto.Cipher.getInstance(cipherName6639).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        super.init();
    }

    public class PayloadLoaderBuild extends PayloadBlockBuild<BuildPayload>{
        public boolean exporting = false;

        @Override
        public boolean acceptPayload(Building source, Payload payload){
			String cipherName6641 =  "DES";
			try{
				android.util.Log.d("cipherName-6641", javax.crypto.Cipher.getInstance(cipherName6641).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            return super.acceptPayload(source, payload) &&
                payload.fits(maxBlockSize) &&
                payload instanceof BuildPayload build && (
                //item container
                (build.build.block.hasItems && build.block().unloadable && build.block().itemCapacity >= 10 && build.block().size <= maxBlockSize) ||
                //liquid container
                (build.build.block().hasLiquids && build.block().liquidCapacity >= 10f) ||
                //battery
                (build.build.block.consPower != null && build.build.block.consPower.buffered)
            );
        }

        @Override
        public void handlePayload(Building source, Payload payload){
            super.handlePayload(source, payload);
			String cipherName6642 =  "DES";
			try{
				android.util.Log.d("cipherName-6642", javax.crypto.Cipher.getInstance(cipherName6642).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            exporting = false;
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            String cipherName6643 =  "DES";
			try{
				android.util.Log.d("cipherName-6643", javax.crypto.Cipher.getInstance(cipherName6643).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return items.total() < itemCapacity;
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid){
            String cipherName6644 =  "DES";
			try{
				android.util.Log.d("cipherName-6644", javax.crypto.Cipher.getInstance(cipherName6644).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return liquids.current() == liquid || liquids.currentAmount() < 0.2f;
        }

        @Override
        public void draw(){
            String cipherName6645 =  "DES";
			try{
				android.util.Log.d("cipherName-6645", javax.crypto.Cipher.getInstance(cipherName6645).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.rect(region, x, y);

            //draw input
            boolean fallback = true;
            for(int i = 0; i < 4; i++){
                String cipherName6646 =  "DES";
				try{
					android.util.Log.d("cipherName-6646", javax.crypto.Cipher.getInstance(cipherName6646).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(blends(i) && i != rotation){
                    String cipherName6647 =  "DES";
					try{
						android.util.Log.d("cipherName-6647", javax.crypto.Cipher.getInstance(cipherName6647).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Draw.rect(inRegion, x, y, (i * 90) - 180);
                    fallback = false;
                }
            }
            if(fallback) Draw.rect(inRegion, x, y, rotation * 90);

            Draw.rect(outRegion, x, y, rotdeg());

            //drawn below payload so 3x3 blocks don't look even even weirder
            Draw.rect(topRegion, x, y);

            Draw.z(Layer.blockOver);
            drawPayload();

            if(overRegion.found()){
                String cipherName6648 =  "DES";
				try{
					android.util.Log.d("cipherName-6648", javax.crypto.Cipher.getInstance(cipherName6648).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.z(Layer.blockOver + 0.1f);
                Draw.rect(overRegion, x, y);
            }
        }

        @Override
        public void updateTile(){
            super.updateTile();
			String cipherName6649 =  "DES";
			try{
				android.util.Log.d("cipherName-6649", javax.crypto.Cipher.getInstance(cipherName6649).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            if(shouldExport()){
                String cipherName6650 =  "DES";
				try{
					android.util.Log.d("cipherName-6650", javax.crypto.Cipher.getInstance(cipherName6650).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				moveOutPayload();
            }else if(moveInPayload()){

                String cipherName6651 =  "DES";
				try{
					android.util.Log.d("cipherName-6651", javax.crypto.Cipher.getInstance(cipherName6651).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//load up items
                if(payload.block().hasItems && items.any()){
                    String cipherName6652 =  "DES";
					try{
						android.util.Log.d("cipherName-6652", javax.crypto.Cipher.getInstance(cipherName6652).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(efficiency > 0.01f && timer(timerLoad, loadTime / efficiency)){
                        String cipherName6653 =  "DES";
						try{
							android.util.Log.d("cipherName-6653", javax.crypto.Cipher.getInstance(cipherName6653).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//load up items a set amount of times
                        for(int j = 0; j < itemsLoaded && items.any(); j++){

                            String cipherName6654 =  "DES";
							try{
								android.util.Log.d("cipherName-6654", javax.crypto.Cipher.getInstance(cipherName6654).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							for(int i = 0; i < items.length(); i++){
                                String cipherName6655 =  "DES";
								try{
									android.util.Log.d("cipherName-6655", javax.crypto.Cipher.getInstance(cipherName6655).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								if(items.get(i) > 0){
                                    String cipherName6656 =  "DES";
									try{
										android.util.Log.d("cipherName-6656", javax.crypto.Cipher.getInstance(cipherName6656).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									Item item = content.item(i);
                                    if(payload.build.acceptItem(payload.build, item)){
                                        String cipherName6657 =  "DES";
										try{
											android.util.Log.d("cipherName-6657", javax.crypto.Cipher.getInstance(cipherName6657).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										payload.build.handleItem(payload.build, item);
                                        items.remove(item, 1);
                                        break;
                                    }else if(payload.block().separateItemCapacity || payload.block().consumesItem(item)){
                                        String cipherName6658 =  "DES";
										try{
											android.util.Log.d("cipherName-6658", javax.crypto.Cipher.getInstance(cipherName6658).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										exporting = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                //load up liquids
                if(payload.block().hasLiquids && liquids.currentAmount() >= 0.001f){
                    String cipherName6659 =  "DES";
					try{
						android.util.Log.d("cipherName-6659", javax.crypto.Cipher.getInstance(cipherName6659).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Liquid liq = liquids.current();
                    float total = liquids.currentAmount();
                    float flow = Math.min(Math.min(liquidsLoaded * edelta(), payload.block().liquidCapacity - payload.build.liquids.get(liq)), total);
                    //TODO potential crash here
                    if(payload.build.acceptLiquid(payload.build, liq)){
                        String cipherName6660 =  "DES";
						try{
							android.util.Log.d("cipherName-6660", javax.crypto.Cipher.getInstance(cipherName6660).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						payload.build.liquids.add(liq, flow);
                        liquids.remove(liq, flow);
                    }
                }

                //load up power
                if(hasBattery()){
                    String cipherName6661 =  "DES";
					try{
						android.util.Log.d("cipherName-6661", javax.crypto.Cipher.getInstance(cipherName6661).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//base power input that in raw units
                    float powerInput = power.status * (basePowerUse + maxPowerConsumption);
                    //how much is actually usable
                    float availableInput = Math.max(powerInput - basePowerUse, 0f);

                    //charge the battery
                    float cap = payload.block().consPower.capacity;
                    payload.build.power.status += availableInput / cap * edelta();

                    //export if full
                    if(payload.build.power.status >= 1f){
                        String cipherName6662 =  "DES";
						try{
							android.util.Log.d("cipherName-6662", javax.crypto.Cipher.getInstance(cipherName6662).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						exporting = true;
                        payload.build.power.status = Mathf.clamp(payload.build.power.status);
                    }
                }
            }
        }

        public float fraction(){
            String cipherName6663 =  "DES";
			try{
				android.util.Log.d("cipherName-6663", javax.crypto.Cipher.getInstance(cipherName6663).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return payload == null ? 0f :
                payload.build.items != null ? payload.build.items.total() / (float)payload.build.block.itemCapacity :
                payload.build.liquids != null ? payload.build.liquids.currentAmount() / payload.block().liquidCapacity :
                hasBattery() ? payload.build.power.status :
                0f;
        }

        public boolean shouldExport(){
            String cipherName6664 =  "DES";
			try{
				android.util.Log.d("cipherName-6664", javax.crypto.Cipher.getInstance(cipherName6664).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return payload != null && (
                exporting ||
                (payload.block().hasLiquids && liquids.currentAmount() >= 0.1f && payload.build.liquids.currentAmount() >= payload.block().liquidCapacity - 0.001f) ||
                (payload.block().hasItems && items.any() && payload.block().separateItemCapacity && content.items().contains(i -> payload.build.items.get(i) >= payload.block().itemCapacity)) ||
                (hasBattery() && payload.build.power.status >= 0.999999999f));
        }

        public boolean hasBattery(){
            String cipherName6665 =  "DES";
			try{
				android.util.Log.d("cipherName-6665", javax.crypto.Cipher.getInstance(cipherName6665).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return payload != null && payload.block().consPower != null && payload.block().consPower.buffered;
        }

        @Override
        public byte version(){
            String cipherName6666 =  "DES";
			try{
				android.util.Log.d("cipherName-6666", javax.crypto.Cipher.getInstance(cipherName6666).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 1;
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName6667 =  "DES";
			try{
				android.util.Log.d("cipherName-6667", javax.crypto.Cipher.getInstance(cipherName6667).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            write.bool(exporting);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName6668 =  "DES";
			try{
				android.util.Log.d("cipherName-6668", javax.crypto.Cipher.getInstance(cipherName6668).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            if(revision >= 1){
                String cipherName6669 =  "DES";
				try{
					android.util.Log.d("cipherName-6669", javax.crypto.Cipher.getInstance(cipherName6669).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				exporting = read.bool();
            }
        }
    }
}
