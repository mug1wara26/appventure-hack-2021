package com.example.appventure_hack_2021.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.appventure_hack_2021.*
import com.example.appventure_hack_2021.models.History

val modes = listOf(
    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
    AppCompatDelegate.MODE_NIGHT_NO,
    AppCompatDelegate.MODE_NIGHT_YES,
    AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
)
class SettingsFragment : Fragment(), NavigationActivity.OnEnterListener {
    private var initialised = false

    private var lastIndexTheme = 0
    private lateinit var themeSpinner: Spinner

    fun getModeIndex(): Int {
        val index = modes.indexOf(AppCompatDelegate.getDefaultNightMode())
        if (index == 3) return 0
        return index
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val settingsRef = userRef.child("settings")

        themeSpinner = view.findViewById(R.id.theme_spinner)
        initialised = true
        onEnter()
        themeSpinner.adapter = ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.themes,
            android.R.layout.simple_spinner_item
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        themeSpinner.setSelection(user.settings.theme_idx)
        // ^ required to not popup on entering settings screen in dark mode, no idea why the
        // call to onEnter above doesn't work, but it doesn't work.
        // the onEnter call doesn't change the selectedItem to 2 but leaves it at 0
        themeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, index: Int, id: Long) {
                if (getModeIndex() == index) {
                    // mode did not change
                    return
                }
                val dialog = ConfirmDialogFragment(R.string.confirm_theme_change)
                dialog.onConfirm = {
                    AppCompatDelegate.setDefaultNightMode(modes[index])

                    // setDefaultNightMode reloads view, have to reset to the proper fragment
                    (activity as NavigationActivity).navView.selectedItemId = R.id.nav_home
                    lastIndexTheme = index
                    settingsRef.child("theme_idx").setValue(index)
                }
                dialog.onCancel = {
                    themeSpinner.setSelection(lastIndexTheme)
                    // lastIndexTheme = index
                }
                dialog.show(childFragmentManager, "ThemeConfirmDialogFragment")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_UNSPECIFIED)
            }
        }

        view.findViewById<Button>(R.id.clear_history_button).setOnClickListener {
            val dialog = ConfirmDialogFragment(R.string.confirm_clear_history)
            dialog.onConfirm = {
                userRef.child("history").setValue(listOf<History>())
            }
            dialog.show(childFragmentManager, "ClearHistoryConfirmDialogFragment")
        }
        return view
    }

    override fun onPause() {
        super.onPause()
        refreshUser()
    }

    override fun onEnter() {
        if (initialised) {
            themeSpinner.setSelection(getModeIndex())
        }
    }
}