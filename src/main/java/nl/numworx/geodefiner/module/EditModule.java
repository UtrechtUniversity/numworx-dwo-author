package nl.numworx.geodefiner.module;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import fi.euclides.event.Tracker;
import fi.euclides.model.AbstractViewer;
import nl.numworx.geodefiner.CommandPanel;
import nl.numworx.geodefiner.Definitions;
import nl.numworx.geodefiner.Instance;
import nl.numworx.geodefiner.common.Randomizer;

@Module
public abstract class EditModule {

	@Provides @Singleton static Instance instance(Randomizer r) {
		return new Instance(r);
	}
	@Binds abstract Randomizer randomizer(CommandPanel p);
	@Binds abstract Tracker tracker(AbstractViewer v);
	
	@Provides @Singleton @Named("random") static Map<String,Number> random() {
		return new LinkedHashMap<String,Number>();
	}
	
	@Provides static AbstractViewer viewer(Instance instance) {
		return instance.getViewer();
	}
	@Provides static Definitions definitions(Instance instance) {
		return instance.getDefinitions();
	}
}
