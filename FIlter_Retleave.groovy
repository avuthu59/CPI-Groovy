import groovy.util.XmlParser
import groovy.xml.XmlUtil
import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    // Parse the XML document
    def xml = message.getBody(String)
    def toDate = message.getProperty("EndDate")
    def list = new XmlParser().parseText(xml)

    list.CompoundEmployee.each { employee ->
        def personIdExternal = employee.person.person_id_external.text()
        println "Person ID External: $personIdExternal"

        employee.person.each { person ->
            person.employment_information.job_information.each { job ->
                def eventReason = job.event_reason.text()
                println "Person ID External: $personIdExternal, Event Reason: $eventReason"
                if (eventReason == "RETLEAVE") {
                    // Remove the job_information node
                    println "Removing job_information for Person ID External: $personIdExternal"
                    job.parent().remove(job)  // Removes the job_information node
                }
            }
        }
    }

    message.setBody(XmlUtil.serialize(list))
    return message
}