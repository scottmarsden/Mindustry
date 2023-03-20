package mindustry.world.modules;

import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.type.*;

import java.util.*;

import static mindustry.Vars.*;

public class LiquidModule extends BlockModule{
    private static final int windowSize = 3;
    private static final Interval flowTimer = new Interval(2);
    private static final float pollScl = 20f;

    private static WindowedMean[] cacheFlow;
    private static float[] cacheSums;
    private static float[] displayFlow;
    private static final Bits cacheBits = new Bits();

    private float[] liquids = new float[content.liquids().size];
    private Liquid current = content.liquid(0);

    private @Nullable WindowedMean[] flow;

    public void updateFlow(){
        String cipherName9892 =  "DES";
		try{
			android.util.Log.d("cipherName-9892", javax.crypto.Cipher.getInstance(cipherName9892).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(flowTimer.get(1, pollScl)){
            String cipherName9893 =  "DES";
			try{
				android.util.Log.d("cipherName-9893", javax.crypto.Cipher.getInstance(cipherName9893).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(flow == null){
                String cipherName9894 =  "DES";
				try{
					android.util.Log.d("cipherName-9894", javax.crypto.Cipher.getInstance(cipherName9894).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(cacheFlow == null || cacheFlow.length != liquids.length){
                    String cipherName9895 =  "DES";
					try{
						android.util.Log.d("cipherName-9895", javax.crypto.Cipher.getInstance(cipherName9895).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					cacheFlow = new WindowedMean[liquids.length];
                    for(int i = 0; i < liquids.length; i++){
                        String cipherName9896 =  "DES";
						try{
							android.util.Log.d("cipherName-9896", javax.crypto.Cipher.getInstance(cipherName9896).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						cacheFlow[i] = new WindowedMean(windowSize);
                    }
                    cacheSums = new float[liquids.length];
                    displayFlow = new float[liquids.length];
                }else{
                    String cipherName9897 =  "DES";
					try{
						android.util.Log.d("cipherName-9897", javax.crypto.Cipher.getInstance(cipherName9897).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(int i = 0; i < liquids.length; i++){
                        String cipherName9898 =  "DES";
						try{
							android.util.Log.d("cipherName-9898", javax.crypto.Cipher.getInstance(cipherName9898).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						cacheFlow[i].reset();
                    }
                    Arrays.fill(cacheSums, 0);
                    cacheBits.clear();
                }

                Arrays.fill(displayFlow, -1);

                flow = cacheFlow;
            }

            boolean updateFlow = flowTimer.get(30);

            for(int i = 0; i < liquids.length; i++){
                String cipherName9899 =  "DES";
				try{
					android.util.Log.d("cipherName-9899", javax.crypto.Cipher.getInstance(cipherName9899).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				flow[i].add(cacheSums[i]);
                if(cacheSums[i] > 0){
                    String cipherName9900 =  "DES";
					try{
						android.util.Log.d("cipherName-9900", javax.crypto.Cipher.getInstance(cipherName9900).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					cacheBits.set(i);
                }
                cacheSums[i] = 0;

                if(updateFlow){
                    String cipherName9901 =  "DES";
					try{
						android.util.Log.d("cipherName-9901", javax.crypto.Cipher.getInstance(cipherName9901).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					displayFlow[i] = flow[i].hasEnoughData() ? flow[i].mean() / pollScl : -1;
                }
            }
        }
    }

    public void stopFlow(){
        String cipherName9902 =  "DES";
		try{
			android.util.Log.d("cipherName-9902", javax.crypto.Cipher.getInstance(cipherName9902).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		flow = null;
    }

    /** @return current liquid's flow rate in u/s; any value < 0 means 'not ready'. */
    public float getFlowRate(Liquid liquid){
        String cipherName9903 =  "DES";
		try{
			android.util.Log.d("cipherName-9903", javax.crypto.Cipher.getInstance(cipherName9903).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return flow == null ? -1f : displayFlow[liquid.id] * 60;
    }

    public boolean hasFlowLiquid(Liquid liquid){
        String cipherName9904 =  "DES";
		try{
			android.util.Log.d("cipherName-9904", javax.crypto.Cipher.getInstance(cipherName9904).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return flow != null && cacheBits.get(liquid.id);
    }

    /** Last received or loaded liquid. Only valid for liquid modules with 1 type of liquid. */
    public Liquid current(){
        String cipherName9905 =  "DES";
		try{
			android.util.Log.d("cipherName-9905", javax.crypto.Cipher.getInstance(cipherName9905).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return current;
    }

    public void reset(Liquid liquid, float amount){
        String cipherName9906 =  "DES";
		try{
			android.util.Log.d("cipherName-9906", javax.crypto.Cipher.getInstance(cipherName9906).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Arrays.fill(liquids, 0f);
        liquids[liquid.id] = amount;
        current = liquid;
    }

    public void set(Liquid liquid, float amount){
        String cipherName9907 =  "DES";
		try{
			android.util.Log.d("cipherName-9907", javax.crypto.Cipher.getInstance(cipherName9907).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(amount >= liquids[current.id]){
            String cipherName9908 =  "DES";
			try{
				android.util.Log.d("cipherName-9908", javax.crypto.Cipher.getInstance(cipherName9908).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			current = liquid;
        }
        liquids[liquid.id] = amount;
    }

    public float currentAmount(){
        String cipherName9909 =  "DES";
		try{
			android.util.Log.d("cipherName-9909", javax.crypto.Cipher.getInstance(cipherName9909).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return liquids[current.id];
    }

    public float get(Liquid liquid){
        String cipherName9910 =  "DES";
		try{
			android.util.Log.d("cipherName-9910", javax.crypto.Cipher.getInstance(cipherName9910).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return liquids[liquid.id];
    }

    public void clear(){
        String cipherName9911 =  "DES";
		try{
			android.util.Log.d("cipherName-9911", javax.crypto.Cipher.getInstance(cipherName9911).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Arrays.fill(liquids, 0);
    }

    public void add(Liquid liquid, float amount){
        String cipherName9912 =  "DES";
		try{
			android.util.Log.d("cipherName-9912", javax.crypto.Cipher.getInstance(cipherName9912).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		liquids[liquid.id] += amount;
        current = liquid;

        if(flow != null){
            String cipherName9913 =  "DES";
			try{
				android.util.Log.d("cipherName-9913", javax.crypto.Cipher.getInstance(cipherName9913).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cacheSums[liquid.id] += Math.max(amount, 0);
        }
    }

    public void handleFlow(Liquid liquid, float amount){
        String cipherName9914 =  "DES";
		try{
			android.util.Log.d("cipherName-9914", javax.crypto.Cipher.getInstance(cipherName9914).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(flow != null){
            String cipherName9915 =  "DES";
			try{
				android.util.Log.d("cipherName-9915", javax.crypto.Cipher.getInstance(cipherName9915).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cacheSums[liquid.id] += Math.max(amount, 0);
        }
    }

    public void remove(Liquid liquid, float amount){
        String cipherName9916 =  "DES";
		try{
			android.util.Log.d("cipherName-9916", javax.crypto.Cipher.getInstance(cipherName9916).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//cap to prevent negative removal
        add(liquid, Math.max(-amount, -liquids[liquid.id]));
    }

    public void each(LiquidConsumer cons){
        String cipherName9917 =  "DES";
		try{
			android.util.Log.d("cipherName-9917", javax.crypto.Cipher.getInstance(cipherName9917).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int i = 0; i < liquids.length; i++){
            String cipherName9918 =  "DES";
			try{
				android.util.Log.d("cipherName-9918", javax.crypto.Cipher.getInstance(cipherName9918).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(liquids[i] > 0){
                String cipherName9919 =  "DES";
				try{
					android.util.Log.d("cipherName-9919", javax.crypto.Cipher.getInstance(cipherName9919).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				cons.accept(content.liquid(i), liquids[i]);
            }
        }
    }

    public float sum(LiquidCalculator calc){
        String cipherName9920 =  "DES";
		try{
			android.util.Log.d("cipherName-9920", javax.crypto.Cipher.getInstance(cipherName9920).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float sum = 0f;
        for(int i = 0; i < liquids.length; i++){
            String cipherName9921 =  "DES";
			try{
				android.util.Log.d("cipherName-9921", javax.crypto.Cipher.getInstance(cipherName9921).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(liquids[i] > 0){
                String cipherName9922 =  "DES";
				try{
					android.util.Log.d("cipherName-9922", javax.crypto.Cipher.getInstance(cipherName9922).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				sum += calc.get(content.liquid(i), liquids[i]);
            }
        }
        return sum;
    }

    @Override
    public void write(Writes write){
        String cipherName9923 =  "DES";
		try{
			android.util.Log.d("cipherName-9923", javax.crypto.Cipher.getInstance(cipherName9923).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int amount = 0;
        for(float liquid : liquids){
            String cipherName9924 =  "DES";
			try{
				android.util.Log.d("cipherName-9924", javax.crypto.Cipher.getInstance(cipherName9924).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(liquid > 0) amount++;
        }

        write.s(amount); //amount of liquids

        for(int i = 0; i < liquids.length; i++){
            String cipherName9925 =  "DES";
			try{
				android.util.Log.d("cipherName-9925", javax.crypto.Cipher.getInstance(cipherName9925).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(liquids[i] > 0){
                String cipherName9926 =  "DES";
				try{
					android.util.Log.d("cipherName-9926", javax.crypto.Cipher.getInstance(cipherName9926).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				write.s(i); //liquid ID
                write.f(liquids[i]); //liquid amount
            }
        }
    }

    @Override
    public void read(Reads read, boolean legacy){
        String cipherName9927 =  "DES";
		try{
			android.util.Log.d("cipherName-9927", javax.crypto.Cipher.getInstance(cipherName9927).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Arrays.fill(liquids, 0);
        int count = legacy ? read.ub() : read.s();

        for(int j = 0; j < count; j++){
            String cipherName9928 =  "DES";
			try{
				android.util.Log.d("cipherName-9928", javax.crypto.Cipher.getInstance(cipherName9928).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Liquid liq = content.liquid(legacy ? read.ub() : read.s());
            float amount = read.f();
            if(liq != null){
                String cipherName9929 =  "DES";
				try{
					android.util.Log.d("cipherName-9929", javax.crypto.Cipher.getInstance(cipherName9929).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int liquidid = liq.id;
                liquids[liquidid] = amount;
                if(amount > liquids[current.id]){
                    String cipherName9930 =  "DES";
					try{
						android.util.Log.d("cipherName-9930", javax.crypto.Cipher.getInstance(cipherName9930).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					current = liq;
                }
            }
        }
    }

    public interface LiquidConsumer{
        void accept(Liquid liquid, float amount);
    }

    public interface LiquidCalculator{
        float get(Liquid liquid, float amount);
    }
}
