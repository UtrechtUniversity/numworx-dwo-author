package nl.numworx.geodefiner.ui;

import java.util.Optional;

import javax.annotation.Nullable;
import javax.inject.Provider;

import dagger.Binds;
import dagger.MembersInjector;
import dagger.Module;
import dagger.Provides;
import nl.numworx.geodefiner.GeoDefiner;
import nl.numworx.geodefiner.merge.RenameAction;
import nl.numworx.geodefiner.merge.RenameActionImpl;

@Module
public abstract class RenameModule {

	@Provides static RenameActionImpl impl(MembersInjector<RenameActionImpl> inject) {
		RenameActionImpl instance = new RenameActionImpl();
		inject.injectMembers(instance);
		return instance;
	}
		
	@Binds abstract RenameAction rename(RenameActionImpl impl);
		
}
