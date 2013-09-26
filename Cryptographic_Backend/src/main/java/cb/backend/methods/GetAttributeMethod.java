package cb.backend.methods;

import cb.backend.Method;

public abstract class GetAttributeMethod implements Method {
	static public class ReturnValue {
		public final String attributeValue;

        public ReturnValue(String attributeValue) {
            this.attributeValue = attributeValue;
        }
    }
	static public class Args {
		public final String attribute;
		public final int handler;

        public Args(String attribute, int handler) {
            this.attribute = attribute;
            this.handler = handler;
        }
    }
	protected final String attribute;
	protected final int handler;
	
	public GetAttributeMethod(Args args) {
		this.attribute = args.attribute;
		this.handler = args.handler;
	}

}
