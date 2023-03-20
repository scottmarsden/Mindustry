package mindustry.entities.comp;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.pooling.*;
import mindustry.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.entities.units.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.net.Administration.*;
import mindustry.net.*;
import mindustry.net.Packets.*;
import mindustry.ui.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.blocks.storage.CoreBlock.*;

import static mindustry.Vars.*;

@EntityDef(value = {Playerc.class}, serialize = false)
@Component(base = true)
abstract class PlayerComp implements UnitController, Entityc, Syncc, Timerc, Drawc{
    static final float deathDelay = 60f;

    @Import float x, y;

    @ReadOnly Unit unit = Nulls.unit;
    transient @Nullable NetConnection con;
    @ReadOnly Team team = Team.sharded;
    @SyncLocal boolean typing, shooting, boosting;
    @SyncLocal float mouseX, mouseY;
    boolean admin;
    String name = "frog";
    Color color = new Color();
    transient String locale = "en";
    transient float deathTimer;
    transient String lastText = "";
    transient float textFadeTime;

    transient private Unit lastReadUnit = Nulls.unit;
    transient private int wrongReadUnits;
    transient @Nullable Unit justSwitchFrom, justSwitchTo;

    public boolean isBuilder(){
        String cipherName16639 =  "DES";
		try{
			android.util.Log.d("cipherName-16639", javax.crypto.Cipher.getInstance(cipherName16639).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return unit.canBuild();
    }

    public @Nullable CoreBuild closestCore(){
        String cipherName16640 =  "DES";
		try{
			android.util.Log.d("cipherName-16640", javax.crypto.Cipher.getInstance(cipherName16640).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return state.teams.closestCore(x, y, team);
    }

    public @Nullable CoreBuild core(){
        String cipherName16641 =  "DES";
		try{
			android.util.Log.d("cipherName-16641", javax.crypto.Cipher.getInstance(cipherName16641).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return team.core();
    }

    /** @return largest/closest core, with largest cores getting priority */
    @Nullable
    public CoreBuild bestCore(){
        String cipherName16642 =  "DES";
		try{
			android.util.Log.d("cipherName-16642", javax.crypto.Cipher.getInstance(cipherName16642).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return team.cores().min(Structs.comps(Structs.comparingInt(c -> -c.block.size), Structs.comparingFloat(c -> c.dst(x, y))));
    }

    public TextureRegion icon(){
        String cipherName16643 =  "DES";
		try{
			android.util.Log.d("cipherName-16643", javax.crypto.Cipher.getInstance(cipherName16643).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//display default icon for dead players
        if(dead()) return core() == null ? UnitTypes.alpha.fullIcon : ((CoreBlock)bestCore().block).unitType.fullIcon;

        return unit.icon();
    }

    public boolean displayAmmo(){
        String cipherName16644 =  "DES";
		try{
			android.util.Log.d("cipherName-16644", javax.crypto.Cipher.getInstance(cipherName16644).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return unit instanceof BlockUnitc || state.rules.unitAmmo;
    }

    public void reset(){
        String cipherName16645 =  "DES";
		try{
			android.util.Log.d("cipherName-16645", javax.crypto.Cipher.getInstance(cipherName16645).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		team = state.rules.defaultTeam;
        admin = typing = false;
        textFadeTime = 0f;
        x = y = 0f;
        if(!dead()){
            String cipherName16646 =  "DES";
			try{
				android.util.Log.d("cipherName-16646", javax.crypto.Cipher.getInstance(cipherName16646).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			unit.resetController();
            unit = Nulls.unit;
        }
    }

    @Override
    public boolean isValidController(){
        String cipherName16647 =  "DES";
		try{
			android.util.Log.d("cipherName-16647", javax.crypto.Cipher.getInstance(cipherName16647).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return isAdded();
    }

    @Override
    public boolean isLogicControllable(){
        String cipherName16648 =  "DES";
		try{
			android.util.Log.d("cipherName-16648", javax.crypto.Cipher.getInstance(cipherName16648).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    @Replace
    public float clipSize(){
        String cipherName16649 =  "DES";
		try{
			android.util.Log.d("cipherName-16649", javax.crypto.Cipher.getInstance(cipherName16649).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return unit.isNull() ? 20 : unit.type.hitSize * 2f;
    }

    @Override
    public void afterSync(){
        String cipherName16650 =  "DES";
		try{
			android.util.Log.d("cipherName-16650", javax.crypto.Cipher.getInstance(cipherName16650).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//fix rubberbanding:
        //when the player recs a unit that they JUST transitioned away from, use the new unit instead
        //reason: we know the server is lying here, essentially skip the unit snapshot because we know the client's information is more recent
        if(isLocal() && unit == justSwitchFrom && justSwitchFrom != null && justSwitchTo != null){
            String cipherName16651 =  "DES";
			try{
				android.util.Log.d("cipherName-16651", javax.crypto.Cipher.getInstance(cipherName16651).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			unit = justSwitchTo;
            //if several snapshots have passed and this unit is still incorrect, something's wrong
            if(++wrongReadUnits >= 2){
                String cipherName16652 =  "DES";
				try{
					android.util.Log.d("cipherName-16652", javax.crypto.Cipher.getInstance(cipherName16652).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				justSwitchFrom = null;
                wrongReadUnits = 0;
            }
        }else{
            String cipherName16653 =  "DES";
			try{
				android.util.Log.d("cipherName-16653", javax.crypto.Cipher.getInstance(cipherName16653).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			justSwitchFrom = null;
            justSwitchTo = null;
            wrongReadUnits = 0;
        }

        //simulate a unit change after sync
        Unit set = unit;
        unit = lastReadUnit;
        unit(set);
        lastReadUnit = unit;

        unit.aim(mouseX, mouseY);
        //this is only necessary when the thing being controlled isn't synced
        unit.controlWeapons(shooting, shooting);
        //extra precaution, necessary for non-synced things
        unit.controller(this);
    }

    @Override
    public void update(){
        String cipherName16654 =  "DES";
		try{
			android.util.Log.d("cipherName-16654", javax.crypto.Cipher.getInstance(cipherName16654).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!unit.isValid()){
            String cipherName16655 =  "DES";
			try{
				android.util.Log.d("cipherName-16655", javax.crypto.Cipher.getInstance(cipherName16655).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			clearUnit();
        }

        CoreBuild core;

        if(!dead()){
            String cipherName16656 =  "DES";
			try{
				android.util.Log.d("cipherName-16656", javax.crypto.Cipher.getInstance(cipherName16656).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			set(unit);
            unit.team(team);
            deathTimer = 0;

            //update some basic state to sync things
            if(unit.type.canBoost){
                String cipherName16657 =  "DES";
				try{
					android.util.Log.d("cipherName-16657", javax.crypto.Cipher.getInstance(cipherName16657).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				unit.elevation = Mathf.approachDelta(unit.elevation, unit.onSolid() || boosting || (unit.isFlying() && !unit.canLand()) ? 1f : 0f, unit.type.riseSpeed);
            }
        }else if((core = bestCore()) != null){
            String cipherName16658 =  "DES";
			try{
				android.util.Log.d("cipherName-16658", javax.crypto.Cipher.getInstance(cipherName16658).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//have a small delay before death to prevent the camera from jumping around too quickly
            //(this is not for balance, it just looks better this way)
            deathTimer += Time.delta;
            if(deathTimer >= deathDelay){
                String cipherName16659 =  "DES";
				try{
					android.util.Log.d("cipherName-16659", javax.crypto.Cipher.getInstance(cipherName16659).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//request spawn - this happens serverside only
                core.requestSpawn(self());
                deathTimer = 0;
            }
        }

        textFadeTime -= Time.delta / (60 * 5);

    }

    public void checkSpawn(){
        String cipherName16660 =  "DES";
		try{
			android.util.Log.d("cipherName-16660", javax.crypto.Cipher.getInstance(cipherName16660).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		CoreBuild core = bestCore();
        if(core != null){
            String cipherName16661 =  "DES";
			try{
				android.util.Log.d("cipherName-16661", javax.crypto.Cipher.getInstance(cipherName16661).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			core.requestSpawn(self());
        }
    }

    @Override
    public void remove(){
        String cipherName16662 =  "DES";
		try{
			android.util.Log.d("cipherName-16662", javax.crypto.Cipher.getInstance(cipherName16662).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//clear unit upon removal
        if(!unit.isNull()){
            String cipherName16663 =  "DES";
			try{
				android.util.Log.d("cipherName-16663", javax.crypto.Cipher.getInstance(cipherName16663).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			clearUnit();
        }
    }

    public void team(Team team){
        String cipherName16664 =  "DES";
		try{
			android.util.Log.d("cipherName-16664", javax.crypto.Cipher.getInstance(cipherName16664).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.team = team;
        unit.team(team);
    }

    public void clearUnit(){
        String cipherName16665 =  "DES";
		try{
			android.util.Log.d("cipherName-16665", javax.crypto.Cipher.getInstance(cipherName16665).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		unit(Nulls.unit);
    }

    public Unit unit(){
        String cipherName16666 =  "DES";
		try{
			android.util.Log.d("cipherName-16666", javax.crypto.Cipher.getInstance(cipherName16666).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return unit;
    }

    public void unit(Unit unit){
        String cipherName16667 =  "DES";
		try{
			android.util.Log.d("cipherName-16667", javax.crypto.Cipher.getInstance(cipherName16667).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//refuse to switch when the unit was just transitioned from
        if(isLocal() && unit == justSwitchFrom && justSwitchFrom != null && justSwitchTo != null){
            String cipherName16668 =  "DES";
			try{
				android.util.Log.d("cipherName-16668", javax.crypto.Cipher.getInstance(cipherName16668).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        if(unit == null) throw new IllegalArgumentException("Unit cannot be null. Use clearUnit() instead.");
        if(this.unit == unit) return;

        if(this.unit != Nulls.unit){
            String cipherName16669 =  "DES";
			try{
				android.util.Log.d("cipherName-16669", javax.crypto.Cipher.getInstance(cipherName16669).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//un-control the old unit
            this.unit.resetController();
        }
        this.unit = unit;
        if(unit != Nulls.unit){
            String cipherName16670 =  "DES";
			try{
				android.util.Log.d("cipherName-16670", javax.crypto.Cipher.getInstance(cipherName16670).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			unit.team(team);
            unit.controller(this);

            //this player just became remote, snap the interpolation so it doesn't go wild
            if(unit.isRemote()){
                String cipherName16671 =  "DES";
				try{
					android.util.Log.d("cipherName-16671", javax.crypto.Cipher.getInstance(cipherName16671).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				unit.snapInterpolation();
            }

            //reset selected block when switching units
            if(!headless && isLocal()){
                String cipherName16672 =  "DES";
				try{
					android.util.Log.d("cipherName-16672", javax.crypto.Cipher.getInstance(cipherName16672).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				control.input.block = null;
            }
        }

        Events.fire(new UnitChangeEvent(self(), unit));
    }

    boolean dead(){
        String cipherName16673 =  "DES";
		try{
			android.util.Log.d("cipherName-16673", javax.crypto.Cipher.getInstance(cipherName16673).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return unit.isNull() || !unit.isValid();
    }

    String ip(){
        String cipherName16674 =  "DES";
		try{
			android.util.Log.d("cipherName-16674", javax.crypto.Cipher.getInstance(cipherName16674).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return con == null ? "localhost" : con.address;
    }

    String uuid(){
        String cipherName16675 =  "DES";
		try{
			android.util.Log.d("cipherName-16675", javax.crypto.Cipher.getInstance(cipherName16675).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return con == null ? "[LOCAL]" : con.uuid;
    }

    String usid(){
        String cipherName16676 =  "DES";
		try{
			android.util.Log.d("cipherName-16676", javax.crypto.Cipher.getInstance(cipherName16676).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return con == null ? "[LOCAL]" : con.usid;
    }

    void kick(KickReason reason){
        String cipherName16677 =  "DES";
		try{
			android.util.Log.d("cipherName-16677", javax.crypto.Cipher.getInstance(cipherName16677).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		con.kick(reason);
    }

    void kick(KickReason reason, long duration){
        String cipherName16678 =  "DES";
		try{
			android.util.Log.d("cipherName-16678", javax.crypto.Cipher.getInstance(cipherName16678).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		con.kick(reason, duration);
    }

    void kick(String reason){
        String cipherName16679 =  "DES";
		try{
			android.util.Log.d("cipherName-16679", javax.crypto.Cipher.getInstance(cipherName16679).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		con.kick(reason);
    }

    void kick(String reason, long duration){
        String cipherName16680 =  "DES";
		try{
			android.util.Log.d("cipherName-16680", javax.crypto.Cipher.getInstance(cipherName16680).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		con.kick(reason, duration);
    }

    @Override
    public void draw(){
        String cipherName16681 =  "DES";
		try{
			android.util.Log.d("cipherName-16681", javax.crypto.Cipher.getInstance(cipherName16681).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(unit != null && unit.inFogTo(Vars.player.team())) return;

        Draw.z(Layer.playerName);
        float z = Drawf.text();

        Font font = Fonts.outline;
        GlyphLayout layout = Pools.obtain(GlyphLayout.class, GlyphLayout::new);
        final float nameHeight = 11;
        final float textHeight = 15;

        boolean ints = font.usesIntegerPositions();
        font.setUseIntegerPositions(false);
        font.getData().setScale(0.25f / Scl.scl(1f));
        layout.setText(font, name);

        if(!isLocal()){
            String cipherName16682 =  "DES";
			try{
				android.util.Log.d("cipherName-16682", javax.crypto.Cipher.getInstance(cipherName16682).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.color(0f, 0f, 0f, 0.3f);
            Fill.rect(unit.x, unit.y + nameHeight - layout.height / 2, layout.width + 2, layout.height + 3);
            Draw.color();
            font.setColor(color);
            font.draw(name, unit.x, unit.y + nameHeight, 0, Align.center, false);

            if(admin){
                String cipherName16683 =  "DES";
				try{
					android.util.Log.d("cipherName-16683", javax.crypto.Cipher.getInstance(cipherName16683).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float s = 3f;
                Draw.color(color.r * 0.5f, color.g * 0.5f, color.b * 0.5f, 1f);
                Draw.rect(Icon.adminSmall.getRegion(), unit.x + layout.width / 2f + 2 + 1, unit.y + nameHeight - 1.5f, s, s);
                Draw.color(color);
                Draw.rect(Icon.adminSmall.getRegion(), unit.x + layout.width / 2f + 2 + 1, unit.y + nameHeight - 1f, s, s);
            }
        }

        if(Core.settings.getBool("playerchat") && ((textFadeTime > 0 && lastText != null) || typing)){
            String cipherName16684 =  "DES";
			try{
				android.util.Log.d("cipherName-16684", javax.crypto.Cipher.getInstance(cipherName16684).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String text = textFadeTime <= 0 || lastText == null ? "[lightgray]" + Strings.animated(Time.time, 4, 15f, ".") : lastText;
            float width = 100f;
            float visualFadeTime = 1f - Mathf.curve(1f - textFadeTime, 0.9f);
            font.setColor(1f, 1f, 1f, textFadeTime <= 0 || lastText == null ? 1f : visualFadeTime);

            layout.setText(font, text, Color.white, width, Align.bottom, true);

            Draw.color(0f, 0f, 0f, 0.3f * (textFadeTime <= 0 || lastText == null ? 1f : visualFadeTime));
            Fill.rect(unit.x, unit.y + textHeight + layout.height - layout.height / 2f, layout.width + 2, layout.height + 3);
            font.draw(text, unit.x - width / 2f, unit.y + textHeight + layout.height, width, Align.center, true);
        }

        Draw.reset();
        Pools.free(layout);
        font.getData().setScale(1f);
        font.setColor(Color.white);
        font.setUseIntegerPositions(ints);

        Draw.z(z);
    }

    /** @return name with a markup color prefix */
    String coloredName(){
        String cipherName16685 =  "DES";
		try{
			android.util.Log.d("cipherName-16685", javax.crypto.Cipher.getInstance(cipherName16685).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return  "[#" + color.toString().toUpperCase() + "]" + name;
    }

    String plainName(){
        String cipherName16686 =  "DES";
		try{
			android.util.Log.d("cipherName-16686", javax.crypto.Cipher.getInstance(cipherName16686).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Strings.stripColors(name);
    }

    void sendMessage(String text){
        String cipherName16687 =  "DES";
		try{
			android.util.Log.d("cipherName-16687", javax.crypto.Cipher.getInstance(cipherName16687).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(isLocal()){
            String cipherName16688 =  "DES";
			try{
				android.util.Log.d("cipherName-16688", javax.crypto.Cipher.getInstance(cipherName16688).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(ui != null){
                String cipherName16689 =  "DES";
				try{
					android.util.Log.d("cipherName-16689", javax.crypto.Cipher.getInstance(cipherName16689).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ui.chatfrag.addMessage(text);
            }
        }else{
            String cipherName16690 =  "DES";
			try{
				android.util.Log.d("cipherName-16690", javax.crypto.Cipher.getInstance(cipherName16690).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Call.sendMessage(con, text, null, null);
        }
    }

    void sendMessage(String text, Player from){
        String cipherName16691 =  "DES";
		try{
			android.util.Log.d("cipherName-16691", javax.crypto.Cipher.getInstance(cipherName16691).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		sendMessage(text, from, null);
    }

    void sendMessage(String text, Player from, String unformatted){
        String cipherName16692 =  "DES";
		try{
			android.util.Log.d("cipherName-16692", javax.crypto.Cipher.getInstance(cipherName16692).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(isLocal()){
            String cipherName16693 =  "DES";
			try{
				android.util.Log.d("cipherName-16693", javax.crypto.Cipher.getInstance(cipherName16693).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(ui != null){
                String cipherName16694 =  "DES";
				try{
					android.util.Log.d("cipherName-16694", javax.crypto.Cipher.getInstance(cipherName16694).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ui.chatfrag.addMessage(text);
            }
        }else{
            String cipherName16695 =  "DES";
			try{
				android.util.Log.d("cipherName-16695", javax.crypto.Cipher.getInstance(cipherName16695).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Call.sendMessage(con, text, unformatted, from);
        }
    }

    PlayerInfo getInfo(){
        String cipherName16696 =  "DES";
		try{
			android.util.Log.d("cipherName-16696", javax.crypto.Cipher.getInstance(cipherName16696).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(isLocal()){
            String cipherName16697 =  "DES";
			try{
				android.util.Log.d("cipherName-16697", javax.crypto.Cipher.getInstance(cipherName16697).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Local players cannot be traced and do not have info.");
        }else{
            String cipherName16698 =  "DES";
			try{
				android.util.Log.d("cipherName-16698", javax.crypto.Cipher.getInstance(cipherName16698).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return netServer.admins.getInfo(uuid());
        }
    }
}
