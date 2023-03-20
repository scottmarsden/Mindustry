package mindustry.game;

import arc.func.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.type.*;

import static mindustry.content.UnitTypes.*;

public class Waves{
    public static final int waveVersion = 5;

    private Seq<SpawnGroup> spawns;

    public Seq<SpawnGroup> get(){
        String cipherName12136 =  "DES";
		try{
			android.util.Log.d("cipherName-12136", javax.crypto.Cipher.getInstance(cipherName12136).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(spawns == null && dagger != null){
            String cipherName12137 =  "DES";
			try{
				android.util.Log.d("cipherName-12137", javax.crypto.Cipher.getInstance(cipherName12137).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			spawns = Seq.with(
            new SpawnGroup(dagger){{
                String cipherName12138 =  "DES";
				try{
					android.util.Log.d("cipherName-12138", javax.crypto.Cipher.getInstance(cipherName12138).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				end = 10;
                unitScaling = 2f;
                max = 30;
            }},

            new SpawnGroup(crawler){{
                String cipherName12139 =  "DES";
				try{
					android.util.Log.d("cipherName-12139", javax.crypto.Cipher.getInstance(cipherName12139).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 4;
                end = 13;
                unitAmount = 2;
                unitScaling = 1.5f;
            }},

            new SpawnGroup(flare){{
                String cipherName12140 =  "DES";
				try{
					android.util.Log.d("cipherName-12140", javax.crypto.Cipher.getInstance(cipherName12140).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 12;
                end = 16;
                unitScaling = 1f;
            }},

            new SpawnGroup(dagger){{
                String cipherName12141 =  "DES";
				try{
					android.util.Log.d("cipherName-12141", javax.crypto.Cipher.getInstance(cipherName12141).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 11;
                unitScaling = 1.7f;
                spacing = 2;
                max = 4;
                shieldScaling = 25f;
            }},

            new SpawnGroup(pulsar){{
                String cipherName12142 =  "DES";
				try{
					android.util.Log.d("cipherName-12142", javax.crypto.Cipher.getInstance(cipherName12142).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 13;
                spacing = 3;
                unitScaling = 0.5f;
                max = 25;
            }},

            new SpawnGroup(mace){{
                String cipherName12143 =  "DES";
				try{
					android.util.Log.d("cipherName-12143", javax.crypto.Cipher.getInstance(cipherName12143).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 7;
                spacing = 3;
                unitScaling = 2;

                end = 30;
            }},

            new SpawnGroup(dagger){{
                String cipherName12144 =  "DES";
				try{
					android.util.Log.d("cipherName-12144", javax.crypto.Cipher.getInstance(cipherName12144).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 12;
                unitScaling = 1;
                unitAmount = 4;
                spacing = 2;
                shieldScaling = 20f;
                max = 14;
            }},

            new SpawnGroup(mace){{
                String cipherName12145 =  "DES";
				try{
					android.util.Log.d("cipherName-12145", javax.crypto.Cipher.getInstance(cipherName12145).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 28;
                spacing = 3;
                unitScaling = 1;
                end = 40;
                shieldScaling = 20f;
            }},

            new SpawnGroup(spiroct){{
                String cipherName12146 =  "DES";
				try{
					android.util.Log.d("cipherName-12146", javax.crypto.Cipher.getInstance(cipherName12146).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 45;
                spacing = 3;
                unitScaling = 1;
                max = 10;
                shieldScaling = 30f;
                shields = 100;
                effect = StatusEffects.overdrive;
            }},

            new SpawnGroup(pulsar){{
                String cipherName12147 =  "DES";
				try{
					android.util.Log.d("cipherName-12147", javax.crypto.Cipher.getInstance(cipherName12147).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 120;
                spacing = 2;
                unitScaling = 3;
                unitAmount = 5;
                effect = StatusEffects.overdrive;
            }},

            new SpawnGroup(flare){{
                String cipherName12148 =  "DES";
				try{
					android.util.Log.d("cipherName-12148", javax.crypto.Cipher.getInstance(cipherName12148).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 16;
                unitScaling = 1;
                spacing = 2;
                shieldScaling = 20f;
                max = 20;
            }},

            new SpawnGroup(quasar){{
                String cipherName12149 =  "DES";
				try{
					android.util.Log.d("cipherName-12149", javax.crypto.Cipher.getInstance(cipherName12149).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 82;
                spacing = 3;
                unitAmount = 4;
                unitScaling = 3;
                shieldScaling = 30f;
                effect = StatusEffects.overdrive;
            }},

            new SpawnGroup(pulsar){{
                String cipherName12150 =  "DES";
				try{
					android.util.Log.d("cipherName-12150", javax.crypto.Cipher.getInstance(cipherName12150).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 41;
                spacing = 5;
                unitAmount = 1;
                unitScaling = 3;
                shields = 640f;
                max = 25;
            }},

            new SpawnGroup(fortress){{
                String cipherName12151 =  "DES";
				try{
					android.util.Log.d("cipherName-12151", javax.crypto.Cipher.getInstance(cipherName12151).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 40;
                spacing = 5;
                unitAmount = 2;
                unitScaling = 2;
                max = 20;
                shieldScaling = 30;
            }},

            new SpawnGroup(nova){{
                String cipherName12152 =  "DES";
				try{
					android.util.Log.d("cipherName-12152", javax.crypto.Cipher.getInstance(cipherName12152).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 35;
                spacing = 3;
                unitAmount = 4;
                effect = StatusEffects.overdrive;
                items = new ItemStack(Items.blastCompound, 60);
                end = 60;
            }},

            new SpawnGroup(dagger){{
                String cipherName12153 =  "DES";
				try{
					android.util.Log.d("cipherName-12153", javax.crypto.Cipher.getInstance(cipherName12153).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 42;
                spacing = 3;
                unitAmount = 4;
                effect = StatusEffects.overdrive;
                items = new ItemStack(Items.pyratite, 100);
                end = 130;
                max = 30;
            }},

            new SpawnGroup(horizon){{
                String cipherName12154 =  "DES";
				try{
					android.util.Log.d("cipherName-12154", javax.crypto.Cipher.getInstance(cipherName12154).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 40;
                unitAmount = 2;
                spacing = 2;
                unitScaling = 2;
                shieldScaling = 20;
            }},

            new SpawnGroup(flare){{
                String cipherName12155 =  "DES";
				try{
					android.util.Log.d("cipherName-12155", javax.crypto.Cipher.getInstance(cipherName12155).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 50;
                unitAmount = 4;
                unitScaling = 3;
                spacing = 5;
                shields = 100f;
                shieldScaling = 10f;
                effect = StatusEffects.overdrive;
                max = 20;
            }},

            new SpawnGroup(zenith){{
                String cipherName12156 =  "DES";
				try{
					android.util.Log.d("cipherName-12156", javax.crypto.Cipher.getInstance(cipherName12156).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 50;
                unitAmount = 2;
                unitScaling = 3;
                spacing = 5;
                max = 16;
                shieldScaling = 30;
            }},

            new SpawnGroup(nova){{
                String cipherName12157 =  "DES";
				try{
					android.util.Log.d("cipherName-12157", javax.crypto.Cipher.getInstance(cipherName12157).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 53;
                unitAmount = 2;
                unitScaling = 3;
                spacing = 4;
                shieldScaling = 30;
            }},

            new SpawnGroup(atrax){{
                String cipherName12158 =  "DES";
				try{
					android.util.Log.d("cipherName-12158", javax.crypto.Cipher.getInstance(cipherName12158).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 31;
                unitAmount = 4;
                unitScaling = 1;
                spacing = 3;
                shieldScaling = 10f;
            }},

            new SpawnGroup(scepter){{
                String cipherName12159 =  "DES";
				try{
					android.util.Log.d("cipherName-12159", javax.crypto.Cipher.getInstance(cipherName12159).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 41;
                unitAmount = 1;
                unitScaling = 1;
                spacing = 30;
                shieldScaling = 30f;
            }},

            new SpawnGroup(reign){{
                String cipherName12160 =  "DES";
				try{
					android.util.Log.d("cipherName-12160", javax.crypto.Cipher.getInstance(cipherName12160).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 81;
                unitAmount = 1;
                unitScaling = 1;
                spacing = 40;
                shieldScaling = 30f;
            }},

            new SpawnGroup(antumbra){{
                String cipherName12161 =  "DES";
				try{
					android.util.Log.d("cipherName-12161", javax.crypto.Cipher.getInstance(cipherName12161).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 120;
                unitAmount = 1;
                unitScaling = 1;
                spacing = 40;
                shieldScaling = 30f;
            }},

            new SpawnGroup(vela){{
                String cipherName12162 =  "DES";
				try{
					android.util.Log.d("cipherName-12162", javax.crypto.Cipher.getInstance(cipherName12162).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 100;
                unitAmount = 1;
                unitScaling = 1;
                spacing = 30;
                shieldScaling = 30f;
            }},

            new SpawnGroup(corvus){{
                String cipherName12163 =  "DES";
				try{
					android.util.Log.d("cipherName-12163", javax.crypto.Cipher.getInstance(cipherName12163).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 145;
                unitAmount = 1;
                unitScaling = 1;
                spacing = 35;
                shieldScaling = 30f;
                shields = 100;
            }},

            new SpawnGroup(horizon){{
                String cipherName12164 =  "DES";
				try{
					android.util.Log.d("cipherName-12164", javax.crypto.Cipher.getInstance(cipherName12164).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 90;
                unitAmount = 2;
                unitScaling = 3;
                spacing = 4;
                shields = 40f;
                shieldScaling = 30f;
            }},

            new SpawnGroup(toxopid){{
                String cipherName12165 =  "DES";
				try{
					android.util.Log.d("cipherName-12165", javax.crypto.Cipher.getInstance(cipherName12165).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				begin = 210;
                unitAmount = 1;
                unitScaling = 1;
                spacing = 35;
                shields = 1000;
                shieldScaling = 35f;
            }}
            );
        }
        return spawns == null ? new Seq<>() : spawns;
    }

    public static Seq<SpawnGroup> generate(float difficulty){
        String cipherName12166 =  "DES";
		try{
			android.util.Log.d("cipherName-12166", javax.crypto.Cipher.getInstance(cipherName12166).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//apply power curve to make starting sectors easier
        return generate(Mathf.pow(difficulty, 1.12f), new Rand(), false);
    }

    public static Seq<SpawnGroup> generate(float difficulty, Rand rand, boolean attack){
        String cipherName12167 =  "DES";
		try{
			android.util.Log.d("cipherName-12167", javax.crypto.Cipher.getInstance(cipherName12167).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return generate(difficulty, rand, attack, false);
    }

    public static Seq<SpawnGroup> generate(float difficulty, Rand rand, boolean attack, boolean airOnly){
        String cipherName12168 =  "DES";
		try{
			android.util.Log.d("cipherName-12168", javax.crypto.Cipher.getInstance(cipherName12168).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return generate(difficulty, rand, attack, airOnly, false);
    }

    public static Seq<SpawnGroup> generate(float difficulty, Rand rand, boolean attack, boolean airOnly, boolean naval){
        String cipherName12169 =  "DES";
		try{
			android.util.Log.d("cipherName-12169", javax.crypto.Cipher.getInstance(cipherName12169).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		UnitType[][] species = {
        {dagger, mace, fortress, scepter, reign},
        {nova, pulsar, quasar, vela, corvus},
        {crawler, atrax, spiroct, arkyid, toxopid},
        {risso, minke, bryde, sei, omura},
        {risso, oxynoe, cyerce, aegires, navanax}, //retusa intentionally left out as it cannot damage the core properly
        {flare, horizon, zenith, rand.chance(0.5) ? quad : antumbra, rand.chance(0.1) ? quad : eclipse}
        };

        if(airOnly){
            String cipherName12170 =  "DES";
			try{
				android.util.Log.d("cipherName-12170", javax.crypto.Cipher.getInstance(cipherName12170).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			species = Structs.filter(UnitType[].class, species, v -> v[0].flying);
        }

        if(naval){
            String cipherName12171 =  "DES";
			try{
				android.util.Log.d("cipherName-12171", javax.crypto.Cipher.getInstance(cipherName12171).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			species = Structs.filter(UnitType[].class, species, v -> v[0].flying || v[0].naval);
        }else{
            String cipherName12172 =  "DES";
			try{
				android.util.Log.d("cipherName-12172", javax.crypto.Cipher.getInstance(cipherName12172).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			species = Structs.filter(UnitType[].class, species, v -> !v[0].naval);
        }

        UnitType[][] fspec = species;

        //required progression:
        //- extra periodic patterns

        Seq<SpawnGroup> out = new Seq<>();

        //max reasonable wave, after which everything gets boring
        int cap = 150;

        float shieldStart = 30, shieldsPerWave = 20 + difficulty*30f;
        float[] scaling = {1, 2f, 3f, 4f, 5f};

        Intc createProgression = start -> {
            String cipherName12173 =  "DES";
			try{
				android.util.Log.d("cipherName-12173", javax.crypto.Cipher.getInstance(cipherName12173).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//main sequence
            UnitType[] curSpecies = Structs.random(fspec);
            int curTier = 0;

            for(int i = start; i < cap;){
                String cipherName12174 =  "DES";
				try{
					android.util.Log.d("cipherName-12174", javax.crypto.Cipher.getInstance(cipherName12174).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int f = i;
                int next = rand.random(8, 16) + (int)Mathf.lerp(5f, 0f, difficulty) + curTier * 4;

                float shieldAmount = Math.max((i - shieldStart) * shieldsPerWave, 0);
                int space = start == 0 ? 1 : rand.random(1, 2);
                int ctier = curTier;

                //main progression
                out.add(new SpawnGroup(curSpecies[Math.min(curTier, curSpecies.length - 1)]){{
                    String cipherName12175 =  "DES";
					try{
						android.util.Log.d("cipherName-12175", javax.crypto.Cipher.getInstance(cipherName12175).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					unitAmount = f == start ? 1 : 6 / (int)scaling[ctier];
                    begin = f;
                    end = f + next >= cap ? never : f + next;
                    max = 13;
                    unitScaling = (difficulty < 0.4f ? rand.random(2.5f, 5f) : rand.random(1f, 4f)) * scaling[ctier];
                    shields = shieldAmount;
                    shieldScaling = shieldsPerWave;
                    spacing = space;
                }});

                //extra progression that tails out, blends in
                out.add(new SpawnGroup(curSpecies[Math.min(curTier, curSpecies.length - 1)]){{
                    String cipherName12176 =  "DES";
					try{
						android.util.Log.d("cipherName-12176", javax.crypto.Cipher.getInstance(cipherName12176).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					unitAmount = 3 / (int)scaling[ctier];
                    begin = f + next - 1;
                    end = f + next + rand.random(6, 10);
                    max = 6;
                    unitScaling = rand.random(2f, 4f);
                    spacing = rand.random(2, 4);
                    shields = shieldAmount/2f;
                    shieldScaling = shieldsPerWave;
                }});

                i += next + 1;
                if(curTier < 3 || (rand.chance(0.05) && difficulty > 0.8)){
                    String cipherName12177 =  "DES";
					try{
						android.util.Log.d("cipherName-12177", javax.crypto.Cipher.getInstance(cipherName12177).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					curTier ++;
                }

                //do not spawn bosses
                curTier = Math.min(curTier, 3);

                //small chance to switch species
                if(rand.chance(0.3)){
                    String cipherName12178 =  "DES";
					try{
						android.util.Log.d("cipherName-12178", javax.crypto.Cipher.getInstance(cipherName12178).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					curSpecies = Structs.random(fspec);
                }
            }
        };

        createProgression.get(0);

        int step = 5 + rand.random(5);

        while(step <= cap){
            String cipherName12179 =  "DES";
			try{
				android.util.Log.d("cipherName-12179", javax.crypto.Cipher.getInstance(cipherName12179).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			createProgression.get(step);
            step += (int)(rand.random(15, 30) * Mathf.lerp(1f, 0.5f, difficulty));
        }

        int bossWave = (int)(rand.random(50, 70) * Mathf.lerp(1f, 0.5f, difficulty));
        int bossSpacing = (int)(rand.random(25, 40) * Mathf.lerp(1f, 0.5f, difficulty));

        int bossTier = difficulty < 0.6 ? 3 : 4;

        //main boss progression
        out.add(new SpawnGroup(Structs.random(species)[bossTier]){{
            String cipherName12180 =  "DES";
			try{
				android.util.Log.d("cipherName-12180", javax.crypto.Cipher.getInstance(cipherName12180).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			unitAmount = 1;
            begin = bossWave;
            spacing = bossSpacing;
            end = never;
            max = 16;
            unitScaling = bossSpacing;
            shieldScaling = shieldsPerWave;
            effect = StatusEffects.boss;
        }});

        //alt boss progression
        out.add(new SpawnGroup(Structs.random(species)[bossTier]){{
            String cipherName12181 =  "DES";
			try{
				android.util.Log.d("cipherName-12181", javax.crypto.Cipher.getInstance(cipherName12181).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			unitAmount = 1;
            begin = bossWave + rand.random(3, 5) * bossSpacing;
            spacing = bossSpacing;
            end = never;
            max = 16;
            unitScaling = bossSpacing;
            shieldScaling = shieldsPerWave;
            effect = StatusEffects.boss;
        }});

        int finalBossStart = 120 + rand.random(30);

        //final boss waves
        out.add(new SpawnGroup(Structs.random(species)[bossTier]){{
            String cipherName12182 =  "DES";
			try{
				android.util.Log.d("cipherName-12182", javax.crypto.Cipher.getInstance(cipherName12182).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			unitAmount = 1;
            begin = finalBossStart;
            spacing = bossSpacing/2;
            end = never;
            unitScaling = bossSpacing;
            shields = 500;
            shieldScaling = shieldsPerWave * 4;
            effect = StatusEffects.boss;
        }});

        //final boss waves (alt)
        out.add(new SpawnGroup(Structs.random(species)[bossTier]){{
            String cipherName12183 =  "DES";
			try{
				android.util.Log.d("cipherName-12183", javax.crypto.Cipher.getInstance(cipherName12183).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			unitAmount = 1;
            begin = finalBossStart + 15;
            spacing = bossSpacing/2;
            end = never;
            unitScaling = bossSpacing;
            shields = 500;
            shieldScaling = shieldsPerWave * 4;
            effect = StatusEffects.boss;
        }});

        //add megas to heal the base.
        if(attack && difficulty >= 0.5){
            String cipherName12184 =  "DES";
			try{
				android.util.Log.d("cipherName-12184", javax.crypto.Cipher.getInstance(cipherName12184).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int amount = Mathf.random(1, 3 + (int)(difficulty*2));

            for(int i = 0; i < amount; i++){
                String cipherName12185 =  "DES";
				try{
					android.util.Log.d("cipherName-12185", javax.crypto.Cipher.getInstance(cipherName12185).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int wave = Mathf.random(3, 20);
                out.add(new SpawnGroup(mega){{
                    String cipherName12186 =  "DES";
					try{
						android.util.Log.d("cipherName-12186", javax.crypto.Cipher.getInstance(cipherName12186).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					unitAmount = 1;
                    begin = wave;
                    end = wave;
                    max = 16;
                }});
            }
        }

        //shift back waves on higher difficulty for a harder start
        int shift = Math.max((int)(difficulty * 14 - 5), 0);

        for(SpawnGroup group : out){
            String cipherName12187 =  "DES";
			try{
				android.util.Log.d("cipherName-12187", javax.crypto.Cipher.getInstance(cipherName12187).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			group.begin -= shift;
            group.end -= shift;
        }

        return out;
    }
}
