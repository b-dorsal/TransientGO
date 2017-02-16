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


import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

/**
 *
 * @author Shakhar Dasgupta <sdasgupt@oswego.edu>
 */
public class TransientsPopulator {

  private static final String CALTECH_AUTHOR_IVORN = "ivo://nvo.caltech/voeventnet";

  private final int limit;
  private final ArrayList<Transient> transients;

  public TransientsPopulator(int limit) {
    this.limit = limit;
    transients = new ArrayList<>(limit);
    //transients = new ArrayList<>(limit);
  }

  private void populate() throws MalformedURLException, UnsupportedEncodingException, IOException, SAXException, ParserConfigurationException {
    String encoded_ivorn = URLEncoder.encode(CALTECH_AUTHOR_IVORN, "UTF-8");
    URL listUrl = new URL("http://voeventdb.4pisky.org/apiv1/list/ivorn?ivorn_prefix=" + encoded_ivorn + "&limit=" + limit + "&order=-author_datetime");
    XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
    TransientHandler handler = new TransientHandler();
    xmlReader.setContentHandler(handler);
    try (InputStream listInputStream = listUrl.openStream();
         JsonReader jsonReader = Json.createReader(listInputStream)) {
      JsonObject object = jsonReader.readObject();
      JsonArray results = object.getJsonArray("result");
      //ArrayList<String> ivorns = new ArrayList<>();
      for (JsonValue result : results.getValuesAs(JsonValue.class)) {
        String ivorn = result.toString();
        //ivorns.add(ivorn);
        transients.add(getTransientByIVORN(ivorn.substring(1, ivorn.length() - 1)));
      }
    }
  }

  private static Transient getTransientByIVORN(String ivorn) throws ParserConfigurationException, SAXException, UnsupportedEncodingException, MalformedURLException, IOException {
    XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
    TransientHandler handler = new TransientHandler();
    xmlReader.setContentHandler(handler);
    String encodedIvorn = URLEncoder.encode(ivorn, "UTF-8");
    URL xmlUrl = new URL("http://voeventdb.4pisky.org/apiv1/packet/xml/" + encodedIvorn);
    try (InputStream xmlInputStream = xmlUrl.openStream()) {
      InputSource inputSource = new InputSource(xmlInputStream);
      xmlReader.parse(inputSource);
    }
    return handler.getTransient();
  }

  public ArrayList<Transient> getTransients() {
    try {
      populate();
    } catch (UnsupportedEncodingException ex) {
      Logger.getLogger(TransientsPopulator.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException | SAXException | ParserConfigurationException ex) {
      Logger.getLogger(TransientsPopulator.class.getName()).log(Level.SEVERE, null, ex);
    }
    return transients;
  }

  public static Transient getTransient(String ivorn) {
    Transient trans = null;
    try {
      trans = getTransientByIVORN(ivorn);
    } catch (ParserConfigurationException | SAXException | IOException ex) {
      Logger.getLogger(TransientsPopulator.class.getName()).log(Level.SEVERE, null, ex);
    }
    return trans;
  }
}
