package mindustry.graphics.g3d;

import arc.math.*;
import arc.math.geom.*;

//TODO clean this up somehow
public class PlanetGrid{
    private static final PlanetGrid[] cache = new PlanetGrid[10];

    private static final float x = -0.525731112119133606f;
    private static final float z = -0.850650808352039932f;

    private static final Vec3[] iTiles = {
    new Vec3(-x, 0, z), new Vec3(x, 0, z), new Vec3(-x, 0, -z), new Vec3(x, 0, -z),
    new Vec3(0, z, x), new Vec3(0, z, -x), new Vec3(0, -z, x), new Vec3(0, -z, -x),
    new Vec3(z, x, 0), new Vec3(-z, x, 0), new Vec3(z, -x, 0), new Vec3(-z, -x, 0)
    };

    private static final int[][] iTilesP = {
    {9, 4, 1, 6, 11}, {4, 8, 10, 6, 0}, {11, 7, 3, 5, 9}, {2, 7, 10, 8, 5},
    {9, 5, 8, 1, 0}, {2, 3, 8, 4, 9}, {0, 1, 10, 7, 11}, {11, 6, 10, 3, 2},
    {5, 3, 10, 1, 4}, {2, 5, 4, 0, 11}, {3, 7, 6, 1, 8}, {7, 2, 9, 0, 6}
    };

    public int size;
    public Ptile[] tiles;
    public Corner[] corners;
    public Edge[] edges;

    //this is protected so if you want to make strange grids you should know what you're doing.
    protected PlanetGrid(int size){
        String cipherName14313 =  "DES";
		try{
			android.util.Log.d("cipherName-14313", javax.crypto.Cipher.getInstance(cipherName14313).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.size = size;

        tiles = new Ptile[tileCount(size)];
        for(int i = 0; i < tiles.length; i++){
            String cipherName14314 =  "DES";
			try{
				android.util.Log.d("cipherName-14314", javax.crypto.Cipher.getInstance(cipherName14314).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tiles[i] = new Ptile(i, i < 12 ? 5 : 6);
        }

        corners = new Corner[cornerCount(size)];
        for(int i = 0; i < corners.length; i++){
            String cipherName14315 =  "DES";
			try{
				android.util.Log.d("cipherName-14315", javax.crypto.Cipher.getInstance(cipherName14315).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			corners[i] = new Corner(i);
        }

        edges = new Edge[edgeCount(size)];
        for(int i = 0; i < edges.length; i++){
            String cipherName14316 =  "DES";
			try{
				android.util.Log.d("cipherName-14316", javax.crypto.Cipher.getInstance(cipherName14316).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			edges[i] = new Edge(i);
        }
    }

    public static PlanetGrid create(int size){
        String cipherName14317 =  "DES";
		try{
			android.util.Log.d("cipherName-14317", javax.crypto.Cipher.getInstance(cipherName14317).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//cache grids between calls, since only ~5 different grids total are needed
        if(size < cache.length && cache[size] != null){
            String cipherName14318 =  "DES";
			try{
				android.util.Log.d("cipherName-14318", javax.crypto.Cipher.getInstance(cipherName14318).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return cache[size];
        }

        PlanetGrid result;
        if(size == 0){
            String cipherName14319 =  "DES";
			try{
				android.util.Log.d("cipherName-14319", javax.crypto.Cipher.getInstance(cipherName14319).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			result = initialGrid();
        }else{
            String cipherName14320 =  "DES";
			try{
				android.util.Log.d("cipherName-14320", javax.crypto.Cipher.getInstance(cipherName14320).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			result = subdividedGrid(create(size - 1));
        }

        //store grid in cache
        if(size < cache.length){
            String cipherName14321 =  "DES";
			try{
				android.util.Log.d("cipherName-14321", javax.crypto.Cipher.getInstance(cipherName14321).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cache[size] = result;
        }

        return result;
    }

    public static PlanetGrid initialGrid(){
        String cipherName14322 =  "DES";
		try{
			android.util.Log.d("cipherName-14322", javax.crypto.Cipher.getInstance(cipherName14322).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		PlanetGrid grid = new PlanetGrid(0);

        for(Ptile t : grid.tiles){
            String cipherName14323 =  "DES";
			try{
				android.util.Log.d("cipherName-14323", javax.crypto.Cipher.getInstance(cipherName14323).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			t.v = iTiles[t.id];
            for(int k = 0; k < 5; k++){
                String cipherName14324 =  "DES";
				try{
					android.util.Log.d("cipherName-14324", javax.crypto.Cipher.getInstance(cipherName14324).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				t.tiles[k] = grid.tiles[iTilesP[t.id][k]];
            }
        }
        for(int i = 0; i < 5; i++){
            String cipherName14325 =  "DES";
			try{
				android.util.Log.d("cipherName-14325", javax.crypto.Cipher.getInstance(cipherName14325).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			addCorner(i, grid, 0, iTilesP[0][(i + 4) % 5], iTilesP[0][i]);
        }
        for(int i = 0; i < 5; i++){
            String cipherName14326 =  "DES";
			try{
				android.util.Log.d("cipherName-14326", javax.crypto.Cipher.getInstance(cipherName14326).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			addCorner(i + 5, grid, 3, iTilesP[3][(i + 4) % 5], iTilesP[3][i]);
        }
        addCorner(10, grid, 10, 1, 8);
        addCorner(11, grid, 1, 10, 6);
        addCorner(12, grid, 6, 10, 7);
        addCorner(13, grid, 6, 7, 11);
        addCorner(14, grid, 11, 7, 2);
        addCorner(15, grid, 11, 2, 9);
        addCorner(16, grid, 9, 2, 5);
        addCorner(17, grid, 9, 5, 4);
        addCorner(18, grid, 4, 5, 8);
        addCorner(19, grid, 4, 8, 1);

        //add corners to corners
        for(Corner c : grid.corners){
            String cipherName14327 =  "DES";
			try{
				android.util.Log.d("cipherName-14327", javax.crypto.Cipher.getInstance(cipherName14327).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int k = 0; k < 3; k++){
                String cipherName14328 =  "DES";
				try{
					android.util.Log.d("cipherName-14328", javax.crypto.Cipher.getInstance(cipherName14328).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				c.corners[k] = c.tiles[k].corners[(pos(c.tiles[k], c) + 1) % 5];
            }
        }
        //new edges
        int nextEdge = 0;
        for(Ptile t : grid.tiles){
            String cipherName14329 =  "DES";
			try{
				android.util.Log.d("cipherName-14329", javax.crypto.Cipher.getInstance(cipherName14329).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int k = 0; k < 5; k++){
                String cipherName14330 =  "DES";
				try{
					android.util.Log.d("cipherName-14330", javax.crypto.Cipher.getInstance(cipherName14330).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(t.edges[k] == null){
                    String cipherName14331 =  "DES";
					try{
						android.util.Log.d("cipherName-14331", javax.crypto.Cipher.getInstance(cipherName14331).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					addEdge(nextEdge++, grid, t.id, iTilesP[t.id][k]);
                }
            }
        }
        return grid;
    }

    public static PlanetGrid subdividedGrid(PlanetGrid prev){
        String cipherName14332 =  "DES";
		try{
			android.util.Log.d("cipherName-14332", javax.crypto.Cipher.getInstance(cipherName14332).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		PlanetGrid grid = new PlanetGrid(prev.size + 1);

        int prevTiles = prev.tiles.length;
        int prevCorners = prev.corners.length;

        //old tiles
        for(int i = 0; i < prevTiles; i++){
            String cipherName14333 =  "DES";
			try{
				android.util.Log.d("cipherName-14333", javax.crypto.Cipher.getInstance(cipherName14333).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			grid.tiles[i].v = prev.tiles[i].v;
            for(int k = 0; k < grid.tiles[i].edgeCount; k++){

                String cipherName14334 =  "DES";
				try{
					android.util.Log.d("cipherName-14334", javax.crypto.Cipher.getInstance(cipherName14334).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				grid.tiles[i].tiles[k] = grid.tiles[prev.tiles[i].corners[k].id + prevTiles];
            }
        }
        //old corners become tiles
        for(int i = 0; i < prevCorners; i++){
            String cipherName14335 =  "DES";
			try{
				android.util.Log.d("cipherName-14335", javax.crypto.Cipher.getInstance(cipherName14335).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			grid.tiles[i + prevTiles].v = prev.corners[i].v;
            for(int k = 0; k < 3; k++){
                String cipherName14336 =  "DES";
				try{
					android.util.Log.d("cipherName-14336", javax.crypto.Cipher.getInstance(cipherName14336).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				grid.tiles[i + prevTiles].tiles[2 * k] = grid.tiles[prev.corners[i].corners[k].id + prevTiles];
                grid.tiles[i + prevTiles].tiles[2 * k + 1] = grid.tiles[prev.corners[i].tiles[k].id];
            }
        }
        //new corners
        int nextCorner = 0;
        for(Ptile n : prev.tiles){
            String cipherName14337 =  "DES";
			try{
				android.util.Log.d("cipherName-14337", javax.crypto.Cipher.getInstance(cipherName14337).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Ptile t = grid.tiles[n.id];
            for(int k = 0; k < t.edgeCount; k++){
                String cipherName14338 =  "DES";
				try{
					android.util.Log.d("cipherName-14338", javax.crypto.Cipher.getInstance(cipherName14338).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				addCorner(nextCorner, grid, t.id, t.tiles[(k + t.edgeCount - 1) % t.edgeCount].id, t.tiles[k].id);
                nextCorner++;
            }
        }
        //connect corners
        for(Corner c : grid.corners){
            String cipherName14339 =  "DES";
			try{
				android.util.Log.d("cipherName-14339", javax.crypto.Cipher.getInstance(cipherName14339).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int k = 0; k < 3; k++){
                String cipherName14340 =  "DES";
				try{
					android.util.Log.d("cipherName-14340", javax.crypto.Cipher.getInstance(cipherName14340).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				c.corners[k] = c.tiles[k].corners[(pos(c.tiles[k], c) + 1) % (c.tiles[k].edgeCount)];
            }
        }
        //new edges
        int nextEdge = 0;
        for(Ptile t : grid.tiles){
            String cipherName14341 =  "DES";
			try{
				android.util.Log.d("cipherName-14341", javax.crypto.Cipher.getInstance(cipherName14341).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int k = 0; k < t.edgeCount; k++){
                String cipherName14342 =  "DES";
				try{
					android.util.Log.d("cipherName-14342", javax.crypto.Cipher.getInstance(cipherName14342).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(t.edges[k] == null){
                    String cipherName14343 =  "DES";
					try{
						android.util.Log.d("cipherName-14343", javax.crypto.Cipher.getInstance(cipherName14343).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					addEdge(nextEdge, grid, t.id, t.tiles[k].id);
                    nextEdge++;
                }
            }
        }

        return grid;
    }

    static void addCorner(int id, PlanetGrid grid, int t1, int t2, int t3){
        String cipherName14344 =  "DES";
		try{
			android.util.Log.d("cipherName-14344", javax.crypto.Cipher.getInstance(cipherName14344).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Corner c = grid.corners[id];
        Ptile[] t = {grid.tiles[t1], grid.tiles[t2], grid.tiles[t3]};
        c.v.set(t[0].v).add(t[1].v).add(t[2].v).nor();
        for(int i = 0; i < 3; i++){
            String cipherName14345 =  "DES";
			try{
				android.util.Log.d("cipherName-14345", javax.crypto.Cipher.getInstance(cipherName14345).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			t[i].corners[pos(t[i], t[(i + 2) % 3])] = c;
            c.tiles[i] = t[i];
        }
    }

    static void addEdge(int id, PlanetGrid grid, int t1, int t2){
        String cipherName14346 =  "DES";
		try{
			android.util.Log.d("cipherName-14346", javax.crypto.Cipher.getInstance(cipherName14346).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Edge e = grid.edges[id];
        Ptile[] t = {grid.tiles[t1], grid.tiles[t2]};
        Corner[] c = {
        grid.corners[t[0].corners[pos(t[0], t[1])].id],
        grid.corners[t[0].corners[(pos(t[0], t[1]) + 1) % t[0].edgeCount].id]};
        for(int i = 0; i < 2; i++){
            String cipherName14347 =  "DES";
			try{
				android.util.Log.d("cipherName-14347", javax.crypto.Cipher.getInstance(cipherName14347).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			t[i].edges[pos(t[i], t[(i + 1) % 2])] = e;
            e.tiles[i] = t[i];
            c[i].edges[pos(c[i], c[(i + 1) % 2])] = e;
            e.corners[i] = c[i];
        }
    }

    static int pos(Ptile t, Ptile n){
        String cipherName14348 =  "DES";
		try{
			android.util.Log.d("cipherName-14348", javax.crypto.Cipher.getInstance(cipherName14348).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int i = 0; i < t.edgeCount; i++){
            String cipherName14349 =  "DES";
			try{
				android.util.Log.d("cipherName-14349", javax.crypto.Cipher.getInstance(cipherName14349).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(t.tiles[i] == n) return i;
        }
        return -1;
    }

    static int pos(Ptile t, Corner c){
        String cipherName14350 =  "DES";
		try{
			android.util.Log.d("cipherName-14350", javax.crypto.Cipher.getInstance(cipherName14350).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int i = 0; i < t.edgeCount; i++){
            String cipherName14351 =  "DES";
			try{
				android.util.Log.d("cipherName-14351", javax.crypto.Cipher.getInstance(cipherName14351).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(t.corners[i] == c) return i;
        }
        return -1;
    }

    static int pos(Corner c, Corner n){
        String cipherName14352 =  "DES";
		try{
			android.util.Log.d("cipherName-14352", javax.crypto.Cipher.getInstance(cipherName14352).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int i = 0; i < 3; i++){
            String cipherName14353 =  "DES";
			try{
				android.util.Log.d("cipherName-14353", javax.crypto.Cipher.getInstance(cipherName14353).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(c.corners[i] == n) return i;
        }
        return -1;
    }

    static int tileCount(int size){
        String cipherName14354 =  "DES";
		try{
			android.util.Log.d("cipherName-14354", javax.crypto.Cipher.getInstance(cipherName14354).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return 10 * Mathf.pow(3, size) + 2;
    }

    static int cornerCount(int size){
        String cipherName14355 =  "DES";
		try{
			android.util.Log.d("cipherName-14355", javax.crypto.Cipher.getInstance(cipherName14355).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return 20 * Mathf.pow(3, size);
    }

    static int edgeCount(int size){
        String cipherName14356 =  "DES";
		try{
			android.util.Log.d("cipherName-14356", javax.crypto.Cipher.getInstance(cipherName14356).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return 30 * Mathf.pow(3, size);
    }

    public static class Ptile{
        public static final Ptile empty = new Ptile(0, 0);

        public int id;
        public int edgeCount;

        public Ptile[] tiles;
        public Corner[] corners;
        public Edge[] edges;

        public Vec3 v = new Vec3();

        public Ptile(int id, int edgeCount){
            String cipherName14357 =  "DES";
			try{
				android.util.Log.d("cipherName-14357", javax.crypto.Cipher.getInstance(cipherName14357).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.id = id;
            this.edgeCount = edgeCount;

            tiles = new Ptile[edgeCount];
            corners = new Corner[edgeCount];
            edges = new Edge[edgeCount];
        }
    }

    public static class Corner{
        public int id;
        public Ptile[] tiles = new Ptile[3];
        public Corner[] corners = new Corner[3];
        public Edge[] edges = new Edge[3];
        public Vec3 v = new Vec3();

        public Corner(int id){
            String cipherName14358 =  "DES";
			try{
				android.util.Log.d("cipherName-14358", javax.crypto.Cipher.getInstance(cipherName14358).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.id = id;
        }
    }

    public static class Edge{
        public int id;
        public Ptile[] tiles = new Ptile[2];
        public Corner[] corners = new Corner[2];

        public Edge(int id){
            String cipherName14359 =  "DES";
			try{
				android.util.Log.d("cipherName-14359", javax.crypto.Cipher.getInstance(cipherName14359).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.id = id;
        }
    }
}
