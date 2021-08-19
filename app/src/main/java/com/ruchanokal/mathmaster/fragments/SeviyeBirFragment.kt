package com.ruchanokal.mathmaster.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
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
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.games.Games
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.ruchanokal.mathmaster.R
import com.ruchanokal.mathmaster.adapter.RoomAdapter
import com.ruchanokal.mathmaster.adapter.ViewPagerAdapter
import com.ruchanokal.mathmaster.classes.RecyclerItemClickListener
import com.ruchanokal.mathmaster.model.ScreenItem
import kotlinx.android.synthetic.main.fragment_ana.*
import kotlinx.android.synthetic.main.fragment_seviye_bir.*
import kotlinx.android.synthetic.main.fragment_seviye_iki.*
import kotlinx.android.synthetic.main.fragment_seviye_uc.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.layout_coklu_mod_ayarlari.*
import kotlinx.android.synthetic.main.layout_onbes_saniye_bekleyin.*
import kotlinx.android.synthetic.main.layout_progress_dialog.*
import kotlinx.android.synthetic.main.oyun_berabere.*
import kotlinx.android.synthetic.main.oyun_bitti.*
import kotlinx.android.synthetic.main.oyun_bitti.carpiOyunBitti
import kotlinx.android.synthetic.main.oyun_bitti.menuyeGitOyunBittiButton
import kotlinx.android.synthetic.main.oyun_bitti.oyunBittiText
import kotlinx.android.synthetic.main.oyun_bitti.tekrarOyunBittiButton
import kotlinx.android.synthetic.main.oyunu_kaybettin.*
import kotlinx.android.synthetic.main.oyunu_kaybettin_puanla.*
import kotlinx.android.synthetic.main.oyunu_kazandin.*
import kotlinx.android.synthetic.main.oyunu_kazandin_puanla.*
import kotlinx.android.synthetic.main.oyunu_kazandin_puanla.nameBirinciOyuncuSonuc
import kotlinx.android.synthetic.main.oyunu_kazandin_puanla.nameIkinciOyuncuSonuc
import kotlinx.android.synthetic.main.oyunu_kazandin_puanla.puanSayaciBirinciOyuncuSonuc
import kotlinx.android.synthetic.main.oyunu_kazandin_puanla.puanSayaciIkinciOyuncuSonuc
import kotlinx.android.synthetic.main.pop_op_level.*
import kotlinx.android.synthetic.main.pop_up_sonsuz_mod.*
import kotlinx.coroutines.*
import java.lang.Runnable
import java.text.DecimalFormat
import java.util.*


class SeviyeBirFragment : Fragment() {

    private val RC_LEADERBOARD_UI = 9004
    // Seviye-1 Banner :: ca-app-pub-5016889744069609/7426793526
    // Test ID :: ca-app-pub-3940256099942544/6300978111

    private var mInterstitialAd: InterstitialAd? = null
    var reklamPeriyodu = 0

    private var mRewardedAd: RewardedAd? = null

    lateinit var db: FirebaseFirestore
    lateinit var db2: FirebaseFirestore
    lateinit var mAuth: FirebaseAuth
    var coklukullaniciInteger: Int = 0
    var sorgulanacakKullaniciAdi = "abcd"

    lateinit var progressDialog: ProgressDialog
    lateinit var progressDialogOyunBitti: ProgressDialog
    lateinit var progressDialogTekrarOyna : ProgressDialog
    var progressDialogOnBesSaniye: ProgressDialog? = null

    var modeInteger = 0
    var forExitInteger = 0
    var artikKazandinInteger = 0
    var oyunuExitleKazandin = 0
    var abc = 0
    var onemliDeger = 0
    var puanKontrol = 0
    var kontrolSayisi = 0
    var galibiyetSayisi = 0
    var maglubiyetSayisi = 0
    var beraberlikSayisi = 0
    var galibiyetOrani : Double? = null

    var rastgeleSayi : Int? = null
    var userUid: String? = null
    var kullaniciAdim2 = "ikinci"
    var email = ""
    var sonucString: String? = null
    var ikinciOyuncuKullaniciAdi: String? = null
    var job : Job? = null

    var yeniIndex = 855550

    var onPauseTime : Long? = null
    var onResumeTime : Long? = null


    var time2 : Long = 60000
    var time1 : Long = 60000

    lateinit var viewPagerAdapter: PagerAdapter
    var mList = arrayListOf<ScreenItem>()
    var position = 0
    var puan = 0
    var min = 20
    var max = 50
    var zorlukSeviyesi = 1
    var degistirmePeriyoduMulti = 4000
    var oyunBittiMi = false
    var puanSayaciIkinciOyuncu : TextView? = null
    var puanSayaciBirinciOyuncu : TextView? = null


    var reference: ListenerRegistration? = null
    var reference2: ListenerRegistration? = null
    var reference3: ListenerRegistration? = null
    var reference4: ListenerRegistration? = null
    var reference5: ListenerRegistration? = null
    var reference6: ListenerRegistration? = null
    var reference7: ListenerRegistration? = null
    var reference9: ListenerRegistration? = null
    var reference10: ListenerRegistration? = null
    var reference11: ListenerRegistration? = null
    var reference12: ListenerRegistration? = null
    var reference13: ListenerRegistration? = null
    var reference14: ListenerRegistration? = null
    var reference15: ListenerRegistration? = null
    var reference16: ListenerRegistration? = null
    var reference17: ListenerRegistration? = null
    var reference18: ListenerRegistration? = null
    var reference19: ListenerRegistration? = null
    var reference20: ListenerRegistration? = null
    var reference21: ListenerRegistration? = null
    var reference22: ListenerRegistration? = null
    var reference23: ListenerRegistration? = null

    var playerName = "abc"
    var roomName = "abc"

    lateinit var preferences: SharedPreferences
    private var backPressedTime: Long = 0
    var dialog = activity?.let { Dialog(it) }


    val numberArrayList = arrayListOf<TextView>()
    val levelTextViewArray = arrayListOf<TextView>()
    val roomsList = arrayListOf<String>()
    val zorlukList = arrayListOf<Long>()
    val indexList = arrayListOf<Long>()

    val levelPuanArrayForSonsuzMod = arrayOf(100, 220, 350, 500, 650, 800, 950, 1100, 1250, 1400, 1550, 1700, 1850, 2000, 2200, 2400, 2600)
    val levelPuanArrayForLevelMod =
        arrayOf(100, 120, 130, 150, 180, 200, 220, 250, 270, 300, 320, 360, 400, 430, 470, 500, 600)


    val minArray = arrayOf(20, 30, 30, 30, 30, 40, 40, 40, 40, 50, 50, 50, 50, 50, 60, 70)
    val maxArray =
        arrayOf(50, 70, 90, 100, 110, 115, 120, 125, 130, 140, 150, 160, 170, 180, 190, 200)


    var geriSaymaSuresi: Long = 15000
    var degistirmePeriyodu: Long = 2500
    var levelPuanIndex = 0
    var level = 1
    var levelForSpeed = 1
    private lateinit var adRequestForRewardAd: AdRequest
    private lateinit var adRequest: AdRequest
    private lateinit var roomAdapter : RoomAdapter
    private lateinit var oyuncularListesi : RecyclerView
    val galibiyetHashMap = hashMapOf<Any,Any>()

    var runnable: Runnable = Runnable { }
    var runnable2: Runnable = Runnable { }
    var runnable3: Runnable = Runnable { }
    var runnable4: Runnable = Runnable { }
    val handler: Handler = Handler(Looper.getMainLooper())

    companion object {
        var geriSayan: CountDownTimer? = null
        var geriSayanBirinci: CountDownTimer? = null
        var geriSayanIkinci: CountDownTimer? = null
        var baslangicGeriSayan: CountDownTimer? = null
        var onbesSaniyeGeriSayan: CountDownTimer? = null
        var oyunSonucuGeriSayan: CountDownTimer? = null

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view: View = inflater.inflate(R.layout.fragment_seviye_bir, container, false)


        return view
    }

    override fun onResume() {
        super.onResume()

        if (requireView() == null) {
            return
        }

        if ( rastgeleSayi == 555 ) {

            db.collection("Problems").document(userUid!!).delete()

            onResumeTime = System.currentTimeMillis()

            val aradakiSaniyeFarki = onResumeTime!! - onPauseTime!!

            if ( aradakiSaniyeFarki >= 15000 ){

                reklamPeriyodu++

                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(activity)
                }

                //kaybettin
                showKaybettinDialog()

                artikKazandinInteger = 10

                if (coklukullaniciInteger == 1) {

                    geriSayanBirinci?.cancel()
                    handler.removeCallbacks(runnable3)

                } else {

                    geriSayanIkinci?.cancel()
                    handler.removeCallbacks(runnable4)

                }



            }

            rastgeleSayi = 0
        }

        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { v, keyCode, event ->
            if (event.action === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {


                if (backPressedTime + 3000 > System.currentTimeMillis()) {

                    dialog?.dismiss()
                    geriSayan?.cancel()
                    baslangicGeriSayan?.cancel()

                    var enYuksekSkor = ""

                    val eskiSkor = preferences.getInt("skor", 0)

                    if (puan > eskiSkor) {

                        preferences?.edit()?.putInt("skor", puan)?.apply()
                        enYuksekSkor = puan.toString()


                    } else {
                        enYuksekSkor = eskiSkor.toString()
                    }

                    val action = SeviyeBirFragmentDirections.actionSeviyeBirFragmentToAnaFragment(
                        enyuksekskor1 = enYuksekSkor,
                        levelbirincioyun = level
                    )
                    Navigation.findNavController(v).navigate(action)



                } else {

                    if (modeInteger == 3) {


                        if ( artikKazandinInteger == 10 ) {

                            geriSayan?.cancel()
                            geriSayanBirinci?.cancel()
                            geriSayanIkinci?.cancel()
                            baslangicGeriSayan?.cancel()
                            onbesSaniyeGeriSayan?.cancel()

                            db.collection("Rooms").document(userUid!!).delete()

                            if (coklukullaniciInteger == 1 ){

                                db.collection("RoomIndex").document(roomName).delete()

                            }

                            val action = SeviyeBirFragmentDirections.actionSeviyeBirFragmentToAnaFragment()
                            NavHostFragment.findNavController(this).navigate(action)



                        } else {

                            val alert = AlertDialog.Builder(context)

                            alert.setTitle("Çıkış")

                            alert.setMessage("Çıkış yapmak istediğinize emin misiniz? Eğer çıkış yaparsanız hükmen mağlup sayılacaksınız!")

                            alert.setCancelable(false)

                            alert.setPositiveButton("Evet") { dialogInterface: DialogInterface, i: Int ->

                            geriSayan?.cancel()
                            geriSayanBirinci?.cancel()
                            geriSayanIkinci?.cancel()
                            baslangicGeriSayan?.cancel()
                            onbesSaniyeGeriSayan?.cancel()

                            handler.removeCallbacks(runnable3)
                            handler.removeCallbacks(runnable4)

                            db.collection("Rooms").document(userUid!!).delete()
                            db.collection("Puanlar").document(userUid!!).delete()

                            if (coklukullaniciInteger == 1){

                                db.collection("RoomIndex").document(roomName).delete()

                                oyuncularListesi.isEnabled = true

                            }


                            val action = SeviyeBirFragmentDirections.actionSeviyeBirFragmentToAnaFragment()
                            NavHostFragment.findNavController(this).navigate(action)

                            val hashMap = hashMapOf<Any, Any>()
                            hashMap.put("exitInteger", coklukullaniciInteger)
                            hashMap.put("roomName",roomName)


                            db.collection("Exits").document(roomName).set(hashMap).addOnFailureListener { exception ->

                                Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT).show()


                            }.addOnSuccessListener {

                                forExitInteger = 15
                            }



                            }

                            alert.setNegativeButton("Hayır") { dialogInterface: DialogInterface, i: Int ->



                            }

                            alert.show()


                        }




                    } else {

                        Toast.makeText(
                            context,
                            R.string.cikis_yapmak_icin_tekrar_dokunun,
                            Toast.LENGTH_LONG
                        ).show()

                    }



                }

                backPressedTime = System.currentTimeMillis()


                // handle back button's click listener
                true
            } else false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        birinciSonucTextSeviyeBir.text = ""
        ikinciSonucTextSeviyeBir.text = ""

        adRequest = AdRequest.Builder().build()
        adView1.loadAd(adRequest)

        adRequestForRewardAd = AdRequest.Builder().build()

        loadRewardedAds()



        db = FirebaseFirestore.getInstance()
        db2 = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()

        db.clearPersistence()
        db2.clearPersistence()


        puanTablosuCokOyunculu.visibility = View.GONE
        oyunEkraniLayout.visibility = View.GONE
        seviyeBirOyunBaslangicText.visibility = View.GONE
        levelsLayout.visibility = View.GONE
        puanCardView.visibility = View.GONE

        preferences = activity?.getSharedPreferences("birinci", Context.MODE_PRIVATE)!!

        val isChecked = preferences.getBoolean("isChecked", false)


        if (isChecked == true) {

            levelsLayout.visibility = View.VISIBLE
            introScreenLayout.visibility = View.GONE

            levelModu.setOnClickListener {

                levelDetay()

            }

            sonsuzMod.setOnClickListener {

                oyunuBaslatma2()

            }

            cokluModOyunuKur.setOnClickListener {

                cokluModOyunuKur()

                puanSayaciIkinciOyuncu = requireActivity().findViewById(R.id.puanSayaciIkinciOyuncu)
                puanSayaciBirinciOyuncu = requireActivity().findViewById(R.id.puanSayaciBirinciOyuncu)


            }


        } else {

            introScreenLayout.visibility = View.VISIBLE
            checkboxForIntro.visibility = View.GONE

            val birinciSayfa = ScreenItem(
                requireContext().getString(R.string.nasil_oynanir),
                requireContext().getString(R.string.nasil_oynanir_seviyebir_1),
                R.drawable.birincioyunbuyuk
            )
            val ikinciSayfa = ScreenItem(
                requireContext().getString(R.string.nasil_oynanir),
                requireContext().getString(R.string.nasil_oynanir_seviyebir_2),
                R.drawable.birincioyunanlatim
            )
            val ucuncuSayfa = ScreenItem(
                requireContext().getString(R.string.nasil_oynanir),
                requireContext().getString(R.string.nasil_oynanir_seviyebir_3),
                R.drawable.levelinfinitemod
            )
            val dorduncuSayfa = ScreenItem(
                requireContext().getString(R.string.nasil_oynanir),
                requireContext().getString(R.string.nasil_oynanir_seviyebir_4),
                R.drawable.levelinfinitemod
            )


            mList.add(birinciSayfa)
            mList.add(ikinciSayfa)
            mList.add(ucuncuSayfa)
            mList.add(dorduncuSayfa)

            viewPagerAdapter = ViewPagerAdapter(requireContext(), mList)
            screenPager.adapter = viewPagerAdapter
            tablayoutSeviye.setupWithViewPager(screenPager)

            devamButton.setOnClickListener {

                position = screenPager.currentItem

                if (position < mList.size) {

                    position++
                    screenPager.currentItem = position
                }

                if (position == mList.size - 1) {

                    checkboxForIntro.visibility = View.VISIBLE
                    tablayoutSeviye.visibility = View.GONE

                } else if (position == mList.size) {

                    buttonVeBilgilendirmeLayout.visibility = View.GONE
                    introScreenLayout.visibility = View.GONE
                    levelsLayout.visibility = View.VISIBLE

                }

            }

            tablayoutSeviye.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {

                    if (tab.position == mList.size - 1) {

                        checkboxForIntro.visibility = View.VISIBLE
                        tablayoutSeviye.visibility = View.GONE

                    } else if (position == mList.size) {

                        buttonVeBilgilendirmeLayout.visibility = View.GONE
                        introScreenLayout.visibility = View.GONE
                        levelsLayout.visibility = View.VISIBLE
                    }

                }

                override fun onTabUnselected(tab: TabLayout.Tab) {

                }

                override fun onTabReselected(tab: TabLayout.Tab) {

                }

            })

            checkboxForIntro.setOnCheckedChangeListener { compoundButton, b ->

                if (compoundButton.isChecked) {

                    preferences.edit().putBoolean("isChecked", true).apply()

                } else {

                    preferences.edit().putBoolean("isChecked", false).apply()

                }

            }

            levelModu.setOnClickListener {

                levelDetay()

            }

            sonsuzMod.setOnClickListener {

                oyunuBaslatma2()

            }

            cokluModOyunuKur.setOnClickListener {

                cokluModOyunuKur()

            }

        }


    }

    private fun loadRewardedAds() {

        //Test ID :: ca-app-pub-3940256099942544/5224354917
        // Real ID :: ca-app-pub-5016889744069609/6263839370

        RewardedAd.load(
            context,
            "ca-app-pub-5016889744069609/6263839370",
            adRequestForRewardAd,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {

                    mRewardedAd = null

                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {

                    mRewardedAd = rewardedAd

                }
            })

    }

    private fun levelDetay() {

        levelsLayout.visibility = View.GONE
        introScreenLayout.visibility = View.GONE
        //oyunuBaslatma()

        levelTextViewArray.add(levelbir)
        levelTextViewArray.add(leveliki)
        levelTextViewArray.add(leveluc)
        levelTextViewArray.add(leveldort)
        levelTextViewArray.add(levelbes)
        levelTextViewArray.add(levelalti)
        levelTextViewArray.add(levelyedi)
        levelTextViewArray.add(levelsekiz)
        levelTextViewArray.add(leveldokuz)
        levelTextViewArray.add(levelon)
        levelTextViewArray.add(levelonbir)
        levelTextViewArray.add(leveloniki)
        levelTextViewArray.add(levelonuc)
        levelTextViewArray.add(levelondort)
        levelTextViewArray.add(levelonbes)
        levelTextViewArray.add(levelonalti)

        var levelSayisi = 0
        level = preferences.getInt("level", 1)

        while (levelSayisi < level) {

            levelTextViewArray[levelSayisi].text = (levelSayisi + 1).toString()
            levelTextViewArray[levelSayisi].setTextColor(Color.parseColor("#C89635"))
            levelTextViewArray[levelSayisi].setBackgroundResource(R.drawable.border_for_popup3)

            levelSayisi++
        }

        levellarPage1.visibility = View.VISIBLE

        for (a in 1..levelSayisi) {

            levelTextViewArray[a - 1].setOnClickListener {

                levellarPage1.visibility = View.GONE

                oyunuBaslatma()

                level = a


            }

        }

    }

    private fun oyunuBaslatma() {

        puanCardView.visibility = View.GONE

        progressFrameLayout1.visibility = View.VISIBLE
        levelProgressBar.progress = 0

        seviyeBirOyunBaslangicText.visibility = View.VISIBLE
        introScreenLayout.visibility = View.GONE
        levelsLayout.visibility = View.GONE

        baslangicGeriSayan = object : CountDownTimer(3200, 1000) {
            override fun onTick(p0: Long) {

                seviyeBirOyunBaslangicText.text = ((p0) / 1000).toString()
            }

            override fun onFinish() {

                relativeLayoutForBanner1.visibility = View.VISIBLE
                seviyeBirOyunBaslangicText.visibility = View.GONE
                oyunEkraniLayout.visibility = View.VISIBLE

                startingTheGame()

            }

        }.start()

    }

    private fun oyunuBaslatma2() {

        puanCardView.visibility = View.VISIBLE
        progressFrameLayout1.visibility = View.GONE

        seviyeBirOyunBaslangicText.visibility = View.VISIBLE
        introScreenLayout.visibility = View.GONE
        levelsLayout.visibility = View.GONE

        baslangicGeriSayan = object : CountDownTimer(3200, 1000) {
            override fun onTick(p0: Long) {

                seviyeBirOyunBaslangicText.text = ((p0) / 1000).toString()

            }

            override fun onFinish() {

                relativeLayoutForBanner1.visibility = View.VISIBLE
                seviyeBirOyunBaslangicText.visibility = View.GONE
                oyunEkraniLayout.visibility = View.VISIBLE

                startingTheGame2()
            }

        }.start()

    }

    private fun cokluModOyunuKur() {


        cokluOyunOdasiLayout.visibility = View.VISIBLE
        puanTablosuCokOyunculu.visibility = View.VISIBLE
        progressFrameLayout1.visibility = View.GONE
        introScreenLayout.visibility = View.GONE
        levelsLayout.visibility = View.GONE


        startingTheGame3()

        sorgulanacakKullaniciAdi =
            arguments?.let { SeviyeBirFragmentArgs.fromBundle(it).kullaniciAdi }.toString()

        userUid?.let { it1 -> db.collection("Puanlar").document(it1).delete() }
        userUid?.let { it1 -> db.collection("Problems").document(it1).delete() }
        db.collection("RoomIndex").document(sorgulanacakKullaniciAdi).delete()
        db.collection("Rooms").document(userUid!!).delete()


    }

    private fun startingTheGame3() {

        oyuncularListesi = requireActivity().findViewById(R.id.oyuncularListesi)

        userUid = mAuth.currentUser?.uid!!

        coklukullaniciInteger = 5

        reklamPeriyodu = preferences.getInt("reklamPeriyodu",0)

        oyunKurmaButonu.setOnClickListener {

            oyunKurmaButonu.isEnabled = false

            dialog = activity?.let { Dialog(it) }

            if (dialog != null) {

                oyunKurmaButonu.isEnabled = true

                dialog!!.setContentView(R.layout.layout_coklu_mod_ayarlari)

                dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                dialog!!.oyunuKurCokluModZorluk.setOnClickListener {

                    if (dialog!!.temel.isChecked) {

                        dialog!!.dismiss()
                        zorlukSeviyesi = 1
                        oyunKurucusu()

                    } else if (dialog!!.kolay.isChecked ) {

                        dialog!!.dismiss()
                        zorlukSeviyesi = 2
                        oyunKurucusu()

                    } else if (dialog!!.orta.isChecked ) {

                        dialog!!.dismiss()
                        zorlukSeviyesi = 3
                        oyunKurucusu()

                    } else if (dialog!!.zor.isChecked ) {

                        dialog!!.dismiss()
                        zorlukSeviyesi = 4
                        oyunKurucusu()

                    } else if (dialog!!.uzman.isChecked ){

                        dialog!!.dismiss()
                        zorlukSeviyesi = 5
                        oyunKurucusu()

                    }


                }



                dialog!!.show()

            }


        }

        context?.let {
            RecyclerItemClickListener(it,oyuncularListesi,object : RecyclerItemClickListener.OnItemClickListener {

                override fun onItemClick(view: View, position: Int) {

                    oyuncularListesi.isEnabled = false

                    roomName = roomsList.get(position)

                    if (!sorgulanacakKullaniciAdi.equals(roomName)) {

                        var listeSayisi = 0

                        if ( indexList!= null && indexList.size > 0 ){

                            println("indexlist dolu")

                            for (index in indexList){

                                if ( position == index.toString().toInt()){

                                    Toast.makeText(context,"Bu oda doludur!",Toast.LENGTH_SHORT).show()

                                    break

                                } else {

                                    listeSayisi++
                                    println("listeSayisi: " + listeSayisi)

                                    if (listeSayisi == indexList.size){

                                        println("listeSayisi eşit")

                                            email = mAuth.currentUser?.email.toString()

                                            if (mAuth.currentUser!!.displayName != null && !mAuth.currentUser!!.displayName.equals("")) {


                                                kullaniciAdim2 = mAuth.currentUser!!.displayName.toString()

                                                playerName = kullaniciAdim2
                                                val hashMap2 = hashMapOf<Any, Any>()
                                                if (roomName != null) {
                                                    hashMap2.put("player2", roomName)
                                                    hashMap2.put("kullaniciAdim", kullaniciAdim2)
                                                }


                                                if (userUid != null) {
                                                    db.collection("Rooms").document(userUid!!).set(hashMap2).addOnSuccessListener {


                                                        coklukullaniciInteger = 2

                                                        roomListener()

                                                        val roomMap = hashMapOf<Any,Any>()
                                                        roomMap.put("roomlistIndex",position)

                                                        db.collection("RoomIndex").document(roomName).set(roomMap)


                                                    }
                                                }


                                            } else {

                                                reference15 = db.collection("Users").whereEqualTo("email", email)
                                                    .addSnapshotListener { value, error ->


                                                        if (error != null) {

                                                            Toast.makeText(context, "Bir hata oluştu", Toast.LENGTH_LONG).show()

                                                        } else {

                                                            if (value != null) {
                                                                if (!value.isEmpty) {

                                                                    val documents = value.documents

                                                                    for (document in documents) {

                                                                        kullaniciAdim2 = document.get("kullaniciAdi") as String

                                                                    }

                                                                    playerName = kullaniciAdim2
                                                                    val hashMap2 = hashMapOf<Any, Any>()
                                                                    if (roomName != null) {
                                                                        hashMap2.put("player2", roomName)
                                                                        hashMap2.put("kullaniciAdim", kullaniciAdim2)
                                                                    }


                                                                    if (userUid != null) {
                                                                        db.collection("Rooms").document(userUid!!).set(hashMap2)
                                                                            .addOnSuccessListener {

                                                                                coklukullaniciInteger = 2

                                                                                roomListener()

                                                                                val roomMap = hashMapOf<Any,Any>()
                                                                                roomMap.put("roomlistIndex",position)

                                                                                db.collection("RoomIndex").document(roomName).set(roomMap)

                                                                                reference15?.remove()



                                                                            }


                                                                    }


                                                                }

                                                            }


                                                        }

                                                    }

                                            }






                                    }

                                }

                            }

                        } else {

                            println("indexlist boş")

                                email = mAuth.currentUser?.email.toString()

                                if (mAuth.currentUser!!.displayName != null && !mAuth.currentUser!!.displayName.equals("")) {


                                    kullaniciAdim2 = mAuth.currentUser!!.displayName.toString()

                                    playerName = kullaniciAdim2
                                    val hashMap2 = hashMapOf<Any, Any>()
                                    if (roomName != null) {
                                        hashMap2.put("player2", roomName)
                                        hashMap2.put("kullaniciAdim", kullaniciAdim2)
                                    }


                                    if (userUid != null) {
                                        db.collection("Rooms").document(userUid!!).set(hashMap2).addOnSuccessListener {


                                            coklukullaniciInteger = 2

                                            roomListener()

                                            val roomMap = hashMapOf<Any,Any>()
                                            roomMap.put("roomlistIndex",position)

                                            db.collection("RoomIndex").document(roomName).set(roomMap)


                                        }
                                    }


                                } else {

                                    reference15 = db.collection("Users").whereEqualTo("email", email)
                                        .addSnapshotListener { value, error ->


                                            if (error != null) {

                                                Toast.makeText(context, "Bir hata oluştu", Toast.LENGTH_LONG).show()

                                            } else {

                                                if (value != null) {
                                                    if (!value.isEmpty) {

                                                        val documents = value.documents

                                                        for (document in documents) {

                                                            kullaniciAdim2 = document.get("kullaniciAdi") as String

                                                        }

                                                        playerName = kullaniciAdim2
                                                        val hashMap2 = hashMapOf<Any, Any>()
                                                        if (roomName != null) {
                                                            hashMap2.put("player2", roomName)
                                                            hashMap2.put("kullaniciAdim", kullaniciAdim2)
                                                        }


                                                        if (userUid != null) {
                                                            db.collection("Rooms").document(userUid!!).set(hashMap2)
                                                                .addOnSuccessListener {

                                                                    coklukullaniciInteger = 2

                                                                    roomListener()

                                                                    val roomMap = hashMapOf<Any,Any>()
                                                                    roomMap.put("roomlistIndex",position)

                                                                    db.collection("RoomIndex").document(roomName).set(roomMap)

                                                                    reference15?.remove()



                                                                }


                                                        }


                                                    }

                                                }


                                            }

                                        }

                                }






                        }




                    } else {

                        Toast.makeText(context,"Kendi kurduğunuz odaya giremezsiniz!",Toast.LENGTH_SHORT).show()

                    }



                }

            })
        }?.let { oyuncularListesi.addOnItemTouchListener(it) }

        addRoomsEventListener()


    }

    private fun oyunKurucusu() {

        oyunKurmaButonu.text = "Oyun Kuruluyor..."
        oyunKurmaButonu.isEnabled = false


        //// GOOGLE İSMİ SORGULAMA /////
        if (mAuth.currentUser!!.displayName != null &&
            !mAuth.currentUser!!.displayName.equals("")) {

            playerName = mAuth.currentUser!!.displayName.toString()

            roomName = playerName

            val hashMap = hashMapOf<Any, Any>()
            if (playerName != null) {
                hashMap.put("player1", playerName)
                hashMap.put("zorlukSeviyesi",zorlukSeviyesi)
            }

            if (userUid != null) {

                db.collection("Rooms").document(userUid!!).set(hashMap).addOnSuccessListener {


                    coklukullaniciInteger = 1

                    roomListener()

                }

            }


        } else {

            ///// KULLANICI ADI SORGULAMA //////
            email = mAuth.currentUser?.email.toString()

            reference14 = db.collection("Users").whereEqualTo("email", email)
                .addSnapshotListener { value, error ->


                    if (error != null) {

                        Toast.makeText(context, "Bir hata oluştu", Toast.LENGTH_LONG).show()


                    } else {

                        if (value != null) {
                            if (!value.isEmpty) {

                                val documents = value.documents

                                for (document in documents) {

                                    playerName = document.get("kullaniciAdi") as String

                                    roomName = playerName

                                }

                                val hashMap = hashMapOf<Any, Any>()
                                if (playerName != null) {
                                    hashMap.put("player1", playerName)
                                    hashMap.put("zorlukSeviyesi",zorlukSeviyesi)
                                }

                                if (userUid != null) {
                                    db.collection("Rooms").document(userUid!!).set(hashMap).addOnSuccessListener {

                                        coklukullaniciInteger = 1

                                        roomListener()

                                        reference14?.remove()

                                    }
                                }


                            }

                        }


                    }

                }



        }

    }

    private fun addRoomsEventListener() {


        reference = db2.collection("Rooms").addSnapshotListener { value, error ->


            if (error == null) {

                if (value != null) {

                    if (!value.isEmpty) {

                        oyuncularListesi.visibility = View.VISIBLE

                        var deger : String? = null
                        var zorluk : Long? = null

                        roomsList.clear()
                        zorlukList.clear()

                        val documents = value.documents

                        for (document in documents) {

                            deger = document.getString("player1")
                            zorluk = document.getLong("zorlukSeviyesi")

                            if (deger != null) {
                                roomsList.add(deger)
                            }

                            if (zorluk != null) {
                                zorlukList.add(zorluk)
                            }

                        }




                        /*
                        val arrayAdapter: ArrayAdapter<String>? =
                            context?.let {
                                ArrayAdapter(
                                    it,
                                    android.R.layout.simple_list_item_1,
                                    roomsList
                                )
                        }

                         */


                        if ( roomsList != null && roomsList.size > 0 ) {


                            roomAdapter = RoomAdapter(roomsList,zorlukList)
                            oyuncularListesi.adapter = roomAdapter
                            oyuncularListesi.layoutManager = LinearLayoutManager(context)


                            if (roomAdapter != null) {
                                roomAdapter.notifyDataSetChanged()
                            }

                            reference20 = db.collection("RoomIndex").addSnapshotListener { value, error ->

                                if (error != null) {

                                    println(error.localizedMessage)


                                } else {

                                    if ( value != null ){

                                        if ( !value.isEmpty ){

                                            indexList.clear()

                                            val documents = value.documents

                                            for ( document in documents) {

                                                val roomIndex = document["roomlistIndex"]

                                                yeniIndex = roomIndex.toString().toInt()

                                                if (yeniIndex != null){
                                                    indexList.add(yeniIndex.toLong())
                                                }

                                                if (oyuncularListesi != null && oyuncularListesi.size > 0 ) {

                                                    for ( a in 1..oyuncularListesi.size) {

                                                        oyuncularListesi.get(a-1).setBackgroundResource(R.drawable.borderanabutton1)

                                                    }

                                                    for (index in indexList) {

                                                        oyuncularListesi.get(index.toString().toInt()).setBackgroundResource(R.drawable.borderanabutton3)

                                                    }


                                                }

                                            }

                                        } else {

                                             yeniIndex = 855550

                                            if (oyuncularListesi != null && oyuncularListesi.size > 0 ) {

                                                for ( a in 1..oyuncularListesi.size) {

                                                    oyuncularListesi.get(a-1).setBackgroundResource(R.drawable.borderanabutton1)

                                                }

                                            }

                                        }

                                    }

                                }



                            }


                        }

                    } else {

                        oyuncularListesi.visibility = View.GONE

                    }
                }
            }
        }

    }

    private fun roomListener() {

        reference2 =
            db.collection("Rooms").document(userUid!!).addSnapshotListener { value, error ->

                if (error != null) {

                    oyunKurmaButonu.setText("Oyun Kur")
                    oyunKurmaButonu.isEnabled = true
                    oyuncularListesi.isEnabled = true

                    Toast.makeText(context, requireContext().getString(R.string.lutfen_internet_baglantinizi_kontrol_edin),
                        Toast.LENGTH_SHORT).show()

                } else {


                    oyunKurmaButonu.setText("Oyun Kur")
                    oyunKurmaButonu.isEnabled = true
                    oyuncularListesi.isEnabled = true

                    cokluOyunOdasiLayout.visibility = View.GONE
                    oyunEkraniLayout.visibility = View.VISIBLE

                    enteringRoom()

                    reference2?.remove()

                }

            }


    }

    private fun enteringRoom() {


        if (coklukullaniciInteger == 1) {

            reference3 = db.collection("Rooms")
                .whereEqualTo("player1", roomName).addSnapshotListener { value, error ->


                    if (value != null) {

                        if (!value.isEmpty) {

                            cokluOyunBaslasinBirinciOyuncu()

                        } else {


                        }

                    }

                }


        } else {

            reference4 = db.collection("Rooms")
                .whereEqualTo("player2", roomName).addSnapshotListener { value, error ->


                    if (value != null) {

                        if (!value.isEmpty) {

                            cokluOyunBaslasinIkinciOyuncu()


                        } else {


                        }

                    }

                }

        }

    }

    private fun cokluOyunBaslasinIkinciOyuncu() {


        modeInteger = 3
        puanSayaciIkinciOyuncu?.text = 0.toString()
        puanSayaciBirinciOyuncu?.text = 0.toString()

        artiText.text = ""
        esittirSoruText.text = ""
        seviyeBirKronometre.text = "Oyun Başlıyor..."

        puanCardView.visibility = View.GONE

        var kirpilacakBirinciName: String = ""

        if (roomName.length >= 9) {

            kirpilacakBirinciName = roomName.substring(0, 9) + ".."

        } else {

            kirpilacakBirinciName = roomName

        }

        nameBirinciOyuncu.text = kirpilacakBirinciName


        reference22 = db.collection("Galibiyetler").document(userUid!!).addSnapshotListener { value, error ->

            println("reference22 çağrıldı")

            if (value != null){

                println("null değil çağrıldı")


                if ( value.exists()){

                    galibiyetSayisi = value["galibiyet"].toString().toInt()
                    maglubiyetSayisi = value["maglubiyet"].toString().toInt()
                    beraberlikSayisi = value["beraberlik"].toString().toInt()

                    println("galibiyet sayisi: " + galibiyetSayisi)
                    println("maglubiyet sayisi: " + maglubiyetSayisi)
                    println("beraberlik sayisi: " + beraberlikSayisi)


                    reference22?.remove()
                }


            }


        }




        reference21 =  db.collection("Rooms").whereEqualTo("player1",roomName).addSnapshotListener { value, error ->

            if (value != null){

                if ( !value.isEmpty ){

                    val documents = value.documents
                    for ( document in documents){

                        val zorluk = document.getLong("zorlukSeviyesi")

                        zorlukSeviyesi = zorluk.toString().toInt()


                    }

                    reference21?.remove()


                    reference6 = db.collection("Rooms").whereEqualTo("player2", roomName)
                        .addSnapshotListener { value, error ->


                            if (value != null) {

                                if (!value.isEmpty) {

                                    val documents = value.documents

                                    var ikinciOyuncuKullaniciAdi : String? = null

                                    for (document in documents) {

                                        ikinciOyuncuKullaniciAdi = document.getString("kullaniciAdim")

                                    }

                                    if (ikinciOyuncuKullaniciAdi != null) {


                                        var kirpilacakIkinciName: String = ""

                                        if (ikinciOyuncuKullaniciAdi.length >= 9) {

                                            kirpilacakIkinciName =
                                                ikinciOyuncuKullaniciAdi.substring(0, 9) + ".."

                                        } else {

                                            kirpilacakIkinciName = ikinciOyuncuKullaniciAdi

                                        }

                                        nameIkinciOyuncu.text = kirpilacakIkinciName


                                        oyunBaslangicLayout.visibility = View.VISIBLE
                                        seviyeBirOyunBaslangicText.visibility = View.VISIBLE

                                        baslangicGeriSayan = object : CountDownTimer(3200, 1000) {
                                            override fun onTick(p0: Long) {

                                                seviyeBirOyunBaslangicText.text = ((p0) / 1000).toString()

                                            }

                                            override fun onFinish() {

                                                relativeLayoutForBanner1.visibility = View.VISIBLE
                                                seviyeBirOyunBaslangicText.visibility = View.GONE
                                                oyunBaslangicLayout.visibility = View.GONE

                                                gameIsStarting2()

                                                reference6?.remove()


                                            }

                                        }.start()


                                    }



                                }

                            }


                        }





                }

            }


        }

    }

    private fun gameIsStarting2() {


        InterstitialAd.load(context,"ca-app-pub-5016889744069609/4186825009", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {

                if (reklamPeriyodu %3 == 0 ){

                    mInterstitialAd = interstitialAd

                }
            }
        })


        reference11 = db.collection("Exits").whereEqualTo("roomName",roomName).addSnapshotListener { value, error ->


            if (value != null){

                if ( !value.isEmpty){

                    val documents = value.documents


                    for ( document in documents) {

                        val exitInteger = document.getLong("exitInteger")

                        if (exitInteger != null && exitInteger == 1.toLong()) {

                            //progressDialogOnBesSaniye?.dismiss()
                            db.collection("Rooms").document(userUid!!).delete()

                            reklamPeriyodu++

                            if (mInterstitialAd != null) {
                                mInterstitialAd?.show(activity)
                            }


                            showKazandinDialogExit()

                            geriSayanIkinci?.cancel()
                            handler.removeCallbacks(runnable4)

                            artikKazandinInteger = 10
                            reference11?.remove()


                        }

                    }

                }
            }

        }

        reference10 =  db.collection("Problems").whereEqualTo("roomName",roomName).addSnapshotListener { value, error ->


            if ( value != null ){

                if ( !value.isEmpty ){


                    val documents = value.documents

                    for ( document in documents){

                        val sorunInteger = document.getLong("sorunInteger")


                        if (sorunInteger != null && sorunInteger == 1.toLong()){


                            progressDialogOnBesSaniye = ProgressDialog(context)

                            if (progressDialogOnBesSaniye != null) {

                                progressDialogOnBesSaniye!!.show()
                                progressDialogOnBesSaniye!!.setContentView(R.layout.layout_onbes_saniye_bekleyin)
                                progressDialogOnBesSaniye!!.setCancelable(true)
                                progressDialogOnBesSaniye!!.setCanceledOnTouchOutside(false)

                                handler.removeCallbacks(runnable4)
                                geriSayanIkinci?.cancel()

                            }


                            onbesSaniyeGeriSayan = object : CountDownTimer(15200, 1000) {
                                override fun onTick(p0: Long) {

                                    progressDialogOnBesSaniye!!.onbesSaniyeBekleyinText.text = (p0 / 1000).toString()

                                }

                                override fun onFinish() {

                                    progressDialogOnBesSaniye!!.dismiss()
                                    handler.removeCallbacks(runnable4)

                                    for (a in 0..8) {

                                        numberArrayList[a].isClickable = false
                                        numberArrayList[a].setTextColor(Color.parseColor("#2C2C2C"))
                                        numberArrayList[a].setBackgroundColor(Color.parseColor("#808080"))

                                    }

                                    db.collection("Rooms").document(userUid!!).delete()

                                    reklamPeriyodu++

                                    if (mInterstitialAd != null) {
                                        mInterstitialAd?.show(activity)
                                    }

                                    showKazandinDialog()

                                    artikKazandinInteger = 10

                                    geriSayanIkinci?.cancel()

                                    reference10?.remove()

                                }

                            }.start()

                        }


                    }

                } else {

                    if ( progressDialogOnBesSaniye != null) {

                        progressDialogOnBesSaniye!!.dismiss()
                        onbesSaniyeGeriSayan?.cancel()
                        handler.post(runnable4)
                        timer2(time2)

                    }

                }


            }

        }

        imageViewKronometre1.visibility = View.VISIBLE


        numberArrayList.add(birinciTextSeviyeBir)
        numberArrayList.add(ikinciTextSeviyeBir)
        numberArrayList.add(ucuncuTextSeviyeBir)
        numberArrayList.add(dorduncuTextSeviyeBir)
        numberArrayList.add(besinciTextSeviyeBir)
        numberArrayList.add(altinciTextSeviyeBir)
        numberArrayList.add(yedinciTextSeviyeBir)
        numberArrayList.add(sekizinciTextSeviyeBir)
        numberArrayList.add(dokuzuncuTextSeviyeBir)

        gaming2()

        timer2(time2)

    }

    private fun timer2(baslatilacakSaniye: Long) {

        artiText.text = "+"
        esittirSoruText.text = "= ?"


        geriSayanIkinci = object : CountDownTimer(baslatilacakSaniye, 1000) {
            override fun onTick(p0: Long) {

                time2 = p0

                seviyeBirKronometre.text = (p0 / 1000).toString()

            }

            override fun onFinish() {

                handler.removeCallbacks(runnable4)
                geriSayanIkinci?.cancel()


                for (a in 0..8) {

                    numberArrayList[a].isClickable = false
                    numberArrayList[a].setTextColor(Color.parseColor("#2C2C2C"))
                    numberArrayList[a].setBackgroundColor(Color.parseColor("#808080"))

                }


                seviyeBirKronometre.text = requireContext().getString(R.string.sure_bitti)
                puanSayaciIkinciOyuncu?.setText(puan.toString())

                db.collection("Rooms").document(userUid!!)?.delete()

                progressDialogOyunBitti = ProgressDialog(context)
                progressDialogOyunBitti.show()
                progressDialogOyunBitti.setContentView(R.layout.oyuncunun_puani_bekleniyor)
                progressDialogOyunBitti.setCancelable(true)
                progressDialogOyunBitti.setCanceledOnTouchOutside(false)

                oyunSonucuGeriSayan = object : CountDownTimer(45000, 1000) {
                    override fun onTick(p0: Long) {

                    reference18 = db.collection("Rooms").whereEqualTo("player1",roomName).addSnapshotListener { value, error ->

                            if (value?.isEmpty!!) {


                                oyunSonucuGeriSayan?.cancel()

                                val puan1 = puanSayaciBirinciOyuncu?.text.toString().toInt()

                                progressDialogOyunBitti.dismiss()


                                if ( puan > puan1){

                                    reklamPeriyodu++

                                    if (mInterstitialAd != null) {
                                        mInterstitialAd?.show(activity)
                                    }

                                    showKazandinDialogPuanla()
                                    reference18?.remove()


                                } else if (puan1 > puan) {

                                    reklamPeriyodu++

                                    if (mInterstitialAd != null) {
                                        mInterstitialAd?.show(activity)
                                    }

                                    showKaybettinDialogPuanla()
                                    reference18?.remove()


                                } else {

                                    reklamPeriyodu++

                                    if (mInterstitialAd != null) {
                                        mInterstitialAd?.show(activity)
                                    }


                                    showBerabere()

                                    reference18?.remove()


                                }


                            }

                        }


                    }

                    override fun onFinish() {

                    reference18?.remove()

                    oyunSonucuGeriSayan?.cancel()
                    progressDialogOyunBitti.dismiss()

                    reklamPeriyodu++

                    if (mInterstitialAd != null) {
                        mInterstitialAd?.show(activity)
                    }

                    showKazandinDialogExit()

                    artikKazandinInteger = 10


                    }


                }.start()


            }

        }.start()


    }

    private fun gaming2() {

        if (zorlukSeviyesi == 1) {

            min = 0
            max = 10
            degistirmePeriyoduMulti = 4000


        } else if ( zorlukSeviyesi == 2) {

            min = 10
            max = 30
            degistirmePeriyoduMulti = 3000


        } else if ( zorlukSeviyesi == 3) {

            min = 20
            max = 50
            degistirmePeriyoduMulti = 2500


        } else if ( zorlukSeviyesi == 4) {

            min = 40
            max = 100
            degistirmePeriyoduMulti = 1500


        } else if ( zorlukSeviyesi == 5) {

            min = 80
            max = 140
            degistirmePeriyoduMulti = 950


        }


        for (arraylist in numberArrayList) {

            arraylist.setBackgroundColor(Color.WHITE)
            arraylist.setTextColor(Color.parseColor("#6E6E6E"))

        }

        puanSayaciIkinciOyuncu?.setText(puan.toString())

        val hashMap = hashMapOf<Any, Any>()
        hashMap.put("puan2", puan)
        hashMap.put("kullaniciAdim", kullaniciAdim2)

        userUid?.let { it1 ->
            db.collection("Puanlar").document(it1).set(hashMap).addOnSuccessListener {

                puanKontrol = puan

                reference7 = db.collection("Puanlar").whereEqualTo("kullaniciAdim", roomName)
                    .addSnapshotListener { value, error ->


                        if (value != null) {

                            if (!value.isEmpty) {

                                val documents = value.documents

                                for (document in documents) {

                                    val puan1 = document.getLong("puan1")

                                    if (puan1 != null) {


                                        puanSayaciBirinciOyuncu?.text = puan1.toString()


                                    }

                                }

                            }

                        }
                    }

            }
        }


        val birinciRandomSayi = Random().nextInt(max - min) + min
        val ikinciRandomSayi = Random().nextInt(max - min) + min

        birinciSonucTextSeviyeBir.text = "${birinciRandomSayi}"

        ikinciSonucTextSeviyeBir.text = "${ikinciRandomSayi}"

        val sonuc = birinciRandomSayi + ikinciRandomSayi

        val ikiMin = 2 * min
        val ikiMax = 2 * max

        var randomSonuc = Random().nextInt(ikiMax - ikiMin) + ikiMin
        var randomSonuc2 = Random().nextInt(ikiMax - ikiMin) + ikiMin

        var temelseviyeRandom1 = Random().nextInt(20)
        var temelseviyeRandom2 = Random().nextInt(20)


        runnable4 = object : Runnable {
            override fun run() {


                for (array in numberArrayList) {

                    array.text = null
                }


                var random1 = Random().nextInt(9)
                var random2 = Random().nextInt(9)

                while (random2 == random1) {

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


                if (zorlukSeviyesi == 1) {

                    while (temelseviyeRandom1 == sonuc) {

                        temelseviyeRandom1 = Random().nextInt(20)

                    }

                    while (temelseviyeRandom2 == sonuc || temelseviyeRandom2 == temelseviyeRandom1){

                        temelseviyeRandom2 = Random().nextInt(20)

                    }

                    while (randomSonuc == sonuc || randomSonuc == temelseviyeRandom1 || randomSonuc == temelseviyeRandom2) {

                        randomSonuc = Random().nextInt(ikiMax - ikiMin) + ikiMin

                    }


                    while (randomSonuc2 == sonuc || randomSonuc2 == temelseviyeRandom1 || randomSonuc2 == temelseviyeRandom2 ||
                        randomSonuc2 == randomSonuc
                    ) {

                        randomSonuc2 = Random().nextInt(ikiMax - ikiMin) + ikiMin

                    }



                    numberArrayList[random1].text = "${randomSonuc}"

                    numberArrayList[random2].text = "${randomSonuc2}"

                    numberArrayList[random3].text = "${temelseviyeRandom1}"

                    numberArrayList[random4].text = "${temelseviyeRandom2}"

                    numberArrayList[random5].text = "${sonuc}"


                } else {


                    while (randomSonuc == sonuc || randomSonuc == sonuc + 10 || randomSonuc == sonuc - 10) {

                        randomSonuc = Random().nextInt(ikiMax - ikiMin) + ikiMin

                    }

                    while (randomSonuc2 == sonuc || randomSonuc2 == sonuc + 10 || randomSonuc2 == sonuc - 10 ||
                        randomSonuc2 == randomSonuc
                    ) {

                        randomSonuc2 = Random().nextInt(ikiMax - ikiMin) + ikiMin

                    }



                    numberArrayList[random1].text = "${randomSonuc}"

                    numberArrayList[random2].text = "${randomSonuc2}"

                    numberArrayList[random3].text = "${sonuc + 10}"

                    numberArrayList[random4].text = "${sonuc - 10}"

                    numberArrayList[random5].text = "${sonuc}"


                }


                handler.postDelayed(this, degistirmePeriyoduMulti.toLong())
            }

        }

        handler.post(runnable4)

        if ( puanKontrol - puan > 10 || puan - puanKontrol > 10 ){


            kontrolSayisi++

            Toast.makeText(context,requireActivity().getString(R.string.lutfen_internet_baglantinizi_kontrol_edin),Toast.LENGTH_SHORT).show()

            if (kontrolSayisi == 4) {

                showKaybettinDialogInternetYok()

                geriSayanIkinci?.cancel()
                handler.removeCallbacks(runnable4)

                for (a in 0..8) {

                    numberArrayList[a].isClickable = false
                    numberArrayList[a].setTextColor(Color.parseColor("#2C2C2C"))
                    numberArrayList[a].setBackgroundColor(Color.parseColor("#808080"))

                }


                artikKazandinInteger = 10

                reference?.remove()
                reference2?.remove()
                reference3?.remove()
                reference4?.remove()
                reference5?.remove()
                reference6?.remove()
                reference7?.remove()
                reference9?.remove()
                reference10?.remove()
                reference11?.remove()
                reference12?.remove()
                reference13?.remove()
                reference14?.remove()
                reference15?.remove()
                reference16?.remove()
                reference17?.remove()
                reference18?.remove()
                reference19?.remove()
                reference20?.remove()
                reference21?.remove()
                reference22?.remove()
                reference23?.remove()

            }


        }

        sonucString = sonuc.toString()


        for (a in 0..8) {

            numberArrayList[a].setOnClickListener {

                val clickString = numberArrayList.get(a).text.toString()

                handler.removeCallbacks(runnable4)


                for (c in 0..8) {

                    numberArrayList[c].isClickable = false
                }

                if (clickString.equals(sonucString)) {


                    numberArrayList[a].setBackgroundColor(Color.parseColor("#008000"))
                    numberArrayList[a].setTextColor(Color.parseColor("#2C2C2C"))

                    puan = puan + 10

                    puanSayaciIkinciOyuncu?.text = puan.toString()

                    handler.postDelayed(object : Runnable {
                        override fun run() {

                            for (c in 0..8) {

                                numberArrayList[c].text = null
                                numberArrayList[c].setBackgroundColor(Color.WHITE)
                            }

                            gaming2()

                        }

                    }, 300)

                    //return@setOnClickListener


                } else {


                    puan -= 10

                    for (c in 0..8) {

                        numberArrayList[c].setTextColor(Color.parseColor("#2C2C2C"))
                        //numberArrayList[c].setBackgroundColor(Color.parseColor("#808080"))

                    }



                    numberArrayList[a].setBackgroundColor(Color.parseColor("#D2042D"))


                    for (x in numberArrayList) {

                        if (x.text.toString().equals(sonucString)) {

                            x.setBackgroundColor(Color.parseColor("#008000"))

                        }

                    }

                    handler.postDelayed(object : Runnable {

                        override fun run() {


                            for (c in 0..8) {

                                numberArrayList[c].text = null
                                numberArrayList[c].setBackgroundColor(Color.WHITE)
                            }


                            gaming2()
                        }

                    }, 300)


                }

            }

        }


    }

    private fun cokluOyunBaslasinBirinciOyuncu() {


        seviyeBirKronometre.text = "Rakip Bekleniyor..."

        puanSayaciIkinciOyuncu?.text = 0.toString()
        puanSayaciBirinciOyuncu?.text = 0.toString()

        progressDialog = ProgressDialog(context)
        progressDialog.show()
        progressDialog.setContentView(R.layout.layout_progress_dialog)
        progressDialog.ikinciOyuncuBekleniyor.text = "Oyuncu bekleniyor.."
        progressDialog.setCancelable(true)
        progressDialog.setCanceledOnTouchOutside(false)

        artiText.text = ""
        esittirSoruText.text = ""

        oyunEkraniLayout.visibility = View.VISIBLE
        puanCardView.visibility = View.GONE

        var kisaltilmisBirinciName: String = ""

        if (roomName.length >= 9) {

            kisaltilmisBirinciName = roomName.substring(0, 9) + ".."

        } else {

            kisaltilmisBirinciName = roomName

        }

        nameBirinciOyuncu.text = kisaltilmisBirinciName

        reference23 = db.collection("Galibiyetler").document(userUid!!).addSnapshotListener { value, error ->


            if (value != null){

                if ( value.exists()){

                    galibiyetSayisi = value["galibiyet"].toString().toInt()
                    maglubiyetSayisi = value["maglubiyet"].toString().toInt()
                    beraberlikSayisi = value["beraberlik"].toString().toInt()

                    println("galibiyet sayisi: " + galibiyetSayisi)
                    println("maglubiyet sayisi: " + maglubiyetSayisi)
                    println("beraberlik sayisi: " + beraberlikSayisi)

                    reference23?.remove()

                }
            }
        }




        reference5 = db.collection("Rooms").whereEqualTo("player2", roomName)
            .addSnapshotListener { value, error ->

                if (value != null) {


                    if (!value.isEmpty) {


                    val documents = value.documents

                    for (document in documents) {

                    ikinciOyuncuKullaniciAdi = document.getString("kullaniciAdim")

                    }

                    if (ikinciOyuncuKullaniciAdi != null) {

                        var kisaltilmisIkinciName: String = ""

                        if (ikinciOyuncuKullaniciAdi!!.length >= 9) {

                            kisaltilmisIkinciName =
                                ikinciOyuncuKullaniciAdi!!.substring(0, 9) + ".."

                        } else {

                            kisaltilmisIkinciName = ikinciOyuncuKullaniciAdi!!
                        }

                        nameIkinciOyuncu.text = kisaltilmisIkinciName

                        modeInteger = 3
                        progressDialog.dismiss()
                        seviyeBirKronometre.text = "Oyun Başlıyor..."


                        oyunBaslangicLayout.visibility = View.VISIBLE
                        seviyeBirOyunBaslangicText.visibility = View.VISIBLE

                        baslangicGeriSayan = object : CountDownTimer(3200, 1000) {
                            override fun onTick(p0: Long) {

                                seviyeBirOyunBaslangicText.text = ((p0) / 1000).toString()

                            }

                            override fun onFinish() {

                                relativeLayoutForBanner1.visibility = View.VISIBLE
                                seviyeBirOyunBaslangicText.visibility = View.GONE
                                oyunBaslangicLayout.visibility = View.GONE

                                gameIsStarting()

                            }

                        }.start()


                    }



                    }

                }


            }


    }

    private fun gameIsStarting() {

        InterstitialAd.load(context,"ca-app-pub-5016889744069609/4186825009", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {

                if ( reklamPeriyodu %3 == 0 ){

                    mInterstitialAd = interstitialAd

                }

            }
        })


        reference12 = db.collection("Exits").whereEqualTo("roomName",roomName).addSnapshotListener { value, error ->

            if (value != null ){

                if ( !value.isEmpty){

                    val documents = value.documents

                    for (document in documents) {

                        val exitInteger = document.getLong("exitInteger")

                        if (exitInteger != null && exitInteger == 2.toLong()){

                            //progressDialogOnBesSaniye?.dismiss()


                            db.collection("Rooms").document(userUid!!).delete()
                            db.collection("RoomIndex").document(roomName).delete()

                            reklamPeriyodu++

                            if (mInterstitialAd != null) {
                                mInterstitialAd?.show(activity)
                            }


                            showKazandinDialogExit()

                            geriSayanBirinci?.cancel()
                            handler.removeCallbacks(runnable3)

                            artikKazandinInteger = 10

                            reference12?.remove()
                            reference7?.remove()

                        }

                    }

                }

            }
        }

        reference9 = db.collection("Problems").whereEqualTo("roomName",roomName).addSnapshotListener { value, error ->

            if ( value != null ){

                if ( !value.isEmpty ){


                    val documents  = value.documents

                    for ( document in documents ) {

                        val sorunInteger = document.getLong("sorunInteger")

                        if (sorunInteger != null && sorunInteger == 2.toLong()) {

                            progressDialogOnBesSaniye = ProgressDialog(context)

                            if (progressDialogOnBesSaniye != null) {

                                progressDialogOnBesSaniye!!.show()
                                progressDialogOnBesSaniye!!.setContentView(R.layout.layout_onbes_saniye_bekleyin)
                                progressDialogOnBesSaniye!!.setCancelable(true)
                                progressDialogOnBesSaniye!!.setCanceledOnTouchOutside(false)

                                handler.removeCallbacks(runnable3)
                                geriSayanBirinci?.cancel()

                            }


                            onbesSaniyeGeriSayan = object : CountDownTimer(15200, 1000) {
                                override fun onTick(p0: Long) {

                                    progressDialogOnBesSaniye!!.onbesSaniyeBekleyinText.text = (p0 / 1000).toString()


                                }

                                override fun onFinish() {

                                    progressDialogOnBesSaniye!!.dismiss()

                                    handler.removeCallbacks(runnable3)

                                    for (a in 0..8) {

                                        numberArrayList[a].isClickable = false
                                        numberArrayList[a].setTextColor(Color.parseColor("#2C2C2C"))
                                        numberArrayList[a].setBackgroundColor(Color.parseColor("#808080"))

                                    }

                                    db.collection("Rooms").document(userUid!!).delete()
                                    db.collection("RoomIndex").document(roomName).delete()

                                    reklamPeriyodu++


                                    if (mInterstitialAd != null) {
                                        mInterstitialAd?.show(activity)
                                    }


                                    showKazandinDialog()

                                    artikKazandinInteger = 10

                                    geriSayanBirinci?.cancel()

                                    reference9?.remove()


                                }

                            }.start()

                        }




                    }


                } else {

                    if ( progressDialogOnBesSaniye != null) {

                        onbesSaniyeGeriSayan?.cancel()
                        progressDialogOnBesSaniye!!.dismiss()

                        handler.post(runnable3)
                        timer1(time1)

                    }

                }


            }

        }


        imageViewKronometre1.visibility = View.VISIBLE

        numberArrayList.add(birinciTextSeviyeBir)
        numberArrayList.add(ikinciTextSeviyeBir)
        numberArrayList.add(ucuncuTextSeviyeBir)
        numberArrayList.add(dorduncuTextSeviyeBir)
        numberArrayList.add(besinciTextSeviyeBir)
        numberArrayList.add(altinciTextSeviyeBir)
        numberArrayList.add(yedinciTextSeviyeBir)
        numberArrayList.add(sekizinciTextSeviyeBir)
        numberArrayList.add(dokuzuncuTextSeviyeBir)



        gaming1()
        timer1(time1)



    }

    private fun timer1(time: Long) {

        artiText.text = "+"
        esittirSoruText.text = "= ?"

        geriSayanBirinci = object : CountDownTimer(time, 1000) {
            override fun onTick(p0: Long) {

                time1 = p0

                seviyeBirKronometre.text = (p0 / 1000).toString()

            }

            override fun onFinish() {

                handler.removeCallbacks(runnable3)
                geriSayanBirinci?.cancel()


                for (a in 0..8) {

                    numberArrayList[a].isClickable = false
                    numberArrayList[a].setTextColor(Color.parseColor("#2C2C2C"))
                    numberArrayList[a].setBackgroundColor(Color.parseColor("#808080"))

                }


                seviyeBirKronometre.text = requireContext().getString(R.string.sure_bitti)
                puanSayaciBirinciOyuncu?.setText(puan.toString())

                db.collection("Rooms").document(userUid!!)?.delete()
                db.collection("RoomIndex").document(roomName).delete()


                progressDialogOyunBitti = ProgressDialog(context)
                progressDialogOyunBitti.show()
                progressDialogOyunBitti.setContentView(R.layout.oyuncunun_puani_bekleniyor)
                progressDialogOyunBitti.setCancelable(true)
                progressDialogOyunBitti.setCanceledOnTouchOutside(false)

                oyunSonucuGeriSayan = object : CountDownTimer(45000, 1000) {
                    override fun onTick(p0: Long) {

                        reference19 = db.collection("Rooms").whereEqualTo("player2",roomName).addSnapshotListener { value, error ->

                            if (value?.isEmpty!!) {

                                oyunSonucuGeriSayan?.cancel()

                                val puan2 = puanSayaciIkinciOyuncu?.text.toString().toInt()

                                progressDialogOyunBitti.dismiss()


                                if ( puan > puan2){

                                    reklamPeriyodu++

                                    if (mInterstitialAd != null) {
                                        mInterstitialAd?.show(activity)
                                    }

                                    showKazandinDialogPuanla()
                                    reference19?.remove()

                                } else if (puan2 > puan) {

                                    reklamPeriyodu++

                                    if (mInterstitialAd != null) {
                                        mInterstitialAd?.show(activity)
                                    }

                                    showKaybettinDialogPuanla()
                                    reference19?.remove()

                                } else {

                                    reklamPeriyodu++

                                    if (mInterstitialAd != null) {
                                        mInterstitialAd?.show(activity)
                                    }

                                    showBerabere()
                                    reference19?.remove()

                                }


                            }



                        }

                    }

                    override fun onFinish() {

                        reference19?.remove()

                        oyunSonucuGeriSayan?.cancel()
                        progressDialogOyunBitti.dismiss()

                        reklamPeriyodu++

                        if (mInterstitialAd != null) {
                            mInterstitialAd?.show(activity)
                        }

                        showKazandinDialogExit()

                        artikKazandinInteger = 10


                    }


                }.start()







            }

        }.start()


    }



    private fun gaming1() {


        if (zorlukSeviyesi == 1) {

            min = 0
            max = 10
            degistirmePeriyoduMulti = 4000


        } else if ( zorlukSeviyesi == 2) {

            min = 10
            max = 30
            degistirmePeriyoduMulti = 3000


        } else if ( zorlukSeviyesi == 3) {

            min = 20
            max = 50
            degistirmePeriyoduMulti = 2500

        } else if ( zorlukSeviyesi == 4) {

            min = 40
            max = 100
            degistirmePeriyoduMulti = 1500

        } else if ( zorlukSeviyesi == 5) {

            min = 80
            max = 140
            degistirmePeriyoduMulti = 950

        }


        for (arraylist in numberArrayList) {

            arraylist.setBackgroundColor(Color.WHITE)
            arraylist.setTextColor(Color.parseColor("#6E6E6E"))

        }

        puanSayaciBirinciOyuncu?.setText(puan.toString())

        val hashMap = hashMapOf<Any, Any>()
        hashMap.put("puan1", puan)
        hashMap.put("kullaniciAdim", roomName)


        db.collection("Puanlar").document(userUid!!).set(hashMap).addOnSuccessListener {

            puanKontrol = puan

            reference7 =
                db.collection("Puanlar").whereEqualTo("kullaniciAdim", ikinciOyuncuKullaniciAdi)
                    .addSnapshotListener { value, error ->

                        if (value != null) {

                            if (!value.isEmpty) {

                                val documents = value.documents

                                for (document in documents) {

                                    val puan2 = document.getLong("puan2")

                                    if (puan2 != null) {



                                        puanSayaciIkinciOyuncu?.text = puan2.toString()


                                    }

                                }

                            }

                        }
                    }

        }


        val birinciRandomSayi = Random().nextInt(max - min) + min
        val ikinciRandomSayi = Random().nextInt(max - min) + min

        birinciSonucTextSeviyeBir.text = "${birinciRandomSayi}"

        ikinciSonucTextSeviyeBir.text = "${ikinciRandomSayi}"

        val sonuc = birinciRandomSayi + ikinciRandomSayi

        val ikiMin = 2 * min
        val ikiMax = 2 * max

        var randomSonuc = Random().nextInt(ikiMax - ikiMin) + ikiMin
        var randomSonuc2 = Random().nextInt(ikiMax - ikiMin) + ikiMin

        var temelseviyeRandom1 = Random().nextInt(20)
        var temelseviyeRandom2 = Random().nextInt(20)


        runnable3 = object : Runnable {
        override fun run() {

        for (array in numberArrayList) {

            array.text = null
        }


        var random1 = Random().nextInt(9)
        var random2 = Random().nextInt(9)

        while (random2 == random1) {

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


        if (zorlukSeviyesi == 1) {


            while (temelseviyeRandom1 == sonuc) {

                temelseviyeRandom1 = Random().nextInt(20)

            }

            while (temelseviyeRandom2 == sonuc || temelseviyeRandom2 == temelseviyeRandom1){

                temelseviyeRandom2 = Random().nextInt(20)

            }

            while (randomSonuc == sonuc || randomSonuc == temelseviyeRandom1 || randomSonuc == temelseviyeRandom2) {

                randomSonuc = Random().nextInt(ikiMax - ikiMin) + ikiMin

            }


            while (randomSonuc2 == sonuc || randomSonuc2 == temelseviyeRandom1 || randomSonuc2 == temelseviyeRandom2 ||
                randomSonuc2 == randomSonuc
            ) {

                randomSonuc2 = Random().nextInt(ikiMax - ikiMin) + ikiMin

            }


            numberArrayList[random1].text = "${randomSonuc}"

            numberArrayList[random2].text = "${randomSonuc2}"

            numberArrayList[random3].text = "${temelseviyeRandom1}"

            numberArrayList[random4].text = "${temelseviyeRandom2}"

            numberArrayList[random5].text = "${sonuc}"


        } else {


            while (randomSonuc == sonuc || randomSonuc == sonuc + 10 || randomSonuc == sonuc - 10) {

                randomSonuc = Random().nextInt(ikiMax - ikiMin) + ikiMin

            }

            while (randomSonuc2 == sonuc || randomSonuc2 == sonuc + 10 || randomSonuc2 == sonuc - 10 ||
                randomSonuc2 == randomSonuc
            ) {

                randomSonuc2 = Random().nextInt(ikiMax - ikiMin) + ikiMin

            }



            numberArrayList[random1].text = "${randomSonuc}"

            numberArrayList[random2].text = "${randomSonuc2}"

            numberArrayList[random3].text = "${sonuc + 10}"

            numberArrayList[random4].text = "${sonuc - 10}"

            numberArrayList[random5].text = "${sonuc}"


        }


        handler.postDelayed(this, degistirmePeriyoduMulti.toLong())

            }

        }

        handler.post(runnable3)


        if ( puan - puanKontrol > 10 || puanKontrol - puan > 10 ) {

            kontrolSayisi++

            Toast.makeText(context,requireActivity().getString(R.string.lutfen_internet_baglantinizi_kontrol_edin),Toast.LENGTH_SHORT).show()

            if (kontrolSayisi == 4) {

                showKaybettinDialogInternetYok()

                geriSayanBirinci?.cancel()
                handler.removeCallbacks(runnable3)

                for (a in 0..8) {

                    numberArrayList[a].isClickable = false
                    numberArrayList[a].setTextColor(Color.parseColor("#2C2C2C"))
                    numberArrayList[a].setBackgroundColor(Color.parseColor("#808080"))

                }


                artikKazandinInteger = 10

                reference?.remove()
                reference2?.remove()
                reference3?.remove()
                reference4?.remove()
                reference5?.remove()
                reference6?.remove()
                reference7?.remove()
                reference9?.remove()
                reference10?.remove()
                reference11?.remove()
                reference12?.remove()
                reference13?.remove()
                reference14?.remove()
                reference15?.remove()
                reference16?.remove()
                reference17?.remove()
                reference18?.remove()
                reference19?.remove()
                reference20?.remove()
                reference21?.remove()
                reference22?.remove()
                reference23?.remove()

            }


        }


        sonucString = sonuc.toString()


        for (a in 0..8) {

            numberArrayList[a].setOnClickListener {

                val clickString = numberArrayList.get(a).text.toString()

                handler.removeCallbacks(runnable3)



                for (c in 0..8) {

                    numberArrayList[c].isClickable = false
                }

                if (clickString.equals(sonucString)) {


                    numberArrayList[a].setBackgroundColor(Color.parseColor("#008000"))
                    numberArrayList[a].setTextColor(Color.parseColor("#2C2C2C"))

                    puan = puan + 10


                    puanSayaciBirinciOyuncu?.text = puan.toString()


                    handler.postDelayed(object : Runnable {
                        override fun run() {

                            for (c in 0..8) {

                                numberArrayList[c].text = null
                                numberArrayList[c].setBackgroundColor(Color.WHITE)
                            }

                            gaming1()

                        }

                    }, 300)

                    //return@setOnClickListener


                } else {


                    puan -= 10



                    for (c in 0..8) {

                        numberArrayList[c].setTextColor(Color.parseColor("#2C2C2C"))
                        //numberArrayList[c].setBackgroundColor(Color.parseColor("#808080"))
                    }

                    numberArrayList[a].setBackgroundColor(Color.parseColor("#D2042D"))


                    for (x in numberArrayList) {

                        if (x.text.toString().equals(sonucString)) {

                            x.setBackgroundColor(Color.parseColor("#008000"))

                        }
                    }

                    handler.postDelayed(object : Runnable {

                        override fun run() {


                            for (c in 0..8) {

                                numberArrayList[c].text = null
                                numberArrayList[c].setBackgroundColor(Color.WHITE)
                            }


                            gaming1()
                        }

                    }, 300)


                }

            }

        }

    }

    ////////////Infinite Mode//////////////
    private fun startingTheGame2() {

        modeInteger = 2

        levelsForSpeed()


        min = minArray[level - 1]
        max = maxArray[level - 1]
        levelPuanIndex = levelForSpeed - 1

        puanCardView.visibility = View.VISIBLE
        val layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)
        puanCardView.layoutParams = layoutParams

        puanSayaciSeviyeBir.setText(puan.toString())
        imageViewKronometre1.visibility = View.VISIBLE


        numberArrayList.add(birinciTextSeviyeBir)
        numberArrayList.add(ikinciTextSeviyeBir)
        numberArrayList.add(ucuncuTextSeviyeBir)
        numberArrayList.add(dorduncuTextSeviyeBir)
        numberArrayList.add(besinciTextSeviyeBir)
        numberArrayList.add(altinciTextSeviyeBir)
        numberArrayList.add(yedinciTextSeviyeBir)
        numberArrayList.add(sekizinciTextSeviyeBir)
        numberArrayList.add(dokuzuncuTextSeviyeBir)


        for (arraylist in numberArrayList) {

            arraylist.setBackgroundColor(Color.WHITE)
            arraylist.setTextColor(Color.parseColor("#6E6E6E"))

        }


        val birinciRandomSayi = Random().nextInt(max - min) + min
        val ikinciRandomSayi = Random().nextInt(max - min) + min

        birinciSonucTextSeviyeBir.text = "${birinciRandomSayi}"

        ikinciSonucTextSeviyeBir.text = "${ikinciRandomSayi}"

        val sonuc = birinciRandomSayi + ikinciRandomSayi

        val ikiMin = 2 * min
        val ikiMax = 2 * max

        var randomSonuc = Random().nextInt(ikiMax - ikiMin) + ikiMin
        var randomSonuc2 = Random().nextInt(ikiMax - ikiMin) + ikiMin


        val runnable2 = object : Runnable {
            override fun run() {


                for (array in numberArrayList) {

                    array.text = null
                }


                var random1 = Random().nextInt(9)
                var random2 = Random().nextInt(9)

                while (random2 == random1) {

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


                while (randomSonuc == sonuc || randomSonuc == sonuc + 10 || randomSonuc == sonuc - 10) {

                    randomSonuc = Random().nextInt(ikiMax - ikiMin) + ikiMin

                }

                while (randomSonuc2 == sonuc || randomSonuc2 == sonuc + 10 || randomSonuc2 == sonuc - 10 ||
                    randomSonuc2 == randomSonuc
                ) {

                    randomSonuc2 = Random().nextInt(ikiMax - ikiMin) + ikiMin

                }


                numberArrayList[random1].text = "${randomSonuc}"

                numberArrayList[random2].text = "${randomSonuc2}"

                numberArrayList[random3].text = "${sonuc + 10}"

                numberArrayList[random4].text = "${sonuc - 10}"

                numberArrayList[random5].text = "${sonuc}"


                handler.postDelayed(this, degistirmePeriyodu)
            }

        }

        handler.post(runnable2)

        val sonucString = sonuc.toString()

        geriSayan = object : CountDownTimer(geriSaymaSuresi, 1000) {
            override fun onTick(p0: Long) {


                seviyeBirKronometre.text = (p0 / 1000).toString()


                for (a in 0..8) {

                    numberArrayList[a].setOnClickListener {


                        val clickString = numberArrayList.get(a).text.toString()


                        handler.removeCallbacks(runnable2)
                        cancel()

                        for (c in 0..8) {
                            //numberArrayList[c].text = null
                            numberArrayList[c].isClickable = false
                        }

                        if (clickString.equals(sonucString)) {


                            numberArrayList[a].setBackgroundColor(Color.parseColor("#008000"))
                            numberArrayList[a].setTextColor(Color.parseColor("#2C2C2C"))

                            puan = puan + 10


                            if (puan == levelPuanArrayForSonsuzMod[levelPuanIndex]) {

                                if (levelPuanIndex != 15) {

                                    levelPuanIndex++
                                    levelForSpeed++
                                    levelsForSpeed()

                                }

                            }

                            puanSayaciSeviyeBir.text = puan.toString()


                            handler.postDelayed(object : Runnable {
                                override fun run() {

                                    for (c in 0..8) {

                                        numberArrayList[c].text = null
                                        numberArrayList[c].setBackgroundResource(R.drawable.border2)
                                    }

                                    //cancel()

                                    startingTheGame2()

                                }

                            }, 300)

                            return@setOnClickListener


                        } else {

                            for (c in 0..8) {

                                //numberArrayList[c].text = null
                                numberArrayList[c].isClickable = false
                                numberArrayList[c].setTextColor(Color.parseColor("#2C2C2C"))
                                numberArrayList[c].setBackgroundColor(Color.parseColor("#808080"))

                            }

                            numberArrayList[a].setBackgroundColor(Color.parseColor("#D2042D"))


                            for (x in numberArrayList) {

                                if (x.text.toString().equals(sonucString)) {

                                    x.setBackgroundColor(Color.parseColor("#008000"))

                                }

                            }

                            handler.postDelayed(object : Runnable {

                                override fun run() {


                                    showDialog2()

                                    seviyeBirKronometre.setOnClickListener {

                                        levelForSpeed = 1

                                        startingTheGame2()

                                    }

                                }


                            }, 300)


                        }


                    }


                }


            }

            override fun onFinish() {

                handler.removeCallbacks(runnable2)

                for (a in 0..8) {

                    numberArrayList[a].isClickable = false
                    numberArrayList[a].setTextColor(Color.parseColor("#2C2C2C"))
                    numberArrayList[a].setBackgroundColor(Color.parseColor("#808080"))

                }

                for (x in numberArrayList) {

                    if (x.text.toString().equals(sonucString)) {

                        x.setBackgroundColor(Color.parseColor("#008000"))

                    }

                }

                seviyeBirKronometre.text = requireContext().getString(R.string.sure_bitti)


                seviyeBirKronometre.setOnClickListener {

                    levelForSpeed = 1

                    startingTheGame2()

                }

                puanSayaciSeviyeBir.setText(puan.toString())

                showDialog2()

            }

        }.start()

    }

    ////////////Level Mode//////////////
    private fun startingTheGame() {

        modeInteger = 1

        oyunBittiMi = false

        levels()

        levelProgressBar.max = levelPuanArrayForLevelMod[levelPuanIndex]
        levelProgressBar.progress = puan


        levelPuanIndex = level - 1
        min = minArray[level - 1]
        max = maxArray[level - 1]

        //puanSayaciSeviyeBir.setText(puan.toString()+"/"+levelPuanArrayForLevelMod[levelPuanIndex])


        levelTextSeviyeBir.text =
            requireContext().getString(R.string.level_buyuk) + " " + level.toString()

        imageViewKronometre1.visibility = View.VISIBLE


        numberArrayList.add(birinciTextSeviyeBir)
        numberArrayList.add(ikinciTextSeviyeBir)
        numberArrayList.add(ucuncuTextSeviyeBir)
        numberArrayList.add(dorduncuTextSeviyeBir)
        numberArrayList.add(besinciTextSeviyeBir)
        numberArrayList.add(altinciTextSeviyeBir)
        numberArrayList.add(yedinciTextSeviyeBir)
        numberArrayList.add(sekizinciTextSeviyeBir)
        numberArrayList.add(dokuzuncuTextSeviyeBir)


        for (arraylist in numberArrayList) {

            arraylist.setBackgroundColor(Color.WHITE)
            arraylist.setTextColor(Color.parseColor("#6E6E6E"))

        }


        val birinciRandomSayi = Random().nextInt(max - min) + min
        val ikinciRandomSayi = Random().nextInt(max - min) + min

        birinciSonucTextSeviyeBir.text = "${birinciRandomSayi}"

        ikinciSonucTextSeviyeBir.text = "${ikinciRandomSayi}"

        val sonuc = birinciRandomSayi + ikinciRandomSayi

        val ikiMin = 2 * min
        val ikiMax = 2 * max

        var randomSonuc = Random().nextInt(ikiMax - ikiMin) + ikiMin
        var randomSonuc2 = Random().nextInt(ikiMax - ikiMin) + ikiMin


        val runnable = object : Runnable {
            override fun run() {


                for (array in numberArrayList) {

                    array.text = null
                }


                var random1 = Random().nextInt(9)
                var random2 = Random().nextInt(9)

                while (random2 == random1) {

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


                while (randomSonuc == sonuc || randomSonuc == sonuc + 10 || randomSonuc == sonuc - 10) {

                    randomSonuc = Random().nextInt(ikiMax - ikiMin) + ikiMin

                }

                while (randomSonuc2 == sonuc || randomSonuc2 == sonuc + 10 || randomSonuc2 == sonuc - 10 || randomSonuc2 == randomSonuc) {

                    randomSonuc2 = Random().nextInt(ikiMax - ikiMin) + ikiMin

                }


                numberArrayList[random1].text = "${randomSonuc}"

                numberArrayList[random2].text = "${randomSonuc2}"

                numberArrayList[random3].text = "${sonuc + 10}"

                numberArrayList[random4].text = "${sonuc - 10}"

                numberArrayList[random5].text = "${sonuc}"


                handler.postDelayed(this, degistirmePeriyodu)
            }

        }


        handler.post(runnable)


        val sonucString = sonuc.toString()

        geriSayan = object : CountDownTimer(geriSaymaSuresi, 1000) {
            override fun onTick(p0: Long) {


                seviyeBirKronometre.text = (p0 / 1000).toString()


                for (a in 0..8) {

                    numberArrayList[a].setOnClickListener {

                        val clickString = numberArrayList.get(a).text.toString()

                        handler.removeCallbacks(runnable)
                        cancel()

                        for (c in 0..8) {

                            numberArrayList[c].isClickable = false

                        }



                        if (clickString.equals(sonucString)) {

                            numberArrayList[a].setBackgroundColor(Color.parseColor("#008000"))
                            numberArrayList[a].setTextColor(Color.parseColor("#2C2C2C"))

                            puan = puan + 10

                            levelProgressBar.progress = puan


                            if (puan == levelPuanArrayForLevelMod[levelPuanIndex]) {


                                if (levelPuanIndex == 15) {

                                    showOyunBittiDialog()

                                    oyunBittiMi = true

                                } else {

                                    levelPuanIndex++

                                    level++

                                }


                                levels()


                                preferences.edit().putInt("level", level).apply()


                                //puanSayaciSeviyeBir.text = puan.toString()+ "/" + levelPuanArrayForLevelMod[levelPuanIndex]

                                puan = 0

                            }



                            if (oyunBittiMi == false) {

                                handler.postDelayed(object : Runnable {
                                    override fun run() {

                                        for (c in 0..8) {

                                            numberArrayList[c].text = null
                                            numberArrayList[c].setBackgroundResource(R.drawable.border2)
                                        }

                                        startingTheGame()

                                    }


                                }, 300)


                            } else {

                                seviyeBirKronometre.setOnClickListener {

                                    puan = 0

                                    startingTheGame()


                                }


                            }


                            return@setOnClickListener


                        } else {

                            for (c in 0..8) {

                                //numberArrayList[c].text = null
                                numberArrayList[c].isClickable = false
                                numberArrayList[c].setTextColor(Color.parseColor("#2C2C2C"))
                                numberArrayList[c].setBackgroundColor(Color.parseColor("#808080"))

                            }

                            numberArrayList[a].setBackgroundColor(Color.parseColor("#D2042D"))


                            for (x in numberArrayList) {

                                if (x.text.toString().equals(sonucString)) {

                                    x.setBackgroundColor(Color.parseColor("#008000"))


                                }

                            }

                            handler.postDelayed(object : Runnable {
                                override fun run() {


                                    showDialog()

                                    seviyeBirKronometre.setOnClickListener {


                                        startingTheGame()


                                    }

                                }


                            }, 300)


                        }


                    }

                }

            }


            override fun onFinish() {

                handler.removeCallbacks(runnable)

                for (a in 0..8) {

                    numberArrayList[a].isClickable = false
                    numberArrayList[a].setTextColor(Color.parseColor("#2C2C2C"))
                    numberArrayList[a].setBackgroundColor(Color.parseColor("#808080"))

                }

                for (x in numberArrayList) {

                    if (x.text.toString().equals(sonucString)) {

                        x.setBackgroundColor(Color.parseColor("#008000"))

                    }

                }


                seviyeBirKronometre.text = requireContext().getString(R.string.sure_bitti)


                seviyeBirKronometre.setOnClickListener {

                    puan = 0

                    startingTheGame()

                }

                //puanSayaciSeviyeBir.setText(puan.toString()+"/"+levelPuanArrayForLevelMod[levelPuanIndex+1])

                showDialog()

            }

        }.start()

    }

    private fun showDialog() {

        dialog = activity?.let { Dialog(it) }

        if (dialog != null) {

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

                seviyeBirKronometre.setText(requireContext().getString(R.string.tekrar_oyna))
                imageViewKronometre1.visibility = View.GONE

                dialog!!.dismiss()

            }


            dialog!!.reklamIzleVeDevamEtLevel.setOnClickListener {

                dialog!!.dismiss()

                if (mRewardedAd != null) {

                    mRewardedAd?.show(activity, OnUserEarnedRewardListener() {


                        fun onUserEarnedReward(rewardItem: RewardItem) {

                            var rewardAmount = rewardItem.amount
                            var rewardType = rewardItem.type

                            seviyeBirKronometre.setText(requireContext().getString(R.string.oyuna_devam))
                            seviyeBirKronometre.setOnClickListener {

                                startingTheGame()

                            }

                            loadRewardedAds()
                        }
                        onUserEarnedReward(it)

                    })
                } else {
                    Toast.makeText(
                        context,
                        requireContext().getString(R.string.reklam_yuklenemedi),
                        Toast.LENGTH_LONG
                    ).show()

                }


            }

            //dialog!!.oyunBittiLevelText.text = "LEVEL ${level}"
            dialog!!.oyunBittiLevelText.text = requireContext().getString(R.string.level_buyuk) +
                    " " + level.toString()

            dialog!!.progressbarLevelOyunBitti.max = levelPuanArrayForLevelMod[levelPuanIndex]
            dialog!!.progressbarLevelOyunBitti.progress = puan

            val yuzdelik =
                (puan.toDouble() / levelPuanArrayForLevelMod[levelPuanIndex].toDouble()) * 100

            dialog!!.levelYuzdelik.text = "%${yuzdelik.toInt()}"


            if (yuzdelik > 75) {

                dialog!!.levelTextAciklama.text =
                    requireContext().getString(R.string.tuh_be_az_kalmisti)

            } else if (yuzdelik > 49) {

                dialog!!.levelTextAciklama.text =
                    requireContext().getString(R.string.yarisini_bitirdin_bile)

            } else if (yuzdelik > 35) {

                dialog!!.levelTextAciklama.text =
                    requireContext().getString(R.string.neredeyse_yarisina_geldin)

            } else {

                dialog!!.levelTextAciklama.text = requireContext().getString(R.string.bir_daha_dene)

            }


            dialog!!.show()

        }
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

    }

    private fun showDialog2() {


        dialog = activity?.let { Dialog(it) }

        if (dialog != null) {

            dialog!!.setContentView(R.layout.pop_up_sonsuz_mod)

            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.setCancelable(true)
            dialog!!.setCanceledOnTouchOutside(false)

            dialog!!.tekrarOynaButton.setOnClickListener {

                levelForSpeed = 1

                dialog!!.dismiss()

                handler.removeCallbacks(runnable2)

                puan = 0


                startingTheGame2()

            }

            dialog!!.carpi.setOnClickListener {

                puan = 0

                seviyeBirKronometre.setText(requireContext().getString(R.string.tekrar_oyna))
                imageViewKronometre1.visibility = View.GONE

                dialog!!.dismiss()

            }

            dialog!!.reklamIzleVeDevamEt.setOnClickListener {

                dialog!!.dismiss()

                if (mRewardedAd != null) {

                    mRewardedAd?.show(activity, OnUserEarnedRewardListener() {


                        fun onUserEarnedReward(rewardItem: RewardItem) {

                            var rewardAmount = rewardItem.amount
                            var rewardType = rewardItem.type

                            seviyeBirKronometre.setText(requireContext().getString(R.string.oyuna_devam))
                            seviyeBirKronometre.setOnClickListener {

                                startingTheGame2()
                            }

                            loadRewardedAds()
                        }
                        onUserEarnedReward(it)

                    })
                } else {

                    Toast.makeText(
                        context,
                        requireContext().getString(R.string.reklam_yuklenemedi),
                        Toast.LENGTH_LONG
                    ).show()

                }


            }

            dialog!!.skorunRakam.text = "${puan}"

            val eskiSkor = preferences.getInt("skor", -1)

            if (puan > eskiSkor) {

                preferences?.edit()?.putInt("skor", puan)?.apply()
                dialog!!.enYuksekSkorText.text =
                    "${requireContext().getString(R.string.en_yuksek_skor)} : " + puan


            } else {

                dialog!!.enYuksekSkorText.text =
                    "${requireContext().getString(R.string.en_yuksek_skor)} : " + eskiSkor

            }

            val account = GoogleSignIn.getLastSignedInAccount(context)

            if (account != null) {

                Games.getLeaderboardsClient(context, account)
                    .submitScore(getString(R.string.leaderboard_id_1), puan.toLong())

            }

            dialog!!.en_yuksek_skorlar.setOnClickListener {

                showLeaderboard()

            }




            dialog!!.show()
        }

        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    private fun showOyunBittiDialog() {

        dialog = activity?.let { Dialog(it) }

        if (dialog != null) {

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

                seviyeBirKronometre.setText(requireContext().getString(R.string.tekrar_oyna))
                imageViewKronometre1.visibility = View.GONE

                dialog!!.dismiss()

            }

            dialog!!.menuyeGitOyunBittiButton.setOnClickListener {

                dialog!!.dismiss()

                val action = SeviyeBirFragmentDirections.actionSeviyeBirFragmentToAnaFragment(
                    levelbirincioyun = level
                )
                NavHostFragment.findNavController(this).navigate(action)


            }



            dialog!!.show()

        }


    }

    private fun showKazandinDialog() {

        artikKazandinInteger = 10

        galibiyetSayisi++

        var macSayisi = galibiyetSayisi + maglubiyetSayisi + beraberlikSayisi

        galibiyetOrani = ((galibiyetSayisi / macSayisi)*100).toString().toDouble()

        val format = DecimalFormat("0.#")
        val gercekGalibiyetOrani = format.format(galibiyetOrani)


        dialog = activity?.let { Dialog(it) }

        if (dialog != null) {

            dialog!!.setContentView(R.layout.oyunu_kazandin)

            dialog!!.galibiyetOraniTextOyunuKazandin.text = "Galibiyet Oranı  :  %${gercekGalibiyetOrani}"

            galibiyetHashMap.put("galibiyet",galibiyetSayisi)
            galibiyetHashMap.put("maglubiyet",maglubiyetSayisi)
            galibiyetHashMap.put("beraberlik",beraberlikSayisi)
            galibiyetHashMap.put("playerName",playerName)


            db.collection("Galibiyetler").document(userUid!!).set(galibiyetHashMap)

            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.setCancelable(false)

            dialog!!.carpiOyunBitti.setOnClickListener {

                userUid?.let { it1 -> db.collection("Puanlar").document(it1).delete() }
                userUid?.let { it1 -> db.collection("Problems").document(it1).delete() }

                dialog!!.dismiss()

            }

            dialog!!.menuyeGitOyunBittiButton.setOnClickListener {

                userUid?.let { it1 -> db.collection("Puanlar").document(it1).delete() }
                userUid?.let { it1 -> db.collection("Problems").document(it1).delete() }

                dialog!!.dismiss()

                val action = SeviyeBirFragmentDirections.actionSeviyeBirFragmentToAnaFragment()
                NavHostFragment.findNavController(this).navigate(action)


            }


            dialog!!.show()

        }

    }

    private fun showKaybettinDialog() {

        artikKazandinInteger = 10

        maglubiyetSayisi++

        var macSayisi = galibiyetSayisi + maglubiyetSayisi + beraberlikSayisi

        galibiyetOrani = ((galibiyetSayisi / macSayisi)*100).toString().toDouble()

        val format = DecimalFormat("0.#")
        val gercekGalibiyetOrani = format.format(galibiyetOrani)

        dialog = activity?.let { Dialog(it) }

        if (dialog != null) {

            dialog!!.setContentView(R.layout.oyunu_kaybettin)

            dialog!!.galibiyetOraniTextOyunuKaybettin.text = "Galibiyet Oranı  :  %${gercekGalibiyetOrani}"

            galibiyetHashMap.put("galibiyet",galibiyetSayisi)
            galibiyetHashMap.put("maglubiyet",maglubiyetSayisi)
            galibiyetHashMap.put("beraberlik",beraberlikSayisi)
            galibiyetHashMap.put("playerName",playerName)


            db.collection("Galibiyetler").document(userUid!!).set(galibiyetHashMap)

            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.setCancelable(false)

            //handler.removeCallbacks(runnable2)


            dialog!!.carpiOyunBitti.setOnClickListener {

                userUid?.let { it1 -> db.collection("Puanlar").document(it1).delete() }
                userUid?.let { it1 -> db.collection("Problems").document(it1).delete() }

                dialog!!.dismiss()

            }

            dialog!!.menuyeGitOyunBittiButton.setOnClickListener {

                userUid?.let { it1 -> db.collection("Puanlar").document(it1).delete() }
                userUid?.let { it1 -> db.collection("Problems").document(it1).delete() }

                dialog!!.dismiss()

                val action = SeviyeBirFragmentDirections.actionSeviyeBirFragmentToAnaFragment()
                NavHostFragment.findNavController(this).navigate(action)


            }

            dialog!!.show()

        }

    }

    private fun showKaybettinDialogInternetYok() {

        artikKazandinInteger = 10

        maglubiyetSayisi++

        var macSayisi = galibiyetSayisi + maglubiyetSayisi + beraberlikSayisi

        galibiyetOrani = ((galibiyetSayisi / macSayisi)*100).toString().toDouble()

        val format = DecimalFormat("0.#")
        val gercekGalibiyetOrani = format.format(galibiyetOrani)

        dialog = activity?.let { Dialog(it) }

        if (dialog != null) {

            dialog!!.setContentView(R.layout.oyunu_kaybettin)

            dialog!!.galibiyetOraniTextOyunuKaybettin.text = "Galibiyet Oranı  :  %${gercekGalibiyetOrani}"

            galibiyetHashMap.put("galibiyet",galibiyetSayisi)
            galibiyetHashMap.put("maglubiyet",maglubiyetSayisi)
            galibiyetHashMap.put("beraberlik",beraberlikSayisi)
            galibiyetHashMap.put("playerName",playerName)


            db.collection("Galibiyetler").document(userUid!!).set(galibiyetHashMap)

            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.setCancelable(false)

            //handler.removeCallbacks(runnable2)


            dialog!!.carpiOyunBitti.setOnClickListener {

                userUid?.let { it1 -> db.collection("Puanlar").document(it1).delete() }
                userUid?.let { it1 -> db.collection("Problems").document(it1).delete() }

                dialog!!.dismiss()

            }

            dialog!!.menuyeGitOyunBittiButton.setOnClickListener {

                userUid?.let { it1 -> db.collection("Puanlar").document(it1).delete() }
                userUid?.let { it1 -> db.collection("Problems").document(it1).delete() }

                dialog!!.dismiss()

                val action = SeviyeBirFragmentDirections.actionSeviyeBirFragmentToAnaFragment()
                NavHostFragment.findNavController(this).navigate(action)


            }

            dialog!!.show()

        }

    }

    private fun showKazandinDialogExit() {

        artikKazandinInteger = 10

        galibiyetSayisi++

        var macSayisi = galibiyetSayisi + maglubiyetSayisi + beraberlikSayisi

        galibiyetOrani = ((galibiyetSayisi / macSayisi)*100).toString().toDouble()

        val format = DecimalFormat("0.#")
        val gercekGalibiyetOrani = format.format(galibiyetOrani)

        oyunuExitleKazandin = 10
        reference7?.remove()

        if (coklukullaniciInteger == 1) {

            reference11 = db.collection("Exits").whereEqualTo("roomName",roomName).addSnapshotListener { value, error ->

                if (value != null){

                    if ( !value.isEmpty){

                        val documents = value.documents

                        for ( document in documents) {

                            val exitInteger = document.getLong("exitInteger")

                            if (exitInteger != null && exitInteger == 2.toLong()) {

                                db.collection("Exits").document(roomName).delete()
                                reference11?.remove()

                            }

                        }

                    }
                }

            }

        } else if ( coklukullaniciInteger == 2) {

            reference11 = db.collection("Exits").whereEqualTo("roomName",roomName).addSnapshotListener { value, error ->

                if (value != null){

                    if ( !value.isEmpty){

                        val documents = value.documents

                        for ( document in documents) {

                            val exitInteger = document.getLong("exitInteger")

                            if (exitInteger != null && exitInteger == 1.toLong()) {


                                db.collection("Exits").document(roomName).delete()
                                reference11?.remove()

                            }

                        }

                    }
                }

            }

        }

        dialog = activity?.let { Dialog(it) }

        if (dialog != null) {

            dialog!!.setContentView(R.layout.oyunu_kazandin)

            dialog!!.galibiyetOraniTextOyunuKazandin.text = "Galibiyet Oranı  :  %${gercekGalibiyetOrani}"

            galibiyetHashMap.put("galibiyet",galibiyetSayisi)
            galibiyetHashMap.put("maglubiyet",maglubiyetSayisi)
            galibiyetHashMap.put("beraberlik",beraberlikSayisi)
            galibiyetHashMap.put("playerName",playerName)


            db.collection("Galibiyetler").document(userUid!!).set(galibiyetHashMap)

            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.setCancelable(false)

            handler.removeCallbacks(runnable3)
            handler.removeCallbacks(runnable4)


            reference?.remove()
            reference2?.remove()
            reference3?.remove()
            reference4?.remove()
            reference5?.remove()
            reference6?.remove()
            reference7?.remove()
            reference9?.remove()
            reference10?.remove()
            reference11?.remove()
            reference12?.remove()
            reference13?.remove()
            reference14?.remove()
            reference15?.remove()
            reference20?.remove()
            reference21?.remove()
            reference22?.remove()
            reference23?.remove()


            dialog!!.oyunBittiText.text = "Rakibin oyundan çıktığı için sen kazandın!"


            dialog!!.carpiOyunBitti.setOnClickListener {

                userUid?.let { it1 -> db.collection("Puanlar").document(it1).delete() }
                userUid?.let { it1 -> db.collection("Problems").document(it1).delete() }
                db.collection("Exits").document(roomName).delete()

                dialog!!.dismiss()

            }

            dialog!!.menuyeGitOyunBittiButton.setOnClickListener {

                userUid?.let { it1 -> db.collection("Puanlar").document(it1).delete() }
                userUid?.let { it1 -> db.collection("Problems").document(it1).delete() }
                db.collection("Exits").document(roomName).delete()


                dialog!!.dismiss()

                val action = SeviyeBirFragmentDirections.actionSeviyeBirFragmentToAnaFragment()
                NavHostFragment.findNavController(this).navigate(action)


            }


            dialog!!.show()

        }

    }

    private fun showKazandinDialogPuanla() {

        galibiyetSayisi++

        var macSayisi = galibiyetSayisi + maglubiyetSayisi + beraberlikSayisi

        galibiyetOrani = ((galibiyetSayisi.toDouble() / macSayisi.toDouble())*100)

        val format = DecimalFormat("0.#")
        val gercekGalibiyetOrani = format.format(galibiyetOrani)

        artikKazandinInteger = 10

        dialog = activity?.let { Dialog(it) }

        if (dialog != null) {


            dialog!!.setContentView(R.layout.oyunu_kazandin_puanla)

            if (coklukullaniciInteger == 1) {

                dialog!!.nameBirinciOyuncuSonuc.text = nameBirinciOyuncu.text.toString()
                dialog!!.nameIkinciOyuncuSonuc.text = nameIkinciOyuncu.text.toString()
                dialog!!.puanSayaciBirinciOyuncuSonuc.text = puan.toString()
                dialog!!.puanSayaciIkinciOyuncuSonuc.text = puanSayaciIkinciOyuncu?.text.toString()

            } else {

                dialog!!.nameBirinciOyuncuSonuc.text = nameBirinciOyuncu.text.toString()
                dialog!!.nameIkinciOyuncuSonuc.text = nameIkinciOyuncu.text.toString()
                dialog!!.puanSayaciBirinciOyuncuSonuc.text = puanSayaciBirinciOyuncu?.text.toString()
                dialog!!.puanSayaciIkinciOyuncuSonuc.text = puan.toString()

            }

            dialog!!.galibiyetOraniTextOyunuKazandinPuanla.text = "Galibiyet Oranı  :  %${gercekGalibiyetOrani}"

            galibiyetHashMap.put("galibiyet",galibiyetSayisi)
            galibiyetHashMap.put("maglubiyet",maglubiyetSayisi)
            galibiyetHashMap.put("beraberlik",beraberlikSayisi)
            galibiyetHashMap.put("playerName",playerName)


            db.collection("Galibiyetler").document(userUid!!).set(galibiyetHashMap)

            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.setCancelable(false)

            //handler.removeCallbacks(runnable2)

            reference?.remove()
            reference2?.remove()
            reference3?.remove()
            reference4?.remove()
            reference5?.remove()
            reference6?.remove()
            reference7?.remove()
            reference9?.remove()
            reference10?.remove()
            reference11?.remove()
            reference12?.remove()
            reference13?.remove()
            reference14?.remove()
            reference15?.remove()
            reference16?.remove()
            reference17?.remove()
            reference18?.remove()
            reference19?.remove()
            reference20?.remove()
            reference21?.remove()
            reference22?.remove()
            reference23?.remove()


            dialog!!.tekrarOyunBittiButton.setOnClickListener {

                userUid?.let { it1 -> db.collection("Puanlar").document(it1).delete() }
                userUid?.let { it1 -> db.collection("Problems").document(it1).delete() }

                dialog!!.dismiss()

                tekrarOyna()

            }

            dialog!!.carpiOyunBitti.setOnClickListener {

                userUid?.let { it1 -> db.collection("Puanlar").document(it1).delete() }
                userUid?.let { it1 -> db.collection("Problems").document(it1).delete() }

                dialog!!.dismiss()

                //db.collection("Exits").document(roomName).delete()


            }

            dialog!!.menuyeGitOyunBittiButton.setOnClickListener {

                userUid?.let { it1 -> db.collection("Puanlar").document(it1).delete() }
                userUid?.let { it1 -> db.collection("Problems").document(it1).delete() }

                dialog!!.dismiss()

                val action = SeviyeBirFragmentDirections.actionSeviyeBirFragmentToAnaFragment()
                NavHostFragment.findNavController(this).navigate(action)


            }


            dialog!!.show()

        }

    }

    private fun showKaybettinDialogPuanla() {

        maglubiyetSayisi++

        var macSayisi = galibiyetSayisi + maglubiyetSayisi + beraberlikSayisi

        galibiyetOrani = ((galibiyetSayisi.toDouble() / macSayisi.toDouble())*100)

        println("galibiyetOrani: " + galibiyetOrani)
        println("macSayisi: " + macSayisi)
        println("galibiyetSayisi: " + galibiyetSayisi)


        val format = DecimalFormat("0.#")

        val gercekGalibiyetOrani = format.format(galibiyetOrani)

        artikKazandinInteger = 10

        dialog = activity?.let { Dialog(it) }

        if (dialog != null) {

            dialog!!.setContentView(R.layout.oyunu_kaybettin_puanla)

            if (coklukullaniciInteger == 1) {

                dialog!!.nameBirinciOyuncuSonuc.text = nameBirinciOyuncu.text.toString()
                dialog!!.nameIkinciOyuncuSonuc.text = nameIkinciOyuncu.text.toString()
                dialog!!.puanSayaciBirinciOyuncuSonuc.text = puan.toString()
                dialog!!.puanSayaciIkinciOyuncuSonuc.text = puanSayaciIkinciOyuncu?.text.toString()

            } else {

                dialog!!.nameBirinciOyuncuSonuc.text = nameBirinciOyuncu.text.toString()
                dialog!!.nameIkinciOyuncuSonuc.text = nameIkinciOyuncu.text.toString()
                dialog!!.puanSayaciBirinciOyuncuSonuc.text = puanSayaciBirinciOyuncu?.text.toString()
                dialog!!.puanSayaciIkinciOyuncuSonuc.text = puan.toString()

            }

            dialog!!.galibiyetOraniTextOyunuKaybettinPuanla.text = "Galibiyet Oranı  :  %${gercekGalibiyetOrani}"

            galibiyetHashMap.put("galibiyet",galibiyetSayisi)
            galibiyetHashMap.put("maglubiyet",maglubiyetSayisi)
            galibiyetHashMap.put("beraberlik",beraberlikSayisi)
            galibiyetHashMap.put("playerName",playerName)


            db.collection("Galibiyetler").document(userUid!!).set(galibiyetHashMap)

            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.setCancelable(false)


            reference?.remove()
            reference2?.remove()
            reference3?.remove()
            reference4?.remove()
            reference5?.remove()
            reference6?.remove()
            reference7?.remove()
            reference9?.remove()
            reference10?.remove()
            reference11?.remove()
            reference12?.remove()
            reference13?.remove()
            reference14?.remove()
            reference15?.remove()
            reference16?.remove()
            reference17?.remove()
            reference18?.remove()
            reference19?.remove()
            reference20?.remove()
            reference21?.remove()
            reference22?.remove()
            reference23?.remove()


            dialog!!.tekrarOyunBittiButton.setOnClickListener {

                dialog!!.dismiss()

                userUid?.let { it1 -> db.collection("Puanlar").document(it1).delete() }
                userUid?.let { it1 -> db.collection("Problems").document(it1).delete() }

                tekrarOyna()

            }

            dialog!!.carpiOyunBitti.setOnClickListener {

                dialog!!.dismiss()

                userUid?.let { it1 -> db.collection("Puanlar").document(it1).delete() }
                userUid?.let { it1 -> db.collection("Problems").document(it1).delete() }

            }

            dialog!!.menuyeGitOyunBittiButton.setOnClickListener {

                dialog!!.dismiss()

                userUid?.let { it1 -> db.collection("Puanlar").document(it1).delete() }
                userUid?.let { it1 -> db.collection("Problems").document(it1).delete() }

                val action = SeviyeBirFragmentDirections.actionSeviyeBirFragmentToAnaFragment()
                NavHostFragment.findNavController(this).navigate(action)


            }


            dialog!!.show()

        }

    }

    private fun showBerabere() {

        beraberlikSayisi++

        var macSayisi = galibiyetSayisi + maglubiyetSayisi + beraberlikSayisi

        galibiyetOrani = ((galibiyetSayisi / macSayisi)*100).toString().toDouble()

        val format = DecimalFormat("0.#")
        val gercekGalibiyetOrani = format.format(galibiyetOrani)

        artikKazandinInteger = 10

        dialog = activity?.let { Dialog(it) }

        if (dialog != null) {

            dialog!!.setContentView(R.layout.oyun_berabere)

            if (coklukullaniciInteger == 1) {

                dialog!!.nameBirinciOyuncuSonuc.text = nameBirinciOyuncu.text.toString()
                dialog!!.nameIkinciOyuncuSonuc.text = nameIkinciOyuncu.text.toString()
                dialog!!.puanSayaciBirinciOyuncuSonuc.text = puan.toString()
                dialog!!.puanSayaciIkinciOyuncuSonuc.text = puanSayaciIkinciOyuncu?.text.toString()

            } else {

                dialog!!.nameBirinciOyuncuSonuc.text = nameBirinciOyuncu.text.toString()
                dialog!!.nameIkinciOyuncuSonuc.text = nameIkinciOyuncu.text.toString()
                dialog!!.puanSayaciBirinciOyuncuSonuc.text = puanSayaciBirinciOyuncu?.text.toString()
                dialog!!.puanSayaciIkinciOyuncuSonuc.text = puan.toString()

            }

            dialog!!.galibiyetOraniTextOyunBerabere.text = "Galibiyet Oranı  :  %${gercekGalibiyetOrani}"

            galibiyetHashMap.put("galibiyet",galibiyetSayisi)
            galibiyetHashMap.put("maglubiyet",maglubiyetSayisi)
            galibiyetHashMap.put("beraberlik",beraberlikSayisi)
            galibiyetHashMap.put("playerName",playerName)


            db.collection("Galibiyetler").document(userUid!!).set(galibiyetHashMap)

            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.setCancelable(false)


            reference?.remove()
            reference2?.remove()
            reference3?.remove()
            reference4?.remove()
            reference5?.remove()
            reference6?.remove()
            reference7?.remove()
            reference9?.remove()
            reference10?.remove()
            reference11?.remove()
            reference12?.remove()
            reference13?.remove()
            reference14?.remove()
            reference15?.remove()
            reference16?.remove()
            reference17?.remove()
            reference18?.remove()
            reference19?.remove()
            reference20?.remove()
            reference21?.remove()
            reference22?.remove()
            reference23?.remove()


            dialog!!.tekrarOyunBittiButton.setOnClickListener {

                dialog!!.dismiss()

                userUid?.let { it1 -> db.collection("Puanlar").document(it1).delete() }
                userUid?.let { it1 -> db.collection("Problems").document(it1).delete() }

                tekrarOyna()

            }

            dialog!!.carpiOyunBitti.setOnClickListener {

                dialog!!.dismiss()

                userUid?.let { it1 -> db.collection("Puanlar").document(it1).delete() }
                userUid?.let { it1 -> db.collection("Problems").document(it1).delete() }

            }

            dialog!!.menuyeGitOyunBittiButton.setOnClickListener {

                dialog!!.dismiss()

                userUid?.let { it1 -> db.collection("Puanlar").document(it1).delete() }
                userUid?.let { it1 -> db.collection("Problems").document(it1).delete() }

                val action = SeviyeBirFragmentDirections.actionSeviyeBirFragmentToAnaFragment()
                NavHostFragment.findNavController(this).navigate(action)


            }


            dialog!!.show()

        }

    }

    private fun tekrarOyna() {

        artiText.text = ""
        esittirSoruText.text = ""
        birinciSonucTextSeviyeBir.text = ""
        ikinciSonucTextSeviyeBir.text = ""

        val player1 = nameBirinciOyuncu.text.toString()

        roomName = player1

        progressDialogTekrarOyna = ProgressDialog(context)
        progressDialogTekrarOyna.show()
        progressDialogTekrarOyna.setContentView(R.layout.layout_progress_dialog)
        progressDialogTekrarOyna.ikinciOyuncuBekleniyor.setText("Rakip bekleniyor...")
        progressDialogTekrarOyna.setCancelable(true)
        progressDialogTekrarOyna.setCanceledOnTouchOutside(false)

        if ( coklukullaniciInteger == 1 ) {

            val hashMap = hashMapOf<Any,Any>()
            hashMap.put("player1",roomName)

            userUid?.let { db.collection("Rooms").document(it).set(hashMap).addOnSuccessListener {

               reference16 =  db.collection("Rooms").whereEqualTo("player2",roomName).addSnapshotListener { value, error ->

                    if (error == null) {

                        if (value != null) {

                            if (!value.isEmpty) {

                                val documents = value.documents

                                for (document in documents) {

                                    val ikinciOyuncuVarMi = document.getString("player2")

                                    if (ikinciOyuncuVarMi != null) {

                                        birincininOyunu()
                                        reference16?.remove()

                                    }


                                }

                            }
                        }
                    }

                }

            }


            }

        } else {

            val player2 = nameIkinciOyuncu.text.toString()

            val hashMap = hashMapOf<Any,Any>()
            hashMap.put("player2",roomName)
            hashMap.put("kullaniciAdim", player2)

            userUid?.let { db.collection("Rooms").document(it).set(hashMap).addOnSuccessListener {

                reference17 =  db.collection("Rooms").whereEqualTo("player1",roomName).addSnapshotListener { value, error ->

                    if (error == null) {

                        if (value != null) {

                            if (!value.isEmpty) {

                                val documents = value.documents

                                for (document in documents) {

                                    val birinciOyuncuVarMi = document.getString("player1")

                                    if (birinciOyuncuVarMi != null) {

                                        ikincininOyunu()

                                        reference17?.remove()

                                    }


                                }

                            }
                        }
                    }

                }

            } }

        }

    }

    private fun ikincininOyunu() {

        progressDialogTekrarOyna.dismiss()

        puan=0
        puanKontrol = 0

        puanSayaciIkinciOyuncu?.text = 0.toString()
        puanSayaciBirinciOyuncu?.text = 0.toString()

        for (c in 0..8) {

            numberArrayList[c].text = null
            numberArrayList[c].setBackgroundColor(Color.WHITE)
        }

        seviyeBirKronometre.text = "Oyun Başlıyor..."

        artikKazandinInteger = 0

        baslangicGeriSayan = object : CountDownTimer(3200, 1000) {
            override fun onTick(p0: Long) {

                seviyeBirOyunBaslangicText.text = ((p0) / 1000).toString()

            }

            override fun onFinish() {

                seviyeBirOyunBaslangicText.visibility = View.GONE
                oyunBaslangicLayout.visibility = View.GONE

                time2 = 60000

                gameIsStarting2()

            }

        }.start()

    }

    private fun birincininOyunu() {

        progressDialogTekrarOyna.dismiss()

        puan=0
        puanKontrol = 0

        puanSayaciIkinciOyuncu?.text = 0.toString()
        puanSayaciBirinciOyuncu?.text = 0.toString()

        for (c in 0..8) {

            numberArrayList[c].text = null
            numberArrayList[c].setBackgroundColor(Color.WHITE)
        }

        seviyeBirKronometre.text = "Oyun Başlıyor..."

        artikKazandinInteger = 0

        baslangicGeriSayan = object : CountDownTimer(3200, 1000) {
            override fun onTick(p0: Long) {

                seviyeBirOyunBaslangicText.text = ((p0) / 1000).toString()

            }

            override fun onFinish() {

                seviyeBirOyunBaslangicText.visibility = View.GONE
                oyunBaslangicLayout.visibility = View.GONE

                time1 = 60000

                gameIsStarting()
            }

        }.start()

    }

    private fun levels() {


        if (level == 1) {

            geriSaymaSuresi = 15300
            degistirmePeriyodu = 2500

        } else if (level == 2) {

            geriSaymaSuresi = 14300
            degistirmePeriyodu = 2300

        } else if (level == 3) {

            geriSaymaSuresi = 13800
            degistirmePeriyodu = 2000

        } else if (level == 4) {

            geriSaymaSuresi = 13400
            degistirmePeriyodu = 1800

        } else if (level == 5) {

            geriSaymaSuresi = 13000
            degistirmePeriyodu = 1600

        } else if (level == 6) {

            geriSaymaSuresi = 12500
            degistirmePeriyodu = 1400

        } else if (level == 7) {

            geriSaymaSuresi = 12000
            degistirmePeriyodu = 1300

        } else if (level == 8) {

            geriSaymaSuresi = 11500
            degistirmePeriyodu = 1200

        } else if (level == 9) {

            geriSaymaSuresi = 11000
            degistirmePeriyodu = 1150

        } else if (level == 10) {

            geriSaymaSuresi = 10000
            degistirmePeriyodu = 1100

        } else if (level == 11) {

            geriSaymaSuresi = 9900
            degistirmePeriyodu = 1000

        } else if (level == 12) {

            geriSaymaSuresi = 9800
            degistirmePeriyodu = 950

        } else if (level == 13) {

            geriSaymaSuresi = 9700
            degistirmePeriyodu = 900

        } else if (level == 14) {

            geriSaymaSuresi = 9600
            degistirmePeriyodu = 900

        } else if (level == 15) {

            geriSaymaSuresi = 9500
            degistirmePeriyodu = 880

        } else if (level == 16) {

            geriSaymaSuresi = 9500
            degistirmePeriyodu = 860

        }


    }

    private fun levelsForSpeed() {


        if (levelForSpeed == 1) {

            geriSaymaSuresi = 15300
            degistirmePeriyodu = 2500

        } else if (levelForSpeed == 2) {

            geriSaymaSuresi = 14300
            degistirmePeriyodu = 2300

        } else if (levelForSpeed == 3) {

            geriSaymaSuresi = 13800
            degistirmePeriyodu = 2000

        } else if (levelForSpeed == 4) {

            geriSaymaSuresi = 13400
            degistirmePeriyodu = 1800

        } else if (levelForSpeed == 5) {

            geriSaymaSuresi = 13000
            degistirmePeriyodu = 1600

        } else if (levelForSpeed == 6) {

            geriSaymaSuresi = 12500
            degistirmePeriyodu = 1400

        } else if (levelForSpeed == 7) {

            geriSaymaSuresi = 12000
            degistirmePeriyodu = 1300

        } else if (levelForSpeed == 8) {

            geriSaymaSuresi = 11500
            degistirmePeriyodu = 1200

        } else if (levelForSpeed == 9) {

            geriSaymaSuresi = 11000
            degistirmePeriyodu = 1150

        } else if (levelForSpeed == 10) {

            geriSaymaSuresi = 10000
            degistirmePeriyodu = 1100

        } else if (levelForSpeed == 11) {

            geriSaymaSuresi = 9900
            degistirmePeriyodu = 1000

        } else if (levelForSpeed == 12) {

            geriSaymaSuresi = 9800
            degistirmePeriyodu = 950

        } else if (levelForSpeed == 13) {

            geriSaymaSuresi = 9700
            degistirmePeriyodu = 900

        } else if (levelForSpeed == 14) {

            geriSaymaSuresi = 9600
            degistirmePeriyodu = 900

        } else if (levelForSpeed == 15) {

            geriSaymaSuresi = 9500
            degistirmePeriyodu = 880

        } else if (levelForSpeed == 16) {

            geriSaymaSuresi = 9500
            degistirmePeriyodu = 860

        }


    }

    private fun showLeaderboard() {

        val account = GoogleSignIn.getLastSignedInAccount(context)

        if (account != null) {

            Games.getLeaderboardsClient(context, account)
                .getLeaderboardIntent(requireContext().getString(R.string.leaderboard_id_1))
                .addOnSuccessListener { intent ->
                    startActivityForResult(
                        intent,
                        RC_LEADERBOARD_UI
                    )
                }

        } else {

            Toast.makeText(
                context, "En Yüksek Puanları görebilmeniz için " +
                        "Google hesabınızla giriş yapmalısınız.", Toast.LENGTH_LONG
            ).show()


        }

    }

    override fun onDetach() {
        super.onDetach()

        geriSayan?.cancel()

        if ( coklukullaniciInteger == 1  || coklukullaniciInteger == 2) {

            geriSayanBirinci?.cancel()
            geriSayanIkinci?.cancel()

        }

        baslangicGeriSayan?.cancel()


    }

    override fun onDestroyView() {
        super.onDestroyView()

        println("onDestroyView çağrıldı")

        if ( coklukullaniciInteger == 1  || coklukullaniciInteger == 2) {

            db.collection("Rooms").document(userUid!!).delete()
            db.collection("Problems").document(userUid!!).delete()

            oyuncularListesi.isEnabled = true

            preferences.edit().putInt("reklamPeriyodu", reklamPeriyodu).apply()

            if (coklukullaniciInteger == 1){
                db.collection("RoomIndex").document(roomName).delete()
            }

            reference?.remove()
            reference2?.remove()
            reference3?.remove()
            reference4?.remove()
            reference5?.remove()
            reference6?.remove()
            reference7?.remove()
            reference9?.remove()
            reference10?.remove()
            reference11?.remove()
            reference12?.remove()
            reference13?.remove()
            reference14?.remove()
            reference15?.remove()
            reference16?.remove()
            reference17?.remove()
            reference18?.remove()
            reference19?.remove()
            reference20?.remove()
            reference21?.remove()
            reference22?.remove()
            reference23?.remove()



            if ( oyunuExitleKazandin == 10 ) {

                if (coklukullaniciInteger == 1) {

                    reference11 = db.collection("Exits").whereEqualTo("roomName",roomName).addSnapshotListener { value, error ->

                        if (value != null){

                            if ( !value.isEmpty){

                                val documents = value.documents

                                for ( document in documents) {

                                    val exitInteger = document.getLong("exitInteger")

                                    if (exitInteger != null && exitInteger == 2.toLong()) {

                                        db.collection("Exits").document(roomName).delete()
                                        reference11?.remove()

                                    }

                                }

                            }
                        }

                    }

                } else if ( coklukullaniciInteger == 2) {

                    reference11 = db.collection("Exits").whereEqualTo("roomName",roomName).addSnapshotListener { value, error ->

                        if (value != null){

                            if ( !value.isEmpty){

                                val documents = value.documents

                                for ( document in documents) {

                                    val exitInteger = document.getLong("exitInteger")

                                    if (exitInteger != null && exitInteger == 1.toLong()) {


                                        db.collection("Exits").document(roomName).delete()
                                        reference11?.remove()

                                    }

                                }

                            }
                        }

                    }

                }

            }

        }



    }

    override fun onDestroy() {
        super.onDestroy()


        if ( coklukullaniciInteger == 1  || coklukullaniciInteger == 2) {

            geriSayanBirinci?.cancel()
            geriSayanIkinci?.cancel()

        }

        geriSayan?.cancel()

    }

    override fun onPause() {
        super.onPause()


        if ( forExitInteger == 0 && modeInteger == 3 && artikKazandinInteger != 10 ) {

            val hashMap = hashMapOf<Any, Any>()
            hashMap.put("sorunInteger", coklukullaniciInteger)
            hashMap.put("roomName",roomName)

            userUid?.let {
                db.collection("Problems").document(it).set(hashMap).addOnFailureListener { exception ->

                    Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT).show()


                }.addOnSuccessListener {

                    println("problem çağrıldı")

                    rastgeleSayi = 555

                    onPauseTime = System.currentTimeMillis()

                }

            }


        }

     }


}