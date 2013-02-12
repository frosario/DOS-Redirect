package org.dosredirect

class DosFilterFilters {

    def filters = {
        all(controller:'*', action:'*') {
            before = {
                def dosCheckController = new DosCheckController()
                dosCheckController.index()
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
