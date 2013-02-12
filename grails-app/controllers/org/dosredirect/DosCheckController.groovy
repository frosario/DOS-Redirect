package org.dosredirect
import org.slf4j.LoggerFactory;
import org.slf4j.Logger


class DosCheckController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DosCheckController.class)
    AntiDOSService antiDOSService

    def index() { 
        def ip = request.getRemoteAddr()
        def visitRecord = antiDOSService.requestCountFor(ip)
        if (antiDOSService.ipThresholdReached(visitRecord)) {
            antiDOSService.changeCount("increase",visitRecord)
            try {
                def u = grailsApplication.config.antiDOS.redirectURL
            } catch (Exception e) {
                LOGGER.error("Exception: " + e.getMessage())
                def u = "http://192.168.1.1"
            } finally {
                redirect(url: u)
            }
        }
        else {
            antiDOSService.changeCount("increase",visitRecord)
        }
    }
}

