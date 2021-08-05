package com.ritek.freshwalls.shaderprograms;

import android.content.Context;
import android.opengl.GLES20;


import com.ritek.freshwalls.shaderprograms.shaderutil.Constants;
import com.ritek.freshwalls.shaderprograms.shaderutil.ShaderHelper;
import com.ritek.freshwalls.shaderprograms.shaderutil.TextResourceReader;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


public class ShaderProgram {

    //Uniform常量
    //protected static final String U_MATRIX = "u_Matrix";
    //protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    //protected static final String U_COLOR = "u_Color";

    //Attribute 常量

    //###################Vertex Data###################
    //Vertex Array 和 Simple Color 对象移过来

    private static FloatBuffer VERTEX_BUF, TEXTURE_COORD_BUF;
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;
    private static final float[] VERTEX_DATA = {
            //Order of coordinates: X, Y
            1.0f, -1.0f,
            -1.0f, -1.0f,
            1.0f, 1.0f,
            -1.0f, 1.0f,
    };

    private static final float[] TEXTURE_DATA = {
            //Order of coordinates: X, Y
            1.0f, 0.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
    };

    //###################Memory Allocate & Build Program###################


    protected final int programOrig;
    //protected final Buffer vertexBuffer;

    protected ShaderProgram(Context context, InputStream givenVertex, InputStream givenFrag){

        if (VERTEX_BUF == null) {
            VERTEX_BUF = ByteBuffer.allocateDirect(VERTEX_DATA.length * 4)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();
            VERTEX_BUF.put(VERTEX_DATA);
            VERTEX_BUF.position(0);
        }

        if (TEXTURE_COORD_BUF == null) {
            TEXTURE_COORD_BUF = ByteBuffer.allocateDirect(TEXTURE_DATA.length * 4)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();
            TEXTURE_COORD_BUF.put(TEXTURE_DATA);
            TEXTURE_COORD_BUF.position(0);
        }


        //拿到着色器GLSL文件，用buildProgram构建Program
        programOrig = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(context, givenVertex),
                TextResourceReader.readTextFileFromResource(context, givenFrag));


    }


    //###################Init Input Function###################

    void setupShaderInputs(int program, int[] iResolution, int[] iChannels, float mouseX,float mouseY,float sensorX,float sensorY,float sensorZ,float sensorAccelX,float sensorAccelY,float screenValue,float totalAlpha,float texAlpha,int orientation,float offsetX,float offsetY,float time) {
        GLES20.glUseProgram(program);

        int vPositionLocation = GLES20.glGetAttribLocation(program, "a_Position");
        GLES20.glEnableVertexAttribArray(vPositionLocation);
        GLES20.glVertexAttribPointer(vPositionLocation, 2, GLES20.GL_FLOAT, false, 8, VERTEX_BUF);

        int vTexCoordLocation = GLES20.glGetAttribLocation(program, "a_TextureCoordinates");
        GLES20.glEnableVertexAttribArray(vTexCoordLocation);
        GLES20.glVertexAttribPointer(vTexCoordLocation, 2, GLES20.GL_FLOAT, false, 8, TEXTURE_COORD_BUF);

        int iResolutionLocation = GLES20.glGetUniformLocation(program, "u_resolution");
        GLES20.glUniform2f(iResolutionLocation,(float) iResolution[0],(float) iResolution[1]);
        //GLES20.glUniform3fv(iResolutionLocation, 1,FloatBuffer.wrap(new float[]{(float) iResolution[0], (float) iResolution[1], 1.0f}));

        int iGlobalTimeLocation = GLES20.glGetUniformLocation(program, "u_time");
        GLES20.glUniform1f(iGlobalTimeLocation, time);

        for (int i = 0; i < iChannels.length; i++) {
            int sTextureLocation = GLES20.glGetUniformLocation(program, "iChannel" + i);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + i);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iChannels[i]);
            GLES20.glUniform1i(sTextureLocation, i);
        }

        int iMouseLocation = GLES20.glGetUniformLocation(program,"u_mouse");
        GLES20.glUniform2f(iMouseLocation,mouseX,mouseY);

        int iSensorXLocation = GLES20.glGetUniformLocation(program,"u_sensor_x");
        GLES20.glUniform1f(iSensorXLocation,sensorX);

        int iSensorYLocation = GLES20.glGetUniformLocation(program,"u_sensor_y");
        GLES20.glUniform1f(iSensorYLocation,sensorY);

        int iSensorZLocation = GLES20.glGetUniformLocation(program,"u_sensor_z");
        GLES20.glUniform1f(iSensorZLocation,sensorZ);

        int iSensorAccelLocation = GLES20.glGetUniformLocation(program,"u_sensor_accel");
        GLES20.glUniform2f(iSensorAccelLocation,sensorAccelX,sensorAccelY);

        int iScreenOnLocation = GLES20.glGetUniformLocation(program,"u_screen_value");
        GLES20.glUniform1f(iScreenOnLocation,screenValue);

        int iTotalAlphaLocation = GLES20.glGetUniformLocation(program,"u_total_alpha");
        GLES20.glUniform1f(iTotalAlphaLocation,totalAlpha);

        int iTexAlphaLocation = GLES20.glGetUniformLocation(program,"u_tex_alpha");
        GLES20.glUniform1f(iTexAlphaLocation,texAlpha);

        int iOrientationLocation = GLES20.glGetUniformLocation(program,"u_orig_orientation");
        GLES20.glUniform1i(iOrientationLocation,orientation);


        int iOffsetLocation = GLES20.glGetUniformLocation(program, "u_offset");
        GLES20.glUniform2f(iOffsetLocation, offsetX,offsetY);

    }



}