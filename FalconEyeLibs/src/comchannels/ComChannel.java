
package comchannels;

import messages.tcp.network.NetworkTcpMessage;
import objects.Student;
import util.Constants;

public interface ComChannel extends Constants {
    
    public void sendMessage( NetworkTcpMessage msg );
    
    public void setAttachment( Student s );
}
