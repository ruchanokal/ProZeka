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
import com.ruchanokal.mathmaster.adapter.ViewPagerAdapterYedek
import com.ruchanokal.mathmaster.model.ScreenItem
import kotlinx.android.synthetic.main.fragment_seviye_bir.*
import kotlinx.android.synthetic.main.fragment_seviye_iki.*
import kotlinx.android.synthetic.main.fragment_seviye_uc.*
import kotlinx.android.synthetic.main.oyun_bitti.*
import kotlinx.android.synthetic.main.pop_op_level.*
import kotlinx.android.synthetic.main.pop_up_sonsuz_mod.*
import java.util.*


class SeviyeUcFragment : Fragment() {

    private val RC_LEADERBOARD_UI = 9004
    // Seviye-3 Banner :: ca-app-pub-5016889744069609/4716869707
    // Test ID :: ca-app-pub-3940256099942544/6300978111

    private var mRewardedAd: RewardedAd? = null

    lateinit var viewPagerAdapter: PagerAdapter
    //var mList = arrayListOf<ScreenItem>()
    var arrayOfLayouts = arrayOf(R.layout.layout_seviye3_bilgilendirme_1,
    R.layout.layout_seviye3_bilgilendirme_2,
    R.layout.layout_seviye3_bilgilendirme_3,
    R.layout.layout_seviye3_bilgilendirme_4,
    R.layout.layout_seviye3_bilgilendirme_5)


    var position = 0
    var puan = 0
    var oyunBittiMi = false


    lateinit var preferences : SharedPreferences
    private var backPressedTime : Long = 0
    var dialog = activity?.let { Dialog(it) }
    val levelTextViewArray = arrayListOf<TextView>()

    val levelPuanArrayForSonsuzMod3 = arrayOf(100,220,350,500,650,800,950,1100,1250,1400,1550,1700,1850,2000,2200,2400,2600)
    val levelPuanArrayForLevelMod3 = arrayOf(100,120,130,150,180,200,220,250,270,300,320,360,400,430,470,500,600)


    var geriSaymaSuresi : Long = 15000
    var levelPuanIndex = 0
    var level = 1
    var levelForSpeed = 1


    var runnable : Runnable = Runnable {  }
    var handler : Handler = Handler(Looper.getMainLooper())
    private lateinit var adRequestForReward : AdRequest


    companion object{
        var geriSayan : CountDownTimer? = null
        var baslangicGeriSayan : CountDownTimer? = null

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View =  inflater.inflate(R.layout.fragment_seviye_uc, container, false)


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

                    var enYuksekSkor = ""


                    val eskiSkor = preferences.getInt("skor3",0)

                    if (puan > eskiSkor) {

                        preferences?.edit()?.putInt("skor3",puan)?.apply()
                        enYuksekSkor = puan.toString()

                    } else {
                        enYuksekSkor = eskiSkor.toString()
                    }

                    val action = SeviyeUcFragmentDirections.actionSeviyeUcFragmentToAnaFragment(enyuksekskor3 = enYuksekSkor,levelucuncuoyun = level)
                    Navigation.findNavController(v).navigate(action)




                } else {

                    Toast.makeText(context,requireActivity().getString(R.string.cikis_yapmak_icin_tekrar_dokunun),Toast.LENGTH_LONG).show()

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
        adView3.loadAd(adRequest)

        adRequestForReward = AdRequest.Builder().build()

        loadRewardedAds()

        levelsLayout3.visibility = View.GONE
        oyunEkraniLayout3.visibility = View.GONE
        seviyeUcOyunBaslangicText.visibility = View.GONE

        preferences = activity?.getSharedPreferences("ucuncu", Context.MODE_PRIVATE)!!
        val isChecked = preferences.getBoolean("isChecked",false)

        if (isChecked == true) {

            levelsLayout3.visibility = View.VISIBLE
            introScreenLayout3.visibility = View.GONE

            levelModu3.setOnClickListener {
                levelDetay()
            }

            sonsuzMod3.setOnClickListener {
                oyunBaslangici2()
            }



        } else {

            checkboxForIntro3.visibility = View.GONE

            viewPagerAdapter = context?.let { ViewPagerAdapterYedek(it) }!!
            screenPager3.adapter = viewPagerAdapter
            tablayoutSeviye3.setupWithViewPager(screenPager3)

            devamButton3.setOnClickListener {

                position = screenPager3.currentItem

                if (position < arrayOfLayouts.size){

                    position++
                    screenPager3.currentItem = position

                }

                if (position == arrayOfLayouts.size - 1 ) {

                    checkboxForIntro3.visibility = View.VISIBLE
                    tablayoutSeviye3.visibility = View.GONE

                } else if ( position == arrayOfLayouts.size) {


                    introScreenLayout3.visibility = View.GONE
                    levelsLayout3.visibility = View.VISIBLE


                }


            }

            tablayoutSeviye3.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {

                    if(tab.position == arrayOfLayouts.size - 1 ){

                        checkboxForIntro3.visibility = View.VISIBLE
                        tablayoutSeviye3.visibility = View.GONE

                    } else if ( position == arrayOfLayouts.size) {

                        introScreenLayout3.visibility = View.GONE
                        levelsLayout3.visibility = View.VISIBLE

                    }

                }

                override fun onTabUnselected(tab: TabLayout.Tab) {

                }

                override fun onTabReselected(tab: TabLayout.Tab) {

                }

            })

            checkboxForIntro3.setOnCheckedChangeListener { compoundButton, b ->

                if (compoundButton.isChecked){

                    preferences.edit().putBoolean("isChecked",true).apply()

                } else {

                    preferences.edit().putBoolean("isChecked",false).apply()

                }

            }

            levelModu3.setOnClickListener {

                levelDetay()

            }

            sonsuzMod3.setOnClickListener {

                oyunBaslangici2()
            }



        }



    }

    private fun loadRewardedAds() {

        // Test ID :: ca-app-pub-3940256099942544/5224354917
        // Real ID :: ca-app-pub-5016889744069609/3172139152

        RewardedAd.load(context,"ca-app-pub-5016889744069609/3172139152", adRequestForReward, object : RewardedAdLoadCallback() {
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


        levelsLayout3.visibility = View.GONE
        introScreenLayout3.visibility = View.GONE
        //oyunuBaslatma()

        levelTextViewArray.add(levelbirSeviye3)
        levelTextViewArray.add(levelikiSeviye3)
        levelTextViewArray.add(levelucSeviye3)
        levelTextViewArray.add(leveldortSeviye3)
        levelTextViewArray.add(levelbesSeviye3)
        levelTextViewArray.add(levelaltiSeviye3)
        levelTextViewArray.add(levelyediSeviye3)
        levelTextViewArray.add(levelsekizSeviye3)
        levelTextViewArray.add(leveldokuzSeviye3)
        levelTextViewArray.add(levelonSeviye3)
        levelTextViewArray.add(levelonbirSeviye3)
        levelTextViewArray.add(levelonikiSeviye3)
        levelTextViewArray.add(levelonucSeviye3)
        levelTextViewArray.add(levelondortSeviye3)
        levelTextViewArray.add(levelonbesSeviye3)
        levelTextViewArray.add(levelonaltiSeviye3)


        var levelSayisi = 0
        level = preferences.getInt("level",1)

        while ( levelSayisi < level) {

            levelTextViewArray[levelSayisi].text = (levelSayisi+1).toString()
            levelTextViewArray[levelSayisi].setTextColor(Color.parseColor("#C89635"))
            levelTextViewArray[levelSayisi].setBackgroundResource(R.drawable.border_for_popup3)

            levelSayisi++
        }

        levellarPage3.visibility = View.VISIBLE

        for ( a in 1..levelSayisi) {

            levelTextViewArray[a-1].setOnClickListener {

                levellarPage3.visibility = View.GONE

                oyunBaslangici()

                level = a


            }

        }



    }


    private  fun oyunBaslangici() {

        puanCardView3.visibility = View.GONE
        progressFrameLayout3.visibility = View.VISIBLE

        level3ProgressBar.progress = 0

        seviyeUcOyunBaslangicText.visibility = View.VISIBLE
        introScreenLayout3.visibility = View.GONE
        levelsLayout3.visibility = View.GONE


        baslangicGeriSayan = object : CountDownTimer(3200,1000) {
            override fun onTick(p0: Long) {

                seviyeUcOyunBaslangicText.text = ((p0)/1000).toString()

            }

            override fun onFinish() {

                relativeLayoutForBanner3.visibility = View.VISIBLE
                seviyeUcOyunBaslangicText.visibility = View.GONE
                oyunEkraniLayout3.visibility = View.VISIBLE


                startingTheGame()


            }


        }.start()


    }


    private fun oyunBaslangici2() {

        puanCardView3.visibility = View.VISIBLE

        progressFrameLayout3.visibility = View.GONE

        seviyeUcOyunBaslangicText.visibility = View.VISIBLE
        introScreenLayout3.visibility = View.GONE
        levelsLayout3.visibility = View.GONE


        baslangicGeriSayan = object : CountDownTimer(3200,1000) {
            override fun onTick(p0: Long) {

                seviyeUcOyunBaslangicText.text = ((p0)/1000).toString()

            }

            override fun onFinish() {

                relativeLayoutForBanner3.visibility = View.VISIBLE
                seviyeUcOyunBaslangicText.visibility = View.GONE
                oyunEkraniLayout3.visibility = View.VISIBLE

                startingTheGame2()

            }


        }.start()



    }


    private fun startingTheGame() {

        oyunBittiMi = false

        ucuncuSeviyeUcuncuSutun.visibility = View.VISIBLE

        levels()

        levelPuanIndex = level - 1

        level3ProgressBar.max = levelPuanArrayForLevelMod3[levelPuanIndex]
        level3ProgressBar.progress = puan


        ////////////////////////?????????????????////////////////////////////
        ////////////////////////?????????????????////////////////////////////
        ////////////////////////?????????????????////////////////////////////

        //puanSayaciSeviyeUc.setText(puan.toString()+"/"+levelPuanArrayForLevelMod3[levelPuanIndex])


        levelTextSeviyeUc.text = requireContext().getString(R.string.level_buyuk) + " " + level.toString()

        imageViewKronometre3.visibility = View.VISIBLE

        val textArray = arrayListOf<TextView>()
        val textArrayForThird = arrayListOf<TextView>()

        textArray.add(renk1Text)
        textArray.add(renk2Text)
        textArray.add(renk3Text)
        textArray.add(renk4Text)
        textArray.add(renk5Text)
        textArray.add(renk6Text)
        textArray.add(renk7Text)
        textArray.add(renk8Text)
        textArray.add(renk9Text)
        textArray.add(renk10Text)
        textArray.add(renk11Text)
        textArray.add(renk12Text)
        textArray.add(renk13Text)
        textArray.add(renk14Text)
        textArray.add(renk15Text)
        textArray.add(renk16Text)
        textArray.add(renk17Text)
        textArray.add(renk18Text)

        textArrayForThird.add(renk1Text)
        textArrayForThird.add(renk2Text)
        textArrayForThird.add(renk3Text)
        textArrayForThird.add(renk4Text)
        textArrayForThird.add(renk5Text)
        textArrayForThird.add(renk6Text)
        textArrayForThird.add(renk7Text)
        textArrayForThird.add(renk8Text)
        textArrayForThird.add(renk9Text)
        textArrayForThird.add(renk10Text)
        textArrayForThird.add(renk11Text)
        textArrayForThird.add(renk12Text)


        var goreceliArray = arrayOf(requireActivity().getString(R.string.metin),
            requireActivity().getString(R.string.renk))

        var renkMetinDizisi = arrayOf(requireActivity().getString(R.string.kirmizi),
            requireActivity().getString(R.string.mavi),
            requireActivity().getString(R.string.sari),
            requireActivity().getString(R.string.turuncu),
            requireActivity().getString(R.string.yesil),
            requireActivity().getString(R.string.siyah),
            requireActivity().getString(R.string.gri))


        var renkDizisi = arrayOf("#ff0000","#0000ff","#ffc000","#ff8000","#008000","#000000","#808080")

        if ( level > 11) {

            goreceliArray = arrayOf(requireActivity().getString(R.string.metin),
                requireActivity().getString(R.string.renk),
                requireActivity().getString(R.string.ayni))



            renkMetinDizisi = arrayOf(requireActivity().getString(R.string.kirmizi),
                requireActivity().getString(R.string.mavi),
                requireActivity().getString(R.string.sari),
                requireActivity().getString(R.string.turuncu),
                requireActivity().getString(R.string.mor),
                requireActivity().getString(R.string.yesil),
                requireActivity().getString(R.string.siyah),
                requireActivity().getString(R.string.gri),
                requireActivity().getString(R.string.pembe))


            renkDizisi = arrayOf("#ff0000","#0000ff","#ffc000","#ff8000","#800080","#008000","#000000","#808080","#FF1493")

        } else if ( level > 5 ) {

            goreceliArray = arrayOf(requireActivity().getString(R.string.metin),
                requireActivity().getString(R.string.renk),
                requireActivity().getString(R.string.ayni))

        }


        goreceliText.text = goreceliArray[Random().nextInt(goreceliArray.size)]

        renkSecimi.text = renkMetinDizisi[Random().nextInt(renkMetinDizisi.size)]
        renkSecimi.setTextColor(Color.parseColor(renkDizisi[Random().nextInt(renkDizisi.size)]))

        val secilenRenkTexti = renkSecimi.text.toString()
        val secilenRenk = renkSecimi.currentTextColor.toString()

        var randomRenk = 0
        var randomRenkMetni = 0
        var x = 0

        if (goreceliText.text.toString().equals(requireActivity().getString(R.string.ayni))) {

            for (a in textArrayForThird) {

                randomRenk = Random().nextInt(renkDizisi.size)
                randomRenkMetni = Random().nextInt(renkMetinDizisi.size)

                val randomColor = Color.parseColor(renkDizisi[randomRenk])
                a.setText(renkMetinDizisi[randomRenkMetni])

                a.setTextColor(randomColor)

                if ( randomRenk == randomRenkMetni ) {

                    x++

                }

            }


        } else {

            for (a in textArray) {

                randomRenk = Random().nextInt(renkDizisi.size)
                randomRenkMetni = Random().nextInt(renkMetinDizisi.size)

                val randomColor = Color.parseColor(renkDizisi[randomRenk])
                a.setText(renkMetinDizisi[randomRenkMetni])

                a.setTextColor(randomColor)

            }

        }


        var kacTane = 0

        if (goreceliText.text.toString().equals(requireActivity().getString(R.string.metin))){


            for (a in textArray){


                if (secilenRenkTexti.equals(a.text.toString())) {

                    kacTane++

                }

            }

        } else if (goreceliText.text.toString().equals(requireActivity().getString(R.string.renk))){

            for (a in textArray){

                val renk = a.currentTextColor.toString()


                if (secilenRenk.equals(renk)) {

                    kacTane++

                }

            }

        } else {


            renkSecimi.text = "${requireActivity().getString(R.string.metin)} = ${requireActivity().getString(R.string.renk)}"
            renkSecimi.setTextColor(Color.BLACK)

            ucuncuSeviyeUcuncuSutun.visibility = View.GONE

            kacTane = x


        }



        var sayiAraligi = 0

        if (kacTane < 5) {

            sayiAraligi = 5

        } else if (kacTane < 10) {

            sayiAraligi = 10

        } else if ( kacTane < 15) {

            sayiAraligi = 15

        } else if ( kacTane < 19) {

            sayiAraligi = 18
        }


        var randomSecenek1 = Random().nextInt(sayiAraligi)

        while ( randomSecenek1 == kacTane) {

            randomSecenek1 = Random().nextInt(sayiAraligi)

        }

        var randomSecenek2 = Random().nextInt(sayiAraligi)

        while ( randomSecenek2 == kacTane || randomSecenek2 == randomSecenek1) {

            randomSecenek2 = Random().nextInt(sayiAraligi)

        }

        var randomSecenek3 = Random().nextInt(sayiAraligi)

        while ( randomSecenek3 == kacTane || randomSecenek3 == randomSecenek1 || randomSecenek3 == randomSecenek2) {

            randomSecenek3 = Random().nextInt(sayiAraligi)

        }





        val secenekArray = arrayOf(kacTane, randomSecenek1, randomSecenek2, randomSecenek3)

        var birincisi = secenekArray[Random().nextInt(secenekArray.size)]
        var ikincisi = secenekArray[Random().nextInt(secenekArray.size)]

        while ( ikincisi == birincisi) {

            ikincisi = secenekArray[Random().nextInt(secenekArray.size)]

        }

        var ucuncusu = secenekArray[Random().nextInt(secenekArray.size)]

        while ( ucuncusu == birincisi || ucuncusu == ikincisi) {

            ucuncusu = secenekArray[Random().nextInt(secenekArray.size)]

        }

        var dorduncusu = secenekArray[Random().nextInt(secenekArray.size)]

        while ( dorduncusu == birincisi || dorduncusu == ikincisi  || dorduncusu == ucuncusu) {

            dorduncusu = secenekArray[Random().nextInt(secenekArray.size)]

        }



        birinciSecenek.text = birincisi.toString()
        ikinciSecenek.text = ikincisi.toString()
        ucuncuSecenek.text = ucuncusu.toString()
        dorduncuSecenek.text = dorduncusu.toString()

        val buttonArray = arrayOf(birinciSecenek,
            ikinciSecenek,
            ucuncuSecenek,
            dorduncuSecenek)

        for (z in buttonArray) {

            z.setBackgroundColor(Color.parseColor("#FF6200EE"))
            //z.setBackgroundColor(resources.getColor(R.color.design_default_color_primary))

        }



        geriSayan = object : CountDownTimer(geriSaymaSuresi,1000){
            override fun onTick(p0: Long) {

                seviyeUcKronometre.text = ((p0)/1000).toString()


            }

            override fun onFinish() {

                seviyeUcKronometre.text = requireActivity().getString(R.string.sure_bitti)

                for (c in 0..3) {

                    buttonArray[c].isClickable = false
                    buttonArray[c].setBackgroundColor(Color.parseColor("#808080"))

                }

                for (x in buttonArray) {

                    if ( x.text.toString().equals(kacTane.toString()) )  {

                        x.setBackgroundColor(Color.parseColor("#00cc00"))

                    }
                }


                showDialog()

                seviyeUcKronometre.setOnClickListener {

                    startingTheGame()

                }


                //puanSayaciSeviyeUc.text = "Skorun: ${puan}"


            }


        }.start()




        for (c in 0..3) {

            buttonArray[c].setOnClickListener {

                geriSayan?.cancel()

                for (a in 0..3) {

                    buttonArray[a].isClickable = false

                }


                val tiklananSecenek = buttonArray[c].text.toString()

                if (tiklananSecenek.equals(kacTane.toString())) {

                    buttonArray[c].setBackgroundColor(Color.parseColor("#00cc00"))


                    puan = puan + 10

                    level3ProgressBar.progress = puan

                        if(puan == levelPuanArrayForLevelMod3[levelPuanIndex]){


                            if ( levelPuanIndex == 15 ) {

                                showOyunBittiDialog()

                                oyunBittiMi = true

                            } else {


                                levelPuanIndex++
                                level++

                            }

                            levels()


                            preferences.edit().putInt("level",level).apply()

                            //puanSayaciSeviyeUc.text = "${puan}/${levelPuanArrayForLevelMod3[levelPuanIndex]}"

                            puan = 0


                        }


                        if (oyunBittiMi == false) {

                            handler.postDelayed(object :Runnable {
                                override fun run() {

                                    startingTheGame()
                                }


                            },300)


                        } else {

                            seviyeUcKronometre.setOnClickListener {

                                puan = 0

                                startingTheGame()

                            }

                        }




                        } else {

                        for (a in 0..3) {

                            buttonArray[a].isClickable = false
                            buttonArray[a].setBackgroundColor(Color.parseColor("#808080"))

                        }

                        buttonArray[c].setBackgroundColor(Color.parseColor("#e50000"))

                        for (x in buttonArray) {

                            if ( x.text.toString().equals(kacTane.toString()) )  {

                                x.setBackgroundColor(Color.parseColor("#00cc00"))

                            }
                        }


                        handler.postDelayed( object :Runnable {
                            override fun run() {

                                showDialog()


                                seviyeUcKronometre.setOnClickListener {
                                    startingTheGame()
                                }




                            }

                        },600)


                    }


            }


        }


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

                seviyeUcKronometre.setText(requireActivity().getString(R.string.tekrar_oyna))
                imageViewKronometre3.visibility = View.GONE

                dialog!!.dismiss()

            }

            dialog!!.menuyeGitOyunBittiButton.setOnClickListener {

                dialog!!.dismiss()

                val action = SeviyeUcFragmentDirections.actionSeviyeUcFragmentToAnaFragment(levelucuncuoyun = level)
                NavHostFragment.findNavController(this).navigate(action)


            }





            dialog!!.show()

        }


    }


    private fun startingTheGame2() {

        levelsForSpeed()

        levelPuanIndex = levelForSpeed-1

        ucuncuSeviyeUcuncuSutun.visibility = View.VISIBLE


        val layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)
        puanCardView3.layoutParams = layoutParams

        puanSayaciSeviyeUc.text = puan.toString()
        imageViewKronometre3.visibility = View.VISIBLE

        val textArray = arrayListOf<TextView>()
        val textArrayForThird = arrayListOf<TextView>()

        textArray.add(renk1Text)
        textArray.add(renk2Text)
        textArray.add(renk3Text)
        textArray.add(renk4Text)
        textArray.add(renk5Text)
        textArray.add(renk6Text)
        textArray.add(renk7Text)
        textArray.add(renk8Text)
        textArray.add(renk9Text)
        textArray.add(renk10Text)
        textArray.add(renk11Text)
        textArray.add(renk12Text)
        textArray.add(renk13Text)
        textArray.add(renk14Text)
        textArray.add(renk15Text)
        textArray.add(renk16Text)
        textArray.add(renk17Text)
        textArray.add(renk18Text)

        textArrayForThird.add(renk1Text)
        textArrayForThird.add(renk2Text)
        textArrayForThird.add(renk3Text)
        textArrayForThird.add(renk4Text)
        textArrayForThird.add(renk5Text)
        textArrayForThird.add(renk6Text)
        textArrayForThird.add(renk7Text)
        textArrayForThird.add(renk8Text)
        textArrayForThird.add(renk9Text)
        textArrayForThird.add(renk10Text)
        textArrayForThird.add(renk11Text)
        textArrayForThird.add(renk12Text)



        var goreceliArray = arrayOf(requireActivity().getString(R.string.metin),
            requireActivity().getString(R.string.renk))

        var renkMetinDizisi = arrayOf(requireActivity().getString(R.string.kirmizi),
            requireActivity().getString(R.string.mavi),
            requireActivity().getString(R.string.sari),
            requireActivity().getString(R.string.turuncu),
            requireActivity().getString(R.string.yesil),
            requireActivity().getString(R.string.siyah),
            requireActivity().getString(R.string.gri))


        var renkDizisi = arrayOf("#ff0000","#0000ff","#ffc000","#ff8000","#008000","#000000","#808080")

        if ( levelForSpeed > 11 )  {

            goreceliArray = arrayOf(requireActivity().getString(R.string.metin),
                requireActivity().getString(R.string.renk),
                requireActivity().getString(R.string.ayni))


            renkMetinDizisi = arrayOf(requireActivity().getString(R.string.kirmizi),
                requireActivity().getString(R.string.mavi),
                requireActivity().getString(R.string.sari),
                requireActivity().getString(R.string.turuncu),
                requireActivity().getString(R.string.mor),
                requireActivity().getString(R.string.yesil),
                requireActivity().getString(R.string.siyah),
                requireActivity().getString(R.string.gri),
                requireActivity().getString(R.string.pembe))


            renkDizisi = arrayOf("#ff0000","#0000ff","#ffc000","#ff8000","#800080","#008000","#000000","#808080","#FF1493")

        } else if ( levelForSpeed > 5 ) {

            goreceliArray = arrayOf(requireActivity().getString(R.string.metin),
                requireActivity().getString(R.string.renk),
                requireActivity().getString(R.string.ayni))

        }

        goreceliText.text = goreceliArray[Random().nextInt(goreceliArray.size)]

        renkSecimi.text = renkMetinDizisi[Random().nextInt(renkMetinDizisi.size)]
        renkSecimi.setTextColor(Color.parseColor(renkDizisi[Random().nextInt(renkDizisi.size)]))

        val secilenRenkTexti = renkSecimi.text.toString()
        val secilenRenk = renkSecimi.currentTextColor.toString()

        var randomRenk = 0
        var randomRenkMetni = 0
        var x = 0

        if (goreceliText.text.toString().equals(requireActivity().getString(R.string.ayni))) {

            for (a in textArrayForThird) {

                randomRenk = Random().nextInt(renkDizisi.size)
                randomRenkMetni = Random().nextInt(renkMetinDizisi.size)

                val randomColor = Color.parseColor(renkDizisi[randomRenk])
                a.setText(renkMetinDizisi[randomRenkMetni])

                a.setTextColor(randomColor)

                if ( randomRenk == randomRenkMetni ) {

                    x++

                }

            }


        } else {

            for (a in textArray) {

                randomRenk = Random().nextInt(renkDizisi.size)
                randomRenkMetni = Random().nextInt(renkMetinDizisi.size)

                val randomColor = Color.parseColor(renkDizisi[randomRenk])
                a.setText(renkMetinDizisi[randomRenkMetni])

                a.setTextColor(randomColor)

            }


        }


        var kacTane = 0

        if (goreceliText.text.toString().equals(requireActivity().getString(R.string.metin))){


            for (a in textArray){


                if (secilenRenkTexti.equals(a.text.toString())) {

                    kacTane++

                }

            }

        } else if (goreceliText.text.toString().equals(requireActivity().getString(R.string.renk))){

            for (a in textArray){

                val renk = a.currentTextColor.toString()


                if (secilenRenk.equals(renk)) {

                    kacTane++

                }

            }

        } else {


            renkSecimi.text = "${requireActivity().getString(R.string.metin)} = ${requireActivity().getString(R.string.renk)}"
            renkSecimi.setTextColor(Color.BLACK)

            ucuncuSeviyeUcuncuSutun.visibility = View.GONE

            kacTane = x


        }



        var sayiAraligi = 0

        if (kacTane < 5) {

            sayiAraligi = 5

        } else if (kacTane < 10) {

            sayiAraligi = 10

        } else if ( kacTane < 15) {

            sayiAraligi = 15

        } else if ( kacTane < 19) {

            sayiAraligi = 18
        }


        var randomSecenek1 = Random().nextInt(sayiAraligi)

        while ( randomSecenek1 == kacTane) {

            randomSecenek1 = Random().nextInt(sayiAraligi)

        }

        var randomSecenek2 = Random().nextInt(sayiAraligi)

        while ( randomSecenek2 == kacTane || randomSecenek2 == randomSecenek1) {

            randomSecenek2 = Random().nextInt(sayiAraligi)

        }

        var randomSecenek3 = Random().nextInt(sayiAraligi)

        while ( randomSecenek3 == kacTane || randomSecenek3 == randomSecenek1 || randomSecenek3 == randomSecenek2) {

            randomSecenek3 = Random().nextInt(sayiAraligi)

        }





        val secenekArray = arrayOf(kacTane, randomSecenek1, randomSecenek2, randomSecenek3)

        var birincisi = secenekArray[Random().nextInt(secenekArray.size)]
        var ikincisi = secenekArray[Random().nextInt(secenekArray.size)]

        while ( ikincisi == birincisi) {

            ikincisi = secenekArray[Random().nextInt(secenekArray.size)]

        }

        var ucuncusu = secenekArray[Random().nextInt(secenekArray.size)]

        while ( ucuncusu == birincisi || ucuncusu == ikincisi) {

            ucuncusu = secenekArray[Random().nextInt(secenekArray.size)]

        }

        var dorduncusu = secenekArray[Random().nextInt(secenekArray.size)]

        while ( dorduncusu == birincisi || dorduncusu == ikincisi  || dorduncusu == ucuncusu) {

            dorduncusu = secenekArray[Random().nextInt(secenekArray.size)]

        }



        birinciSecenek.text = birincisi.toString()
        ikinciSecenek.text = ikincisi.toString()
        ucuncuSecenek.text = ucuncusu.toString()
        dorduncuSecenek.text = dorduncusu.toString()

        val buttonArray = arrayOf(birinciSecenek,
            ikinciSecenek,
            ucuncuSecenek,
            dorduncuSecenek)

        for (z in buttonArray) {

            z.setBackgroundColor(Color.parseColor("#FF6200EE"))
            //z.setBackgroundColor(resources.getColor(R.color.design_default_color_primary))

        }



        geriSayan = object : CountDownTimer(geriSaymaSuresi,1000){
            override fun onTick(p0: Long) {

                seviyeUcKronometre.text = ((p0)/1000).toString()


            }

            override fun onFinish() {


                for (c in 0..3) {

                    buttonArray[c].isClickable = false
                    buttonArray[c].setBackgroundColor(Color.parseColor("#808080"))

                }

                for (x in buttonArray) {

                    if ( x.text.toString().equals(kacTane.toString()) )  {

                        x.setBackgroundColor(Color.parseColor("#00cc00"))

                    }
                }

                seviyeUcKronometre.text = requireActivity().getString(R.string.sure_bitti)


                showDialog2()



                seviyeUcKronometre.setOnClickListener {

                    levelForSpeed =1

                    println("levelForSpeed 2: " + levelForSpeed)

                    startingTheGame2()

                }


                //puanSayaciSeviyeUc.text = "Skorun: ${puan}"


            }


        }.start()




        for (c in 0..3) {

            buttonArray[c].setOnClickListener {

                geriSayan?.cancel()

                for (a in 0..3) {

                    buttonArray[a].isClickable = false

                }


                val tiklananSecenek = buttonArray[c].text.toString()

                if (tiklananSecenek.equals(kacTane.toString())) {

                    buttonArray[c].setBackgroundColor(Color.parseColor("#00cc00"))
                    puan = puan + 10


                    if ( puan == levelPuanArrayForSonsuzMod3[levelPuanIndex]){

                        if ( levelPuanIndex != 15 ) {

                            levelPuanIndex++
                            levelForSpeed++
                            levelsForSpeed()

                        }

                    }

                    puanSayaciSeviyeUc.text = puan.toString()



                    handler.postDelayed(object :Runnable {
                        override fun run() {

                            startingTheGame2()

                        }


                    },300)


                } else {

                    for (a in 0..3) {

                        buttonArray[a].isClickable = false
                        buttonArray[a].setBackgroundColor(Color.parseColor("#808080"))

                    }

                    buttonArray[c].setBackgroundColor(Color.parseColor("#e50000"))

                    for (x in buttonArray) {

                        if ( x.text.toString().equals(kacTane.toString()) )  {

                            x.setBackgroundColor(Color.parseColor("#00cc00"))

                        }
                    }


                    handler.postDelayed( object :Runnable {
                        override fun run() {

                            showDialog2()


                            seviyeUcKronometre.setOnClickListener {
                                levelForSpeed = 1
                                startingTheGame2()
                            }



                        }

                    },600)


                }


            }


        }



    }

    private fun showDialog() {

        dialog = activity?.let { Dialog(it) }


        if (dialog != null){

            dialog!!.setContentView(R.layout.pop_op_level)

            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.setCancelable(false)


            dialog!!.tekrarOynaButtonLevel.setOnClickListener {

                puan = 0

                dialog!!.dismiss()

                startingTheGame()

            }

            dialog!!.carpiLevel.setOnClickListener {

                puan = 0

                seviyeUcKronometre.setText(requireActivity().getString(R.string.tekrar_oyna))
                imageViewKronometre3.visibility = View.GONE

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

                            seviyeUcKronometre.setText(requireActivity().getString(R.string.oyuna_devam))
                            seviyeUcKronometre.setOnClickListener {

                                startingTheGame()

                            }

                            loadRewardedAds()
                        }
                        onUserEarnedReward(it)

                    })
                } else {
                    println("The rewarded ad wasn't ready yet.")
                    Toast.makeText(context,requireActivity().getString(R.string.reklam_yuklenemedi),Toast.LENGTH_LONG).show()

                    seviyeUcKronometre.setText(requireActivity().getString(R.string.tekrar_oyna))

                    puan = 0

                    seviyeUcKronometre.setOnClickListener {

                        startingTheGame()

                    }


                }


            }

            //dialog!!.oyunBittiLevelText.text = "LEVEL ${level}"
            dialog!!.oyunBittiLevelText.text = requireContext().getString(R.string.level_buyuk) + " " + level.toString()

            dialog!!.progressbarLevelOyunBitti.max = levelPuanArrayForLevelMod3[levelPuanIndex]
            dialog!!.progressbarLevelOyunBitti.progress = puan

            val yuzdelik  = (puan.toDouble() / levelPuanArrayForLevelMod3[levelPuanIndex].toDouble()) *100

            dialog!!.levelYuzdelik.text = "%${yuzdelik.toInt()}"


            if ( yuzdelik > 75 ) {

                dialog!!.levelTextAciklama.text = requireActivity().getString(R.string.tuh_be_az_kalmisti)

            } else if ( yuzdelik > 49) {

                dialog!!.levelTextAciklama.text = requireActivity().getString(R.string.yarisini_bitirdin_bile)

            } else if ( yuzdelik > 35) {

                dialog!!.levelTextAciklama.text = requireActivity().getString(R.string.neredeyse_yarisina_geldin)

            } else {

                dialog!!.levelTextAciklama.text = requireActivity().getString(R.string.bir_daha_dene)

            }

            dialog!!.show()

        }


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

                startingTheGame2()

            }



            dialog!!.carpi.setOnClickListener {

                levelForSpeed = 1

                puan = 0

                seviyeUcKronometre.setText(requireActivity().getString(R.string.tekrar_oyna))
                imageViewKronometre3.visibility = View.GONE

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

                            seviyeUcKronometre.setText(requireActivity().getString(R.string.oyuna_devam))
                            seviyeUcKronometre.setOnClickListener {

                                startingTheGame2()
                            }

                            loadRewardedAds()
                        }
                        onUserEarnedReward(it)

                    })
                } else {
                    println("The rewarded ad wasn't ready yet.")
                    Toast.makeText(context,requireActivity().getString(R.string.reklam_yuklenemedi),Toast.LENGTH_LONG).show()

                    seviyeUcKronometre.setText(requireActivity().getString(R.string.tekrar_oyna))

                    puan = 0

                    seviyeUcKronometre.setOnClickListener {

                        startingTheGame()

                    }


                }


            }

            dialog!!.skorunRakam.text = "${puan}"

            val eskiSkor = preferences.getInt("skor3",-1)

            if (puan > eskiSkor) {

                preferences?.edit()?.putInt("skor3",puan)?.apply()
                dialog!!.enYuksekSkorText.text = "${requireActivity().getString(R.string.en_yuksek_skor)} : " + puan

            } else {

                dialog!!.enYuksekSkorText.text = "${requireActivity().getString(R.string.en_yuksek_skor)} : " + eskiSkor

            }

            val account = GoogleSignIn.getLastSignedInAccount(context)

            if ( account != null) {

                Games.getLeaderboardsClient(context, account)
                    .submitScore(getString(R.string.leaderboard_id_3), puan.toLong())

            }


            dialog!!.en_yuksek_skorlar.setOnClickListener {

                showLeaderboard()

            }


            dialog!!.show()

        }

    }



    private fun levels() {


        if ( level == 1){

            geriSaymaSuresi = 15300

        } else if ( level == 2){

            geriSaymaSuresi = 14300

        } else if ( level == 3){

            geriSaymaSuresi = 13800

        } else if ( level == 4){

            geriSaymaSuresi = 13400

        } else if ( level == 5){

            geriSaymaSuresi = 13000

        } else if ( level == 6){

            geriSaymaSuresi = 12500

        } else if ( level == 7){

            geriSaymaSuresi = 12000

        } else if ( level == 8){

            geriSaymaSuresi = 11500

        } else if ( level == 9){

            geriSaymaSuresi = 11000

        } else if ( level == 10){

            geriSaymaSuresi = 10000

        } else if ( level == 11){

            geriSaymaSuresi = 9900

        } else if ( level == 12){

            geriSaymaSuresi = 9800

        } else if ( level == 13){

            geriSaymaSuresi = 9700

        } else if ( level == 14){

            geriSaymaSuresi = 9700

        } else if ( level == 15){

            geriSaymaSuresi = 9600

        } else if ( level == 16){

            geriSaymaSuresi = 9500

        }

    }


    private fun levelsForSpeed() {


        if ( levelForSpeed == 1){

            geriSaymaSuresi = 15300

        } else if ( levelForSpeed == 2){

            geriSaymaSuresi = 14300

        } else if ( levelForSpeed == 3){

            geriSaymaSuresi = 13800

        } else if ( levelForSpeed == 4){

            geriSaymaSuresi = 13400

        } else if ( levelForSpeed == 5){

            geriSaymaSuresi = 13000

        } else if ( levelForSpeed == 6){

            geriSaymaSuresi = 12500

        } else if ( levelForSpeed == 7){

            geriSaymaSuresi = 12000

        } else if ( levelForSpeed == 8){

            geriSaymaSuresi = 11500

        } else if ( levelForSpeed == 9){

            geriSaymaSuresi = 11000

        } else if ( levelForSpeed == 10){

            geriSaymaSuresi = 10000

        } else if ( levelForSpeed == 11){

            geriSaymaSuresi = 9900

        } else if ( levelForSpeed == 12){

            geriSaymaSuresi = 9800

        } else if ( levelForSpeed == 13){

            geriSaymaSuresi = 9700

        } else if ( levelForSpeed == 14){

            geriSaymaSuresi = 9700

        } else if ( levelForSpeed == 15){

            geriSaymaSuresi = 9600

        } else if ( levelForSpeed == 16){

            geriSaymaSuresi = 9500

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
                .getLeaderboardIntent(requireContext().getString(R.string.leaderboard_id_3))
                .addOnSuccessListener { intent -> startActivityForResult(intent, RC_LEADERBOARD_UI) }

        } else {

            Toast.makeText(context,"En Yüksek Puanları görebilmeniz için " +
                    "Google hesabınızla giriş yapmalısınız.",Toast.LENGTH_LONG).show()


        }

    }


}