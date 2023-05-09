package nl.numworx.geodefiner.module;

import javax.inject.Singleton;

import org.cbook.cbookif.CBookContext;

import dagger.BindsInstance;
import dagger.Component;
import nl.numworx.geodefiner.Editor;

@Component(modules= {EditModule.class})
@Singleton
public interface EditComponents {
	Editor editor();
	@Component.Builder
	interface Builder {
		@BindsInstance Builder context(CBookContext context);
		EditComponents build();
	}
}
