package com.example.myoengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class RWGlsurfaceview extends GLSurfaceView {

    public RWGlsurfaceview(Context context) {
        this(context,null);
    }

    public RWGlsurfaceview(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        setRenderer(new RWRender(context));
    }


}
