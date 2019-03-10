package com.example.myoengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

import com.example.RWShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class RWRender implements GLSurfaceView.Renderer {

    private Context context;

//    private final float[] vertexData ={
//            -1f, 0f,
//            0f, 1f,
//            1f, 0f
//    };

    private final float[] textPoint = {
            -1f, -1f,
            1f, -1f,
            -1f, 1f,
            1f, 1f
    };

    private final float[] texturePoint = {
            1f,0f,
            0f,0f,
            1f, 1f,
            0f, 1f
    };

    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private int program;
    private int avPosition;
    private int afPosition;
    private int afColor;
    private int textureid;

    public RWRender(Context context)
    {
        this.context = context;
        vertexBuffer = ByteBuffer.allocateDirect(textPoint.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(textPoint);
        vertexBuffer.position(0);

        textureBuffer = ByteBuffer.allocateDirect(texturePoint.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(texturePoint);
        textureBuffer.position(0);
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);
        String vertexSource = RWShaderUtil.getSource(context, R.raw.vertex_shader);
        String fragmentSource = RWShaderUtil.getSource(context, R.raw.fragment_shader);
        program = RWShaderUtil.createProgram(vertexSource, fragmentSource);
        if(program > 0)
        {
            avPosition = GLES20.glGetAttribLocation(program, "av_Position");
            afPosition = GLES20.glGetAttribLocation(program, "af_Position");
//            afColor = GLES20.glGetUniformLocation(program, "af_Color");

            int textures[] = new int[1];
            GLES20.glGenTextures(1,textures,0);
            if (textures[0] == 0){
                return;
            }
            textureid = textures[0];
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureid);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.og);
            if(bitmap == null)
            {
                return;
            }
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmap,0);
            bitmap.recycle();
            bitmap = null;
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
//        GLES20.glClearColor(1.0f,0.0f,0.0f,1.0f);
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        GLES20.glUseProgram(program);
//        GLES20.glUniform4f(afColor, 1f, 0f, 0f, 1f);
//        GLES20.glEnableVertexAttribArray(avPosition);
//        GLES20.glVertexAttribPointer(avPosition, 2, GLES20.GL_FLOAT, false, 8, vertexBuffer);
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glEnableVertexAttribArray(avPosition);
        GLES20.glVertexAttribPointer(avPosition, 2, GLES20.GL_FLOAT, false, 8, vertexBuffer);

        GLES20.glEnableVertexAttribArray(afPosition);
        GLES20.glVertexAttribPointer(afPosition, 2, GLES20.GL_FLOAT, false, 8, textureBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}
