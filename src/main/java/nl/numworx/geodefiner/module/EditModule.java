package nl.numworx.geodefiner.module;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import fi.euclides.event.Tracker;
import fi.euclides.model.AbstractViewer;
import fi.euclides.swing.AWTViewer;
import nl.numworx.geodefiner.Definitions;
import nl.numworx.geodefiner.GeoDefiner;
import nl.numworx.geodefiner.Instance;
import nl.numworx.geodefiner.InstanceViewer;
import nl.numworx.geodefiner.ToolboxPanel;
import nl.numworx.geodefiner.WiskOpdrRandomizer;
import nl.numworx.geodefiner.common.Randomizer;
import nl.numworx.geodefiner.merge.MergeModule;

@Module(includes= {MergeModule.class})
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
	
	@Provides @Singleton @Named("context") static  Optional<JPopupMenu> popup(
	    @Named("open") Provider<Action> safeAction,
	    @Named("safe") Provider<Action> openAction,
	    @Named("merge") Provider<Action> mergeAction
	    ) {
	  if (GeoDefiner.isPremium && GeoDefiner.isExperimental) {
	    JPopupMenu popup = new JPopupMenu();
	    popup.add(new JMenuItem(openAction.get()));
	    popup.add(new JMenuItem(safeAction.get()));
	    popup.add(new JMenuItem(mergeAction.get()));	    
	    return Optional.of(popup);
	  }
	  return Optional.empty();
	}
}
