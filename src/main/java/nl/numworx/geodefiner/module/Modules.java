package nl.numworx.geodefiner.module;

import java.util.Map;
import java.util.WeakHashMap;

import javax.inject.Singleton;

import org.cbook.cbookif.CBookEventHandler;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Model;
import fi.euclides.proof.LabelDelegate;
import nl.numworx.geodefiner.HerleidList;
import nl.numworx.geodefiner.common.Instance;
import nl.numworx.geodefiner.common.NamingModel;
import nl.numworx.geodefiner.common.math.Expression;

@Module
public abstract class Modules {
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
	
	@Provides @Singleton static
	CBookEventHandler eventHandler(Instance instance) {
		return new CBookEventHandler(instance);
	}
}
