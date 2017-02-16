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

import com.google.android.stardroid.activities.util.SerialBitmap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;

/**
 *
 * @author Shakhar Dasgupta <sdasgupt@oswego.edu>
 */
public class TransientHandler extends DefaultHandler {

    private Transient transientObject;

    StringBuffer accumulator;
    private String ivorn;
    private String id;
    private float rightAscension;
    private float declination;
    private boolean isDateTime;
    private String dateTime;
    private float magnitude;
    private String imageURL;
    private String lightCurvePageURL;

    public void startDocument() throws SAXException {
        accumulator = new StringBuffer();
        isDateTime = false;
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if (qName.equals("voe:VOEvent")) {
            ivorn = atts.getValue("ivorn");
        } else if (qName.equals("Param")) {
            switch (atts.getValue("name")) {
                case "ID":
                    id = atts.getValue("value");
                    break;
                case "RA":
                    rightAscension = Float.parseFloat(atts.getValue("value"));
                    break;
                case "Dec":
                    declination = Float.parseFloat(atts.getValue("value"));
                    break;
                case "magnitude":
                    magnitude = Float.parseFloat(atts.getValue("value"));
                    break;
                case "imgmaster":
                    imageURL = atts.getValue("value");
                    break;
                case "lightcurve":
                    lightCurvePageURL = atts.getValue("value");
                    break;
                default:
                    break;
            }
        } else if (qName.equals("ISOTime")) {
            isDateTime = true;
        }
    }

    public void characters(char[] ch, int start, int length) {
        if (isDateTime) {
            accumulator.append(ch, start, length);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("ISOTime")) {
            dateTime = accumulator.toString();
            accumulator.setLength(0);
            isDateTime = false;
        }
    }

    public void endDocument() throws SAXException {
        StringBuffer lightCurveBuffer = new StringBuffer(lightCurvePageURL);
        lightCurveBuffer.insert(lightCurveBuffer.lastIndexOf("/") + 1, "imgs/");
        lightCurveBuffer.replace(lightCurveBuffer.indexOf("p.html"), lightCurveBuffer.length(), ".png");
        String lightCurveURL = lightCurveBuffer.toString();


        SerialBitmap graphBit;
        SerialBitmap imageBit;
        try {
            imageBit = new SerialBitmap(imageURL);
            graphBit = new SerialBitmap(lightCurveURL);
            transientObject = new Transient(ivorn, id, rightAscension, declination, dateTime, magnitude, imageBit, graphBit);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public Transient getTransient() {
        return transientObject;
    }
}
