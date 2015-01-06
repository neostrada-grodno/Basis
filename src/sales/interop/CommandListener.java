package sales.interop;

import javax.jms.Message;
import javax.jms.MessageListener;
import org.apache.log4j.Logger;
import sales.SalesApp;
import sales.Workplace;

public class CommandListener implements MessageListener {
    
    private static Logger logger = Logger.getLogger(SalesApp.class.getName());
    private static CommandMessageConverter converter = new CommandMessageConverter();
    private static Workplace workplace;
    
    public CommandListener(Workplace workplace) {
        this.workplace = workplace;
    }
    
    public void onMessage(Message message) {
        
        try {
            if (message != null) {
                if (message instanceof CommandMessage) {
                    processMessage((CommandMessage) message);
                } else {
                    processMessage((CommandMessage) converter.fromMessage(message));
                }
            }
            
        } catch (Exception e) {
            logger.error(e);
        }
    }
    
    private void processMessage(CommandMessage msg) {
        
        try {
            if (msg.command.equals("AddNomenclature")) {
                workplace.addProductItem(new String(msg.getData(), "UTF-8"));
            } else if (msg.command.equals("Commit")) {
                workplace.commit(false);
            } else if (msg.command.equals("Clear")) {
                workplace.clear(false);
            }
            
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
