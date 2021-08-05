package com.ritek.freshwalls.shaderprograms.shaderutil;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class TextResourceReader {

    public static String readTextFileFromResource(Context context, InputStream inputStream){
        StringBuilder body = new StringBuilder();

        try{
//            InputStream inputStream = context.getResources().getAssets().open(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String nextLine;

            while((nextLine = bufferedReader.readLine()) != null){
                body.append(nextLine);
                body.append('\n');
            }
        } catch (IOException e){
            throw new RuntimeException("Could not open resource:" ,e);
        } catch (Resources.NotFoundException nfe){
            throw new RuntimeException("Resource not found:" ,nfe);
        }

        return body.toString();
    };


}
