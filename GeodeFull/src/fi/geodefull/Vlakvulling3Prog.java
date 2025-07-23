package fi.geodefull;




public class Vlakvulling3Prog extends TekenApplet3D
{	
	double zijde;
	int lengte,breedte;
	
	public void tekenprogramma()
	{	zijde = 30;
		lengte = 6;
		breedte = 3;
		stap(-220,150);
		for(int i=0 ; i<breedte ; i++)
		{	rij(zijde,lengte);
		}
	}
	void driehoek(double z)
	{	vulAan("blauw");
		for(int i=0 ; i<3 ; i++)
		{	vooruit(z);
			rechts(120);
		}
		vulUit();
	}
	void vierkant(double z)
	{	vulAan("groen");
		for(int i=0 ; i<4 ; i++)
		{	vooruit(z);
			rechts(90);
		}
		vulUit();
	}
	void zeshoek(double z)
	{	vulAan("geel");
		for(int i=0 ; i<6 ; i++)
		{	vooruit(z);
			rechts(60);
		}
		vulUit();
	}
	void twaalfhoek(double z)
	{	zeshoek(z);
		for(int i=0 ; i<6 ; i++)
		{	links(90);
			vierkant(z);
			rechts(90);
			vooruit(z);
			links(90);
			driehoek(z);
			rechts(150);
		}
	}	
	void rij(double z, int len)
	{	links (60);
		for(int j=0 ; j<2 ; j++)
		{	for(int i=0 ; i<len ; i++)
			{	twaalfhoek(z);
				rechts(120);
				vooruit(z);
				rechts(30);
				vooruit(z);
				rechts(30);
				vooruit(z);
				rechts(180);
			}
			vooruit(z);
			links(90);
			vooruit(z);
			links(90);
		}
		penUit();
		links(90); vooruit(z);
		links(30); vooruit(z);
		links(30); vooruit(z);
		rechts(90);vooruit(z);
		links(60); vooruit(z);
		links(60); vooruit(z);
		links(120);
		penAan();
	}
}
