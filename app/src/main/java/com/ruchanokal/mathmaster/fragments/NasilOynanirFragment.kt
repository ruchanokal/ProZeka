package com.ruchanokal.mathmaster.fragments

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.ruchanokal.mathmaster.R
import com.ruchanokal.mathmaster.adapter.ViewPagerAdapter
import com.ruchanokal.mathmaster.adapter.ViewPagerAdapterYedek
import com.ruchanokal.mathmaster.model.ScreenItem
import kotlinx.android.synthetic.main.fragment_nasil_oynanir.*
import kotlinx.android.synthetic.main.fragment_seviye_bir.*
import kotlinx.android.synthetic.main.fragment_seviye_uc.*


class NasilOynanirFragment : Fragment() {

    lateinit var viewPagerAdapter: PagerAdapter
    var mList = arrayListOf<ScreenItem>()
    var position = 0
    var keyInteger = 0
    var arrayOfLayouts = arrayOf(R.layout.layout_seviye3_bilgilendirme_1,
        R.layout.layout_seviye3_bilgilendirme_2,
        R.layout.layout_seviye3_bilgilendirme_3,
        R.layout.layout_seviye3_bilgilendirme_4,
        R.layout.layout_seviye3_bilgilendirme_5)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nasil_oynanir, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {

            keyInteger = NasilOynanirFragmentArgs.fromBundle(it).keyInteger

        }

        if ( keyInteger == 1) {

            val birinciSayfa = ScreenItem(requireContext().getString(R.string.nasil_oynanir),requireContext().getString(R.string.nasil_oynanir_seviyebir_1), R.drawable.birincioyunbuyuk)
            val ikinciSayfa = ScreenItem(requireContext().getString(R.string.nasil_oynanir),requireContext().getString(R.string.nasil_oynanir_seviyebir_2), R.drawable.birincioyunanlatim)
            val ucuncuSayfa = ScreenItem(requireContext().getString(R.string.nasil_oynanir),requireContext().getString(R.string.nasil_oynanir_seviyebir_3), R.drawable.levelinfinitemod)
            val dorduncuSayfa = ScreenItem(requireContext().getString(R.string.nasil_oynanir),requireContext().getString(R.string.nasil_oynanir_seviyebir_4), R.drawable.levelinfinitemod)


            mList.add(birinciSayfa)
            mList.add(ikinciSayfa)
            mList.add(ucuncuSayfa)
            mList.add(dorduncuSayfa)

            viewPagerAdapter = ViewPagerAdapter(requireContext(),mList)


        } else if ( keyInteger == 2 ) {

            val birinciSayfa = ScreenItem(requireContext().getString(R.string.nasil_oynanir),requireContext().getString(R.string.nasil_oynanir_seviyeiki_1),R.drawable.ikincioyunbuyuk)
            val ikinciSayfa = ScreenItem(requireContext().getString(R.string.nasil_oynanir),requireContext().getString(R.string.nasil_oynanir_seviyeiki_2),R.drawable.ikincioyunbirincianlatim)
            val ucuncuSayfa = ScreenItem(requireContext().getString(R.string.nasil_oynanir),requireContext().getString(R.string.nasil_oynanir_seviyeiki_3),R.drawable.ikincioyunbirincianlatim)
            val dorduncuSayfa = ScreenItem(requireContext().getString(R.string.nasil_oynanir),requireContext().getString(R.string.nasil_oynanir_seviyeiki_4),R.drawable.ikincioyunikincianlatim)
            val besinciSayfa = ScreenItem(requireContext().getString(R.string.nasil_oynanir),requireContext().getString(R.string.nasil_oynanir_seviyeiki_5),R.drawable.levelinfinitemod)
            val altinciSayfa = ScreenItem(requireContext().getString(R.string.nasil_oynanir),requireContext().getString(R.string.nasil_oynanir_seviyeiki_6),R.drawable.levelinfinitemod)

            mList.add(birinciSayfa)
            mList.add(ikinciSayfa)
            mList.add(ucuncuSayfa)
            mList.add(dorduncuSayfa)
            mList.add(besinciSayfa)
            mList.add(altinciSayfa)

            viewPagerAdapter = ViewPagerAdapter(requireContext(),mList)


        } else if ( keyInteger == 3) {

            viewPagerAdapter = ViewPagerAdapterYedek(requireContext())

        }



        screenPagerNasilOynanir.adapter = viewPagerAdapter
        tablayoutNasilOynanir.setupWithViewPager(screenPagerNasilOynanir)


        devamButtonNasilOynanir.setOnClickListener {

            position = screenPagerNasilOynanir.currentItem

            if ( keyInteger == 3) {

                if (position < arrayOfLayouts.size){

                    position++
                    screenPagerNasilOynanir.currentItem = position
                    devamButtonNasilOynanir.visibility = View.VISIBLE

                }

                if (position == arrayOfLayouts.size - 1 ) {

                    devamButtonNasilOynanir.visibility = View.GONE

                }

            } else {

                if ( position < mList.size) {

                    position++
                    screenPagerNasilOynanir.currentItem = position
                    devamButtonNasilOynanir.visibility = View.VISIBLE

                }

                if (position == mList.size -1 ) {

                    devamButtonNasilOynanir.visibility = View.GONE
                    println("2 "+position +" "+mList.size)
                }


            }


        }


        tablayoutNasilOynanir.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {



                if ( keyInteger == 3) {

                    if(tab.position == arrayOfLayouts.size - 1 ){

                        devamButtonNasilOynanir.visibility = View.GONE

                    } else {

                        devamButtonNasilOynanir.visibility = View.VISIBLE
                    }



                } else {


                    if(tab.position == mList.size - 1 ) {

                        devamButtonNasilOynanir.visibility = View.GONE

                    } else {

                        devamButtonNasilOynanir.visibility = View.VISIBLE

                    }



                }



            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })


        val callback = object  : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)



    }
}