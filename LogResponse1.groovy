import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import com.sap.it.api.mapping.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;


def Message processData(Message message) {

	map = message.getProperties();
//	def employeeCount=map.get("P_employeeCount");
	property_ENABLE_PAYLOAD_LOGGING = map.get("ENABLE_PAYLOAD_LOGGING");
	if (property_ENABLE_PAYLOAD_LOGGING.toUpperCase().equals("TRUE")) {
		def header = message.getHeaders() as String;
		def body = message.getBody(java.lang.String) as String;
		
		String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
		String logTitle = timeStamp + "- Get Current Effective Records -";
		  
		def messageLog = messageLogFactory.getMessageLog(message);
		if (messageLog != null) {
			messageLog.addAttachmentAsString(logTitle, body, "text/xml");
		}
	}
	return message;
}