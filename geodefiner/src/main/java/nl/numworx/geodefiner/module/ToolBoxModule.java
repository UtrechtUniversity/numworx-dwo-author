package nl.numworx.geodefiner.module;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntKey;
import dagger.multibindings.IntoMap;
import fi.euclides.event.AddBissectriceHandler;
import fi.euclides.event.AddBoogHandler;
import fi.euclides.event.AddBoogHandler2;
import fi.euclides.event.AddFocusHandler;
import fi.euclides.event.AddKegelsnedeHandler;
import fi.euclides.event.AddLijnHandler;
import fi.euclides.event.AddLocusHandler;
import fi.euclides.event.AddLoodLijnHandler;
import fi.euclides.event.AddMiddelPuntHandler;
import fi.euclides.event.AddParallelHandler;
import fi.euclides.event.AddPoollijnHandler;
import fi.euclides.event.AddPuntHandler;
import fi.euclides.event.AddRaakLijnHandler;
import fi.euclides.event.AddSpiegelHandler;
import fi.euclides.event.EventHandler;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.proof.AfstandHandler;
import fi.euclides.proof.OppHandler;
import fi.euclides.proof.VectorHandler;
import fi.euclides.swing.AWTViewer;
import fi.euclides.swing.PanHandler;
import fi.euclides.swing.XXXAction;
import fi.euclides.util.Messages;
import nl.numworx.geodefiner.AddHoekPuntHandler;
import nl.numworx.geodefiner.CirkelRadiusHandler;
import nl.numworx.geodefiner.ColorHandler;
import nl.numworx.geodefiner.DashHandler;
import nl.numworx.geodefiner.Definitions;
import nl.numworx.geodefiner.FormuleHandler;
import nl.numworx.geodefiner.TextHandler;
import nl.numworx.geodefiner.TrailAction;
import nl.numworx.geodefiner.common.AddCirkelHandler;
import nl.numworx.geodefiner.common.AddPolygonHandler;
import nl.numworx.geodefiner.common.AddSnapPuntHandler;
import nl.numworx.geodefiner.common.FilteredDestroyHandler;
import nl.numworx.geodefiner.common.GeoTriangleHandler;
import nl.numworx.geodefiner.common.HoekHandler;
import nl.numworx.geodefiner.common.Instance;
import nl.numworx.geodefiner.common.ResetHandler;
import nl.numworx.geodefiner.common.Tools;
import nl.numworx.geodefiner.common.UIShim;
import nl.numworx.geodefiner.common.ZoomInHandler;
import nl.numworx.geodefiner.common.ZoomOutHandler;
import nl.numworx.geodefiner.tools.CirkelAction;
import nl.numworx.geodefiner.tools.PuntAction;
import nl.numworx.geodefiner.tools.XXXXAction;
import nl.numworx.geodefiner.ui.AngleModel;
import nl.numworx.geodefiner.ui.CircleModel;
import nl.numworx.geodefiner.ui.DashModel;
import nl.numworx.geodefiner.ui.LineModel;
import nl.numworx.geodefiner.ui.PointModel;
import nl.numworx.geodefiner.ui.RayModel;
import nl.numworx.geodefiner.ui.SegmentModel;
import nl.numworx.geodefiner.ui.TextModel;
import nl.numworx.geodefiner.ui.TriangleModel;
import nl.numworx.geodefiner.ui.UIEditor;

@Module
public abstract class ToolBoxModule implements Tools {

	@Provides @Singleton @IntoMap @IntKey(RESET) static 
	Action reset(Instance instance, AWTViewer viewer) {
		ResetHandler resetter = new ResetHandler(Messages.getString("ToolBoxModule.0"), instance); //$NON-NLS-1$
		return new XXXAction(Messages.getString("ToolBoxModule.1"), "/reset.png", resetter, viewer); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Provides @Singleton @IntoMap @IntKey(DESTROY) static 
	Action destroy(Instance instance, AWTViewer viewer) {
		XXXAction xaction = new XXXAction(Messages.getString("Euclides.37"), "/delete.png", new FilteredDestroyHandler(instance),viewer); //$NON-NLS-1$ //$NON-NLS-2$
		xaction.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
		return xaction;
	}

	@Provides @Singleton @IntoMap @IntKey(SELECTOR) static 
	Action selector(Instance instance, AWTViewer viewer) {
		EventHandler selector = instance.selector;
		XXXAction xaction = new XXXAction(Messages.getString("Euclides.35"), "/move.png", selector, viewer); //$NON-NLS-1$ //$NON-NLS-2$
		xaction.cursor = Cursor.getDefaultCursor();
		return xaction;
	}
	
	@Provides @Singleton @Named("point") static
	UIShim<Destroyable, UIEditor> getpointShim(AWTViewer viewer, PointModel model, Instance instance) {
		return new UIShim<>(model, instance.getStateConfiguration(), viewer);
	}
		
	@Provides @Singleton @IntoMap @IntKey(POINT) static 
	Action point(AWTViewer viewer, @Named("point") UIShim<Destroyable, UIEditor> shim, Instance instance) {
		return new PuntAction(Messages.getString("Euclides.46"), "/point.png", new AddSnapPuntHandler(shim),viewer, shim); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Provides @Singleton @IntoMap @IntKey(LINE) static 
	Action line(AWTViewer viewer, LineModel model, Instance instance, @Named("point") UIShim<Destroyable, UIEditor> chain) {
		UIShim<Destroyable, UIEditor> shim = new UIShim<>(model, instance.getStateConfiguration(), viewer);
		shim.setChain(chain);
		return new XXXXAction(Messages.getString("Euclides.50"), "/line.png", new AddLijnHandler(AddLijnHandler.LINE),viewer, shim); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Provides @Singleton @IntoMap @IntKey(SEGMENT) static 
	Action segment(AWTViewer viewer, SegmentModel model, Instance instance, @Named("point") UIShim<Destroyable, UIEditor> chain) {
		UIShim<Destroyable, UIEditor> shim = new UIShim<>(model, instance.getStateConfiguration(), viewer);
		shim.setChain(chain);
		return new XXXXAction(Messages.getString("Euclides.48"), "/segment.png", new AddLijnHandler(AddLijnHandler.SEGMENT),viewer, shim); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Provides @Singleton @IntoMap @IntKey(HALFLINE) static 
	Action halfline(AWTViewer viewer, RayModel model, Instance instance, @Named("point") UIShim<Destroyable, UIEditor> chain) {
		UIShim<Destroyable, UIEditor> shim = new UIShim<>(model, instance.getStateConfiguration(), viewer);
		shim.setChain(chain);
		return new XXXXAction(Messages.getString("Euclides.49"), "/ray.png", new AddLijnHandler(AddLijnHandler.RAY),viewer, shim); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Provides @Singleton @IntoMap @IntKey(PERPENDICULAR) static 
	Action perpendicular(AWTViewer viewer, LineModel model, Instance instance, @Named("point") UIShim<Destroyable, UIEditor> chain) {
		UIShim<Destroyable, UIEditor> shim = new UIShim<>(model, instance.getStateConfiguration(), viewer);
		shim.setChain(chain);
		return new XXXXAction(Messages.getString("Euclides.56"), "/plumb.png", new AddLoodLijnHandler(),viewer, shim); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Provides @Singleton @IntoMap @IntKey(PARALLEL) static 
	Action parallel(AWTViewer viewer, LineModel model, Instance instance, @Named("point") UIShim<Destroyable, UIEditor> chain) {
		UIShim<Destroyable, UIEditor> shim = new UIShim<>(model, instance.getStateConfiguration(), viewer);
		shim.setChain(chain);
		return new XXXXAction(Messages.getString("Euclides.58"), "/parallel.png", new AddParallelHandler(),viewer,shim); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Provides @Singleton @IntoMap @IntKey(PAN) static 
	Action pan(AWTViewer viewer) {
		XXXAction xaction=new XXXAction(Messages.getString("Euclides.41"), "/pan.png", new PanHandler(Messages.getString("Euclides.41"), viewer), viewer); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		xaction.cursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
		return xaction;
	}
	@Provides @Singleton @IntoMap @IntKey(TRIANGLE) static 
	Action triangle(AWTViewer viewer, TriangleModel model, Instance instance, @Named("point") UIShim<Destroyable, UIEditor> chain) {
		UIShim<Destroyable, UIEditor> shim = new UIShim<>(model, instance.getStateConfiguration(), viewer);
		shim.setChain(chain);
		return new XXXXAction(Messages.getString("ToolBoxModule.22"), "/triangle.png", new AddPolygonHandler(Messages.getString("ToolBoxModule.24")),viewer, shim); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	@Provides @Singleton @IntoMap @IntKey(CIRCLE) static 
	Action circle(AWTViewer viewer, CircleModel model, Instance instance, @Named("point") UIShim<Destroyable, UIEditor> chain) {
		UIShim<Destroyable, UIEditor> shim = new UIShim<>(model, instance.getStateConfiguration(), viewer);
		shim.setChain(chain);
		return new CirkelAction(Messages.getString("Euclides.52"), "/circle.png", new AddCirkelHandler(),viewer, shim); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Provides @Singleton @IntoMap @IntKey(ARC) static 
	Action arc(AWTViewer viewer, CircleModel model, Instance instance, @Named("point") UIShim<Destroyable, UIEditor> chain) {
		UIShim<Destroyable, UIEditor> shim = new UIShim<>(model, instance.getStateConfiguration(), viewer);
		shim.setChain(chain);
		return new XXXXAction(Messages.getString("ToolBoxModule.27"), "/angle.png", new AddBoogHandler2(Messages.getString("ToolBoxModule.29")),viewer, shim); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	@Provides @Singleton @IntoMap @IntKey(MIDPOINT) static 
	Action midpoint(AWTViewer viewer, PointModel model, Instance instance) {
		UIShim<Destroyable, UIEditor> shim = new UIShim<>(model, instance.getStateConfiguration(), viewer);
		return new XXXXAction(Messages.getString("Euclides.54"), "/midpoint.png", new AddMiddelPuntHandler(),viewer, shim); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@Provides @Singleton @IntoMap @IntKey(BISECTRICE) static 
	Action bissectrice(AWTViewer viewer, LineModel model, Instance instance, @Named("point") UIShim<Destroyable, UIEditor> chain) {
		UIShim<Destroyable, UIEditor> shim = new UIShim<>(model, instance.getStateConfiguration(), viewer);
		shim.setChain(chain);
		return new XXXXAction(Messages.getString("Euclides.60"), "/bissectrice.png", new AddBissectriceHandler(),viewer, shim); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Provides @Singleton @IntoMap @IntKey(MIRROR) static 
	Action mirror(AWTViewer viewer) {
		return new XXXAction(Messages.getString("Euclides.62"), "/mirror.png", new AddSpiegelHandler(),viewer); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Provides @Singleton @IntoMap @IntKey(CONIC_SECTION) static 
	Action conic(AWTViewer viewer) {
		return new XXXAction(Messages.getString("ToolBoxModule.36"), "/quadric.png", new AddKegelsnedeHandler(Messages.getString("ToolBoxModule.38")),viewer); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	@Provides @Singleton @IntoMap @IntKey(FOCUS) static 
	Action focus(AWTViewer viewer) {
		return new XXXAction(Messages.getString("ToolBoxModule.39"), "/quadric.png", new AddFocusHandler(), viewer); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Provides @Singleton @IntoMap @IntKey(LOCUS) static 
	Action locus(AWTViewer viewer) {
		return new XXXAction(Messages.getString("ToolBoxModule.41"), "/objecttracker.png", new AddLocusHandler("Meetkundige plaats"), viewer); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	@Provides @Singleton @IntoMap @IntKey(TANGENT) static 
	Action tangent(AWTViewer viewer, LineModel model, Instance instance) {
		UIShim<Destroyable, UIEditor> shim = new UIShim<>(model, instance.getStateConfiguration(), viewer);
		return new XXXXAction(Messages.getString("ToolBoxModule.44"), "/line.png", new AddRaakLijnHandler(), viewer, shim); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Provides @Singleton @IntoMap @IntKey(POLELINE) static 
	Action poleline(AWTViewer viewer, LineModel model, Instance instance, @Named("point") UIShim<Destroyable, UIEditor> chain) {
		UIShim<Destroyable, UIEditor> shim = new UIShim<>(model, instance.getStateConfiguration(), viewer);
		shim.setChain(chain);
		return new XXXXAction(Messages.getString("ToolBoxModule.46"), "/line.png", new AddPoollijnHandler(), viewer,shim); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@Provides @Singleton @IntoMap @IntKey(DISTANCE) static 
	Action distance(AWTViewer viewer) {
		return new XXXAction(Messages.getString("Euclides.88"), "/distance.png", new AfstandHandler(Messages.getString("Euclides.90")), viewer); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	@Provides @Singleton @IntoMap @IntKey(AREA) static 
	Action area(AWTViewer viewer) {
		return new XXXAction(Messages.getString("Euclides.91"), "/area.png", new OppHandler(Messages.getString("Euclides.93")), viewer); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	@Provides @Singleton @IntoMap @IntKey(ANGLE) static 
	Action angle(AWTViewer viewer, AngleModel angleModel, Instance instance) {
		UIShim<Label, UIEditor> shim = new UIShim<>(angleModel, instance.getStateConfiguration(), viewer);
		HoekHandler handler = new HoekHandler(Messages.getString("Euclides.86"));
		return new XXXXAction(Messages.getString("Euclides.85"), "/angle2.png", handler, viewer, shim); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	@Provides @Singleton @IntoMap @IntKey(VECTOR) static 
	Action vector(AWTViewer viewer) {
		return new XXXAction(Messages.getString("ToolBoxModule.57"), "/arrow.png", new VectorHandler(Messages.getString("ToolBoxModule.59")), viewer); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	@Provides @Singleton @IntoMap @IntKey(FORMULA) static 
	Action formula(AWTViewer viewer, Definitions definitions) {
		FormuleHandler formule =	new FormuleHandler(Messages.getString("ToolBoxModule.60"), definitions); //$NON-NLS-1$
		return new XXXAction(Messages.getString("ToolBoxModule.61"), "/function.png", formule, viewer); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Provides @Singleton @IntoMap @IntKey(TEXT) static 
	Action text(AWTViewer viewer, TextModel textModel, Instance instance) {
		textModel.setDXY(6f,-5f);
		UIShim<Label, UIEditor> shim = new UIShim<>(textModel, instance.getStateConfiguration(), viewer);
		TextHandler text = new TextHandler(Messages.getString("AddLoodLijnHandler.1")); //$NON-NLS-1$
		return new XXXXAction(Messages.getString("ToolBoxModule.64"), "/showname.png", text, viewer, shim); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Provides @Singleton @IntoMap @IntKey(TRAIL) static 
	Action trail(AWTViewer viewer) {
		return new TrailAction(Messages.getString("Euclides.44"), viewer); //$NON-NLS-1$
	}
	@Provides @Singleton @IntoMap @IntKey(CIRCLE_WITH_RADIUS) static 
	Action circle_with_radius(AWTViewer viewer, CircleModel model, Instance instance, @Named("point") UIShim<Destroyable, UIEditor> chain) {
		UIShim<Destroyable, UIEditor> shim = new UIShim<>(model, instance.getStateConfiguration(), viewer);
		shim.setChain(chain);
		return new XXXXAction(Messages.getString("Euclides.104"), "/fixedcircle.png", new CirkelRadiusHandler(Messages.getString("AddCirkelHandler.0")), viewer,shim); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	@Provides @Singleton @IntoMap @IntKey(COLOR_PALETTE) static
	Action color_palette(Instance instance, AWTViewer viewer) {
		EventHandler handler = new ColorHandler(Messages.getString("ToolBoxModule.70"), instance.getStateConfiguration());		 //$NON-NLS-1$
		return new XXXAction(Messages.getString("ToolBoxModule.71"), "/colorpalette-active.png", handler, viewer); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@Provides @Singleton @IntoMap @IntKey(LINE_PALETTE) static 
	Action line_palette(Instance instance, AWTViewer viewer, DashModel model) {
		EventHandler handler = new DashHandler(Messages.getString("ToolBoxModule.73"), instance.getStateConfiguration(), model); //$NON-NLS-1$
		UIShim<Destroyable, UIEditor> shim = new UIShim<>(model, instance.getStateConfiguration(), viewer);
		return new XXXXAction(Messages.getString("ToolBoxModule.74"), "/dashedline-active.png", handler, viewer, shim); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@Provides @Singleton @IntoMap @IntKey(ANGLE_POINT) static
	Action anglePoint(Instance instance, AWTViewer viewer, AddHoekPuntHandler handler, PointModel model) {
		UIShim<Destroyable, UIEditor> shim = new UIShim<>(model, instance.getStateConfiguration(), viewer);
		return new XXXXAction("Punt onder hoek", "/anglepoint.png", handler, viewer,shim);		
	}
	
/* 		actions.set(DISTANCE,new XXXAction(, viewer));
		actions.set(AREA,new XXXAction(, viewer));

*/	
	@Provides @Singleton @IntoMap @IntKey(GEO_TRIANGLE) static
	Action geodriehoek(Instance instance, AWTViewer viewer) {
		GeoTriangleHandler handler = new GeoTriangleHandler(Messages.getString("ToolBoxModule.75"), instance.selector);
		return new XXXAction(Messages.getString("ToolBoxModule.75"), "/geodriehoekKnop.png", handler, viewer);
	}
	
	@Provides @Singleton @IntoMap @IntKey(ZOOM_IN) static
	Action zoomin(Instance instance, AWTViewer viewer) {
		ZoomInHandler handler = new ZoomInHandler(Messages.getString("ToolBoxModule.76")); 
		return new XXXAction(Messages.getString("ToolBoxModule.76"), "/magnify.png", handler, viewer);
	}

	@Provides @Singleton @IntoMap @IntKey(ZOOM_OUT) static
	Action zoomout(Instance instance, AWTViewer viewer) {
		ZoomOutHandler handler = new ZoomOutHandler(Messages.getString("ToolBoxModule.77")); 
		return new XXXAction(Messages.getString("ToolBoxModule.77"), "/minify.png", handler, viewer);
	}


}
