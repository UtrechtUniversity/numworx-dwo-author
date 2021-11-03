package nl.numworx.geodefiner.module;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import org.cbook.cbookif.CBookEventHandler;

import dagger.Binds;
import dagger.BindsOptionalOf;
import dagger.MembersInjector;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.IntoSet;
import dagger.multibindings.StringKey;
import fi.euclides.event.Tracker;
import fi.euclides.model.AbstractViewer;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Model;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.swing.AWTViewer;
import nl.numworx.geodefiner.Definitions;
import nl.numworx.geodefiner.GeoDefiner;
import nl.numworx.geodefiner.HerleidList;
import nl.numworx.geodefiner.Instance;
import nl.numworx.geodefiner.InstanceViewer;
import nl.numworx.geodefiner.ToolboxPanel;
import nl.numworx.geodefiner.WiskOpdrRandomizer;
import nl.numworx.geodefiner.common.CheckObjectList;
import nl.numworx.geodefiner.common.DefaultRandomizer;
import nl.numworx.geodefiner.common.NamingModel;
import nl.numworx.geodefiner.common.Randomizer;
import nl.numworx.geodefiner.common.math.Expression;
import nl.numworx.geodefiner.common.math.ToC;
import nl.numworx.geodefiner.merge.MergeModule;
import nl.numworx.geodefiner.merge.RenameAction;
import nl.numworx.geodefiner.ui.EditModels;
import nl.numworx.geodefiner.ui.Models;
import nl.numworx.geodefiner.ui.RenameModule;
import nl.numworx.geodefiner.ui.UIModelFactory;

@Module(includes= {MergeModule.class, ToolBoxModule.class, DelegateModule.class}, subcomponents = {EditModels.class})
public abstract class EditModule {
  
  
    @Provides @Singleton static nl.numworx.geodefiner.common.Instance virgin() {
      return new Instance(null);
    }
  
    @Provides @Singleton static Instance instance(nl.numworx.geodefiner.common.Instance virgin, MembersInjector<Instance> injector) {
      Instance instance = (Instance) virgin;
      instance.inject(injector);
      return instance;
    }

//	@Provides @Singleton static Instance instance(WiskOpdrRandomizer r, JToolBar toolbar) {
//		return new Instance(r, toolbar);
//	}

	@BindsOptionalOf abstract RenameAction rename();
	@Binds abstract Models.Builder builder(EditModels.Builder builder);

	
	@Binds abstract Randomizer randomizer(WiskOpdrRandomizer p);
    @Binds abstract DefaultRandomizer defaultRandomizer(WiskOpdrRandomizer p);
	@Binds abstract Tracker tracker(InstanceViewer v);
	@Binds abstract AWTViewer awtviewer(InstanceViewer v);
	@Binds abstract AbstractViewer abstractViewer(InstanceViewer v);
//	@Binds abstract nl.numworx.geodefiner.common.Instance commonInstance(Instance instance);
	
	@Provides @Singleton @Named("random") static Map<String,Number> random() {
		return new LinkedHashMap<String,Number>();
	}
	
//	@Provides static InstanceViewer viewer(Instance instance) {
//		return instance.getViewer();
//	}

	@Provides @Named("checkBtn") static JButton checkBtn (Instance instance) {
		return instance.checkBtn;
	}

	@Provides @Named("validator") static JComponent validator(Instance instance) {
		return instance.asComponent();
	}
	
	@Provides @Singleton static JToolBar toolbar() {
		return new JToolBar();
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
// shared?	
	    @Provides @Singleton static
	    CBookEventHandler eventHandler(nl.numworx.geodefiner.common.Instance instance) {
	        return new CBookEventHandler(instance);
	    }
	    @Provides @Singleton static
	    CheckObjectList checkObjectList(nl.numworx.geodefiner.common.Instance instance, Tracker viewer, Expression expression) {
	        CheckObjectList c = new CheckObjectList(viewer, expression);
	        c.setInstance(instance);
	        return c;
	    }
	    @Provides @Singleton static
	    Expression expression(Map<String, LabelDelegate> symbols) {
	        Expression e = new Expression();
	        e.symbolmap.putAll(symbols);
	        return e;
	    }
	    
	    @Provides @IntoMap @StringKey("list1.list") static
	    LabelDelegate herleidList() { return new HerleidList(); }

	    @Provides @Singleton static
	    Definitions definitions(InstanceViewer tracker, UIModelFactory fac) {
	        return new Definitions(tracker, fac);
	    }
	    @Provides @Named("expressions") static
	    Map<String,String> expressions() {
	      return new LinkedHashMap<String, String>();
	    }
	    @Provides @Singleton static
	    NamingModel nameMapper(Model m) {
	        return new NamingModel(m, new WeakHashMap<String,Destroyable>());
	    }
	    @Provides @Singleton static
	    Model model() { return new Model(); }

	    @Binds @IntoSet abstract LabelDelegate toc(ToC toc);
	    @Provides @Singleton static
	    ToC toctoc() { return new ToC(); }

	    @Provides static nl.numworx.geodefiner.common.Instance.Selector selector(nl.numworx.geodefiner.common.Instance instance) {
	        return instance.selector;
	    }

}
