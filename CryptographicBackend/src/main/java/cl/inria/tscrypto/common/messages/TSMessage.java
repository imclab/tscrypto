package cl.inria.tscrypto.common.messages;

import cl.inria.tscrypto.common.datatypes.JsonFormat;
import cl.inria.tscrypto.common.utils.Util;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * Threshold Cryptography abstract message.
 * Composed of: type, version, created_at, payload (useful data to be defined by inherited types)
 * @author acansado
 *
 */
public abstract class TSMessage extends JsonFormat {

	String type;
	String version;
	long createdAt;

	public TSMessage() {
	}

	public TSMessage(String type, String version) {
		this.type = type;
		this.version = version;
		this.createdAt = System.currentTimeMillis();
	}
	
    @SuppressWarnings("unchecked")
	public static <T> T fromJson(String jsonText) throws JsonParseException {

        Gson gson = Util.GsonFactory(true);

        return (T) gson.fromJson(jsonText, TSMessage.class);
    }

	/**
	 * type getter
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * createdAt getter
	 * @return
	 */
	public long getCreatedAt(){
		return createdAt;
	}
}
