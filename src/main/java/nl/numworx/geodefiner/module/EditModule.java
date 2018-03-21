package nl.numworx.geodefiner.module;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.swing.JButton;
import javax.swing.JComponent;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import fi.euclides.event.Tracker;
import fi.euclides.model.AbstractViewer;
import nl.numworx.geodefiner.CommandPanel;
import nl.numworx.geodefiner.Definitions;
import nl.numworx.geodefiner.Instance;
import nl.numworx.geodefiner.WiskOpdrRandomizer;
import nl.numworx.geodefiner.common.Randomizer;

@Module
public abstract class EditModule {

	@Provides @Singleton static Instance instance(WiskOpdrRandomizer r) {
		return new Instance(r);
	}
	@Binds abstract Randomizer randomizer(WiskOpdrRandomizer p);
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
	@Provides @Named("checkBtn") static JButton checkBtn (Instance instance) {
		return instance.checkBtn;
	}

	@Provides @Named("validator") static JComponent validator(Instance instance) {
		return instance.asComponent();
	}
}
