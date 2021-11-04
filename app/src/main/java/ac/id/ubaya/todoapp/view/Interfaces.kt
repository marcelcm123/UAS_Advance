package ac.id.ubaya.todoapp.view

import ac.id.ubaya.todoapp.model.Todo
import android.view.View
import android.widget.CompoundButton

interface TodoCheckedChangeListener {
    fun onCheckChanged(cb: CompoundButton,
                       isChecked:Boolean,
                       obj: Todo
    )
}
interface TodoEditClick {
    fun onTodoEditClick(v: View)
}
interface RadioClick {
    fun onRadioClick(v:View, priority:Int, obj:Todo)
}
interface TodoSaveChangesClick {
    fun onTodoSaveChangesClick(v: View, obj: Todo)
}

