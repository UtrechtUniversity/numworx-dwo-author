package nl.numworx.geodefiner.module;

import java.awt.Cursor;

import javax.inject.Singleton;
import javax.swing.Action;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntKey;
import dagger.multibindings.IntoMap;
import fi.euclides.event.AddBissectriceHandler;
import fi.euclides.event.AddBoogHandler;
import fi.euclides.event.AddFocusHandler;
import fi.euclides.event.AddKegelsnedeHandler;
import fi.euclides.event.AddLijnHandler;
import fi.euclides.event.AddLocusHandler;
import fi.euclides.event.AddLoodLijnHandler;
import fi.euclides.event.AddMiddelPuntHandler;
import fi.euclides.event.AddParallelHandler;
import fi.euclides.event.AddPoollijnHandler;
import fi.euclides.event.AddRaakLijnHandler;
import fi.euclides.event.AddSpiegelHandler;
import fi.euclides.event.EventHandler;
import fi.euclides.proof.AfstandHandler;
import fi.euclides.proof.HoekHandler;
import fi.euclides.proof.OppHandler;
import fi.euclides.proof.VectorHandler;
import fi.euclides.swing.AWTViewer;
import fi.euclides.swing.CirkelAction;
import fi.euclides.swing.PanHandler;
import fi.euclides.swing.PuntAction;
import fi.euclides.swing.XXXAction;
import fi.euclides.util.Messages;
import nl.numworx.geodefiner.CirkelRadiusHandler;
import nl.numworx.geodefiner.Definitions;
import nl.numworx.geodefiner.FormuleHandler;
import nl.numworx.geodefiner.TextHandler;
import nl.numworx.geodefiner.TrailAction;
import nl.numworx.geodefiner.common.AddCirkelHandler;
import nl.numworx.geodefiner.common.AddPolygonHandler;
import nl.numworx.geodefiner.common.AddSnapPuntHandler;
import nl.numworx.geodefiner.common.FilteredDestroyHandler;
import nl.numworx.geodefiner.common.Instance;
import nl.numworx.geodefiner.common.ResetHandler;
import nl.numworx.geodefiner.common.Tools;

@Module
public abstract class ToolBoxModule implements Tools {

	@Provides @Singleton @IntoMap @IntKey(RESET) static 
	Action reset(Instance instance, AWTViewer viewer) {
		ResetHandler resetter = new ResetHandler("Reset", instance);
		return new XXXAction("Reset", "/reseticon.gif", resetter, viewer);
	}

	@Provides @Singleton @IntoMap @IntKey(DESTROY) static 
	Action destroy(Instance instance, AWTViewer viewer) {
		XXXAction xaction = new XXXAction(Messages.getString("Euclides.37"), "/delete.png", new FilteredDestroyHandler(instance),viewer);
		xaction.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
		return xaction;
	}

	@Provides @Singleton @IntoMap @IntKey(SELECTOR) static 
	Action selector(Instance instance, AWTViewer viewer) {
		EventHandler selector = instance.selector;
		XXXAction xaction = new XXXAction(Messages.getString("Euclides.35"), "/move.png", selector, viewer);
		xaction.cursor = Cursor.getDefaultCursor();
		return xaction;
	}
	
	@Provides @Singleton @IntoMap @IntKey(POINT) static 
	Action point(AWTViewer viewer) {
		return new PuntAction(Messages.getString("Euclides.46"), "/point.png", new AddSnapPuntHandler(),viewer);
	}
	@Provides @Singleton @IntoMap @IntKey(LINE) static 
	Action line(AWTViewer viewer) {
		return new XXXAction(Messages.getString("Euclides.50"), "/line.png", new AddLijnHandler(AddLijnHandler.LINE),viewer);
	}
	@Provides @Singleton @IntoMap @IntKey(SEGMENT) static 
	Action segment(AWTViewer viewer) {
		return new XXXAction(Messages.getString("Euclides.48"), "/segment.png", new AddLijnHandler(AddLijnHandler.SEGMENT),viewer);
	}
	@Provides @Singleton @IntoMap @IntKey(HALFLINE) static 
	Action halfline(AWTViewer viewer) {
		return new XXXAction(Messages.getString("Euclides.49"), "/ray.png", new AddLijnHandler(AddLijnHandler.RAY),viewer);
	}

	@Provides @Singleton @IntoMap @IntKey(PERPENDICULAR) static 
	Action perpendicular(AWTViewer viewer) {
		return new XXXAction(Messages.getString("Euclides.56"), "/plumb.png", new AddLoodLijnHandler(),viewer);
	}
	@Provides @Singleton @IntoMap @IntKey(PARALLEL) static 
	Action parallel(AWTViewer viewer) {
		return new XXXAction(Messages.getString("Euclides.58"), "/parallel.png", new AddParallelHandler(),viewer);
	}

	@Provides @Singleton @IntoMap @IntKey(PAN) static 
	Action pan(AWTViewer viewer) {
		XXXAction xaction=new XXXAction(Messages.getString("Euclides.41"), "/pan.png", new PanHandler(Messages.getString("Euclides.41"), viewer), viewer);
		xaction.cursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
		return xaction;
	}
	@Provides @Singleton @IntoMap @IntKey(TRIANGLE) static 
	Action triangle(AWTViewer viewer) {
		return new XXXAction("Veelhoek", "/triangle.png", new AddPolygonHandler("Veelhoek"),viewer);
	}

	@Provides @Singleton @IntoMap @IntKey(CIRCLE) static 
	Action circle(AWTViewer viewer) {
		return new CirkelAction(Messages.getString("Euclides.52"), "/circle.png", new AddCirkelHandler(),viewer);
	}

	@Provides @Singleton @IntoMap @IntKey(ARC) static 
	Action arc(AWTViewer viewer) {
		return new XXXAction("Boog", "/angle.png", new AddBoogHandler("Boog"),viewer);
	}

	@Provides @Singleton @IntoMap @IntKey(MIDPOINT) static 
	Action midpoint(AWTViewer viewer) {
		return new XXXAction(Messages.getString("Euclides.54"), "/midpoint.png", new AddMiddelPuntHandler(),viewer);
	}
	
	@Provides @Singleton @IntoMap @IntKey(BISECTRICE) static 
	Action bissectrice(AWTViewer viewer) {
		return new XXXAction(Messages.getString("Euclides.60"), "/bissectrice.png", new AddBissectriceHandler(),viewer);
	}
	@Provides @Singleton @IntoMap @IntKey(MIRROR) static 
	Action mirror(AWTViewer viewer) {
		return new XXXAction(Messages.getString("Euclides.62"), "/mirror.png", new AddSpiegelHandler(),viewer);
	}
	@Provides @Singleton @IntoMap @IntKey(CONIC_SECTION) static 
	Action conic(AWTViewer viewer) {
		return new XXXAction("Kegelsnede", "/quadric.png", new AddKegelsnedeHandler("Kegelsnede"),viewer);
	}
	@Provides @Singleton @IntoMap @IntKey(FOCUS) static 
	Action focus(AWTViewer viewer) {
		return new XXXAction("Brandpunt", "/quadric.png", new AddFocusHandler(), viewer);
	}
	@Provides @Singleton @IntoMap @IntKey(LOCUS) static 
	Action locus(AWTViewer viewer) {
		return new XXXAction("Meetkundige plaats", "/objecttracker.png", new AddLocusHandler("Meetkundige plaats"), viewer);
	}
	@Provides @Singleton @IntoMap @IntKey(TANGENT) static 
	Action tangent(AWTViewer viewer) {
		return new XXXAction("Raaklijn", "/line.png", new AddRaakLijnHandler(), viewer);
	}

	@Provides @Singleton @IntoMap @IntKey(POLELINE) static 
	Action poleline(AWTViewer viewer) {
		return new XXXAction("Poollijn", "/line.png", new AddPoollijnHandler(), viewer);
	}
	
	@Provides @Singleton @IntoMap @IntKey(DISTANCE) static 
	Action distance(AWTViewer viewer) {
		return new XXXAction(Messages.getString("Euclides.88"), "/distance.png", new AfstandHandler(Messages.getString("Euclides.90")), viewer);
	}
	@Provides @Singleton @IntoMap @IntKey(AREA) static 
	Action area(AWTViewer viewer) {
		return new XXXAction(Messages.getString("Euclides.91"), "/area.png", new OppHandler(Messages.getString("Euclides.93")), viewer);
	}
	@Provides @Singleton @IntoMap @IntKey(ANGLE) static 
	Action angle(AWTViewer viewer) {
		return new XXXAction(Messages.getString("Euclides.85"), "/angle2.png", new HoekHandler(Messages.getString("Euclides.85")), viewer);
	}
	@Provides @Singleton @IntoMap @IntKey(VECTOR) static 
	Action vector(AWTViewer viewer) {
		return new XXXAction("Vector", "/arrow.png", new VectorHandler("Vector"), viewer);
	}

	@Provides @Singleton @IntoMap @IntKey(FORMULA) static 
	Action formula(AWTViewer viewer, Definitions definitions) {
		FormuleHandler formule =	new FormuleHandler("Definitie", definitions);
		return new XXXAction("Definitie", "/function.png", formule, viewer);
	}
	@Provides @Singleton @IntoMap @IntKey(TEXT) static 
	Action text(AWTViewer viewer) {
		TextHandler text = new TextHandler(Messages.getString("AddLoodLijnHandler.1"));
		return new XXXAction("Tekst", "/showname.png", text, viewer);
	}
	@Provides @Singleton @IntoMap @IntKey(TRAIL) static 
	Action trail(AWTViewer viewer) {
		return new TrailAction(Messages.getString("Euclides.44"), viewer);
	}
	@Provides @Singleton @IntoMap @IntKey(CIRCLE_WITH_RADIUS) static 
	Action circle_with_radius(AWTViewer viewer) {
		return new XXXAction("Cirkel met opgegeven straal", "/fixedcircle.png", new CirkelRadiusHandler(Messages.getString("AddCirkelHandler.0")), viewer);
	}
	
/* 		actions.set(DISTANCE,new XXXAction(, viewer));
		actions.set(AREA,new XXXAction(, viewer));

*/	
}
