//precision mediump float;
//uniform vec4 af_Color;
//void main(){
//    gl_FragColor = af_Color;
//}
precision mediump float;
varying vec2 v_texPo;
uniform sampler2D sTexture;
void main() {
    gl_FragColor=texture2D(sTexture, v_texPo);
}