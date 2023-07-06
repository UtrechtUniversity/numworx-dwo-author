package nl.numworx.geodefiner.ui;

import java.util.Optional;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.inject.Provider;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import fi.euclides.event.Tracker;
import fi.euclides.model.Locus;
import nl.numworx.geodefiner.GeoDefiner;
import nl.numworx.geodefiner.merge.RenameAction;
import nl.numworx.geodefiner.merge.RenameActionImpl;

@Module
public abstract class ModelsModule {
	@Provides @Named("locus") static
	ColorModel<Locus> locusModel(Tracker t, Optional<RenameAction> ra) {
		ColorModel<Locus> cm = new ColorModel<Locus>();
		cm.setRename(ra);
		cm.set(t);
		return cm;
	}

	@Provides @Named("vgl") static
	LineModel vglModel(Tracker t) {
		return new LineModel(t, Optional.empty());
	}
	
	@Provides @Named("ivgl") static
	ColorModel<Locus> ivglModel(Tracker t) {
		return locusModel(t, Optional.empty());
	}

}
