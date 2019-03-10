package com.example;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RWShaderUtil {

    public static String getSource(Context context, int rawId) {

        InputStream inputStream = context.getResources().openRawResource(rawId);
        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder builder = new StringBuilder();
        try {
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static int loadShader(String source, int type) {

        int shader = GLES20.glCreateShader(type);
        if (shader != 0) {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            int params[] = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, params, 0);
            if (params[0] != GLES20.GL_TRUE) {
                Log.d("rw", "shader compile error");
                GLES20.glDeleteShader(shader);
                shader = 0;
                return shader;
            }
        }
        return shader;
    }

    public static int createProgram(String vertexSource, String fragmentSource){

        int vertexShader = loadShader(vertexSource, GLES20.GL_VERTEX_SHADER);
        if (vertexShader == 0){
            return 0;
        }
        int fragmentShader = loadShader(fragmentSource, GLES20.GL_FRAGMENT_SHADER);
        if (fragmentShader == 0){
            return 0;
        }
        int program = GLES20.glCreateProgram();
        if (program != 0){
            GLES20.glAttachShader(program,vertexShader);
            GLES20.glAttachShader(program,fragmentShader);
            GLES20.glLinkProgram(program);
            int params[] = new int[1];
            GLES20.glGetProgramiv(program,GLES20.GL_LINK_STATUS,params,0);
            if (params[0] != GLES20.GL_TRUE){
                Log.d("rw", "link program error");
                GLES20.glDeleteProgram(program);
                return 0;
            }
        }
        return program;
    }
}
