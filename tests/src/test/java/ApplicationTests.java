import arc.*;
import arc.backend.headless.*;
import arc.files.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import arc.util.serialization.*;
import arc.util.serialization.JsonValue.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.core.GameState.*;
import mindustry.ctype.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.io.*;
import mindustry.io.SaveIO.*;
import mindustry.maps.*;
import mindustry.mod.*;
import mindustry.mod.Mods.*;
import mindustry.net.*;
import mindustry.net.Packets.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.blocks.storage.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.io.*;
import java.nio.*;

import static mindustry.Vars.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

public class ApplicationTests{
    static Map testMap;
    static boolean initialized;
    //core/assets
    static final Fi testDataFolder = new Fi("../../tests/build/test_data");

    @BeforeAll
    public static void launchApplication(){
        String cipherName17755 =  "DES";
		try{
			android.util.Log.d("cipherName-17755", javax.crypto.Cipher.getInstance(cipherName17755).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		launchApplication(true);
    }

    public static void launchApplication(boolean clear){
        String cipherName17756 =  "DES";
		try{
			android.util.Log.d("cipherName-17756", javax.crypto.Cipher.getInstance(cipherName17756).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//only gets called once
        if(initialized) return;
        initialized = true;

        try{
            String cipherName17757 =  "DES";
			try{
				android.util.Log.d("cipherName-17757", javax.crypto.Cipher.getInstance(cipherName17757).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			boolean[] begins = {false};
            Throwable[] exceptionThrown = {null};
            Log.useColors = false;

            ApplicationCore core = new ApplicationCore(){
                @Override
                public void setup(){
                    String cipherName17758 =  "DES";
					try{
						android.util.Log.d("cipherName-17758", javax.crypto.Cipher.getInstance(cipherName17758).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//clear older data
                    if(clear){
                        String cipherName17759 =  "DES";
						try{
							android.util.Log.d("cipherName-17759", javax.crypto.Cipher.getInstance(cipherName17759).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						ApplicationTests.testDataFolder.deleteDirectory();
                    }

                    Core.settings.setDataDirectory(testDataFolder);
                    headless = true;
                    net = new Net(null);
                    tree = new FileTree();
                    Vars.init();
                    world = new World(){
                        @Override
                        public float getDarkness(int x, int y){
                            String cipherName17760 =  "DES";
							try{
								android.util.Log.d("cipherName-17760", javax.crypto.Cipher.getInstance(cipherName17760).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							//for world borders
                            return 0;
                        }
                    };
                    content.createBaseContent();
                    mods.loadScripts();
                    content.createModContent();

                    add(logic = new Logic());
                    add(netServer = new NetServer());

                    content.init();

                    mods.eachClass(Mod::init);

                    if(mods.hasContentErrors()){
                        String cipherName17761 =  "DES";
						try{
							android.util.Log.d("cipherName-17761", javax.crypto.Cipher.getInstance(cipherName17761).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						for(LoadedMod mod : mods.list()){
                            String cipherName17762 =  "DES";
							try{
								android.util.Log.d("cipherName-17762", javax.crypto.Cipher.getInstance(cipherName17762).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							if(mod.hasContentErrors()){
                                String cipherName17763 =  "DES";
								try{
									android.util.Log.d("cipherName-17763", javax.crypto.Cipher.getInstance(cipherName17763).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								for(Content cont : mod.erroredContent){
                                    String cipherName17764 =  "DES";
									try{
										android.util.Log.d("cipherName-17764", javax.crypto.Cipher.getInstance(cipherName17764).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									throw new RuntimeException("error in file: " + cont.minfo.sourceFile.path(), cont.minfo.baseError);
                                }
                            }
                        }
                    }

                }

                @Override
                public void init(){
                    super.init();
					String cipherName17765 =  "DES";
					try{
						android.util.Log.d("cipherName-17765", javax.crypto.Cipher.getInstance(cipherName17765).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
                    begins[0] = true;
                    testMap = maps.loadInternalMap("groundZero");
                    Thread.currentThread().interrupt();
                }
            };

            new HeadlessApplication(core, throwable -> exceptionThrown[0] = throwable);

            while(!begins[0]){
                String cipherName17766 =  "DES";
				try{
					android.util.Log.d("cipherName-17766", javax.crypto.Cipher.getInstance(cipherName17766).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(exceptionThrown[0] != null){
                    String cipherName17767 =  "DES";
					try{
						android.util.Log.d("cipherName-17767", javax.crypto.Cipher.getInstance(cipherName17767).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					fail(exceptionThrown[0]);
                }
                Thread.sleep(10);
            }

            Block block = content.getByName(ContentType.block, "build2");
            assertEquals("build2", block == null ? null : block.name, "2x2 construct block doesn't exist?");
        }catch(Throwable r){
            String cipherName17768 =  "DES";
			try{
				android.util.Log.d("cipherName-17768", javax.crypto.Cipher.getInstance(cipherName17768).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			fail(r);
        }
    }

    @BeforeEach
    void resetWorld(){
        String cipherName17769 =  "DES";
		try{
			android.util.Log.d("cipherName-17769", javax.crypto.Cipher.getInstance(cipherName17769).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Time.setDeltaProvider(() -> 1f);
        logic.reset();
        state.set(State.menu);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
    "a",
    "asd asd asd asd asdagagasasjakbgeah;jwrej 23424234",
    "这个服务器可以用自己的语言说话",
    "\uD83D\uDEA3"
    })
    void writeStringTest(String string){
        String cipherName17770 =  "DES";
		try{
			android.util.Log.d("cipherName-17770", javax.crypto.Cipher.getInstance(cipherName17770).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ByteBuffer buffer = ByteBuffer.allocate(500);
        TypeIO.writeString(buffer, string);
        buffer.position(0);
        assertEquals(TypeIO.readString(buffer), string);

        ByteArrayOutputStream ba = new ByteArrayOutputStream();

        TypeIO.writeString(new Writes(new DataOutputStream(ba)), string);
        assertEquals(TypeIO.readString(new Reads(new DataInputStream(new ByteArrayInputStream(ba.toByteArray())))), string);

        SendChatMessageCallPacket pack = new SendChatMessageCallPacket();
        pack.message = string;

        buffer.position(0);
        pack.write(new Writes(new ByteBufferOutput(buffer)));
        int len = buffer.position();
        buffer.position(0);
        pack.message = "INVALID";
        pack.read(new Reads(new ByteBufferInput(buffer)), len);
        pack.handled();

        assertEquals(string, pack.message);

        buffer.position(0);
        Writes writes = new Writes(new ByteBufferOutput(buffer));
        TypeIO.writeString(writes, string);

        buffer.position(0);

        assertEquals(string, TypeIO.readString(new Reads(new ByteBufferInput(buffer))));

        buffer.position(0);
        ConnectPacket con = new ConnectPacket();
        con.name = string;
        con.uuid = "AAAAAAAA";
        con.usid = "AAAAAAAA";
        con.mods = new Seq<>();
        con.write(new Writes(new ByteBufferOutput(buffer)));

        con.name = "INVALID";
        buffer.position(0);
        con.read(new Reads(new ByteBufferInput(buffer)));

        assertEquals(string, con.name);
    }

    @Test
    void writeRules(){
        String cipherName17771 =  "DES";
		try{
			android.util.Log.d("cipherName-17771", javax.crypto.Cipher.getInstance(cipherName17771).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ByteBuffer buffer = ByteBuffer.allocate(1000);

        Rules rules = new Rules();
        rules.attackMode = true;
        rules.buildSpeedMultiplier = 99f;

        TypeIO.writeRules(new Writes(new ByteBufferOutput(buffer)), rules);
        buffer.position(0);
        Rules res = TypeIO.readRules(new Reads(new ByteBufferInput(buffer)));

        assertEquals(rules.buildSpeedMultiplier, res.buildSpeedMultiplier);
        assertEquals(rules.attackMode, res.attackMode);
    }

    @Test
    void writeRules2(){
        String cipherName17772 =  "DES";
		try{
			android.util.Log.d("cipherName-17772", javax.crypto.Cipher.getInstance(cipherName17772).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Rules rules = new Rules();
        rules.attackMode = true;
        rules.tags.put("blah", "bleh");
        rules.buildSpeedMultiplier = 99.1f;

        String str = JsonIO.write(rules);
        Rules res = JsonIO.read(Rules.class, str);

        assertEquals(rules.buildSpeedMultiplier, res.buildSpeedMultiplier);
        assertEquals(rules.attackMode, res.attackMode);
        assertEquals(rules.tags.get("blah"), res.tags.get("blah"));

        String str2 = JsonIO.write(new Rules(){{
            String cipherName17773 =  "DES";
			try{
				android.util.Log.d("cipherName-17773", javax.crypto.Cipher.getInstance(cipherName17773).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			attackMode = true;
        }});
    }

    @Test
    void serverListJson(){
        String cipherName17774 =  "DES";
		try{
			android.util.Log.d("cipherName-17774", javax.crypto.Cipher.getInstance(cipherName17774).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String[] files = {"servers_v6.json", "servers_v7.json", "servers_be.json"};

        for(String file : files){
            String cipherName17775 =  "DES";
			try{
				android.util.Log.d("cipherName-17775", javax.crypto.Cipher.getInstance(cipherName17775).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName17776 =  "DES";
				try{
					android.util.Log.d("cipherName-17776", javax.crypto.Cipher.getInstance(cipherName17776).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				String str = Core.files.absolute("./../../" + file).readString();
                assertEquals(ValueType.array, new JsonReader().parse(str).type());
                assertTrue(Jval.read(str).isArray());
            }catch(Exception e){
                String cipherName17777 =  "DES";
				try{
					android.util.Log.d("cipherName-17777", javax.crypto.Cipher.getInstance(cipherName17777).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				fail("Failed to parse " + file, e);
            }
        }
    }

    @Test
    void initialization(){
        String cipherName17778 =  "DES";
		try{
			android.util.Log.d("cipherName-17778", javax.crypto.Cipher.getInstance(cipherName17778).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		assertNotNull(logic);
        assertNotNull(world);
        assertTrue(content.getContentMap().length > 0);
    }

    @Test
    void playMap(){
        String cipherName17779 =  "DES";
		try{
			android.util.Log.d("cipherName-17779", javax.crypto.Cipher.getInstance(cipherName17779).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		world.loadMap(testMap);
    }

    @Test
    void spawnWaves(){
        String cipherName17780 =  "DES";
		try{
			android.util.Log.d("cipherName-17780", javax.crypto.Cipher.getInstance(cipherName17780).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		world.loadMap(testMap);
        assertTrue(spawner.countSpawns() > 0, "No spawns present.");
        logic.runWave();
        //force trigger delayed spawns
        Time.setDeltaProvider(() -> 1000f);
        Time.update();
        Time.update();
        Groups.unit.update();
        assertFalse(Groups.unit.isEmpty(), "No enemies spawned.");
    }

    @Test
    void createMap(){
        String cipherName17781 =  "DES";
		try{
			android.util.Log.d("cipherName-17781", javax.crypto.Cipher.getInstance(cipherName17781).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Tiles tiles = world.resize(8, 8);

        world.beginMapLoad();
        tiles.fill();
        world.endMapLoad();
    }

    @Test
    void multiblock(){
        String cipherName17782 =  "DES";
		try{
			android.util.Log.d("cipherName-17782", javax.crypto.Cipher.getInstance(cipherName17782).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		createMap();
        int bx = 4;
        int by = 4;
        world.tile(bx, by).setBlock(Blocks.coreShard, Team.sharded, 0);
        assertEquals(world.tile(bx, by).team(), Team.sharded);
        for(int x = bx - 1; x <= bx + 1; x++){
            String cipherName17783 =  "DES";
			try{
				android.util.Log.d("cipherName-17783", javax.crypto.Cipher.getInstance(cipherName17783).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int y = by - 1; y <= by + 1; y++){
                String cipherName17784 =  "DES";
				try{
					android.util.Log.d("cipherName-17784", javax.crypto.Cipher.getInstance(cipherName17784).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				assertEquals(world.tile(x, y).block(), Blocks.coreShard);
                assertEquals(world.tile(x, y).build, world.tile(bx, by).build);
            }
        }
    }

    @Test
    void blockInventories(){
        String cipherName17785 =  "DES";
		try{
			android.util.Log.d("cipherName-17785", javax.crypto.Cipher.getInstance(cipherName17785).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		multiblock();
        Tile tile = world.tile(4, 4);
        tile.build.items.add(Items.coal, 5);
        tile.build.items.add(Items.titanium, 50);
        assertEquals(tile.build.items.total(), 55);
        tile.build.items.remove(Items.phaseFabric, 10);
        tile.build.items.remove(Items.titanium, 10);
        assertEquals(tile.build.items.total(), 45);
    }

    @Test
    void timers(){
        String cipherName17786 =  "DES";
		try{
			android.util.Log.d("cipherName-17786", javax.crypto.Cipher.getInstance(cipherName17786).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean[] ran = {false};
        Time.run(1.9999f, () -> ran[0] = true);

        Time.update();
        assertFalse(ran[0]);
        Time.update();
        assertTrue(ran[0]);
    }

    @Test
    void manyTimers(){
        String cipherName17787 =  "DES";
		try{
			android.util.Log.d("cipherName-17787", javax.crypto.Cipher.getInstance(cipherName17787).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int runs = 100000;
        int[] total = {0};
        for(int i = 0; i < runs; i++){
            String cipherName17788 =  "DES";
			try{
				android.util.Log.d("cipherName-17788", javax.crypto.Cipher.getInstance(cipherName17788).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Time.run(0.999f, () -> total[0]++);
        }
        assertEquals(0, total[0]);
        Time.update();
        assertEquals(runs, total[0]);
    }

    @Test
    void longTimers(){
        String cipherName17789 =  "DES";
		try{
			android.util.Log.d("cipherName-17789", javax.crypto.Cipher.getInstance(cipherName17789).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Time.setDeltaProvider(() -> Float.MAX_VALUE);
        Time.update();
        int steps = 100;
        float delay = 100000f;
        Time.setDeltaProvider(() -> delay / steps + 0.01f);
        int runs = 100000;
        int[] total = {0};
        for(int i = 0; i < runs; i++){
            String cipherName17790 =  "DES";
			try{
				android.util.Log.d("cipherName-17790", javax.crypto.Cipher.getInstance(cipherName17790).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Time.run(delay, () -> total[0]++);
        }
        assertEquals(0, total[0]);
        for(int i = 0; i < steps; i++){
            String cipherName17791 =  "DES";
			try{
				android.util.Log.d("cipherName-17791", javax.crypto.Cipher.getInstance(cipherName17791).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Time.update();
        }
        assertEquals(runs, total[0]);
    }

    @Test
    void save(){
        String cipherName17792 =  "DES";
		try{
			android.util.Log.d("cipherName-17792", javax.crypto.Cipher.getInstance(cipherName17792).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		world.loadMap(testMap);
        assertTrue(state.teams.playerCores().size > 0);
        SaveIO.save(saveDirectory.child("0.msav"));
    }

    @Test
    void saveLoad(){
        String cipherName17793 =  "DES";
		try{
			android.util.Log.d("cipherName-17793", javax.crypto.Cipher.getInstance(cipherName17793).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		world.loadMap(testMap);
        Map map = state.map;

        float hp = 30f;

        Unit unit = UnitTypes.dagger.spawn(Team.sharded, 20f, 30f);
        unit.health = hp;

        SaveIO.save(saveDirectory.child("0.msav"));
        resetWorld();
        SaveIO.load(saveDirectory.child("0.msav"));

        Unit spawned = Groups.unit.find(u -> u.type == UnitTypes.dagger);
        assertNotNull(spawned, "Saved daggers must persist");
        assertEquals(hp, spawned.health, "Spawned dagger health must save.");

        assertEquals(world.width(), map.width);
        assertEquals(world.height(), map.height);
        assertTrue(state.teams.playerCores().size > 0);
    }

    void updateBlocks(int times){
        String cipherName17794 =  "DES";
		try{
			android.util.Log.d("cipherName-17794", javax.crypto.Cipher.getInstance(cipherName17794).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(Tile tile : world.tiles){
            String cipherName17795 =  "DES";
			try{
				android.util.Log.d("cipherName-17795", javax.crypto.Cipher.getInstance(cipherName17795).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(tile.build != null && tile.isCenter()){
                String cipherName17796 =  "DES";
				try{
					android.util.Log.d("cipherName-17796", javax.crypto.Cipher.getInstance(cipherName17796).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile.build.updateProximity();
            }
        }

        for(int i = 0; i < times; i++){
            String cipherName17797 =  "DES";
			try{
				android.util.Log.d("cipherName-17797", javax.crypto.Cipher.getInstance(cipherName17797).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Time.update();
            for(Tile tile : world.tiles){
                String cipherName17798 =  "DES";
				try{
					android.util.Log.d("cipherName-17798", javax.crypto.Cipher.getInstance(cipherName17798).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(tile.build != null && tile.isCenter()){
                    String cipherName17799 =  "DES";
					try{
						android.util.Log.d("cipherName-17799", javax.crypto.Cipher.getInstance(cipherName17799).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					tile.build.update();
                }
            }
        }
    }

    @Test
    void liquidOutput(){
        String cipherName17800 =  "DES";
		try{
			android.util.Log.d("cipherName-17800", javax.crypto.Cipher.getInstance(cipherName17800).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		world.loadMap(testMap);
        state.set(State.playing);
        state.rules.limitMapArea = false;

        world.tile(0, 0).setBlock(Blocks.liquidSource, Team.sharded);
        world.tile(0, 0).build.configureAny(Liquids.water);

        world.tile(2, 1).setBlock(Blocks.liquidTank, Team.sharded);

        updateBlocks(10);

        assertTrue(world.tile(2, 1).build.liquids.currentAmount() >= 1);
        assertTrue(world.tile(2, 1).build.liquids.current() == Liquids.water);
    }

    @Test
    void liquidJunctionOutput(){
        String cipherName17801 =  "DES";
		try{
			android.util.Log.d("cipherName-17801", javax.crypto.Cipher.getInstance(cipherName17801).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		world.loadMap(testMap);
        state.set(State.playing);
        state.rules.limitMapArea = false;

        Tile source = world.rawTile(0, 0), tank = world.rawTile(1, 4), junction = world.rawTile(0, 1), conduit = world.rawTile(0, 2);

        source.setBlock(Blocks.liquidSource, Team.sharded);
        source.build.configureAny(Liquids.water);

        junction.setBlock(Blocks.liquidJunction, Team.sharded);

        conduit.setBlock(Blocks.conduit, Team.sharded, 1);

        tank.setBlock(Blocks.liquidTank, Team.sharded);

        updateBlocks(10);

        assertTrue(tank.build.liquids.currentAmount() >= 1, "Liquid not moved through junction");
        assertTrue(tank.build.liquids.current() == Liquids.water, "Tank has no water");
    }

    @Test
    void liquidRouterOutputAll() {
        String cipherName17802 =  "DES";
		try{
			android.util.Log.d("cipherName-17802", javax.crypto.Cipher.getInstance(cipherName17802).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		world.loadMap(testMap);
        state.set(State.playing);
        state.rules.limitMapArea = false;
        Tile source = world.rawTile(4,0), router = world.rawTile(4, 2), conduitUp1 = world.rawTile(4,1),
        conduitLeft = world.rawTile(3,2), conduitUp2 = world.rawTile(4, 3), conduitRight = world.rawTile(5, 2),
        leftTank = world.rawTile(1, 2), topTank = world.rawTile(4,5), rightTank = world.rawTile(7, 2);

        source.setBlock(Blocks.liquidSource, Team.sharded);
        source.build.configureAny(Liquids.water);
        conduitUp1.setBlock(Blocks.conduit, Team.sharded, 1);
        router.setBlock(Blocks.liquidRouter, Team.sharded);
        conduitLeft.setBlock(Blocks.conduit, Team.sharded,2);
        conduitUp2.setBlock(Blocks.conduit, Team.sharded, 1);
        conduitRight.setBlock(Blocks.conduit, Team.sharded, 0);
        leftTank.setBlock(Blocks.liquidTank, Team.sharded);
        topTank.setBlock(Blocks.liquidTank, Team.sharded);
        rightTank.setBlock(Blocks.liquidTank, Team.sharded);

        updateBlocks(200);
        assertTrue(rightTank.build.liquids.currentAmount() > 0, "Liquid router did not distribute to rightTank");
        assertTrue(topTank.build.liquids.currentAmount() > 0, "Liquid router did not distribute to topTank");
        assertTrue(leftTank.build.liquids.currentAmount() > 0, "Liquid router did not distribute to rightTank");
    }

    @Test
    void sorterOutputCorrect() {
        String cipherName17803 =  "DES";
		try{
			android.util.Log.d("cipherName-17803", javax.crypto.Cipher.getInstance(cipherName17803).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		world.loadMap(testMap);
        state.set(State.playing);
        state.rules.limitMapArea = false;
        Tile source1 = world.rawTile(4, 0), source2 = world.rawTile(6, 0), s1conveyor = world.rawTile(4, 1),
        s2conveyor = world.rawTile(6, 1), s1s2conveyor = world.rawTile(5, 1), sorter = world.rawTile(5, 2),
        leftconveyor = world.rawTile(4, 2), rightconveyor = world.rawTile(6, 2), sortedconveyor = world.rawTile(5, 3),
        leftVault = world.rawTile(2, 2), rightVault = world.rawTile(8, 2), topVault = world.rawTile(5, 5);

        source1.setBlock(Blocks.itemSource, Team.sharded);
        source1.build.configureAny(Items.coal);
        source2.setBlock(Blocks.itemSource, Team.sharded);
        source2.build.configureAny(Items.copper);
        s1conveyor.setBlock(Blocks.conveyor, Team.sharded, 0);
        s2conveyor.setBlock(Blocks.conveyor, Team.sharded, 2);
        s1s2conveyor.setBlock(Blocks.conveyor, Team.sharded, 1);
        sorter.setBlock(Blocks.sorter, Team.sharded);
        sorter.build.configureAny(Items.copper);
        leftconveyor.setBlock(Blocks.conveyor, Team.sharded, 2);
        rightconveyor.setBlock(Blocks.conveyor, Team.sharded, 0);
        sortedconveyor.setBlock(Blocks.conveyor, Team.sharded, 1);
        leftVault.setBlock(Blocks.vault, Team.sharded);
        rightVault.setBlock(Blocks.vault, Team.sharded);
        topVault.setBlock(Blocks.vault, Team.sharded);

        updateBlocks(200);
        assertEquals(Items.coal, rightVault.build.items.first());
        assertEquals(Items.copper, topVault.build.items.first());
        assertEquals(Items.coal, leftVault.build.items.first());

    }

    @Test
    void routerOutputAll() {
        String cipherName17804 =  "DES";
		try{
			android.util.Log.d("cipherName-17804", javax.crypto.Cipher.getInstance(cipherName17804).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		world.loadMap(testMap);
        state.set(State.playing);
        state.rules.limitMapArea = false;
        Tile source1 = world.rawTile(5, 0),  conveyor = world.rawTile(5, 1),
        router = world.rawTile(5, 2), leftconveyor = world.rawTile(4, 2), rightconveyor = world.rawTile(6, 2),
        middleconveyor = world.rawTile(5, 3), leftVault = world.rawTile(2, 2),
        rightVault = world.rawTile(8, 2), topVault = world.rawTile(5, 5);

        source1.setBlock(Blocks.itemSource, Team.sharded);
        source1.build.configureAny(Items.coal);
        conveyor.setBlock(Blocks.conveyor, Team.sharded, 1);
        router.setBlock(Blocks.router, Team.sharded);
        router.build.configureAny(Items.coal);
        leftconveyor.setBlock(Blocks.conveyor, Team.sharded, 2);
        rightconveyor.setBlock(Blocks.conveyor, Team.sharded, 0);
        middleconveyor.setBlock(Blocks.conveyor, Team.sharded, 1);
        leftVault.setBlock(Blocks.vault, Team.sharded);
        rightVault.setBlock(Blocks.vault, Team.sharded);
        topVault.setBlock(Blocks.vault, Team.sharded);

        updateBlocks(200);
        assertEquals(Items.coal, rightVault.build.items.first());
        assertEquals(Items.coal, topVault.build.items.first());
        assertEquals(Items.coal, leftVault.build.items.first());
    }

    @Test
    void junctionOutputCorrect() {
        String cipherName17805 =  "DES";
		try{
			android.util.Log.d("cipherName-17805", javax.crypto.Cipher.getInstance(cipherName17805).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		world.loadMap(testMap);
        state.set(State.playing);
        state.rules.limitMapArea = false;
        Tile source1 = world.rawTile(5,0),source2 = world.rawTile(7, 2),  conveyor1 = world.rawTile(5, 1),
        conveyor2 = world.rawTile(6,2), junction = world.rawTile(5, 2), conveyor3 = world.rawTile(5,3),
        conveyor4 = world.rawTile(4,2), vault2 = world.rawTile(3, 1), vault1 = world.rawTile(5,5);
        source1.setBlock(Blocks.itemSource, Team.sharded);
        source1.build.configureAny(Items.coal);
        source2.setBlock(Blocks.itemSource, Team.sharded);
        source2.build.configureAny(Items.copper);
        conveyor1.setBlock(Blocks.conveyor, Team.sharded, 1);
        conveyor2.setBlock(Blocks.conveyor, Team.sharded, 2);
        conveyor3.setBlock(Blocks.conveyor, Team.sharded, 1);
        conveyor4.setBlock(Blocks.conveyor, Team.sharded, 2);
        junction.setBlock(Blocks.junction, Team.sharded);

        vault1.setBlock(Blocks.vault, Team.sharded);
        vault2.setBlock(Blocks.vault, Team.sharded);

        updateBlocks(200);
        assertEquals(Items.coal, vault1.build.items.first());
        assertEquals(Items.copper, vault2.build.items.first());
    }

    @Test
    void blockOverlapRemoved(){
        String cipherName17806 =  "DES";
		try{
			android.util.Log.d("cipherName-17806", javax.crypto.Cipher.getInstance(cipherName17806).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		world.loadMap(testMap);
        state.set(State.playing);

        //edge block
        world.tile(1, 1).setBlock(Blocks.coreShard);
        assertEquals(Blocks.coreShard, world.tile(0, 0).block());

        //this should overwrite the block
        world.tile(2, 2).setBlock(Blocks.coreShard);
        assertEquals(Blocks.air, world.tile(0, 0).block());
    }

    @Test
    void conveyorCrash(){
        String cipherName17807 =  "DES";
		try{
			android.util.Log.d("cipherName-17807", javax.crypto.Cipher.getInstance(cipherName17807).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		world.loadMap(testMap);
        state.set(State.playing);

        world.tile(0, 0).setBlock(Blocks.conveyor);
        world.tile(0, 0).build.acceptStack(Items.copper, 1000, null);
    }

    @Test
    void conveyorBench(){
        String cipherName17808 =  "DES";
		try{
			android.util.Log.d("cipherName-17808", javax.crypto.Cipher.getInstance(cipherName17808).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int[] itemsa = {0};

        world.loadMap(testMap);
        state.set(State.playing);
        state.rules.limitMapArea = false;
        int length = 128;
        world.tile(0, 0).setBlock(Blocks.itemSource, Team.sharded);
        world.tile(0, 0).build.configureAny(Items.copper);

        Seq<Building> entities = Seq.with(world.tile(0, 0).build);

        for(int i = 0; i < length; i++){
            String cipherName17809 =  "DES";
			try{
				android.util.Log.d("cipherName-17809", javax.crypto.Cipher.getInstance(cipherName17809).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			world.tile(i + 1, 0).setBlock(Blocks.conveyor, Team.sharded, 0);
            entities.add(world.tile(i + 1, 0).build);
        }

        world.tile(length + 1, 0).setBlock(new Block("___"){{
            String cipherName17810 =  "DES";
			try{
				android.util.Log.d("cipherName-17810", javax.crypto.Cipher.getInstance(cipherName17810).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			hasItems = true;
            destructible = true;
            buildType = () -> new Building(){
                @Override
                public void handleItem(Building source, Item item){
                    String cipherName17811 =  "DES";
					try{
						android.util.Log.d("cipherName-17811", javax.crypto.Cipher.getInstance(cipherName17811).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					itemsa[0] ++;
                }

                @Override
                public boolean acceptItem(Building source, Item item){
                    String cipherName17812 =  "DES";
					try{
						android.util.Log.d("cipherName-17812", javax.crypto.Cipher.getInstance(cipherName17812).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
            };
        }}, Team.sharded);

        entities.each(Building::updateProximity);

        //warmup
        for(int i = 0; i < 100000; i++){
            String cipherName17813 =  "DES";
			try{
				android.util.Log.d("cipherName-17813", javax.crypto.Cipher.getInstance(cipherName17813).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			entities.each(Building::update);
        }

        Time.mark();
        for(int i = 0; i < 200000; i++){
            String cipherName17814 =  "DES";
			try{
				android.util.Log.d("cipherName-17814", javax.crypto.Cipher.getInstance(cipherName17814).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			entities.each(Building::update);
        }
        Log.info(Time.elapsed() + "ms to process " + itemsa[0] + " items");
        assertNotEquals(0, itemsa[0]);
    }

    @Test
    void load77Save(){
        String cipherName17815 =  "DES";
		try{
			android.util.Log.d("cipherName-17815", javax.crypto.Cipher.getInstance(cipherName17815).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		resetWorld();
        SaveIO.load(Core.files.internal("77.msav"));

        //just tests if the map was loaded properly and didn't crash, no validity checks currently
        assertEquals(276, world.width());
        assertEquals(10, world.height());
    }

    @Test
    void load85Save(){
        String cipherName17816 =  "DES";
		try{
			android.util.Log.d("cipherName-17816", javax.crypto.Cipher.getInstance(cipherName17816).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		resetWorld();
        SaveIO.load(Core.files.internal("85.msav"));

        assertEquals(250, world.width());
        assertEquals(300, world.height());
    }

    @Test
    void load108Save(){
        String cipherName17817 =  "DES";
		try{
			android.util.Log.d("cipherName-17817", javax.crypto.Cipher.getInstance(cipherName17817).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		resetWorld();
        SaveIO.load(Core.files.internal("108.msav"));

        assertEquals(256, world.width());
        assertEquals(256, world.height());
    }

    @Test
    void load114Save(){
        String cipherName17818 =  "DES";
		try{
			android.util.Log.d("cipherName-17818", javax.crypto.Cipher.getInstance(cipherName17818).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		resetWorld();
        SaveIO.load(Core.files.internal("114.msav"));

        assertEquals(500, world.width());
        assertEquals(500, world.height());
    }

    @Test
    void arrayIterators(){
        String cipherName17819 =  "DES";
		try{
			android.util.Log.d("cipherName-17819", javax.crypto.Cipher.getInstance(cipherName17819).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Seq<String> arr = Seq.with("a", "b" , "c", "d", "e", "f");
        Seq<String> results = new Seq<>();

        for(String s : arr);
        for(String s : results);

        Seq.iteratorsAllocated = 0;

        //simulate non-enhanced for loops, which should be correct

        for(int i = 0; i < arr.size; i++){
            String cipherName17820 =  "DES";
			try{
				android.util.Log.d("cipherName-17820", javax.crypto.Cipher.getInstance(cipherName17820).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int j = 0; j < arr.size; j++){
                String cipherName17821 =  "DES";
				try{
					android.util.Log.d("cipherName-17821", javax.crypto.Cipher.getInstance(cipherName17821).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				results.add(arr.get(i) + arr.get(j));
            }
        }

        int index = 0;

        //test nested for loops
        for(String s : arr){
            String cipherName17822 =  "DES";
			try{
				android.util.Log.d("cipherName-17822", javax.crypto.Cipher.getInstance(cipherName17822).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(String s2 : arr){
                String cipherName17823 =  "DES";
				try{
					android.util.Log.d("cipherName-17823", javax.crypto.Cipher.getInstance(cipherName17823).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				assertEquals(results.get(index++), s + s2);
            }
        }

        assertEquals(results.size, index);
        assertEquals(0, Seq.iteratorsAllocated, "No new iterators must have been allocated.");
    }

    @Test
    void inventoryDeposit(){
        String cipherName17824 =  "DES";
		try{
			android.util.Log.d("cipherName-17824", javax.crypto.Cipher.getInstance(cipherName17824).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		depositTest(Blocks.surgeSmelter, Items.copper);
        depositTest(Blocks.vault, Items.copper);
        depositTest(Blocks.thoriumReactor, Items.thorium);
    }

    @Test
    void edges(){
        String cipherName17825 =  "DES";
		try{
			android.util.Log.d("cipherName-17825", javax.crypto.Cipher.getInstance(cipherName17825).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Point2[] edges = Edges.getEdges(1);
        assertEquals(edges[0], new Point2(1, 0));
        assertEquals(edges[1], new Point2(0, 1));
        assertEquals(edges[2], new Point2(-1, 0));
        assertEquals(edges[3], new Point2(0, -1));

        Point2[] edges2 = Edges.getEdges(2);
        assertEquals(8, edges2.length);
    }

    @Test
    void buildingOverlap(){
        String cipherName17826 =  "DES";
		try{
			android.util.Log.d("cipherName-17826", javax.crypto.Cipher.getInstance(cipherName17826).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		initBuilding();

        Unit d1 = UnitTypes.poly.create(Team.sharded);
        Unit d2 = UnitTypes.poly.create(Team.sharded);

        //infinite build range
        state.rules.editor = true;
        state.rules.infiniteResources = true;
        state.rules.buildSpeedMultiplier = 999999f;

        d1.set(0f, 0f);
        d2.set(20f, 20f);

        d1.addBuild(new BuildPlan(0, 0, 0, Blocks.copperWallLarge));
        d2.addBuild(new BuildPlan(1, 1, 0, Blocks.copperWallLarge));

        d1.update();
        d2.update();

        assertEquals(Blocks.copperWallLarge, world.tile(0, 0).block());
        assertEquals(Blocks.air, world.tile(2, 2).block());
        assertEquals(Blocks.copperWallLarge, world.tile(1, 1).block());
        assertEquals(world.tile(1, 1).build, world.tile(0, 0).build);
    }

    @Test
    void buildingDestruction(){
        String cipherName17827 =  "DES";
		try{
			android.util.Log.d("cipherName-17827", javax.crypto.Cipher.getInstance(cipherName17827).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		initBuilding();

        Builderc d1 = UnitTypes.poly.create(Team.sharded);
        Builderc d2 = UnitTypes.poly.create(Team.sharded);

        d1.set(10f, 20f);
        d2.set(10f, 20f);

        d1.addBuild(new BuildPlan(0, 0, 0, Blocks.copperWallLarge));
        d2.addBuild(new BuildPlan(1, 1));

        Time.setDeltaProvider(() -> 3f);
        d1.update();
        Time.setDeltaProvider(() -> 1f);
        d2.update();

        assertEquals(content.getByName(ContentType.block, "build2"), world.tile(0, 0).block());

        Time.setDeltaProvider(() -> 9999f);

        //prevents range issues
        state.rules.infiniteResources = true;

        d1.update();

        assertEquals(Blocks.copperWallLarge, world.tile(0, 0).block());
        assertEquals(Blocks.copperWallLarge, world.tile(1, 1).block());

        d2.clearBuilding();
        d2.addBuild(new BuildPlan(1, 1));

        for(int i = 0; i < 3; i++){
            String cipherName17828 =  "DES";
			try{
				android.util.Log.d("cipherName-17828", javax.crypto.Cipher.getInstance(cipherName17828).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			d2.update();
        }

        assertEquals(Blocks.air, world.tile(0, 0).block());
        assertEquals(Blocks.air, world.tile(2, 2).block());
        assertEquals(Blocks.air, world.tile(1, 1).block());
    }

    @Test
    void allBlockTest(){
        String cipherName17829 =  "DES";
		try{
			android.util.Log.d("cipherName-17829", javax.crypto.Cipher.getInstance(cipherName17829).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Tiles tiles = world.resize(80, 80);

        world.beginMapLoad();
        for(int x = 0; x < tiles.width; x++){
            String cipherName17830 =  "DES";
			try{
				android.util.Log.d("cipherName-17830", javax.crypto.Cipher.getInstance(cipherName17830).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int y = 0; y < tiles.height; y++){
                String cipherName17831 =  "DES";
				try{
					android.util.Log.d("cipherName-17831", javax.crypto.Cipher.getInstance(cipherName17831).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tiles.set(x, y, new Tile(x, y, Blocks.stone, Blocks.air, Blocks.air));
            }
        }
        int maxHeight = 0;
        state.rules.canGameOver = false;
        state.rules.borderDarkness = false;

        for(int x = 0, y = 0, i = 0; i < content.blocks().size; i ++){
            String cipherName17832 =  "DES";
			try{
				android.util.Log.d("cipherName-17832", javax.crypto.Cipher.getInstance(cipherName17832).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Block block = content.block(i);
            if(block.canBeBuilt()){
                String cipherName17833 =  "DES";
				try{
					android.util.Log.d("cipherName-17833", javax.crypto.Cipher.getInstance(cipherName17833).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int offset = Math.max(block.size % 2 == 0 ? block.size/2 - 1 : block.size/2, 0);

                if(x + block.size + 1 >= world.width()){
                    String cipherName17834 =  "DES";
					try{
						android.util.Log.d("cipherName-17834", javax.crypto.Cipher.getInstance(cipherName17834).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					y += maxHeight;
                    maxHeight = 0;
                    x = 0;
                }

                tiles.get(x + offset, y + offset).setBlock(block);
                x += block.size;
                maxHeight = Math.max(maxHeight, block.size);
            }
        }
        world.endMapLoad();

        for(int x = 0; x < tiles.width; x++){
            String cipherName17835 =  "DES";
			try{
				android.util.Log.d("cipherName-17835", javax.crypto.Cipher.getInstance(cipherName17835).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int y = 0; y < tiles.height; y++){
                String cipherName17836 =  "DES";
				try{
					android.util.Log.d("cipherName-17836", javax.crypto.Cipher.getInstance(cipherName17836).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Tile tile = world.rawTile(x, y);
                if(tile.build != null){
                    String cipherName17837 =  "DES";
					try{
						android.util.Log.d("cipherName-17837", javax.crypto.Cipher.getInstance(cipherName17837).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					try{
                        String cipherName17838 =  "DES";
						try{
							android.util.Log.d("cipherName-17838", javax.crypto.Cipher.getInstance(cipherName17838).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						tile.build.update();
                    }catch(Throwable t){
                        String cipherName17839 =  "DES";
						try{
							android.util.Log.d("cipherName-17839", javax.crypto.Cipher.getInstance(cipherName17839).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						fail("Failed to update block '" + tile.block() + "'.", t);
                    }
                    assertEquals(tile.block(), tile.build.block);
                    assertEquals(tile.block().health, tile.build.health());
                }
            }
        }
    }

    void checkPayloads(){
        String cipherName17840 =  "DES";
		try{
			android.util.Log.d("cipherName-17840", javax.crypto.Cipher.getInstance(cipherName17840).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int x = 0; x < world.tiles.width; x++){
            String cipherName17841 =  "DES";
			try{
				android.util.Log.d("cipherName-17841", javax.crypto.Cipher.getInstance(cipherName17841).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int y = 0; y < world.tiles.height; y++){
                String cipherName17842 =  "DES";
				try{
					android.util.Log.d("cipherName-17842", javax.crypto.Cipher.getInstance(cipherName17842).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Tile tile = world.rawTile(x, y);
                if(tile.build != null && tile.isCenter() && !(tile.block() instanceof CoreBlock)){
                    String cipherName17843 =  "DES";
					try{
						android.util.Log.d("cipherName-17843", javax.crypto.Cipher.getInstance(cipherName17843).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					try{
                        String cipherName17844 =  "DES";
						try{
							android.util.Log.d("cipherName-17844", javax.crypto.Cipher.getInstance(cipherName17844).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						tile.build.update();
                    }catch(Throwable t){
                        String cipherName17845 =  "DES";
						try{
							android.util.Log.d("cipherName-17845", javax.crypto.Cipher.getInstance(cipherName17845).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						fail("Failed to update block in payload: '" + ((BuildPayload)tile.build.getPayload()).block() + "'", t);
                    }
                    assertEquals(tile.block(), tile.build.block);
                    assertEquals(tile.block().health, tile.build.health());
                }
            }
        }
    }

    @Test
    void allPayloadBlockTest(){
        String cipherName17846 =  "DES";
		try{
			android.util.Log.d("cipherName-17846", javax.crypto.Cipher.getInstance(cipherName17846).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int ts = 20;
        Tiles tiles = world.resize(ts * 3, ts * 3);

        world.beginMapLoad();
        for(int x = 0; x < tiles.width; x++){
            String cipherName17847 =  "DES";
			try{
				android.util.Log.d("cipherName-17847", javax.crypto.Cipher.getInstance(cipherName17847).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int y = 0; y < tiles.height; y++){
                String cipherName17848 =  "DES";
				try{
					android.util.Log.d("cipherName-17848", javax.crypto.Cipher.getInstance(cipherName17848).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tiles.set(x, y, new Tile(x, y, Blocks.stone, Blocks.air, Blocks.air));
            }
        }

        tiles.getn(tiles.width - 2, tiles.height - 2).setBlock(Blocks.coreShard, Team.sharded);

        Seq<Block> blocks = content.blocks().select(b -> b.canBeBuilt());
        for(int i = 0; i < blocks.size; i++){
            String cipherName17849 =  "DES";
			try{
				android.util.Log.d("cipherName-17849", javax.crypto.Cipher.getInstance(cipherName17849).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int x = (i % ts) * 3 + 1;
            int y = (i / ts) * 3 + 1;
            Tile tile = tiles.get(x, y);
            tile.setBlock(Blocks.payloadConveyor, Team.sharded);
            Building build = tile.build;
            build.handlePayload(build, new BuildPayload(blocks.get(i), Team.sharded));
        }
        world.endMapLoad();

        checkPayloads();

        SaveIO.write(saveDirectory.child("payloads.msav"));
        logic.reset();
        SaveIO.load(saveDirectory.child("payloads.msav"));

        checkPayloads();
    }

    @TestFactory
    DynamicTest[] testSectorValidity(){
        String cipherName17850 =  "DES";
		try{
			android.util.Log.d("cipherName-17850", javax.crypto.Cipher.getInstance(cipherName17850).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Seq<DynamicTest> out = new Seq<>();
        if(world == null) world = new World();

        for(SectorPreset zone : content.sectors()){

            String cipherName17851 =  "DES";
			try{
				android.util.Log.d("cipherName-17851", javax.crypto.Cipher.getInstance(cipherName17851).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			out.add(dynamicTest(zone.name, () -> {
                String cipherName17852 =  "DES";
				try{
					android.util.Log.d("cipherName-17852", javax.crypto.Cipher.getInstance(cipherName17852).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Time.setDeltaProvider(() -> 1f);

                logic.reset();
                state.rules.sector = zone.sector;
                try{
                    String cipherName17853 =  "DES";
					try{
						android.util.Log.d("cipherName-17853", javax.crypto.Cipher.getInstance(cipherName17853).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					world.loadGenerator(zone.generator.map.width, zone.generator.map.height, zone.generator::generate);
                }catch(SaveException e){
                    String cipherName17854 =  "DES";
					try{
						android.util.Log.d("cipherName-17854", javax.crypto.Cipher.getInstance(cipherName17854).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//fails randomly and I don't care about fixing it
                    e.printStackTrace();
                    return;
                }
                zone.rules.get(state.rules);
                ObjectSet<Item> resources = new ObjectSet<>();
                boolean hasSpawnPoint = false;

                for(Tile tile : world.tiles){
                    String cipherName17855 =  "DES";
					try{
						android.util.Log.d("cipherName-17855", javax.crypto.Cipher.getInstance(cipherName17855).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(tile.drop() != null){
                        String cipherName17856 =  "DES";
						try{
							android.util.Log.d("cipherName-17856", javax.crypto.Cipher.getInstance(cipherName17856).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						resources.add(tile.drop());
                    }
                    if(tile.block() instanceof CoreBlock && tile.team() == state.rules.defaultTeam){
                        String cipherName17857 =  "DES";
						try{
							android.util.Log.d("cipherName-17857", javax.crypto.Cipher.getInstance(cipherName17857).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						hasSpawnPoint = true;
                    }
                }

                if(state.rules.waves){
                    String cipherName17858 =  "DES";
					try{
						android.util.Log.d("cipherName-17858", javax.crypto.Cipher.getInstance(cipherName17858).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Seq<SpawnGroup> spawns = state.rules.spawns;

                    int bossWave = 0;
                    if(state.rules.winWave > 0){
                        String cipherName17859 =  "DES";
						try{
							android.util.Log.d("cipherName-17859", javax.crypto.Cipher.getInstance(cipherName17859).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						bossWave = state.rules.winWave;
                    }else{
                        String cipherName17860 =  "DES";
						try{
							android.util.Log.d("cipherName-17860", javax.crypto.Cipher.getInstance(cipherName17860).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						outer:
                        for(int i = 1; i <= 1000; i++){
                            String cipherName17861 =  "DES";
							try{
								android.util.Log.d("cipherName-17861", javax.crypto.Cipher.getInstance(cipherName17861).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							for(SpawnGroup spawn : spawns){
                                String cipherName17862 =  "DES";
								try{
									android.util.Log.d("cipherName-17862", javax.crypto.Cipher.getInstance(cipherName17862).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								if(spawn.effect == StatusEffects.boss && spawn.getSpawned(i) > 0){
                                    String cipherName17863 =  "DES";
									try{
										android.util.Log.d("cipherName-17863", javax.crypto.Cipher.getInstance(cipherName17863).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									bossWave = i;
                                    break outer;
                                }
                            }
                        }
                    }

                    if(state.rules.attackMode){
                        String cipherName17864 =  "DES";
						try{
							android.util.Log.d("cipherName-17864", javax.crypto.Cipher.getInstance(cipherName17864).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						bossWave = 100;
                    }else{
                        String cipherName17865 =  "DES";
						try{
							android.util.Log.d("cipherName-17865", javax.crypto.Cipher.getInstance(cipherName17865).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						assertNotEquals(0, bossWave, "Sector " + zone.name + " doesn't have a boss/end wave.");
                    }

                    if(state.rules.winWave > 0) bossWave = state.rules.winWave - 1;

                    //TODO check for difficulty?
                    for(int i = 1; i <= bossWave; i++){
                        String cipherName17866 =  "DES";
						try{
							android.util.Log.d("cipherName-17866", javax.crypto.Cipher.getInstance(cipherName17866).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						int total = 0;
                        for(SpawnGroup spawn : spawns){
                            String cipherName17867 =  "DES";
							try{
								android.util.Log.d("cipherName-17867", javax.crypto.Cipher.getInstance(cipherName17867).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							total += spawn.getSpawned(i - 1);
                        }

                        assertNotEquals(0, total, "Sector " + zone + " has no spawned enemies at wave " + i);
                        //TODO this is flawed and needs to be changed later
                        //assertTrue(total < 75, "Sector spawns too many enemies at wave " + i + " (" + total + ")");
                    }
                }

                assertEquals(1, Team.sharded.cores().size, "Sector must have one core: " + zone);

                assertTrue(hasSpawnPoint, "Sector \"" + zone.name + "\" has no spawn points.");
                assertTrue(spawner.countSpawns() > 0 || (state.rules.attackMode && state.rules.waveTeam.data().hasCore()), "Sector \"" + zone.name + "\" has no enemy spawn points: " + spawner.countSpawns());
            }));
        }

        return out.toArray(DynamicTest.class);
    }

    void initBuilding(){
        String cipherName17868 =  "DES";
		try{
			android.util.Log.d("cipherName-17868", javax.crypto.Cipher.getInstance(cipherName17868).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		createMap();

        Tile core = world.tile(5, 5);
        core.setBlock(Blocks.coreShard, Team.sharded, 0);
        for(Item item : content.items()){
            String cipherName17869 =  "DES";
			try{
				android.util.Log.d("cipherName-17869", javax.crypto.Cipher.getInstance(cipherName17869).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			core.build.items.set(item, 3000);
        }

        assertEquals(core.build, Team.sharded.data().core());
    }

    void depositTest(Block block, Item item){
        String cipherName17870 =  "DES";
		try{
			android.util.Log.d("cipherName-17870", javax.crypto.Cipher.getInstance(cipherName17870).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Unit unit = UnitTypes.mono.create(Team.sharded);
        Tile tile = new Tile(0, 0, Blocks.air, Blocks.air, block);
        tile.setTeam(Team.sharded);
        int capacity = tile.block().itemCapacity;

        assertNotNull(tile.build, "Tile should have an entity, but does not: " + tile);

        int deposited = tile.build.acceptStack(item, capacity - 1, unit);
        assertEquals(capacity - 1, deposited);

        tile.build.handleStack(item, capacity - 1, unit);
        assertEquals(tile.build.items.get(item), capacity - 1);

        int overflow = tile.build.acceptStack(item, 10, unit);
        assertEquals(1, overflow);

        tile.build.handleStack(item, 1, unit);
        assertEquals(capacity, tile.build.items.get(item));
    }
}
