package net.survival.multiplayer;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ClientMessage extends Message
{
    private static final int MESSAGE_SIGNATURE = 0xDEADBEEF;
    private static final byte MESSAGE_TYPE_LOGIN = 1;
    private static final byte MESSAGE_TYPE_LOGOUT = 2;
    private static final byte MESSAGE_TYPE_CHUNK = 3;
    private static final byte MESSAGE_TYPE_SET_PROPERTY = 4;
    
    private LoginMessage loginMessage;
    private LogoutMessage logoutMessage;
    private ChunkFragmentMessage chunkFragmentMessage;
    private ArrayList<SetPropertyMessage> setPropertyMessages;

    private ClientMessage() {}

    @Override
    public void serialize(ByteBuffer buffer) {
        buffer.putInt(MESSAGE_SIGNATURE);
        
        if (loginMessage != null) {
            buffer.put(MESSAGE_TYPE_LOGIN);
        }
        else if (logoutMessage != null) {
            buffer.put(MESSAGE_TYPE_LOGOUT);
        }
        else if (chunkFragmentMessage != null) {
            buffer.put(MESSAGE_TYPE_CHUNK);
        }
        else if (setPropertyMessages != null) {
            buffer.put(MESSAGE_TYPE_SET_PROPERTY);
        }
    }
    
    @Override
    public boolean deserialize(ByteBuffer buffer) {
        reset();
        
        int signature = buffer.getInt();
        if (signature != MESSAGE_SIGNATURE)
            return false;
        
        byte messageType = buffer.get();
        
        if (messageType == MESSAGE_TYPE_LOGIN) {
            LoginMessage message = new LoginMessage();
            if (message.deserialize(buffer))
                loginMessage = message;
            else
                return false;
        }
        else if (messageType == MESSAGE_TYPE_LOGOUT) {
            LogoutMessage message = new LogoutMessage();
            if (message.deserialize(buffer))
                logoutMessage = message;
            else
                return false;
        }
        else if (messageType == MESSAGE_TYPE_CHUNK) {
            ChunkFragmentMessage message = new ChunkFragmentMessage();
            if (message.deserialize(buffer))
                chunkFragmentMessage = message;
            else
                return false;
        }
        else if (messageType == MESSAGE_TYPE_SET_PROPERTY) {
            return false;
        }
        
        return true;
    }
    
    public LoginMessage getLoginMessage() {
        return loginMessage;
    }
    
    public LogoutMessage getLogoutMessage() {
        return logoutMessage;
    }

    public Iterable<SetPropertyMessage> getSetPropertyMessages() {
        return setPropertyMessages;
    }
    
    private void reset()
    {
        loginMessage = null;
        logoutMessage = null;
        chunkFragmentMessage = null;
        setPropertyMessages = null;
    }
}