package freddie
import freddie.*

class DosCheckController {

    def index() { 
        def a = new AntiDOSService()
        def b = a.requestCountFor("1.1.1.1")
        render b.lastVisit
        //render request.getHeader("Client-IP")
    }
}
