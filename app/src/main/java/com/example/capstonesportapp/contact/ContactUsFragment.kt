package com.example.capstonesportapp.contact

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.capstonesportapp.databinding.FragmentContactUsBinding
import kotlinx.android.synthetic.main.contact_us_form.*

class ContactUsFragment : Fragment() {
    private var _binding: FragmentContactUsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactUsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            contactUsLayout.setOnClickListener {
                clearFocusAllInputs()
            }
            btnSubmit.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_SUBJECT, edtSubject.text)
                    .putExtra(Intent.EXTRA_TEXT, edtMessage.text)

                if (activity?.packageManager?.resolveActivity(intent, 0) != null) {
                    clearFocusAllInputs()
                    startActivity(intent)
                }
            }
        }
    }

    private fun clearFocusAllInputs() {
        hideKeyboard()
        with(binding) {
            incContactForm.outlinedBody.clearFocus()
            incContactForm.outlinedSubject.clearFocus()
        }
    }

    private fun hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}