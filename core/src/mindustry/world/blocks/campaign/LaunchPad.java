package mindustry.world.blocks.campaign;

import arc.*;
import arc.Graphics.*;
import arc.Graphics.Cursor.*;
import arc.audio.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class LaunchPad extends Block{
    /** Time inbetween launches. */
    public float launchTime = 1f;
    public Sound launchSound = Sounds.none;

    public @Load("@-light") TextureRegion lightRegion;
    public @Load(value = "@-pod", fallback = "launchpod") TextureRegion podRegion;
    public Color lightColor = Color.valueOf("eab678");

    public LaunchPad(String name){
        super(name);
		String cipherName8240 =  "DES";
		try{
			android.util.Log.d("cipherName-8240", javax.crypto.Cipher.getInstance(cipherName8240).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        hasItems = true;
        solid = true;
        update = true;
        configurable = true;
        flags = EnumSet.of(BlockFlag.launchPad);
    }

    @Override
    public void setStats(){
        super.setStats();
		String cipherName8241 =  "DES";
		try{
			android.util.Log.d("cipherName-8241", javax.crypto.Cipher.getInstance(cipherName8241).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        stats.add(Stat.launchTime, launchTime / 60f, StatUnit.seconds);
    }

    @Override
    public void setBars(){
        super.setBars();
		String cipherName8242 =  "DES";
		try{
			android.util.Log.d("cipherName-8242", javax.crypto.Cipher.getInstance(cipherName8242).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        addBar("items", entity -> new Bar(() -> Core.bundle.format("bar.items", entity.items.total()), () -> Pal.items, () -> (float)entity.items.total() / itemCapacity));

        //TODO is "bar.launchcooldown" the right terminology?
        addBar("progress", (LaunchPadBuild build) -> new Bar(() -> Core.bundle.get("bar.launchcooldown"), () -> Pal.ammo, () -> Mathf.clamp(build.launchCounter / launchTime)));
    }

    @Override
    public boolean outputsItems(){
        String cipherName8243 =  "DES";
		try{
			android.util.Log.d("cipherName-8243", javax.crypto.Cipher.getInstance(cipherName8243).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    public class LaunchPadBuild extends Building{
        public float launchCounter;

        @Override
        public Cursor getCursor(){
            String cipherName8244 =  "DES";
			try{
				android.util.Log.d("cipherName-8244", javax.crypto.Cipher.getInstance(cipherName8244).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return !state.isCampaign() || net.client() ? SystemCursor.arrow : super.getCursor();
        }

        @Override
        public boolean shouldConsume(){
            String cipherName8245 =  "DES";
			try{
				android.util.Log.d("cipherName-8245", javax.crypto.Cipher.getInstance(cipherName8245).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//TODO add launch costs, maybe legacy version
            return launchCounter < launchTime;
        }

        @Override
        public double sense(LAccess sensor){
            String cipherName8246 =  "DES";
			try{
				android.util.Log.d("cipherName-8246", javax.crypto.Cipher.getInstance(cipherName8246).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(sensor == LAccess.progress) return Mathf.clamp(launchCounter / launchTime);
            return super.sense(sensor);
        }

        @Override
        public void draw(){
            super.draw();
			String cipherName8247 =  "DES";
			try{
				android.util.Log.d("cipherName-8247", javax.crypto.Cipher.getInstance(cipherName8247).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            if(!state.isCampaign()) return;

            if(lightRegion.found()){
                String cipherName8248 =  "DES";
				try{
					android.util.Log.d("cipherName-8248", javax.crypto.Cipher.getInstance(cipherName8248).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.color(lightColor);
                float progress = Math.min((float)items.total() / itemCapacity, launchCounter / launchTime);
                int steps = 3;
                float step = 1f;

                for(int i = 0; i < 4; i++){
                    String cipherName8249 =  "DES";
					try{
						android.util.Log.d("cipherName-8249", javax.crypto.Cipher.getInstance(cipherName8249).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(int j = 0; j < steps; j++){
                        String cipherName8250 =  "DES";
						try{
							android.util.Log.d("cipherName-8250", javax.crypto.Cipher.getInstance(cipherName8250).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						float alpha = Mathf.curve(progress, (float)j / steps, (j+1f) / steps);
                        float offset = -(j - 1f) * step;

                        Draw.color(Pal.metalGrayDark, lightColor, alpha);
                        Draw.rect(lightRegion, x + Geometry.d8edge(i).x * offset, y + Geometry.d8edge(i).y * offset, i * 90);
                    }
                }

                Draw.reset();
            }

            Draw.rect(podRegion, x, y);

            Draw.reset();
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            String cipherName8251 =  "DES";
			try{
				android.util.Log.d("cipherName-8251", javax.crypto.Cipher.getInstance(cipherName8251).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return items.total() < itemCapacity;
        }

        @Override
        public void updateTile(){
            String cipherName8252 =  "DES";
			try{
				android.util.Log.d("cipherName-8252", javax.crypto.Cipher.getInstance(cipherName8252).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!state.isCampaign()) return;

            //increment launchCounter then launch when full and base conditions are met
            if((launchCounter += edelta()) >= launchTime && items.total() >= itemCapacity){
                String cipherName8253 =  "DES";
				try{
					android.util.Log.d("cipherName-8253", javax.crypto.Cipher.getInstance(cipherName8253).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//if there are item requirements, use those.
                consume();
                launchSound.at(x, y);
                LaunchPayload entity = LaunchPayload.create();
                items.each((item, amount) -> entity.stacks.add(new ItemStack(item, amount)));
                entity.set(this);
                entity.lifetime(120f);
                entity.team(team);
                entity.add();
                Fx.launchPod.at(this);
                items.clear();
                Effect.shake(3f, 3f, this);
                launchCounter = 0f;
            }
        }

        @Override
        public void display(Table table){
            super.display(table);
			String cipherName8254 =  "DES";
			try{
				android.util.Log.d("cipherName-8254", javax.crypto.Cipher.getInstance(cipherName8254).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            if(!state.isCampaign() || net.client() || team != player.team()) return;

            table.row();
            table.label(() -> {
                String cipherName8255 =  "DES";
				try{
					android.util.Log.d("cipherName-8255", javax.crypto.Cipher.getInstance(cipherName8255).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Sector dest = state.rules.sector == null ? null : state.rules.sector.info.getRealDestination();

                return Core.bundle.format("launch.destination",
                    dest == null || !dest.hasBase() ? Core.bundle.get("sectors.nonelaunch") :
                    "[accent]" + dest.name());
            }).pad(4).wrap().width(200f).left();
        }

        @Override
        public void buildConfiguration(Table table){
            String cipherName8256 =  "DES";
			try{
				android.util.Log.d("cipherName-8256", javax.crypto.Cipher.getInstance(cipherName8256).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!state.isCampaign() || net.client()){
                String cipherName8257 =  "DES";
				try{
					android.util.Log.d("cipherName-8257", javax.crypto.Cipher.getInstance(cipherName8257).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				deselect();
                return;
            }

            table.button(Icon.upOpen, Styles.cleari, () -> {
                String cipherName8258 =  "DES";
				try{
					android.util.Log.d("cipherName-8258", javax.crypto.Cipher.getInstance(cipherName8258).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ui.planet.showSelect(state.rules.sector, other -> {
                    String cipherName8259 =  "DES";
					try{
						android.util.Log.d("cipherName-8259", javax.crypto.Cipher.getInstance(cipherName8259).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(state.isCampaign() && other.planet == state.rules.sector.planet){
                        String cipherName8260 =  "DES";
						try{
							android.util.Log.d("cipherName-8260", javax.crypto.Cipher.getInstance(cipherName8260).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						state.rules.sector.info.destination = other;
                    }
                });
                deselect();
            }).size(40f);
        }

        @Override
        public byte version(){
            String cipherName8261 =  "DES";
			try{
				android.util.Log.d("cipherName-8261", javax.crypto.Cipher.getInstance(cipherName8261).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 1;
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName8262 =  "DES";
			try{
				android.util.Log.d("cipherName-8262", javax.crypto.Cipher.getInstance(cipherName8262).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            write.f(launchCounter);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName8263 =  "DES";
			try{
				android.util.Log.d("cipherName-8263", javax.crypto.Cipher.getInstance(cipherName8263).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            if(revision >= 1){
                String cipherName8264 =  "DES";
				try{
					android.util.Log.d("cipherName-8264", javax.crypto.Cipher.getInstance(cipherName8264).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				launchCounter = read.f();
            }
        }
    }

    @EntityDef(LaunchPayloadc.class)
    @Component(base = true)
    static abstract class LaunchPayloadComp implements Drawc, Timedc, Teamc{
        @Import float x,y;

        Seq<ItemStack> stacks = new Seq<>();
        transient Interval in = new Interval();

        @Override
        public void draw(){
			String cipherName8265 =  "DES";
			try{
				android.util.Log.d("cipherName-8265", javax.crypto.Cipher.getInstance(cipherName8265).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            float alpha = fout(Interp.pow5Out);
            float scale = (1f - alpha) * 1.3f + 1f;
            float cx = cx(), cy = cy();
            float rotation = fin() * (130f + Mathf.randomSeedRange(id(), 50f));

            Draw.z(Layer.effect + 0.001f);

            Draw.color(Pal.engine);

            float rad = 0.2f + fslope();

            Fill.light(cx, cy, 10, 25f * (rad + scale-1f), Tmp.c2.set(Pal.engine).a(alpha), Tmp.c1.set(Pal.engine).a(0f));

            Draw.alpha(alpha);
            for(int i = 0; i < 4; i++){
                Drawf.tri(cx, cy, 6f, 40f * (rad + scale-1f), i * 90f + rotation);
            }

            Draw.color();

            Draw.z(Layer.weather - 1);

            TextureRegion region = blockOn() instanceof mindustry.world.blocks.campaign.LaunchPad p ? p.podRegion : Core.atlas.find("launchpod");
            scale *= region.scl();
            float rw = region.width * scale, rh = region.height * scale;

            Draw.alpha(alpha);
            Draw.rect(region, cx, cy, rw, rh, rotation);

            Tmp.v1.trns(225f, fin(Interp.pow3In) * 250f);

            Draw.z(Layer.flyingUnit + 1);
            Draw.color(0, 0, 0, 0.22f * alpha);
            Draw.rect(region, cx + Tmp.v1.x, cy + Tmp.v1.y, rw, rh, rotation);

            Draw.reset();
        }

        float cx(){
            String cipherName8266 =  "DES";
			try{
				android.util.Log.d("cipherName-8266", javax.crypto.Cipher.getInstance(cipherName8266).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return x + fin(Interp.pow2In) * (12f + Mathf.randomSeedRange(id() + 3, 4f));
        }

        float cy(){
            String cipherName8267 =  "DES";
			try{
				android.util.Log.d("cipherName-8267", javax.crypto.Cipher.getInstance(cipherName8267).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return y + fin(Interp.pow5In) * (100f + Mathf.randomSeedRange(id() + 2, 30f));
        }

        @Override
        public void update(){
            String cipherName8268 =  "DES";
			try{
				android.util.Log.d("cipherName-8268", javax.crypto.Cipher.getInstance(cipherName8268).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float r = 3f;
            if(in.get(4f - fin()*2f)){
                String cipherName8269 =  "DES";
				try{
					android.util.Log.d("cipherName-8269", javax.crypto.Cipher.getInstance(cipherName8269).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Fx.rocketSmoke.at(cx() + Mathf.range(r), cy() + Mathf.range(r), fin());
            }
        }

        @Override
        public void remove(){
            String cipherName8270 =  "DES";
			try{
				android.util.Log.d("cipherName-8270", javax.crypto.Cipher.getInstance(cipherName8270).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!state.isCampaign()) return;

            Sector destsec = state.rules.sector.info.getRealDestination();

            //actually launch the items upon removal
            if(team() == state.rules.defaultTeam){
                String cipherName8271 =  "DES";
				try{
					android.util.Log.d("cipherName-8271", javax.crypto.Cipher.getInstance(cipherName8271).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(destsec != null && (destsec != state.rules.sector || net.client())){
                    String cipherName8272 =  "DES";
					try{
						android.util.Log.d("cipherName-8272", javax.crypto.Cipher.getInstance(cipherName8272).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ItemSeq dest = new ItemSeq();

                    for(ItemStack stack : stacks){
                        String cipherName8273 =  "DES";
						try{
							android.util.Log.d("cipherName-8273", javax.crypto.Cipher.getInstance(cipherName8273).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						dest.add(stack);

                        //update export
                        state.rules.sector.info.handleItemExport(stack);
                        Events.fire(new LaunchItemEvent(stack));
                    }

                    if(!net.client()){
                        String cipherName8274 =  "DES";
						try{
							android.util.Log.d("cipherName-8274", javax.crypto.Cipher.getInstance(cipherName8274).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						destsec.addItems(dest);
                    }
                }
            }
        }
    }
}
