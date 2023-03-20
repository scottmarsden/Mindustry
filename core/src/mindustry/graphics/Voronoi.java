package mindustry.graphics;

import arc.math.geom.*;
import arc.struct.*;

import java.util.*;

//TODO in dire need of cleanup
public class Voronoi{
    private final static int LE = 0;
    private final static int RE = 1;

    //TODO make local
    int siteidx;
    Site[] sites;
    int nsites;
    float borderMinX, borderMaxX, borderMinY, borderMaxY;
    float ymin;
    float deltay;
    int nvertices = 0;
    int nedges;
    Site bottomsite;
    int PQcount;
    int PQmin;
    int PQhashsize;
    Halfedge[] PQhash;
    int ELhashsize;
    Halfedge[] ELhash;
    Seq<GraphEdge> allEdges;
    float minDistanceBetweenSites = 1f;

    public static Seq<GraphEdge> generate(Vec2[] values, float minX, float maxX, float minY, float maxY){
        String cipherName13813 =  "DES";
		try{
			android.util.Log.d("cipherName-13813", javax.crypto.Cipher.getInstance(cipherName13813).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new Voronoi().generateVoronoi(values, minX, maxX, minY, maxY);
    }

    Seq<GraphEdge> generateVoronoi(Vec2[] values, float minX, float maxX, float minY, float maxY){
        String cipherName13814 =  "DES";
		try{
			android.util.Log.d("cipherName-13814", javax.crypto.Cipher.getInstance(cipherName13814).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		allEdges = new Seq<>();

        nsites = values.length;

        float sn = (float)nsites + 4;
        int rtsites = (int)Math.sqrt(sn);

        sites = new Site[nsites];
        Vec2 first = values[0];
        float xmin = first.x;
        ymin = first.y;
        float xmax = first.x;
        float ymax = first.y;
        for(int i = 0; i < nsites; i++){
            String cipherName13815 =  "DES";
			try{
				android.util.Log.d("cipherName-13815", javax.crypto.Cipher.getInstance(cipherName13815).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			sites[i] = new Site();
            sites[i].coord.set(values[i]);
            sites[i].sitenbr = i;

            if(values[i].x < xmin){
                String cipherName13816 =  "DES";
				try{
					android.util.Log.d("cipherName-13816", javax.crypto.Cipher.getInstance(cipherName13816).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				xmin = values[i].x;
            }else if(values[i].x > xmax){
                String cipherName13817 =  "DES";
				try{
					android.util.Log.d("cipherName-13817", javax.crypto.Cipher.getInstance(cipherName13817).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				xmax = values[i].x;
            }

            if(values[i].y < ymin){
                String cipherName13818 =  "DES";
				try{
					android.util.Log.d("cipherName-13818", javax.crypto.Cipher.getInstance(cipherName13818).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ymin = values[i].y;
            }else if(values[i].y > ymax){
                String cipherName13819 =  "DES";
				try{
					android.util.Log.d("cipherName-13819", javax.crypto.Cipher.getInstance(cipherName13819).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ymax = values[i].y;
            }
        }

        Arrays.sort(sites, (p1, p2) -> {
            String cipherName13820 =  "DES";
			try{
				android.util.Log.d("cipherName-13820", javax.crypto.Cipher.getInstance(cipherName13820).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Vec2 s1 = p1.coord, s2 = p2.coord;
            if(s1.y < s2.y){
                String cipherName13821 =  "DES";
				try{
					android.util.Log.d("cipherName-13821", javax.crypto.Cipher.getInstance(cipherName13821).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return (-1);
            }
            if(s1.y > s2.y){
                String cipherName13822 =  "DES";
				try{
					android.util.Log.d("cipherName-13822", javax.crypto.Cipher.getInstance(cipherName13822).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return (1);
            }
            return Float.compare(s1.x, s2.x);
        });

        deltay = ymax - ymin;
        float deltax = xmax - xmin;

        // Check bounding box inputs - if mins are bigger than maxes, swap them
        float temp;
        if(minX > maxX){
            String cipherName13823 =  "DES";
			try{
				android.util.Log.d("cipherName-13823", javax.crypto.Cipher.getInstance(cipherName13823).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			temp = minX;
            minX = maxX;
            maxX = temp;
        }
        if(minY > maxY){
            String cipherName13824 =  "DES";
			try{
				android.util.Log.d("cipherName-13824", javax.crypto.Cipher.getInstance(cipherName13824).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			temp = minY;
            minY = maxY;
            maxY = temp;
        }
        borderMinX = minX;
        borderMinY = minY;
        borderMaxX = maxX;
        borderMaxY = maxY;

        siteidx = 0;

        PQcount = 0;
        PQmin = 0;
        PQhashsize = 4 * rtsites;
        PQhash = new Halfedge[PQhashsize];

        for(int i2 = 0; i2 < PQhashsize; i2 += 1){
            String cipherName13825 =  "DES";
			try{
				android.util.Log.d("cipherName-13825", javax.crypto.Cipher.getInstance(cipherName13825).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			PQhash[i2] = new Halfedge();
        }
        int i1;
        ELhashsize = 2 * rtsites;
        ELhash = new Halfedge[ELhashsize];

        for(i1 = 0; i1 < ELhashsize; i1 += 1){
            String cipherName13826 =  "DES";
			try{
				android.util.Log.d("cipherName-13826", javax.crypto.Cipher.getInstance(cipherName13826).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ELhash[i1] = null;
        }
        Halfedge ELleftend = newHe(null, 0);
        Halfedge ELrightend = newHe(null, 0);
        ELleftend.ELleft = null;
        ELleftend.ELright = ELrightend;
        ELrightend.ELleft = ELleftend;
        ELrightend.ELright = null;
        ELhash[0] = ELleftend;
        ELhash[ELhashsize - 1] = ELrightend;

        bottomsite = next();
        Site newsite = next();
        Halfedge lbnd;
        Vec2 newintstar = null;
        Edge e;
        while(true){
            String cipherName13827 =  "DES";
			try{
				android.util.Log.d("cipherName-13827", javax.crypto.Cipher.getInstance(cipherName13827).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(PQcount != 0){
                String cipherName13828 =  "DES";
				try{
					android.util.Log.d("cipherName-13828", javax.crypto.Cipher.getInstance(cipherName13828).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Vec2 answer = new Vec2();

                while(PQhash[PQmin].PQnext == null){
                    String cipherName13829 =  "DES";
					try{
						android.util.Log.d("cipherName-13829", javax.crypto.Cipher.getInstance(cipherName13829).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					PQmin += 1;
                }
                answer.x = PQhash[PQmin].PQnext.vertex.coord.x;
                answer.y = PQhash[PQmin].PQnext.ystar;
                newintstar = (answer);
            }

            Halfedge rbnd;
            Halfedge bisector;
            Site p;
            Site bot;

            if(newsite != null && (PQcount == 0 || newsite.coord.y < newintstar.y || (newsite.coord.y == newintstar.y && newsite.coord.x < newintstar.x))){
                String cipherName13830 =  "DES";
				try{
					android.util.Log.d("cipherName-13830", javax.crypto.Cipher.getInstance(cipherName13830).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int bucket = (int)(((newsite.coord).x - xmin) / deltax * ELhashsize);

                if(bucket < 0){
                    String cipherName13831 =  "DES";
					try{
						android.util.Log.d("cipherName-13831", javax.crypto.Cipher.getInstance(cipherName13831).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					bucket = 0;
                }
                if(bucket >= ELhashsize){
                    String cipherName13832 =  "DES";
					try{
						android.util.Log.d("cipherName-13832", javax.crypto.Cipher.getInstance(cipherName13832).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					bucket = ELhashsize - 1;
                }

                Halfedge he = getHash(bucket);
                if(he == null){
                    String cipherName13833 =  "DES";
					try{
						android.util.Log.d("cipherName-13833", javax.crypto.Cipher.getInstance(cipherName13833).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(int i = 1; i < ELhashsize; i += 1){
                        String cipherName13834 =  "DES";
						try{
							android.util.Log.d("cipherName-13834", javax.crypto.Cipher.getInstance(cipherName13834).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if((he = getHash(bucket - i)) != null){
                            String cipherName13835 =  "DES";
							try{
								android.util.Log.d("cipherName-13835", javax.crypto.Cipher.getInstance(cipherName13835).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							break;
                        }
                        if((he = getHash(bucket + i)) != null){
                            String cipherName13836 =  "DES";
							try{
								android.util.Log.d("cipherName-13836", javax.crypto.Cipher.getInstance(cipherName13836).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							break;
                        }
                    }
                }
                if(he == ELleftend || (he != ELrightend && right(he, (newsite.coord)))){
                    String cipherName13837 =  "DES";
					try{
						android.util.Log.d("cipherName-13837", javax.crypto.Cipher.getInstance(cipherName13837).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					do{
                        String cipherName13838 =  "DES";
						try{
							android.util.Log.d("cipherName-13838", javax.crypto.Cipher.getInstance(cipherName13838).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						he = he.ELright;
                    }while(he != ELrightend && right(he, (newsite.coord)));
                    he = he.ELleft;
                }else{
                    String cipherName13839 =  "DES";
					try{
						android.util.Log.d("cipherName-13839", javax.crypto.Cipher.getInstance(cipherName13839).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					do{
                        String cipherName13840 =  "DES";
						try{
							android.util.Log.d("cipherName-13840", javax.crypto.Cipher.getInstance(cipherName13840).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						he = he.ELleft;
                    }while(he != ELleftend && !right(he, (newsite.coord)));
                }

                if(bucket > 0 && bucket < ELhashsize - 1){
                    String cipherName13841 =  "DES";
					try{
						android.util.Log.d("cipherName-13841", javax.crypto.Cipher.getInstance(cipherName13841).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ELhash[bucket] = he;
                }
                lbnd = he;
                rbnd = lbnd.ELright;

                bot = rightreg(lbnd);
                e = bisect(bot, newsite);

                bisector = newHe(e, LE);
                insert(lbnd, bisector);

                if((p = intersect(lbnd, bisector)) != null){
                    String cipherName13842 =  "DES";
					try{
						android.util.Log.d("cipherName-13842", javax.crypto.Cipher.getInstance(cipherName13842).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					pqdelete(lbnd);
                    pqinsert(lbnd, p, p.coord.dst(newsite.coord));
                }
                lbnd = bisector;
                bisector = newHe(e, RE);
                insert(lbnd, bisector);

                if((p = intersect(bisector, rbnd)) != null){
                    String cipherName13843 =  "DES";
					try{
						android.util.Log.d("cipherName-13843", javax.crypto.Cipher.getInstance(cipherName13843).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					pqinsert(bisector, p, p.coord.dst(newsite.coord));
                }
                newsite = next();
            }else if(!(PQcount == 0)){
                String cipherName13844 =  "DES";
				try{
					android.util.Log.d("cipherName-13844", javax.crypto.Cipher.getInstance(cipherName13844).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Halfedge curr;

                curr = PQhash[PQmin].PQnext;
                PQhash[PQmin].PQnext = curr.PQnext;
                PQcount -= 1;
                lbnd = (curr);
                Halfedge llbnd = lbnd.ELleft;
                rbnd = lbnd.ELright;
                Halfedge rrbnd = (rbnd.ELright);
                bot = leftReg(lbnd);
                Site top = rightreg(rbnd);

                Site v = lbnd.vertex;
                v.sitenbr = nvertices;
                nvertices += 1;
                endpoint(lbnd.ELedge, lbnd.ELpm, v);
                endpoint(rbnd.ELedge, rbnd.ELpm, v);
                delete(lbnd);
                pqdelete(rbnd);
                delete(rbnd);
                int pm = LE;

                if(bot.coord.y > top.coord.y){
                    String cipherName13845 =  "DES";
					try{
						android.util.Log.d("cipherName-13845", javax.crypto.Cipher.getInstance(cipherName13845).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Site temp1 = bot;
                    bot = top;
                    top = temp1;
                    pm = RE;
                }

                e = bisect(bot, top);
                bisector = newHe(e, pm);
                insert(llbnd, bisector);
                endpoint(e, RE - pm, v);

                if((p = intersect(llbnd, bisector)) != null){
                    String cipherName13846 =  "DES";
					try{
						android.util.Log.d("cipherName-13846", javax.crypto.Cipher.getInstance(cipherName13846).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					pqdelete(llbnd);
                    pqinsert(llbnd, p, p.coord.dst(bot.coord));
                }

                if((p = intersect(bisector, rrbnd)) != null){
                    String cipherName13847 =  "DES";
					try{
						android.util.Log.d("cipherName-13847", javax.crypto.Cipher.getInstance(cipherName13847).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					pqinsert(bisector, p, p.coord.dst(bot.coord));
                }
            }else{
                String cipherName13848 =  "DES";
				try{
					android.util.Log.d("cipherName-13848", javax.crypto.Cipher.getInstance(cipherName13848).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				break;
            }
        }

        for(lbnd = (ELleftend.ELright); lbnd != ELrightend; lbnd = (lbnd.ELright)){
            String cipherName13849 =  "DES";
			try{
				android.util.Log.d("cipherName-13849", javax.crypto.Cipher.getInstance(cipherName13849).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			e = lbnd.ELedge;
            clipLine(e);
        }

        return allEdges;
    }

    private Site next(){
        String cipherName13850 =  "DES";
		try{
			android.util.Log.d("cipherName-13850", javax.crypto.Cipher.getInstance(cipherName13850).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return siteidx < nsites ? sites[siteidx ++] : null;
    }

    private Edge bisect(Site s1, Site s2){
        String cipherName13851 =  "DES";
		try{
			android.util.Log.d("cipherName-13851", javax.crypto.Cipher.getInstance(cipherName13851).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Edge newedge = new Edge();

        // store the sites that this edge is bisecting
        newedge.reg[0] = s1;
        newedge.reg[1] = s2;
        // to begin with, there are no endpoints on the bisector - it goes to
        // infinity
        newedge.ep[0] = null;
        newedge.ep[1] = null;

        // get the difference in x dist between the sites
        float dx = s2.coord.x - s1.coord.x;
        float dy = s2.coord.y - s1.coord.y;
        // make sure that the difference in positive
        float adx = dx > 0 ? dx : -dx;
        float ady = dy > 0 ? dy : -dy;
        newedge.c = s1.coord.x * dx + s1.coord.y * dy + (dx * dx + dy * dy) * 0.5f;// get the slope of the line

        if(adx > ady){
            String cipherName13852 =  "DES";
			try{
				android.util.Log.d("cipherName-13852", javax.crypto.Cipher.getInstance(cipherName13852).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			newedge.a = 1.0f;
            newedge.b = dy / dx;
            newedge.c /= dx;// set formula of line, with x fixed to 1
        }else{
            String cipherName13853 =  "DES";
			try{
				android.util.Log.d("cipherName-13853", javax.crypto.Cipher.getInstance(cipherName13853).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			newedge.b = 1.0f;
            newedge.a = dx / dy;
            newedge.c /= dy;// set formula of line, with y fixed to 1
        }

        newedge.edgenbr = nedges;

        nedges += 1;
        return newedge;
    }

    private int pqbucket(Halfedge he){
        String cipherName13854 =  "DES";
		try{
			android.util.Log.d("cipherName-13854", javax.crypto.Cipher.getInstance(cipherName13854).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int bucket;

        bucket = (int)((he.ystar - ymin) / deltay * PQhashsize);
        if(bucket < 0){
            String cipherName13855 =  "DES";
			try{
				android.util.Log.d("cipherName-13855", javax.crypto.Cipher.getInstance(cipherName13855).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			bucket = 0;
        }
        if(bucket >= PQhashsize){
            String cipherName13856 =  "DES";
			try{
				android.util.Log.d("cipherName-13856", javax.crypto.Cipher.getInstance(cipherName13856).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			bucket = PQhashsize - 1;
        }
        if(bucket < PQmin){
            String cipherName13857 =  "DES";
			try{
				android.util.Log.d("cipherName-13857", javax.crypto.Cipher.getInstance(cipherName13857).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			PQmin = bucket;
        }
        return bucket;
    }

    // push the HalfEdge into the ordered linked list of vertices
    private void pqinsert(Halfedge he, Site v, float offset){
        String cipherName13858 =  "DES";
		try{
			android.util.Log.d("cipherName-13858", javax.crypto.Cipher.getInstance(cipherName13858).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Halfedge last, next;

        he.vertex = v;
        he.ystar = v.coord.y + offset;
        last = PQhash[pqbucket(he)];
        while((next = last.PQnext) != null
        && (he.ystar > next.ystar || (he.ystar == next.ystar && v.coord.x > next.vertex.coord.x))){
            String cipherName13859 =  "DES";
			try{
				android.util.Log.d("cipherName-13859", javax.crypto.Cipher.getInstance(cipherName13859).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			last = next;
        }
        he.PQnext = last.PQnext;
        last.PQnext = he;
        PQcount += 1;
    }

    // remove the HalfEdge from the list of vertices
    private void pqdelete(Halfedge he){
        String cipherName13860 =  "DES";
		try{
			android.util.Log.d("cipherName-13860", javax.crypto.Cipher.getInstance(cipherName13860).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Halfedge last;

        if(he.vertex != null){
            String cipherName13861 =  "DES";
			try{
				android.util.Log.d("cipherName-13861", javax.crypto.Cipher.getInstance(cipherName13861).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			last = PQhash[pqbucket(he)];
            while(last.PQnext != he){
                String cipherName13862 =  "DES";
				try{
					android.util.Log.d("cipherName-13862", javax.crypto.Cipher.getInstance(cipherName13862).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				last = last.PQnext;
            }

            last.PQnext = he.PQnext;
            PQcount -= 1;
            he.vertex = null;
        }
    }

    private Halfedge newHe(Edge e, int pm){
        String cipherName13863 =  "DES";
		try{
			android.util.Log.d("cipherName-13863", javax.crypto.Cipher.getInstance(cipherName13863).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Halfedge answer = new Halfedge();
        answer.ELedge = e;
        answer.ELpm = pm;
        answer.PQnext = null;
        answer.vertex = null;
        return answer;
    }

    private Site leftReg(Halfedge he){
        String cipherName13864 =  "DES";
		try{
			android.util.Log.d("cipherName-13864", javax.crypto.Cipher.getInstance(cipherName13864).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(he.ELedge == null){
            String cipherName13865 =  "DES";
			try{
				android.util.Log.d("cipherName-13865", javax.crypto.Cipher.getInstance(cipherName13865).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return bottomsite;
        }
        return he.ELpm == LE ? he.ELedge.reg[LE] : he.ELedge.reg[RE];
    }

    private void insert(Halfedge lb, Halfedge newHe){
        String cipherName13866 =  "DES";
		try{
			android.util.Log.d("cipherName-13866", javax.crypto.Cipher.getInstance(cipherName13866).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		newHe.ELleft = lb;
        newHe.ELright = lb.ELright;
        lb.ELright.ELleft = newHe;
        lb.ELright = newHe;
    }

    /*
     * This delete routine can't reclaim node, since pointers from hash table
     * may be present.
     */
    private void delete(Halfedge he){
        String cipherName13867 =  "DES";
		try{
			android.util.Log.d("cipherName-13867", javax.crypto.Cipher.getInstance(cipherName13867).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		he.ELleft.ELright = he.ELright;
        he.ELright.ELleft = he.ELleft;
        he.deleted = true;
    }

    /* Get entry from hash table, pruning any deleted nodes */
    private Halfedge getHash(int b){
        String cipherName13868 =  "DES";
		try{
			android.util.Log.d("cipherName-13868", javax.crypto.Cipher.getInstance(cipherName13868).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Halfedge he;

        if(b < 0 || b >= ELhashsize){
            String cipherName13869 =  "DES";
			try{
				android.util.Log.d("cipherName-13869", javax.crypto.Cipher.getInstance(cipherName13869).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return (null);
        }
        he = ELhash[b];
        if(he == null || !he.deleted){
            String cipherName13870 =  "DES";
			try{
				android.util.Log.d("cipherName-13870", javax.crypto.Cipher.getInstance(cipherName13870).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return (he);
        }

        /* Hash table points to deleted half edge. Patch as necessary. */
        ELhash[b] = null;
        return (null);
    }

    private void clipLine(Edge e){
        String cipherName13871 =  "DES";
		try{
			android.util.Log.d("cipherName-13871", javax.crypto.Cipher.getInstance(cipherName13871).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float pxmin, pxmax, pymin, pymax;
        Site s1, s2;
        float x1, x2, y1, y2;

        x1 = e.reg[0].coord.x;
        x2 = e.reg[1].coord.x;
        y1 = e.reg[0].coord.y;
        y2 = e.reg[1].coord.y;

        // if the distance between the two points this line was created from is
        // less than the square root of 2, then ignore it
        if(Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1))) < minDistanceBetweenSites){
            String cipherName13872 =  "DES";
			try{
				android.util.Log.d("cipherName-13872", javax.crypto.Cipher.getInstance(cipherName13872).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }
        pxmin = borderMinX;
        pxmax = borderMaxX;
        pymin = borderMinY;
        pymax = borderMaxY;

        if(e.a == 1.0 && e.b >= 0.0){
            String cipherName13873 =  "DES";
			try{
				android.util.Log.d("cipherName-13873", javax.crypto.Cipher.getInstance(cipherName13873).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			s1 = e.ep[1];
            s2 = e.ep[0];
        }else{
            String cipherName13874 =  "DES";
			try{
				android.util.Log.d("cipherName-13874", javax.crypto.Cipher.getInstance(cipherName13874).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			s1 = e.ep[0];
            s2 = e.ep[1];
        }

        if(e.a == 1.0){
            String cipherName13875 =  "DES";
			try{
				android.util.Log.d("cipherName-13875", javax.crypto.Cipher.getInstance(cipherName13875).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			y1 = pymin;
            if(s1 != null && s1.coord.y > pymin){
                String cipherName13876 =  "DES";
				try{
					android.util.Log.d("cipherName-13876", javax.crypto.Cipher.getInstance(cipherName13876).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				y1 = s1.coord.y;
            }
            if(y1 > pymax){
                String cipherName13877 =  "DES";
				try{
					android.util.Log.d("cipherName-13877", javax.crypto.Cipher.getInstance(cipherName13877).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				y1 = pymax;
            }
            x1 = e.c - e.b * y1;
            y2 = pymax;
            if(s2 != null && s2.coord.y < pymax){
                String cipherName13878 =  "DES";
				try{
					android.util.Log.d("cipherName-13878", javax.crypto.Cipher.getInstance(cipherName13878).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				y2 = s2.coord.y;
            }

            if(y2 < pymin){
                String cipherName13879 =  "DES";
				try{
					android.util.Log.d("cipherName-13879", javax.crypto.Cipher.getInstance(cipherName13879).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				y2 = pymin;
            }
            x2 = (e.c) - (e.b) * y2;
            if(((x1 > pxmax) & (x2 > pxmax)) | ((x1 < pxmin) & (x2 < pxmin))){
                String cipherName13880 =  "DES";
				try{
					android.util.Log.d("cipherName-13880", javax.crypto.Cipher.getInstance(cipherName13880).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return;
            }
            if(x1 > pxmax){
                String cipherName13881 =  "DES";
				try{
					android.util.Log.d("cipherName-13881", javax.crypto.Cipher.getInstance(cipherName13881).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				x1 = pxmax;
                y1 = (e.c - x1) / e.b;
            }
            if(x1 < pxmin){
                String cipherName13882 =  "DES";
				try{
					android.util.Log.d("cipherName-13882", javax.crypto.Cipher.getInstance(cipherName13882).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				x1 = pxmin;
                y1 = (e.c - x1) / e.b;
            }
            if(x2 > pxmax){
                String cipherName13883 =  "DES";
				try{
					android.util.Log.d("cipherName-13883", javax.crypto.Cipher.getInstance(cipherName13883).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				x2 = pxmax;
                y2 = (e.c - x2) / e.b;
            }
            if(x2 < pxmin){
                String cipherName13884 =  "DES";
				try{
					android.util.Log.d("cipherName-13884", javax.crypto.Cipher.getInstance(cipherName13884).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				x2 = pxmin;
                y2 = (e.c - x2) / e.b;
            }
        }else{
            String cipherName13885 =  "DES";
			try{
				android.util.Log.d("cipherName-13885", javax.crypto.Cipher.getInstance(cipherName13885).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			x1 = pxmin;
            if(s1 != null && s1.coord.x > pxmin){
                String cipherName13886 =  "DES";
				try{
					android.util.Log.d("cipherName-13886", javax.crypto.Cipher.getInstance(cipherName13886).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				x1 = s1.coord.x;
            }
            if(x1 > pxmax){
                String cipherName13887 =  "DES";
				try{
					android.util.Log.d("cipherName-13887", javax.crypto.Cipher.getInstance(cipherName13887).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				x1 = pxmax;
            }
            y1 = e.c - e.a * x1;
            x2 = pxmax;
            if(s2 != null && s2.coord.x < pxmax){
                String cipherName13888 =  "DES";
				try{
					android.util.Log.d("cipherName-13888", javax.crypto.Cipher.getInstance(cipherName13888).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				x2 = s2.coord.x;
            }
            if(x2 < pxmin){
                String cipherName13889 =  "DES";
				try{
					android.util.Log.d("cipherName-13889", javax.crypto.Cipher.getInstance(cipherName13889).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				x2 = pxmin;
            }
            y2 = e.c - e.a * x2;
            if(((y1 > pymax) & (y2 > pymax)) | ((y1 < pymin) & (y2 < pymin))){
                String cipherName13890 =  "DES";
				try{
					android.util.Log.d("cipherName-13890", javax.crypto.Cipher.getInstance(cipherName13890).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return;
            }
            if(y1 > pymax){
                String cipherName13891 =  "DES";
				try{
					android.util.Log.d("cipherName-13891", javax.crypto.Cipher.getInstance(cipherName13891).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				y1 = pymax;
                x1 = (e.c - y1) / e.a;
            }
            if(y1 < pymin){
                String cipherName13892 =  "DES";
				try{
					android.util.Log.d("cipherName-13892", javax.crypto.Cipher.getInstance(cipherName13892).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				y1 = pymin;
                x1 = (e.c - y1) / e.a;
            }
            if(y2 > pymax){
                String cipherName13893 =  "DES";
				try{
					android.util.Log.d("cipherName-13893", javax.crypto.Cipher.getInstance(cipherName13893).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				y2 = pymax;
                x2 = (e.c - y2) / e.a;
            }
            if(y2 < pymin){
                String cipherName13894 =  "DES";
				try{
					android.util.Log.d("cipherName-13894", javax.crypto.Cipher.getInstance(cipherName13894).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				y2 = pymin;
                x2 = (e.c - y2) / e.a;
            }
        }

        GraphEdge newEdge = new GraphEdge();
        allEdges.add(newEdge);
        newEdge.x1 = x1;
        newEdge.y1 = y1;
        newEdge.x2 = x2;
        newEdge.y2 = y2;

        newEdge.site1 = e.reg[0].sitenbr;
        newEdge.site2 = e.reg[1].sitenbr;
    }

    private void endpoint(Edge e, int lr, Site s){
        String cipherName13895 =  "DES";
		try{
			android.util.Log.d("cipherName-13895", javax.crypto.Cipher.getInstance(cipherName13895).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		e.ep[lr] = s;
        if(e.ep[RE - lr] == null){
            String cipherName13896 =  "DES";
			try{
				android.util.Log.d("cipherName-13896", javax.crypto.Cipher.getInstance(cipherName13896).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }
        clipLine(e);
    }

    private boolean right(Halfedge el, Vec2 p){
        String cipherName13897 =  "DES";
		try{
			android.util.Log.d("cipherName-13897", javax.crypto.Cipher.getInstance(cipherName13897).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Edge e = el.ELedge;
        Site topsite = e.reg[1];
        boolean rightOf = p.x > topsite.coord.x;
        if(rightOf && el.ELpm == LE){
            String cipherName13898 =  "DES";
			try{
				android.util.Log.d("cipherName-13898", javax.crypto.Cipher.getInstance(cipherName13898).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return true;
        }
        if(!rightOf && el.ELpm == RE){
            String cipherName13899 =  "DES";
			try{
				android.util.Log.d("cipherName-13899", javax.crypto.Cipher.getInstance(cipherName13899).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        boolean above;
        if(e.a == 1.0){
            String cipherName13900 =  "DES";
			try{
				android.util.Log.d("cipherName-13900", javax.crypto.Cipher.getInstance(cipherName13900).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float dyp = p.y - topsite.coord.y;
            float dxp = p.x - topsite.coord.x;
            boolean fast = false;
            if((!rightOf & (e.b < 0.0)) | (rightOf & (e.b >= 0.0))){
                String cipherName13901 =  "DES";
				try{
					android.util.Log.d("cipherName-13901", javax.crypto.Cipher.getInstance(cipherName13901).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				above = dyp >= e.b * dxp;
                fast = above;
            }else{
                String cipherName13902 =  "DES";
				try{
					android.util.Log.d("cipherName-13902", javax.crypto.Cipher.getInstance(cipherName13902).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				above = p.x + p.y * e.b > e.c;
                if(e.b < 0.0){
                    String cipherName13903 =  "DES";
					try{
						android.util.Log.d("cipherName-13903", javax.crypto.Cipher.getInstance(cipherName13903).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					above = !above;
                }
                if(!above){
                    String cipherName13904 =  "DES";
					try{
						android.util.Log.d("cipherName-13904", javax.crypto.Cipher.getInstance(cipherName13904).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					fast = true;
                }
            }
            if(!fast){
                String cipherName13905 =  "DES";
				try{
					android.util.Log.d("cipherName-13905", javax.crypto.Cipher.getInstance(cipherName13905).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float dxs = topsite.coord.x - (e.reg[0]).coord.x;
                above = e.b * (dxp * dxp - dyp * dyp) < dxs * dyp
                * (1.0 + 2.0 * dxp / dxs + e.b * e.b);
                if(e.b < 0.0){
                    String cipherName13906 =  "DES";
					try{
						android.util.Log.d("cipherName-13906", javax.crypto.Cipher.getInstance(cipherName13906).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					above = !above;
                }
            }
        }else{
            String cipherName13907 =  "DES";
			try{
				android.util.Log.d("cipherName-13907", javax.crypto.Cipher.getInstance(cipherName13907).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float yl = e.c - e.a * p.x;
            float t1 = p.y - yl;
            float t2 = p.x - topsite.coord.x;
            float t3 = yl - topsite.coord.y;
            above = t1 * t1 > t2 * t2 + t3 * t3;
        }
        return ((el.ELpm == LE) == above);
    }

    private Site rightreg(Halfedge he){
        String cipherName13908 =  "DES";
		try{
			android.util.Log.d("cipherName-13908", javax.crypto.Cipher.getInstance(cipherName13908).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(he.ELedge == null) return bottomsite;

        return (he.ELpm == LE ? he.ELedge.reg[RE] : he.ELedge.reg[LE]);
    }

    private Site intersect(Halfedge el1, Halfedge el2){
        String cipherName13909 =  "DES";
		try{
			android.util.Log.d("cipherName-13909", javax.crypto.Cipher.getInstance(cipherName13909).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Edge e1, e2, e;
        Halfedge el;
        float d, xint, yint;
        boolean right_of_site;
        Site v;

        e1 = el1.ELedge;
        e2 = el2.ELedge;
        if(e1 == null || e2 == null){
            String cipherName13910 =  "DES";
			try{
				android.util.Log.d("cipherName-13910", javax.crypto.Cipher.getInstance(cipherName13910).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        if(e1.reg[1] == e2.reg[1]){
            String cipherName13911 =  "DES";
			try{
				android.util.Log.d("cipherName-13911", javax.crypto.Cipher.getInstance(cipherName13911).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        d = e1.a * e2.b - e1.b * e2.a;
        if(-1.0e-10 < d && d < 1.0e-10){
            String cipherName13912 =  "DES";
			try{
				android.util.Log.d("cipherName-13912", javax.crypto.Cipher.getInstance(cipherName13912).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        xint = (e1.c * e2.b - e2.c * e1.b) / d;
        yint = (e2.c * e1.a - e1.c * e2.a) / d;

        if((e1.reg[1].coord.y < e2.reg[1].coord.y)
        || (e1.reg[1].coord.y == e2.reg[1].coord.y && e1.reg[1].coord.x < e2.reg[1].coord.x)){
            String cipherName13913 =  "DES";
			try{
				android.util.Log.d("cipherName-13913", javax.crypto.Cipher.getInstance(cipherName13913).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			el = el1;
            e = e1;
        }else{
            String cipherName13914 =  "DES";
			try{
				android.util.Log.d("cipherName-13914", javax.crypto.Cipher.getInstance(cipherName13914).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			el = el2;
            e = e2;
        }

        right_of_site = xint >= e.reg[1].coord.x;
        if((right_of_site && el.ELpm == LE)
        || (!right_of_site && el.ELpm == RE)){
            String cipherName13915 =  "DES";
			try{
				android.util.Log.d("cipherName-13915", javax.crypto.Cipher.getInstance(cipherName13915).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        v = new Site();
        v.coord.x = xint;
        v.coord.y = yint;
        return (v);
    }

    static class Site{
        Vec2 coord = new Vec2();
        int sitenbr;
    }

    static class Halfedge{
        Halfedge ELleft, ELright;
        Edge ELedge;
        boolean deleted;
        int ELpm;
        Site vertex;
        float ystar;
        Halfedge PQnext;
    }

    public static class GraphEdge{
        public float x1, y1, x2, y2;

        public int site1, site2;
    }

    static class Edge{
        float a = 0, b = 0, c = 0;
        Site[] ep = new Site[2];
        Site[] reg = new Site[2];
        int edgenbr;
    }
}
