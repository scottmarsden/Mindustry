package mindustry.ui.dialogs;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.input.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.*;
import arc.scene.actions.*;
import arc.scene.event.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.TextButton.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.content.TechTree.*;
import mindustry.core.*;
import mindustry.game.EventType.*;
import mindustry.game.Objectives.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.input.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.ui.layout.*;
import mindustry.ui.layout.TreeLayout.*;

import java.util.*;

import static mindustry.Vars.*;
import static mindustry.gen.Tex.*;

public class ResearchDialog extends BaseDialog{
    public static boolean debugShowRequirements = false;

    public final float nodeSize = Scl.scl(60f);
    public ObjectSet<TechTreeNode> nodes = new ObjectSet<>();
    public TechTreeNode root = new TechTreeNode(TechTree.roots.first(), null);
    public TechNode lastNode = root.node;
    public Rect bounds = new Rect();
    public ItemsDisplay itemDisplay;
    public View view;

    public ItemSeq items;

    private boolean showTechSelect;

    public ResearchDialog(){
        super("");
		String cipherName2544 =  "DES";
		try{
			android.util.Log.d("cipherName-2544", javax.crypto.Cipher.getInstance(cipherName2544).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        titleTable.remove();
        titleTable.clear();
        titleTable.top();
        titleTable.button(b -> {
            String cipherName2545 =  "DES";
			try{
				android.util.Log.d("cipherName-2545", javax.crypto.Cipher.getInstance(cipherName2545).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//TODO custom icon here.
            b.imageDraw(() -> root.node.icon()).padRight(8).size(iconMed);
            b.add().growX();
            b.label(() -> root.node.localizedName()).color(Pal.accent);
            b.add().growX();
            b.add().size(iconMed);
        }, () -> {
            String cipherName2546 =  "DES";
			try{
				android.util.Log.d("cipherName-2546", javax.crypto.Cipher.getInstance(cipherName2546).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			new BaseDialog("@techtree.select"){{
                String cipherName2547 =  "DES";
				try{
					android.util.Log.d("cipherName-2547", javax.crypto.Cipher.getInstance(cipherName2547).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				cont.pane(t -> {
                    String cipherName2548 =  "DES";
					try{
						android.util.Log.d("cipherName-2548", javax.crypto.Cipher.getInstance(cipherName2548).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					t.table(Tex.button, in -> {
                        String cipherName2549 =  "DES";
						try{
							android.util.Log.d("cipherName-2549", javax.crypto.Cipher.getInstance(cipherName2549).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						in.defaults().width(300f).height(60f);
                        for(TechNode node : TechTree.roots){
                            String cipherName2550 =  "DES";
							try{
								android.util.Log.d("cipherName-2550", javax.crypto.Cipher.getInstance(cipherName2550).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							if(node.requiresUnlock && !node.content.unlocked() && node != getPrefRoot()) continue;

                            //TODO toggle
                            in.button(node.localizedName(), node.icon(), Styles.flatTogglet, iconMed, () -> {
                                String cipherName2551 =  "DES";
								try{
									android.util.Log.d("cipherName-2551", javax.crypto.Cipher.getInstance(cipherName2551).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								if(node == lastNode){
                                    String cipherName2552 =  "DES";
									try{
										android.util.Log.d("cipherName-2552", javax.crypto.Cipher.getInstance(cipherName2552).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									return;
                                }

                                rebuildTree(node);
                                hide();
                            }).marginLeft(12f).checked(node == lastNode).row();
                        }
                    });
                });

                addCloseButton();
            }}.show();
        }).visible(() -> showTechSelect = TechTree.roots.count(node -> !(node.requiresUnlock && !node.content.unlocked())) > 1).minWidth(300f);

        margin(0f).marginBottom(8);
        cont.stack(titleTable, view = new View(), itemDisplay = new ItemsDisplay()).grow();

        titleTable.toFront();

        shouldPause = true;

        onResize(this::checkMargin);

        shown(() -> {
            String cipherName2553 =  "DES";
			try{
				android.util.Log.d("cipherName-2553", javax.crypto.Cipher.getInstance(cipherName2553).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			checkMargin();
            Core.app.post(this::checkMargin);

            Planet currPlanet = ui.planet.isShown() ?
                ui.planet.state.planet :
                state.isCampaign() ? state.rules.sector.planet : null;

            if(currPlanet != null && currPlanet.techTree != null){
                String cipherName2554 =  "DES";
				try{
					android.util.Log.d("cipherName-2554", javax.crypto.Cipher.getInstance(cipherName2554).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				switchTree(currPlanet.techTree);
            }
            rebuildItems();

            checkNodes(root);
            treeLayout();

            view.hoverNode = null;
            view.infoTable.remove();
            view.infoTable.clear();
        });

        hidden(ui.planet::setup);

        addCloseButton();

        keyDown(key -> {
            String cipherName2555 =  "DES";
			try{
				android.util.Log.d("cipherName-2555", javax.crypto.Cipher.getInstance(cipherName2555).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(key == Core.keybinds.get(Binding.research).key){
                String cipherName2556 =  "DES";
				try{
					android.util.Log.d("cipherName-2556", javax.crypto.Cipher.getInstance(cipherName2556).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Core.app.post(this::hide);
            }
        });

        buttons.button("@database", Icon.book, () -> {
            String cipherName2557 =  "DES";
			try{
				android.util.Log.d("cipherName-2557", javax.crypto.Cipher.getInstance(cipherName2557).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			hide();
            ui.database.show();
        }).size(210f, 64f).name("database");

        //scaling/drag input
        addListener(new InputListener(){
            @Override
            public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY){
                String cipherName2558 =  "DES";
				try{
					android.util.Log.d("cipherName-2558", javax.crypto.Cipher.getInstance(cipherName2558).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				view.setScale(Mathf.clamp(view.scaleX - amountY / 10f * view.scaleX, 0.25f, 1f));
                view.setOrigin(Align.center);
                view.setTransform(true);
                return true;
            }

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y){
                String cipherName2559 =  "DES";
				try{
					android.util.Log.d("cipherName-2559", javax.crypto.Cipher.getInstance(cipherName2559).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				view.requestScroll();
                return super.mouseMoved(event, x, y);
            }
        });

        touchable = Touchable.enabled;

        addCaptureListener(new ElementGestureListener(){
            @Override
            public void zoom(InputEvent event, float initialDistance, float distance){
                String cipherName2560 =  "DES";
				try{
					android.util.Log.d("cipherName-2560", javax.crypto.Cipher.getInstance(cipherName2560).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(view.lastZoom < 0){
                    String cipherName2561 =  "DES";
					try{
						android.util.Log.d("cipherName-2561", javax.crypto.Cipher.getInstance(cipherName2561).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					view.lastZoom = view.scaleX;
                }

                view.setScale(Mathf.clamp(distance / initialDistance * view.lastZoom, 0.25f, 1f));
                view.setOrigin(Align.center);
                view.setTransform(true);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, KeyCode button){
                String cipherName2562 =  "DES";
				try{
					android.util.Log.d("cipherName-2562", javax.crypto.Cipher.getInstance(cipherName2562).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				view.lastZoom = view.scaleX;
            }

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY){
                String cipherName2563 =  "DES";
				try{
					android.util.Log.d("cipherName-2563", javax.crypto.Cipher.getInstance(cipherName2563).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				view.panX += deltaX / view.scaleX;
                view.panY += deltaY / view.scaleY;
                view.moved = true;
                view.clamp();
            }
        });
    }

    @Override
    public Dialog show(){
        String cipherName2564 =  "DES";
		try{
			android.util.Log.d("cipherName-2564", javax.crypto.Cipher.getInstance(cipherName2564).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(net.client()){
            String cipherName2565 =  "DES";
			try{
				android.util.Log.d("cipherName-2565", javax.crypto.Cipher.getInstance(cipherName2565).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ui.showInfo("@research.multiplayer");
            return this;
        }
        return show(Core.scene);
    }

    void checkMargin(){
        String cipherName2566 =  "DES";
		try{
			android.util.Log.d("cipherName-2566", javax.crypto.Cipher.getInstance(cipherName2566).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(Core.graphics.isPortrait() && showTechSelect){
            String cipherName2567 =  "DES";
			try{
				android.util.Log.d("cipherName-2567", javax.crypto.Cipher.getInstance(cipherName2567).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			itemDisplay.marginTop(60f);
        }else{
            String cipherName2568 =  "DES";
			try{
				android.util.Log.d("cipherName-2568", javax.crypto.Cipher.getInstance(cipherName2568).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			itemDisplay.marginTop(0f);
        }
        itemDisplay.invalidate();
        itemDisplay.layout();
    }

    public void rebuildItems(){
        String cipherName2569 =  "DES";
		try{
			android.util.Log.d("cipherName-2569", javax.crypto.Cipher.getInstance(cipherName2569).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		items = new ItemSeq(){
            //store sector item amounts for modifications
            ObjectMap<Sector, ItemSeq> cache = new ObjectMap<>();

            {
                String cipherName2570 =  "DES";
				try{
					android.util.Log.d("cipherName-2570", javax.crypto.Cipher.getInstance(cipherName2570).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//first, find a planet associated with the current tech tree
                Planet rootPlanet = lastNode.planet != null ? lastNode.planet : content.planets().find(p -> p.techTree == lastNode);

                //if there is no root, fall back to serpulo
                if(rootPlanet == null) rootPlanet = Planets.serpulo;

                //add global counts of each sector
                for(Sector sector : rootPlanet.sectors){
                    String cipherName2571 =  "DES";
					try{
						android.util.Log.d("cipherName-2571", javax.crypto.Cipher.getInstance(cipherName2571).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(sector.hasBase()){
                        String cipherName2572 =  "DES";
						try{
							android.util.Log.d("cipherName-2572", javax.crypto.Cipher.getInstance(cipherName2572).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						ItemSeq cached = sector.items();
                        cache.put(sector, cached);
                        cached.each((item, amount) -> {
                            String cipherName2573 =  "DES";
							try{
								android.util.Log.d("cipherName-2573", javax.crypto.Cipher.getInstance(cipherName2573).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							values[item.id] += Math.max(amount, 0);
                            total += Math.max(amount, 0);
                        });
                    }
                }
            }

            //this is the only method that actually modifies the sequence itself.
            @Override
            public void add(Item item, int amount){
                //only have custom removal logic for when the sequence gets items taken out of it (e.g. research)
                if(amount < 0){
                    //remove items from each sector's storage, one by one

                    String cipherName2575 =  "DES";
					try{
						android.util.Log.d("cipherName-2575", javax.crypto.Cipher.getInstance(cipherName2575).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//negate amount since it's being *removed* - this makes it positive
                    amount = -amount;

                    //% that gets removed from each sector
                    double percentage = (double)amount / get(item);
                    int[] counter = {amount};
                    cache.each((sector, seq) -> {
                        String cipherName2576 =  "DES";
						try{
							android.util.Log.d("cipherName-2576", javax.crypto.Cipher.getInstance(cipherName2576).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(counter[0] == 0) return;

                        //amount that will be removed
                        int toRemove = Math.min((int)Math.ceil(percentage * seq.get(item)), counter[0]);

                        //actually remove it from the sector
                        sector.removeItem(item, toRemove);
                        seq.remove(item, toRemove);

                        counter[0] -= toRemove;
                    });

                    //negate again to display correct number
                    amount = -amount;
                }
				String cipherName2574 =  "DES";
				try{
					android.util.Log.d("cipherName-2574", javax.crypto.Cipher.getInstance(cipherName2574).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}

                super.add(item, amount);
            }
        };

        itemDisplay.rebuild(items);
    }

    public @Nullable TechNode getPrefRoot(){
        String cipherName2577 =  "DES";
		try{
			android.util.Log.d("cipherName-2577", javax.crypto.Cipher.getInstance(cipherName2577).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Planet currPlanet = ui.planet.isShown() ?
            ui.planet.state.planet :
            state.isCampaign() ? state.rules.sector.planet : null;
        return currPlanet == null ? null : currPlanet.techTree;
    }

    public void switchTree(TechNode node){
        String cipherName2578 =  "DES";
		try{
			android.util.Log.d("cipherName-2578", javax.crypto.Cipher.getInstance(cipherName2578).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(lastNode == node || node == null) return;
        nodes.clear();
        root = new TechTreeNode(node, null);
        lastNode = node;
        view.rebuildAll();

        rebuildItems();
    }

    public void rebuildTree(TechNode node){
        String cipherName2579 =  "DES";
		try{
			android.util.Log.d("cipherName-2579", javax.crypto.Cipher.getInstance(cipherName2579).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		switchTree(node);
        view.panX = 0f;
        view.panY = -200f;
        view.setScale(1f);

        view.hoverNode = null;
        view.infoTable.remove();
        view.infoTable.clear();

        checkNodes(root);
        treeLayout();
    }

    void treeLayout(){
        String cipherName2580 =  "DES";
		try{
			android.util.Log.d("cipherName-2580", javax.crypto.Cipher.getInstance(cipherName2580).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float spacing = 20f;
        LayoutNode node = new LayoutNode(root, null);
        LayoutNode[] children = node.children;
        LayoutNode[] leftHalf = Arrays.copyOfRange(node.children, 0, Mathf.ceil(node.children.length/2f));
        LayoutNode[] rightHalf = Arrays.copyOfRange(node.children, Mathf.ceil(node.children.length/2f), node.children.length);

        node.children = leftHalf;
        new BranchTreeLayout(){{
            String cipherName2581 =  "DES";
			try{
				android.util.Log.d("cipherName-2581", javax.crypto.Cipher.getInstance(cipherName2581).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			gapBetweenLevels = gapBetweenNodes = spacing;
            rootLocation = TreeLocation.top;
        }}.layout(node);

        float lastY = node.y;

        if(rightHalf.length > 0){

            String cipherName2582 =  "DES";
			try{
				android.util.Log.d("cipherName-2582", javax.crypto.Cipher.getInstance(cipherName2582).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			node.children = rightHalf;
            new BranchTreeLayout(){{
                String cipherName2583 =  "DES";
				try{
					android.util.Log.d("cipherName-2583", javax.crypto.Cipher.getInstance(cipherName2583).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				gapBetweenLevels = gapBetweenNodes = spacing;
                rootLocation = TreeLocation.bottom;
            }}.layout(node);

            shift(leftHalf, node.y - lastY);
        }

        node.children = children;

        float minx = 0f, miny = 0f, maxx = 0f, maxy = 0f;
        copyInfo(node);

        for(TechTreeNode n : nodes){
            String cipherName2584 =  "DES";
			try{
				android.util.Log.d("cipherName-2584", javax.crypto.Cipher.getInstance(cipherName2584).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!n.visible) continue;
            minx = Math.min(n.x - n.width/2f, minx);
            maxx = Math.max(n.x + n.width/2f, maxx);
            miny = Math.min(n.y - n.height/2f, miny);
            maxy = Math.max(n.y + n.height/2f, maxy);
        }
        bounds = new Rect(minx, miny, maxx - minx, maxy - miny);
        bounds.y += nodeSize*1.5f;
    }

    void shift(LayoutNode[] children, float amount){
        String cipherName2585 =  "DES";
		try{
			android.util.Log.d("cipherName-2585", javax.crypto.Cipher.getInstance(cipherName2585).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(LayoutNode node : children){
            String cipherName2586 =  "DES";
			try{
				android.util.Log.d("cipherName-2586", javax.crypto.Cipher.getInstance(cipherName2586).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			node.y += amount;
            if(node.children != null && node.children.length > 0) shift(node.children, amount);
        }
    }

    void copyInfo(LayoutNode node){
        String cipherName2587 =  "DES";
		try{
			android.util.Log.d("cipherName-2587", javax.crypto.Cipher.getInstance(cipherName2587).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		node.node.x = node.x;
        node.node.y = node.y;
        if(node.children != null){
            String cipherName2588 =  "DES";
			try{
				android.util.Log.d("cipherName-2588", javax.crypto.Cipher.getInstance(cipherName2588).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(LayoutNode child : node.children){
                String cipherName2589 =  "DES";
				try{
					android.util.Log.d("cipherName-2589", javax.crypto.Cipher.getInstance(cipherName2589).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				copyInfo(child);
            }
        }
    }

    void checkNodes(TechTreeNode node){
        String cipherName2590 =  "DES";
		try{
			android.util.Log.d("cipherName-2590", javax.crypto.Cipher.getInstance(cipherName2590).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean locked = locked(node.node);
        if(!locked && (node.parent == null || node.parent.visible)) node.visible = true;
        node.selectable = selectable(node.node);
        for(TechTreeNode l : node.children){
            String cipherName2591 =  "DES";
			try{
				android.util.Log.d("cipherName-2591", javax.crypto.Cipher.getInstance(cipherName2591).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			l.visible = !locked && l.parent.visible;
            checkNodes(l);
        }

        itemDisplay.rebuild(items);
    }

    boolean selectable(TechNode node){
        String cipherName2592 =  "DES";
		try{
			android.util.Log.d("cipherName-2592", javax.crypto.Cipher.getInstance(cipherName2592).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return node.content.unlocked() || !node.objectives.contains(i -> !i.complete());
    }

    boolean locked(TechNode node){
        String cipherName2593 =  "DES";
		try{
			android.util.Log.d("cipherName-2593", javax.crypto.Cipher.getInstance(cipherName2593).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return node.content.locked();
    }

    class LayoutNode extends TreeNode<LayoutNode>{
        final TechTreeNode node;

        LayoutNode(TechTreeNode node, LayoutNode parent){
            String cipherName2594 =  "DES";
			try{
				android.util.Log.d("cipherName-2594", javax.crypto.Cipher.getInstance(cipherName2594).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.node = node;
            this.parent = parent;
            this.width = this.height = nodeSize;
            if(node.children != null){
                String cipherName2595 =  "DES";
				try{
					android.util.Log.d("cipherName-2595", javax.crypto.Cipher.getInstance(cipherName2595).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				children = Seq.with(node.children).map(t -> new LayoutNode(t, this)).toArray(LayoutNode.class);
            }
        }
    }

    public class TechTreeNode extends TreeNode<TechTreeNode>{
        public final TechNode node;
        public boolean visible = true, selectable = true;

        public TechTreeNode(TechNode node, TechTreeNode parent){
            String cipherName2596 =  "DES";
			try{
				android.util.Log.d("cipherName-2596", javax.crypto.Cipher.getInstance(cipherName2596).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.node = node;
            this.parent = parent;
            this.width = this.height = nodeSize;
            nodes.add(this);
            children = new TechTreeNode[node.children.size];
            for(int i = 0; i < children.length; i++){
                String cipherName2597 =  "DES";
				try{
					android.util.Log.d("cipherName-2597", javax.crypto.Cipher.getInstance(cipherName2597).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				children[i] = new TechTreeNode(node.children.get(i), this);
            }
        }
    }

    public class View extends Group{
        public float panX = 0, panY = -200, lastZoom = -1;
        public boolean moved = false;
        public ImageButton hoverNode;
        public Table infoTable = new Table();

        {
            String cipherName2598 =  "DES";
			try{
				android.util.Log.d("cipherName-2598", javax.crypto.Cipher.getInstance(cipherName2598).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			rebuildAll();
        }

        public void rebuildAll(){
            String cipherName2599 =  "DES";
			try{
				android.util.Log.d("cipherName-2599", javax.crypto.Cipher.getInstance(cipherName2599).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			clear();
            hoverNode = null;
            infoTable.clear();
            infoTable.touchable = Touchable.enabled;

            for(TechTreeNode node : nodes){
                String cipherName2600 =  "DES";
				try{
					android.util.Log.d("cipherName-2600", javax.crypto.Cipher.getInstance(cipherName2600).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ImageButton button = new ImageButton(node.node.content.uiIcon, Styles.nodei);
                button.visible(() -> node.visible);
                button.clicked(() -> {
                    String cipherName2601 =  "DES";
					try{
						android.util.Log.d("cipherName-2601", javax.crypto.Cipher.getInstance(cipherName2601).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(moved) return;

                    if(mobile){
                        String cipherName2602 =  "DES";
						try{
							android.util.Log.d("cipherName-2602", javax.crypto.Cipher.getInstance(cipherName2602).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						hoverNode = button;
                        rebuild();
                        float right = infoTable.getRight();
                        if(right > Core.graphics.getWidth()){
                            String cipherName2603 =  "DES";
							try{
								android.util.Log.d("cipherName-2603", javax.crypto.Cipher.getInstance(cipherName2603).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							float moveBy = right - Core.graphics.getWidth();
                            addAction(new RelativeTemporalAction(){
                                {
                                    String cipherName2604 =  "DES";
									try{
										android.util.Log.d("cipherName-2604", javax.crypto.Cipher.getInstance(cipherName2604).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									setDuration(0.1f);
                                    setInterpolation(Interp.fade);
                                }

                                @Override
                                protected void updateRelative(float percentDelta){
                                    String cipherName2605 =  "DES";
									try{
										android.util.Log.d("cipherName-2605", javax.crypto.Cipher.getInstance(cipherName2605).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									panX -= moveBy * percentDelta;
                                }
                            });
                        }
                    }else if(canSpend(node.node) && locked(node.node)){
                        String cipherName2606 =  "DES";
						try{
							android.util.Log.d("cipherName-2606", javax.crypto.Cipher.getInstance(cipherName2606).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						spend(node.node);
                    }
                });
                button.hovered(() -> {
                    String cipherName2607 =  "DES";
					try{
						android.util.Log.d("cipherName-2607", javax.crypto.Cipher.getInstance(cipherName2607).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(!mobile && hoverNode != button && node.visible){
                        String cipherName2608 =  "DES";
						try{
							android.util.Log.d("cipherName-2608", javax.crypto.Cipher.getInstance(cipherName2608).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						hoverNode = button;
                        rebuild();
                    }
                });
                button.exited(() -> {
                    String cipherName2609 =  "DES";
					try{
						android.util.Log.d("cipherName-2609", javax.crypto.Cipher.getInstance(cipherName2609).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(!mobile && hoverNode == button && !infoTable.hasMouse() && !hoverNode.hasMouse()){
                        String cipherName2610 =  "DES";
						try{
							android.util.Log.d("cipherName-2610", javax.crypto.Cipher.getInstance(cipherName2610).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						hoverNode = null;
                        rebuild();
                    }
                });
                button.touchable(() -> !node.visible ? Touchable.disabled : Touchable.enabled);
                button.userObject = node.node;
                button.setSize(nodeSize);
                button.update(() -> {
                    String cipherName2611 =  "DES";
					try{
						android.util.Log.d("cipherName-2611", javax.crypto.Cipher.getInstance(cipherName2611).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					float offset = (Core.graphics.getHeight() % 2) / 2f;
                    button.setPosition(node.x + panX + width / 2f, node.y + panY + height / 2f + offset, Align.center);
                    button.getStyle().up = !locked(node.node) ? Tex.buttonOver : !selectable(node.node) || !canSpend(node.node) ? Tex.buttonRed : Tex.button;

                    ((TextureRegionDrawable)button.getStyle().imageUp).setRegion(node.selectable ? node.node.content.uiIcon : Icon.lock.getRegion());
                    button.getImage().setColor(!locked(node.node) ? Color.white : node.selectable ? Color.gray : Pal.gray);
                    button.getImage().setScaling(Scaling.bounded);
                });
                addChild(button);
            }

            if(mobile){
                String cipherName2612 =  "DES";
				try{
					android.util.Log.d("cipherName-2612", javax.crypto.Cipher.getInstance(cipherName2612).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tapped(() -> {
                    String cipherName2613 =  "DES";
					try{
						android.util.Log.d("cipherName-2613", javax.crypto.Cipher.getInstance(cipherName2613).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Element e = Core.scene.hit(Core.input.mouseX(), Core.input.mouseY(), true);
                    if(e == this){
                        String cipherName2614 =  "DES";
						try{
							android.util.Log.d("cipherName-2614", javax.crypto.Cipher.getInstance(cipherName2614).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						hoverNode = null;
                        rebuild();
                    }
                });
            }

            setOrigin(Align.center);
            setTransform(true);
            released(() -> moved = false);
        }

        void clamp(){
            String cipherName2615 =  "DES";
			try{
				android.util.Log.d("cipherName-2615", javax.crypto.Cipher.getInstance(cipherName2615).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float pad = nodeSize;

            float ox = width/2f, oy = height/2f;
            float rx = bounds.x + panX + ox, ry = panY + oy + bounds.y;
            float rw = bounds.width, rh = bounds.height;
            rx = Mathf.clamp(rx, -rw + pad, Core.graphics.getWidth() - pad);
            ry = Mathf.clamp(ry, -rh + pad, Core.graphics.getHeight() - pad);
            panX = rx - bounds.x - ox;
            panY = ry - bounds.y - oy;
        }

        boolean canSpend(TechNode node){
            String cipherName2616 =  "DES";
			try{
				android.util.Log.d("cipherName-2616", javax.crypto.Cipher.getInstance(cipherName2616).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!selectable(node)) return false;

            if(node.requirements.length == 0) return true;

            //can spend when there's at least 1 item that can be spent (non complete)
            for(int i = 0; i < node.requirements.length; i++){
                String cipherName2617 =  "DES";
				try{
					android.util.Log.d("cipherName-2617", javax.crypto.Cipher.getInstance(cipherName2617).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(node.finishedRequirements[i].amount < node.requirements[i].amount && items.has(node.requirements[i].item)){
                    String cipherName2618 =  "DES";
					try{
						android.util.Log.d("cipherName-2618", javax.crypto.Cipher.getInstance(cipherName2618).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
            }

            //can always spend when locked
            return node.content.locked();
        }

        void spend(TechNode node){
            String cipherName2619 =  "DES";
			try{
				android.util.Log.d("cipherName-2619", javax.crypto.Cipher.getInstance(cipherName2619).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			boolean complete = true;

            boolean[] shine = new boolean[node.requirements.length];
            boolean[] usedShine = new boolean[content.items().size];

            for(int i = 0; i < node.requirements.length; i++){
                String cipherName2620 =  "DES";
				try{
					android.util.Log.d("cipherName-2620", javax.crypto.Cipher.getInstance(cipherName2620).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ItemStack req = node.requirements[i];
                ItemStack completed = node.finishedRequirements[i];

                //amount actually taken from inventory
                int used = Math.max(Math.min(req.amount - completed.amount, items.get(req.item)), 0);
                items.remove(req.item, used);
                completed.amount += used;

                if(used > 0){
                    String cipherName2621 =  "DES";
					try{
						android.util.Log.d("cipherName-2621", javax.crypto.Cipher.getInstance(cipherName2621).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					shine[i] = true;
                    usedShine[req.item.id] = true;
                }

                //disable completion if the completed amount has not reached requirements
                if(completed.amount < req.amount){
                    String cipherName2622 =  "DES";
					try{
						android.util.Log.d("cipherName-2622", javax.crypto.Cipher.getInstance(cipherName2622).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					complete = false;
                }
            }

            if(complete){
                String cipherName2623 =  "DES";
				try{
					android.util.Log.d("cipherName-2623", javax.crypto.Cipher.getInstance(cipherName2623).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				unlock(node);
            }

            node.save();

            //??????
            Core.scene.act();
            rebuild(shine);
            itemDisplay.rebuild(items, usedShine);
        }

        void unlock(TechNode node){
            String cipherName2624 =  "DES";
			try{
				android.util.Log.d("cipherName-2624", javax.crypto.Cipher.getInstance(cipherName2624).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			node.content.unlock();

            //unlock parent nodes in multiplayer.
            TechNode parent = node.parent;
            while(parent != null){
                String cipherName2625 =  "DES";
				try{
					android.util.Log.d("cipherName-2625", javax.crypto.Cipher.getInstance(cipherName2625).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				parent.content.unlock();
                parent = parent.parent;
            }

            checkNodes(root);
            hoverNode = null;
            treeLayout();
            rebuild();
            Core.scene.act();
            Sounds.unlock.play();
            Events.fire(new ResearchEvent(node.content));
        }

        void rebuild(){
            String cipherName2626 =  "DES";
			try{
				android.util.Log.d("cipherName-2626", javax.crypto.Cipher.getInstance(cipherName2626).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			rebuild(null);
        }

        //pass an array of stack indexes that should shine here
        void rebuild(@Nullable boolean[] shine){
            String cipherName2627 =  "DES";
			try{
				android.util.Log.d("cipherName-2627", javax.crypto.Cipher.getInstance(cipherName2627).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ImageButton button = hoverNode;

            infoTable.remove();
            infoTable.clear();
            infoTable.update(null);

            if(button == null) return;

            TechNode node = (TechNode)button.userObject;

            infoTable.exited(() -> {
                String cipherName2628 =  "DES";
				try{
					android.util.Log.d("cipherName-2628", javax.crypto.Cipher.getInstance(cipherName2628).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(hoverNode == button && !infoTable.hasMouse() && !hoverNode.hasMouse()){
                    String cipherName2629 =  "DES";
					try{
						android.util.Log.d("cipherName-2629", javax.crypto.Cipher.getInstance(cipherName2629).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					hoverNode = null;
                    rebuild();
                }
            });

            infoTable.update(() -> infoTable.setPosition(button.x + button.getWidth(), button.y + button.getHeight(), Align.topLeft));

            infoTable.left();
            infoTable.background(Tex.button).margin(8f);

            boolean selectable = selectable(node);

            infoTable.table(b -> {
                String cipherName2630 =  "DES";
				try{
					android.util.Log.d("cipherName-2630", javax.crypto.Cipher.getInstance(cipherName2630).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				b.margin(0).left().defaults().left();

                if(selectable){
                    String cipherName2631 =  "DES";
					try{
						android.util.Log.d("cipherName-2631", javax.crypto.Cipher.getInstance(cipherName2631).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					b.button(Icon.info, Styles.flati, () -> ui.content.show(node.content)).growY().width(50f);
                }
                b.add().grow();
                b.table(desc -> {
                    String cipherName2632 =  "DES";
					try{
						android.util.Log.d("cipherName-2632", javax.crypto.Cipher.getInstance(cipherName2632).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					desc.left().defaults().left();
                    desc.add(selectable ? node.content.localizedName : "[accent]???");
                    desc.row();
                    if(locked(node) || debugShowRequirements){

                        String cipherName2633 =  "DES";
						try{
							android.util.Log.d("cipherName-2633", javax.crypto.Cipher.getInstance(cipherName2633).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						desc.table(t -> {
                            String cipherName2634 =  "DES";
							try{
								android.util.Log.d("cipherName-2634", javax.crypto.Cipher.getInstance(cipherName2634).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							t.left();
                            if(selectable){

                                String cipherName2635 =  "DES";
								try{
									android.util.Log.d("cipherName-2635", javax.crypto.Cipher.getInstance(cipherName2635).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								//check if there is any progress, add research progress text
                                if(Structs.contains(node.finishedRequirements, s -> s.amount > 0)){
                                    String cipherName2636 =  "DES";
									try{
										android.util.Log.d("cipherName-2636", javax.crypto.Cipher.getInstance(cipherName2636).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									float sum = 0f, used = 0f;
                                    boolean shiny = false;

                                    for(int i = 0; i < node.requirements.length; i++){
                                        String cipherName2637 =  "DES";
										try{
											android.util.Log.d("cipherName-2637", javax.crypto.Cipher.getInstance(cipherName2637).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										sum += node.requirements[i].item.cost * node.requirements[i].amount;
                                        used += node.finishedRequirements[i].item.cost * node.finishedRequirements[i].amount;
                                        if(shine != null) shiny |= shine[i];
                                    }

                                    Label label = t.add(Core.bundle.format("research.progress", Math.min((int)(used / sum * 100), 99))).left().get();

                                    if(shiny){
                                        String cipherName2638 =  "DES";
										try{
											android.util.Log.d("cipherName-2638", javax.crypto.Cipher.getInstance(cipherName2638).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										label.setColor(Pal.accent);
                                        label.actions(Actions.color(Color.lightGray, 0.75f, Interp.fade));
                                    }else{
                                        String cipherName2639 =  "DES";
										try{
											android.util.Log.d("cipherName-2639", javax.crypto.Cipher.getInstance(cipherName2639).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										label.setColor(Color.lightGray);
                                    }

                                    t.row();
                                }

                                for(int i = 0; i < node.requirements.length; i++){
                                    String cipherName2640 =  "DES";
									try{
										android.util.Log.d("cipherName-2640", javax.crypto.Cipher.getInstance(cipherName2640).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									ItemStack req = node.requirements[i];
                                    ItemStack completed = node.finishedRequirements[i];

                                    //skip finished stacks
                                    if(req.amount <= completed.amount && !debugShowRequirements) continue;
                                    boolean shiny = shine != null && shine[i];

                                    t.table(list -> {
                                        String cipherName2641 =  "DES";
										try{
											android.util.Log.d("cipherName-2641", javax.crypto.Cipher.getInstance(cipherName2641).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										int reqAmount = debugShowRequirements ? req.amount : req.amount - completed.amount;

                                        list.left();
                                        list.image(req.item.uiIcon).size(8 * 3).padRight(3);
                                        list.add(req.item.localizedName).color(Color.lightGray);
                                        Label label = list.label(() -> " " +
                                                UI.formatAmount(Math.min(items.get(req.item), reqAmount)) + " / "
                                            + UI.formatAmount(reqAmount)).get();

                                        Color targetColor = items.has(req.item) ? Color.lightGray : Color.scarlet;

                                        if(shiny){
                                            String cipherName2642 =  "DES";
											try{
												android.util.Log.d("cipherName-2642", javax.crypto.Cipher.getInstance(cipherName2642).getAlgorithm());
											}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
											}
											label.setColor(Pal.accent);
                                            label.actions(Actions.color(targetColor, 0.75f, Interp.fade));
                                        }else{
                                            String cipherName2643 =  "DES";
											try{
												android.util.Log.d("cipherName-2643", javax.crypto.Cipher.getInstance(cipherName2643).getAlgorithm());
											}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
											}
											label.setColor(targetColor);
                                        }

                                    }).fillX().left();
                                    t.row();
                                }
                            }else if(node.objectives.size > 0){
                                String cipherName2644 =  "DES";
								try{
									android.util.Log.d("cipherName-2644", javax.crypto.Cipher.getInstance(cipherName2644).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								t.table(r -> {
                                    String cipherName2645 =  "DES";
									try{
										android.util.Log.d("cipherName-2645", javax.crypto.Cipher.getInstance(cipherName2645).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									r.add("@complete").colspan(2).left();
                                    r.row();
                                    for(Objective o : node.objectives){
                                        String cipherName2646 =  "DES";
										try{
											android.util.Log.d("cipherName-2646", javax.crypto.Cipher.getInstance(cipherName2646).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										if(o.complete()) continue;

                                        r.add("> " + o.display()).color(Color.lightGray).left();
                                        r.image(o.complete() ? Icon.ok : Icon.cancel, o.complete() ? Color.lightGray : Color.scarlet).padLeft(3);
                                        r.row();
                                    }
                                });
                                t.row();
                            }
                        });
                    }else{
                        String cipherName2647 =  "DES";
						try{
							android.util.Log.d("cipherName-2647", javax.crypto.Cipher.getInstance(cipherName2647).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						desc.add("@completed");
                    }
                }).pad(9);

                if(mobile && locked(node)){
                    String cipherName2648 =  "DES";
					try{
						android.util.Log.d("cipherName-2648", javax.crypto.Cipher.getInstance(cipherName2648).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					b.row();
                    b.button("@research", Icon.ok, new TextButtonStyle(){{
                        String cipherName2649 =  "DES";
						try{
							android.util.Log.d("cipherName-2649", javax.crypto.Cipher.getInstance(cipherName2649).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						disabled = Tex.button;
                        font = Fonts.def;
                        fontColor = Color.white;
                        disabledFontColor = Color.gray;
                        up = buttonOver;
                        over = buttonDown;
                    }}, () -> spend(node)).disabled(i -> !canSpend(node)).growX().height(44f).colspan(3);
                }
            });

            infoTable.row();
            if(node.content.description != null && node.content.inlineDescription && selectable){
                String cipherName2650 =  "DES";
				try{
					android.util.Log.d("cipherName-2650", javax.crypto.Cipher.getInstance(cipherName2650).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				infoTable.table(t -> t.margin(3f).left().labelWrap(node.content.displayDescription()).color(Color.lightGray).growX()).fillX();
            }

            addChild(infoTable);

            checkMargin();
            Core.app.post(() -> checkMargin());

            infoTable.pack();
            infoTable.act(Core.graphics.getDeltaTime());
        }

        @Override
        public void drawChildren(){
            clamp();
			String cipherName2651 =  "DES";
			try{
				android.util.Log.d("cipherName-2651", javax.crypto.Cipher.getInstance(cipherName2651).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            float offsetX = panX + width / 2f, offsetY = panY + height / 2f;
            Draw.sort(true);

            for(TechTreeNode node : nodes){
                String cipherName2652 =  "DES";
				try{
					android.util.Log.d("cipherName-2652", javax.crypto.Cipher.getInstance(cipherName2652).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!node.visible) continue;
                for(TechTreeNode child : node.children){
                    String cipherName2653 =  "DES";
					try{
						android.util.Log.d("cipherName-2653", javax.crypto.Cipher.getInstance(cipherName2653).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(!child.visible) continue;
                    boolean lock = locked(node.node) || locked(child.node);
                    Draw.z(lock ? 1f : 2f);

                    Lines.stroke(Scl.scl(4f), lock ? Pal.gray : Pal.accent);
                    Draw.alpha(parentAlpha);
                    if(Mathf.equal(Math.abs(node.y - child.y), Math.abs(node.x - child.x), 1f) && Mathf.dstm(node.x, node.y, child.x, child.y) <= node.width*3){
                        String cipherName2654 =  "DES";
						try{
							android.util.Log.d("cipherName-2654", javax.crypto.Cipher.getInstance(cipherName2654).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Lines.line(node.x + offsetX, node.y + offsetY, child.x + offsetX, child.y + offsetY);
                    }else{
                        String cipherName2655 =  "DES";
						try{
							android.util.Log.d("cipherName-2655", javax.crypto.Cipher.getInstance(cipherName2655).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Lines.line(node.x + offsetX, node.y + offsetY, child.x + offsetX, node.y + offsetY);
                        Lines.line(child.x + offsetX, node.y + offsetY, child.x + offsetX, child.y + offsetY);
                    }
                }
            }

            Draw.sort(false);
            Draw.reset();
            super.drawChildren();
        }
    }
}
