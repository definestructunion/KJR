package kjr.gfx;

/**
 * <pre>
 * Brief: Data for tiles when rendering.
 * 
 * Note: coord_start_x/y acts as the origin
 *       of the draw calls, where if
 *       coord_start_x/y = 50, then calling
 *       batch.draw(var, 50, 50) is the same as
 *       batch.draw(var, 0, 0) if coord_start_x/y
 *       was the default (0)
 * 
 * Layman:
 * 
 * Since KJR is mainly a tile based rendering
 * framework, TileData makes it much easier
 * to render as a tilemap
 * 
 * Contains:
 * - tile_size as int - the size of every tile
 * - offset_x as int - the offset of x from (0, 0)
 * - offset_y as int - the offset of y from (0, 0)
 * - coord_start_x as int - the x = 0 for drawing
 * - coord_start_y as int - the y = 0 for drawing
 * </pre>
 */
public class TileData
{
    /**
     * <pre>
     * Brief: Offset from the screen (0, 0)
     * 
     * Note: For more info, read the TileData javadoc
     * </pre>
     */
    public int offset_x, offset_y;
    public int coord_start_x, coord_start_y;
    public int tile_size;

    private TileData(int tile_size, int offset_x, int offset_y, int coord_start_x, int coord_start_y)
    {
        this.offset_x = offset_x;
        this.offset_y = offset_y;
        this.coord_start_x = coord_start_x;
        this.coord_start_y = coord_start_y;
        this.tile_size = tile_size;
    }

    public static TileData create(int tile_size, int offset_x, int offset_y, int coord_start_x, int coord_start_y)
    {
        return new TileData(tile_size, offset_x, offset_y, coord_start_x, coord_start_y);
    }

    public static TileData createOffset(int tile_size, int offset_x, int offset_y)
    {
        return new TileData(tile_size, offset_x, offset_y, 0, 0);
    }

    public static TileData createCoordOffset(int tile_size, int coord_start_x, int coord_start_y)
    {
        return new TileData(tile_size, 0, 0, coord_start_x, coord_start_y);
    }
}