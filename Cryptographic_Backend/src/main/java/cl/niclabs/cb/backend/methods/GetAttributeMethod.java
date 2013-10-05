package cl.niclabs.cb.backend.methods;

import cl.niclabs.cb.backend.Method;

public abstract class GetAttributeMethod implements Method {
	static public class ReturnValue {
		public final String attributeValue;

        public ReturnValue(String attributeValue) {
            this.attributeValue = attributeValue;
        }
    }
	static public class Args {
		public final String attribute;
		public final String handler;

        public Args(String attribute, String handler) {
            this.attribute = attribute;
            this.handler = handler;
        }
    }
	protected final String attribute;
	protected final String handler;
	
	public GetAttributeMethod(Args args) {
		this.attribute = args.attribute;
		this.handler = args.handler;
	}

}
