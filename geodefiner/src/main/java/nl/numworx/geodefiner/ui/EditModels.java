package nl.numworx.geodefiner.ui;

import dagger.Subcomponent;

@Subcomponent(modules = { ModelsModule.class, RenameModule.class })
public interface EditModels extends Models {
	  @Subcomponent.Builder interface Builder extends Models.Builder {
		  EditModels build();
	  }

}
