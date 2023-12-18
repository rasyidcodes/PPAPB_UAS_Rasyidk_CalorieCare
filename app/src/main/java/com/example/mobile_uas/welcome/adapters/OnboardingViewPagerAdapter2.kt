package com.example.uts_mobile_caloricare_rasyidk.welcomingPage.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mobile_uas.R
import com.example.uts_mobile_caloricare_rasyidk.welcomingPage.fragments.WelcomingFragment

class OnboardingViewPagerAdapter2(
    fragmentActivity: FragmentActivity,
    private val context: Context
) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WelcomingFragment.newInstance(
                context.resources.getString(R.string.title_onboarding_1),
                context.resources.getString(R.string.description_onboarding_1),
                R.raw.asset_vegetable
            )
            1 -> WelcomingFragment.newInstance(
                context.resources.getString(R.string.title_onboarding_2),
                context.resources.getString(R.string.description_onboarding_2),
                R.raw.asset_ebook
            )
            else -> WelcomingFragment.newInstance(
                context.resources.getString(R.string.title_onboarding_3),
                context.resources.getString(R.string.description_onboarding_3),
                R.raw.asset_frenchfries
            )
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}