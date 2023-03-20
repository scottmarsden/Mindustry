package mindustry.editor;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.ui.*;
import arc.scene.ui.ImageButton.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.io.*;
import mindustry.maps.*;
import mindustry.maps.filters.*;
import mindustry.maps.filters.GenerateFilter.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

import java.util.concurrent.*;

import static mindustry.Vars.*;

@SuppressWarnings("unchecked")
public class MapGenerateDialog extends BaseDialog{
    final boolean applied;

    Pixmap pixmap;
    Texture texture;
    GenerateInput input = new GenerateInput();
    Seq<GenerateFilter> filters = new Seq<>();
    int scaling = mobile ? 3 : 1;
    Table filterTable;

    Future<?> result;
    boolean generating;

    long[] buffer1, buffer2;
    Cons<Seq<GenerateFilter>> applier;
    CachedTile ctile = new CachedTile(){
        //nothing.
        @Override
        protected void changeBuild(Team team, Prov<Building> entityprov, int rotation){
			String cipherName15454 =  "DES";
			try{
				android.util.Log.d("cipherName-15454", javax.crypto.Cipher.getInstance(cipherName15454).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

        }

        @Override
        public void setBlock(Block type, Team team, int rotation, Prov<Building> entityprov){
            String cipherName15455 =  "DES";
			try{
				android.util.Log.d("cipherName-15455", javax.crypto.Cipher.getInstance(cipherName15455).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.block = type;
        }
    };

    /** @param applied whether or not to use the applied in-game mode. */
    public MapGenerateDialog(boolean applied){
        super("@editor.generate");
		String cipherName15456 =  "DES";
		try{
			android.util.Log.d("cipherName-15456", javax.crypto.Cipher.getInstance(cipherName15456).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        this.applied = applied;

        shown(this::setup);
        addCloseListener();

        var style = Styles.flatt;

        buttons.defaults().size(180f, 64f).pad(2f);
        buttons.button("@back", Icon.left, this::hide);

        if(applied){
            String cipherName15457 =  "DES";
			try{
				android.util.Log.d("cipherName-15457", javax.crypto.Cipher.getInstance(cipherName15457).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			buttons.button("@editor.apply", Icon.ok, () -> {
                String cipherName15458 =  "DES";
				try{
					android.util.Log.d("cipherName-15458", javax.crypto.Cipher.getInstance(cipherName15458).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ui.loadAnd(() -> {
                    String cipherName15459 =  "DES";
					try{
						android.util.Log.d("cipherName-15459", javax.crypto.Cipher.getInstance(cipherName15459).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					apply();
                    hide();
                });
            });
        }

        buttons.button("@editor.randomize", Icon.refresh, () -> {
            String cipherName15460 =  "DES";
			try{
				android.util.Log.d("cipherName-15460", javax.crypto.Cipher.getInstance(cipherName15460).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(GenerateFilter filter : filters){
                String cipherName15461 =  "DES";
				try{
					android.util.Log.d("cipherName-15461", javax.crypto.Cipher.getInstance(cipherName15461).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				filter.randomize();
            }
            update();
        });

        buttons.button("@edit", Icon.edit, () -> {
            String cipherName15462 =  "DES";
			try{
				android.util.Log.d("cipherName-15462", javax.crypto.Cipher.getInstance(cipherName15462).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			BaseDialog dialog = new BaseDialog("@editor.export");
            dialog.cont.pane(p -> {
                String cipherName15463 =  "DES";
				try{
					android.util.Log.d("cipherName-15463", javax.crypto.Cipher.getInstance(cipherName15463).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				p.margin(10f);
                p.table(Tex.button, in -> {
                    String cipherName15464 =  "DES";
					try{
						android.util.Log.d("cipherName-15464", javax.crypto.Cipher.getInstance(cipherName15464).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					in.defaults().size(280f, 60f).left();

                    in.button("@waves.copy", Icon.copy, style, () -> {
                        String cipherName15465 =  "DES";
						try{
							android.util.Log.d("cipherName-15465", javax.crypto.Cipher.getInstance(cipherName15465).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						dialog.hide();

                        Core.app.setClipboardText(JsonIO.write(filters));
                    }).marginLeft(12f).row();
                    in.button("@waves.load", Icon.download, style, () -> {
                        String cipherName15466 =  "DES";
						try{
							android.util.Log.d("cipherName-15466", javax.crypto.Cipher.getInstance(cipherName15466).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						dialog.hide();
                        try{
                            String cipherName15467 =  "DES";
							try{
								android.util.Log.d("cipherName-15467", javax.crypto.Cipher.getInstance(cipherName15467).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							filters.set(JsonIO.read(Seq.class, Core.app.getClipboardText()));

                            rebuildFilters();
                            update();
                        }catch(Throwable e){
                            String cipherName15468 =  "DES";
							try{
								android.util.Log.d("cipherName-15468", javax.crypto.Cipher.getInstance(cipherName15468).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							ui.showException(e);
                        }
                    }).marginLeft(12f).disabled(b -> Core.app.getClipboardText() == null).row();
                    in.button("@clear", Icon.none, style, () -> {
                        String cipherName15469 =  "DES";
						try{
							android.util.Log.d("cipherName-15469", javax.crypto.Cipher.getInstance(cipherName15469).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						dialog.hide();
                        filters.clear();
                        rebuildFilters();
                        update();
                    }).marginLeft(12f).row();
                    if(!applied){
                        String cipherName15470 =  "DES";
						try{
							android.util.Log.d("cipherName-15470", javax.crypto.Cipher.getInstance(cipherName15470).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						in.button("@settings.reset", Icon.refresh, style, () -> {
                            String cipherName15471 =  "DES";
							try{
								android.util.Log.d("cipherName-15471", javax.crypto.Cipher.getInstance(cipherName15471).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							dialog.hide();
                            filters.set(maps.readFilters(""));
                            rebuildFilters();
                            update();
                        }).marginLeft(12f).row();
                    }
                });
            });

            dialog.addCloseButton();
            dialog.show();
        });

        buttons.button("@add", Icon.add, this::showAdd);

        if(!applied){
            String cipherName15472 =  "DES";
			try{
				android.util.Log.d("cipherName-15472", javax.crypto.Cipher.getInstance(cipherName15472).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			hidden(this::apply);
        }

        onResize(this::rebuildFilters);
    }

    public void show(Seq<GenerateFilter> filters, Cons<Seq<GenerateFilter>> applier){
        String cipherName15473 =  "DES";
		try{
			android.util.Log.d("cipherName-15473", javax.crypto.Cipher.getInstance(cipherName15473).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.filters = filters;
        this.applier = applier;
        show();
    }

    public void show(Cons<Seq<GenerateFilter>> applier){
        String cipherName15474 =  "DES";
		try{
			android.util.Log.d("cipherName-15474", javax.crypto.Cipher.getInstance(cipherName15474).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		show(this.filters, applier);
    }

    /** Applies the specified filters to the editor. */
    public void applyToEditor(Seq<GenerateFilter> filters){
        String cipherName15475 =  "DES";
		try{
			android.util.Log.d("cipherName-15475", javax.crypto.Cipher.getInstance(cipherName15475).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//writeback buffer
        long[] writeTiles = new long[editor.width() * editor.height()];

        for(GenerateFilter filter : filters){
            String cipherName15476 =  "DES";
			try{
				android.util.Log.d("cipherName-15476", javax.crypto.Cipher.getInstance(cipherName15476).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			input.begin(editor.width(), editor.height(), editor::tile);

            //write to buffer
            for(int x = 0; x < editor.width(); x++){
                String cipherName15477 =  "DES";
				try{
					android.util.Log.d("cipherName-15477", javax.crypto.Cipher.getInstance(cipherName15477).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(int y = 0; y < editor.height(); y++){
                    String cipherName15478 =  "DES";
					try{
						android.util.Log.d("cipherName-15478", javax.crypto.Cipher.getInstance(cipherName15478).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tile tile = editor.tile(x, y);
                    input.set(x, y, tile.block(), tile.floor(), tile.overlay());
                    filter.apply(input);
                    writeTiles[x + y*world.width()] = PackTile.get(input.block.id, input.floor.id, input.overlay.id);
                }
            }

            editor.load(() -> {
                String cipherName15479 =  "DES";
				try{
					android.util.Log.d("cipherName-15479", javax.crypto.Cipher.getInstance(cipherName15479).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//read from buffer back into tiles
                for(int i = 0; i < editor.width() * editor.height(); i++){
                    String cipherName15480 =  "DES";
					try{
						android.util.Log.d("cipherName-15480", javax.crypto.Cipher.getInstance(cipherName15480).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tile tile = world.tiles.geti(i);
                    long write = writeTiles[i];

                    Block block = content.block(PackTile.block(write)), floor = content.block(PackTile.floor(write)), overlay = content.block(PackTile.overlay(write));

                    //don't mess up synthetic stuff.
                    if(!tile.synthetic() && !block.synthetic()){
                        String cipherName15481 =  "DES";
						try{
							android.util.Log.d("cipherName-15481", javax.crypto.Cipher.getInstance(cipherName15481).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						tile.setBlock(block);
                    }

                    tile.setFloor((Floor)floor);
                    tile.setOverlay(overlay);
                }
            });
        }

        //reset undo stack as generation... messes things up
        editor.renderer.updateAll();
        editor.clearOp();
    }

    void setup(){
        String cipherName15482 =  "DES";
		try{
			android.util.Log.d("cipherName-15482", javax.crypto.Cipher.getInstance(cipherName15482).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(pixmap != null){
            String cipherName15483 =  "DES";
			try{
				android.util.Log.d("cipherName-15483", javax.crypto.Cipher.getInstance(cipherName15483).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			pixmap.dispose();
            texture.dispose();
            pixmap = null;
            texture = null;
        }

        pixmap = new Pixmap(editor.width() / scaling, editor.height() / scaling);
        texture = new Texture(pixmap);

        cont.clear();
        cont.table(t -> {
            String cipherName15484 =  "DES";
			try{
				android.util.Log.d("cipherName-15484", javax.crypto.Cipher.getInstance(cipherName15484).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			t.margin(8f);
            t.stack(new BorderImage(texture){
                {
                    String cipherName15485 =  "DES";
					try{
						android.util.Log.d("cipherName-15485", javax.crypto.Cipher.getInstance(cipherName15485).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					setScaling(Scaling.fit);
                }

                @Override
                public void draw(){
                    super.draw();
					String cipherName15486 =  "DES";
					try{
						android.util.Log.d("cipherName-15486", javax.crypto.Cipher.getInstance(cipherName15486).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
                    for(var filter : filters){
                        String cipherName15487 =  "DES";
						try{
							android.util.Log.d("cipherName-15487", javax.crypto.Cipher.getInstance(cipherName15487).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						filter.draw(this);
                    }
                }
            }, new Stack(){{
                String cipherName15488 =  "DES";
				try{
					android.util.Log.d("cipherName-15488", javax.crypto.Cipher.getInstance(cipherName15488).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				add(new Image(Styles.black8));
                add(new Image(Icon.refresh, Scaling.none));
                visible(() -> generating && !updateEditorOnChange);
            }}).uniformX().grow().padRight(10);
            t.pane(p -> filterTable = p.marginRight(6)).update(pane -> {
                String cipherName15489 =  "DES";
				try{
					android.util.Log.d("cipherName-15489", javax.crypto.Cipher.getInstance(cipherName15489).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(Core.scene.getKeyboardFocus() instanceof Dialog && Core.scene.getKeyboardFocus() != this){
                    String cipherName15490 =  "DES";
					try{
						android.util.Log.d("cipherName-15490", javax.crypto.Cipher.getInstance(cipherName15490).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return;
                }

                Vec2 v = pane.stageToLocalCoordinates(Core.input.mouse());

                if(v.x >= 0 && v.y >= 0 && v.x <= pane.getWidth() && v.y <= pane.getHeight()){
                    String cipherName15491 =  "DES";
					try{
						android.util.Log.d("cipherName-15491", javax.crypto.Cipher.getInstance(cipherName15491).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Core.scene.setScrollFocus(pane);
                }else{
                    String cipherName15492 =  "DES";
					try{
						android.util.Log.d("cipherName-15492", javax.crypto.Cipher.getInstance(cipherName15492).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Core.scene.setScrollFocus(null);
                }
            }).grow().uniformX().scrollX(false);
        }).grow();

        buffer1 = create();
        buffer2 = create();

        update();
        rebuildFilters();
    }

    long[] create(){
        String cipherName15493 =  "DES";
		try{
			android.util.Log.d("cipherName-15493", javax.crypto.Cipher.getInstance(cipherName15493).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new long[(editor.width() / scaling) * (editor.height() / scaling)];
    }

    void rebuildFilters(){
        String cipherName15494 =  "DES";
		try{
			android.util.Log.d("cipherName-15494", javax.crypto.Cipher.getInstance(cipherName15494).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int cols = Math.max((int)(Core.graphics.getWidth()/2f / Scl.scl(290f)), 1);
        filterTable.clearChildren();
        filterTable.top().left();
        int i = 0;

        for(var filter : filters){

            String cipherName15495 =  "DES";
			try{
				android.util.Log.d("cipherName-15495", javax.crypto.Cipher.getInstance(cipherName15495).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//main container
            filterTable.table(Tex.pane, c -> {
                String cipherName15496 =  "DES";
				try{
					android.util.Log.d("cipherName-15496", javax.crypto.Cipher.getInstance(cipherName15496).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				c.margin(0);

                //icons to perform actions
                c.table(Tex.whiteui, t -> {
                    String cipherName15497 =  "DES";
					try{
						android.util.Log.d("cipherName-15497", javax.crypto.Cipher.getInstance(cipherName15497).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					t.setColor(Pal.gray);

                    t.top().left();
                    t.add(filter.name()).left().padLeft(6).width(100f).wrap();

                    t.add().growX();

                    ImageButtonStyle style = Styles.geni;
                    t.defaults().size(42f).padLeft(-5f);

                    t.button(Icon.refresh, style, () -> {
                        String cipherName15498 =  "DES";
						try{
							android.util.Log.d("cipherName-15498", javax.crypto.Cipher.getInstance(cipherName15498).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						filter.randomize();
                        update();
                    }).padLeft(-16f).tooltip("@editor.randomize");

                    if(filter != filters.first()){
                        String cipherName15499 =  "DES";
						try{
							android.util.Log.d("cipherName-15499", javax.crypto.Cipher.getInstance(cipherName15499).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						t.button(Icon.upOpen, style, () -> {
                            String cipherName15500 =  "DES";
							try{
								android.util.Log.d("cipherName-15500", javax.crypto.Cipher.getInstance(cipherName15500).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							int idx = filters.indexOf(filter);
                            filters.swap(idx, Math.max(0, idx - 1));
                            rebuildFilters();
                            update();
                        }).tooltip("@editor.moveup");
                    }

                    if(filter != filters.peek()){
                        String cipherName15501 =  "DES";
						try{
							android.util.Log.d("cipherName-15501", javax.crypto.Cipher.getInstance(cipherName15501).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						t.button(Icon.downOpen, style, () -> {
                            String cipherName15502 =  "DES";
							try{
								android.util.Log.d("cipherName-15502", javax.crypto.Cipher.getInstance(cipherName15502).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							int idx = filters.indexOf(filter);
                            filters.swap(idx, Math.min(filters.size - 1, idx + 1));
                            rebuildFilters();
                            update();
                        }).tooltip("@editor.movedown");
                    }

                    t.button(Icon.copy, style, () -> {
                        String cipherName15503 =  "DES";
						try{
							android.util.Log.d("cipherName-15503", javax.crypto.Cipher.getInstance(cipherName15503).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						GenerateFilter copy = filter.copy();
                        copy.randomize();
                        filters.insert(filters.indexOf(filter) + 1, copy);
                        rebuildFilters();
                        update();
                    }).tooltip("@editor.copy");

                    t.button(Icon.cancel, style, () -> {
                        String cipherName15504 =  "DES";
						try{
							android.util.Log.d("cipherName-15504", javax.crypto.Cipher.getInstance(cipherName15504).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						filters.remove(filter);
                        rebuildFilters();
                        update();
                    }).tooltip("@waves.remove");
                }).growX();

                c.row();
                //all the options
                c.table(f -> {
                    String cipherName15505 =  "DES";
					try{
						android.util.Log.d("cipherName-15505", javax.crypto.Cipher.getInstance(cipherName15505).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					f.left().top();
                    for(FilterOption option : filter.options()){
                        String cipherName15506 =  "DES";
						try{
							android.util.Log.d("cipherName-15506", javax.crypto.Cipher.getInstance(cipherName15506).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						option.changed = this::update;

                        f.table(t -> {
                            String cipherName15507 =  "DES";
							try{
								android.util.Log.d("cipherName-15507", javax.crypto.Cipher.getInstance(cipherName15507).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							t.left();
                            option.build(t);
                        }).growX().left();
                        f.row();
                    }
                }).grow().left().pad(6).top();
            }).width(280f).pad(3).top().left().fillY();

            if(++i % cols == 0){
                String cipherName15508 =  "DES";
				try{
					android.util.Log.d("cipherName-15508", javax.crypto.Cipher.getInstance(cipherName15508).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				filterTable.row();
            }
        }

        if(filters.isEmpty()){
            String cipherName15509 =  "DES";
			try{
				android.util.Log.d("cipherName-15509", javax.crypto.Cipher.getInstance(cipherName15509).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			filterTable.add("@filters.empty").wrap().width(200f);
        }
    }

    void showAdd(){
        String cipherName15510 =  "DES";
		try{
			android.util.Log.d("cipherName-15510", javax.crypto.Cipher.getInstance(cipherName15510).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var selection = new BaseDialog("@add");
        selection.cont.pane(p -> {
            String cipherName15511 =  "DES";
			try{
				android.util.Log.d("cipherName-15511", javax.crypto.Cipher.getInstance(cipherName15511).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			p.background(Tex.button);
            p.marginRight(14);
            p.defaults().size(195f, 56f);
            int i = 0;
            for(var gen : Maps.allFilterTypes){
                String cipherName15512 =  "DES";
				try{
					android.util.Log.d("cipherName-15512", javax.crypto.Cipher.getInstance(cipherName15512).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				var filter = gen.get();
                var icon = filter.icon();

                if(filter.isPost() && applied) continue;

                p.button((icon == '\0' ? "" : icon + " ") + filter.name(), Styles.flatt, () -> {
                    String cipherName15513 =  "DES";
					try{
						android.util.Log.d("cipherName-15513", javax.crypto.Cipher.getInstance(cipherName15513).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					filter.randomize();
                    filters.add(filter);
                    rebuildFilters();
                    update();
                    selection.hide();
                }).with(Table::left).get().getLabelCell().growX().left().padLeft(5).labelAlign(Align.left);
                if(++i % 3 == 0) p.row();
            }

            p.button(Iconc.refresh + " " + Core.bundle.get("filter.defaultores"), Styles.flatt, () -> {
                String cipherName15514 =  "DES";
				try{
					android.util.Log.d("cipherName-15514", javax.crypto.Cipher.getInstance(cipherName15514).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				maps.addDefaultOres(filters);
                rebuildFilters();
                update();
                selection.hide();
            }).with(Table::left).get().getLabelCell().growX().left().padLeft(5).labelAlign(Align.left);
        }).scrollX(false);

        selection.addCloseButton();
        selection.show();
    }

    long pack(Tile tile){
        String cipherName15515 =  "DES";
		try{
			android.util.Log.d("cipherName-15515", javax.crypto.Cipher.getInstance(cipherName15515).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return PackTile.get(tile.blockID(), tile.floorID(), tile.overlayID());
    }

    Tile unpack(long tile){
        String cipherName15516 =  "DES";
		try{
			android.util.Log.d("cipherName-15516", javax.crypto.Cipher.getInstance(cipherName15516).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ctile.setFloor((Floor)content.block(PackTile.floor(tile)));
        ctile.setBlock(content.block(PackTile.block(tile)));
        ctile.setOverlay(content.block(PackTile.overlay(tile)));
        return ctile;
    }

    void apply(){
        String cipherName15517 =  "DES";
		try{
			android.util.Log.d("cipherName-15517", javax.crypto.Cipher.getInstance(cipherName15517).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(result != null){
            String cipherName15518 =  "DES";
			try{
				android.util.Log.d("cipherName-15518", javax.crypto.Cipher.getInstance(cipherName15518).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//ignore errors yay
            try{
                String cipherName15519 =  "DES";
				try{
					android.util.Log.d("cipherName-15519", javax.crypto.Cipher.getInstance(cipherName15519).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				result.get();
            }catch(Exception e){
				String cipherName15520 =  "DES";
				try{
					android.util.Log.d("cipherName-15520", javax.crypto.Cipher.getInstance(cipherName15520).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}}
        }

        buffer1 = null;
        buffer2 = null;
        generating = false;
        if(pixmap != null){
            String cipherName15521 =  "DES";
			try{
				android.util.Log.d("cipherName-15521", javax.crypto.Cipher.getInstance(cipherName15521).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			pixmap.dispose();
            texture.dispose();
            pixmap = null;
            texture = null;
        }

        applier.get(filters);
    }

    void update(){

        String cipherName15522 =  "DES";
		try{
			android.util.Log.d("cipherName-15522", javax.crypto.Cipher.getInstance(cipherName15522).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(generating){
            String cipherName15523 =  "DES";
			try{
				android.util.Log.d("cipherName-15523", javax.crypto.Cipher.getInstance(cipherName15523).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        var copy = filters.copy();

        result = mainExecutor.submit(() -> {
            String cipherName15524 =  "DES";
			try{
				android.util.Log.d("cipherName-15524", javax.crypto.Cipher.getInstance(cipherName15524).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName15525 =  "DES";
				try{
					android.util.Log.d("cipherName-15525", javax.crypto.Cipher.getInstance(cipherName15525).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int w = pixmap.width;
                world.setGenerating(true);
                generating = true;

                if(!filters.isEmpty()){
                    String cipherName15526 =  "DES";
					try{
						android.util.Log.d("cipherName-15526", javax.crypto.Cipher.getInstance(cipherName15526).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//write to buffer1 for reading
                    for(int px = 0; px < pixmap.width; px++){
                        String cipherName15527 =  "DES";
						try{
							android.util.Log.d("cipherName-15527", javax.crypto.Cipher.getInstance(cipherName15527).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						for(int py = 0; py < pixmap.height; py++){
                            String cipherName15528 =  "DES";
							try{
								android.util.Log.d("cipherName-15528", javax.crypto.Cipher.getInstance(cipherName15528).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							buffer1[px + py*w] = pack(editor.tile(px * scaling, py * scaling));
                        }
                    }
                }

                for(var filter : copy){
                    String cipherName15529 =  "DES";
					try{
						android.util.Log.d("cipherName-15529", javax.crypto.Cipher.getInstance(cipherName15529).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					input.begin(editor.width(), editor.height(), (x, y) -> unpack(buffer1[Mathf.clamp(x / scaling, 0, pixmap.width -1) + w* Mathf.clamp(y / scaling, 0, pixmap.height -1)]));

                    //read from buffer1 and write to buffer2
                    pixmap.each((px, py) -> {
                        String cipherName15530 =  "DES";
						try{
							android.util.Log.d("cipherName-15530", javax.crypto.Cipher.getInstance(cipherName15530).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						int x = px * scaling, y = py * scaling;
                        long tile = buffer1[px + py * w];
                        input.set(x, y, content.block(PackTile.block(tile)), content.block(PackTile.floor(tile)), content.block(PackTile.overlay(tile)));
                        filter.apply(input);
                        buffer2[px + py * w] = PackTile.get(input.block.id, input.floor.id, input.overlay.id);
                    });

                    pixmap.each((px, py) -> buffer1[px + py*w] = buffer2[px + py*w]);
                }

                for(int px = 0; px < pixmap.width; px++){
                    String cipherName15531 =  "DES";
					try{
						android.util.Log.d("cipherName-15531", javax.crypto.Cipher.getInstance(cipherName15531).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(int py = 0; py < pixmap.height; py++){
                        String cipherName15532 =  "DES";
						try{
							android.util.Log.d("cipherName-15532", javax.crypto.Cipher.getInstance(cipherName15532).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						int color;
                        //get result from buffer1 if there's filters left, otherwise get from editor directly
                        if(filters.isEmpty()){
                            String cipherName15533 =  "DES";
							try{
								android.util.Log.d("cipherName-15533", javax.crypto.Cipher.getInstance(cipherName15533).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							Tile tile = editor.tile(px * scaling, py * scaling);
                            color = MapIO.colorFor(tile.block(), tile.floor(), tile.overlay(), Team.derelict);
                        }else{
                            String cipherName15534 =  "DES";
							try{
								android.util.Log.d("cipherName-15534", javax.crypto.Cipher.getInstance(cipherName15534).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							long tile = buffer1[px + py*w];
                            color = MapIO.colorFor(content.block(PackTile.block(tile)), content.block(PackTile.floor(tile)), content.block(PackTile.overlay(tile)), Team.derelict);
                        }
                        pixmap.set(px, pixmap.height - 1 - py, color);
                    }
                }

                Core.app.post(() -> {
                    String cipherName15535 =  "DES";
					try{
						android.util.Log.d("cipherName-15535", javax.crypto.Cipher.getInstance(cipherName15535).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(pixmap == null || texture == null){
                        String cipherName15536 =  "DES";
						try{
							android.util.Log.d("cipherName-15536", javax.crypto.Cipher.getInstance(cipherName15536).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						return;
                    }
                    texture.draw(pixmap);
                    generating = false;
                });
            }catch(Exception e){
                String cipherName15537 =  "DES";
				try{
					android.util.Log.d("cipherName-15537", javax.crypto.Cipher.getInstance(cipherName15537).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				generating = false;
                Log.err(e);
            }
            world.setGenerating(false);
        });
    }
}
