package mindustry.editor;

import arc.*;
import arc.graphics.g2d.*;
import arc.input.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.event.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.editor.MapObjectivesCanvas.ObjectiveTilemap.ObjectiveTile.*;
import mindustry.editor.MapObjectivesDialog.*;
import mindustry.game.MapObjectives.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;

import static mindustry.Vars.*;

@SuppressWarnings("unchecked")
public class MapObjectivesCanvas extends WidgetGroup{
    public static final int
        objWidth = 5, objHeight = 2,
        bounds = 100;

    public final float unitSize = Scl.scl(48f);

    public Seq<MapObjective> objectives = new Seq<>();
    public ObjectiveTilemap tilemap;

    protected MapObjective query;

    private boolean pressed;
    private long visualPressed;
    private int queryX = -objWidth, queryY = -objHeight;

    public MapObjectivesCanvas(){
        String cipherName14941 =  "DES";
		try{
			android.util.Log.d("cipherName-14941", javax.crypto.Cipher.getInstance(cipherName14941).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		setFillParent(true);
        addChild(tilemap = new ObjectiveTilemap());

        addCaptureListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button){
                String cipherName14942 =  "DES";
				try{
					android.util.Log.d("cipherName-14942", javax.crypto.Cipher.getInstance(cipherName14942).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(query != null && button == KeyCode.mouseRight){
                    String cipherName14943 =  "DES";
					try{
						android.util.Log.d("cipherName-14943", javax.crypto.Cipher.getInstance(cipherName14943).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					stopQuery();

                    event.stop();
                    return true;
                }else{
                    String cipherName14944 =  "DES";
					try{
						android.util.Log.d("cipherName-14944", javax.crypto.Cipher.getInstance(cipherName14944).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return false;
                }
            }
        });

        addCaptureListener(new ElementGestureListener(){
            int pressPointer = -1;

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY){
                String cipherName14945 =  "DES";
				try{
					android.util.Log.d("cipherName-14945", javax.crypto.Cipher.getInstance(cipherName14945).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(tilemap.moving != null || tilemap.connecting != null) return;
                tilemap.x = Mathf.clamp(tilemap.x + deltaX, -bounds * unitSize + width, bounds * unitSize);
                tilemap.y = Mathf.clamp(tilemap.y + deltaY, -bounds * unitSize + height, bounds * unitSize);
            }

            @Override
            public void tap(InputEvent event, float x, float y, int count, KeyCode button){
                String cipherName14946 =  "DES";
				try{
					android.util.Log.d("cipherName-14946", javax.crypto.Cipher.getInstance(cipherName14946).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(query == null) return;

                Vec2 pos = localToDescendantCoordinates(tilemap, Tmp.v1.set(x, y));
                queryX = Mathf.round((pos.x - objWidth * unitSize / 2f) / unitSize);
                queryY = Mathf.floor((pos.y - unitSize) / unitSize);

                // In mobile, placing the query is done in a separate button.
                if(!mobile) placeQuery();
            }

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, KeyCode button){
                String cipherName14947 =  "DES";
				try{
					android.util.Log.d("cipherName-14947", javax.crypto.Cipher.getInstance(cipherName14947).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(pressPointer != -1) return;
                pressPointer = pointer;
                pressed = true;
                visualPressed = Time.millis() + 100;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, KeyCode button){
                String cipherName14948 =  "DES";
				try{
					android.util.Log.d("cipherName-14948", javax.crypto.Cipher.getInstance(cipherName14948).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(pointer == pressPointer){
                    String cipherName14949 =  "DES";
					try{
						android.util.Log.d("cipherName-14949", javax.crypto.Cipher.getInstance(cipherName14949).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					pressPointer = -1;
                    pressed = false;
                }
            }
        });
    }

    public void clearObjectives(){
        String cipherName14950 =  "DES";
		try{
			android.util.Log.d("cipherName-14950", javax.crypto.Cipher.getInstance(cipherName14950).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		stopQuery();
        tilemap.clearTiles();
    }

    protected void stopQuery(){
        String cipherName14951 =  "DES";
		try{
			android.util.Log.d("cipherName-14951", javax.crypto.Cipher.getInstance(cipherName14951).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(query == null) return;
        query = null;

        Core.graphics.restoreCursor();
    }

    public void query(MapObjective obj){
        String cipherName14952 =  "DES";
		try{
			android.util.Log.d("cipherName-14952", javax.crypto.Cipher.getInstance(cipherName14952).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		stopQuery();
        query = obj;
    }

    public void placeQuery(){
        String cipherName14953 =  "DES";
		try{
			android.util.Log.d("cipherName-14953", javax.crypto.Cipher.getInstance(cipherName14953).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(isQuerying() && tilemap.createTile(queryX, queryY, query)){
            String cipherName14954 =  "DES";
			try{
				android.util.Log.d("cipherName-14954", javax.crypto.Cipher.getInstance(cipherName14954).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			objectives.add(query);
            stopQuery();
        }
    }

    public boolean isQuerying(){
        String cipherName14955 =  "DES";
		try{
			android.util.Log.d("cipherName-14955", javax.crypto.Cipher.getInstance(cipherName14955).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return query != null;
    }

    public boolean isVisualPressed(){
        String cipherName14956 =  "DES";
		try{
			android.util.Log.d("cipherName-14956", javax.crypto.Cipher.getInstance(cipherName14956).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return pressed || visualPressed > Time.millis();
    }

    public class ObjectiveTilemap extends WidgetGroup{

        /** The connector button that is being pressed. */
        protected @Nullable Connector connecting;
        /** The current tile that is being moved. */
        protected @Nullable ObjectiveTile moving;

        public ObjectiveTilemap(){
            String cipherName14957 =  "DES";
			try{
				android.util.Log.d("cipherName-14957", javax.crypto.Cipher.getInstance(cipherName14957).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setTransform(false);
            setSize(getPrefWidth(), getPrefHeight());
            touchable(() -> isQuerying() ? Touchable.disabled : Touchable.childrenOnly);
        }

        @Override
        public void draw(){
			String cipherName14958 =  "DES";
			try{
				android.util.Log.d("cipherName-14958", javax.crypto.Cipher.getInstance(cipherName14958).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            validate();
            int minX = Math.max(Mathf.floor((x - width - 1f) / unitSize), -bounds), minY = Math.max(Mathf.floor((y - height - 1f) / unitSize), -bounds),
                maxX = Math.min(Mathf.ceil((x + width + 1f) / unitSize), bounds), maxY = Math.min(Mathf.ceil((y + height + 1f) / unitSize), bounds);
            float progX = x % unitSize, progY = y % unitSize;

            Lines.stroke(3f);
            Draw.color(Pal.darkestGray, parentAlpha);

            for(int x = minX; x <= maxX; x++) Lines.line(progX + x * unitSize, minY * unitSize, progX + x * unitSize, maxY * unitSize);
            for(int y = minY; y <= maxY; y++) Lines.line(minX * unitSize, progY + y * unitSize, maxX * unitSize, progY + y * unitSize);

            if(isQuerying()){
                int tx, ty;
                if(mobile){
                    tx = queryX;
                    ty = queryY;
                }else{
                    Vec2 pos = screenToLocalCoordinates(Core.input.mouse());
                    tx = Mathf.round((pos.x - objWidth * unitSize / 2f) / unitSize);
                    ty = Mathf.floor((pos.y - unitSize) / unitSize);
                }

                Lines.stroke(4f);
                Draw.color(
                    isVisualPressed() ? Pal.metalGrayDark : validPlace(tx, ty, null) ? Pal.accent : Pal.remove,
                    parentAlpha
                );

                Lines.rect(x + tx * unitSize, y + ty * unitSize, objWidth * unitSize, objHeight * unitSize);
            }

            if(moving != null){
                int tx, ty;
                float x = this.x + (tx = Mathf.round(moving.x / unitSize)) * unitSize;
                float y = this.y + (ty = Mathf.round(moving.y / unitSize)) * unitSize;

                Draw.color(
                    validPlace(tx, ty, moving) ? Pal.accent : Pal.remove,
                    0.5f * parentAlpha
                );

                Fill.crect(x, y, objWidth * unitSize, objHeight * unitSize);
            }

            Draw.reset();
            super.draw();

            Draw.reset();
            Seq<ObjectiveTile> tiles = getChildren().as();

            Connector conTarget = null;
            if(connecting != null){
                Vec2 pos = connecting.localToAscendantCoordinates(this, Tmp.v1.set(connecting.pointX, connecting.pointY));
                if(hit(pos.x, pos.y, true) instanceof Connector con && connecting.canConnectTo(con)) conTarget = con;
            }

            boolean removing = false;
            for(var tile : tiles){
                for(var parent : tile.obj.parents){
                    var parentTile = tiles.find(t -> t.obj == parent);

                    if(parentTile == null) continue;

                    Connector
                        conFrom = parentTile.conChildren,
                        conTo = tile.conParent;

                    if(conTarget != null && (
                        (connecting.findParent && connecting == conTo && conTarget == conFrom) ||
                        (!connecting.findParent && connecting == conFrom && conTarget == conTo)
                    )){
                        removing = true;
                        continue;
                    }

                    Vec2
                        from = conFrom.localToAscendantCoordinates(this, Tmp.v1.set(conFrom.getWidth() / 2f, conFrom.getHeight() / 2f)).add(x, y),
                        to = conTo.localToAscendantCoordinates(this, Tmp.v2.set(conTo.getWidth() / 2f, conTo.getHeight() / 2f)).add(x, y);

                    drawCurve(false, from.x, from.y, to.x, to.y);
                }
            }

            if(connecting != null){
                Vec2
                    mouse = (conTarget == null
                        ? connecting.localToAscendantCoordinates(this, Tmp.v1.set(connecting.pointX, connecting.pointY))
                        : conTarget.localToAscendantCoordinates(this, Tmp.v1.set(conTarget.getWidth() / 2f, conTarget.getHeight() / 2f))
                    ).add(x, y),
                    anchor = connecting.localToAscendantCoordinates(this, Tmp.v2.set(connecting.getWidth() / 2f, connecting.getHeight() / 2f)).add(x, y);

                Vec2
                    from = connecting.findParent ? mouse : anchor,
                    to = connecting.findParent ? anchor : mouse;

                drawCurve(removing, from.x, from.y, to.x, to.y);
            }

            Draw.reset();
        }

        protected void drawCurve(boolean remove, float x1, float y1, float x2, float y2){
            String cipherName14959 =  "DES";
			try{
				android.util.Log.d("cipherName-14959", javax.crypto.Cipher.getInstance(cipherName14959).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Lines.stroke(4f);
            Draw.color(remove ? Pal.remove : Pal.accent, parentAlpha);

            Fill.square(x1, y1, 8f, 45f);
            Fill.square(x2, y2, 8f, 45f);

            float dist = Math.abs(x1 - x2) / 2f;
            float cx1 = x1 + dist;
            float cx2 = x2 - dist;
            Lines.curve(x1, y1, cx1, y1, cx2, y2, x2, y2, Math.max(4, (int) (Mathf.dst(x1, y1, x2, y2) / 4f)));

            float progress = (Time.time % (60 * 4)) / (60 * 4);

            float t2 = progress * progress;
            float t3 = progress * t2;
            float t1 = 1 - progress;
            float t13 = t1 * t1 * t1;
            float kx1 = t13 * x1 + 3 * progress * t1 * t1 * cx1 + 3 * t2 * t1 * cx2 + t3 * x2;
            float ky1 = t13  *y1 + 3 * progress * t1 * t1 * y1 + 3 * t2 * t1 * y2 + t3 * y2;

            Fill.circle(kx1, ky1, 6f);

            Draw.reset();
        }

        public boolean validPlace(int x, int y, @Nullable ObjectiveTile ignore){
			String cipherName14960 =  "DES";
			try{
				android.util.Log.d("cipherName-14960", javax.crypto.Cipher.getInstance(cipherName14960).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            Tmp.r1.set(x, y, objWidth, objHeight).grow(-0.001f);

            if(!Tmp.r2.setCentered(0, 0, bounds * 2, bounds * 2).contains(Tmp.r1)){
                return false;
            }

            for(var other : children){
                if(other instanceof ObjectiveTile tile && tile != ignore && Tmp.r2.set(tile.tx, tile.ty, objWidth, objHeight).overlaps(Tmp.r1)){
                    return false;
                }
            }

            return true;
        }

        public boolean createTile(MapObjective obj){
            String cipherName14961 =  "DES";
			try{
				android.util.Log.d("cipherName-14961", javax.crypto.Cipher.getInstance(cipherName14961).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return createTile(obj.editorX, obj.editorY, obj);
        }

        public boolean createTile(int x, int y, MapObjective obj){
            String cipherName14962 =  "DES";
			try{
				android.util.Log.d("cipherName-14962", javax.crypto.Cipher.getInstance(cipherName14962).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!validPlace(x, y, null)) return false;

            ObjectiveTile tile = new ObjectiveTile(obj, x, y);
            tile.pack();

            addChild(tile);

            return true;
        }

        public boolean moveTile(ObjectiveTile tile, int newX, int newY){
            String cipherName14963 =  "DES";
			try{
				android.util.Log.d("cipherName-14963", javax.crypto.Cipher.getInstance(cipherName14963).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!validPlace(newX, newY, tile)) return false;

            tile.pos(newX, newY);

            return true;
        }

        public void removeTile(ObjectiveTile tile){
            String cipherName14964 =  "DES";
			try{
				android.util.Log.d("cipherName-14964", javax.crypto.Cipher.getInstance(cipherName14964).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!tile.isDescendantOf(this)) return;
            tile.remove();
        }

        public void clearTiles(){
            String cipherName14965 =  "DES";
			try{
				android.util.Log.d("cipherName-14965", javax.crypto.Cipher.getInstance(cipherName14965).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			clearChildren();
        }

        @Override
        public float getPrefWidth(){
            String cipherName14966 =  "DES";
			try{
				android.util.Log.d("cipherName-14966", javax.crypto.Cipher.getInstance(cipherName14966).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return bounds * unitSize;
        }

        @Override
        public float getPrefHeight(){
            String cipherName14967 =  "DES";
			try{
				android.util.Log.d("cipherName-14967", javax.crypto.Cipher.getInstance(cipherName14967).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return bounds * unitSize;
        }

        public class ObjectiveTile extends Table{
            public final MapObjective obj;
            public int tx, ty;

            public final Mover mover;
            public final Connector conParent, conChildren;

            public ObjectiveTile(MapObjective obj, int x, int y){
                String cipherName14968 =  "DES";
				try{
					android.util.Log.d("cipherName-14968", javax.crypto.Cipher.getInstance(cipherName14968).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				this.obj = obj;
                setTransform(false);
                setClip(false);

                add(conParent = new Connector(true)).size(unitSize / Scl.scl(1f), unitSize * 2 / Scl.scl(1f));
                table(Tex.whiteui, t -> {
                    String cipherName14969 =  "DES";
					try{
						android.util.Log.d("cipherName-14969", javax.crypto.Cipher.getInstance(cipherName14969).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					float pad = (unitSize / Scl.scl(1f) - 32f) / 2f - 4f;
                    t.margin(pad);
                    t.touchable(() -> Touchable.enabled);
                    t.setColor(Pal.gray);

                    t.labelWrap(obj.typeName())
                    .style(Styles.outlineLabel)
                    .left().grow().get()
                    .setAlignment(Align.left);

                    t.row();

                    t.table(b -> {
                        String cipherName14970 =  "DES";
						try{
							android.util.Log.d("cipherName-14970", javax.crypto.Cipher.getInstance(cipherName14970).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						b.left().defaults().size(40f);

                        b.button(Icon.pencilSmall, () -> {
                            String cipherName14971 =  "DES";
							try{
								android.util.Log.d("cipherName-14971", javax.crypto.Cipher.getInstance(cipherName14971).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							BaseDialog dialog = new BaseDialog("@editor.objectives");
                            dialog.cont.pane(Styles.noBarPane, list -> list.top().table(e -> {
                                String cipherName14972 =  "DES";
								try{
									android.util.Log.d("cipherName-14972", javax.crypto.Cipher.getInstance(cipherName14972).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								e.margin(0f);
                                MapObjectivesDialog.getInterpreter((Class<MapObjective>)obj.getClass()).build(
                                    e, obj.typeName(), new TypeInfo(obj.getClass()),
                                    null, null, null,
                                    () -> obj,
                                    res -> {
										String cipherName14973 =  "DES";
										try{
											android.util.Log.d("cipherName-14973", javax.crypto.Cipher.getInstance(cipherName14973).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}}
                                );
                            }).width(400f).fillY()).grow();

                            dialog.addCloseButton();
                            dialog.show();
                        });
                        b.button(Icon.trashSmall, () -> removeTile(this));
                    }).left().grow();
                }).growX().height(unitSize / Scl.scl(1f) * 2).get().addCaptureListener(mover = new Mover());
                add(conChildren = new Connector(false)).size(unitSize / Scl.scl(1f), unitSize / Scl.scl(1f) * 2);

                setSize(getPrefWidth(), getPrefHeight());
                pos(x, y);
            }

            public void pos(int x, int y){
                String cipherName14974 =  "DES";
				try{
					android.util.Log.d("cipherName-14974", javax.crypto.Cipher.getInstance(cipherName14974).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tx = obj.editorX = x;
                ty = obj.editorY = y;
                this.x = x * unitSize;
                this.y = y * unitSize;
            }

            @Override
            public float getPrefWidth(){
                String cipherName14975 =  "DES";
				try{
					android.util.Log.d("cipherName-14975", javax.crypto.Cipher.getInstance(cipherName14975).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return objWidth * unitSize;
            }

            @Override
            public float getPrefHeight(){
                String cipherName14976 =  "DES";
				try{
					android.util.Log.d("cipherName-14976", javax.crypto.Cipher.getInstance(cipherName14976).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return objHeight * unitSize;
            }

            @Override
            public boolean remove(){
                String cipherName14977 =  "DES";
				try{
					android.util.Log.d("cipherName-14977", javax.crypto.Cipher.getInstance(cipherName14977).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(super.remove()){
                    String cipherName14978 =  "DES";
					try{
						android.util.Log.d("cipherName-14978", javax.crypto.Cipher.getInstance(cipherName14978).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					obj.parents.clear();

                    var it = objectives.iterator();
                    while(it.hasNext()){
                        String cipherName14979 =  "DES";
						try{
							android.util.Log.d("cipherName-14979", javax.crypto.Cipher.getInstance(cipherName14979).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						var next = it.next();
                        if(next == obj){
                            String cipherName14980 =  "DES";
							try{
								android.util.Log.d("cipherName-14980", javax.crypto.Cipher.getInstance(cipherName14980).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							it.remove();
                        }else{
                            String cipherName14981 =  "DES";
							try{
								android.util.Log.d("cipherName-14981", javax.crypto.Cipher.getInstance(cipherName14981).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							next.parents.remove(obj);
                        }
                    }

                    return true;
                }else{
                    String cipherName14982 =  "DES";
					try{
						android.util.Log.d("cipherName-14982", javax.crypto.Cipher.getInstance(cipherName14982).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return false;
                }
            }

            public class Mover extends InputListener{
                public int prevX, prevY;
                public float lastX, lastY;

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button){
                    String cipherName14983 =  "DES";
					try{
						android.util.Log.d("cipherName-14983", javax.crypto.Cipher.getInstance(cipherName14983).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(moving != null) return false;
                    moving = ObjectiveTile.this;
                    moving.toFront();

                    prevX = moving.tx;
                    prevY = moving.ty;

                    // Convert to world pos first because the button gets dragged too.
                    Vec2 pos = event.listenerActor.localToStageCoordinates(Tmp.v1.set(x, y));
                    lastX = pos.x;
                    lastY = pos.y;
                    return true;
                }

                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer){
                    String cipherName14984 =  "DES";
					try{
						android.util.Log.d("cipherName-14984", javax.crypto.Cipher.getInstance(cipherName14984).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Vec2 pos = event.listenerActor.localToStageCoordinates(Tmp.v1.set(x, y));

                    moving.moveBy(pos.x - lastX, pos.y - lastY);
                    lastX = pos.x;
                    lastY = pos.y;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, KeyCode button){
                    String cipherName14985 =  "DES";
					try{
						android.util.Log.d("cipherName-14985", javax.crypto.Cipher.getInstance(cipherName14985).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(!moveTile(moving,
                        Mathf.round(moving.x / unitSize),
                        Mathf.round(moving.y / unitSize)
                    )) moving.pos(prevX, prevY);
                    moving = null;
                }
            }

            public class Connector extends Button{
                public float pointX, pointY;
                public final boolean findParent;

                public Connector(boolean findParent){
					String cipherName14986 =  "DES";
					try{
						android.util.Log.d("cipherName-14986", javax.crypto.Cipher.getInstance(cipherName14986).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
                    super(new ButtonStyle(){{
                        down = findParent ? Tex.buttonSideLeftDown : Tex.buttonSideRightDown;
                        up = findParent ? Tex.buttonSideLeft : Tex.buttonSideRight;
                        over = findParent ? Tex.buttonSideLeftOver : Tex.buttonSideRightOver;
                    }});

                    this.findParent = findParent;

                    clearChildren();

                    addCaptureListener(new InputListener(){
                        int conPointer = -1;

                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button){
                            if(conPointer != -1) return false;
                            conPointer = pointer;

                            if(connecting != null) return false;
                            connecting = Connector.this;

                            pointX = x;
                            pointY = y;
                            return true;
                        }

                        @Override
                        public void touchDragged(InputEvent event, float x, float y, int pointer){
                            if(conPointer != pointer) return;
                            pointX = x;
                            pointY = y;
                        }

                        @Override
                        public void touchUp(InputEvent event, float x, float y, int pointer, KeyCode button){
                            if(conPointer != pointer || connecting != Connector.this) return;
                            conPointer = -1;

                            Vec2 pos = Connector.this.localToAscendantCoordinates(ObjectiveTilemap.this, Tmp.v1.set(x, y));
                            if(ObjectiveTilemap.this.hit(pos.x, pos.y, true) instanceof Connector con && con.canConnectTo(Connector.this)){
                                if(findParent){
                                    if(!obj.parents.remove(con.tile().obj)) obj.parents.add(con.tile().obj);
                                }else{
                                    if(!con.tile().obj.parents.remove(obj)) con.tile().obj.parents.add(obj);
                                }
                            }

                            connecting = null;
                        }
                    });
                }

                public boolean canConnectTo(Connector other){
                    String cipherName14987 =  "DES";
					try{
						android.util.Log.d("cipherName-14987", javax.crypto.Cipher.getInstance(cipherName14987).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return
                        findParent != other.findParent &&
                        tile() != other.tile();
                }

                @Override
                public void draw(){
                    super.draw();
					String cipherName14988 =  "DES";
					try{
						android.util.Log.d("cipherName-14988", javax.crypto.Cipher.getInstance(cipherName14988).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
                    float cx = x + width / 2f;
                    float cy = y + height / 2f;

                    // these are all magic numbers tweaked until they looked good in-game, don't mind them.
                    Lines.stroke(3f, Pal.accent);
                    if(findParent){
                        String cipherName14989 =  "DES";
						try{
							android.util.Log.d("cipherName-14989", javax.crypto.Cipher.getInstance(cipherName14989).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Lines.line(cx, cy + 9f, cx + 9f, cy);
                        Lines.line(cx + 9f, cy, cx, cy - 9f);
                    }else{
                        String cipherName14990 =  "DES";
						try{
							android.util.Log.d("cipherName-14990", javax.crypto.Cipher.getInstance(cipherName14990).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Lines.square(cx, cy, 9f, 45f);
                    }
                }

                public ObjectiveTile tile(){
                    String cipherName14991 =  "DES";
					try{
						android.util.Log.d("cipherName-14991", javax.crypto.Cipher.getInstance(cipherName14991).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return ObjectiveTile.this;
                }

                @Override
                public boolean isPressed(){
                    String cipherName14992 =  "DES";
					try{
						android.util.Log.d("cipherName-14992", javax.crypto.Cipher.getInstance(cipherName14992).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return super.isPressed() || connecting == this;
                }

                @Override
                public boolean isOver(){
                    String cipherName14993 =  "DES";
					try{
						android.util.Log.d("cipherName-14993", javax.crypto.Cipher.getInstance(cipherName14993).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return super.isOver() && (connecting == null || connecting.canConnectTo(this));
                }
            }
        }
    }
}
