package nl.numworx.geodefiner.merge;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.swing.Action;
import javax.swing.JFileChooser;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class MergeModule {

  @Binds @Named("safe") abstract Action storeAction(StoreAction action);
  @Binds @Named("merge") abstract Action mergeAction(MergeAction action);
  @Binds @Named("open") abstract Action openAction(OpenAction action);

  @Provides @Singleton static JFileChooser filechooser() {
    return new fi.beans.numworxlf.JFileChooser();
  }
}
