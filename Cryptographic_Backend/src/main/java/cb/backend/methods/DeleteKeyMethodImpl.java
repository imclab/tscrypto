package cb.backend.methods;

import cb.backend.KeyStorage;
import cb.backend.MapKeyStorage;
import cb.backend.Method;
import cb.backend.ResponseMessage;

/**
 * Created with IntelliJ IDEA.
 * User: franchoco
 * Date: 25-09-13
 * Time: 07:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeleteKeyMethodImpl implements Method {

    final long handler;

    public DeleteKeyMethodImpl(long handler) {
        this.handler = handler;
    }

    @Override
    public ResponseMessage execute() {
        KeyStorage keyStorage = MapKeyStorage.getInstance();
        try {
            keyStorage.deleteKeyPair(handler);
            return ResponseMessage.OKMessage(null);
        } catch (Exception e) {
            return ResponseMessage.ErrorMessage(e.getMessage());
        }
    }
}
