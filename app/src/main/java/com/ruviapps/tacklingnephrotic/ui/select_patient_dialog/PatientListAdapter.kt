package com.ruviapps.tacklingnephrotic.ui.select_patient_dialog

import android.net.Uri
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.ruviapps.tacklingnephrotic.R
import com.ruviapps.tacklingnephrotic.databinding.FragmentPatientProfileBinding
import com.ruviapps.tacklingnephrotic.databinding.PatientCardBinding
import com.ruviapps.tacklingnephrotic.domain.Patient
import com.squareup.picasso.Picasso
import java.io.File
import java.util.*

class PatientListAdapter(private val clickListener: PatientClickListener) : ListAdapter<Patient,PatientListAdapter.PatientViewHolder>(PatientDiffItemCallback()) {


    class PatientDiffItemCallback: DiffUtil.ItemCallback<Patient>(){
        override fun areItemsTheSame(oldItem: Patient, newItem: Patient): Boolean {
        return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Patient, newItem: Patient): Boolean {
            return oldItem.patientId == newItem.patientId
        }

    }



    class PatientViewHolder(val binding : PatientCardBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(patient: Patient,clickListener: PatientClickListener){
              binding.patient = patient
              binding.click = clickListener
            binding.executePendingBindings()

        }

        companion object{
            fun from(viewGroup: ViewGroup) : PatientViewHolder {
                val layoutInflater = LayoutInflater.from(viewGroup.context)
                val binding = PatientCardBinding.inflate(layoutInflater,viewGroup,false)
                return PatientViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
      return  PatientViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val patientAtPosition = getItem(position) as Patient
        holder.bind(patientAtPosition,clickListener)

    }


}
class PatientClickListener(private val clickListener : (id : Long)-> Unit){
    fun onClick(patient : Patient) : Boolean{
        clickListener(patient.patientId)
        return true
    }
}

@BindingAdapter("patientImage")
fun ShapeableImageView.setPatientImage(uri : String){
    if(uri.isNotEmpty()) {
        Picasso.get().load(uri)
            .placeholder(R.mipmap.patient)
            .error(R.drawable.ic_baseline_broken_image_24)
            .resize(100, 100).centerCrop().into(this)
    }else{
        setImageResource(R.mipmap.patient)
    }
}

@BindingAdapter("patientAge")
fun TextView.setPatientAge(age : Int){
    text = if(age<=0){
        context.getString(R.string.not_available)
    }else
    {
        age.toString() + " " + context.getString(R.string.age_year)
    }
}
@BindingAdapter("patientName")
fun setPatientName(view : TextView,name : String?){
    if(name ==null || name.isEmpty()){
       view.text =  view.context.getString(R.string.no_name)
    }else
    {
        view.text = name.replaceFirstChar {
            if (it.isLowerCase())
                it.titlecase(Locale.getDefault())
            else
                it.toString()
        }
    }
}