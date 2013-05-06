package cb.backend;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MapKeyStorage implements KeyStorage {
	final static private KeyStorage instance = new MapKeyStorage();
	HashMap<Integer, String[]> store;
	
	public static KeyStorage getInstance() {
		return instance;
	}
	
	private MapKeyStorage() {
		store = new HashMap<Integer, String[]>();
	}
	
	@Override
	public int storeKeyPair(String id, String label, String keyType,
			int keySize, KeyPair pair) {
		
		X509EncodedKeySpec publicSpec = 
				new X509EncodedKeySpec(pair.getPublic().getEncoded());
		PKCS8EncodedKeySpec privateSpec =
				new PKCS8EncodedKeySpec(pair.getPrivate().getEncoded());
		
		String[] vals = {
				id, 
				label, 
				keyType, 
				Integer.toString(keySize), 
				Signer.bytesToHex(publicSpec.getEncoded()),
				Signer.bytesToHex(privateSpec.getEncoded())
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
		byte[] byteKey = Signer.hexToBytes(store.get(handler)[5]);
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
		byte[] byteKey = Signer.hexToBytes(store.get(new Integer(handler))[4]);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(byteKey);
		
		try {
			KeyFactory kf = KeyFactory.getInstance(store.get(handler)[2]);
			return kf.generatePublic(spec);
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public int findHandler(String id) throws Exception {
		Set<Map.Entry<Integer, String[]>> entries = store.entrySet();
		
		Iterator<Map.Entry<Integer, String[]>> it = entries.iterator();
		while(it.hasNext()) {
			Map.Entry<Integer, String[]> entry = it.next();
			if (entry.getValue()[0].equals(id))
				return entry.getKey().intValue();
		}
		
		throw new Exception("Llave no encontrada.");
	}

	@Override
	public String getAttribute(String attr, int handler) throws Exception {
		String[] values = store.get(new Integer(handler));
		if (attr.equals("id")) {
			return values[0];
		}
		else if (attr.equals("label")) {
			return values[1];
		}
		else if (attr.equals("keyType")) {
			return values[2];
		}
		else if (attr.equals("keySize")) {
			return values[3];
		}
		else {
			throw new Exception("Atributo no valido.");
		}
	}

}
