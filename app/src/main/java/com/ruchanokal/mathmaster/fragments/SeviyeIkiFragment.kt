package com.ruchanokal.mathmaster.fragments

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager.widget.PagerAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.games.Games
import com.google.android.material.tabs.TabLayout
import com.ruchanokal.mathmaster.R
import com.ruchanokal.mathmaster.adapter.ViewPagerAdapter
import com.ruchanokal.mathmaster.model.ScreenItem
import kotlinx.android.synthetic.main.fragment_seviye_bir.*
import kotlinx.android.synthetic.main.fragment_seviye_iki.*
import kotlinx.android.synthetic.main.fragment_seviye_uc.*
import kotlinx.android.synthetic.main.oyun_bitti.*
import kotlinx.android.synthetic.main.pop_op_level.*
import kotlinx.android.synthetic.main.pop_up_sonsuz_mod.*
import kotlinx.android.synthetic.main.pop_up_sonsuz_mod.tekrarOynaButton
import java.util.*


class SeviyeIkiFragment : Fragment() {

    private val RC_LEADERBOARD_UI = 9004
    // Seviye-2 Banner :: ca-app-pub-5016889744069609/9478241792
    // Test ID :: ca-app-pub-3940256099942544/6300978111

    private var mRewardedAd: RewardedAd? = null

    lateinit var viewPagerAdapter: PagerAdapter
    var mList = arrayListOf<ScreenItem>()
    var position = 0
    var puan = 0
    var oyunBittiMi = false

    var min2 = 20
    var max2 = 50


    lateinit var preferences : SharedPreferences
    private var backPressedTime : Long = 0
    var dialog = activity?.let { Dialog(it) }

    val numberArrayList2 = arrayListOf<TextView>()
    val levelTextViewArray = arrayListOf<TextView>()

    val levelPuanArrayForSonsuzMod2 = arrayOf(100,220,350,500,650,800,950,1100,1250,1400,1550,1700,1850,2000,2200,2400,2600)
    val levelPuanArrayForLevelMod2 = arrayOf(100,120,130,150,180,200,220,250,270,300,320,360,400,430,470,500,600)


    val minArray2 = arrayOf(20,30,30,30,30,40,40,40,40,50,50,50,50,50,60,70)
    val maxArray2 = arrayOf(50,60,70,80,90,100,110,120,130,140,150,160,170,180,190,200)

    var geriSaymaSuresi : Long = 15200
    var degistirmePeriyodu : Long = 2500
    var levelPuanIndex = 0
    var level = 1
    var levelForSpeed = 1

    private lateinit var adRequestForReward: AdRequest


    var runnable : Runnable = Runnable {  }
    var runnable2 : Runnable = Runnable {  }
    val handler : Handler = Handler(Looper.getMainLooper())

    companion object{
        var geriSayan : CountDownTimer? = null
        var baslangicGeriSayan : CountDownTimer? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        val view : View = inflater.inflate(R.layout.fragment_seviye_iki, container, false)

        return view
    }


    override fun onResume() {
        super.onResume()
        if (requireView() == null) {
            return
        }
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { v, keyCode, event ->
            if (event.action === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {


                if (backPressedTime + 3000 > System.currentTimeMillis())
                {
                    dialog?.dismiss()
                    geriSayan?.cancel()
                    baslangicGeriSayan?.cancel()

                    var enYuksekSkor2 = ""

                    val eskiSkor = preferences.getInt("skor2",0)

                    if (puan > eskiSkor) {

                        preferences?.edit()?.putInt("skor2",puan)?.apply()
                        enYuksekSkor2 = puan.toString()

                    } else {
                        enYuksekSkor2 = eskiSkor.toString()
                    }

                    val action = SeviyeIkiFragmentDirections.actionSeviyeIkiFragmentToAnaFragment(enyuksekskor2 = enYuksekSkor2)
                    Navigation.findNavController(v).navigate(action)

                } else {

                    Toast.makeText(context,requireActivity().getString(R.string.cikis_yapmak_icin_tekrar_dokunun), Toast.LENGTH_LONG).show()

                }

                backPressedTime = System.currentTimeMillis()


                // handle back button's click listener
                true
            } else false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adRequest = AdRequest.Builder().build()
        adView2.loadAd(adRequest)

        adRequestForReward = AdRequest.Builder().build()

        loadRewardedAds()


        oyunEkraniLayout2.visibility = View.GONE
        seviyeIkiOyunBaslangicText.visibility = View.GONE

        preferences = activity?.getSharedPreferences("ikinci", Context.MODE_PRIVATE)!!
        val isChecked = preferences.getBoolean("isChecked",false)

        if ( isChecked == true) {


            levelsLayout2.visibility = View.VISIBLE
            introScreenLayout2.visibility = View.GONE


            levelModu2.setOnClickListener {

                //oyunBaslangici()
                levelDetay()


            }

            sonsuzMod2.setOnClickListener {

                oyunBaslangici2()

            }


        } else {

            checkboxForIntro2.visibility = View.GONE

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

            screenPager2.adapter = viewPagerAdapter
            tablayoutSeviye2.setupWithViewPager(screenPager2)

            devamButton2.setOnClickListener {

                position = screenPager2.currentItem

                if (position < mList.size){

                    position++
                    screenPager2.currentItem = position

                }

                if (position == mList.size - 1 ){

                    checkboxForIntro2.visibility = View.VISIBLE
                    tablayoutSeviye2.visibility = View.GONE


                } else if ( position == mList.size) {

                    introScreenLayout2.visibility = View.GONE
                    levelsLayout2.visibility = View.VISIBLE

                }


            }


            tablayoutSeviye2.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {

                    if(tab.position == mList.size - 1 ){


                        buttonVeBilgilendirmeLayout2.visibility = View.VISIBLE
                        tablayoutSeviye2.visibility = View.GONE



                    } else if ( tab.position == mList.size) {

                        introScreenLayout2.visibility = View.GONE
                        levelsLayout2.visibility = View.VISIBLE

                    }


                }

                override fun onTabUnselected(tab: TabLayout.Tab) {

                }

                override fun onTabReselected(tab: TabLayout.Tab) {

                }

            })


            checkboxForIntro2.setOnCheckedChangeListener { compoundButton, b ->

                if (compoundButton.isChecked){

                    preferences.edit().putBoolean("isChecked",true).apply()

                } else {

                    preferences.edit().putBoolean("isChecked",false).apply()

                }

            }


            levelModu2.setOnClickListener {

                levelDetay()


            }

            sonsuzMod2.setOnClickListener {

                oyunBaslangici2()
            }


        }


    }

    private fun loadRewardedAds() {

        //Test ID :: ca-app-pub-3940256099942544/5224354917
        // Real ID :: ca-app-pub-5016889744069609/7359344919

        RewardedAd.load(context,"ca-app-pub-5016889744069609/7359344919", adRequestForReward,
            object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {

                println("reklam yüklenemedi, sebebi: " + adError)
                mRewardedAd = null

            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {

                println("reklam yüklendi.")
                mRewardedAd = rewardedAd
            }
        })
    }

    private fun levelDetay() {


        levelsLayout2.visibility = View.GONE
        introScreenLayout2.visibility = View.GONE
        //oyunuBaslatma()

        levelTextViewArray.add(levelbirSeviye2)
        levelTextViewArray.add(levelikiSeviye2)
        levelTextViewArray.add(levelucSeviye2)
        levelTextViewArray.add(leveldortSeviye2)
        levelTextViewArray.add(levelbesSeviye2)
        levelTextViewArray.add(levelaltiSeviye2)
        levelTextViewArray.add(levelyediSeviye2)
        levelTextViewArray.add(levelsekizSeviye2)
        levelTextViewArray.add(leveldokuzSeviye2)
        levelTextViewArray.add(levelonSeviye2)
        levelTextViewArray.add(levelonbirSeviye2)
        levelTextViewArray.add(levelonikiSeviye2)
        levelTextViewArray.add(levelonucSeviye2)
        levelTextViewArray.add(levelondortSeviye2)
        levelTextViewArray.add(levelonbesSeviye2)
        levelTextViewArray.add(levelonaltiSeviye2)


        var levelSayisi = 0
        level = preferences.getInt("level",1)

        while ( levelSayisi < level) {

            levelTextViewArray[levelSayisi].text = (levelSayisi+1).toString()
            levelTextViewArray[levelSayisi].setTextColor(Color.parseColor("#C89635"))
            levelTextViewArray[levelSayisi].setBackgroundResource(R.drawable.border_for_popup3)

            levelSayisi++
        }

        levellarPage2.visibility = View.VISIBLE

        for ( a in 1..levelSayisi) {

            levelTextViewArray[a-1].setOnClickListener {

                levellarPage2.visibility = View.GONE

                oyunBaslangici()

                level = a


            }

        }



    }


    private fun oyunBaslangici() {

        puanCardView2.visibility = View.GONE

        progressFrameLayout2.visibility = View.VISIBLE

        level2ProgressBar.progress = 0


        seviyeIkiOyunBaslangicText.visibility = View.VISIBLE
        introScreenLayout2.visibility = View.GONE
        levelsLayout2.visibility = View.GONE


        baslangicGeriSayan = object : CountDownTimer(3200,1000) {
            override fun onTick(p0: Long) {

                seviyeIkiOyunBaslangicText.text = ((p0)/1000).toString()

            }

            override fun onFinish() {

                relativeLayoutForBanner2.visibility = View.VISIBLE
                seviyeIkiOyunBaslangicText.visibility = View.GONE
                oyunEkraniLayout2.visibility = View.VISIBLE


                startingTheGame()


            }


        }.start()


    }


    private fun oyunBaslangici2() {

        puanCardView2.visibility = View.VISIBLE

        progressFrameLayout2.visibility = View.GONE

        seviyeIkiOyunBaslangicText.visibility = View.VISIBLE
        introScreenLayout2.visibility = View.GONE
        levelsLayout2.visibility = View.GONE


        baslangicGeriSayan = object : CountDownTimer(3200,1000) {
            override fun onTick(p0: Long) {

                seviyeIkiOyunBaslangicText.text = ((p0)/1000).toString()

            }

            override fun onFinish() {

                relativeLayoutForBanner2.visibility = View.VISIBLE
                seviyeIkiOyunBaslangicText.visibility = View.GONE
                oyunEkraniLayout2.visibility = View.VISIBLE


                startingTheGame2()


            }


        }.start()

    }


    private fun startingTheGame() {

        oyunBittiMi = false

        levels()

        level2ProgressBar.max = levelPuanArrayForLevelMod2[levelPuanIndex]
        level2ProgressBar.progress = puan

        levelPuanIndex = level - 1
        min2 = minArray2[level-1]
        max2 = maxArray2[level-1]

        ////////////////////////?????????????????????????????????////////////////
        ////////////////////////?????????????????????????????????////////////////
        ////////////////////////?????????????????????????????????////////////////

        //puanSayaciSeviyeIki.setText(puan.toString()+"/"+levelPuanArrayForLevelMod2[levelPuanIndex])


        levelTextSeviyeIki.text = requireContext().getString(R.string.level_buyuk)+ " " + level.toString()


        imageViewKronometre2.visibility = View.VISIBLE


        numberArrayList2.add(birinciTextSeviyeIki)
        numberArrayList2.add(ikinciTextSeviyeIki)
        numberArrayList2.add(ucuncuTextSeviyeIki)
        numberArrayList2.add(dorduncuTextSeviyeIki)
        numberArrayList2.add(besinciTextSeviyeIki)
        numberArrayList2.add(altinciTextSeviyeIki)
        numberArrayList2.add(yedinciTextSeviyeIki)
        numberArrayList2.add(sekizinciTextSeviyeIki)
        numberArrayList2.add(dokuzuncuTextSeviyeIki)


        for ( arraylist in numberArrayList2) {

            arraylist.setBackgroundResource(R.drawable.border2)

        }



        val colorArray = arrayOf(Color.parseColor("#ff0000"),Color.parseColor("#0047AB"),
            Color.parseColor("#ffc000"))

        val randomIndex1 = Random().nextInt(3)
        var randomIndex2 = Random().nextInt(3)


        while (randomIndex2 == randomIndex1) {

            randomIndex2 = Random().nextInt(3)

        }

        var randomIndex3 = Random().nextInt(3)

        while (randomIndex3 == randomIndex1 || randomIndex3 == randomIndex2) {

            randomIndex3 = Random().nextInt(3)

        }




        var birinciRandomSayi = Random().nextInt(max2-min2)+min2
        var ikinciRandomSayi = Random().nextInt(max2-min2)+min2

        birinciSonucTextSeviyeIki.text = "${birinciRandomSayi}"
        birinciSonucTextSeviyeIki.setTextColor(colorArray.get(randomIndex1))

        ikinciSonucTextSeviyeIki.text = "${ikinciRandomSayi}"
        ikinciSonucTextSeviyeIki.setTextColor(colorArray.get(randomIndex2))

        val sonuc = birinciRandomSayi + ikinciRandomSayi

        val resultColor = ColorUtils.blendARGB(colorArray.get(randomIndex1),colorArray.get(randomIndex2), 0.5f)
        var resultColor2 = ColorUtils.blendARGB(colorArray.get(randomIndex1),colorArray.get(randomIndex3), 0.5f)
        var resultColor3 = ColorUtils.blendARGB(colorArray.get(randomIndex2),colorArray.get(randomIndex3), 0.5f)

        val sonucRandom1 = Random().nextInt(10)+1

        var miniArray = arrayOf(sonuc+10,sonuc-10)
        var randomSonuc = miniArray[Random().nextInt(miniArray.size)]


        val runnable = object : Runnable {
            override fun run() {


                for ( array in numberArrayList2){

                    array.text = null
                    //array.setBackgroundResource(R.drawable.border2)
                    array.setBackgroundColor(Color.WHITE)
                }

                var random1 = Random().nextInt(9)
                var random2 = Random().nextInt(9)

                while ( random2 == random1){

                    random2 = Random().nextInt(9)
                }

                var random3 = Random().nextInt(9)

                while (random3 == random1 || random3 == random2) {

                    random3 = Random().nextInt(9)
                }

                var random4 = Random().nextInt(9)

                while (random4 == random1 || random4 == random2 || random4 == random3) {

                    random4 = Random().nextInt(9)
                }

                var random5 = Random().nextInt(9)

                while (random5 == random1 || random5 == random2 || random5 == random3 || random5 == random4) {

                    random5 = Random().nextInt(9)
                }

                var random6 = Random().nextInt(9)

                while (random6 == random1 || random6 == random2 ||
                    random6 == random3 || random6 == random4 || random6 == random5) {

                    random6 = Random().nextInt(9)
                }




                numberArrayList2[random1].text = "${sonuc}"
                numberArrayList2[random1].setBackgroundColor(resultColor)


                numberArrayList2[random3].text = "${sonuc-sonucRandom1}"
                numberArrayList2[random3].setBackgroundColor(resultColor)


                numberArrayList2[random2].text = "${randomSonuc}"
                numberArrayList2[random2].setBackgroundColor(resultColor)


                numberArrayList2[random4].text = "${sonuc}"
                numberArrayList2[random4].setBackgroundColor(resultColor2)


                numberArrayList2[random5].text = "${sonuc-sonucRandom1}"
                numberArrayList2[random5].setBackgroundColor(resultColor2)


                numberArrayList2[random6].text = "${randomSonuc}"
                numberArrayList2[random6].setBackgroundColor(resultColor2)


                handler.postDelayed(this,degistirmePeriyodu)
            }

        }


        handler.post(runnable)


        val sonucString = sonuc.toString()



        geriSayan = object : CountDownTimer(geriSaymaSuresi,1000){

            override fun onTick(p0: Long) {


                seviyeIkiKronometre.text = (p0/1000).toString()


                //if (p0 > 2000L){


                    for (a in 0..8) {

                        numberArrayList2[a].setOnClickListener {

                            val clickString = numberArrayList2.get(a).text.toString()


                            val textViewOnemli = numberArrayList2.get(a)
                            val colornumber = (textViewOnemli.getBackground() as ColorDrawable).color

                            println("colorNumber : " + colornumber)

                            handler.removeCallbacks(runnable)

                            cancel()

                            for (c in 0..8) {

                                numberArrayList2[c].isClickable = false

                            }

                            if (clickString.equals(sonucString) && colornumber.equals(resultColor)){


                            puan = puan +10

                            level2ProgressBar.progress = puan

                            if ( puan == levelPuanArrayForLevelMod2[levelPuanIndex]){


                                if (levelPuanIndex == 15) {

                                    showOyunBittiDialog()

                                    oyunBittiMi = true


                                } else {

                                    levelPuanIndex++
                                    level++

                                }


                                levels()

                                preferences.edit().putInt("level",level).apply()

                                //puanSayaciSeviyeIki.text = "${puan}/${levelPuanArrayForLevelMod2[levelPuanIndex]}"

                                puan =0
                            }


                                dogruYanlisImageView.setImageResource(R.drawable.ic_baseline_check_circle_outline_24)


                                if (oyunBittiMi == false){

                                handler.postDelayed( object :Runnable {
                                    override fun run() {

                                        for (c in 0..8) {

                                            numberArrayList2[c].text = null
                                        }

                                        dogruYanlisImageView.setImageResource(android.R.color.transparent)


                                        startingTheGame()

                                    }


                                },500)

                            } else {

                                seviyeIkiKronometre.setOnClickListener {

                                    puan = 0

                                    startingTheGame()


                                }



                            }

                                return@setOnClickListener


                            } else {

                                dogruYanlisImageView.setImageResource(R.drawable.ic_outline_cancel_24)


                                handler.postDelayed( object :Runnable {
                                    override fun run() {

                                        for (c in 0..8) {

                                            numberArrayList2[c].isClickable = false
                                            numberArrayList2[c].setBackgroundColor(Color.parseColor("#808080"))

                                        }

                                        dogruYanlisImageView.setImageResource(android.R.color.transparent)


                                    }


                                },300)

                                showDialog()

                                seviyeIkiKronometre.setOnClickListener {


                                    startingTheGame()

                                }


                            }



                        }



                    }
                //}
            }



            override fun onFinish() {


                handler.removeCallbacks(runnable)

                for (a in 0..8) {

                    numberArrayList2[a].isClickable = false
                    numberArrayList2[a].setBackgroundColor(Color.parseColor("#808080"))

                }
                /*
                for (x in numberArrayList2) {

                    val xColor = (x.getBackground() as ColorDrawable).color


                    if ( x.text.toString().equals(sonucString) && xColor.equals(resultColor)){

                        x.setBackgroundColor(Color.parseColor("#008000"))

                    }

                }

                */

                seviyeIkiKronometre.text = requireContext().getString(R.string.sure_bitti)

                showDialog()


                seviyeIkiKronometre.setOnClickListener {

                    startingTheGame()

                }

                ////////////////////////?????????????????????????????????////////////////
                ////////////////////////?????????????????????????????????////////////////
                ////////////////////////?????????????????????????????????////////////////

                //puanSayaciSeviyeIki.setText(puan.toString()+"/"+levelPuanArrayForLevelMod2[levelPuanIndex])

            }

        }.start()

    }

    private fun showOyunBittiDialog() {

        dialog = activity?.let { Dialog(it) }

        if (dialog != null){

            dialog!!.setContentView(R.layout.oyun_bitti)

            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.setCancelable(false)

            dialog!!.tekrarOyunBittiButton.setOnClickListener {

                dialog!!.dismiss()

                handler.removeCallbacks(runnable)

                puan = 0

                startingTheGame()

            }

            dialog!!.carpiOyunBitti.setOnClickListener {

                seviyeIkiKronometre.setText(requireContext().getString(R.string.tekrar_oyna))
                imageViewKronometre2.visibility = View.GONE

                dialog!!.dismiss()

            }


            dialog!!.menuyeGitOyunBittiButton.setOnClickListener {

                dialog!!.dismiss()

                val action = SeviyeIkiFragmentDirections.actionSeviyeIkiFragmentToAnaFragment(levelikincioyun = level)
                NavHostFragment.findNavController(this).navigate(action)

            }

            dialog!!.show()

        }

    }

    private fun startingTheGame2() {

        levelsForSpeed()

        min2 = minArray2[level-1]
        max2 = maxArray2[level-1]
        levelPuanIndex = levelForSpeed - 1


        val layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)
        puanCardView2.layoutParams = layoutParams

        puanSayaciSeviyeIki.text = puan.toString()

        imageViewKronometre2.visibility = View.VISIBLE


        numberArrayList2.add(birinciTextSeviyeIki)
        numberArrayList2.add(ikinciTextSeviyeIki)
        numberArrayList2.add(ucuncuTextSeviyeIki)
        numberArrayList2.add(dorduncuTextSeviyeIki)
        numberArrayList2.add(besinciTextSeviyeIki)
        numberArrayList2.add(altinciTextSeviyeIki)
        numberArrayList2.add(yedinciTextSeviyeIki)
        numberArrayList2.add(sekizinciTextSeviyeIki)
        numberArrayList2.add(dokuzuncuTextSeviyeIki)


        for ( arraylist in numberArrayList2) {

            arraylist.setBackgroundResource(R.drawable.border2)

        }



        val colorArray = arrayOf(Color.parseColor("#ff0000"),Color.parseColor("#0047AB"),
            Color.parseColor("#ffc000"))

        val randomIndex1 = Random().nextInt(3)
        var randomIndex2 = Random().nextInt(3)


        while (randomIndex2 == randomIndex1) {

            randomIndex2 = Random().nextInt(3)

        }

        var randomIndex3 = Random().nextInt(3)

        while (randomIndex3 == randomIndex1 || randomIndex3 == randomIndex2) {

            randomIndex3 = Random().nextInt(3)

        }




        var birinciRandomSayi = Random().nextInt(max2-min2)+min2
        var ikinciRandomSayi = Random().nextInt(max2-min2)+min2

        birinciSonucTextSeviyeIki.text = "${birinciRandomSayi}"
        birinciSonucTextSeviyeIki.setTextColor(colorArray.get(randomIndex1))

        ikinciSonucTextSeviyeIki.text = "${ikinciRandomSayi}"
        ikinciSonucTextSeviyeIki.setTextColor(colorArray.get(randomIndex2))

        val sonuc = birinciRandomSayi + ikinciRandomSayi

        val resultColor = ColorUtils.blendARGB(colorArray.get(randomIndex1),colorArray.get(randomIndex2), 0.5f)
        var resultColor2 = ColorUtils.blendARGB(colorArray.get(randomIndex1),colorArray.get(randomIndex3), 0.5f)
        var resultColor3 = ColorUtils.blendARGB(colorArray.get(randomIndex2),colorArray.get(randomIndex3), 0.5f)

        val sonucRandom1 = Random().nextInt(10)+1

        var miniArray = arrayOf(sonuc+10,sonuc-10)
        var randomSonuc = miniArray[Random().nextInt(miniArray.size)]


        val runnable2 = object : Runnable {
            override fun run() {


                for ( array in numberArrayList2){

                    array.text = null
                    //array.setBackgroundResource(R.drawable.border2)
                    array.setBackgroundColor(Color.WHITE)
                }

                var random1 = Random().nextInt(9)
                var random2 = Random().nextInt(9)

                while ( random2 == random1){

                    random2 = Random().nextInt(9)
                }

                var random3 = Random().nextInt(9)

                while (random3 == random1 || random3 == random2) {

                    random3 = Random().nextInt(9)
                }

                var random4 = Random().nextInt(9)

                while (random4 == random1 || random4 == random2 || random4 == random3) {

                    random4 = Random().nextInt(9)
                }

                var random5 = Random().nextInt(9)

                while (random5 == random1 || random5 == random2 || random5 == random3 || random5 == random4) {

                    random5 = Random().nextInt(9)
                }

                var random6 = Random().nextInt(9)

                while (random6 == random1 || random6 == random2 ||
                    random6 == random3 || random6 == random4 || random6 == random5) {

                    random6 = Random().nextInt(9)
                }



                numberArrayList2[random1].text = "${sonuc}"
                numberArrayList2[random1].setBackgroundColor(resultColor)


                numberArrayList2[random3].text = "${sonuc-sonucRandom1}"
                numberArrayList2[random3].setBackgroundColor(resultColor)


                numberArrayList2[random2].text = "${randomSonuc}"
                numberArrayList2[random2].setBackgroundColor(resultColor)


                numberArrayList2[random4].text = "${sonuc}"
                numberArrayList2[random4].setBackgroundColor(resultColor2)


                numberArrayList2[random5].text = "${sonuc-sonucRandom1}"
                numberArrayList2[random5].setBackgroundColor(resultColor2)


                numberArrayList2[random6].text = "${randomSonuc}"
                numberArrayList2[random6].setBackgroundColor(resultColor2)


                handler.postDelayed(this,degistirmePeriyodu)
            }

        }


        handler.post(runnable2)




        geriSayan = object : CountDownTimer(geriSaymaSuresi,1000){

            override fun onTick(p0: Long) {


                seviyeIkiKronometre.text = (p0/1000).toString()


                //if (p0 > 2000L){


                for (a in 0..8) {

                    numberArrayList2[a].setOnClickListener {

                        val clickString = numberArrayList2.get(a).text.toString()
                        val sonucString = sonuc.toString()

                        val textViewOnemli = numberArrayList2.get(a)
                        val colornumber = (textViewOnemli.getBackground() as ColorDrawable).color


                        handler.removeCallbacks(runnable2)

                        cancel()

                        for (c in 0..8) {

                            numberArrayList2[c].isClickable = false

                        }

                        if (clickString.equals(sonucString) && colornumber.equals(resultColor)){


                            puan = puan +10

                            if ( puan == levelPuanArrayForSonsuzMod2[levelPuanIndex]){

                                if ( levelPuanIndex != 15 ) {

                                    levelPuanIndex++
                                    levelForSpeed++
                                    levelsForSpeed()

                                }

                            }

                            puanSayaciSeviyeIki.text = "${puan}"


                            dogruYanlisImageView.setImageResource(R.drawable.ic_baseline_check_circle_outline_24)


                            handler.postDelayed( object :Runnable {
                                override fun run() {

                                    for (c in 0..8) {

                                        numberArrayList2[c].text = null
                                    }

                                    dogruYanlisImageView.setImageResource(android.R.color.transparent)

                                    startingTheGame2()

                                }


                            },500)



                            return@setOnClickListener


                        } else {

                            dogruYanlisImageView.setImageResource(R.drawable.ic_outline_cancel_24)

                            handler.postDelayed( object :Runnable {
                                override fun run() {


                                    for (c in 0..8) {
                                        numberArrayList2[c].isClickable = false
                                        numberArrayList2[c].setBackgroundColor(Color.parseColor("#808080"))
                                    }

                                    dogruYanlisImageView.setImageResource(android.R.color.transparent)


                                }


                            },300)

                            showDialog2()

                            seviyeIkiKronometre.setOnClickListener {

                                levelForSpeed = 1

                                startingTheGame2()

                            }

                        }

                    }

                }
                //}
            }



            override fun onFinish() {


                handler.removeCallbacks(runnable2)

                for (a in 0..8) {

                    numberArrayList2[a].isClickable = false
                    numberArrayList2[a].setBackgroundColor(Color.parseColor("#808080"))

                }

                seviyeIkiKronometre.text = requireContext().getString(R.string.sure_bitti)

                puanSayaciSeviyeIki.text = puan.toString()


                showDialog()


                seviyeIkiKronometre.setOnClickListener {

                    levelForSpeed = 1

                    startingTheGame2()

                }

            }


        }.start()



    }


    private fun showDialog() {

        dialog = activity?.let { Dialog(it) }


        if (dialog != null){

            dialog!!.setContentView(R.layout.pop_op_level)

            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.setCancelable(false)


            dialog!!.tekrarOynaButtonLevel.setOnClickListener {


                dialog!!.dismiss()

                handler.removeCallbacks(runnable)

                puan = 0

                startingTheGame()

            }

            dialog!!.carpiLevel.setOnClickListener {

                puan = 0

                seviyeIkiKronometre.setText(requireContext().getString(R.string.tekrar_oyna))
                imageViewKronometre2.visibility = View.GONE

                dialog!!.dismiss()

            }

            dialog!!.reklamIzleVeDevamEtLevel.setOnClickListener {

                dialog!!.dismiss()

                if (mRewardedAd != null) {

                    mRewardedAd?.show(activity, OnUserEarnedRewardListener() {


                        fun onUserEarnedReward(rewardItem: RewardItem) {

                            var rewardAmount = rewardItem.amount
                            var rewardType = rewardItem.type
                            println("User earned the reward.")

                            seviyeIkiKronometre.setText(requireContext().getString(R.string.oyuna_devam))
                            seviyeIkiKronometre.setOnClickListener {

                                startingTheGame()

                            }

                            loadRewardedAds()
                        }
                        onUserEarnedReward(it)

                    })
                } else {
                    println("The rewarded ad wasn't ready yet.")
                    Toast.makeText(context,requireContext().getString(R.string.reklam_yuklenemedi),Toast.LENGTH_LONG).show()

                }


            }

            //dialog!!.oyunBittiLevelText.text = "LEVEL ${level}"
            dialog!!.oyunBittiLevelText.text = requireContext().getString(R.string.level_buyuk) + " " + level.toString()

            dialog!!.progressbarLevelOyunBitti.max = levelPuanArrayForLevelMod2[levelPuanIndex]
            dialog!!.progressbarLevelOyunBitti.progress = puan

            val yuzdelik  = (puan.toDouble() / levelPuanArrayForLevelMod2[levelPuanIndex].toDouble()) *100
            dialog!!.levelYuzdelik.text = "%${yuzdelik.toInt()}"

            if ( yuzdelik > 75 ) {

                dialog!!.levelTextAciklama.text = requireContext().getString(R.string.tuh_be_az_kalmisti)

            } else if ( yuzdelik > 49) {

                dialog!!.levelTextAciklama.text = requireContext().getString(R.string.yarisini_bitirdin_bile)

            } else if ( yuzdelik > 35) {

                dialog!!.levelTextAciklama.text = requireContext().getString(R.string.neredeyse_yarisina_geldin)

            } else {

                dialog!!.levelTextAciklama.text = requireContext().getString(R.string.bir_daha_dene)

            }


            dialog!!.show()

        }

        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

    }


    private fun showDialog2() {



        dialog = activity?.let { Dialog(it) }


        if (dialog != null){

            dialog!!.setContentView(R.layout.pop_up_sonsuz_mod)

            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.setCancelable(false)


            dialog!!.tekrarOynaButton.setOnClickListener {

                puan = 0

                levelForSpeed = 1

                dialog!!.dismiss()

                handler.removeCallbacks(runnable2)

                startingTheGame2()

            }



            dialog!!.carpi.setOnClickListener {

                puan = 0

                seviyeIkiKronometre.setText(requireContext().getString(R.string.tekrar_oyna))
                imageViewKronometre2.visibility = View.GONE

                dialog!!.dismiss()

            }

            dialog!!.reklamIzleVeDevamEt.setOnClickListener {

                dialog!!.dismiss()

                if (mRewardedAd != null) {

                    mRewardedAd?.show(activity, OnUserEarnedRewardListener() {


                        fun onUserEarnedReward(rewardItem: RewardItem) {

                            var rewardAmount = rewardItem.amount
                            var rewardType = rewardItem.type
                            println("User earned the reward.")

                            seviyeIkiKronometre.setText(requireContext().getString(R.string.oyuna_devam))
                            seviyeIkiKronometre.setOnClickListener {

                                startingTheGame2()

                            }

                            loadRewardedAds()
                        }
                        onUserEarnedReward(it)

                    })
                } else {
                    println("The rewarded ad wasn't ready yet.")
                    Toast.makeText(context,requireContext().getString(R.string.reklam_yuklenemedi),Toast.LENGTH_LONG).show()

                }


            }

            dialog!!.skorunRakam.text = "${puan}"

            val eskiSkor = preferences.getInt("skor2",-1)

            if (puan > eskiSkor) {

                preferences?.edit()?.putInt("skor2",puan)?.apply()
                dialog!!.enYuksekSkorText.text = "${requireContext().getString(R.string.en_yuksek_skor)} : " + puan

            } else {

                dialog!!.enYuksekSkorText.text = "${requireContext().getString(R.string.en_yuksek_skor)} : " + eskiSkor

            }


            val account = GoogleSignIn.getLastSignedInAccount(context)

            if ( account != null) {

                Games.getLeaderboardsClient(context, account)
                    .submitScore(getString(R.string.leaderboard_id_2), puan.toLong())

            }


            dialog!!.en_yuksek_skorlar.setOnClickListener {

                showLeaderboard()

            }


            dialog!!.show()

        }

        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

    }


    private fun levels() {


        if ( level == 1){

            geriSaymaSuresi = 15300
            degistirmePeriyodu = 2500

        } else if ( level == 2){

            geriSaymaSuresi = 14300
            degistirmePeriyodu = 2300

        } else if ( level == 3){

            geriSaymaSuresi = 13800
            degistirmePeriyodu = 2000

        } else if ( level == 4){

            geriSaymaSuresi = 13400
            degistirmePeriyodu = 1800

        } else if ( level == 5){

            geriSaymaSuresi = 13000
            degistirmePeriyodu = 1600

        } else if ( level == 6){

            geriSaymaSuresi = 12500
            degistirmePeriyodu = 1400

        } else if ( level == 7){

            geriSaymaSuresi = 12000
            degistirmePeriyodu = 1300

        } else if ( level == 8){

            geriSaymaSuresi = 11500
            degistirmePeriyodu = 1200

        } else if ( level == 9){

            geriSaymaSuresi = 11000
            degistirmePeriyodu = 1150

        } else if ( level == 10){

            geriSaymaSuresi = 10000
            degistirmePeriyodu = 1100

        } else if ( level == 11){

            geriSaymaSuresi = 9900
            degistirmePeriyodu = 1000

        } else if ( level == 12){

            geriSaymaSuresi = 9800
            degistirmePeriyodu = 950

        } else if ( level == 13){

            geriSaymaSuresi = 9700
            degistirmePeriyodu = 900

        } else if ( level == 14){

            geriSaymaSuresi = 9600
            degistirmePeriyodu = 890

        } else if ( level == 15){

            geriSaymaSuresi = 9500
            degistirmePeriyodu = 880

        } else if ( level == 16){

            geriSaymaSuresi = 9500
            degistirmePeriyodu = 860

        }

    }


    private fun levelsForSpeed() {


        if ( levelForSpeed == 1){

            geriSaymaSuresi = 15300
            degistirmePeriyodu = 2500

        } else if ( levelForSpeed == 2){

            geriSaymaSuresi = 14300
            degistirmePeriyodu = 2300

        } else if ( levelForSpeed == 3){

            geriSaymaSuresi = 13800
            degistirmePeriyodu = 2000

        } else if ( levelForSpeed == 4){

            geriSaymaSuresi = 13400
            degistirmePeriyodu = 1800

        } else if ( levelForSpeed == 5){

            geriSaymaSuresi = 13000
            degistirmePeriyodu = 1600

        } else if ( levelForSpeed == 6){

            geriSaymaSuresi = 12500
            degistirmePeriyodu = 1400

        } else if ( levelForSpeed == 7){

            geriSaymaSuresi = 12000
            degistirmePeriyodu = 1300

        } else if ( levelForSpeed == 8){

            geriSaymaSuresi = 11500
            degistirmePeriyodu = 1200

        } else if ( levelForSpeed == 9){

            geriSaymaSuresi = 11000
            degistirmePeriyodu = 1150

        } else if ( levelForSpeed == 10){

            geriSaymaSuresi = 10000
            degistirmePeriyodu = 1100

        } else if ( levelForSpeed == 11){

            geriSaymaSuresi = 9900
            degistirmePeriyodu = 1000

        } else if ( levelForSpeed == 12){

            geriSaymaSuresi = 9800
            degistirmePeriyodu = 950

        } else if ( levelForSpeed == 13){

            geriSaymaSuresi = 9700
            degistirmePeriyodu = 900

        } else if ( levelForSpeed == 14){

            geriSaymaSuresi = 9600
            degistirmePeriyodu = 880

        } else if ( levelForSpeed == 15){

            geriSaymaSuresi = 9500
            degistirmePeriyodu = 880

        } else if ( levelForSpeed == 16){

            geriSaymaSuresi = 9500
            degistirmePeriyodu = 860

        }


    }


    override fun onDetach() {
        super.onDetach()

        geriSayan?.cancel()
        baslangicGeriSayan?.cancel()

    }

    private fun showLeaderboard() {

        val account = GoogleSignIn.getLastSignedInAccount(context)

        if ( account != null) {

            Games.getLeaderboardsClient(context, account)
                .getLeaderboardIntent(requireContext().getString(R.string.leaderboard_id_2))
                .addOnSuccessListener { intent -> startActivityForResult(intent, RC_LEADERBOARD_UI) }

        } else {

            Toast.makeText(context,"En Yüksek Puanları görebilmeniz için " +
                    "Google hesabınızla giriş yapmalısınız.",Toast.LENGTH_LONG).show()

        }

    }




}