package kjr.sfx;

import org.lwjgl.openal.*;
import static org.lwjgl.openal.ALC10.*;

public class AudioDevice
{
    private String def_device_name = null;
    private long device;
    private long context;
    private int[] attributes = new int[1];

    private ALCCapabilities alcCapabilities;
    //private ALCapabilities  alCapabilities;

    public AudioDevice()
    {
        attributes[0] = 0;

        def_device_name = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        device = alcOpenDevice(def_device_name);

        context = alcCreateContext(device, attributes);

        alcMakeContextCurrent(context);

        alcCapabilities = ALC.createCapabilities(device);
        AL.createCapabilities(alcCapabilities);
    }

    public void delete()
    {
        alcDestroyContext(context);
        alcCloseDevice(device);
    }
}