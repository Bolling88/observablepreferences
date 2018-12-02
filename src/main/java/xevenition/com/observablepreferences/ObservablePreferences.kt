package xevenition.com.observablepreferences

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class ObservablePreferences(app: Application) {
    private val listener: SharedPreferences.OnSharedPreferenceChangeListener

    private val integerObservableMap = hashMapOf<String, BehaviorSubject<Int>>()
    private val stringObservableMap = hashMapOf<String, BehaviorSubject<String>>()
    private val booleanObservableMap = hashMapOf<String, BehaviorSubject<Boolean>>()
    private val longObservableMap = hashMapOf<String, BehaviorSubject<Long>>()
    private val floatObservableMap = hashMapOf<String, BehaviorSubject<Float>>()
    private val stringSetObservableMap = hashMapOf<String, BehaviorSubject<MutableSet<String>>>()

    private val appSharedPrefsConstant = app.applicationInfo.loadLabel(app.packageManager).toString()
    private val appSharedPrefs: SharedPreferences =
        app.getSharedPreferences(appSharedPrefsConstant, Activity.MODE_PRIVATE)

    init {
        //no need to unsubscribe this, it is all stored in a weak reference
        listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            integerObservableMap[key]?.onNext(prefs.getInt(key, -1))
            stringObservableMap[key]?.onNext(prefs.getString(key, ""))
            booleanObservableMap[key]?.onNext(prefs.getBoolean(key, false))
            longObservableMap[key]?.onNext(prefs.getLong(key, -1))
            floatObservableMap[key]?.onNext(prefs.getFloat(key, -1f))
            stringSetObservableMap[key]?.onNext(prefs.getStringSet(key, mutableSetOf()))
        }
        appSharedPrefs.registerOnSharedPreferenceChangeListener(listener)
    }

    //String methods
    fun getString(key: String): String {
        return getString(key, "")
    }

    fun getString(key: String, defaultValue: String): String {
        return appSharedPrefs.getString(key, defaultValue)
    }

    fun saveString(key: String, content: String) {
        val prefsEditor = appSharedPrefs.edit()
        prefsEditor.putString(key, content)
        prefsEditor.commit()
    }

    fun getObservableString(key: String): BehaviorSubject<String> {
        return getObservableString(key, "")
    }

    fun getObservableString(key: String, defaultValue: String): BehaviorSubject<String> {
        return if (stringObservableMap.containsKey(key)) {
            val publisher = stringObservableMap[key]!!
            publisher.onNext(appSharedPrefs.getString(key, defaultValue))
            publisher
        } else {
            val publisher = BehaviorSubject.create<String>()
            publisher.subscribeOn(Schedulers.io())
            stringObservableMap[key] = publisher
            publisher.onNext(getString(key, defaultValue))
            publisher
        }
    }

    //Long methods
    fun getLong(key: String): Long {
        return getLong(key, -1L)
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return appSharedPrefs.getLong(key, defaultValue)
    }

    fun saveLong(key: String, content: Long) {
        val prefsEditor = appSharedPrefs.edit()
        prefsEditor.putLong(key, content)
        prefsEditor.commit()
    }

    fun getObservableLong(key: String): BehaviorSubject<Long> {
        return getObservableLong(key, -1L)
    }

    fun getObservableLong(key: String, defaultValue: Long): BehaviorSubject<Long> {
        return if (longObservableMap.containsKey(key)) {
            val publisher = longObservableMap[key]!!
            publisher.onNext(appSharedPrefs.getLong(key, defaultValue))
            publisher
        } else {
            val publisher = BehaviorSubject.create<Long>()
            publisher.subscribeOn(Schedulers.io())
            longObservableMap[key] = publisher
            publisher.onNext(getLong(key, defaultValue))
            publisher
        }
    }

    //Boolean methods
    fun getBoolean(key: String): Boolean {
        return getBoolean(key, false)
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return appSharedPrefs.getBoolean(key, defaultValue)
    }

    fun saveBoolean(key: String, content: Boolean) {
        val prefsEditor = appSharedPrefs.edit()
        prefsEditor.putBoolean(key, content)
        prefsEditor.commit()
    }

    fun getObservableBoolean(key: String): BehaviorSubject<Boolean> {
        return getObservableBoolean(key, false)
    }

    fun getObservableBoolean(key: String, defaultValue: Boolean): BehaviorSubject<Boolean> {
        return if (booleanObservableMap.containsKey(key)) {
            val publisher = booleanObservableMap[key]!!
            publisher.onNext(appSharedPrefs.getBoolean(key, defaultValue))
            publisher
        } else {
            val publisher = BehaviorSubject.create<Boolean>()
            publisher.subscribeOn(Schedulers.io())
            booleanObservableMap[key] = publisher
            publisher.onNext(getBoolean(key, defaultValue))
            publisher
        }
    }

    //Int methods
    fun getInt(key: String): Int {
        return getInt(key, -1)
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return appSharedPrefs.getInt(key, defaultValue)
    }

    fun saveInt(key: String, content: Int) {
        val prefsEditor = appSharedPrefs.edit()
        prefsEditor.putInt(key, content)
        prefsEditor.commit()
    }

    fun getObservableInt(key: String): BehaviorSubject<Int> {
        return getObservableInt(key, -1)
    }

    fun getObservableInt(key: String, defaultValue: Int): BehaviorSubject<Int> {
        return if (integerObservableMap.containsKey(key)) {
            val publisher = integerObservableMap[key]!!
            publisher.onNext(appSharedPrefs.getInt(key, defaultValue))
            publisher
        } else {
            val publisher = BehaviorSubject.create<Int>()
            publisher.subscribeOn(Schedulers.io())
            integerObservableMap[key] = publisher
            publisher.onNext(getInt(key, defaultValue))
            publisher
        }
    }

    //Float methods
    fun getFloat(key: String): Float {
        return getFloat(key, -1f)
    }

    fun getFloat(key: String, defaultValue: Float): Float {
        return appSharedPrefs.getFloat(key, defaultValue)
    }

    fun saveFloat(key: String, content: Float) {
        val prefsEditor = appSharedPrefs.edit()
        prefsEditor.putFloat(key, content)
        prefsEditor.commit()
    }

    fun getObservableFloat(key: String): BehaviorSubject<Float> {
        return getObservableFloat(key, -1f)
    }

    fun getObservableFloat(key: String, defaultValue: Float): BehaviorSubject<Float> {
        return if (floatObservableMap.containsKey(key)) {
            val publisher = floatObservableMap[key]!!
            publisher.onNext(appSharedPrefs.getFloat(key, defaultValue))
            publisher
        } else {
            val publisher = BehaviorSubject.create<Float>()
            publisher.subscribeOn(Schedulers.io())
            floatObservableMap[key] = publisher
            publisher.onNext(defaultValue)
            publisher
        }
    }

    //StringSet methods
    fun getStringSet(key: String): MutableSet<String> {
        return getStringSet(key, mutableSetOf())
    }

    fun getStringSet(key: String, defaultValue: MutableSet<String>): MutableSet<String> {
        return appSharedPrefs.getStringSet(key, defaultValue)
    }

    fun saveStringSet(key: String, content: MutableSet<String>) {
        val prefsEditor = appSharedPrefs.edit()
        prefsEditor.putStringSet(key, content)
        prefsEditor.commit()
    }

    fun getObservableStringSet(key: String): BehaviorSubject<MutableSet<String>> {
        return getObservableStringSet(key, mutableSetOf())
    }

    fun getObservableStringSet(key: String, defaultValue: MutableSet<String>): BehaviorSubject<MutableSet<String>> {
        return if (stringSetObservableMap.containsKey(key)) {
            val publisher = stringSetObservableMap[key]!!
            publisher.onNext(appSharedPrefs.getStringSet(key, defaultValue))
            publisher
        } else {
            val publisher = BehaviorSubject.create<MutableSet<String>>()
            publisher.subscribeOn(Schedulers.io())
            stringSetObservableMap[key] = publisher
            publisher.onNext(defaultValue)
            publisher
        }
    }
}