package kjr.sfx;

import org.lwjgl.system.*;
import java.nio.*;
import java.util.ArrayList;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.*;

public class Audio
{
    private ArrayList<Integer> sources = new ArrayList<Integer>();
    private String file_path;
    private int source;
    private int id;
    private int channels;
    private int sample_rate;
    private int format = -1;
    private float gain = 1.0f;

    public Audio(String file_path)
    {
        this.file_path = file_path;
        load();
    }

    public Audio(String file_path, float volume)
    {
        this.file_path = file_path;
        this.gain = volume;
        load();
    }

    private void load()
    {
        ShortBuffer raw_audio;

        try(MemoryStack stack = stackPush())
        {
            IntBuffer channels_buffer = stack.mallocInt(1);
            IntBuffer sample_rate_buffer = stack.mallocInt(1);

            raw_audio = stb_vorbis_decode_filename(file_path, channels_buffer, sample_rate_buffer);

            channels = channels_buffer.get(0);
            sample_rate = sample_rate_buffer.get(0);
        }

        if(channels == 1)
        {
            format = AL_FORMAT_MONO16;
        }

        else if(channels == 2)
        {
            format = AL_FORMAT_STEREO16;
        }

        else
        {
            System.out.println("Could not find a valid audio format.");
        }

        id = alGenBuffers();
        source = alGenSources();

        alBufferData(id, format, raw_audio, sample_rate);
        alSourcef(source, AL_GAIN, gain);

        alSourcei(source, AL_BUFFER, id);
        free(raw_audio);
    }

    public void play()
    {
        if(sources.size() > 0)
        {
            int[] state = new int[1];
            for(int i = 0; i < sources.size(); ++i)
            {
                int source_val = sources.get(i).intValue();
                alGetSourcei(source_val, AL_SOURCE_STATE, state);
                if(state[0] == AL_STOPPED)
                {
                    alDeleteSources(source_val);
                }
            }

            int new_source = alGenSources();
            alSourcef(new_source, AL_GAIN, gain);
            alSourcei(new_source, AL_BUFFER, id);
            sources.add(new_source);
            alSourcePlay(new_source);
        }

        else
        {
            int new_source = alGenSources();
            alSourcef(new_source, AL_GAIN, gain);
            alSourcei(new_source, AL_BUFFER, id);
            sources.add(new_source);
            alSourcePlay(new_source);
        }
    }

    public void delete()
    {
        alDeleteBuffers(id);
        alDeleteSources(source);
    }
}
