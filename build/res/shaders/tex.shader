#shader vertex
#version 330 core

layout (location = 0) in float is_text;
layout (location = 1) in vec4 position;
layout (location = 2) in vec2 uv;
layout (location = 3) in float tid;
layout (location = 4) in vec4 color;

uniform mat4 pr_matrix;

out DATA
{
	float is_text;
	vec4 position;
	vec2 uv;
    float tid;
	vec4 color;
} vs_out;

void main()
{
	vs_out.is_text = float(is_text);
	gl_Position = pr_matrix * position;
	vs_out.position = position;
	vs_out.uv = uv;
    vs_out.tid = tid;
	vs_out.color = color;
}

#shader fragment
#version 330 core

layout (location = 0) out vec4 color;

uniform vec4 colour;
uniform vec2 light_pos;

in DATA
{
	float is_text;
	vec4 position;
	vec2 uv;
    float tid;
	vec4 color;
} fs_in;

uniform sampler2D textures[32];

void main()
{
    vec4 texColor = fs_in.color; 

    if(fs_in.tid > 0.0)
    {
        int tid = int(fs_in.tid - 0.5);
		texColor = fs_in.color * texture(textures[tid], fs_in.uv);

		if(float(fs_in.is_text) == 1.0f)
		{
			if(texColor.a > 0.0)
			{
				texColor = vec4(fs_in.color.r, fs_in.color.g, fs_in.color.b, texColor.a);
			}

			else
			{
				texColor = vec4(0, 0, 0, 0);
			}
		}
    }

    color = texColor;
}