//Groovy script you can use in SAP Cloud Platform Integration (CPI) to parse your XML input, 
//loop through each <CompoundEmployee>, then within that, loop through <employment_information>/<job_information>, and remove the entire <employment_information> block if employment_type = 23.
import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.*

Message processData(Message message) {
    def body = message.getBody(String)
    def xml = new XmlParser().parseText(body)

    // Collect all CompoundEmployee nodes to remove
    def toRemove = xml.'CompoundEmployee'.findAll { ce ->
        ce.person.'employment_information'.any { ei ->
            ei.'job_information'.'employment_type'.text() == '23'
        }
    }

    // Remove those CompoundEmployee blocks
    toRemove.each { ce ->
        xml.remove(ce)
    }

    // Convert back to XML string
    def writer = new StringWriter()
    def printer = new XmlNodePrinter(new PrintWriter(writer))
    printer.setPreserveWhitespace(true)
    printer.print(xml)

    message.setBody(writer.toString())
    return message
}
