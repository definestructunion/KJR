package kjr.gui;

import kjr.gfx.Box;

public enum Align
{
    TopRight,
    TopLeft,
    CenterRight,
    CenterLeft,
    Center,
    BottomRight,
    BottomLeft;

    public Box toPosition(Box boxToAlignTo, Box boxBeingChanged)
    {
        return align(boxToAlignTo, boxBeingChanged, 0, 0);
    }

    public Box toPosition(Box boxToAlignTo, Box boxBeingChanged, int xOffset, int yOffset)
    {
        return align(boxToAlignTo, boxBeingChanged, xOffset, yOffset);
    }

    private Box align(Box boxToAlignTo, Box boxBeingChanged, int xOffset, int yOffset)
    {
        switch(this)
        {
            case TopRight:
                boxBeingChanged.x = (boxToAlignTo.x + boxToAlignTo.width) - boxBeingChanged.width + xOffset;
                boxBeingChanged.y = boxToAlignTo.y + yOffset;
                break;

            case TopLeft:
                boxBeingChanged.x = boxToAlignTo.x + xOffset;
                boxBeingChanged.y = boxToAlignTo.y + yOffset;
                break;

            case CenterRight:
                boxBeingChanged.x = (boxToAlignTo.x + boxToAlignTo.width) - boxBeingChanged.width - xOffset;
                boxBeingChanged.y = (boxToAlignTo.y + ((boxToAlignTo.height + 1) / 2)) - boxBeingChanged.height + yOffset;
                break;

            case CenterLeft:
                boxBeingChanged.x = boxToAlignTo.x + xOffset;
                boxBeingChanged.y = (boxToAlignTo.y + ((boxToAlignTo.height + 1) / 2)) - boxBeingChanged.height + yOffset;
                break;

            case Center:
                boxBeingChanged.x = boxToAlignTo.x + ((boxToAlignTo.width) - (boxBeingChanged.width) / 2) + xOffset;
                boxBeingChanged.y = (boxToAlignTo.y + ((boxToAlignTo.height + 1) / 2)) - boxBeingChanged.height + yOffset;
                break;

            case BottomRight:
                boxBeingChanged.x = (boxToAlignTo.x + boxToAlignTo.width) - boxBeingChanged.width + xOffset;
                boxBeingChanged.y = (boxToAlignTo.y + boxToAlignTo.height) - boxBeingChanged.height - yOffset;
                break;

            case BottomLeft:
                boxBeingChanged.x = boxToAlignTo.x + xOffset;
                boxBeingChanged.y = (boxToAlignTo.y + boxToAlignTo.height) - boxBeingChanged.height - yOffset;
                break;
        }

        return boxBeingChanged;
    }
}