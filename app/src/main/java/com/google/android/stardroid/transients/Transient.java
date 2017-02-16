/*
 * Copyright 2016 Shakhar Dasgupta <sdasgupt@oswego.edu>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.stardroid.transients;

import android.graphics.Bitmap;

import com.google.android.stardroid.activities.util.SerialBitmap;

import java.io.Serializable;

/**
 *
 * @author Shakhar Dasgupta <sdasgupt@oswego.edu>
 */
public class Transient implements Serializable{

    private final String ivorn;
    private final String id;
    private final float rightAscension;
    private final float declination;
    private final String dateTime;
    private final float magnitude;
    private final SerialBitmap imageBMP;
    private final SerialBitmap lightCurveBMP;

    public Transient(String ivorn, String id, float rightAscension, float declination, String dateTime, float magnitude, SerialBitmap imageBMP, SerialBitmap lightCurveBMP) {
        this.ivorn = ivorn;
        this.id = id;
        this.rightAscension = rightAscension;
        this.declination = declination;
        this.dateTime = dateTime;
        this.magnitude = magnitude;
        this.imageBMP = imageBMP;
        this.lightCurveBMP = lightCurveBMP;
    }

    public String getIvorn() {
        return ivorn;
    }

    public String getId() {
        return id;
    }

    public float getRightAscension() {
        return rightAscension;
    }

    public float getDeclination() {
        return declination;
    }

    public String getDateTime() {
        return dateTime;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public Bitmap getImageBMP() {
        return imageBMP.getBitmap();
    }

    public Bitmap getLightCurveBMP() {
        return lightCurveBMP.getBitmap();
    }

}
