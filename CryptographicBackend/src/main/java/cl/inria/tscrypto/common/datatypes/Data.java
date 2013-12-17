package cl.inria.tscrypto.common.datatypes;

public interface Data {

	public String getString(String key);

	public long getLong(String key);

	public int getInt(String key);

	public Data getData(String key);

	public void put(String key, Object value);

}
