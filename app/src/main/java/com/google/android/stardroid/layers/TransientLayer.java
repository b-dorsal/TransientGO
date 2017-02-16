// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.android.stardroid.layers;

import android.content.res.Resources;

import com.google.android.stardroid.R;
import com.google.android.stardroid.base.TimeConstants;
import com.google.android.stardroid.control.AstronomerModel;
import com.google.android.stardroid.renderer.RendererObjectManager.UpdateType;
import com.google.android.stardroid.source.AbstractAstronomicalSource;
import com.google.android.stardroid.source.AstronomicalSource;
import com.google.android.stardroid.source.ImageSource;
import com.google.android.stardroid.source.Sources;
import com.google.android.stardroid.source.TextSource;
import com.google.android.stardroid.source.impl.ImageSourceImpl;
import com.google.android.stardroid.source.impl.TextSourceImpl;
import com.google.android.stardroid.units.GeocentricCoordinates;
import com.google.android.stardroid.units.RaDec;
import com.google.android.stardroid.units.Vector3;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * A {@link Layer} to show active transient objects.
 *
 * @author Brian Dorsey
 */
public class TransientLayer extends AbstractSourceLayer {
  private List<Transient> transients = new ArrayList<>();

  /**
   * Represents a Transient Object/Event.
   */
  private static class Transient {

    private String ID;
    private float RA;
    private float DEC;
    private double MAG;
    private String DATE;
    private String TYPE;

    public GeocentricCoordinates radiant;

    public Transient(String id, float ra, float dec, String date, double mag, String type){
      this.ID = id;
      this.RA = ra;
      this.DEC = dec;
      this.MAG = mag;
      this.TYPE = type;
      this.DATE = date;

      RaDec redec = new RaDec(this.RA, this.DEC);
      this.radiant = new GeocentricCoordinates(0,0,0);
      this.radiant.updateFromRaDec(redec);
    }

    public String getID(){
      return this.ID;
    }
    public double getRA(){
      return this.RA;
    }
  }

  private final AstronomerModel model;

  public TransientLayer(AstronomerModel model, Resources resources) {
    super(resources, true);
    this.model = model;
    initializeTransients();
  }

  private void initializeTransients() {


//    transients.add(new Transient("Transient", 9, 39, "TODAY", 18, "TEST"));
//    transients.add(new Transient("Transient", 50, 20, "TODAY", 45, "TEST2"));
//    transients.add(new Transient("Transient", 22, -15, "TODAY", 50, "TEST3"));
  }

  @Override
  protected void initializeAstroSources(ArrayList<AstronomicalSource> sources) {
    for (Transient tr : transients) {
      sources.add(new TransientRadiantSource(model, tr, getResources()));
    }
  }

  @Override
  public int getLayerDepthOrder() {
    return 80;
  }

  @Override
  public String getPreferenceId() {
    return "source_provider.0";
  }

  @Override
  public String getLayerName() {
    return "Transient Objects";
  }

  @Override
  protected int getLayerNameId() {
    return R.string.show_meteors_pref;
  }

  private static class TransientRadiantSource extends AbstractAstronomicalSource {
    private static final int LABEL_COLOR = 0xf67e81;
    private static final Vector3 UP = new Vector3(0.0f, 1.0f, 0.0f);
    private static final long UPDATE_FREQ_MS = 1L * TimeConstants.MILLISECONDS_PER_DAY;
    private static final float SCALE_FACTOR = 0.03f;

    private final List<ImageSource> imageSources = new ArrayList<>();
    private final List<TextSource> labelSources = new ArrayList<>();

    private final AstronomerModel model;

    private long lastUpdateTimeMs = 0L;
    private ImageSourceImpl theImage;
    private TextSource label;
    private Transient transientS;
    private String name;
    private List<String> searchNames = new ArrayList<>();

    public TransientRadiantSource(AstronomerModel model, Transient transientT, Resources resources) {
      this.model = model;
      this.transientS = transientT;
      this.name = transientS.getID(); //supposed to get a 'nameID' integer, idk what that means lol
      //System.out.println(this.name + "+++++++++++++++++++++++++++++++++++\n\n");

      searchNames.add(name);
      // blank is a 1pxX1px image that should be invisible.
      // We'd prefer not to show any image except on the shower dates, but there
      // appears to be a bug in the renderer/layer interface in that Update values are not
      // respected.  Ditto the label.
      // TODO(johntaylor): fix the bug and remove this blank image
      theImage = new ImageSourceImpl(transientS.radiant, resources, R.drawable.blank, UP, SCALE_FACTOR);
      imageSources.add(theImage);
      label = new TextSourceImpl(transientS.radiant, name, LABEL_COLOR);
      labelSources.add(label);
    }

    @Override
    public List<String> getNames() {
      return searchNames;
    }

    @Override
    public GeocentricCoordinates getSearchLocation() {
      return transientS.radiant;
    }

    private void updateTransient() {
      theImage.setUpVector(UP);
      theImage.setImageId(R.drawable.transientobj);
    }

    @Override
    public Sources initialize() {
      updateTransient();
      return this;
    }

    @Override
    public EnumSet<UpdateType> update() {
      EnumSet<UpdateType> updateTypes = EnumSet.noneOf(UpdateType.class);
      if (Math.abs(model.getTime().getTime() - lastUpdateTimeMs) > UPDATE_FREQ_MS) {
        updateTransient();
        updateTypes.add(UpdateType.Reset);
      }
      return updateTypes;
    }

    @Override
    public List<? extends ImageSource> getImages() {
      return imageSources;
    }

    @Override
    public List<? extends TextSource> getLabels() {
      return labelSources;
    }
  }
}
