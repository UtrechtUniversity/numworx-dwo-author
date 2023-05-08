package fi.euclides.model;

import java.io.IOException;
import java.util.Vector;

import fi.euclides.event.NameMapper;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelDelegate;

public interface Codec {

	Punt readPunt() throws IOException;
	void writePunt(Punt punt) throws IOException;

	Numbers readNumber() throws IOException;
	void writeNumber(Numbers x) throws IOException;

	Destroyable readDestroyable() throws IOException;
	void writeDestroyable(Destroyable op) throws IOException;

	void writeVector(Vector v) throws IOException;
	Vector readVector() throws IOException;
	Lijn readLijn() throws IOException;
	void writeLijn(Lijn lijn) throws IOException;

	void writeUTF(String string) throws IOException;
	String readUTF() throws IOException;
	
	void writeDelegate(LabelDelegate delegate) throws IOException;
	LabelDelegate readDelegate() throws IOException;
	
	NameMapper getModel();
	
	void write(Destroyable[] source) throws IOException;
	Destroyable[] read(Destroyable[] dest) throws IOException;
}
