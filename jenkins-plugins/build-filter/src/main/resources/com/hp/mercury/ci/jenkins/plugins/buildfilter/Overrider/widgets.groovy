def l   = namespace(lib.LayoutTagLib)
def st  = namespace("jelly:stapler")


//note, my.widgets access the getWidgets method of the Overrider class, which is why we reference the BranchAwareBuildHistoryWidget
my.widgets.each { widget ->
    st.include(it:widget, page:"index")
}