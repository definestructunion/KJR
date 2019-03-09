package kjr.util;

import kjr.gfx.Shader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Reads shaders and splits them into two separate parts. ShaderReader is designed
 * to read fragment and vertex shaders, but can be used to read into different types
 * of shaders. Note that {@link kjr.gfx.Shader Shader} only supports vertex and fragment
 * shaders.
 * <p>
 * Split shader files are returned as a {@link String} array of length 2.
 * <p>
 * An {@link IllegalStateException} will be thrown if ShaderReader is not
 * able to find any of the shader tokens in the file or {@link String} presented.
 * An example of this problem:
 * <ul>
 * <li>File: #shader vertex
 * <li>Parameter: #sahder vertexx
 * </ul>
 */
public final class ShaderReader
{
    private static final int SHADER_NONE = -1;
    private static final int SHADER_VERTEX = 0;
    private static final int SHADER_FRAGMENT = 1;

    /**
     * Reads the KJR standard shader. KJR by default comes with
     * a premade {@link kjr.gfx.Shader#KJR_STANDARD_SHADER shader}.
     * @return vertex shader at [0] and fragment shader at [1]
     */
    public static String[] readString()
    {
        return readString(Shader.KJR_STANDARD_SHADER, Shader.KJR_SHADER_TOKEN, Shader.KJR_VERTEX_NAME, Shader.KJR_FRAGMENT_NAME);
    }

    /**
     * Reads a shader and returns an array where [0] = vertex and [1] = fragment.
     * <p>
     * Some things worth noting:
     * <ul>
     * <li>Reads a string value that is not interpreted as a file
     * <li>Will throw a RuntimeException if a type_token was 
     * identified but both the vertex_name and the fragment_name 
     * were incorrectly passed through into this function
     * </ul>
     * @param shader the shader source containing the
     *               vertex and fragment shaders
     * @param type_token the identifier which informs the readString
     *                   function that it's reading into a new shader
     *                   type
     * @param vertex_name the name of the vertex shader
     *                    used on the same line
     *                    as type_token
     * @param fragment_name the name of the fragment shader
     *                      used on the same line
     *                      as type_token
     * @return the vertex shader at index 0 and the fragment shader at index 1
     */
    public static String[] readString(String shader, String type_token, String vertex_name, String fragment_name)
    {
        // what index the line will be fed into
        // SHADER_NONE will cause a runtime exception
        // which is what we want
        int shader_index = SHADER_NONE;

        // the stream we will we reading from
        Scanner stream = new Scanner(shader);
        // the shader sources
        // slot 0 will be the vertex
        // slot 1 will be the fragment
        // slot -1 will cause an exception
        String[] sources = new String[2];
        // default both the sources
        // I've just started using Java
        // and I'm not sure if strings
        // are set to null by default
        // so the operator + will
        // cause an exception
        sources[0] = "";
        sources[1] = "";

        // the current line being read
        String line = "";

        // looping until there's no next line
        // to loop into
        while(stream.hasNextLine())
        {
            // access the line and also advance
            // the lines to go to the next
            line = stream.nextLine();

            // when it reaches a shader token
            // by default (#shader)
            // it indicates that it's reading
            // into a new shader type

            // so if the line contains the type
            // token, it checks which shader name
            // is contained in the line

            // if no valid name is given
            // it throws a runtime exception
            // as it makes no sense writing
            // to a non supported shader
            if(line.contains(type_token))
            {
                if(line.contains(vertex_name))
                {
                    // it's now going to write into a vertex shader
                    shader_index = SHADER_VERTEX;
                }

                else if(line.contains(fragment_name))
                {
                    // it's now going to write into a fragment shader
                    shader_index = SHADER_FRAGMENT;
                }

                // throw an error if neither one suits the following
                // conditions
                else
                {
                    stream.close();
                    throw new RuntimeException("Shader token line doesn't contain the provided vertex and fragment shader names");
                }
            }

            // this syntax allows us to write a vertex and fragment shader
            // together in one file, however, it's not standard syntax
            // and if these tokens are written into the String array
            // and the Strings are compiled
            // it will fail as OpenGL does not allow the tokens
            // to be in the file

            // thus, we only write into the file if there is no
            // token to check for

            // for example:
            // #shader vertex
            // SHADER CODE
            // the only part that would be written into
            // the array would be SHADER CODE
            else
            {
                if(shader_index == -1)
                {
                    stream.close();
                    throw new IllegalStateException("Unable to find a valid shader token.");
                }

                sources[shader_index] += (line + '\n');
            }
        }

        stream.close();
        return sources;
    }

    /**
     * Reads a shader file and returns an array where [0] = vertex and [1] = fragment.
     * <p>
     * Will throw a RuntimeException if a type_token was 
     * identified but both the vertex_name and the fragment_name 
     * were incorrectly passed through into this function
     * @param file_path the path to the shader source
     * @param type_token the identifier which informs the readString
     *                   function that it's reading into a new shader
     *                   type
     * @param vertex_name the name of the vertex shader
     *                    used on the same line
     *                    as type_token
     * @param fragment_name the name of the fragment shader
     *                      used on the same line
     *                      as type_token
     * @return the vertex shader at index 0 and the fragment shader at index 1
     */
    public static String[] readFile(String file_path, String type_token, String vertex_name, String fragment_name) throws IOException
    {
        String shader = new String(Files.readAllBytes(Paths.get(file_path)));
        return readString(shader, type_token, vertex_name, fragment_name);
    }
}
