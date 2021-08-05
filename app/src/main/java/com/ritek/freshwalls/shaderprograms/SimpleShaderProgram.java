package com.ritek.freshwalls.shaderprograms;

import android.content.Context;
import android.opengl.GLES20;

import com.ritek.freshwalls.shaderprograms.shaderutil.TexRenderBuffer;

import java.io.InputStream;


public class SimpleShaderProgram extends ShaderProgram {


    private TexRenderBuffer bufA;
    private TexRenderBuffer bufB;
    private TexRenderBuffer bufC;
    private TexRenderBuffer bufPrev;
    private TexRenderBuffer bufLight;
    private TexRenderBuffer bufBlur;

    public SimpleShaderProgram(Context context, InputStream mVert, InputStream mFrag){
        super(context,mVert,mFrag);
    }

    //###################Init Input Value & Draw Program###################
    public void setUniforms(int width, int height, float mouseX, float mouseY, float sensorX, float sensorY, float sensorZ, float sensorAccelX,float sensorAccelY,float screenValue,int textureId,float totalAlpha,float texAlpha,int orientation,float offsetX,float offsetY,float time){



        setupShaderInputs(programOrig,
                new int[]{width, height},
                new int[]{textureId},
                mouseX,mouseY,
                sensorX,sensorY,sensorZ,sensorAccelX,sensorAccelY,
                screenValue,totalAlpha,texAlpha,orientation,offsetX,offsetY,time);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }


}
