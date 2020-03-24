package nl.numworx.geodefiner.ui;

import javax.inject.Named;

import dagger.BindsInstance;
import dagger.Subcomponent;
import fi.euclides.model.Destroyable;

@Subcomponent(modules = { ModelsModule.class })
public interface Models {
  OModel omodel();
  UModel umodel();
  PointModel pointmodel();

  @Subcomponent.Builder
  interface Builder {
    Models build();
    @BindsInstance Builder init(@Named("p") Destroyable p);
  }

}
