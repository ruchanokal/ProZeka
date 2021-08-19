package com.ruchanokal.mathmaster.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.ruchanokal.mathmaster.R
import com.ruchanokal.mathmaster.model.ScreenItem
import kotlinx.android.synthetic.main.layout_viewpager2.view.*

class ViewPagerAdapter : PagerAdapter {

    lateinit var  mContext : Context
    lateinit var screenItem: List<ScreenItem>

    constructor(context: Context, screenItem: List<ScreenItem>) {
        this.mContext = context
        this.screenItem = screenItem
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

       var inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
       var layoutScreen = inflater.inflate(R.layout.layout_viewpager2,null)


       layoutScreen.textView.text = screenItem.get(position).title
       layoutScreen.textView2.text = screenItem.get(position).description
       screenItem.get(position).screenImage?.let { layoutScreen.imageView.setImageResource(it) }

       container.addView(layoutScreen)

        return layoutScreen

    }


    override fun getCount(): Int {
        return  screenItem.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

        container.removeView(`object` as View?)
    }
}