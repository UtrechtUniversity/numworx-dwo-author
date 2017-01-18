package fi.euclides.util;

public interface Adapter {
	<T> T adapt(Class<T> cls);

	
	Adapter NULL = new Adapter() {

		public <T> T adapt(Class<T> cls) {
			return null;
		}
		
	};
	
}
