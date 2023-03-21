package com.shiva.notesapp.model

import android.icu.text.CaseMap.Title

data class NotesModel(
    var id : String ,
    var title: String,
    var content :String,
    var date:Long,
    var color : Int
) {
    constructor():this(
        "",
        "",
        "",
        0,
        0,

    )
}
