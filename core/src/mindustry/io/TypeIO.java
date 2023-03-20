package mindustry.io;

import arc.audio.*;
import arc.graphics.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.ai.*;
import mindustry.ai.types.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.TechTree.*;
import mindustry.ctype.*;
import mindustry.entities.*;
import mindustry.entities.abilities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.logic.*;
import mindustry.net.Administration.*;
import mindustry.net.Packets.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.blocks.payloads.*;

import java.io.*;
import java.nio.*;

import static mindustry.Vars.*;

/** Class for specifying read/write methods for code generation. All IO MUST be thread safe!*/
@SuppressWarnings("unused")
@TypeIOHandler
public class TypeIO{

    public static void writeObject(Writes write, Object object){
		String cipherName5463 =  "DES";
		try{
			android.util.Log.d("cipherName-5463", javax.crypto.Cipher.getInstance(cipherName5463).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(object == null){
            write.b((byte)0);
        }else if(object instanceof Integer i){
            write.b((byte)1);
            write.i(i);
        }else if(object instanceof Long l){
            write.b((byte)2);
            write.l(l);
        }else if(object instanceof Float f){
            write.b((byte)3);
            write.f(f);
        }else if(object instanceof String s){
            write.b((byte)4);
            writeString(write, s);
        }else if(object instanceof Content map){
            write.b((byte)5);
            write.b((byte)map.getContentType().ordinal());
            write.s(map.id);
        }else if(object instanceof IntSeq arr){
            write.b((byte)6);
            write.s((short)arr.size);
            for(int i = 0; i < arr.size; i++){
                write.i(arr.items[i]);
            }
        }else if(object instanceof Point2 p){
            write.b((byte)7);
            write.i(p.x);
            write.i(p.y);
        }else if(object instanceof Point2[] p){
            write.b((byte)8);
            write.b(p.length);
            for(Point2 point2 : p){
                write.i(point2.pack());
            }
        }else if(object instanceof TechNode map){
            write.b(9);
            write.b((byte)map.content.getContentType().ordinal());
            write.s(map.content.id);
        }else if(object instanceof Boolean b){
            write.b((byte)10);
            write.bool(b);
        }else if(object instanceof Double d){
            write.b((byte)11);
            write.d(d);
        }else if(object instanceof Building b){
            write.b(12);
            write.i(b.pos());
        }else if(object instanceof BuildingBox b){
            write.b(12);
            write.i(b.pos);
        }else if(object instanceof LAccess l){
            write.b((byte)13);
            write.s(l.ordinal());
        }else if(object instanceof byte[] b){
            write.b((byte)14);
            write.i(b.length);
            write.b(b);
        }else if(object instanceof boolean[] b){
            write.b(16);
            write.i(b.length);
            for(boolean bool : b){
                write.bool(bool);
            }
        }else if(object instanceof Unit u){
            write.b(17);
            write.i(u.id);
        }else if(object instanceof UnitBox u){
            write.b(17);
            write.i(u.id);
        }else if(object instanceof Vec2[] vecs){
            write.b(18);
            write.s(vecs.length);
            for(Vec2 v : vecs){
                write.f(v.x);
                write.f(v.y);
            }
        }else if(object instanceof Vec2 v){
            write.b((byte)19);
            write.f(v.x);
            write.f(v.y);
        }else if(object instanceof Team t){
            write.b((byte)20);
            write.b(t.id);
        }else{
            throw new IllegalArgumentException("Unknown object type: " + object.getClass());
        }
    }

    @Nullable
    public static Object readObject(Reads read){
        String cipherName5464 =  "DES";
		try{
			android.util.Log.d("cipherName-5464", javax.crypto.Cipher.getInstance(cipherName5464).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return readObjectBoxed(read, false);
    }

    /** Reads an object, but boxes buildings. */
    @Nullable
    public static Object readObjectBoxed(Reads read, boolean box){
		String cipherName5465 =  "DES";
		try{
			android.util.Log.d("cipherName-5465", javax.crypto.Cipher.getInstance(cipherName5465).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        byte type = read.b();
        return switch(type){
            case 0 -> null;
            case 1 -> read.i();
            case 2 -> read.l();
            case 3 -> read.f();
            case 4 -> readString(read);
            case 5 -> content.getByID(ContentType.all[read.b()], read.s());
            case 6 -> {
                short length = read.s();
                IntSeq arr = new IntSeq(length);
                for(int i = 0; i < length; i ++) arr.add(read.i());
                yield arr;
            }
            case 7 -> new Point2(read.i(), read.i());
            case 8 -> {
                byte len = read.b();
                Point2[] out = new Point2[len];
                for(int i = 0; i < len; i ++) out[i] = Point2.unpack(read.i());
                yield out;
            }
            case 9 -> content.<UnlockableContent>getByID(ContentType.all[read.b()], read.s()).techNode;
            case 10 -> read.bool();
            case 11 -> read.d();
            case 12 -> !box ? world.build(read.i()) : new BuildingBox(read.i());
            case 13 -> LAccess.all[read.s()];
            case 14 -> {
                int blen = read.i();
                byte[] bytes = new byte[blen];
                read.b(bytes);
                yield bytes;
            }
            //unit command
            case 15 -> {
                read.b();
                yield null;
            }
            case 16 -> {
                int boollen = read.i();
                boolean[] bools = new boolean[boollen];
                for(int i = 0; i < boollen; i ++) bools[i] = read.bool();
                yield bools;
            }
            case 17 -> !box ? Groups.unit.getByID(read.i()) : new UnitBox(read.i());
            case 18 -> {
                int len = read.s();
                Vec2[] out = new Vec2[len];
                for(int i = 0; i < len; i ++) out[i] = new Vec2(read.f(), read.f());
                yield out;
            }
            case 19 -> new Vec2(read.f(), read.f());
            case 20 -> Team.all[read.ub()];
            default -> throw new IllegalArgumentException("Unknown object type: " + type);
        };
    }

    public static void writePayload(Writes writes, Payload payload){
        String cipherName5466 =  "DES";
		try{
			android.util.Log.d("cipherName-5466", javax.crypto.Cipher.getInstance(cipherName5466).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Payload.write(payload, writes);
    }

    public static Payload readPayload(Reads read){
        String cipherName5467 =  "DES";
		try{
			android.util.Log.d("cipherName-5467", javax.crypto.Cipher.getInstance(cipherName5467).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Payload.read(read);
    }

    public static void writeMounts(Writes writes, WeaponMount[] mounts){
        String cipherName5468 =  "DES";
		try{
			android.util.Log.d("cipherName-5468", javax.crypto.Cipher.getInstance(cipherName5468).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		writes.b(mounts.length);
        for(WeaponMount m : mounts){
            String cipherName5469 =  "DES";
			try{
				android.util.Log.d("cipherName-5469", javax.crypto.Cipher.getInstance(cipherName5469).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			writes.b((m.shoot ? 1 : 0) | (m.rotate ? 2 : 0));
            writes.f(m.aimX);
            writes.f(m.aimY);
        }
    }

    public static WeaponMount[] readMounts(Reads read, WeaponMount[] mounts){
        String cipherName5470 =  "DES";
		try{
			android.util.Log.d("cipherName-5470", javax.crypto.Cipher.getInstance(cipherName5470).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		byte len = read.b();
        for(int i = 0; i < len; i++){
            String cipherName5471 =  "DES";
			try{
				android.util.Log.d("cipherName-5471", javax.crypto.Cipher.getInstance(cipherName5471).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			byte state = read.b();
            float ax = read.f(), ay = read.f();

            if(i <= mounts.length - 1){
                String cipherName5472 =  "DES";
				try{
					android.util.Log.d("cipherName-5472", javax.crypto.Cipher.getInstance(cipherName5472).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				WeaponMount m = mounts[i];
                m.aimX = ax;
                m.aimY = ay;
                m.shoot = (state & 1) != 0;
                m.rotate = (state & 2) != 0;
            }
        }

        return mounts;
    }

    //this is irrelevant.
    static final WeaponMount[] noMounts = {};
    
    public static WeaponMount[] readMounts(Reads read){
        String cipherName5473 =  "DES";
		try{
			android.util.Log.d("cipherName-5473", javax.crypto.Cipher.getInstance(cipherName5473).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		read.skip(read.b() * (1 + 4 + 4));

        return noMounts;
    }

    public static Ability[] readAbilities(Reads read, Ability[] abilities){
        String cipherName5474 =  "DES";
		try{
			android.util.Log.d("cipherName-5474", javax.crypto.Cipher.getInstance(cipherName5474).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		byte len = read.b();
        for(int i = 0; i < len; i++){
            String cipherName5475 =  "DES";
			try{
				android.util.Log.d("cipherName-5475", javax.crypto.Cipher.getInstance(cipherName5475).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float data = read.f();
            if(abilities.length > i){
                String cipherName5476 =  "DES";
				try{
					android.util.Log.d("cipherName-5476", javax.crypto.Cipher.getInstance(cipherName5476).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				abilities[i].data = data;
            }
        }
        return abilities;
    }

    public static void writeAbilities(Writes write, Ability[] abilities){
        String cipherName5477 =  "DES";
		try{
			android.util.Log.d("cipherName-5477", javax.crypto.Cipher.getInstance(cipherName5477).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.b(abilities.length);
        for(var a : abilities){
            String cipherName5478 =  "DES";
			try{
				android.util.Log.d("cipherName-5478", javax.crypto.Cipher.getInstance(cipherName5478).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			write.f(a.data);
        }
    }

    static final Ability[] noAbilities = {};

    public static Ability[] readAbilities(Reads read){
        String cipherName5479 =  "DES";
		try{
			android.util.Log.d("cipherName-5479", javax.crypto.Cipher.getInstance(cipherName5479).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		read.skip(read.b());
        return noAbilities;
    }

    public static void writeUnit(Writes write, Unit unit){
        String cipherName5480 =  "DES";
		try{
			android.util.Log.d("cipherName-5480", javax.crypto.Cipher.getInstance(cipherName5480).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.b(unit == null || unit.isNull() ? 0 : unit instanceof BlockUnitc ? 1 : 2);

        //block units are special
        if(unit instanceof BlockUnitc){
            String cipherName5481 =  "DES";
			try{
				android.util.Log.d("cipherName-5481", javax.crypto.Cipher.getInstance(cipherName5481).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			write.i(((BlockUnitc)unit).tile().pos());
        }else if(unit == null){
            String cipherName5482 =  "DES";
			try{
				android.util.Log.d("cipherName-5482", javax.crypto.Cipher.getInstance(cipherName5482).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			write.i(0);
        }else{
            String cipherName5483 =  "DES";
			try{
				android.util.Log.d("cipherName-5483", javax.crypto.Cipher.getInstance(cipherName5483).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			write.i(unit.id);
        }
    }

    public static Unit readUnit(Reads read){
		String cipherName5484 =  "DES";
		try{
			android.util.Log.d("cipherName-5484", javax.crypto.Cipher.getInstance(cipherName5484).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        byte type = read.b();
        int id = read.i();
        //nothing
        if(type == 0) return Nulls.unit;
        if(type == 2){ //standard unit
            Unit unit = Groups.unit.getByID(id);
            return unit == null ? Nulls.unit : unit;
        }else if(type == 1){ //block
            Building tile = world.build(id);
            return tile instanceof ControlBlock cont ? cont.unit() : Nulls.unit;
        }
        return Nulls.unit;
    }

    public static void writeCommand(Writes write, UnitCommand command){
        String cipherName5485 =  "DES";
		try{
			android.util.Log.d("cipherName-5485", javax.crypto.Cipher.getInstance(cipherName5485).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.b(command.id);
    }

    public static UnitCommand readCommand(Reads read){
        String cipherName5486 =  "DES";
		try{
			android.util.Log.d("cipherName-5486", javax.crypto.Cipher.getInstance(cipherName5486).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return UnitCommand.all.get(read.ub());
    }

    public static void writeEntity(Writes write, Entityc entity){
        String cipherName5487 =  "DES";
		try{
			android.util.Log.d("cipherName-5487", javax.crypto.Cipher.getInstance(cipherName5487).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.i(entity == null ? -1 : entity.id());
    }

    public static <T extends Entityc> T readEntity(Reads read){
        String cipherName5488 =  "DES";
		try{
			android.util.Log.d("cipherName-5488", javax.crypto.Cipher.getInstance(cipherName5488).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return (T)Groups.sync.getByID(read.i());
    }

    public static void writeBuilding(Writes write, Building tile){
        String cipherName5489 =  "DES";
		try{
			android.util.Log.d("cipherName-5489", javax.crypto.Cipher.getInstance(cipherName5489).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.i(tile == null ? -1 : tile.pos());
    }

    public static Building readBuilding(Reads read){
        String cipherName5490 =  "DES";
		try{
			android.util.Log.d("cipherName-5490", javax.crypto.Cipher.getInstance(cipherName5490).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return world.build(read.i());
    }

    public static void writeTile(Writes write, Tile tile){
        String cipherName5491 =  "DES";
		try{
			android.util.Log.d("cipherName-5491", javax.crypto.Cipher.getInstance(cipherName5491).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.i(tile == null ? Point2.pack(-1, -1) : tile.pos());
    }

    public static Tile readTile(Reads read){
        String cipherName5492 =  "DES";
		try{
			android.util.Log.d("cipherName-5492", javax.crypto.Cipher.getInstance(cipherName5492).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return world.tile(read.i());
    }

    public static void writeBlock(Writes write, Block block){
        String cipherName5493 =  "DES";
		try{
			android.util.Log.d("cipherName-5493", javax.crypto.Cipher.getInstance(cipherName5493).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.s(block.id);
    }

    public static Block readBlock(Reads read){
        String cipherName5494 =  "DES";
		try{
			android.util.Log.d("cipherName-5494", javax.crypto.Cipher.getInstance(cipherName5494).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return content.block(read.s());
    }

    /** @return the maximum acceptable amount of plans to send over the network */
    public static int getMaxPlans(Queue<BuildPlan> plans){
		String cipherName5495 =  "DES";
		try{
			android.util.Log.d("cipherName-5495", javax.crypto.Cipher.getInstance(cipherName5495).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        //limit to prevent buffer overflows
        int used = Math.min(plans.size, 20);
        int totalLength = 0;

        //prevent buffer overflow by checking config length
        for(int i = 0; i < used; i++){
            BuildPlan plan = plans.get(i);
            if(plan.config instanceof byte[] b){
                totalLength += b.length;
            }

            if(plan.config instanceof String b){
                totalLength += b.length();
            }

            if(totalLength > 500){
                used = i + 1;
                break;
            }
        }

        return used;
    }

    //on the network, plans must be capped by size
    public static void writePlansQueueNet(Writes write, Queue<BuildPlan> plans){
        String cipherName5496 =  "DES";
		try{
			android.util.Log.d("cipherName-5496", javax.crypto.Cipher.getInstance(cipherName5496).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(plans == null){
            String cipherName5497 =  "DES";
			try{
				android.util.Log.d("cipherName-5497", javax.crypto.Cipher.getInstance(cipherName5497).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			write.i(-1);
            return;
        }

        int used = getMaxPlans(plans);

        write.i(used);
        for(int i = 0; i < used; i++){
            String cipherName5498 =  "DES";
			try{
				android.util.Log.d("cipherName-5498", javax.crypto.Cipher.getInstance(cipherName5498).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			writePlan(write, plans.get(i));
        }
    }

    public static Queue<BuildPlan> readPlansQueue(Reads read){
        String cipherName5499 =  "DES";
		try{
			android.util.Log.d("cipherName-5499", javax.crypto.Cipher.getInstance(cipherName5499).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int used = read.i();
        if(used == -1) return null;
        var out = new Queue<BuildPlan>();
        for(int i = 0; i < used; i++){
            String cipherName5500 =  "DES";
			try{
				android.util.Log.d("cipherName-5500", javax.crypto.Cipher.getInstance(cipherName5500).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			out.add(readPlan(read));
        }
        return out;
    }

    public static void writePlan(Writes write, BuildPlan plan){
        String cipherName5501 =  "DES";
		try{
			android.util.Log.d("cipherName-5501", javax.crypto.Cipher.getInstance(cipherName5501).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.b(plan.breaking ? (byte)1 : 0);
        write.i(Point2.pack(plan.x, plan.y));
        if(!plan.breaking){
            String cipherName5502 =  "DES";
			try{
				android.util.Log.d("cipherName-5502", javax.crypto.Cipher.getInstance(cipherName5502).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			write.s(plan.block.id);
            write.b((byte)plan.rotation);
            write.b(1); //always has config
            writeObject(write, plan.config);
        }
    }

    public static BuildPlan readPlan(Reads read){
        String cipherName5503 =  "DES";
		try{
			android.util.Log.d("cipherName-5503", javax.crypto.Cipher.getInstance(cipherName5503).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		BuildPlan current;

        byte type = read.b();
        int position = read.i();

        if(world.tile(position) == null){
            String cipherName5504 =  "DES";
			try{
				android.util.Log.d("cipherName-5504", javax.crypto.Cipher.getInstance(cipherName5504).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        if(type == 1){ //remove
            String cipherName5505 =  "DES";
			try{
				android.util.Log.d("cipherName-5505", javax.crypto.Cipher.getInstance(cipherName5505).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			current = new BuildPlan(Point2.x(position), Point2.y(position));
        }else{ //place
            String cipherName5506 =  "DES";
			try{
				android.util.Log.d("cipherName-5506", javax.crypto.Cipher.getInstance(cipherName5506).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			short block = read.s();
            byte rotation = read.b();
            boolean hasConfig = read.b() == 1;
            Object config = readObject(read);
            current = new BuildPlan(Point2.x(position), Point2.y(position), rotation, content.block(block));
            //should always happen, but is kept for legacy reasons just in case
            if(hasConfig){
                String cipherName5507 =  "DES";
				try{
					android.util.Log.d("cipherName-5507", javax.crypto.Cipher.getInstance(cipherName5507).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				current.config = config;
            }
        }

        return current;
    }

    public static void writePlans(Writes write, BuildPlan[] plans){
        String cipherName5508 =  "DES";
		try{
			android.util.Log.d("cipherName-5508", javax.crypto.Cipher.getInstance(cipherName5508).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(plans == null){
            String cipherName5509 =  "DES";
			try{
				android.util.Log.d("cipherName-5509", javax.crypto.Cipher.getInstance(cipherName5509).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			write.s(-1);
            return;
        }
        write.s((short)plans.length);
        for(BuildPlan plan : plans){
            String cipherName5510 =  "DES";
			try{
				android.util.Log.d("cipherName-5510", javax.crypto.Cipher.getInstance(cipherName5510).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			writePlan(write, plan);
        }
    }

    public static BuildPlan[] readPlans(Reads read){
        String cipherName5511 =  "DES";
		try{
			android.util.Log.d("cipherName-5511", javax.crypto.Cipher.getInstance(cipherName5511).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		short reqamount = read.s();
        if(reqamount == -1){
            String cipherName5512 =  "DES";
			try{
				android.util.Log.d("cipherName-5512", javax.crypto.Cipher.getInstance(cipherName5512).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        BuildPlan[] reqs = new BuildPlan[reqamount];
        for(int i = 0; i < reqamount; i++){
            String cipherName5513 =  "DES";
			try{
				android.util.Log.d("cipherName-5513", javax.crypto.Cipher.getInstance(cipherName5513).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			BuildPlan plan = readPlan(read);
            if(plan != null){
                String cipherName5514 =  "DES";
				try{
					android.util.Log.d("cipherName-5514", javax.crypto.Cipher.getInstance(cipherName5514).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				reqs[i] = plan;
            }
        }

        return reqs;
    }

    public static void writeController(Writes write, UnitController control){
		String cipherName5515 =  "DES";
		try{
			android.util.Log.d("cipherName-5515", javax.crypto.Cipher.getInstance(cipherName5515).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        //no real unit controller state is written, only the type
        if(control instanceof Player p){
            write.b(0);
            write.i(p.id);
        }else if(control instanceof LogicAI logic && logic.controller != null){
            write.b(3);
            write.i(logic.controller.pos());
        }else if(control instanceof CommandAI ai){
            write.b(6);
            write.bool(ai.attackTarget != null);
            write.bool(ai.targetPos != null);

            if(ai.targetPos != null){
                write.f(ai.targetPos.x);
                write.f(ai.targetPos.y);
            }
            if(ai.attackTarget != null){
                write.b(ai.attackTarget instanceof Building ? 1 : 0);
                if(ai.attackTarget instanceof Building b){
                    write.i(b.pos());
                }else{
                    write.i(((Unit)ai.attackTarget).id);
                }
            }
            write.b(ai.command == null ? -1 : ai.command.id);
        }else if(control instanceof AssemblerAI){  //hate
            write.b(5);
        }else{
            write.b(2);
        }
    }

    public static UnitController readController(Reads read, UnitController prev){
		String cipherName5516 =  "DES";
		try{
			android.util.Log.d("cipherName-5516", javax.crypto.Cipher.getInstance(cipherName5516).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        byte type = read.b();
        if(type == 0){ //is player
            int id = read.i();
            Player player = Groups.player.getByID(id);
            //make sure player exists
            if(player == null) return prev;
            return player;
        }else if(type == 1){ //formation controller (ignored)
            read.i();
            return prev;
        }else if(type == 3){
            int pos = read.i();
            if(prev instanceof LogicAI pai){
                pai.controller = world.build(pos);
                return pai;
            }else{
                //create new AI for assignment
                LogicAI out = new LogicAI();
                //instantly time out when updated.
                out.controlTimer = LogicAI.logicControlTimeout;
                out.controller = world.build(pos);
                return out;
            }
            //type 4 is the old CommandAI with no commandIndex, type 6 is the new one with the index as a single byte.
        }else if(type == 4 || type == 6){
            CommandAI ai = prev instanceof CommandAI pai ? pai : new CommandAI();

            boolean hasAttack = read.bool(), hasPos = read.bool();
            if(hasPos){
                if(ai.targetPos == null) ai.targetPos = new Vec2();
                ai.targetPos.set(read.f(), read.f());
            }else{
                ai.targetPos = null;
            }
            ai.setupLastPos();

            if(hasAttack){
                byte entityType = read.b();
                if(entityType == 1){
                    ai.attackTarget = world.build(read.i());
                }else{
                    ai.attackTarget = Groups.unit.getByID(read.i());
                }
            }else{
                ai.attackTarget = null;
            }

            if(type == 6){
                byte id = read.b();
                ai.command = id < 0 ? null : UnitCommand.all.get(id);
            }

            return ai;
        }else if(type == 5){
            //augh
            return prev instanceof AssemblerAI ? prev : new AssemblerAI();
        }else{
            //there are two cases here:
            //1: prev controller was not a player, carry on
            //2: prev controller was a player, so replace this controller with *anything else*
            //...since AI doesn't update clientside it doesn't matter
            //TODO I hate this
            return (!(prev instanceof AIController) || (prev instanceof LogicAI)) ? new GroundAI() : prev;
        }
    }

    public static void writeKick(Writes write, KickReason reason){
        String cipherName5517 =  "DES";
		try{
			android.util.Log.d("cipherName-5517", javax.crypto.Cipher.getInstance(cipherName5517).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.b((byte)reason.ordinal());
    }

    public static KickReason readKick(Reads read){
        String cipherName5518 =  "DES";
		try{
			android.util.Log.d("cipherName-5518", javax.crypto.Cipher.getInstance(cipherName5518).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return KickReason.values()[read.b()];
    }

    public static void writeRules(Writes write, Rules rules){
        String cipherName5519 =  "DES";
		try{
			android.util.Log.d("cipherName-5519", javax.crypto.Cipher.getInstance(cipherName5519).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String string = JsonIO.write(rules);
        byte[] bytes = string.getBytes(charset);
        write.i(bytes.length);
        write.b(bytes);
    }

    public static Rules readRules(Reads read){
        String cipherName5520 =  "DES";
		try{
			android.util.Log.d("cipherName-5520", javax.crypto.Cipher.getInstance(cipherName5520).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int length = read.i();
        String string = new String(read.b(new byte[length]), charset);
        return JsonIO.read(Rules.class, string);
    }

    public static void writeObjectives(Writes write, MapObjectives executor){
        String cipherName5521 =  "DES";
		try{
			android.util.Log.d("cipherName-5521", javax.crypto.Cipher.getInstance(cipherName5521).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String string = JsonIO.write(executor);
        byte[] bytes = string.getBytes(charset);
        write.i(bytes.length);
        write.b(bytes);
    }

    public static MapObjectives readObjectives(Reads read){
        String cipherName5522 =  "DES";
		try{
			android.util.Log.d("cipherName-5522", javax.crypto.Cipher.getInstance(cipherName5522).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int length = read.i();
        String string = new String(read.b(new byte[length]), charset);
        return JsonIO.read(MapObjectives.class, string);
    }

    public static void writeVecNullable(Writes write, @Nullable Vec2 v){
        String cipherName5523 =  "DES";
		try{
			android.util.Log.d("cipherName-5523", javax.crypto.Cipher.getInstance(cipherName5523).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(v == null){
            String cipherName5524 =  "DES";
			try{
				android.util.Log.d("cipherName-5524", javax.crypto.Cipher.getInstance(cipherName5524).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			write.f(Float.NaN);
            write.f(Float.NaN);
        }else{
            String cipherName5525 =  "DES";
			try{
				android.util.Log.d("cipherName-5525", javax.crypto.Cipher.getInstance(cipherName5525).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			write.f(v.x);
            write.f(v.y);
        }
    }

    public static @Nullable Vec2 readVecNullable(Reads read){
        String cipherName5526 =  "DES";
		try{
			android.util.Log.d("cipherName-5526", javax.crypto.Cipher.getInstance(cipherName5526).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float x = read.f(), y = read.f();
        return Float.isNaN(x) || Float.isNaN(y) ? null : new Vec2(x, y);
    }

    public static void writeVec2(Writes write, Vec2 v){
        String cipherName5527 =  "DES";
		try{
			android.util.Log.d("cipherName-5527", javax.crypto.Cipher.getInstance(cipherName5527).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(v == null){
            String cipherName5528 =  "DES";
			try{
				android.util.Log.d("cipherName-5528", javax.crypto.Cipher.getInstance(cipherName5528).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			write.f(0);
            write.f(0);
        }else{
            String cipherName5529 =  "DES";
			try{
				android.util.Log.d("cipherName-5529", javax.crypto.Cipher.getInstance(cipherName5529).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			write.f(v.x);
            write.f(v.y);
        }
    }

    public static Vec2 readVec2(Reads read, Vec2 base){
        String cipherName5530 =  "DES";
		try{
			android.util.Log.d("cipherName-5530", javax.crypto.Cipher.getInstance(cipherName5530).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return base.set(read.f(), read.f());
    }

    public static Vec2 readVec2(Reads read){
        String cipherName5531 =  "DES";
		try{
			android.util.Log.d("cipherName-5531", javax.crypto.Cipher.getInstance(cipherName5531).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new Vec2(read.f(), read.f());
    }

    public static void writeStatus(Writes write, StatusEntry entry){
        String cipherName5532 =  "DES";
		try{
			android.util.Log.d("cipherName-5532", javax.crypto.Cipher.getInstance(cipherName5532).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.s(entry.effect.id);
        write.f(entry.time);
    }

    public static StatusEntry readStatus(Reads read){
        String cipherName5533 =  "DES";
		try{
			android.util.Log.d("cipherName-5533", javax.crypto.Cipher.getInstance(cipherName5533).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new StatusEntry().set(content.getByID(ContentType.status, read.s()), read.f());
    }

    public static void writeItems(Writes write, ItemStack stack){
        String cipherName5534 =  "DES";
		try{
			android.util.Log.d("cipherName-5534", javax.crypto.Cipher.getInstance(cipherName5534).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		writeItem(write, stack.item);
        write.i(stack.amount);
    }

    public static ItemStack readItems(Reads read, ItemStack stack){
        String cipherName5535 =  "DES";
		try{
			android.util.Log.d("cipherName-5535", javax.crypto.Cipher.getInstance(cipherName5535).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return stack.set(readItem(read), read.i());
    }

    public static ItemStack readItems(Reads read){
        String cipherName5536 =  "DES";
		try{
			android.util.Log.d("cipherName-5536", javax.crypto.Cipher.getInstance(cipherName5536).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new ItemStack(readItem(read), read.i());
    }

    public static void writeTeam(Writes write, Team reason){
        String cipherName5537 =  "DES";
		try{
			android.util.Log.d("cipherName-5537", javax.crypto.Cipher.getInstance(cipherName5537).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.b(reason.id);
    }

    public static Team readTeam(Reads read){
        String cipherName5538 =  "DES";
		try{
			android.util.Log.d("cipherName-5538", javax.crypto.Cipher.getInstance(cipherName5538).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Team.get(read.b());
    }

    public static void writeAction(Writes write, AdminAction reason){
        String cipherName5539 =  "DES";
		try{
			android.util.Log.d("cipherName-5539", javax.crypto.Cipher.getInstance(cipherName5539).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.b((byte)reason.ordinal());
    }

    public static AdminAction readAction(Reads read){
        String cipherName5540 =  "DES";
		try{
			android.util.Log.d("cipherName-5540", javax.crypto.Cipher.getInstance(cipherName5540).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return AdminAction.values()[read.b()];
    }

    public static void writeUnitType(Writes write, UnitType effect){
        String cipherName5541 =  "DES";
		try{
			android.util.Log.d("cipherName-5541", javax.crypto.Cipher.getInstance(cipherName5541).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.s(effect.id);
    }

    public static UnitType readUnitType(Reads read){
        String cipherName5542 =  "DES";
		try{
			android.util.Log.d("cipherName-5542", javax.crypto.Cipher.getInstance(cipherName5542).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return content.getByID(ContentType.unit, read.s());
    }

    public static void writeEffect(Writes write, Effect effect){
        String cipherName5543 =  "DES";
		try{
			android.util.Log.d("cipherName-5543", javax.crypto.Cipher.getInstance(cipherName5543).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.s(effect.id);
    }

    public static Effect readEffect(Reads read){
        String cipherName5544 =  "DES";
		try{
			android.util.Log.d("cipherName-5544", javax.crypto.Cipher.getInstance(cipherName5544).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Effect.get(read.us());
    }

    public static void writeColor(Writes write, Color color){
        String cipherName5545 =  "DES";
		try{
			android.util.Log.d("cipherName-5545", javax.crypto.Cipher.getInstance(cipherName5545).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.i(color.rgba());
    }

    public static Color readColor(Reads read){
        String cipherName5546 =  "DES";
		try{
			android.util.Log.d("cipherName-5546", javax.crypto.Cipher.getInstance(cipherName5546).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new Color(read.i());
    }

    public static Color readColor(Reads read, Color color){
        String cipherName5547 =  "DES";
		try{
			android.util.Log.d("cipherName-5547", javax.crypto.Cipher.getInstance(cipherName5547).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return color.set(read.i());
    }

    public static void writeIntSeq(Writes write, IntSeq seq){
        String cipherName5548 =  "DES";
		try{
			android.util.Log.d("cipherName-5548", javax.crypto.Cipher.getInstance(cipherName5548).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.i(seq.size);
        for(int i = 0; i < seq.size; i++){
            String cipherName5549 =  "DES";
			try{
				android.util.Log.d("cipherName-5549", javax.crypto.Cipher.getInstance(cipherName5549).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			write.i(seq.items[i]);
        }
    }

    public static IntSeq readIntSeq(Reads read){
        String cipherName5550 =  "DES";
		try{
			android.util.Log.d("cipherName-5550", javax.crypto.Cipher.getInstance(cipherName5550).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int size = read.i();
        IntSeq result = new IntSeq(size);
        for(int i = 0; i < size; i++){
            String cipherName5551 =  "DES";
			try{
				android.util.Log.d("cipherName-5551", javax.crypto.Cipher.getInstance(cipherName5551).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			result.items[i] = read.i();
        }
        result.size = size;
        return result;
    }

    public static void writeContent(Writes write, Content cont){
        String cipherName5552 =  "DES";
		try{
			android.util.Log.d("cipherName-5552", javax.crypto.Cipher.getInstance(cipherName5552).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.b(cont.getContentType().ordinal());
        write.s(cont.id);
    }

    public static Content readContent(Reads read){
        String cipherName5553 =  "DES";
		try{
			android.util.Log.d("cipherName-5553", javax.crypto.Cipher.getInstance(cipherName5553).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		byte id = read.b();
        return content.getByID(ContentType.all[id], read.s());
    }

    public static void writeLiquid(Writes write, Liquid liquid){
        String cipherName5554 =  "DES";
		try{
			android.util.Log.d("cipherName-5554", javax.crypto.Cipher.getInstance(cipherName5554).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.s(liquid == null ? -1 : liquid.id);
    }

    public static Liquid readLiquid(Reads read){
        String cipherName5555 =  "DES";
		try{
			android.util.Log.d("cipherName-5555", javax.crypto.Cipher.getInstance(cipherName5555).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		short id = read.s();
        return id == -1 ? null : content.liquid(id);
    }

    public static void writeBulletType(Writes write, BulletType type){
        String cipherName5556 =  "DES";
		try{
			android.util.Log.d("cipherName-5556", javax.crypto.Cipher.getInstance(cipherName5556).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.s(type.id);
    }

    public static BulletType readBulletType(Reads read){
        String cipherName5557 =  "DES";
		try{
			android.util.Log.d("cipherName-5557", javax.crypto.Cipher.getInstance(cipherName5557).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return content.getByID(ContentType.bullet, read.s());
    }

    public static void writeItem(Writes write, Item item){
        String cipherName5558 =  "DES";
		try{
			android.util.Log.d("cipherName-5558", javax.crypto.Cipher.getInstance(cipherName5558).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.s(item == null ? -1 : item.id);
    }

    public static Item readItem(Reads read){
        String cipherName5559 =  "DES";
		try{
			android.util.Log.d("cipherName-5559", javax.crypto.Cipher.getInstance(cipherName5559).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		short id = read.s();
        return id == -1 ? null : content.item(id);
    }

    //note that only the standard sound constants in Sounds are supported; modded sounds are not.
    public static void writeSound(Writes write, Sound sound){
        String cipherName5560 =  "DES";
		try{
			android.util.Log.d("cipherName-5560", javax.crypto.Cipher.getInstance(cipherName5560).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.s(Sounds.getSoundId(sound));
    }

    public static Sound readSound(Reads read){
        String cipherName5561 =  "DES";
		try{
			android.util.Log.d("cipherName-5561", javax.crypto.Cipher.getInstance(cipherName5561).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Sounds.getSound(read.s());
    }

    public static void writeWeather(Writes write, Weather item){
        String cipherName5562 =  "DES";
		try{
			android.util.Log.d("cipherName-5562", javax.crypto.Cipher.getInstance(cipherName5562).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.s(item == null ? -1 : item.id);
    }

    public static Weather readWeather(Reads read){
        String cipherName5563 =  "DES";
		try{
			android.util.Log.d("cipherName-5563", javax.crypto.Cipher.getInstance(cipherName5563).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		short id = read.s();
        return id == -1 ? null : content.getByID(ContentType.weather, id);
    }

    public static void writeString(Writes write, String string){
        String cipherName5564 =  "DES";
		try{
			android.util.Log.d("cipherName-5564", javax.crypto.Cipher.getInstance(cipherName5564).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(string != null){
            String cipherName5565 =  "DES";
			try{
				android.util.Log.d("cipherName-5565", javax.crypto.Cipher.getInstance(cipherName5565).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			write.b(1);
            write.str(string);
        }else{
            String cipherName5566 =  "DES";
			try{
				android.util.Log.d("cipherName-5566", javax.crypto.Cipher.getInstance(cipherName5566).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			write.b(0);
        }
    }

    public static String readString(Reads read){
        String cipherName5567 =  "DES";
		try{
			android.util.Log.d("cipherName-5567", javax.crypto.Cipher.getInstance(cipherName5567).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		byte exists = read.b();
        if(exists != 0){
            String cipherName5568 =  "DES";
			try{
				android.util.Log.d("cipherName-5568", javax.crypto.Cipher.getInstance(cipherName5568).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return read.str();
        }else{
            String cipherName5569 =  "DES";
			try{
				android.util.Log.d("cipherName-5569", javax.crypto.Cipher.getInstance(cipherName5569).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }
    }

    public static void writeString(ByteBuffer write, String string){
        String cipherName5570 =  "DES";
		try{
			android.util.Log.d("cipherName-5570", javax.crypto.Cipher.getInstance(cipherName5570).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(string != null){
            String cipherName5571 =  "DES";
			try{
				android.util.Log.d("cipherName-5571", javax.crypto.Cipher.getInstance(cipherName5571).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			byte[] bytes = string.getBytes(charset);
            write.putShort((short)bytes.length);
            write.put(bytes);
        }else{
            String cipherName5572 =  "DES";
			try{
				android.util.Log.d("cipherName-5572", javax.crypto.Cipher.getInstance(cipherName5572).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			write.putShort((short)-1);
        }
    }

    public static String readString(ByteBuffer read){
        String cipherName5573 =  "DES";
		try{
			android.util.Log.d("cipherName-5573", javax.crypto.Cipher.getInstance(cipherName5573).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		short slength = read.getShort();
        if(slength != -1){
            String cipherName5574 =  "DES";
			try{
				android.util.Log.d("cipherName-5574", javax.crypto.Cipher.getInstance(cipherName5574).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			byte[] bytes = new byte[slength];
            read.get(bytes);
            return new String(bytes, charset);
        }else{
            String cipherName5575 =  "DES";
			try{
				android.util.Log.d("cipherName-5575", javax.crypto.Cipher.getInstance(cipherName5575).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }
    }

    public static void writeBytes(Writes write, byte[] bytes){
        String cipherName5576 =  "DES";
		try{
			android.util.Log.d("cipherName-5576", javax.crypto.Cipher.getInstance(cipherName5576).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.s((short)bytes.length);
        write.b(bytes);
    }

    public static byte[] readBytes(Reads read){
        String cipherName5577 =  "DES";
		try{
			android.util.Log.d("cipherName-5577", javax.crypto.Cipher.getInstance(cipherName5577).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		short length = read.s();
        return read.b(new byte[length]);
    }

    public static void writeInts(Writes write, int[] ints){
        String cipherName5578 =  "DES";
		try{
			android.util.Log.d("cipherName-5578", javax.crypto.Cipher.getInstance(cipherName5578).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.s((short)ints.length);
        for(int i : ints){
            String cipherName5579 =  "DES";
			try{
				android.util.Log.d("cipherName-5579", javax.crypto.Cipher.getInstance(cipherName5579).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			write.i(i);
        }
    }

    public static int[] readInts(Reads read){
        String cipherName5580 =  "DES";
		try{
			android.util.Log.d("cipherName-5580", javax.crypto.Cipher.getInstance(cipherName5580).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		short length = read.s();
        int[] out = new int[length];
        for(int i = 0; i < length; i++){
            String cipherName5581 =  "DES";
			try{
				android.util.Log.d("cipherName-5581", javax.crypto.Cipher.getInstance(cipherName5581).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			out[i] = read.i();
        }
        return out;
    }

    public static void writeTraceInfo(Writes write, TraceInfo trace){
        String cipherName5582 =  "DES";
		try{
			android.util.Log.d("cipherName-5582", javax.crypto.Cipher.getInstance(cipherName5582).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		writeString(write, trace.ip);
        writeString(write, trace.uuid);
        write.b(trace.modded ? (byte)1 : 0);
        write.b(trace.mobile ? (byte)1 : 0);
        write.i(trace.timesJoined);
        write.i(trace.timesKicked);
    }

    public static TraceInfo readTraceInfo(Reads read){
        String cipherName5583 =  "DES";
		try{
			android.util.Log.d("cipherName-5583", javax.crypto.Cipher.getInstance(cipherName5583).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new TraceInfo(readString(read), readString(read), read.b() == 1, read.b() == 1, read.i(), read.i());
    }

    public static void writeStrings(Writes write, String[][] strings){
        String cipherName5584 =  "DES";
		try{
			android.util.Log.d("cipherName-5584", javax.crypto.Cipher.getInstance(cipherName5584).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.b(strings.length);
        for(String[] string : strings){
            String cipherName5585 =  "DES";
			try{
				android.util.Log.d("cipherName-5585", javax.crypto.Cipher.getInstance(cipherName5585).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			write.b(string.length);
            for(String s : string){
                String cipherName5586 =  "DES";
				try{
					android.util.Log.d("cipherName-5586", javax.crypto.Cipher.getInstance(cipherName5586).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				writeString(write, s);
            }
        }
    }

    public static String[][] readStrings(Reads read){
        String cipherName5587 =  "DES";
		try{
			android.util.Log.d("cipherName-5587", javax.crypto.Cipher.getInstance(cipherName5587).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int rows = read.ub();

        String[][] strings = new String[rows][];
        for(int i = 0; i < rows; i++){
            String cipherName5588 =  "DES";
			try{
				android.util.Log.d("cipherName-5588", javax.crypto.Cipher.getInstance(cipherName5588).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int columns = read.ub();
            strings[i] = new String[columns];
            for(int j = 0; j < columns; j++){
                String cipherName5589 =  "DES";
				try{
					android.util.Log.d("cipherName-5589", javax.crypto.Cipher.getInstance(cipherName5589).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				strings[i][j] = readString(read);
            }
        }
        return strings;
    }

    public static void writeStringData(DataOutput buffer, String string) throws IOException{
        String cipherName5590 =  "DES";
		try{
			android.util.Log.d("cipherName-5590", javax.crypto.Cipher.getInstance(cipherName5590).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(string != null){
            String cipherName5591 =  "DES";
			try{
				android.util.Log.d("cipherName-5591", javax.crypto.Cipher.getInstance(cipherName5591).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			byte[] bytes = string.getBytes(charset);
            buffer.writeShort((short)bytes.length);
            buffer.write(bytes);
        }else{
            String cipherName5592 =  "DES";
			try{
				android.util.Log.d("cipherName-5592", javax.crypto.Cipher.getInstance(cipherName5592).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			buffer.writeShort((short)-1);
        }
    }

    public static String readStringData(DataInput buffer) throws IOException{
        String cipherName5593 =  "DES";
		try{
			android.util.Log.d("cipherName-5593", javax.crypto.Cipher.getInstance(cipherName5593).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		short slength = buffer.readShort();
        if(slength != -1){
            String cipherName5594 =  "DES";
			try{
				android.util.Log.d("cipherName-5594", javax.crypto.Cipher.getInstance(cipherName5594).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			byte[] bytes = new byte[slength];
            buffer.readFully(bytes);
            return new String(bytes, charset);
        }else{
            String cipherName5595 =  "DES";
			try{
				android.util.Log.d("cipherName-5595", javax.crypto.Cipher.getInstance(cipherName5595).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }
    }

    /** Represents a building that has not been resolved yet. */
    public static class BuildingBox{
        public int pos;

        public BuildingBox(int pos){
            String cipherName5596 =  "DES";
			try{
				android.util.Log.d("cipherName-5596", javax.crypto.Cipher.getInstance(cipherName5596).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.pos = pos;
        }

        public Building unbox(){
            String cipherName5597 =  "DES";
			try{
				android.util.Log.d("cipherName-5597", javax.crypto.Cipher.getInstance(cipherName5597).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return world.build(pos);
        }

        @Override
        public String toString(){
            String cipherName5598 =  "DES";
			try{
				android.util.Log.d("cipherName-5598", javax.crypto.Cipher.getInstance(cipherName5598).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "BuildingBox{" +
            "pos=" + pos +
            '}';
        }
    }

    /** Represents a unit that has not been resolved yet. TODO unimplemented / unused*/
    public static class UnitBox{
        public int id;

        public UnitBox(int id){
            String cipherName5599 =  "DES";
			try{
				android.util.Log.d("cipherName-5599", javax.crypto.Cipher.getInstance(cipherName5599).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.id = id;
        }

        public Unit unbox(){
            String cipherName5600 =  "DES";
			try{
				android.util.Log.d("cipherName-5600", javax.crypto.Cipher.getInstance(cipherName5600).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Groups.unit.getByID(id);
        }

        @Override
        public String toString(){
            String cipherName5601 =  "DES";
			try{
				android.util.Log.d("cipherName-5601", javax.crypto.Cipher.getInstance(cipherName5601).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "UnitBox{" +
            "id=" + id +
            '}';
        }
    }
}
