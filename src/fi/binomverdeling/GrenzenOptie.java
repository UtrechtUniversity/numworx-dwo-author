package fi.binomverdeling;

/**
 * Enumeratie om aan te geven welke kans bekeken wordt
 * LINKS is alle gevallen kleiner dan de grens links (bij twee grenzen) of alle gevallen kleiner dan de grens (bij één grens)
 * RECHTS is alle gevallen groter dan de grens rechts (bij twee grenzen) of alle gevallen groter dan de grens (bij één grens)
 * GELIJK is alle gevallen >= grens links en <= grens rechts (bij twee grenzen) of het geval P(X=grens) (bij één grens)
 */
public enum GrenzenOptie {
	LINKS, RECHTS, GELIJK;
}
