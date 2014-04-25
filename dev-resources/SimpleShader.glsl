uniform vec4 ambient_color;
uniform float ambient_intensity;

void main(void)
{
  gl_FragColor = ambient_color * ambient_intensity/2.0;
}