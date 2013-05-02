package cb;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Random;

public class MapKeyStorage implements KeyStorage {
	static final KeyStorage instance = new MapKeyStorage();
	HashMap<Integer, String[]> store;
	
	public static KeyStorage getInstance() {
		return instance;
	}
	
	private MapKeyStorage() {
		store = new HashMap<Integer, String[]>();
	}
	
	final private String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b: bytes) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}
	
	final private byte[] hexToBytes(String s) {
		
		int len = s.length();
		byte[] data = new byte[len/2];
		
		for (int i=0; i < len; i+=2) {
			data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
					+ Character.digit(i+1, 16));
		}
		
		return data;
	}
	@Override
	public int storeKeyPair(String id, String label, String keyType,
			int keySize, KeyPair pair) {
		String[] vals = {
				id, 
				label, 
				keyType, 
				Integer.toString(keySize), 
				bytesToHex(pair.getPublic().getEncoded()),
				bytesToHex(pair.getPrivate().getEncoded())
		};
		
		Random random = new Random();
		Integer handle;
		do {
			handle = random.nextInt();
		} while (store.containsKey(handle));
		
		store.put(handle, vals);
		
		return handle.intValue();
	}

	@Override
	public PrivateKey getPrivateKey(int _handler) {
		Integer handler = _handler;
		byte[] byteKey = hexToBytes(store.get(handler)[5]);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(byteKey);
		
		try {
			KeyFactory kf = KeyFactory.getInstance(store.get(handler)[2]);
			return kf.generatePrivate(spec);
		} catch (Exception e) {
			return null; // No debería llegar acá nunca.
		}
		
	}

	@Override
	public PublicKey getPublicKey(int _handler) {
		Integer handler = _handler;
		byte[] byteKey = hexToBytes(store.get(new Integer(handler))[4]);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(byteKey);
		
		try {
			KeyFactory kf = KeyFactory.getInstance(store.get(handler)[2]);
			return kf.generatePublic(spec);
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public int findHandler(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

}
