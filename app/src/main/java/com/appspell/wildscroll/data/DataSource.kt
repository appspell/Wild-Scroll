package com.appspell.wildscroll.data

import android.content.res.Resources
import com.appspell.wildscroll.R
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


class DataSource(val resources: Resources) {
    val companies: List<Company>
        get() {

            val json = resources.openRawResource(R.raw.mockdata).bufferedReader().use { it.readText() }

            val listType = object : TypeToken<List<Company>>() {}.type
            val list = GsonBuilder().create().fromJson<ArrayList<Company>>(json, listType)
//
//            val alp = "QWERTYUIOOPLKJHGFDSAZXVBNM1234567890"
//            for(i in 0..100000) {
//                var name = ""
//                for (n in 0..20) {
//                    name += alp[Random().nextInt(alp.length)]
//                }
//
//                list.add(Company(name, name[0].toUpperCase().toString()))
//            }
            return list.sortedBy { it.company } //TODO do it in background
        }

}

