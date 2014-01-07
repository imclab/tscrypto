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

package cl.niclabs.tscrypto.utils;

import java.io.File;

/**
 * This class contains the logic to generate the directories's hierarchy for the
 * system. By default, the system is located in /etc/threshold/ and can be
 * modified to be located in other path. The system could have repeated files.
 * This happen because the system has been made to function in different
 * machines. ¡¡¡¡IT MUST TO BE EXECUTED AS SUDO!!!!
 * 
 * The path structure is the following:
 * 
 * /etc/threshold
 * 
 * /conf - contains the configuration files for all the system
 * 
 * /encrypt - contains the public keys for all the components of the system.
 * This allow to the different components to send information in a secure way
 * 
 * /keys - contains the keys created by the keyDealer. This keys are encrypted
 * and they are send to the nodes, but the keyDealer always save a copy in case
 * that the users want move the keys physically, and not via the queues disposed
 * by the system
 * 
 * /log - contains the different logs of the system. Each file has a
 * distinguishing name. The amount of files depends of the amount of components
 * executed in the machine.
 * 
 * /nodes - contains the keys associated with the node located in the system.
 * The amount of keys generated depends of the nodes executed in the machine
 * 
 * /publicKeys - contains the public keys
 * 
 * /verifiers - contains the verifiers associated to the different keys. It's
 * made-up for:
 * 
 * /signatureDealer - contains the verifiers saved by the signatureDealer. The
 * signatureDealer obtains the keys from the data send by the keyDealer
 * 
 * /keyDealer - contains the same verifiers as the signatureDealer. The
 * difference leads in the fact that the user could want to send the information
 * in a different way than the system queues. For that reason the keyDealer
 * needs to save this data.
 * 
 * 
 */

public class DirectoriesHierarchy {

	private String rootPath = "/etc/threshold/";
	private String CONF = "conf/", 
						ENCRYPT = "encrypt/", 
						KEYS = "keys/", 
						LOG = "log/",
						NODES = "nodes/",
						PUBLIC_KEYS = "publicKeys/", 
						VERIFIERS = "verifiers/", 
						SIGNATURE_DEALER = "signatureDealer/", 
						KEY_DEALER = "keyDealer/";
	
	
	private static DirectoriesHierarchy _INSTANCE_ = new DirectoriesHierarchy();
	
	public DirectoriesHierarchy getInstance(){
		return _INSTANCE_;
	}
	
	/**
	 * Return a String where the root path is located
	 * 
	 * @return - the String where the root path is located
	 */
	public String getRootPath(){
		
		return this.rootPath;
		
	}
	/**
	 * 
	 * @param path - new path for the directories's hierarchy
	 */
	public void setRootPath(String path){
		
		String lastCharacter = path.substring(path.length()-1);
		
		if(lastCharacter.equals("/"))
			this.rootPath = path + "threshold/";
		else
			this.rootPath = path + "/" + "threshold/";
	}
	/**
	 * returns a String with the path of the conf directory relative to the root directory
	 * 
	 * @return - A String with the path of the conf directory relative to the root directory
	 */
	public String getConfPath(){
		
		return this.rootPath + this.CONF;
		
	}
	
	/**
	 * returns a String with the path of the encrypt directory relative to the root directory
	 * 
	 * @return - A String with the path of the encrypt directory relative to the root directory
	 */
	public String getEncryptPath(){
		
		return this.rootPath + this.ENCRYPT;
		
	}
	
	/**
	 * returns a String with the path of the keys directory relative to the root directory
	 * 
	 * @return - A String with the path of the keys directory relative to the root directory
	 */
	public String getKeysPath(){
		
		return this.rootPath + this.KEYS;
		
	}
	
	/**
	 * returns a String with the path of the log directory relative to the root directory
	 * 
	 * @return - A String with the path of the log directory relative to the root directory
	 */
	public String getLogPath(){
		
		return this.rootPath + this.LOG;
		
	}
	
	/**
	 * returns a String with the path of the nodes directory relative to the root directory
	 * 
	 * @return - A String with the path of the nodes directory relative to the root directory
	 */
	public String getNodesPath(){
		
		return this.rootPath + this.NODES;
		
	}
	
	/**
	 * returns a String with the path of the publicKeys directory relative to the root directory
	 * 
	 * @return - A String with the path of the publicKeys directory relative to the root directory
	 */
	public String getPublicKeysPath(){
		
		return this.rootPath + this.PUBLIC_KEYS;
		
	}
	
	/**
	 * returns a String with the path of the verifiers directory relative to the root directory
	 * 
	 * @return - A String with the path of the verifiers directory relative to the root directory
	 */
	public String getVerifiersPath(){
		
		return this.rootPath + this.VERIFIERS;
		
	}
	
	/**
	 * returns a String with the path of the signatureDealer directory relative to the root directory
	 * 
	 * @return - A String with the path of the signatureDealer directory relative to the root directory
	 */
	public String getSignatureDealerPath(){
		
		return this.rootPath + this.VERIFIERS + this.SIGNATURE_DEALER;
		
	}
	
	/**
	 * returns a String with the path of the keyDealer directory relative to the root directory
	 * 
	 * @return - A String with the path of the keyDealer directory relative to the root directory
	 */
	public String getKeyDealerPath(){
		
		return this.rootPath + this.VERIFIERS + this.KEY_DEALER;
		
	}

	/**
	 * create the directories's hierarchy for the system
	 */
	public void createHierarchy(){
		
		if(new File(getRootPath()).mkdirs())
			System.out.println("\tSuccess creation of "+ getRootPath());
		else
			System.out.println("\tError creating "+ getRootPath());
		if(new File(getConfPath()).mkdirs())
			System.out.println("\tSuccess creation of "+ getConfPath());
		else
			System.out.println("\tError creating "+ getConfPath());
		if(new File(getEncryptPath()).mkdirs())
			System.out.println("\tSuccess creation of "+ getEncryptPath());
		else
			System.out.println("\tError creating "+ getEncryptPath());
		if(new File(getKeysPath()).mkdirs())
			System.out.println("\tSuccess creation of "+ getKeysPath());
		else
			System.out.println("\tError creating "+ getKeysPath());
		if(new File(getLogPath()).mkdirs())
			System.out.println("\tSuccess creation of "+ getLogPath());
		else
			System.out.println("\tError creating "+ getLogPath());
		if(new File(getNodesPath()).mkdirs())
			System.out.println("\tSuccess creation of "+ getNodesPath());
		else
			System.out.println("\tError creating "+ getNodesPath());
		if(new File(getPublicKeysPath()).mkdirs())
			System.out.println("\tSuccess creation of "+ getPublicKeysPath());
		else
			System.out.println("\tError creating "+ getPublicKeysPath());
		if(new File(getVerifiersPath()).mkdirs())
			System.out.println("\tSuccess creation of "+ getVerifiersPath());
		else
			System.out.println("\tError creating "+ getVerifiersPath());
		if(new File(getSignatureDealerPath()).mkdirs())
			System.out.println("\tSuccess creation of "+ getSignatureDealerPath());
		else
			System.out.println("\tError creating "+ getSignatureDealerPath());
		if(new File(getKeyDealerPath()).mkdirs())
			System.out.println("\tSuccess creation of "+ getKeyDealerPath());
		else
			System.out.println("\tError creating "+ getKeyDealerPath());
		
	}
	
	public static void main(String args[]){
		
		DirectoriesHierarchy directoriesHierarchy =  new DirectoriesHierarchy();
		
		
		System.out.println("Original root path: " + directoriesHierarchy.getRootPath());
		directoriesHierarchy.createHierarchy();
		
	}

}
