package nl.numworx.geodefiner.module;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import nl.numworx.geodefiner.Instance;
import nl.numworx.geodefiner.common.NamingModel;
import nl.numworx.geodefiner.common.math.Expression;

@Component(modules=Modules.class)
@Singleton
public interface Components {
	void inject(Instance instance);

	NamingModel getNameMapper();

	Expression getExpression();
	
	@Component.Builder
	interface Builder {
		@BindsInstance Builder instance(nl.numworx.geodefiner.common.Instance instance);
		Components build();
	}
}
