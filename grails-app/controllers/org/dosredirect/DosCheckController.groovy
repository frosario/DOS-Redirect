package org.dosredirect

class DosCheckController {

    AntiDOSService antiDOSService

    def index() { 
        def ip = request.getRemoteAddr()
        def visitRecord = antiDOSService.requestCountFor(ip)
        if (antiDOSService.ipThresholdReached(visitRecord)) {
            antiDOSService.changeCount("increase",visitRecord)
            redirect(url: grailsApplication.config.antiDOS.redirectURL)
        }
        else {
            render "Not a DOS Attack"
        }
    }
}

