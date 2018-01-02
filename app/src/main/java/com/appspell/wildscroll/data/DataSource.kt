package com.appspell.wildscroll.data

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


class DataSource(private val context: Context) {
    val books: List<Book>
        get() {
            //Sorry for JSON here :)
            val json = "[{\"title\":\"A Journey to the Center of the Earth\",\"author\":\"Jules Verne\"},{\"title\":\"The War of the Worlds\",\"author\":\"H.G. Wells\"},{\"title\":\"Brave New World\",\"author\":\"Aldous Huxley\"},{\"title\":\"When Worlds Collide\",\"author\":\"Edwin Balmer &lt; Philip Wylie\"},{\"title\":\"Odd John\",\"author\":\"Olaf Stapledon\"},{\"title\":\"Nineteen Eighty-Four\",\"author\":\"George Orwell\"},{\"title\":\"Earth Abides\",\"author\":\"George R. Stewart\"},{\"title\":\"Foundation\",\"author\":\"Isaac Asimov\"},{\"title\":\"The Illustrated Man\",\"author\":\"Ray Bradbury\"},{\"title\":\"The Demolished Man\",\"author\":\"Alfred Bester\"},{\"title\":\"Ring Around the Sun\",\"author\":\"Clifford D. Simak\"},{\"title\":\"Mission of Gravity\",\"author\":\"Hal Clement\"},{\"title\":\"The Long Tomorrow\",\"author\":\"Leigh Brackett\"},{\"title\":\"The Chrysalids\",\"author\":\"John Wyndham\"},{\"title\":\"The Death of Grass or No Blade of Grass\",\"author\":\"John Christopher\"},{\"title\":\"Starship Troopers\",\"author\":\"Robert Heinlein\"},{\"title\":\"The Sirens of Titan\",\"author\":\"Kurt Vonnegut\"},{\"title\":\"Alas, Babylon\",\"author\":\"Pat Frank\"},{\"title\":\"A Canticle for Leibowitz\",\"author\":\"Walter M. Miller\"},{\"title\":\"Venus Plus X\",\"author\":\"Theodore Sturgeon\"},{\"title\":\"Solaris\",\"author\":\"Stanislaw Lem\"},{\"title\":\"The Drowned World\",\"author\":\"J.G. Ballard\"},{\"title\":\"Hothouse\",\"author\":\"Brian Aldiss\"},{\"title\":\"A Wrinkle in Time\",\"author\":\"Madeleine L'Engle\"},{\"title\":\"Dune\",\"author\":\"Frank Herbert\"},{\"title\":\"Make Room! Make Room!\",\"author\":\"Harry Harrison\"},{\"title\":\"Logan's Run\",\"author\":\"William F. Nolan &lt; George Clayton Johnson\"},{\"title\":\"Do Androids Dream of Electric Sheep?\",\"author\":\"Philip K. Dick\"},{\"title\":\"The Left Hand of Darkness\",\"author\":\"Ursula K. Le Guin\"},{\"title\":\"Behold the Man\",\"author\":\"Michael Moorcock\"},{\"title\":\"Ringworld\",\"author\":\"Larry Niven\"},{\"title\":\"Rendezvous with Rama\",\"author\":\"Arthur C. Clarke\"},{\"title\":\"Roadside Picnic / Tale of the Troika\",\"author\":\"Boris &lt; Arkady Strugatsky \"},{\"title\":\"The Female Man\",\"author\":\"Joanna Russ\"},{\"title\":\"Man Plus\",\"author\":\"Frederik Pohl\"},{\"title\":\"The Stand\",\"author\":\"Stephen King\"},{\"title\":\"Nor Crystal Tears\",\"author\":\"Alan Dean Foster\"}]"
            val listType = object : TypeToken<List<Book>>() {}.type
            return GsonBuilder().create().fromJson<List<Book>>(json, listType)
        }
}

