package nl.numworx.geodefiner.ui;

import java.util.Optional;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import fi.euclides.event.Tracker;
import fi.euclides.model.Locus;
import nl.numworx.geodefiner.merge.RenameAction;

@Module
public abstract class ModelsModule {
	@Provides @Named("locus") static
	ColorModel<Locus> locusModel(Tracker t, Optional<RenameAction> ra) {
		ColorModel<Locus> cm = new ColorModel<Locus>();
		cm.rename = ra;
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
