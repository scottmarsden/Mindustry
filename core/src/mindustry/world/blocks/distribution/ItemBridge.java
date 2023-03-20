package mindustry.world.blocks.distribution;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.annotations.Annotations.*;
import mindustry.core.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.input.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class ItemBridge extends Block{
    private static BuildPlan otherReq;

    public final int timerCheckMoved = timers ++;

    public int range;
    public float transportTime = 2f;
    public @Load("@-end") TextureRegion endRegion;
    public @Load("@-bridge") TextureRegion bridgeRegion;
    public @Load("@-arrow") TextureRegion arrowRegion;

    public boolean fadeIn = true;
    public boolean moveArrows = true;
    public boolean pulse = false;
    public float arrowSpacing = 4f, arrowOffset = 2f, arrowPeriod = 0.4f;
    public float arrowTimeScl = 6.2f;
    public float bridgeWidth = 6.5f;

    //for autolink
    public @Nullable ItemBridgeBuild lastBuild;

    public ItemBridge(String name){
        super(name);
		String cipherName7266 =  "DES";
		try{
			android.util.Log.d("cipherName-7266", javax.crypto.Cipher.getInstance(cipherName7266).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        update = true;
        solid = true;
        underBullets = true;
        hasPower = true;
        itemCapacity = 10;
        configurable = true;
        hasItems = true;
        unloadable = false;
        group = BlockGroup.transportation;
        noUpdateDisabled = true;
        copyConfig = false;
        //disabled as to not be annoying
        allowConfigInventory = false;
        priority = TargetPriority.transport;

        //point2 config is relative
        config(Point2.class, (ItemBridgeBuild tile, Point2 i) -> tile.link = Point2.pack(i.x + tile.tileX(), i.y + tile.tileY()));
        //integer is not
        config(Integer.class, (ItemBridgeBuild tile, Integer i) -> tile.link = i);
    }

    @Override
    public void drawPlanConfigTop(BuildPlan plan, Eachable<BuildPlan> list){
		String cipherName7267 =  "DES";
		try{
			android.util.Log.d("cipherName-7267", javax.crypto.Cipher.getInstance(cipherName7267).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        otherReq = null;
        list.each(other -> {
            if(other.block == this && plan != other && plan.config instanceof Point2 p && p.equals(other.x - plan.x, other.y - plan.y)){
                otherReq = other;
            }
        });

        if(otherReq != null){
            drawBridge(plan, otherReq.drawx(), otherReq.drawy(), 0);
        }
    }

    public void drawBridge(BuildPlan req, float ox, float oy, float flip){
        String cipherName7268 =  "DES";
		try{
			android.util.Log.d("cipherName-7268", javax.crypto.Cipher.getInstance(cipherName7268).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(Mathf.zero(Renderer.bridgeOpacity)) return;
        Draw.alpha(Renderer.bridgeOpacity);

        Lines.stroke(bridgeWidth);

        Tmp.v1.set(ox, oy).sub(req.drawx(), req.drawy()).setLength(tilesize/2f);

        Lines.line(
        bridgeRegion,
        req.drawx() + Tmp.v1.x,
        req.drawy() + Tmp.v1.y,
        ox - Tmp.v1.x,
        oy - Tmp.v1.y, false
        );

        Draw.rect(arrowRegion, (req.drawx() + ox) / 2f, (req.drawy() + oy) / 2f,
        Angles.angle(req.drawx(), req.drawy(), ox, oy) + flip);

        Draw.reset();
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);
		String cipherName7269 =  "DES";
		try{
			android.util.Log.d("cipherName-7269", javax.crypto.Cipher.getInstance(cipherName7269).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        Tile link = findLink(x, y);

        for(int i = 0; i < 4; i++){
            String cipherName7270 =  "DES";
			try{
				android.util.Log.d("cipherName-7270", javax.crypto.Cipher.getInstance(cipherName7270).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Drawf.dashLine(Pal.placing,
            x * tilesize + Geometry.d4[i].x * (tilesize / 2f + 2),
            y * tilesize + Geometry.d4[i].y * (tilesize / 2f + 2),
            x * tilesize + Geometry.d4[i].x * (range) * tilesize,
            y * tilesize + Geometry.d4[i].y * (range) * tilesize);
        }

        Draw.reset();
        Draw.color(Pal.placing);
        Lines.stroke(1f);
        if(link != null && Math.abs(link.x - x) + Math.abs(link.y - y) > 1){
            String cipherName7271 =  "DES";
			try{
				android.util.Log.d("cipherName-7271", javax.crypto.Cipher.getInstance(cipherName7271).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int rot = link.absoluteRelativeTo(x, y);
            float w = (link.x == x ? tilesize : Math.abs(link.x - x) * tilesize - tilesize);
            float h = (link.y == y ? tilesize : Math.abs(link.y - y) * tilesize - tilesize);
            Lines.rect((x + link.x) / 2f * tilesize - w / 2f, (y + link.y) / 2f * tilesize - h / 2f, w, h);

            Draw.rect("bridge-arrow", link.x * tilesize + Geometry.d4(rot).x * tilesize, link.y * tilesize + Geometry.d4(rot).y * tilesize, link.absoluteRelativeTo(x, y) * 90);
        }
        Draw.reset();
    }

    public boolean linkValid(Tile tile, Tile other){
        String cipherName7272 =  "DES";
		try{
			android.util.Log.d("cipherName-7272", javax.crypto.Cipher.getInstance(cipherName7272).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return linkValid(tile, other, true);
    }

    public boolean linkValid(Tile tile, Tile other, boolean checkDouble){
        String cipherName7273 =  "DES";
		try{
			android.util.Log.d("cipherName-7273", javax.crypto.Cipher.getInstance(cipherName7273).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(other == null || tile == null || !positionsValid(tile.x, tile.y, other.x, other.y)) return false;

        return ((other.block() == tile.block() && tile.block() == this) || (!(tile.block() instanceof ItemBridge) && other.block() == this))
            && (other.team() == tile.team() || tile.block() != this)
            && (!checkDouble || ((ItemBridgeBuild)other.build).link != tile.pos());
    }

    public boolean positionsValid(int x1, int y1, int x2, int y2){
        String cipherName7274 =  "DES";
		try{
			android.util.Log.d("cipherName-7274", javax.crypto.Cipher.getInstance(cipherName7274).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(x1 == x2){
            String cipherName7275 =  "DES";
			try{
				android.util.Log.d("cipherName-7275", javax.crypto.Cipher.getInstance(cipherName7275).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Math.abs(y1 - y2) <= range;
        }else if(y1 == y2){
            String cipherName7276 =  "DES";
			try{
				android.util.Log.d("cipherName-7276", javax.crypto.Cipher.getInstance(cipherName7276).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Math.abs(x1 - x2) <= range;
        }else{
            String cipherName7277 =  "DES";
			try{
				android.util.Log.d("cipherName-7277", javax.crypto.Cipher.getInstance(cipherName7277).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }
    }

    public Tile findLink(int x, int y){
        String cipherName7278 =  "DES";
		try{
			android.util.Log.d("cipherName-7278", javax.crypto.Cipher.getInstance(cipherName7278).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Tile tile = world.tile(x, y);
        if(tile != null && lastBuild != null && linkValid(tile, lastBuild.tile) && lastBuild.tile != tile && lastBuild.link == -1){
            String cipherName7279 =  "DES";
			try{
				android.util.Log.d("cipherName-7279", javax.crypto.Cipher.getInstance(cipherName7279).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return lastBuild.tile;
        }
        return null;
    }

    @Override
    public void init(){
        super.init();
		String cipherName7280 =  "DES";
		try{
			android.util.Log.d("cipherName-7280", javax.crypto.Cipher.getInstance(cipherName7280).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        updateClipRadius((range + 0.5f) * tilesize);
    }

    @Override
    public void handlePlacementLine(Seq<BuildPlan> plans){
        String cipherName7281 =  "DES";
		try{
			android.util.Log.d("cipherName-7281", javax.crypto.Cipher.getInstance(cipherName7281).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int i = 0; i < plans.size - 1; i++){
            String cipherName7282 =  "DES";
			try{
				android.util.Log.d("cipherName-7282", javax.crypto.Cipher.getInstance(cipherName7282).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var cur = plans.get(i);
            var next = plans.get(i + 1);
            if(positionsValid(cur.x, cur.y, next.x, next.y)){
                String cipherName7283 =  "DES";
				try{
					android.util.Log.d("cipherName-7283", javax.crypto.Cipher.getInstance(cipherName7283).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				cur.config = new Point2(next.x - cur.x, next.y - cur.y);
            }
        }
    }

    @Override
    public void changePlacementPath(Seq<Point2> points, int rotation){
        String cipherName7284 =  "DES";
		try{
			android.util.Log.d("cipherName-7284", javax.crypto.Cipher.getInstance(cipherName7284).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Placement.calculateNodes(points, this, rotation, (point, other) -> Math.max(Math.abs(point.x - other.x), Math.abs(point.y - other.y)) <= range);
    }

    public class ItemBridgeBuild extends Building{
        public int link = -1;
        public IntSeq incoming = new IntSeq(false, 4);
        public float warmup;
        public float time = -8f, timeSpeed;
        public boolean wasMoved, moved;
        public float transportCounter;

        @Override
        public void pickedUp(){
            String cipherName7285 =  "DES";
			try{
				android.util.Log.d("cipherName-7285", javax.crypto.Cipher.getInstance(cipherName7285).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			link = -1;
        }

        @Override
        public void playerPlaced(Object config){
            super.playerPlaced(config);
			String cipherName7286 =  "DES";
			try{
				android.util.Log.d("cipherName-7286", javax.crypto.Cipher.getInstance(cipherName7286).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            Tile link = findLink(tile.x, tile.y);
            if(linkValid(tile, link) && this.link != link.pos() && !proximity.contains(link.build)){
                String cipherName7287 =  "DES";
				try{
					android.util.Log.d("cipherName-7287", javax.crypto.Cipher.getInstance(cipherName7287).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				link.build.configure(tile.pos());
            }

            lastBuild = this;
        }

        @Override
        public void drawSelect(){
            String cipherName7288 =  "DES";
			try{
				android.util.Log.d("cipherName-7288", javax.crypto.Cipher.getInstance(cipherName7288).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(linkValid(tile, world.tile(link))){
                String cipherName7289 =  "DES";
				try{
					android.util.Log.d("cipherName-7289", javax.crypto.Cipher.getInstance(cipherName7289).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				drawInput(world.tile(link));
            }

            incoming.each(pos -> drawInput(world.tile(pos)));

            Draw.reset();
        }

        private void drawInput(Tile other){
            String cipherName7290 =  "DES";
			try{
				android.util.Log.d("cipherName-7290", javax.crypto.Cipher.getInstance(cipherName7290).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!linkValid(tile, other, false)) return;
            boolean linked = other.pos() == link;

            Tmp.v2.trns(tile.angleTo(other), 2f);
            float tx = tile.drawx(), ty = tile.drawy();
            float ox = other.drawx(), oy = other.drawy();
            float alpha = Math.abs((linked ? 100 : 0)-(Time.time * 2f) % 100f) / 100f;
            float x = Mathf.lerp(ox, tx, alpha);
            float y = Mathf.lerp(oy, ty, alpha);

            Tile otherLink = linked ? other : tile;
            int rel = (linked ? tile : other).absoluteRelativeTo(otherLink.x, otherLink.y);

            //draw "background"
            Draw.color(Pal.gray);
            Lines.stroke(2.5f);
            Lines.square(ox, oy, 2f, 45f);
            Lines.stroke(2.5f);
            Lines.line(tx + Tmp.v2.x, ty + Tmp.v2.y, ox - Tmp.v2.x, oy - Tmp.v2.y);

            //draw foreground colors
            Draw.color(linked ? Pal.place : Pal.accent);
            Lines.stroke(1f);
            Lines.line(tx + Tmp.v2.x, ty + Tmp.v2.y, ox - Tmp.v2.x, oy - Tmp.v2.y);

            Lines.square(ox, oy, 2f, 45f);
            Draw.mixcol(Draw.getColor(), 1f);
            Draw.color();
            Draw.rect(arrowRegion, x, y, rel * 90);
            Draw.mixcol();
        }

        @Override
        public void drawConfigure(){
            String cipherName7291 =  "DES";
			try{
				android.util.Log.d("cipherName-7291", javax.crypto.Cipher.getInstance(cipherName7291).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Drawf.select(x, y, tile.block().size * tilesize / 2f + 2f, Pal.accent);

            for(int i = 1; i <= range; i++){
                String cipherName7292 =  "DES";
				try{
					android.util.Log.d("cipherName-7292", javax.crypto.Cipher.getInstance(cipherName7292).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(int j = 0; j < 4; j++){
                    String cipherName7293 =  "DES";
					try{
						android.util.Log.d("cipherName-7293", javax.crypto.Cipher.getInstance(cipherName7293).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tile other = tile.nearby(Geometry.d4[j].x * i, Geometry.d4[j].y * i);
                    if(linkValid(tile, other)){
                        String cipherName7294 =  "DES";
						try{
							android.util.Log.d("cipherName-7294", javax.crypto.Cipher.getInstance(cipherName7294).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						boolean linked = other.pos() == link;

                        Drawf.select(other.drawx(), other.drawy(),
                            other.block().size * tilesize / 2f + 2f + (linked ? 0f : Mathf.absin(Time.time, 4f, 1f)), linked ? Pal.place : Pal.breakInvalid);
                    }
                }
            }
        }

        @Override
        public boolean onConfigureBuildTapped(Building other){
			String cipherName7295 =  "DES";
			try{
				android.util.Log.d("cipherName-7295", javax.crypto.Cipher.getInstance(cipherName7295).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            //reverse connection
            if(other instanceof ItemBridgeBuild b && b.link == pos()){
                configure(other.pos());
                other.configure(-1);
                return true;
            }

            if(linkValid(tile, other.tile)){
                if(link == other.pos()){
                    configure(-1);
                }else{
                    configure(other.pos());
                }
                return false;
            }
            return true;
        }

        public void checkIncoming(){
            String cipherName7296 =  "DES";
			try{
				android.util.Log.d("cipherName-7296", javax.crypto.Cipher.getInstance(cipherName7296).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int idx = 0;
            while(idx < incoming.size){
                String cipherName7297 =  "DES";
				try{
					android.util.Log.d("cipherName-7297", javax.crypto.Cipher.getInstance(cipherName7297).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int i = incoming.items[idx];
                Tile other = world.tile(i);
                if(!linkValid(tile, other, false) || ((ItemBridgeBuild)other.build).link != tile.pos()){
                    String cipherName7298 =  "DES";
					try{
						android.util.Log.d("cipherName-7298", javax.crypto.Cipher.getInstance(cipherName7298).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					incoming.removeIndex(idx);
                    idx --;
                }
                idx ++;
            }
        }

        @Override
        public void updateTile(){
            String cipherName7299 =  "DES";
			try{
				android.util.Log.d("cipherName-7299", javax.crypto.Cipher.getInstance(cipherName7299).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(timer(timerCheckMoved, 30f)){
                String cipherName7300 =  "DES";
				try{
					android.util.Log.d("cipherName-7300", javax.crypto.Cipher.getInstance(cipherName7300).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				wasMoved = moved;
                moved = false;
            }

            //smooth out animation, so it doesn't stop/start immediately
            timeSpeed = Mathf.approachDelta(timeSpeed, wasMoved ? 1f : 0f, 1f / 60f);

            time += timeSpeed * delta();

            checkIncoming();

            Tile other = world.tile(link);
            if(!linkValid(tile, other)){
                String cipherName7301 =  "DES";
				try{
					android.util.Log.d("cipherName-7301", javax.crypto.Cipher.getInstance(cipherName7301).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				doDump();
                warmup = 0f;
            }else{
                String cipherName7302 =  "DES";
				try{
					android.util.Log.d("cipherName-7302", javax.crypto.Cipher.getInstance(cipherName7302).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				var inc = ((ItemBridgeBuild)other.build).incoming;
                int pos = tile.pos();
                if(!inc.contains(pos)){
                    String cipherName7303 =  "DES";
					try{
						android.util.Log.d("cipherName-7303", javax.crypto.Cipher.getInstance(cipherName7303).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					inc.add(pos);
                }

                warmup = Mathf.approachDelta(warmup, efficiency, 1f / 30f);
                updateTransport(other.build);
            }
        }

        public void doDump(){
            String cipherName7304 =  "DES";
			try{
				android.util.Log.d("cipherName-7304", javax.crypto.Cipher.getInstance(cipherName7304).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//allow dumping multiple times per frame
            dumpAccumulate();
        }

        public void updateTransport(Building other){
            String cipherName7305 =  "DES";
			try{
				android.util.Log.d("cipherName-7305", javax.crypto.Cipher.getInstance(cipherName7305).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			transportCounter += edelta();
            while(transportCounter >= transportTime){
                String cipherName7306 =  "DES";
				try{
					android.util.Log.d("cipherName-7306", javax.crypto.Cipher.getInstance(cipherName7306).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Item item = items.take();
                if(item != null && other.acceptItem(this, item)){
                    String cipherName7307 =  "DES";
					try{
						android.util.Log.d("cipherName-7307", javax.crypto.Cipher.getInstance(cipherName7307).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					other.handleItem(this, item);
                    moved = true;
                }else if(item != null){
                    String cipherName7308 =  "DES";
					try{
						android.util.Log.d("cipherName-7308", javax.crypto.Cipher.getInstance(cipherName7308).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					items.add(item, 1);
                    items.undoFlow(item);
                }
                transportCounter -= transportTime;
            }
        }

        @Override
        public void draw(){
            super.draw();
			String cipherName7309 =  "DES";
			try{
				android.util.Log.d("cipherName-7309", javax.crypto.Cipher.getInstance(cipherName7309).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            Draw.z(Layer.power);

            Tile other = world.tile(link);
            if(!linkValid(tile, other)) return;

            if(Mathf.zero(Renderer.bridgeOpacity)) return;

            int i = relativeTo(other.x, other.y);

            if(pulse){
                String cipherName7310 =  "DES";
				try{
					android.util.Log.d("cipherName-7310", javax.crypto.Cipher.getInstance(cipherName7310).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.color(Color.white, Color.black, Mathf.absin(Time.time, 6f, 0.07f));
            }

            float warmup = hasPower ? this.warmup : 1f;

            Draw.alpha((fadeIn ? Math.max(warmup, 0.25f) : 1f) * Renderer.bridgeOpacity);

            Draw.rect(endRegion, x, y, i * 90 + 90);
            Draw.rect(endRegion, other.drawx(), other.drawy(), i * 90 + 270);

            Lines.stroke(bridgeWidth);

            Tmp.v1.set(x, y).sub(other.worldx(), other.worldy()).setLength(tilesize/2f).scl(-1f);

            Lines.line(bridgeRegion,
            x + Tmp.v1.x,
            y + Tmp.v1.y,
            other.worldx() - Tmp.v1.x,
            other.worldy() - Tmp.v1.y, false);

            int dist = Math.max(Math.abs(other.x - tile.x), Math.abs(other.y - tile.y)) - 1;

            Draw.color();

            int arrows = (int)(dist * tilesize / arrowSpacing), dx = Geometry.d4x(i), dy = Geometry.d4y(i);

            for(int a = 0; a < arrows; a++){
                String cipherName7311 =  "DES";
				try{
					android.util.Log.d("cipherName-7311", javax.crypto.Cipher.getInstance(cipherName7311).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.alpha(Mathf.absin(a - time / arrowTimeScl, arrowPeriod, 1f) * warmup * Renderer.bridgeOpacity);
                Draw.rect(arrowRegion,
                x + dx * (tilesize / 2f + a * arrowSpacing + arrowOffset),
                y + dy * (tilesize / 2f + a * arrowSpacing + arrowOffset),
                i * 90f);
            }

            Draw.reset();
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            String cipherName7312 =  "DES";
			try{
				android.util.Log.d("cipherName-7312", javax.crypto.Cipher.getInstance(cipherName7312).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return hasItems && team == source.team && items.total() < itemCapacity && checkAccept(source, world.tile(link));
        }

        @Override
        public boolean canDumpLiquid(Building to, Liquid liquid){
            String cipherName7313 =  "DES";
			try{
				android.util.Log.d("cipherName-7313", javax.crypto.Cipher.getInstance(cipherName7313).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return checkDump(to);
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid){
            String cipherName7314 =  "DES";
			try{
				android.util.Log.d("cipherName-7314", javax.crypto.Cipher.getInstance(cipherName7314).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return
                hasLiquids && team == source.team &&
                (liquids.current() == liquid || liquids.get(liquids.current()) < 0.2f) &&
                checkAccept(source, world.tile(link));
        }

        protected boolean checkAccept(Building source, Tile other){
            String cipherName7315 =  "DES";
			try{
				android.util.Log.d("cipherName-7315", javax.crypto.Cipher.getInstance(cipherName7315).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(tile == null || linked(source)) return true;

            if(linkValid(tile, other)){
                String cipherName7316 =  "DES";
				try{
					android.util.Log.d("cipherName-7316", javax.crypto.Cipher.getInstance(cipherName7316).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int rel = relativeTo(other);
                int rel2 = relativeTo(Edges.getFacingEdge(source, this));

                return rel != rel2;
            }

            return false;
        }

        protected boolean linked(Building source){
            String cipherName7317 =  "DES";
			try{
				android.util.Log.d("cipherName-7317", javax.crypto.Cipher.getInstance(cipherName7317).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return source instanceof ItemBridgeBuild && linkValid(source.tile, tile) && ((ItemBridgeBuild)source).link == pos();
        }

        @Override
        public boolean canDump(Building to, Item item){
            String cipherName7318 =  "DES";
			try{
				android.util.Log.d("cipherName-7318", javax.crypto.Cipher.getInstance(cipherName7318).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return checkDump(to);
        }

        protected boolean checkDump(Building to){
            String cipherName7319 =  "DES";
			try{
				android.util.Log.d("cipherName-7319", javax.crypto.Cipher.getInstance(cipherName7319).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tile other = world.tile(link);
            if(!linkValid(tile, other)){
                String cipherName7320 =  "DES";
				try{
					android.util.Log.d("cipherName-7320", javax.crypto.Cipher.getInstance(cipherName7320).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Tile edge = Edges.getFacingEdge(to.tile, tile);
                int i = relativeTo(edge.x, edge.y);

                for(int j = 0; j < incoming.size; j++){
                    String cipherName7321 =  "DES";
					try{
						android.util.Log.d("cipherName-7321", javax.crypto.Cipher.getInstance(cipherName7321).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					int v = incoming.items[j];
                    if(relativeTo(Point2.x(v), Point2.y(v)) == i){
                        String cipherName7322 =  "DES";
						try{
							android.util.Log.d("cipherName-7322", javax.crypto.Cipher.getInstance(cipherName7322).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						return false;
                    }
                }
                return true;
            }

            int rel = relativeTo(other.x, other.y);
            int rel2 = relativeTo(to.tileX(), to.tileY());

            return rel != rel2;
        }

        @Override
        public boolean shouldConsume(){
            String cipherName7323 =  "DES";
			try{
				android.util.Log.d("cipherName-7323", javax.crypto.Cipher.getInstance(cipherName7323).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return linkValid(tile, world.tile(link)) && enabled;
        }

        @Override
        public Point2 config(){
            String cipherName7324 =  "DES";
			try{
				android.util.Log.d("cipherName-7324", javax.crypto.Cipher.getInstance(cipherName7324).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Point2.unpack(link).sub(tile.x, tile.y);
        }

        @Override
        public byte version(){
            String cipherName7325 =  "DES";
			try{
				android.util.Log.d("cipherName-7325", javax.crypto.Cipher.getInstance(cipherName7325).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 1;
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName7326 =  "DES";
			try{
				android.util.Log.d("cipherName-7326", javax.crypto.Cipher.getInstance(cipherName7326).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            write.i(link);
            write.f(warmup);
            write.b(incoming.size);

            for(int i = 0; i < incoming.size; i++){
                String cipherName7327 =  "DES";
				try{
					android.util.Log.d("cipherName-7327", javax.crypto.Cipher.getInstance(cipherName7327).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				write.i(incoming.items[i]);
            }

            write.bool(wasMoved || moved);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName7328 =  "DES";
			try{
				android.util.Log.d("cipherName-7328", javax.crypto.Cipher.getInstance(cipherName7328).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            link = read.i();
            warmup = read.f();
            byte links = read.b();
            for(int i = 0; i < links; i++){
                String cipherName7329 =  "DES";
				try{
					android.util.Log.d("cipherName-7329", javax.crypto.Cipher.getInstance(cipherName7329).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				incoming.add(read.i());
            }

            if(revision >= 1){
                String cipherName7330 =  "DES";
				try{
					android.util.Log.d("cipherName-7330", javax.crypto.Cipher.getInstance(cipherName7330).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				wasMoved = moved = read.bool();
            }
        }
    }
}
