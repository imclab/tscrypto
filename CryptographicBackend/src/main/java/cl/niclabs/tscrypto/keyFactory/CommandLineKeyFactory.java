/*
    Copyright 2013 NIC Chile Research Labs
    This file is part of TsCrypto.

    TsCrypto is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    TsCrypto is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with TsCrypto.  If not, see <http://www.gnu.org/licenses/>.
 */

package cl.niclabs.tscrypto.keyFactory;

import org.apache.commons.cli.*;

import cl.niclabs.tscrypto.common.datatypes.KeyInfo;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.common.utils.Util;
import cl.niclabs.tscrypto.keyFactory.algorithm.KeyFactory;

/**
 * Command Line KeyFactory.
 * Off-line TS-RSA keys factory.
 * Must be provided with node's publickeys in order to save
 * their encrypted keyshares in a file
 * @author acansado
 *
 */
public class CommandLineKeyFactory {

	private static Options options = null; // Command line options

	private static final int DEFAULT_KEYSIZE = 1024;
	
	private static final String KEYSIZE_OPTION = "keysize";
	private static final String K_OPTION = "k";
	private static final String L_OPTION = "l";
	private static final String OUTPUT_FILE_OPTION = "out";
	private static final String PUBLICKEYS_OPTION = "rsakeys";

	private CommandLine cmd = null; // Command Line arguments

	private String outputFile = "";
	private int keysize = DEFAULT_KEYSIZE;
	private int k;
	private int l;

	private String[] rsaKeys;

	static {
		options = new Options();
		options.addOption(KEYSIZE_OPTION, true, "Keysize, default is " + DEFAULT_KEYSIZE + " bits");
		options.addOption(K_OPTION, true, "Minimum number of players");
		options.addOption(L_OPTION, true, "Total number of players");
		options.addOption(OUTPUT_FILE_OPTION, true, "Output file prefix/path");
		options.addOption(PUBLICKEYS_OPTION, true, "Filenames of Public RSA keys");
	}

	/**
	 * Command Line KeyFactory
	 * @param args keysize, k, l, output_filename, public_keys
	 */
	public static void main(String[] args) {
		CommandLineKeyFactory cliProg = new CommandLineKeyFactory();
		cliProg.loadArgs(args);
		cliProg.run();
	}

	private void run() {
		// print basic info
		TSLogger.keyFactory.info("Generating keys with:");
		TSLogger.keyFactory.info("k=" + k);
		TSLogger.keyFactory.info("l=" + l);
		TSLogger.keyFactory.info("keysize=" + keysize);
		for (int id=0; id<rsaKeys.length; id++) {
			TSLogger.keyFactory.info("rsa key file" + id + "=" + rsaKeys[id]);
		}

		// generate keys
		KeyInfo key = KeyFactory.generateKeys(keysize, k, l);
		TSLogger.keyFactory.info("Done... writting key with alias " + key.getKeyMetaInfo().getAlias());
		
		// write to files
		try {
			Util.saveKeyToFile(key, outputFile, rsaKeys);
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	/**
	 * Validate and set command line arguments. Exit after printing usage if
	 * anything is astray
	 * 
	 * @param args
	 *            String[] args as featured in public static void main()
	 */
	private void loadArgs(String[] args) {
		CommandLineParser parser = new PosixParser();
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println("Error parsing arguments");
			e.printStackTrace();
			System.exit(1);
		}

		// Check for mandatory args

		if (!cmd.hasOption(KEYSIZE_OPTION) || !cmd.hasOption(K_OPTION)
				|| !cmd.hasOption(L_OPTION) || !cmd.hasOption(PUBLICKEYS_OPTION)) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar this_jar.jar", options);
			System.exit(1);
		}

		keysize = Integer.valueOf(cmd.getOptionValue(KEYSIZE_OPTION));
		k = Integer.valueOf(cmd.getOptionValue(K_OPTION));
		l = Integer.valueOf(cmd.getOptionValue(L_OPTION));
		rsaKeys = cmd.getOptionValue(PUBLICKEYS_OPTION).split(":");

		// Look for optional args.

		if (cmd.hasOption(OUTPUT_FILE_OPTION)) {
			outputFile = cmd.getOptionValue(OUTPUT_FILE_OPTION);
		}

	}

}
