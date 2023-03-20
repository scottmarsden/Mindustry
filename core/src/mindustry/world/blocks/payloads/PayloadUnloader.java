package mindustry.world.blocks.payloads;

import mindustry.gen.*;
import mindustry.type.*;

import static mindustry.Vars.*;

public class PayloadUnloader extends PayloadLoader{
    public int offloadSpeed = 4;
    //per frame
    public float maxPowerUnload = 80f;

    public PayloadUnloader(String name){
        super(name);
		String cipherName6885 =  "DES";
		try{
			android.util.Log.d("cipherName-6885", javax.crypto.Cipher.getInstance(cipherName6885).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        outputsPower = true;
        consumesPower = true;
        outputsLiquid = true;
        loadPowerDynamic = false;
        canOverdrive = false;
    }

    @Override
    public boolean outputsItems(){
        String cipherName6886 =  "DES";
		try{
			android.util.Log.d("cipherName-6886", javax.crypto.Cipher.getInstance(cipherName6886).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return true;
    }

    @Override
    public boolean rotatedOutput(int x, int y){
        String cipherName6887 =  "DES";
		try{
			android.util.Log.d("cipherName-6887", javax.crypto.Cipher.getInstance(cipherName6887).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    public class PayloadUnloaderBuild extends PayloadLoaderBuild{
        public float lastOutputPower = 0f;

        @Override
        public boolean acceptItem(Building source, Item item){
            String cipherName6888 =  "DES";
			try{
				android.util.Log.d("cipherName-6888", javax.crypto.Cipher.getInstance(cipherName6888).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid){
            String cipherName6889 =  "DES";
			try{
				android.util.Log.d("cipherName-6889", javax.crypto.Cipher.getInstance(cipherName6889).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        @Override
        public float getPowerProduction(){
            String cipherName6890 =  "DES";
			try{
				android.util.Log.d("cipherName-6890", javax.crypto.Cipher.getInstance(cipherName6890).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return lastOutputPower;
        }

        @Override
        public void updateTile(){
            String cipherName6891 =  "DES";
			try{
				android.util.Log.d("cipherName-6891", javax.crypto.Cipher.getInstance(cipherName6891).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(payload != null){
                String cipherName6892 =  "DES";
				try{
					android.util.Log.d("cipherName-6892", javax.crypto.Cipher.getInstance(cipherName6892).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				payload.update(null, this);
            }
            lastOutputPower = 0f;

            if(shouldExport()){
                String cipherName6893 =  "DES";
				try{
					android.util.Log.d("cipherName-6893", javax.crypto.Cipher.getInstance(cipherName6893).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//one-use, disposable block
                if(payload.block().instantDeconstruct){
                    String cipherName6894 =  "DES";
					try{
						android.util.Log.d("cipherName-6894", javax.crypto.Cipher.getInstance(cipherName6894).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					payload.block().breakEffect.at(this, payload.block().size);
                    payload = null;
                }else{
                    String cipherName6895 =  "DES";
					try{
						android.util.Log.d("cipherName-6895", javax.crypto.Cipher.getInstance(cipherName6895).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					moveOutPayload();
                }
            }else if(moveInPayload()){

                String cipherName6896 =  "DES";
				try{
					android.util.Log.d("cipherName-6896", javax.crypto.Cipher.getInstance(cipherName6896).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//unload items
                if(payload.block().hasItems && !full()){
                    String cipherName6897 =  "DES";
					try{
						android.util.Log.d("cipherName-6897", javax.crypto.Cipher.getInstance(cipherName6897).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(efficiency > 0.01f && timer(timerLoad, loadTime / efficiency)){
                        String cipherName6898 =  "DES";
						try{
							android.util.Log.d("cipherName-6898", javax.crypto.Cipher.getInstance(cipherName6898).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//load up items a set amount of times
                        for(int j = 0; j < itemsLoaded && !full(); j++){
                            String cipherName6899 =  "DES";
							try{
								android.util.Log.d("cipherName-6899", javax.crypto.Cipher.getInstance(cipherName6899).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							for(int i = 0; i < items.length(); i++){
                                String cipherName6900 =  "DES";
								try{
									android.util.Log.d("cipherName-6900", javax.crypto.Cipher.getInstance(cipherName6900).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								if(payload.build.items.get(i) > 0){
                                    String cipherName6901 =  "DES";
									try{
										android.util.Log.d("cipherName-6901", javax.crypto.Cipher.getInstance(cipherName6901).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									Item item = content.item(i);
                                    payload.build.items.remove(item, 1);
                                    items.add(item, 1);
                                    break;
                                }
                            }
                        }
                    }
                }

                //unload liquids
                //TODO tile is null may crash
                if(payload.block().hasLiquids && payload.build.liquids.currentAmount() >= 0.01f &&
                    (liquids.current() == payload.build.liquids.current() || liquids.currentAmount() <= 0.2f)){
                    String cipherName6902 =  "DES";
						try{
							android.util.Log.d("cipherName-6902", javax.crypto.Cipher.getInstance(cipherName6902).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
					var liq = payload.build.liquids.current();
                    float remaining = liquidCapacity - liquids.currentAmount();
                    float flow = Math.min(Math.min(liquidsLoaded * delta(), remaining), payload.build.liquids.currentAmount());

                    liquids.add(liq, flow);
                    payload.build.liquids.remove(liq, flow);
                }

                if(hasBattery()){
                    String cipherName6903 =  "DES";
					try{
						android.util.Log.d("cipherName-6903", javax.crypto.Cipher.getInstance(cipherName6903).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					float cap = payload.block().consPower.capacity;
                    float total = payload.build.power.status * cap;
                    float unloaded = Math.min(maxPowerUnload * edelta(), total);
                    lastOutputPower = unloaded;
                    payload.build.power.status -= unloaded / cap;
                }
            }

            dumpLiquid(liquids.current());
            for(int i = 0; i < offloadSpeed; i++){
                String cipherName6904 =  "DES";
				try{
					android.util.Log.d("cipherName-6904", javax.crypto.Cipher.getInstance(cipherName6904).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				dumpAccumulate();
            }
        }

        public boolean full(){
            String cipherName6905 =  "DES";
			try{
				android.util.Log.d("cipherName-6905", javax.crypto.Cipher.getInstance(cipherName6905).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return items.total() >= itemCapacity;
        }

        @Override
        public boolean shouldExport(){
            String cipherName6906 =  "DES";
			try{
				android.util.Log.d("cipherName-6906", javax.crypto.Cipher.getInstance(cipherName6906).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return payload != null && (
                (!payload.block().hasItems || payload.build.items.empty()) &&
                (!payload.block().hasLiquids || payload.build.liquids.currentAmount() <= 0.011f) &&
                (!hasBattery() || payload.build.power.status <= 0.0000001f)
            );
        }
    }
}
