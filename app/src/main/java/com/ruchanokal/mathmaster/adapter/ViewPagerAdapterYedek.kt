package com.ruchanokal.mathmaster.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.ruchanokal.mathmaster.R
import kotlinx.android.synthetic.main.layout_viewpager2.view.*
import kotlinx.android.synthetic.main.layout_viewpager_yedek.view.*


class ViewPagerAdapterYedek : PagerAdapter {

    lateinit var  mContext : Context


    var layoutes = intArrayOf(R.layout.layout_seviye3_bilgilendirme_1,
        R.layout.layout_seviye3_bilgilendirme_2,
        R.layout.layout_seviye3_bilgilendirme_3,
        R.layout.layout_seviye3_bilgilendirme_4,
        R.layout.layout_seviye3_bilgilendirme_5)

    constructor(context: Context) {
        this.mContext = context
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        var inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val viewOne = inflater.inflate(R.layout.layout_seviye3_bilgilendirme_1,container,false)
        val viewTwo = inflater.inflate(R.layout.layout_seviye3_bilgilendirme_2,container,false)
        val viewThree = inflater.inflate(R.layout.layout_seviye3_bilgilendirme_3,container,false)
        val viewFour = inflater.inflate(R.layout.layout_seviye3_bilgilendirme_4,container,false)
        val viewFive = inflater.inflate(R.layout.layout_seviye3_bilgilendirme_5,container,false)

        val viewArray = arrayOf(viewOne,viewTwo,viewThree,viewFour,viewFive)


        container.addView(viewArray[position])

        return viewArray[position]

    }


    override fun getCount(): Int {
        return  layoutes.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

        container.removeView(`object` as View?)
    }
}