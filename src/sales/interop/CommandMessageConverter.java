package sales.interop;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.springframework.jms.support.converter.MessageConverter;

import sales.SalesApp;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class CommandMessageConverter implements MessageConverter {
    
    static Logger logger = Logger.getLogger(SalesApp.class.getName());

    @Override
    public Object fromMessage(Message mes) {
        
        CommandMessage msg = new CommandMessage();
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            MapMessage mm = (MapMessage) mes;
            if (msg != null) {
                msg.setCommand(mm.getString("Command"));
                
                if (mm.getString("Data") != null) {
                    msg.setData(decoder.decodeBuffer(mm.getString("Data")));
                }
                
            }
        } catch (Exception e) {
            logger.error(e);
        }
        
        return msg;
    }
    
    @Override
    public Message toMessage(Object mesobj, Session session) {
        
        MapMessage mm2 = null;
        CommandMessage msg = (CommandMessage) mesobj;
        BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        if (mesobj != null) {
            try {
                mm2 = session.createMapMessage();
                mm2 = session.createMapMessage();
                mm2.setString("Command", msg.getCommand());
                if (msg.getData() != null) {
                    mm2.setString("Data", encoder.encode(msg.getData()));
                }
                return mm2;
                
            } catch (Exception e) {
            }
        }
        return mm2;
    }
}
