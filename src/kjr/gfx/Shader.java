package kjr.gfx;

import java.io.IOException;
import kjr.util.ShaderReader;
import kjr.math.Mat4;
import kjr.math.Vec2;
import kjr.math.Vec3;
import kjr.math.Vec4;

import static org.lwjgl.opengl.GL30.glCreateProgram;
import static org.lwjgl.opengl.GL30.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL30.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL30.glCreateShader;
import static org.lwjgl.opengl.GL30.glShaderSource;
import static org.lwjgl.opengl.GL30.glCompileShader;
import static org.lwjgl.opengl.GL30.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL30.glGetShaderiv;
import static org.lwjgl.opengl.GL30.GL_FALSE;
import static org.lwjgl.opengl.GL30.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL30.glDeleteShader;
import static org.lwjgl.opengl.GL30.glDeleteProgram;
import static org.lwjgl.opengl.GL30.glLinkProgram;
import static org.lwjgl.opengl.GL30.glAttachShader;
import static org.lwjgl.opengl.GL30.glValidateProgram;
import static org.lwjgl.opengl.GL30.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL30.glGetProgramiv;
import static org.lwjgl.opengl.GL30.glUseProgram;
import static org.lwjgl.opengl.GL30.glGetUniformLocation;
import static org.lwjgl.opengl.GL30.glUniform1f;
import static org.lwjgl.opengl.GL30.glUniform1i;
import static org.lwjgl.opengl.GL30.glUniform1iv;
import static org.lwjgl.opengl.GL30.glUniform2f;
import static org.lwjgl.opengl.GL30.glUniform3f;
import static org.lwjgl.opengl.GL30.glUniform4f;
import static org.lwjgl.opengl.GL30.glUniformMatrix4fv;

/**
 * A program to run on the GPU. KJR shaders utilize only vertex and fragment shaders.
 * Shaders are an important aspect in drawing pixels onto the screen.
 * <p>
 * {@link Shader#KJR_STANDARD_SHADER KJR Standard Shader} is the default shader, and is
 * recommended. However, the shader requires OpenGL 3.4+ or else the shader will fail to compile.
 * <p>
 * User is able to make their own shader given they follow some rules:
 * <ul>
 * <li> Use of shader token: In order to have an entire shader in one file, rather than two,
 * KJR uses a shader token, which informs the {@link kjr.util.ShaderReader ShaderReader} that
 * a beyond that line, there is a shader to be read from.
 * "#shader" is the default shader token. Shader tokens are put on the same line before a shader name,
 * which is the next item on the list.
 * <li> Use of shader names: In order to specify which shader is being read, a shader name is put in place
 * to fulfill that role. A shader name is placed on the same life after a shader token. Default shader names
 * are "vertex" for the vertex shader and "fragment" for the fragment shader.
 * <li> Compiles as a shader: The contents of the shader must be able to actually be compiled by OpenGL.
 * </ul>
 * A pseudo example of a shader:
 * <pre>
 * #shader vertex
 * // some vertex shader code
 * #shader fragment
 * // some fragment shader code
 * </pre>
 * <p>
 * All non-public fields/methods are protected, meaning it's possible to inherit from
 * Shader.
 */
public class Shader
{
    // default names for shaders
    protected static final String def_pr_matrix_name = "pr_matrix";
    protected static final String def_textures_name = "textures";

    // constant variables used for 
    // reading a KJR standard shader
    public static final String KJR_SHADER_TOKEN = "#shader";
    public static final String KJR_VERTEX_NAME = "vertex";
    public static final String KJR_FRAGMENT_NAME = "fragment";

    public static final String KJR_STANDARD_SHADER =
    "#shader vertex\n" +
    "#version 330 core\n" +

    "layout (location = 0) in vec4 position;\n" +
    "layout (location = 1) in vec2 uv;\n" +
    "layout (location = 2) in float tid;\n" +
    "layout (location = 3) in vec4 color;\n" +

    "uniform mat4 pr_matrix;\n" +

    "out DATA\n" +
    "{\n" +
    "    vec4 position;\n" +
    "    vec2 uv;\n" +
    "    float tid;\n" +
    "    vec4 color;\n" +
    "} vs_out;\n" +

    "void main()\n" +
    "{\n" +
    "    gl_Position = pr_matrix * position;\n" +
    "    vs_out.position = position;\n" +
    "    vs_out.uv = uv;\n" +
    "    vs_out.tid = tid;\n" +
    "    vs_out.color = color;\n" +
    "}\n" +

    "#shader fragment\n" +
    "#version 330 core\n" +

    "layout (location = 0) out vec4 color;\n" +

    "uniform vec4 colour;\n" +

    "in DATA\n" +
    "{\n" +
    "    vec4 position;\n" +
    "    vec2 uv;\n" +
    "    float tid;\n" +
    "    vec4 color;\n" +
    "} fs_in;\n" +

    "uniform sampler2D textures[32];\n" +

    "void main()\n" +
    "{\n" +
    "    vec4 texColor = fs_in.color;\n" + 

    "    if(fs_in.tid > 0.0)\n" +
    "    {\n" +
    "        int tid = int(fs_in.tid - 0.5);\n" +
    "        texColor = fs_in.color * texture(textures[tid], fs_in.uv);\n" +
    "    }\n" +
    
    "    color = texColor;\n" +
    "};";

    public String pr_matrix = def_pr_matrix_name;
    public String textures = def_textures_name;

    /**
     * <pre>
     * Brief: OpenGL's program ID for the shader.
     * 
     * Layman:
     * 
     * OpenGL uses numbers to refer to stuff like textures and
     * programs. We need to keep track of this object's shader
     * by having the ID tied to the object.
     * </pre>
     */
    protected int id = 0;

    /**
     * <pre>
     * Brief: Creates an out of the box shader ready for use.
     * 
     * Layman:
     * 
     * Using createDefault(), there is no worry for binding
     * texture slots of setting the orthographics view, for
     * it is also handled.
     * </pre>
     * @return Shader - the KJR standard shader
     */
    public static Shader createDefault(int ortho_width, int ortho_height)
    {
        Shader shader = new Shader();

        int[] tex_ids = 
        {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31
        };

        shader.bind();
        shader.setUniform1iv(shader.textures, tex_ids);
        shader.setUniformMat4(shader.pr_matrix, Mat4.ortho(0, ortho_width, 0, ortho_height, -10, 10));
        shader.unbind();

        return shader;
    }

    /**
     * <pre>
     * Brief: Constructs a KJR standard shader.
     * 
     * Note: Read class documentation for more detail.
     * </pre>
     */
    public Shader()
    {
        String[] sources = ShaderReader.readString(KJR_STANDARD_SHADER, KJR_SHADER_TOKEN, KJR_VERTEX_NAME, KJR_FRAGMENT_NAME);
        id = load(sources);
    }

    /**
     * <pre>
     * Brief: Constructs a shader from either a file or a string.
     * 
     * Note: Read class documentation for more detail.
     * </pre>
     * @param shader - the string or file path
     * @param is_file - whether or not to read "shader" as a
     *                 string or as a file
     */
    public Shader(String shader, boolean is_file)
    {
        String[] sources = null;
        try
        {
            sources = (is_file) ? ShaderReader.readFile(shader, KJR_SHADER_TOKEN, KJR_VERTEX_NAME, KJR_FRAGMENT_NAME)
                                : ShaderReader.readString(shader, KJR_SHADER_TOKEN, KJR_VERTEX_NAME, KJR_FRAGMENT_NAME);
        }

        catch(IOException e)
        {
            throw new RuntimeException("Unable to read shader " + ((is_file) ? "file." : "string. Perhaps you're trying to read a file as a String."));
        }
        
        id = load(sources);
    }

    /**
     * <pre>
     * Brief: Loads the shader sources into OpenGL memory
     *        and returns the program ID (shader ID).
     * </pre>
     * @param sources - shader strings where [0] = vertex and [1] = fragment
     * @return - the program ID
     */
    protected int load(String[] sources)
    {
        // program is our shader
        // OpenGL stores our shader in it's own
        // memory, and this wrapper class just helps with
        // neccessary abstraction
        int program = glCreateProgram();

        // we allocate memory to OpenGL for our shaders
        // to access and compile to later on
        int vertex = glCreateShader(GL_VERTEX_SHADER);
        int fragment = glCreateShader(GL_FRAGMENT_SHADER);

        // int is a primitive type, which the value won't be
        // effected if we pass result in as a primitive
        // but an array is by reference which is the
        // reason why glGetShaderiv requires an array
        // and not just a normal value

        // we'll be using result over and over
        // to minimize allocations
        int[] result = new int[1];

        // now with our allocated memory
        // add our data to the vertex shader
        // 0 being the location in the array of
        // our vertex shader
        glShaderSource(vertex, sources[0]);
        // compile the shader then
        // get the status of compilation
        // GL_TRUE means a successful
        // compilation and GL_FALSE
        // means a failed compilation
        glCompileShader(vertex);
        glGetShaderiv(vertex, GL_COMPILE_STATUS, result);

        // OpenGL returns GL_FALSE (0) if compiling wasn't successful
        if(result[0] == GL_FALSE)
        {
            // we don't want failed shaders to run in our program
            // every shader should be ensured to be successful
            // so just throw an exception
            //throw new RuntimeException("Failed to compile vertex shader.");
            String error = glGetShaderInfoLog(vertex);
            System.out.println("Failed to compile vertex shader!\n" + "Detail: " + error);

            glDeleteShader(vertex);
            glDeleteShader(fragment);
            glDeleteProgram(program);
            return -1;
        }

        // now with our allocated memory
        // add our data to the fragment shader
        // 1 being the location in the array of
        // our fragment shader
        glShaderSource(fragment, sources[1]);
        // compile the shader then
        // get the status of compilation
        // GL_TRUE means a successful
        // compilation and GL_FALSE
        // means a failed compilation
        glCompileShader(fragment);
        glGetShaderiv(fragment, GL_COMPILE_STATUS, result);

        // OpenGL returns GL_FALSE (0) if compiling wasn't successful
        if(result[0] == GL_FALSE)
        {
            // we don't want failed shaders to run in our program
            // every shader should be ensured to be successful
            // so just throw an exception
            //throw new RuntimeException("Failed to link the shader program to OpenGL.");
            String error = glGetShaderInfoLog(fragment);
            System.out.println("Failed to compile fragment shader!\n" + "Detail: " + error);

            glDeleteShader(vertex);
            glDeleteShader(fragment);
            glDeleteProgram(program);
            return -1;
        }

        // we want our shader (program) to contain
        // both the vertex and fragment shaders
        // so we need them to be copied over to our
        // shader
        glAttachShader(program, vertex);
        glAttachShader(program, fragment);
        // now that the shaders are linked to our program (shader),
        // we need to link our program fully to OpenGL
        // then quickly validate it to ensure it can
        // run in our current OpenGL context
        glLinkProgram(program);
        glValidateProgram(program);

        // make result equal to the link status (GL_TRUE or GL_FALSE)
        glGetProgramiv(program, GL_LINK_STATUS, result);

        // OpenGL returns GL_FALSE (0) if compiling wasn't successful
        if(result[0] == GL_FALSE)
        {
            System.out.println("Failed to link shaders to program.");

            glDeleteShader(vertex);
            glDeleteShader(fragment);
            glDeleteProgram(program);
            return -1;
        }

        // since our shaders were copied and
        // we need to delete our old unneeded shaders
        // from OpenGL
        glDeleteShader(vertex);
        glDeleteShader(fragment);

        // now we return the program (shader id)
        // back to our constructors that run this
        // function, allowing us to bind our shader easily
        return program;
    }

    /**
     * <pre>
     * Brief: Binds this shader to OpenGL.
     * 
     * Layman:
     * 
     * OpenGL is a state machine, so binding
     * it allows us to use it.
     * </pre>
     */
    public void bind()
    {
        glUseProgram(id);
    }

    /**
     * <pre>
     * Brief: Unbinds this shader from OpenGL.
     * 
     * Layman:
     * 
     * OpenGL is a state machine, so unbinding
     * it allows will stop it from being used.
     * </pre>
     */
    public void unbind()
    {
        // binding the program id 0
        // is effectively saying don't
        // use a program
        glUseProgram(0);
    }

    /**
     * <pre>
     * Brief: Deletes all native memory.
     * 
     * Warning: Will cause memory leak if delete
     * is not called once GC collects the shader.
     * 
     * Layman:
     * 
     * There's memory that the java GC can't collect.
     * You need to explicitely call delete() once
     * done with the shader.
     * 
     * Non-Layman:
     * 
     * Since the shader is bound to native resources (C),
     * java GC does not know about it and therefor cannot collect it.
     * Much like you would explicitely call delete on a pointer object
     * once done with the resource.
     * </pre>
     */
    public void delete()
    {
        glDeleteProgram(id);
    }

    protected int getUniformLocation(String name)
    {
        return glGetUniformLocation(id, name);
    }

    public void setUniform1f(String name, float value)
    {
        glUniform1f(getUniformLocation(name), value);
    }

    public void setUniform1i(String name, int value)
    {
        glUniform1i(getUniformLocation(name), value);
    }

    public void setUniform1iv(String name, int[] values)
    {
        glUniform1iv(getUniformLocation(name), values);
    }

    public void setUniform2f(String name, Vec2 vector)
    {
        glUniform2f(getUniformLocation(name), vector.x, vector.y);
    }

    public void setUniform3f(String name, Vec3 vector)
    {
        glUniform3f(getUniformLocation(name), vector.x, vector.y, vector.z);
    }

    public void setUniform4f(String name, Vec4 vector)
    {
        glUniform4f(getUniformLocation(name), vector.x, vector.y, vector.z, vector.w);
    }

    public void setUniformMat4(String name, Mat4 matrix)
    {
        glUniformMatrix4fv(getUniformLocation(name), false, matrix.elements);
    }

    public void setShaderPRMatrix(Mat4 matrix)
    {
        glUniformMatrix4fv(getUniformLocation(pr_matrix), false, matrix.elements);
    }
}