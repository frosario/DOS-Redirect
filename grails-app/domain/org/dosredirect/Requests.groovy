package org.dosredirect

class Requests {
    String ipaddress = ""
    Integer visits = 0
    Date lastVisit = new Date()

    static constraints = {
        ipaddress size: 7..15, blank: false, unique: true
        visits blank:false
        lastVisit blank: false
    }
}
