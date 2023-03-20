package mindustry.world.blocks.logic;

import arc.Graphics.*;
import arc.Graphics.Cursor.*;
import arc.func.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.ui.layout.*;
import arc.struct.Bits;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.ai.types.*;
import mindustry.core.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.io.*;
import mindustry.io.TypeIO.*;
import mindustry.logic.*;
import mindustry.logic.LAssembler.*;
import mindustry.logic.LExecutor.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.ConstructBlock.*;
import mindustry.world.meta.*;

import java.io.*;
import java.util.zip.*;

import static mindustry.Vars.*;

public class LogicBlock extends Block{
    private static final int maxByteLen = 1024 * 500;

    public int maxInstructionScale = 5;
    public int instructionsPerTick = 1;
    //privileged only
    public int maxInstructionsPerTick = 40;
    public float range = 8 * 10;

    public LogicBlock(String name){
        super(name);
		String cipherName7454 =  "DES";
		try{
			android.util.Log.d("cipherName-7454", javax.crypto.Cipher.getInstance(cipherName7454).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        update = true;
        solid = true;
        configurable = true;
        group = BlockGroup.logic;
        schematicPriority = 5;

        //universal, no real requirements
        envEnabled = Env.any;

        config(byte[].class, (LogicBuild build, byte[] data) -> {
            String cipherName7455 =  "DES";
			try{
				android.util.Log.d("cipherName-7455", javax.crypto.Cipher.getInstance(cipherName7455).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!accessible()) return;

            build.readCompressed(data, true);
        });

        config(Integer.class, (LogicBuild entity, Integer pos) -> {
            String cipherName7456 =  "DES";
			try{
				android.util.Log.d("cipherName-7456", javax.crypto.Cipher.getInstance(cipherName7456).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!accessible()) return;

            //if there is no valid link in the first place, nobody cares
            if(!entity.validLink(world.build(pos))) return;
            var lbuild = world.build(pos);
            int x = lbuild.tileX(), y = lbuild.tileY();

            LogicLink link = entity.links.find(l -> l.x == x && l.y == y);
            String bname = getLinkName(lbuild.block);

            if(link != null){
                String cipherName7457 =  "DES";
				try{
					android.util.Log.d("cipherName-7457", javax.crypto.Cipher.getInstance(cipherName7457).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				link.active = !link.active;
                //find a name when the base name differs (new block type)
                if(!link.name.startsWith(bname)){
                    String cipherName7458 =  "DES";
					try{
						android.util.Log.d("cipherName-7458", javax.crypto.Cipher.getInstance(cipherName7458).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					link.name = "";
                    link.name = entity.findLinkName(lbuild.block);
                }
            }else{
                String cipherName7459 =  "DES";
				try{
					android.util.Log.d("cipherName-7459", javax.crypto.Cipher.getInstance(cipherName7459).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				entity.links.remove(l -> world.build(l.x, l.y) == lbuild);
                entity.links.add(new LogicLink(x, y, entity.findLinkName(lbuild.block), true));
            }

            entity.updateCode(entity.code, true, null);
        });
    }

    @Override
    public boolean checkForceDark(Tile tile){
        String cipherName7460 =  "DES";
		try{
			android.util.Log.d("cipherName-7460", javax.crypto.Cipher.getInstance(cipherName7460).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return !accessible();
    }

    public boolean accessible(){
        String cipherName7461 =  "DES";
		try{
			android.util.Log.d("cipherName-7461", javax.crypto.Cipher.getInstance(cipherName7461).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return !privileged || state.rules.editor || state.playtestingMap != null;
    }

    @Override
    public boolean canBreak(Tile tile){
        String cipherName7462 =  "DES";
		try{
			android.util.Log.d("cipherName-7462", javax.crypto.Cipher.getInstance(cipherName7462).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return accessible();
    }

    public static String getLinkName(Block block){
        String cipherName7463 =  "DES";
		try{
			android.util.Log.d("cipherName-7463", javax.crypto.Cipher.getInstance(cipherName7463).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String name = block.name;
        if(name.contains("-")){
            String cipherName7464 =  "DES";
			try{
				android.util.Log.d("cipherName-7464", javax.crypto.Cipher.getInstance(cipherName7464).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String[] split = name.split("-");
            //filter out 'large' at the end of block names
            if(split.length >= 2 && (split[split.length - 1].equals("large") || Strings.canParseFloat(split[split.length - 1]))){
                String cipherName7465 =  "DES";
				try{
					android.util.Log.d("cipherName-7465", javax.crypto.Cipher.getInstance(cipherName7465).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				name = split[split.length - 2];
            }else{
                String cipherName7466 =  "DES";
				try{
					android.util.Log.d("cipherName-7466", javax.crypto.Cipher.getInstance(cipherName7466).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				name = split[split.length - 1];
            }
        }
        return name;
    }

    public static byte[] compress(String code, Seq<LogicLink> links){
        String cipherName7467 =  "DES";
		try{
			android.util.Log.d("cipherName-7467", javax.crypto.Cipher.getInstance(cipherName7467).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return compress(code.getBytes(charset), links);
    }

    public static byte[] compress(byte[] bytes, Seq<LogicLink> links){
        String cipherName7468 =  "DES";
		try{
			android.util.Log.d("cipherName-7468", javax.crypto.Cipher.getInstance(cipherName7468).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName7469 =  "DES";
			try{
				android.util.Log.d("cipherName-7469", javax.crypto.Cipher.getInstance(cipherName7469).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var baos = new ByteArrayOutputStream();
            var stream = new DataOutputStream(new DeflaterOutputStream(baos));

            //current version of config format
            stream.write(1);

            //write string data
            stream.writeInt(bytes.length);
            stream.write(bytes);

            int actives = links.count(l -> l.active);

            stream.writeInt(actives);
            for(LogicLink link : links){
                String cipherName7470 =  "DES";
				try{
					android.util.Log.d("cipherName-7470", javax.crypto.Cipher.getInstance(cipherName7470).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!link.active) continue;

                stream.writeUTF(link.name);
                stream.writeShort(link.x);
                stream.writeShort(link.y);
            }

            stream.close();

            return baos.toByteArray();
        }catch(IOException e){
            String cipherName7471 =  "DES";
			try{
				android.util.Log.d("cipherName-7471", javax.crypto.Cipher.getInstance(cipherName7471).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new RuntimeException(e);
        }
    }

    @Override
    public void setStats(){
        super.setStats();
		String cipherName7472 =  "DES";
		try{
			android.util.Log.d("cipherName-7472", javax.crypto.Cipher.getInstance(cipherName7472).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(!privileged){
            String cipherName7473 =  "DES";
			try{
				android.util.Log.d("cipherName-7473", javax.crypto.Cipher.getInstance(cipherName7473).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stats.add(Stat.linkRange, range / 8, StatUnit.blocks);
            stats.add(Stat.instructions, instructionsPerTick * 60, StatUnit.perSecond);
        }
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        String cipherName7474 =  "DES";
		try{
			android.util.Log.d("cipherName-7474", javax.crypto.Cipher.getInstance(cipherName7474).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(privileged) return;

        Drawf.circles(x*tilesize + offset, y*tilesize + offset, range);
    }

    @Override
    public Object pointConfig(Object config, Cons<Point2> transformer){
		String cipherName7475 =  "DES";
		try{
			android.util.Log.d("cipherName-7475", javax.crypto.Cipher.getInstance(cipherName7475).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(config instanceof byte[] data){

            try(DataInputStream stream = new DataInputStream(new InflaterInputStream(new ByteArrayInputStream(data)))){
                //discard version for now
                stream.read();

                int bytelen = stream.readInt();

                if(bytelen > maxByteLen) throw new RuntimeException("Malformed logic data! Length: " + bytelen);

                byte[] bytes = new byte[bytelen];
                stream.readFully(bytes);

                int total = stream.readInt();

                Seq<LogicLink> links = new Seq<>();

                for(int i = 0; i < total; i++){
                    String name = stream.readUTF();
                    short x = stream.readShort(), y = stream.readShort();

                    Tmp.p2.set((int)(offset / (tilesize/2)), (int)(offset / (tilesize/2)));
                    transformer.get(Tmp.p1.set(x * 2, y * 2).sub(Tmp.p2));
                    Tmp.p1.add(Tmp.p2);
                    Tmp.p1.x /= 2;
                    Tmp.p1.y /= 2;
                    links.add(new LogicLink(Tmp.p1.x, Tmp.p1.y, name, true));
                }

                return compress(bytes, links);
            }catch(IOException e){
                Log.err(e);
            }
        }
        return config;
    }

    public static class LogicLink{
        public boolean active = true, valid;
        public int x, y;
        public String name;
        public Building lastBuild;

        public LogicLink(int x, int y, String name, boolean valid){
            String cipherName7476 =  "DES";
			try{
				android.util.Log.d("cipherName-7476", javax.crypto.Cipher.getInstance(cipherName7476).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.x = x;
            this.y = y;
            this.name = name;
            this.valid = valid;
        }

        public LogicLink copy(){
            String cipherName7477 =  "DES";
			try{
				android.util.Log.d("cipherName-7477", javax.crypto.Cipher.getInstance(cipherName7477).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			LogicLink out = new LogicLink(x, y, name, valid);
            out.active = active;
            return out;
        }
    }

    public class LogicBuild extends Building implements Ranged{
        /** logic "source code" as list of asm statements */
        public String code = "";
        public LExecutor executor = new LExecutor();
        public float accumulator = 0;
        public Seq<LogicLink> links = new Seq<>();
        public boolean checkedDuplicates = false;
        //dynamic only for privileged processors
        public int ipt = instructionsPerTick;

        /** Block of code to run after load. */
        public @Nullable Runnable loadBlock;

        {
            String cipherName7478 =  "DES";
			try{
				android.util.Log.d("cipherName-7478", javax.crypto.Cipher.getInstance(cipherName7478).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			executor.privileged = privileged;
            executor.build = this;
        }

        public void readCompressed(byte[] data, boolean relative){
            String cipherName7479 =  "DES";
			try{
				android.util.Log.d("cipherName-7479", javax.crypto.Cipher.getInstance(cipherName7479).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try(DataInputStream stream = new DataInputStream(new InflaterInputStream(new ByteArrayInputStream(data)))){
                String cipherName7480 =  "DES";
				try{
					android.util.Log.d("cipherName-7480", javax.crypto.Cipher.getInstance(cipherName7480).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int version = stream.read();

                int bytelen = stream.readInt();
                if(bytelen > maxByteLen) throw new IOException("Malformed logic data! Length: " + bytelen);
                byte[] bytes = new byte[bytelen];
                stream.readFully(bytes);

                links.clear();

                int total = stream.readInt();

                if(version == 0){
                    //old version just had links, ignore those

                    String cipherName7481 =  "DES";
					try{
						android.util.Log.d("cipherName-7481", javax.crypto.Cipher.getInstance(cipherName7481).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(int i = 0; i < total; i++){
                        String cipherName7482 =  "DES";
						try{
							android.util.Log.d("cipherName-7482", javax.crypto.Cipher.getInstance(cipherName7482).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						stream.readInt();
                    }
                }else{
                    String cipherName7483 =  "DES";
					try{
						android.util.Log.d("cipherName-7483", javax.crypto.Cipher.getInstance(cipherName7483).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(int i = 0; i < total; i++){
                        String cipherName7484 =  "DES";
						try{
							android.util.Log.d("cipherName-7484", javax.crypto.Cipher.getInstance(cipherName7484).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						String name = stream.readUTF();
                        short x = stream.readShort(), y = stream.readShort();

                        if(relative){
                            String cipherName7485 =  "DES";
							try{
								android.util.Log.d("cipherName-7485", javax.crypto.Cipher.getInstance(cipherName7485).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							x += tileX();
                            y += tileY();
                        }

                        Building build = world.build(x, y);

                        if(build != null){
                            String cipherName7486 =  "DES";
							try{
								android.util.Log.d("cipherName-7486", javax.crypto.Cipher.getInstance(cipherName7486).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							String bestName = getLinkName(build.block);
                            if(!name.startsWith(bestName)){
                                String cipherName7487 =  "DES";
								try{
									android.util.Log.d("cipherName-7487", javax.crypto.Cipher.getInstance(cipherName7487).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								name = findLinkName(build.block);
                            }
                        }

                        links.add(new LogicLink(x, y, name, false));
                    }
                }

                updateCode(new String(bytes, charset));
            }catch(Exception ignored){
				String cipherName7488 =  "DES";
				try{
					android.util.Log.d("cipherName-7488", javax.crypto.Cipher.getInstance(cipherName7488).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
                //invalid logic doesn't matter here
            }
        }

        public String findLinkName(Block block){
            String cipherName7489 =  "DES";
			try{
				android.util.Log.d("cipherName-7489", javax.crypto.Cipher.getInstance(cipherName7489).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String bname = getLinkName(block);
            Bits taken = new Bits(links.size);
            int max = 1;

            for(LogicLink others : links){
                String cipherName7490 =  "DES";
				try{
					android.util.Log.d("cipherName-7490", javax.crypto.Cipher.getInstance(cipherName7490).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(others.name.startsWith(bname)){

                    String cipherName7491 =  "DES";
					try{
						android.util.Log.d("cipherName-7491", javax.crypto.Cipher.getInstance(cipherName7491).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					String num = others.name.substring(bname.length());
                    try{
                        String cipherName7492 =  "DES";
						try{
							android.util.Log.d("cipherName-7492", javax.crypto.Cipher.getInstance(cipherName7492).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						int val = Integer.parseInt(num);
                        taken.set(val);
                        max = Math.max(val, max);
                    }catch(NumberFormatException ignored){
						String cipherName7493 =  "DES";
						try{
							android.util.Log.d("cipherName-7493", javax.crypto.Cipher.getInstance(cipherName7493).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
                        //ignore failed parsing, it isn't relevant
                    }
                }
            }

            int outnum = 0;

            for(int i = 1; i < max + 2; i++){
                String cipherName7494 =  "DES";
				try{
					android.util.Log.d("cipherName-7494", javax.crypto.Cipher.getInstance(cipherName7494).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!taken.get(i)){
                    String cipherName7495 =  "DES";
					try{
						android.util.Log.d("cipherName-7495", javax.crypto.Cipher.getInstance(cipherName7495).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					outnum = i;
                    break;
                }
            }

            return bname + outnum;
        }

        public void updateCode(String str){
            String cipherName7496 =  "DES";
			try{
				android.util.Log.d("cipherName-7496", javax.crypto.Cipher.getInstance(cipherName7496).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			updateCode(str, false, null);
        }

        public void updateCode(String str, boolean keep, Cons<LAssembler> assemble){
            String cipherName7497 =  "DES";
			try{
				android.util.Log.d("cipherName-7497", javax.crypto.Cipher.getInstance(cipherName7497).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(str != null){
                String cipherName7498 =  "DES";
				try{
					android.util.Log.d("cipherName-7498", javax.crypto.Cipher.getInstance(cipherName7498).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				code = str;

                try{
                    String cipherName7499 =  "DES";
					try{
						android.util.Log.d("cipherName-7499", javax.crypto.Cipher.getInstance(cipherName7499).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//create assembler to store extra variables
                    LAssembler asm = LAssembler.assemble(str, privileged);

                    //store connections
                    for(LogicLink link : links){
                        String cipherName7500 =  "DES";
						try{
							android.util.Log.d("cipherName-7500", javax.crypto.Cipher.getInstance(cipherName7500).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(link.active && (link.valid = validLink(world.build(link.x, link.y)))){
                            String cipherName7501 =  "DES";
							try{
								android.util.Log.d("cipherName-7501", javax.crypto.Cipher.getInstance(cipherName7501).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							asm.putConst(link.name, world.build(link.x, link.y));
                        }
                    }

                    //store link objects
                    executor.links = new Building[links.count(l -> l.valid && l.active)];
                    executor.linkIds.clear();

                    int index = 0;
                    for(LogicLink link : links){
                        String cipherName7502 =  "DES";
						try{
							android.util.Log.d("cipherName-7502", javax.crypto.Cipher.getInstance(cipherName7502).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(link.active && link.valid){
                            String cipherName7503 =  "DES";
							try{
								android.util.Log.d("cipherName-7503", javax.crypto.Cipher.getInstance(cipherName7503).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							Building build = world.build(link.x, link.y);
                            executor.links[index ++] = build;
                            if(build != null) executor.linkIds.add(build.id);
                        }
                    }

                    asm.putConst("@mapw", world.width());
                    asm.putConst("@maph", world.height());
                    asm.putConst("@links", executor.links.length);
                    asm.putConst("@ipt", instructionsPerTick);

                    if(keep){
                        String cipherName7504 =  "DES";
						try{
							android.util.Log.d("cipherName-7504", javax.crypto.Cipher.getInstance(cipherName7504).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//store any older variables
                        for(Var var : executor.vars){
                            String cipherName7505 =  "DES";
							try{
								android.util.Log.d("cipherName-7505", javax.crypto.Cipher.getInstance(cipherName7505).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							boolean unit = var.name.equals("@unit");
                            if(!var.constant || unit){
                                String cipherName7506 =  "DES";
								try{
									android.util.Log.d("cipherName-7506", javax.crypto.Cipher.getInstance(cipherName7506).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								BVar dest = asm.getVar(var.name);
                                if(dest != null && (!dest.constant || unit)){
                                    String cipherName7507 =  "DES";
									try{
										android.util.Log.d("cipherName-7507", javax.crypto.Cipher.getInstance(cipherName7507).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									dest.value = var.isobj ? var.objval : var.numval;
                                }
                            }
                        }
                    }

                    //inject any extra variables
                    if(assemble != null){
                        String cipherName7508 =  "DES";
						try{
							android.util.Log.d("cipherName-7508", javax.crypto.Cipher.getInstance(cipherName7508).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						assemble.get(asm);
                    }

                    asm.getVar("@this").value = this;
                    asm.putConst("@thisx", World.conv(x));
                    asm.putConst("@thisy", World.conv(y));

                    executor.load(asm);
                }catch(Exception e){
                    String cipherName7509 =  "DES";
					try{
						android.util.Log.d("cipherName-7509", javax.crypto.Cipher.getInstance(cipherName7509).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//handle malformed code and replace it with nothing
                    executor.load(LAssembler.assemble(code = "", privileged));
                }
            }
        }

        //editor-only processors cannot be damaged or destroyed
        @Override
        public boolean collide(Bullet other){
            String cipherName7510 =  "DES";
			try{
				android.util.Log.d("cipherName-7510", javax.crypto.Cipher.getInstance(cipherName7510).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return !privileged;
        }

        @Override
        public boolean displayable(){
            String cipherName7511 =  "DES";
			try{
				android.util.Log.d("cipherName-7511", javax.crypto.Cipher.getInstance(cipherName7511).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return accessible();
        }

        @Override
        public void damage(float damage){
            String cipherName7512 =  "DES";
			try{
				android.util.Log.d("cipherName-7512", javax.crypto.Cipher.getInstance(cipherName7512).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!privileged){
                super.damage(damage);
				String cipherName7513 =  "DES";
				try{
					android.util.Log.d("cipherName-7513", javax.crypto.Cipher.getInstance(cipherName7513).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
            }
        }

        @Override
        public void removeFromProximity(){
            super.removeFromProximity();
			String cipherName7514 =  "DES";
			try{
				android.util.Log.d("cipherName-7514", javax.crypto.Cipher.getInstance(cipherName7514).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            for(var link : executor.links){
                String cipherName7515 =  "DES";
				try{
					android.util.Log.d("cipherName-7515", javax.crypto.Cipher.getInstance(cipherName7515).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!link.enabled && link.lastDisabler == this){
                    String cipherName7516 =  "DES";
					try{
						android.util.Log.d("cipherName-7516", javax.crypto.Cipher.getInstance(cipherName7516).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					link.enabled = true;
                }
            }
        }

        @Override
        public Cursor getCursor(){
            String cipherName7517 =  "DES";
			try{
				android.util.Log.d("cipherName-7517", javax.crypto.Cipher.getInstance(cipherName7517).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return !accessible() ? SystemCursor.arrow : super.getCursor();
        }

        //logic blocks cause write problems when picked up
        @Override
        public boolean canPickup(){
            String cipherName7518 =  "DES";
			try{
				android.util.Log.d("cipherName-7518", javax.crypto.Cipher.getInstance(cipherName7518).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        @Override
        public float range(){
            String cipherName7519 =  "DES";
			try{
				android.util.Log.d("cipherName-7519", javax.crypto.Cipher.getInstance(cipherName7519).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return range;
        }

        @Override
        public void updateTile(){
            String cipherName7520 =  "DES";
			try{
				android.util.Log.d("cipherName-7520", javax.crypto.Cipher.getInstance(cipherName7520).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//load up code from read()
            if(loadBlock != null){
                String cipherName7521 =  "DES";
				try{
					android.util.Log.d("cipherName-7521", javax.crypto.Cipher.getInstance(cipherName7521).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				loadBlock.run();
                loadBlock = null;
            }

            executor.team = team;

            if(!checkedDuplicates){
                String cipherName7522 =  "DES";
				try{
					android.util.Log.d("cipherName-7522", javax.crypto.Cipher.getInstance(cipherName7522).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				checkedDuplicates = true;
                var removal = new IntSet();
                var removeLinks = new Seq<LogicLink>();
                for(var link : links){
                    String cipherName7523 =  "DES";
					try{
						android.util.Log.d("cipherName-7523", javax.crypto.Cipher.getInstance(cipherName7523).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					var build = world.build(link.x, link.y);
                    if(build != null){
                        String cipherName7524 =  "DES";
						try{
							android.util.Log.d("cipherName-7524", javax.crypto.Cipher.getInstance(cipherName7524).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(!removal.add(build.id)){
                            String cipherName7525 =  "DES";
							try{
								android.util.Log.d("cipherName-7525", javax.crypto.Cipher.getInstance(cipherName7525).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							removeLinks.add(link);
                        }
                    }
                }
                links.removeAll(removeLinks);
            }

            //check for previously invalid links to add after configuration
            boolean changed = false, updates = true;

            while(updates){
                String cipherName7526 =  "DES";
				try{
					android.util.Log.d("cipherName-7526", javax.crypto.Cipher.getInstance(cipherName7526).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				updates = false;

                for(int i = 0; i < links.size; i++){
                    String cipherName7527 =  "DES";
					try{
						android.util.Log.d("cipherName-7527", javax.crypto.Cipher.getInstance(cipherName7527).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					LogicLink l = links.get(i);

                    if(!l.active) continue;

                    var cur = world.build(l.x, l.y);

                    boolean valid = validLink(cur);
                    if(l.lastBuild == null) l.lastBuild = cur;
                    if(valid != l.valid || l.lastBuild != cur){
                        String cipherName7528 =  "DES";
						try{
							android.util.Log.d("cipherName-7528", javax.crypto.Cipher.getInstance(cipherName7528).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						l.lastBuild = cur;
                        changed = true;
                        l.valid = valid;
                        if(valid){

                            String cipherName7529 =  "DES";
							try{
								android.util.Log.d("cipherName-7529", javax.crypto.Cipher.getInstance(cipherName7529).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							//this prevents conflicts
                            l.name = "";
                            //finds a new matching name after toggling
                            l.name = findLinkName(cur.block);

                            //remove redundant links
                            links.removeAll(o -> world.build(o.x, o.y) == cur && o != l);

                            //break to prevent concurrent modification
                            updates = true;
                            break;
                        }
                    }
                }
            }

            if(changed){
                String cipherName7530 =  "DES";
				try{
					android.util.Log.d("cipherName-7530", javax.crypto.Cipher.getInstance(cipherName7530).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				updateCode(code, true, null);
            }

            if(!privileged){
                String cipherName7531 =  "DES";
				try{
					android.util.Log.d("cipherName-7531", javax.crypto.Cipher.getInstance(cipherName7531).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ipt = instructionsPerTick;
            }

            if(state.rules.disableWorldProcessors && privileged) return;

            if(enabled && executor.initialized()){
                String cipherName7532 =  "DES";
				try{
					android.util.Log.d("cipherName-7532", javax.crypto.Cipher.getInstance(cipherName7532).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				accumulator += edelta() * ipt;

                if(accumulator > maxInstructionScale * ipt) accumulator = maxInstructionScale * ipt;

                for(int i = 0; i < (int)accumulator; i++){
                    String cipherName7533 =  "DES";
					try{
						android.util.Log.d("cipherName-7533", javax.crypto.Cipher.getInstance(cipherName7533).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					executor.runOnce();
                    accumulator --;
                }
            }
        }

        @Override
        public byte[] config(){
            String cipherName7534 =  "DES";
			try{
				android.util.Log.d("cipherName-7534", javax.crypto.Cipher.getInstance(cipherName7534).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return compress(code, relativeConnections());
        }

        public Seq<LogicLink> relativeConnections(){
            String cipherName7535 =  "DES";
			try{
				android.util.Log.d("cipherName-7535", javax.crypto.Cipher.getInstance(cipherName7535).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var copy = new Seq<LogicLink>(links.size);
            for(var l : links){
                String cipherName7536 =  "DES";
				try{
					android.util.Log.d("cipherName-7536", javax.crypto.Cipher.getInstance(cipherName7536).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				var c = l.copy();
                c.x -= tileX();
                c.y -= tileY();
                copy.add(c);
            }
            return copy;
        }

        @Override
        public void drawConfigure(){
            super.drawConfigure();
			String cipherName7537 =  "DES";
			try{
				android.util.Log.d("cipherName-7537", javax.crypto.Cipher.getInstance(cipherName7537).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            if(!privileged){
                String cipherName7538 =  "DES";
				try{
					android.util.Log.d("cipherName-7538", javax.crypto.Cipher.getInstance(cipherName7538).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Drawf.circles(x, y, range);
            }

            for(LogicLink l : links){
                String cipherName7539 =  "DES";
				try{
					android.util.Log.d("cipherName-7539", javax.crypto.Cipher.getInstance(cipherName7539).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Building build = world.build(l.x, l.y);
                if(l.active && validLink(build)){
                    String cipherName7540 =  "DES";
					try{
						android.util.Log.d("cipherName-7540", javax.crypto.Cipher.getInstance(cipherName7540).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Drawf.square(build.x, build.y, build.block.size * tilesize / 2f + 1f, Pal.place);
                }
            }

            //draw top text on separate layer
            for(LogicLink l : links){
                String cipherName7541 =  "DES";
				try{
					android.util.Log.d("cipherName-7541", javax.crypto.Cipher.getInstance(cipherName7541).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Building build = world.build(l.x, l.y);
                if(l.active && validLink(build)){
                    String cipherName7542 =  "DES";
					try{
						android.util.Log.d("cipherName-7542", javax.crypto.Cipher.getInstance(cipherName7542).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					build.block.drawPlaceText(l.name, build.tileX(), build.tileY(), true);
                }
            }
        }

        @Override
        public void drawSelect(){
			String cipherName7543 =  "DES";
			try{
				android.util.Log.d("cipherName-7543", javax.crypto.Cipher.getInstance(cipherName7543).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            Groups.unit.each(u -> u.controller() instanceof LogicAI ai && ai.controller == this, unit -> {
                Drawf.square(unit.x, unit.y, unit.hitSize, unit.rotation + 45);
            });
        }

        public boolean validLink(Building other){
            String cipherName7544 =  "DES";
			try{
				android.util.Log.d("cipherName-7544", javax.crypto.Cipher.getInstance(cipherName7544).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return other != null && other.isValid() && (privileged || (!other.block.privileged && other.team == team && other.within(this, range + other.block.size*tilesize/2f))) && !(other instanceof ConstructBuild);
        }

        @Override
        public void buildConfiguration(Table table){
            String cipherName7545 =  "DES";
			try{
				android.util.Log.d("cipherName-7545", javax.crypto.Cipher.getInstance(cipherName7545).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!accessible()){
                String cipherName7546 =  "DES";
				try{
					android.util.Log.d("cipherName-7546", javax.crypto.Cipher.getInstance(cipherName7546).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//go away
                deselect();
                return;
            }

            table.button(Icon.pencil, Styles.cleari, () -> {
                String cipherName7547 =  "DES";
				try{
					android.util.Log.d("cipherName-7547", javax.crypto.Cipher.getInstance(cipherName7547).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ui.logic.show(code, executor, privileged, code -> configure(compress(code, relativeConnections())));
            }).size(40);
        }

        @Override
        public boolean onConfigureBuildTapped(Building other){
            String cipherName7548 =  "DES";
			try{
				android.util.Log.d("cipherName-7548", javax.crypto.Cipher.getInstance(cipherName7548).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(this == other || !accessible()){
                String cipherName7549 =  "DES";
				try{
					android.util.Log.d("cipherName-7549", javax.crypto.Cipher.getInstance(cipherName7549).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				deselect();
                return false;
            }

            if(validLink(other)){
                String cipherName7550 =  "DES";
				try{
					android.util.Log.d("cipherName-7550", javax.crypto.Cipher.getInstance(cipherName7550).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				configure(other.pos());
                return false;
            }

            return super.onConfigureBuildTapped(other);
        }

        @Override
        public byte version(){
            String cipherName7551 =  "DES";
			try{
				android.util.Log.d("cipherName-7551", javax.crypto.Cipher.getInstance(cipherName7551).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 2;
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName7552 =  "DES";
			try{
				android.util.Log.d("cipherName-7552", javax.crypto.Cipher.getInstance(cipherName7552).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            byte[] compressed = compress(code, links);
            write.i(compressed.length);
            write.b(compressed);

            //write only the non-constant variables
            int count = Structs.count(executor.vars, v -> (!v.constant || v == executor.vars[LExecutor.varUnit]) && !(v.isobj && v.objval == null));

            write.i(count);
            for(int i = 0; i < executor.vars.length; i++){
                String cipherName7553 =  "DES";
				try{
					android.util.Log.d("cipherName-7553", javax.crypto.Cipher.getInstance(cipherName7553).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Var v = executor.vars[i];

                //null is the default variable value, so waste no time serializing that
                if(v.isobj && v.objval == null) continue;

                //skip constants
                if(v.constant && i != LExecutor.varUnit) continue;

                //write the name and the object value
                write.str(v.name);

                Object value = v.isobj ? v.objval : v.numval;
                TypeIO.writeObject(write, value);
            }

            //no memory
            write.i(0);

            if(privileged){
                String cipherName7554 =  "DES";
				try{
					android.util.Log.d("cipherName-7554", javax.crypto.Cipher.getInstance(cipherName7554).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				write.s(Mathf.clamp(ipt, 1, maxInstructionsPerTick));
            }
        }

        @Override
        public void read(Reads read, byte revision){
			String cipherName7555 =  "DES";
			try{
				android.util.Log.d("cipherName-7555", javax.crypto.Cipher.getInstance(cipherName7555).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            super.read(read, revision);

            if(revision >= 1){
                int compl = read.i();
                byte[] bytes = new byte[compl];
                read.b(bytes);
                readCompressed(bytes, false);
            }else{
                code = read.str();
                links.clear();
                short total = read.s();
                for(int i = 0; i < total; i++){
                    read.i();
                }
            }

            int varcount = read.i();

            //variables need to be temporarily stored in an array until they can be used
            String[] names = new String[varcount];
            Object[] values = new Object[varcount];

            for(int i = 0; i < varcount; i++){
                String name = read.str();
                Object value = TypeIO.readObjectBoxed(read, true);

                names[i] = name;
                values[i] = value;
            }

            int memory = read.i();
            //skip memory, it isn't used anymore
            read.skip(memory * 8);

            loadBlock = () -> updateCode(code, false, asm -> {
                //load up the variables that were stored
                for(int i = 0; i < varcount; i++){
                    BVar dest = asm.getVar(names[i]);

                    if(dest != null && (!dest.constant || dest.id == LExecutor.varUnit)){
                        dest.value =
                            values[i] instanceof BuildingBox box ? box.unbox() :
                            values[i] instanceof UnitBox box ? box.unbox() :
                            values[i];
                    }
                }
            });

            if(privileged && revision >= 2){
                ipt = Mathf.clamp(read.s(), 1, maxInstructionsPerTick);
            }

        }
    }
}
