package nl.numworx.geodefiner.module;

import javax.inject.Singleton;
import javax.swing.JToolBar;

import dagger.BindsInstance;
import dagger.Component;
import nl.numworx.geodefiner.Instance;
import nl.numworx.geodefiner.common.DefaultRandomizer;
import nl.numworx.geodefiner.common.Randomizer;

@Component(modules=Modules.class)
@Singleton
public interface Components {
	void inject(Instance instance);
	
	@Component.Builder
	interface Builder {
		@BindsInstance Builder instance(nl.numworx.geodefiner.common.Instance instance);
		@BindsInstance Builder randomizer(DefaultRandomizer random);
		@BindsInstance Builder toolbox(JToolBar toolbox);
		Components build();
	}
}
