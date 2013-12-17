package cl.inria.tscrypto.sigDealer.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SimpleFileReader {

	public static String readAll(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));

		StringBuffer strBuffer = new StringBuffer();
		String line;
		while((line = reader.readLine()) != null)
			strBuffer.append(line);
		
		return strBuffer.toString();
	}

}
