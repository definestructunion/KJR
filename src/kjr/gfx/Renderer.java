package kjr.gfx;

import java.util.ArrayList;

public abstract class Renderer
{
    public int tile_size = 0;

    protected Renderer(int tile_size)
    {
        this.tile_size = tile_size;
        return;
    }

    public void delete()
    {
        
    }

    // for batch rendering types of renderers
    protected float getSlot(ArrayList<Float> slots, float texture_id)
    {
        // if the texture ID is 0, then it's textureless
		// so we can just end it here
		if (texture_id == 0.0f)
			return 0.0f;

        float[] slot = new float[1];
        slot[0] = 0.0f;
		// if the slot wasn't found
		// as well, if getFound returns true
		// it sets our slot to the correct texture slot
		// anyways
        if (!getFound(slots, texture_id, slot))
        {
			// push back our texture ID
			slots.add(texture_id);
			// make the slot equal to the back of the vector
			slot[0] = (float)(slots.size());
		}
		return slot[0];
    }

    protected boolean getFound(ArrayList<Float> slots, float texture_id, float[] slot)
    {
        for (int i = 0; i < slots.size(); ++i)
        {
			// if the texture_ids are equal to eachother
            if (slots.get(i) == texture_id)
            {
				// set the slot to i + 1
				slot[0] = (float)(i + 1);
				return true;
            }
        }

		return false;
    }
}
