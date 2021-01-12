package nl.numworx.geodefiner.ui;

import javax.inject.Named;

import dagger.Subcomponent;
import fi.euclides.model.Locus;

@Subcomponent(modules = { ModelsModule.class })
public interface Models {
  OModel omodel();
  UModel umodel();
  PointModel pointmodel();
  LineModel linemodel();
  RayModel raymodel();
  AxesModel axesmodel();
  SegmentModel segmentmodel();
  CircleModel circlemodel();
  TriangleModel triangleModel();

  @Named("locus") ColorModel<Locus> integralmodel();
  @Named("vgl")   LineModel vglmodel();
  @Named("ivgl")  ColorModel<Locus> inequalitymodel();
  GridModel gridmodel();
  
  TextModel textmodel();
  IntervalModel intervalmodel();
  
  @Subcomponent.Builder interface Builder {
	  Models build();
  }

}
