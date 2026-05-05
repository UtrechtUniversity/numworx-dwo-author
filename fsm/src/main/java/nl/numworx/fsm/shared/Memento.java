package nl.numworx.fsm.shared;

import java.io.IOException;

import fi.euclides.event.NameMapper;
import fi.euclides.model.Model;
import fi.euclides.persist.DataInput;
import fi.euclides.persist.DataOutput;
import fi.euclides.util.DefaultAdapter;

public class Memento extends fi.euclides.persist.Memento {
	public Model getModel() {
		return model;
	}

	public void readNames(DataInput names) {
		int i = 0;
		try {
			do {
				String name = names.readUTF();
				if (name != null) {
					tracker.getMapper().rename(readObjects.elementAt(i), name);
				}
			} while (++i < readObjects.size());
		} catch (IOException eof) {}
	}

	public void readAccepted(DataInput accepted) {
		try {
			do {
				int index = accepted.readInt();
				DefaultAdapter.getDefault(readObjects.elementAt(index)).put(Boolean.TRUE);
			} while(true);
		} catch(IOException eof) {};
	}

	public void writeNames(DataOutput dos) throws IOException {
		int cnt = 0;
		NameMapper mapper = tracker.getMapper();
		for(int i = 0; i < writeObjects.size(); i++) {
			String name = mapper.toString(writeObjects.elementAt(i));
			if (name != null  && !name.isEmpty()) {
				while(cnt++ < i) { dos.writeUTF(null); }
				dos.writeUTF(name);
			}
		}
	}

	public void writeAccepted(DataOutput dos) throws IOException {
		for(int i = 0; i < writeObjects.size(); i++) {
			boolean accept = Boolean.TRUE.equals(writeObjects.get(i).adapt(Boolean.class));
			if (accept) dos.writeInt(i);
		}
	}
}
