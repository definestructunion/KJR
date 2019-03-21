package kjr.gui.tile;

import kjr.gfx.SpriteBatch;

import java.util.ArrayList;

public final class XGUI
{
    private static ArrayList<XConsole> consoles;

    public static void add(XConsole console)
    {
        consoles.add(console);
    }

    public static void remove(XConsole console)
    {
        consoles.remove(console);
    }

    public static ArrayList<XConsole> getList()
    {
        return consoles;
    }

    private static boolean switchBackToLayered = true;

    public static void renderTypeSwitchBackTo(boolean backToLayered) { switchBackToLayered = backToLayered; }

    public static void draw(SpriteBatch renderer)
    {
        renderer.begin();
        renderer.setSortModeDeferred();
        for(XConsole console : consoles)
            console.draw(renderer);
        renderer.end();
        renderer.flush();
        if(switchBackToLayered)
            renderer.setSortModeLayered();
    }

    public static void update()
    {
        if(consoles.size() > 0)
            consoles.get(consoles.size() - 1).update();
    }

    public static void updateAll()
    {
        for(int i = consoles.size() - 1; i >= 0; --i)
            consoles.get(i).update();
    }

    static
    {
        consoles = new ArrayList<>();
    }
}
