package org.dosredirect
import org.dosredirect.*

class DosCheckController {

    def index() { 
        def a = new AntiDOSService()
        def b = a.requestCountFor("1.1.1.1","increase")
        render b.visits
        //render request.getHeader("Client-IP")
    }
}
