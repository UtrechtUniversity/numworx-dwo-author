package nl.numworx.geodefiner.module;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import fi.euclides.proof.Const;
import fi.euclides.proof.DrieOpEenRij;
import fi.euclides.proof.Equidistant;
import fi.euclides.proof.FlipFlop;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.proof.LijnLijnTest;
import fi.euclides.proof.MidpointTester;
import fi.euclides.proof.PointOnObject;
import fi.euclides.proof.VierOpEenCirkel;

@Module
public abstract class DelegateModule {

	@Provides @ElementsIntoSet public static Set<LabelDelegate> all() {
			LabelDelegate[] standards = {
					new PointOnObject(),
					new DrieOpEenRij(),
					new MidpointTester(),
					new VierOpEenCirkel(),
					new LijnLijnTest(" \u2225 ", true),
					new LijnLijnTest(" \u22A5 ", false),
					new Equidistant(),
					new Const(),
					new FlipFlop(),
			};
			return new HashSet<>(Arrays.asList(standards));
	}	
}
