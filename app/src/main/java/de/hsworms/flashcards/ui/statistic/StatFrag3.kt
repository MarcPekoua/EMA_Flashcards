package de.hsworms.flashcards.ui.statistic

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import de.hsworms.flashcard.database.entity.RepositoryWithCards
import de.hsworms.flashcards.R
import kotlinx.android.synthetic.main.fragment_stat_frag3.*
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*

class StatFrag3 () : Fragment() {

    private val fileName = "termin.txt"
    private var tempDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stat_frag3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendar: Calendar = Calendar.getInstance()
        addTermin.setBackgroundColor(Color.GRAY)
        addTermin.setOnClickListener {
             saveDate()
        }
        showTermin.setOnClickListener{
            showDate()
        }

        //initialize the datepicker at the date of today
        datePicker.init(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),object:
            DatePicker.OnDateChangedListener{
            override fun onDateChanged(p0: DatePicker?, year: Int, month: Int, day: Int) {
                var mOnth=""
                var dAy=""
                if(month<9)
                    mOnth="0"+(month+1)
                else
                    mOnth=month.toString()

                if(day<10)
                    dAy="0"+day
                else
                    dAy=day.toString()
                tempDate=year.toString() + "." + (mOnth) + "." + dAy +"\n"
                addTermin.setBackgroundColor(Color.GREEN)
                addTermin.isClickable=true
                Toast.makeText(context,tempDate, Toast.LENGTH_LONG).show()

            }

        })
    }
        /*
            read a saved date from a file
         */
    private fun showDate() {
        try {
            val fileInput: FileInputStream = context?.applicationContext?.openFileInput(fileName)!!
            val streamReader: InputStreamReader = InputStreamReader(fileInput)

            val stringBuilder = fileInput.bufferedReader().use(BufferedReader::readText)


            if(stringBuilder.toString()!=""){
                terminText.setText(stringBuilder.toString()).toString()
            }else{
                terminText.setText("Kein Termin vorhanden").toString()
            }
        }catch (ex:Exception){
            Toast.makeText(context,ex.message, Toast.LENGTH_LONG).show()

        }


    }
    /*
        save date in format yyyy.mm.dd from datepicker
     */

    private fun saveDate(){
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year
        var mOnth=""
        var dAy=""
        if(month<9)
            mOnth="0"+(month+1)
        else
            mOnth=(month+1).toString()

        if(day<10)
            dAy="0"+day
        else
            dAy=day.toString()
        val tempDater: String =year.toString() + "." + mOnth + "." + dAy

        try {
            val fileOutput:FileOutputStream = context?.applicationContext?.openFileOutput(fileName,Context.MODE_PRIVATE)!!
            fileOutput.write(tempDater.toByteArray())
            fileOutput.close()
            Toast.makeText(context,"Termin saved", Toast.LENGTH_SHORT).show()

        }catch (ex:Exception){
            Toast.makeText(context,ex.message, Toast.LENGTH_LONG).show()
        }

    }


}