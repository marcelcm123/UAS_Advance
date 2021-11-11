package ac.id.ubaya.todoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ac.id.ubaya.todoapp.R
import ac.id.ubaya.todoapp.databinding.FragmentCreateTodoBinding
import ac.id.ubaya.todoapp.model.Todo
import ac.id.ubaya.todoapp.util.NotificationHelper
import ac.id.ubaya.todoapp.util.TodoWorker
import ac.id.ubaya.todoapp.viewmodel.DetailTodoViewModel
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.text.format.DateFormat.is24HourFormat
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlinx.android.synthetic.main.fragment_create_todo.*
import java.util.*
import android.text.format.DateFormat;
import kotlinx.android.synthetic.main.fragment_create_todo.view.*
import java.util.concurrent.TimeUnit

class CreateTodoFragment : Fragment() ,
    ButtonAddTodoClickListener,
    RadioClick,
    DateClickListener,
    TimeClickListener,
DatePickerDialog.OnDateSetListener,
TimePickerDialog.OnTimeSetListener{

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_create_todo,container,false)
        return dataBinding.root
    }

    private lateinit var viewModel: DetailTodoViewModel
    private lateinit var dataBinding: FragmentCreateTodoBinding
    var year =0
    var month = 0
    var day = 0
    var hour = 0
    var minute = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.todo = Todo("","",3,0,0)
        viewModel = ViewModelProvider(this).get(DetailTodoViewModel::class.java)
        dataBinding.listener = this
        dataBinding.radioListener = this
        dataBinding.listenerDate = this
        dataBinding.listenerTime = this

        /*btnCreateTodo.setOnClickListener {
            NotificationHelper(view.context)
                .createNotification("Todo Created",
                    "A new todo has been created! Stay focus!")
            val myWorkRequest = OneTimeWorkRequestBuilder<TodoWorker>()
                .setInitialDelay(30,TimeUnit.SECONDS)
                .setInputData(workDataOf(
                    "title" to "Todo Created",
                    "message" to "A new todo has been created! Stay focus!"
                ))
                .build()
            WorkManager.getInstance(requireContext()).enqueue(myWorkRequest)
            var radio = view.findViewById<RadioButton>(radioGroupPriority.checkedRadioButtonId)

            var todo = Todo(txtTitle.text.toString(),txtNotes.text.toString(), radio.tag.toString().toInt(),0)
            val list = listOf(todo)
            viewModel.addTodo(list)
            Toast.makeText(view.context, "Data added", Toast.LENGTH_LONG).show()
            Navigation.findNavController(it).popBackStack()
        }*/
    }

    override fun onButtonAddTodo(v: View) {
        val c = Calendar.getInstance()
        c.set(year,month,day,hour,minute,0)
        val today = Calendar.getInstance()
        val diff = (c.timeInMillis/1000L)-(today.timeInMillis/1000L)

        dataBinding.todo!!.todo_date = (c.timeInMillis/1000L).toInt()

        val list = listOf(dataBinding.todo!!)
        viewModel.addTodo(list)
        Toast.makeText(v.context,"Data added",Toast.LENGTH_LONG).show()
        val myWorkRequest = OneTimeWorkRequestBuilder<TodoWorker>()
            .setInitialDelay(diff,TimeUnit.SECONDS)
            .setInputData(workDataOf(
                "title" to "Todo Created",
                "message" to "A new todo has been created! Stay focus!"
            ))
            .build()
        WorkManager.getInstance(requireContext()).enqueue(myWorkRequest)
        Navigation.findNavController(v).popBackStack()

    }

    override fun onRadioClick(v: View, priority: Int, obj: Todo) {
        obj.priority = priority
    }

    override fun onDateClick(v: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        activity?.let{it1 -> DatePickerDialog(it1,this,year,month,day).show()}
    }

    override fun onTimeClick(v: View) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        TimePickerDialog(activity,this,hour,minute, is24HourFormat(activity)).show()}

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        Calendar.getInstance().let{
            it.set(year,month,day)
            dataBinding.root.txtDate.setText(day.toString().padStart(2,'0')+"-"+month.toString().padStart(2,'0')+"-"+year)
            this.year = year
            this.month = month
            this.day = day
        }

    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        dataBinding.root.txtTime.setText(
            hourOfDay.toString().padStart(2,'0')+":"+minute.toString().padStart(2,'0'))
        hour = hourOfDay
        this.minute = minute
    }
}