package nl.numworx.geodefiner.module;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;

import org.openjdk.tools.javadoc.internal.doclets.toolkit.util.VisibleMemberMap.GetterSetter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import fi.euclides.event.Tracker;
import fi.euclides.model.AbstractViewer;
import fi.euclides.swing.AWTViewer;
import nl.numworx.geodefiner.CommandPanel;
import nl.numworx.geodefiner.Definitions;
import nl.numworx.geodefiner.Instance;
import nl.numworx.geodefiner.InstanceViewer;
import nl.numworx.geodefiner.ToolboxPanel;
import nl.numworx.geodefiner.WiskOpdrRandomizer;
import nl.numworx.geodefiner.common.Randomizer;

@Module()
public abstract class EditModule {

	@Provides @Singleton static Instance instance(WiskOpdrRandomizer r, JToolBar toolbar) {
		return new Instance(r, toolbar);
	}

	@Binds abstract Randomizer randomizer(WiskOpdrRandomizer p);
	@Binds abstract Tracker tracker(InstanceViewer v);
	@Binds abstract AWTViewer awtviewer(InstanceViewer v);
	@Binds abstract AbstractViewer abstractViewer(InstanceViewer v);
	@Binds abstract nl.numworx.geodefiner.common.Instance commonInstance(Instance instance);
	
	@Provides @Singleton @Named("random") static Map<String,Number> random() {
		return new LinkedHashMap<String,Number>();
	}
	
	@Provides static InstanceViewer viewer(Instance instance) {
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
	
	@Provides @Singleton static JToolBar toolbar() {
		return new JToolBar();
	}
	
	@Provides static ToolboxPanel toolboxPanel(Instance instance) {
		return instance.getToolboxPanel();
	}
}
