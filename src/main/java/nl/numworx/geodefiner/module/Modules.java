package nl.numworx.geodefiner.module;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.inject.Singleton;

import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookEventHandler;

import dagger.Binds;
import dagger.BindsOptionalOf;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.IntoSet;
import dagger.multibindings.StringKey;
import fi.euclides.event.Tracker;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Model;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.swing.AWTViewer;
import nl.numworx.geodefiner.Definitions;
import nl.numworx.geodefiner.HerleidList;
import nl.numworx.geodefiner.InstanceViewer;
import nl.numworx.geodefiner.IsColor;
import nl.numworx.geodefiner.WiskOpdrRandomizer;
import nl.numworx.geodefiner.common.CheckObjectList;
import nl.numworx.geodefiner.common.DefaultRandomizer;
import nl.numworx.geodefiner.common.Instance;
import nl.numworx.geodefiner.common.NamingModel;
import nl.numworx.geodefiner.common.Randomizer;
import nl.numworx.geodefiner.common.math.Expression;
import nl.numworx.geodefiner.common.math.ToC;
import nl.numworx.geodefiner.merge.RenameAction;
import nl.numworx.geodefiner.merge.RenameActionImpl;
import nl.numworx.geodefiner.ui.Models;
import nl.numworx.geodefiner.ui.UIModelFactory;

@Module(includes= {Modules.Conversions.class, DelegateModule.class, ToolBoxModule.class},
        subcomponents = { Models.class })
public abstract class Modules {
	
	@Module
	public interface Conversions {
		@Binds Tracker tracker(InstanceViewer viewer);
		@Binds AWTViewer awtviewer(InstanceViewer viewer);
		@Binds @IntoSet LabelDelegate toc(ToC toc);
	    @Binds Randomizer defaultRandomizer(DefaultRandomizer p);
	}
		
	@Provides @Singleton static
	Model model() { return new Model(); }
	
	@Provides @Singleton static
	NamingModel nameMapper(Model m) {
		return new NamingModel(m, new WeakHashMap<String,Destroyable>());
	}
	
	@Provides @Singleton static
	Expression expression(Map<String, LabelDelegate> symbols) {
		Expression e = new Expression();
		e.symbolmap.putAll(symbols);
		return e;
	}
	
	@Provides @IntoMap @StringKey("list1.list") static
	LabelDelegate herleidList() { return new HerleidList(); }
	
	@Provides @IntoMap @StringKey("geodefiner.color") static
	LabelDelegate isColor() { return new IsColor(); }
	
	
	@Provides @Singleton static
	ToC toc() { return new ToC(); }
	
	@Provides @Singleton static
	CBookEventHandler eventHandler(Instance instance) {
		return new CBookEventHandler(instance);
	}
	
	@Provides @Singleton static
	CheckObjectList checkObjectList(Instance instance, Tracker viewer, Expression expression) {
		CheckObjectList c = new CheckObjectList(viewer, expression);
		c.setInstance(instance);
		return c;
	}
	
	@Provides @Singleton static CBookContext nocontext() {
	  return new CBookContext() {
        
        @Override
        public Object getProperty(String arg0) {
          // TODO Auto-generated method stub
          return null;
        }
      };
	}

	@BindsOptionalOf abstract RenameAction rename();
	
	@Provides @Singleton static
	Definitions definitions(InstanceViewer tracker, UIModelFactory fac) {
		return new Definitions(tracker, fac);
	}
	
	@Provides @Named("expressions") static
	Map<String,String> expressions() {
	  return new LinkedHashMap<String, String>();
	}

	@Provides static Instance.Selector selector(Instance instance) {
		return instance.selector;
	}

  }
