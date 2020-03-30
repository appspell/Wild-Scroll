DEPRECATED


# wild-scroll
Android RecyclerView Fast Scroller + Section bar

![alt tag](https://i.imgur.com/sSCEVRf.gif)
![alt tag](https://i.imgur.com/TNhTMZD.gif)
![alt tag](https://i.imgur.com/26QFKMu.gif)

Download
--------

Grab via Maven:
```xml
<dependency>
  <groupId>com.appspell.wild-scroll</groupId>
  <artifactId>wild-scroll</artifactId>
  <version>0.90</version>
  <type>pom</type>
</dependency>
```
or Gradle:
```groovy
implementation 'com.appspell.wild-scroll:wild-scroll:0.90'
```


Minimal working example
-----------------------

In your [layout](/app/src/main/res/layout/fragment_simple.xml) file:
```xml
<com.appspell.wildscroll.view.WildScrollRecyclerView
        android:id="@+id/list"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:listitem="@layout/item_sample_list" />
```

In your [code](/app/src/main/java/com/appspell/wildscroll/fragments/SimpleFragment.kt) (Actvity/Fragment):
```Kotlin
val recyclerView = view.findViewById<RecyclerView>(R.id.list)
with(recyclerView) {
   layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
   adapter = SampleAdapter()
   (adapter as SampleAdapter).items = DataSource(resources).companies
}
```

One important thing that your need to implement `SectionFastScroll` for your [`RecyclerView.Adapter`](/app/src/main/java/com/appspell/wildscroll/adapter/SimpleAdapter.kt)
```Kotlin
class SampleAdapter : RecyclerView.Adapter<ViewHolder>(), SectionFastScroll {
   override fun getSectionName(position: Int): String = items[position].section 
}
```


Set all available attributes in [XML](/app/src/main/res/layout/fragment_custom.xml)
-----------------------------------

```xml
<com.appspell.wildscroll.view.WildScrollRecyclerView
        android:id="@+id/list"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:wildScroll_textColor="@color/textSecondary"
        app:wildScroll_textSize="@dimen/textRegular"
        app:wildScroll_highlightColor="@color/colorSecondary"
        app:wildScroll_highlightTextSize="@dimen/textHighlighted"
        app:wildScroll_sectionBarBackgroundColor="@color/divider"
        app:wildScroll_sectionBarPaddingLeft="@dimen/padding"
        app:wildScroll_sectionBarPaddingRight="@dimen/padding"
        app:wildScroll_sectionBarCollapseDigital="true"
        app:wildScroll_sectionBarGravity="right"
        app:wildScroll_sectionBarEnable="true"
        app:wildScroll_popupEnable="true"
        app:wildScroll_popupBackgroundColor="@color/colorSecondary"
        app:wildScroll_popupPadding="@dimen/padding"
        app:wildScroll_popupTextColor="@color/colorSecondaryLight"
        app:wildScroll_popupTextSize="@dimen/textPopup"
        tools:listitem="@layout/item_sample_list" />
```


Configure in [code](/app/src/main/java/com/appspell/wildscroll/fragments/CustomProgrammaticallyFragment.kt)
-----------------

```Kotlin
        val recyclerView = view.findViewById<WildScrollRecyclerView>(R.id.list)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = SampleSecondAdapter()
            (adapter as SampleSecondAdapter).items = DataSource(resources).companies

            textColor = R.color.colorSecondaryDark
            textSize = R.dimen.textRegular
            textTypeFace = Typeface.SANS_SERIF

            highlightColor = R.color.colorSecondary
            highlightTextSize = R.dimen.fastscroll_section_highlight_text_size
            highlightTextFace = Typeface.DEFAULT_BOLD

            sectionBarPaddingLeft = R.dimen.padding
            sectionBarPaddingRight = R.dimen.padding
            sectionBarCollapseDigital = false
            sectionBarGravity = LEFT

            sectionPopup = SectionLetterPopup(context,
                    textColorRes = R.color.colorSecondaryLight,
                    textSizeDimen = R.dimen.textPopup,
                    textTypeFace = Typeface.DEFAULT_BOLD,
                    backgroundResource = R.drawable.background_popup,
                    paddingRes = R.dimen.padding)
        }
```


Section popups
--------------

You can set your custom popup or use one of included into the library ones.

[`SectionLetterPopup`](/wildscrolllibrary/src/main/java/com/appspell/wildscroll/sections/popup/SectionLetterPopup.kt) - This popup shows section name and can use custom background (`backgroundResource`).

[`SectionCirclePopup`](/wildscrolllibrary/src/main/java/com/appspell/wildscroll/sections/popup/SectionLetterPopup.kt) - Extends `SectionLetterPopup` but use predefined round background.

[`StubSectionPopup`](/wildscrolllibrary/src/main/java/com/appspell/wildscroll/sections/popup/SectionPopup.kt) - It's just a stub. This popup can't be rendered.

If you want to implement your own section popup you should use interface [`com.appspell.wildscroll.sections.popup.SectionPopup`](/wildscrolllibrary/src/main/java/com/appspell/wildscroll/sections/popup/SectionPopup.kt).


XML attributes
--------------


Section bar

`wildScroll_sectionBarEnable` - show or hide section bar

`wildScroll_sectionBarGravity` - section bar positon. (currently available: left and right)

`wildScroll_textColor` - section text color

`wildScroll_textSize` - section text size

`wildScroll_highlightColor` - color for selected section

`wildScroll_highlightTextSize` - text size for selected section

`wildScroll_sectionBarBackgroundColor` - background color for section bar

`wildScroll_sectionBarPaddingLeft` - left padding for section bar

`wildScroll_sectionBarPaddingRight` - right padding for section bar

`wildScroll_sectionBarCollapseDigital` - merge all of numeric sections into one (1..10 -> #)


Popup 

`wildScroll_popupEnable` - show or hide section popup

`wildScroll_popupBackgroundDrawable` - popup background

`wildScroll_popupBackgroundColor` - color for preselected background drawable (like *tint*)

`wildScroll_popupPadding` - paddings for popup

`wildScroll_popupTextColor` - section text color

`wildScroll_popupTextSize` - section text size
