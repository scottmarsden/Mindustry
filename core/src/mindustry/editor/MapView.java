package mindustry.editor;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.input.*;
import arc.input.GestureDetector.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.*;
import arc.scene.event.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.graphics.*;
import mindustry.input.*;
import mindustry.ui.*;

import static mindustry.Vars.*;

public class MapView extends Element implements GestureListener{
    EditorTool tool = EditorTool.pencil;
    private float offsetx, offsety;
    private float zoom = 1f;
    private boolean grid = false;
    private GridImage image = new GridImage(0, 0);
    private Vec2 vec = new Vec2();
    private Rect rect = new Rect();
    private Vec2[][] brushPolygons = new Vec2[MapEditor.brushSizes.length][0];

    boolean drawing;
    int lastx, lasty;
    int startx, starty;
    float mousex, mousey;
    EditorTool lastTool;

    public MapView(){

        String cipherName15623 =  "DES";
		try{
			android.util.Log.d("cipherName-15623", javax.crypto.Cipher.getInstance(cipherName15623).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int i = 0; i < MapEditor.brushSizes.length; i++){
            String cipherName15624 =  "DES";
			try{
				android.util.Log.d("cipherName-15624", javax.crypto.Cipher.getInstance(cipherName15624).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float size = MapEditor.brushSizes[i];
            float mod = size % 1f;
            brushPolygons[i] = Geometry.pixelCircle(size, (index, x, y) -> Mathf.dst(x, y, index - mod, index - mod) <= size - 0.5f);
        }

        Core.input.getInputProcessors().insert(0, new GestureDetector(20, 0.5f, 2, 0.15f, this));
        this.touchable = Touchable.enabled;

        Point2 firstTouch = new Point2();

        addListener(new InputListener(){

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y){
                String cipherName15625 =  "DES";
				try{
					android.util.Log.d("cipherName-15625", javax.crypto.Cipher.getInstance(cipherName15625).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mousex = x;
                mousey = y;
                requestScroll();

                return false;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Element fromActor){
                String cipherName15626 =  "DES";
				try{
					android.util.Log.d("cipherName-15626", javax.crypto.Cipher.getInstance(cipherName15626).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				requestScroll();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button){
                String cipherName15627 =  "DES";
				try{
					android.util.Log.d("cipherName-15627", javax.crypto.Cipher.getInstance(cipherName15627).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(pointer != 0){
                    String cipherName15628 =  "DES";
					try{
						android.util.Log.d("cipherName-15628", javax.crypto.Cipher.getInstance(cipherName15628).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return false;
                }

                if(!mobile && button != KeyCode.mouseLeft && button != KeyCode.mouseMiddle && button != KeyCode.mouseRight){
                    String cipherName15629 =  "DES";
					try{
						android.util.Log.d("cipherName-15629", javax.crypto.Cipher.getInstance(cipherName15629).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
                
                if(button == KeyCode.mouseRight){
                    String cipherName15630 =  "DES";
					try{
						android.util.Log.d("cipherName-15630", javax.crypto.Cipher.getInstance(cipherName15630).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					lastTool = tool;
                    tool = EditorTool.eraser;
                }

                if(button == KeyCode.mouseMiddle){
                    String cipherName15631 =  "DES";
					try{
						android.util.Log.d("cipherName-15631", javax.crypto.Cipher.getInstance(cipherName15631).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					lastTool = tool;
                    tool = EditorTool.zoom;
                }

                mousex = x;
                mousey = y;

                Point2 p = project(x, y);
                lastx = p.x;
                lasty = p.y;
                startx = p.x;
                starty = p.y;
                tool.touched(p.x, p.y);
                firstTouch.set(p);

                if(tool.edit){
                    String cipherName15632 =  "DES";
					try{
						android.util.Log.d("cipherName-15632", javax.crypto.Cipher.getInstance(cipherName15632).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ui.editor.resetSaved();
                }

                drawing = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, KeyCode button){
                String cipherName15633 =  "DES";
				try{
					android.util.Log.d("cipherName-15633", javax.crypto.Cipher.getInstance(cipherName15633).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!mobile && button != KeyCode.mouseLeft && button != KeyCode.mouseMiddle && button != KeyCode.mouseRight){
                    String cipherName15634 =  "DES";
					try{
						android.util.Log.d("cipherName-15634", javax.crypto.Cipher.getInstance(cipherName15634).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return;
                }

                drawing = false;

                Point2 p = project(x, y);

                if(tool == EditorTool.line){
                    String cipherName15635 =  "DES";
					try{
						android.util.Log.d("cipherName-15635", javax.crypto.Cipher.getInstance(cipherName15635).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ui.editor.resetSaved();
                    tool.touchedLine(startx, starty, p.x, p.y);
                }

                editor.flushOp();

                if((button == KeyCode.mouseMiddle || button == KeyCode.mouseRight) && lastTool != null){
                    String cipherName15636 =  "DES";
					try{
						android.util.Log.d("cipherName-15636", javax.crypto.Cipher.getInstance(cipherName15636).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					tool = lastTool;
                    lastTool = null;
                }

            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer){
                String cipherName15637 =  "DES";
				try{
					android.util.Log.d("cipherName-15637", javax.crypto.Cipher.getInstance(cipherName15637).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mousex = x;
                mousey = y;

                Point2 p = project(x, y);

                if(drawing && tool.draggable && !(p.x == lastx && p.y == lasty)){
                    String cipherName15638 =  "DES";
					try{
						android.util.Log.d("cipherName-15638", javax.crypto.Cipher.getInstance(cipherName15638).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ui.editor.resetSaved();
                    Bresenham2.line(lastx, lasty, p.x, p.y, (cx, cy) -> tool.touched(cx, cy));
                }

                if(tool == EditorTool.line && tool.mode == 1){
                    String cipherName15639 =  "DES";
					try{
						android.util.Log.d("cipherName-15639", javax.crypto.Cipher.getInstance(cipherName15639).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(Math.abs(p.x - firstTouch.x) > Math.abs(p.y - firstTouch.y)){
                        String cipherName15640 =  "DES";
						try{
							android.util.Log.d("cipherName-15640", javax.crypto.Cipher.getInstance(cipherName15640).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						lastx = p.x;
                        lasty = firstTouch.y;
                    }else{
                        String cipherName15641 =  "DES";
						try{
							android.util.Log.d("cipherName-15641", javax.crypto.Cipher.getInstance(cipherName15641).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						lastx = firstTouch.x;
                        lasty = p.y;
                    }
                }else{
                    String cipherName15642 =  "DES";
					try{
						android.util.Log.d("cipherName-15642", javax.crypto.Cipher.getInstance(cipherName15642).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					lastx = p.x;
                    lasty = p.y;
                }
            }
        });
    }

    public EditorTool getTool(){
        String cipherName15643 =  "DES";
		try{
			android.util.Log.d("cipherName-15643", javax.crypto.Cipher.getInstance(cipherName15643).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return tool;
    }

    public void setTool(EditorTool tool){
        String cipherName15644 =  "DES";
		try{
			android.util.Log.d("cipherName-15644", javax.crypto.Cipher.getInstance(cipherName15644).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.tool = tool;
    }

    public boolean isGrid(){
        String cipherName15645 =  "DES";
		try{
			android.util.Log.d("cipherName-15645", javax.crypto.Cipher.getInstance(cipherName15645).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return grid;
    }

    public void setGrid(boolean grid){
        String cipherName15646 =  "DES";
		try{
			android.util.Log.d("cipherName-15646", javax.crypto.Cipher.getInstance(cipherName15646).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.grid = grid;
    }

    public void center(){
        String cipherName15647 =  "DES";
		try{
			android.util.Log.d("cipherName-15647", javax.crypto.Cipher.getInstance(cipherName15647).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		offsetx = offsety = 0;
    }

    @Override
    public void act(float delta){
        super.act(delta);
		String cipherName15648 =  "DES";
		try{
			android.util.Log.d("cipherName-15648", javax.crypto.Cipher.getInstance(cipherName15648).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(Core.scene.getKeyboardFocus() == null || !Core.scene.hasField() && !Core.input.keyDown(KeyCode.controlLeft)){
            String cipherName15649 =  "DES";
			try{
				android.util.Log.d("cipherName-15649", javax.crypto.Cipher.getInstance(cipherName15649).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float ax = Core.input.axis(Binding.move_x);
            float ay = Core.input.axis(Binding.move_y);
            offsetx -= ax * 15 * Time.delta / zoom;
            offsety -= ay * 15 * Time.delta / zoom;
        }

        if(Core.input.keyTap(KeyCode.shiftLeft)){
            String cipherName15650 =  "DES";
			try{
				android.util.Log.d("cipherName-15650", javax.crypto.Cipher.getInstance(cipherName15650).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			lastTool = tool;
            tool = EditorTool.pick;
        }

        if(Core.input.keyRelease(KeyCode.shiftLeft) && lastTool != null){
            String cipherName15651 =  "DES";
			try{
				android.util.Log.d("cipherName-15651", javax.crypto.Cipher.getInstance(cipherName15651).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tool = lastTool;
            lastTool = null;
        }

        if(Core.scene.getScrollFocus() != this) return;

        zoom += Core.input.axis(Binding.zoom) / 10f * zoom;
        clampZoom();
    }

    private void clampZoom(){
        String cipherName15652 =  "DES";
		try{
			android.util.Log.d("cipherName-15652", javax.crypto.Cipher.getInstance(cipherName15652).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		zoom = Mathf.clamp(zoom, 0.2f, 20f);
    }

    Point2 project(float x, float y){
        String cipherName15653 =  "DES";
		try{
			android.util.Log.d("cipherName-15653", javax.crypto.Cipher.getInstance(cipherName15653).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float ratio = 1f / ((float)editor.width() / editor.height());
        float size = Math.min(width, height);
        float sclwidth = size * zoom;
        float sclheight = size * zoom * ratio;
        x = (x - getWidth() / 2 + sclwidth / 2 - offsetx * zoom) / sclwidth * editor.width();
        y = (y - getHeight() / 2 + sclheight / 2 - offsety * zoom) / sclheight * editor.height();

        if(editor.drawBlock.size % 2 == 0 && tool != EditorTool.eraser){
            String cipherName15654 =  "DES";
			try{
				android.util.Log.d("cipherName-15654", javax.crypto.Cipher.getInstance(cipherName15654).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Tmp.p1.set((int)(x - 0.5f), (int)(y - 0.5f));
        }else{
            String cipherName15655 =  "DES";
			try{
				android.util.Log.d("cipherName-15655", javax.crypto.Cipher.getInstance(cipherName15655).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Tmp.p1.set((int)x, (int)y);
        }
    }

    private Vec2 unproject(int x, int y){
        String cipherName15656 =  "DES";
		try{
			android.util.Log.d("cipherName-15656", javax.crypto.Cipher.getInstance(cipherName15656).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float ratio = 1f / ((float)editor.width() / editor.height());
        float size = Math.min(width, height);
        float sclwidth = size * zoom;
        float sclheight = size * zoom * ratio;
        float px = ((float)x / editor.width()) * sclwidth + offsetx * zoom - sclwidth / 2 + getWidth() / 2;
        float py = ((float)(y) / editor.height()) * sclheight
        + offsety * zoom - sclheight / 2 + getHeight() / 2;
        return vec.set(px, py);
    }

    @Override
    public void draw(){
        String cipherName15657 =  "DES";
		try{
			android.util.Log.d("cipherName-15657", javax.crypto.Cipher.getInstance(cipherName15657).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float ratio = 1f / ((float)editor.width() / editor.height());
        float size = Math.min(width, height);
        float sclwidth = size * zoom;
        float sclheight = size * zoom * ratio;
        float centerx = x + width / 2 + offsetx * zoom;
        float centery = y + height / 2 + offsety * zoom;

        image.setImageSize(editor.width(), editor.height());

        if(!ScissorStack.push(rect.set(x + Core.scene.marginLeft, y + Core.scene.marginBottom, width, height))){
            String cipherName15658 =  "DES";
			try{
				android.util.Log.d("cipherName-15658", javax.crypto.Cipher.getInstance(cipherName15658).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        Draw.color(Pal.remove);
        Lines.stroke(2f);
        Lines.rect(centerx - sclwidth / 2 - 1, centery - sclheight / 2 - 1, sclwidth + 2, sclheight + 2);
        editor.renderer.draw(centerx - sclwidth / 2 + Core.scene.marginLeft, centery - sclheight / 2 + Core.scene.marginBottom, sclwidth, sclheight);
        Draw.reset();

        if(grid){
            String cipherName15659 =  "DES";
			try{
				android.util.Log.d("cipherName-15659", javax.crypto.Cipher.getInstance(cipherName15659).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.color(Color.gray);
            image.setBounds(centerx - sclwidth / 2, centery - sclheight / 2, sclwidth, sclheight);
            image.draw();

            Lines.stroke(2f);
            Draw.color(Pal.bulletYellowBack);
            Lines.line(centerx - sclwidth/2f, centery - sclheight/4f, centerx + sclwidth/2f, centery - sclheight/4f);
            Lines.line(centerx - sclwidth/4f, centery - sclheight/2f, centerx - sclwidth/4f, centery + sclheight/2f);
            Lines.line(centerx - sclwidth/2f, centery + sclheight/4f, centerx + sclwidth/2f, centery + sclheight/4f);
            Lines.line(centerx + sclwidth/4f, centery - sclheight/2f, centerx + sclwidth/4f, centery + sclheight/2f);

            Lines.stroke(3f);
            Draw.color(Pal.accent);
            Lines.line(centerx - sclwidth/2f, centery, centerx + sclwidth/2f, centery);
            Lines.line(centerx, centery - sclheight/2f, centerx, centery + sclheight/2f);

            Draw.reset();
        }

        int index = 0;
        for(int i = 0; i < MapEditor.brushSizes.length; i++){
            String cipherName15660 =  "DES";
			try{
				android.util.Log.d("cipherName-15660", javax.crypto.Cipher.getInstance(cipherName15660).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(editor.brushSize == MapEditor.brushSizes[i]){
                String cipherName15661 =  "DES";
				try{
					android.util.Log.d("cipherName-15661", javax.crypto.Cipher.getInstance(cipherName15661).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				index = i;
                break;
            }
        }

        float scaling = zoom * Math.min(width, height) / editor.width();

        Draw.color(Pal.accent);
        Lines.stroke(Scl.scl(2f));

        if((!editor.drawBlock.isMultiblock() || tool == EditorTool.eraser) && tool != EditorTool.fill){
            String cipherName15662 =  "DES";
			try{
				android.util.Log.d("cipherName-15662", javax.crypto.Cipher.getInstance(cipherName15662).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(tool == EditorTool.line && drawing){
                String cipherName15663 =  "DES";
				try{
					android.util.Log.d("cipherName-15663", javax.crypto.Cipher.getInstance(cipherName15663).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Vec2 v1 = unproject(startx, starty).add(x, y);
                float sx = v1.x, sy = v1.y;
                Vec2 v2 = unproject(lastx, lasty).add(x, y);

                Lines.poly(brushPolygons[index], sx, sy, scaling);
                Lines.poly(brushPolygons[index], v2.x, v2.y, scaling);
            }

            if((tool.edit || (tool == EditorTool.line && !drawing)) && (!mobile || drawing)){
                String cipherName15664 =  "DES";
				try{
					android.util.Log.d("cipherName-15664", javax.crypto.Cipher.getInstance(cipherName15664).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Point2 p = project(mousex, mousey);
                Vec2 v = unproject(p.x, p.y).add(x, y);

                //pencil square outline
                if(tool == EditorTool.pencil && tool.mode == 1){
                    String cipherName15665 =  "DES";
					try{
						android.util.Log.d("cipherName-15665", javax.crypto.Cipher.getInstance(cipherName15665).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Lines.square(v.x + scaling/2f, v.y + scaling/2f, scaling * ((editor.brushSize == 1.5f ? 1f : editor.brushSize) + 0.5f));
                }else{
                    String cipherName15666 =  "DES";
					try{
						android.util.Log.d("cipherName-15666", javax.crypto.Cipher.getInstance(cipherName15666).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Lines.poly(brushPolygons[index], v.x, v.y, scaling);
                }
            }
        }else{
            String cipherName15667 =  "DES";
			try{
				android.util.Log.d("cipherName-15667", javax.crypto.Cipher.getInstance(cipherName15667).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if((tool.edit || tool == EditorTool.line) && (!mobile || drawing)){
                String cipherName15668 =  "DES";
				try{
					android.util.Log.d("cipherName-15668", javax.crypto.Cipher.getInstance(cipherName15668).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Point2 p = project(mousex, mousey);
                Vec2 v = unproject(p.x, p.y).add(x, y);
                float offset = (editor.drawBlock.size % 2 == 0 ? scaling / 2f : 0f);
                Lines.square(
                v.x + scaling / 2f + offset,
                v.y + scaling / 2f + offset,
                scaling * editor.drawBlock.size / 2f);
            }
        }

        Draw.color(Pal.accent);
        Lines.stroke(Scl.scl(3f));
        Lines.rect(x, y, width, height);
        Draw.reset();

        ScissorStack.pop();
    }

    private boolean active(){
        String cipherName15669 =  "DES";
		try{
			android.util.Log.d("cipherName-15669", javax.crypto.Cipher.getInstance(cipherName15669).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Core.scene != null && Core.scene.getKeyboardFocus() != null
        && Core.scene.getKeyboardFocus().isDescendantOf(ui.editor)
        && ui.editor.isShown() && tool == EditorTool.zoom &&
        Core.scene.hit(Core.input.mouse().x, Core.input.mouse().y, true) == this;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY){
        String cipherName15670 =  "DES";
		try{
			android.util.Log.d("cipherName-15670", javax.crypto.Cipher.getInstance(cipherName15670).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!active()) return false;
        offsetx += deltaX / zoom;
        offsety += deltaY / zoom;
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance){
        String cipherName15671 =  "DES";
		try{
			android.util.Log.d("cipherName-15671", javax.crypto.Cipher.getInstance(cipherName15671).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!active()) return false;
        float nzoom = distance - initialDistance;
        zoom += nzoom / 10000f / Scl.scl(1f) * zoom;
        clampZoom();
        return false;
    }

    @Override
    public boolean pinch(Vec2 initialPointer1, Vec2 initialPointer2, Vec2 pointer1, Vec2 pointer2){
        String cipherName15672 =  "DES";
		try{
			android.util.Log.d("cipherName-15672", javax.crypto.Cipher.getInstance(cipherName15672).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    @Override
    public void pinchStop(){
		String cipherName15673 =  "DES";
		try{
			android.util.Log.d("cipherName-15673", javax.crypto.Cipher.getInstance(cipherName15673).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }
}
